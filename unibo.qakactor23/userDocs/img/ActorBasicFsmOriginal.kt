package it.unibo.kactor

import alice.tuprolog.*
import kotlinx.coroutines.*
import java.util.NoSuchElementException

/*
================================================================
 STATE
================================================================
 */
class State(val stateName : String, val scope: CoroutineScope ) {
    private val edgeList          = mutableListOf<Transition>()
    private val stateEnterAction  = mutableListOf< suspend (State) -> Unit>()
    private val myself : State    = this

    fun transition(edgeName: String, targetState: String, cond: Transition.() -> Unit) {
        val trans = Transition(edgeName, targetState)
        //println("      trans $name $targetState")
        trans.cond() //set eventHandler (given by user) See fireIf
        edgeList.add(trans)
    }
    //Add an action which will be called when the state is entered
    fun action(  a:  suspend (State) -> Unit) {
        //println("State $stateName    | add action  $a")
        stateEnterAction.add( a )
    }
    /*
    fun addAction (action:  suspend (State) -> Unit) {
        stateEnterAction.add(action)
    }
    */
    suspend fun enterState() {
        val myself = this
        scope.launch {
            //println(" --- | State $stateName    | enterState ${myself.stateName} ")
            stateEnterAction.get(0)( myself )
        }.join()
        //println(" --- | State $stateName    | enterState DONE ")
        //stateEnterAction.forEach {  it(this) }
    }

    //Get the appropriate Edge for the Message
    fun getTransitionForMessage(msg: ApplMessage): Transition? {
        //println("State $name       | getTransitionForMessage  $msg  list=${edgeList.size} ")
        val first = edgeList.firstOrNull { it.canHandleMessage(msg) }
        return first
    }
}

/*
================================================================
 Transition
================================================================
 */
class Transition(val edgeName: String, val targetState: String) {

    lateinit var edgeEventHandler: ( ApplMessage ) -> Boolean  //MsgId previous: String
    private val actionList = mutableListOf<(Transition) -> Unit>()

    fun action(action: (Transition) -> Unit) { //MEALY?
        //println("Transition  | add ACTION:  $action")
        actionList.add(action)
    }

    //Invoke when you go down the transition to another state
    fun enterTransition(retrieveState: (String) -> State): State {
        //println("Transition  | enterEdge  retrieveState: ${retrieveState} actionList=$actionList")
        actionList.forEach { it(this) }         //MEALY?
        return retrieveState(targetState)
    }

    fun canHandleMessage(msg: ApplMessage): Boolean {
        //println("Transition  | canHandleMessage: ${msg}  ${msg is Message.Event}" )
        return edgeEventHandler( msg  ) //msg.msgId()
    }
}

/*
================================================================
 ActorBasicFsm
================================================================
 */
