package it.unibo.kactor

import alice.tuprolog.*
import kotlinx.coroutines.*
import unibo.comm22.utils.ColorsOut
import java.util.NoSuchElementException

/*
================================================================
 STATE
================================================================
 */
class State(val stateName : String, val scope: CoroutineScope ) {
    private var edgeList          = mutableListOf<Transition>()
    private val stateEnterAction  = mutableListOf< suspend (State) -> Unit>()
    private val myself : State    = this

    fun transition(edgeName: String, targetState: String, cond: Transition.() -> Unit ) {
        val trans = Transition(edgeName, targetState, null)
        //println("      trans $name $targetState")
        trans.cond() //set eventHandler (given by user) See fireIf
        edgeList.add(trans)
    }
    //Aug2022
    //Called by a state as a transition
    fun interrupthandle(edgeName: String, targetState: String, cond: Transition.() -> Unit, storage: MutableList<Transition> ) {
        val trans = Transition(edgeName, targetState, edgeList)
        //println("      trans $name $targetState")
        trans.cond() //set eventHandler (given by user) See fireIf
        edgeList.add(trans)
        //AUG2022: ottengo la lista delle transizioni dello stato (con interrupt)
        //L'operazione resume deve ripristinare questa lista nello stato che gestisce l'interrupt
        //invocando storeTransitionsOfStateInterrupted
        /*
        if( storage.size > 0 ) {                     //The storage must be empty
             System.exit(1)
        }*/
        edgeList.forEach{ storage.add(it) }  //Ricopio
        //println("&&&& ${stateName} interrupthandle ${storage.size}")
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
    fun getTransitionForMessage(msg: IApplMessage): Transition? {
        //println("State $name       | getTransitionForMessage  $msg  list=${edgeList.size} ")
        val first = edgeList.firstOrNull { it.canHandleMessage(msg) }
        return first
    }

    fun storeTransitionsOfStateInterrupted( t: MutableList<Transition> ) {
        //println("&&&&  $stateName storeTransitionsOfStateInterrupted ${t}")
        edgeList  = mutableListOf<Transition>()   //clear
        t.forEach { edgeList.add(it) }   //Ricopio
        //t.clear()                      //NOOO: Reset the storage (sideeffect)
     }
}

/*
================================================================
 Transition
================================================================
 */
class Transition(val edgeName: String, val targetState: String,
                 val globalEdges: MutableList<Transition>? ) {  //Aug2022

    lateinit var edgeEventHandler: ( IApplMessage ) -> Boolean  //MsgId previous: String
    private val actionList       = mutableListOf<(Transition) -> Unit>()
    //private val globalActionList = mutableListOf<Transition>()

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

    fun canHandleMessage(msg: IApplMessage): Boolean {
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
    protected lateinit var currentState: State	//inherited
    protected var currentMsg : IApplMessage      = NoMsg
    protected var msgToReply       = NoMsg
    lateinit protected var mybody: ActorBasicFsm.() -> Unit
    var stateTimer : TimerActor?   = null

    private val stateList = mutableListOf<State>()
    private val msgQueueStore = mutableListOf<IApplMessage>()
 
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



    fun setBody( buildbody: ActorBasicFsm.() -> Unit, initialStateName: String ) {
        buildbody()            //Build the structural part
        currentState = getStateByName(initialStateName)
        //println("ActorBasicFsm $name |  initialize currentState=${currentState.stateName}")
        scope.launch { autoMsg(autoStartMsg) }  //auto-start
    }

	//Now there is a state ....
 	override  fun writeMsgLog( msg: IApplMessage ){ //APR2020
		//Update the log of the actor
         sysUtil.updateLogfile(actorLogfileName,  "item($name,${currentState.stateName},$msg).", dir=msgLogDir )  
		//Update the log of the context
         if( context !== null )
			 sysUtil.updateLogfile(context!!.ctxLogfileName, "item($name,${currentState.stateName},$msg).", dir=msgLogNoCtxDir )		
	}
	


    override suspend fun actorBody(msg: IApplMessage) {
         if ( !isStarted && msg.msgId() == autoStartMsg.msgId() ) fsmStartWork( msg )
         else  fsmwork(msg)
    }


    suspend fun fsmStartWork( msg: IApplMessage ) {
        isStarted = true
        //println("ActorBasicFsm $name | fsmStartWork in STATE ${currentState.stateName}")
        currentMsg = msg
        currentState.enterState()
        var nextState = checkTransition(NoMsg) //check FIRST EMPTY MOVE
        while (nextState is State) {
            currentMsg   = NoMsg
            currentState = nextState
            currentState.enterState()
			nextState = checkTransition(NoMsg) //check other EMPTY MOVE
         }
          sysUtil.traceprintln("$tt ActorBasicFsm $name | fsmStartWork ENDS ")
     }



     suspend fun fsmwork(msg: IApplMessage) {
        //sysUtil.traceprintln("$tt ActorBasicFsm $name | fsmwork in ${currentState.stateName} $msg")
        var nextState = checkTransition(msg)
        var b         = handleCurrentMessage(msg, nextState)
        if (b)  elabMsgInState( )
        sysUtil.traceprintln("$tt ActorBasicFsm $name | fsmwork ENDS for $msg")
   }

    //Aug2022
    fun returnFromInterrupt( t: MutableList<Transition> ) {
        //println("&&&&  ${currentState.stateName} returnFromInterrupt ${t.size}")
        currentState.storeTransitionsOfStateInterrupted( t )
    }


    suspend fun elabMsgInState( ) {
        sysUtil.traceprintln("$tt ActorBasicFsm $name | elabMsgInState in ${currentState.stateName} $currentMsg")
    	currentState.enterState() //execute local actions (Moore automaton)
	    checkEmptyMove() 
    	if( elabMsgQueueStore() ) elabMsgInState()
    }
	 


    suspend fun checkEmptyMove() {
		//sysUtil.traceprintln("$tt ActorBasicFsm $name | checkDoEmptyMoveInState msgQueueStoreSize=:${msgQueueStore.size}")
        var nextState = checkTransition(NoMsg) //EMPTY MOVE
        if (nextState is State) {
            currentMsg   = NoMsg
			currentState = nextState
			elabMsgInState( )
        }
    }

     fun handleCurrentMessage(msg: IApplMessage, nextState: State?, memo: Boolean = true): Boolean {
        sysUtil.traceprintln("$tt ActorBasicFsmmmm $name | handleCurrentMessage in ${currentState.stateName} msg=${msg.msgId()} memo=$memo")
        if (nextState is State) {
            currentMsg   = msg
            if( currentMsg.isRequest() ){
				//Dec2021: currentMsg.msgId() + currentMsg.msgReceiver() MEGLIO !
				//Chi fa requestMap.remove? Il metodo answer in ActorBasic
				requestMap.put(currentMsg.msgId(), currentMsg) }  //Request
            var msgBody = currentMsg.msgContent()
            val endTheTimer = currentMsg.msgId() != "local_noMsg" &&
                            ( ! msgBody.startsWith("local_tout_")
                                    ||
                                //msgBody.startsWith("local_tout_") &&
                                    ( msgBody.contains(currentState.stateName) &&
                                      msgBody.contains(this.name) )
                            )
            currentState = nextState

            if( endTheTimer && (stateTimer !== null) ){
                stateTimer!!.endTimer() //terminate TimerActor
             }
            return true
        } else { //No nextstate => EXCLUDE EVENTS FROM msgQueueStore
            if (!memo) return false   //
            if (!(msg.isEvent()) && ! discardMessages) {
                msgQueueStore.add(msg)
                println("		$tt ActorBasicFsm $name |  added $msg in msgQueueStore")
            }
            else {
				//sysUtil.traceprintln("$tt ActorBasicFsm $name | DISCARDING : ${msg.msgId()}")
				sysUtil.updateLogfile(actorLogfileName,  "discard($name,${currentState.stateName},$msg).", dir=msgLogDir) 
			}
			return false
        }
    }

	


	suspend private fun elabMsgQueueStore(  ) : Boolean {
        msgQueueStore.forEach {
           val state = checkTransition(it)
           if (state is State) {				
        	    sysUtil.traceprintln("$tt ActorBasicFsm $name | elabMsgQueueStore state=${state.stateName},curState=${currentState.stateName},it=${it}  ")
                currentMsg = msgQueueStore.get( msgQueueStore.indexOf(it) )
                msgQueueStore.remove(it)
				var b = handleCurrentMessage(currentMsg, state)	//sets currentState
				//sysUtil.traceprintln("$tt ActorBasicFsm $name | elabMsgQueueStore state=${state.stateName},curState=${currentState.stateName}, currentMsg=$currentMsg ")
				if( b ) return true		 
           }
		}
        return false
	}

    private fun checkTransition(msg: IApplMessage): State? {
        val trans = currentState.getTransitionForMessage(msg)
        //sysUtil.traceprintln("$tt ActorBasicFsm $name | checkTransition, $msg, curState=${currentState.stateName}, trans=$trans")
        return if (trans != null) {
             trans.enterTransition {
                  getStateByName(it)
             }
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



    fun storeCurrentMessageForReply() {
        msgToReply = currentMsg
        //println(getName() + " 			msgToReply:" +msgToReply );
    }




    suspend fun replyToCaller(msgId: String, msg: String) {
        //sysUtil.traceprintln("$tt ActorBasicFsm $name | replyToCaller msgToReply=" + msgToReply);
        val caller = msgToReply.msgSender()
        //println( " replyToCaller  $msgId : $msg  to $caller" );
        forward(msgId, msg, caller)
    }
	
/*
 -------------------------------------------------------------------
UTILITIES TO HANDLE MSG CONTENT
 -------------------------------------------------------------------
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