abstract class ActorBasicFsm(  qafsmname:  String,
                      fsmscope: CoroutineScope = GlobalScope,
					  discardMessages : Boolean = false,
                      confined :    Boolean = false,
                      ioBound :     Boolean = false,
                      channelSize : Int = 50
                    ): ActorBasic(  qafsmname, fsmscope, discardMessages, confined, ioBound, channelSize ) {

    val autoStartMsg = MsgUtil.buildDispatch(name, "autoStartSysMsg", "start", name)
	
    private var isStarted = false
    protected var myself : ActorBasicFsm
    protected lateinit var currentState: State
    protected var currentMsg       = NoMsg
    protected var msgToReply       = NoMsg
    lateinit protected var mybody: ActorBasicFsm.() -> Unit
    var stateTimer : TimerActor?   = null

    private val stateList = mutableListOf<State>()
    private val msgQueueStore = mutableListOf<ApplMessage>()

    //================================== STRUCTURAL =======================================
    fun state(stateName: String, build: State.() -> Unit) {
        val state = State(stateName, scope)
        state.build()
        stateList.add(state)
    }

    private fun getStateByName(name: String): State {
        return stateList.firstOrNull { it.stateName == name }
            ?: throw NoSuchElementException(name)
    }
    //===========================================================================================

    init {
        //println("ActorBasicFsm INIT")private val
        myself  = this
        setBody(getBody(), getInitialState())
    }

    abstract fun getBody(): (ActorBasicFsm.() -> Unit)
    abstract fun getInitialState(): String

@kotlinx.coroutines.ObsoleteCoroutinesApi

    fun setBody( buildbody: ActorBasicFsm.() -> Unit, initialStateName: String ) {
        buildbody()            //Build the structural part
        currentState = getStateByName(initialStateName)
        //println("ActorBasicFsm $name |  initialize currentState=${currentState.stateName}")
        scope.launch { autoMsg(autoStartMsg) }  //auto-start
    }

@kotlinx.coroutines.ObsoleteCoroutinesApi

    override suspend fun actorBody(msg: ApplMessage) {
        //println(" --- | ActorBasicFsm $name | msg=$msg")
        if (msg.msgId() == autoStartMsg.msgId() && !isStarted) {
            //scope.launch{ fsmwork() } //The actot must continue to receive msgs
            fsmStartWork()
            //println("ActorBasicFsm $name | BACK TO MAIN ACTOR AFTER INIT")
        } else {
            fsmwork(msg)
            //println("ActorBasicFsm $name | BACK TO MAIN ACTOR")
        }
    }

    suspend fun fsmStartWork() {
        isStarted = true
        //println("ActorBasicFsm $name | fsmStartWork in STATE ${currentState.stateName}")
        currentState.enterState()
        checkDoEmptyMove()
    }

@kotlinx.coroutines.ObsoleteCoroutinesApi

//APR2020:
    suspend fun fsmwork(msg: ApplMessage) {
        //sysUtil.traceprintln("$tt ActorBasicFsm $name | fsmwork in ${currentState.stateName} $msg")
        var nextState = checkTransition(msg)
        var b         = handleCurrentMessage(msg, nextState)
        if (b) { 
            currentState.enterState() //execute local actions (Moore automaton)
            val nextState1 = lookAtMsgQueueStore()
			if( nextState1 is State ) fsmwork(currentMsg) //handle previous message
			else checkDoEmptyMoveInState()	 
        }
        //sysUtil.traceprintln("$tt ActorBasicFsm $name | fsmwork ENDS for $msg")
   }

    suspend fun checkDoEmptyMove() { //used by fsmStartWork
        var nextState = checkTransition(NoMsg) //EMPTY MOVE
        while (nextState is State) {
            currentMsg = NoMsg
            currentState = nextState
            currentState.enterState()
            nextState = checkTransition(NoMsg) //EMPTY MOVE
        }
    }
@kotlinx.coroutines.ObsoleteCoroutinesApi

    suspend fun checkDoEmptyMoveInState() {
		sysUtil.traceprintln("$tt ActorBasicFsm $name | checkDoEmptyMoveInState msgQueueStoreSize=:${msgQueueStore.size}")
        var nextState = checkTransition(NoMsg) //EMPTY MOVE
        if (nextState is State) {
            currentMsg   = NoMsg
            currentState = nextState
            currentState.enterState()
            val nextState1 = lookAtMsgQueueStore(emptyMove=true)
			if( nextState1 is State ) fsmwork(currentMsg)			 
        }
    }
	
    fun handleCurrentMessage(msg: ApplMessage, nextState: State?, memo: Boolean = true): Boolean {
        sysUtil.traceprintln("$tt ActorBasicFsm $name | handleCurrentMessage in ${currentState.stateName} msg=${msg.msgId()}")
        if (nextState is State) {
            currentMsg   = msg
            if( currentMsg.isRequest() ){ requestMap.put(currentMsg.msgId(), currentMsg) }  //Request
            var msgBody = currentMsg.msgContent()
            val endTheTimer = currentMsg.msgId() != "local_noMsg" &&
                            ( ! msgBody.startsWith("local_tout_")
                                    ||
                                //msgBody.startsWith("local_tout_") &&
                                    ( msgBody.contains(currentState.stateName) &&
                                      msgBody.contains(this.name) )
                            )
            currentState = nextState

            //sysUtil.traceprintln("               %%% ActorBasicFsm $name | handleCurrentMessage currentState= ${currentState.stateName}  ")
            //sysUtil.traceprintln("               %%% ActorBasicFsm $name | handleCurrentMessage currentMsg= $currentMsg  ")
            /*
            if( currentMsg.msgId() != "local_noMsg" &&
                ! currentMsg.msgContent().startsWith("local_tout_")  &&
                currentMsg.msgContent().contains(currentState.stateName)
                && (stateTimer !== null) ) {
            */
            if( endTheTimer && (stateTimer !== null) ){
                stateTimer!!.endTimer() //terminate TimerActor
                //stateTimer = null
            }

            return true
        } else { //EXCLUDE EVENTS FROM msgQueueStore
            if (!memo) return false
            if (!(msg.isEvent()) && ! discardMessages) {
                msgQueueStore.add(msg)
                println("		$tt ActorBasicFsm $name |  added $msg in msgQueueStore")
            }
            //else sysUtil.traceprintln("$tt ActorBasicFsm $name | DISCARDING THE EVENT: ${msg.msgId()}")
            return false
        }
    }


    private fun lookAtMsgQueueStore(emptyMove : Boolean = false ): State? {
        sysUtil.traceprintln("$tt ActorBasicFsm $name | lookAtMsgQueueStore :${msgQueueStore.size}")
        msgQueueStore.forEach {
            val state = checkTransition(it)
            if (state is State) {
				sysUtil.traceprintln("$tt ActorBasicFsm $name | lookAtMsgQueueStore state=${state.stateName},curState=${currentState.stateName} ")
				if( ! emptyMove && state != currentState ){
					println(" AAAAAAAAAAAAAAA $tt ActorBasicFsm $name | lookAtMsgQueueStore state=${state.stateName},curState=${currentState.stateName} ")
					return null
				} 
                currentMsg = msgQueueStore.get( msgQueueStore.indexOf(it) )
                //sysUtil.traceprintln("$tt ActorBasicFsm $name | lookAtMsgQueueStore FOUND $currentMsg state=${state.stateName}")
                msgQueueStore.remove(it)
                return state
            }
        }
        return null
    }

    private fun checkTransition(msg: ApplMessage): State? {
        val trans = currentState.getTransitionForMessage(msg)
        sysUtil.traceprintln("$tt ActorBasicFsm $name | checkTransition, $msg, curState=${currentState.stateName}, trans=$trans")
        return if (trans != null) {
            trans.enterTransition { getStateByName(it) }
        } else {
            //sysUtil.traceprintln("$tt ActorBasicFsm $name | checkTransition in ${currentState.stateName} NO next State for $msg !!!")
            null
        }
    }

    fun doswitch(): Transition.() -> Unit {
        return { edgeEventHandler = { true } }
    }
    fun doswitchGuarded( guard:()->Boolean ): Transition.() -> Unit {
        return { edgeEventHandler = { guard() } }
    }


	
    fun whenEvent(evName: String): Transition.() -> Unit {
        return {
            edgeEventHandler = {
                //println("whenEvent $it - $evName");
                it.isEvent() && it.msgId() == evName }
                //it == evName } //it.isEvent() && it.msgId() == evName }
        }
    }
    fun whenEventGuarded(evName: String, guard:()->Boolean ): Transition.() -> Unit {
        return {
            edgeEventHandler = {
                //println("whenEventGuarded $it - $evName");
                it.isEvent() && it.msgId() == evName && guard() }
                //it == evName && guard()  } //it.isEvent() && it.msgId() == evName }
        }
    }

    fun whenDispatch(msgName: String ): Transition.() -> Unit {
			//println("$tt ActorBasicFsm $name | whenDispatch  planning $msgName  " )
            return {
                edgeEventHandler = {
                    //println("ActorBasicFsm $name | ${currentState.stateName} whenDispatch $it  $msgName  ")
                    it.isDispatch() && it.msgId() == msgName  }
                    //it == msgName }  //it.isDispatch() && it.msgId() == msgName }
            } 
    }
    fun whenDispatchGuarded(msgName: String, guard:()->Boolean ): Transition.() -> Unit {
        return {
            edgeEventHandler = {
                //println("whenDispatchGuarded $it - $evName");
                it.isDispatch() && it.msgId() == msgName && guard()  }
                //it == msgName && guard() } //it.isDispatch() && it.msgId() == msgName }
        }
    }

    fun whenRequest(msgName: String): Transition.() -> Unit {
        //sysUtil.traceprintln("$tt ActorBasicFsm $name | whenRequest $currentMsg" )
        return {
            edgeEventHandler = {
                //sysUtil.traceprintln("$tt ActorBasicFsm $name | ${currentState.stateName} whenRequest $it  $msgName")
                it.isRequest() && it.msgId() == msgName }
                //it == msgName   }  //it.isRequest() && it.msgId() == msgName }
        }
    }
    fun whenRequestGuarded(msgName: String, guard:()->Boolean): Transition.() -> Unit {
        return {
            edgeEventHandler = {
                //sysUtil.traceprintln("$tt ActorBasicFsm $name | ${currentState.stateName} whenRequestGuarded $it, $msgName")
                it.isRequest() && it.msgId() == msgName && guard()  }
                //it == msgName   && guard() }  //it.isRequest() && it.msgId() == msgName }
        }
    }
    fun whenReply(msgName: String): Transition.() -> Unit {
        return {
            edgeEventHandler = {
                //sysUtil.traceprintln("$tt ActorBasicFsm $name | ${currentState.stateName} whenReply $it  $msgName")
                it.isReply() && it.msgId() == msgName }
                //it == msgName   }  //it.isReply() && it.msgId() == msgName }
        }
    }
    fun whenReplyGuarded(msgName: String, guard:()->Boolean): Transition.() -> Unit {
        return {
            edgeEventHandler = {
                //sysUtil.traceprintln("$tt ActorBasicFsm $name | ${currentState.stateName} whenReplyGuarded $it  $msgName")
                it.isReply() && it.msgId() == msgName && guard() }
                //it == msgName  && guard() }  //it.isReply() && it.msgId() == msgName }
        }
    }

    fun whenTimeout( timerEventName : String ): Transition.() -> Unit {
                return {
                    edgeEventHandler = {
                        //println("whenTimeoutt $it")
                        it.isEvent() && it.msgId() == timerEventName
                        //it == timerEventName
                    } //it.isEvent() && it.msgId() == timerEventName }
                }
    }

/*
    fun ignoreCurrentCaller() {
        currentMsg = NoMsg
    }
*/

    fun storeCurrentMessageForReply() {
        msgToReply = currentMsg
        //println(getName() + " 			msgToReply:" +msgToReply );
    }


@kotlinx.coroutines.ObsoleteCoroutinesApi

    suspend fun replyToCaller(msgId: String, msg: String) {
        //sysUtil.traceprintln("$tt ActorBasicFsm $name | replyToCaller msgToReply=" + msgToReply);
        val caller = msgToReply.msgSender()
        //println( " replyToCaller  $msgId : $msg  to $caller" );
        forward(msgId, msg, caller)
    }
 /*
    suspend fun replyToCaller(msgId: String, msg: String) {
        if (currentMsg != null) {
            val caller = currentMsg.msgSender()
            println( " replyToCaller  $msgId : $msg  to $caller" );
            forward(msgId, caller, msg)
        }
        else if (msgToReply != NoMsg) { //Aug4
            println( " replyToCaller msgToReply=" + msgToReply);
            currentMsg = msgToReply
            replyToCaller(msgId, msg)
            currentMsg = NoMsg
        }
    }
*/
/*
UTILITIES TO HANDLE MSG CONTENT
 */
    private var msgArgList = mutableListOf<String>()

	//called by onMsg translated
    fun checkMsgContent(template : Term, curT : Term,  content : String ) : Boolean{
        msgArgList = mutableListOf<String>()
        if( pengine.unify(curT, template ) && pengine.unify(curT, Term.createTerm(content) ) ){
            val tt   = Term.createTerm( curT.toString() )  as Struct
            val ttar = tt.arity
            for( i in 0..ttar-1 ) msgArgList.add( tt.getArg(i).toString().replace("'","") )
            return true
        }
        return false
    }
    fun  payloadArg( n : Int  ) : String{
         return msgArgList.elementAt(n)
    }

}