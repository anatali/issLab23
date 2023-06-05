package it.unibo.kactor
import alice.tuprolog.Prolog
import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Theory
import  unibo.basicomm23.interfaces.Interaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import unibo.basicomm23.utils.ColorsOut
import unibo.basicomm23.utils.CommUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


/*
ECLIPSE KOTLIN
https://dl.bintray.com/jetbrains/kotlin/eclipse-plugin/last/
*/

//A module in kotlin is a set of Kotlin files compiled together
object sysUtil{
	private val pengine = Prolog()
    internal val dispatchMap :  MutableMap<String,Struct> = mutableMapOf<String,Struct>()
	internal val ctxsMap :      MutableMap<String, QakContext> = mutableMapOf<String, QakContext>()
	internal val ctxActorMap :  MutableMap<String, ActorBasic> = mutableMapOf<String, ActorBasic>()
	val ctxOnHost =  mutableListOf<QakContext>()

	val runtimeEnvironment     = Runtime.getRuntime()
	val userDirectory          = System.getProperty("user.dir")
	val cpus                   = Runtime.getRuntime().availableProcessors()
	


	val singleThreadContext    = newSingleThreadContext("qaksingle")
	val ioBoundThreadContext   = newFixedThreadPoolContext(64, "qakiopool")
	val cpusThreadContext      = newFixedThreadPoolContext(cpus, "qakcpuspool")

	var mqttBrokerIP : String? = ""
	var mqttBrokerPort : String? = ""
	var mqttBrokerEventTopic : String? = ""
	var trace   : Boolean = false
	var logMsgs : Boolean = false
	val connActive : MutableSet<Interaction> = mutableSetOf<Interaction>()    //Oct2019

	
@JvmStatic    	fun getMqttEventTopic() : String {
		if(mqttBrokerEventTopic !== null ) return mqttBrokerEventTopic!!
		else return "unibo/qak/events"
	}

@JvmStatic    	fun getPrologEngine() : Prolog = pengine
@JvmStatic    	fun curThread() : String = "thread=${Thread.currentThread().name}"

@JvmStatic    	fun getContext( ctxName : String ) : QakContext?  { return ctxsMap.get(ctxName.toLowerCase())}
@JvmStatic    	fun getActor( actorName : String ) : ActorBasic? {  return ctxActorMap.get(actorName.toLowerCase())}

@JvmStatic    	fun getActorContextName( actorName : String): String?{
		val ctxName = solve( "qactor($actorName,CTX,_)", "CTX" )
		return ctxName
	}
	fun setActorContextName( actorName : String, ctxName : String ) {
		//if does not exists ...
		val res = solve( "qactor($actorName,$ctxName,_)", "" )
		if( res == "success") println("$actorName already set in $ctxName")
		else solve( "assertz( qactor($actorName,$ctxName,_) )", "" )
	}
@JvmStatic    	fun getActorContext ( actorName : String): QakContext?{
		val ctxName = solve( "qactor($actorName,CTX,_)", "CTX" )
		//println("               %%% sysUtil |  getActorContext ctxName=${ctxName} - ${ctxsMap.get( ctxName )}")
		return ctxsMap.get( ctxName )
	}
	@JvmStatic 	fun createContexts(  hostName : String,
					desrFilePath:String, rulesFilePath:String, localContextName: String? = null){
		//sysUtil.aboutThreads("sysUtil createContexts  localContextName=$localContextName host=$hostName")

		loadTheory( desrFilePath )
		loadTheory( rulesFilePath )
		if( solve("tracing", "" ).equals("success") ) trace=true
		if( solve("msglogging", "" ).equals("success") ) logMsgs=true
		try {
			mqttBrokerIP         = solve("mqttBroker(IP,_,_)", "IP" )
			mqttBrokerPort       = solve("mqttBroker(_,PORT,_)", "PORT")
			mqttBrokerEventTopic = solve("mqttBroker(_,_,EVTOPIC)", "EVTOPIC")
		}catch(e: Exception){
			println("               %%% sysUtil | NO MQTT borker FOUND")
		}
        //Create the messages
        try {
            val dispatcNames      = solve("getDispatchIds(D)", "D")
            val dispatchNamesList = strRepToList(dispatcNames!!)
            dispatchNamesList.forEach { d -> createDispatch(d) }
        }catch( e : Exception){ //println("               %%% sysUtil | NO DISPATCH FOUND")
		}
        //Create the contexts
		//println("               %%% sysUtil | getCtxNames( X )" )
		val ctxs: String? = solve("getCtxNames( X )", "X")
			//context( CTX, HOST, PROTOCOL, PORT )
			val ctxsList = strRepToList(ctxs!!)
			//waits for all the other context before activating the actors
		sysUtil.aboutThreads("sysUtil createContexts  ctxsList=$ctxsList  ")
		if(localContextName==null) { //before Giannatempo
			ctxsList.forEach { ctx -> createTheContext(ctx, hostName = hostName) }//foreach ctx
			addProxyToOtherCtxs(ctxsList, hostName = hostName)  //here could wait in polling ...
		}else {
			//CommUtils.outblue("sysUtil createContexts $localContextName")
			ctxsList.forEach { ctx ->  //NOV22
				val ctxhostName = solve("context( CTX, HOST, PROTOCOL, PORT )".replace("CTX",ctx), "HOST")
				createTheContext(ctx, hostName = ctxhostName!!, localContextName) }//foreach ctx
			addProxyToOtherCtxs(ctxsList, hostName = hostName, localContextName)  //here could wait in polling ...
		}
		//APR2020: removed, since we use CoAP e no more TCP
 			//addProxyToOtherCtxs(ctxsList, hostName = hostName)  //xxx here could wait in polling ...
	}//createContexts

    fun createDispatch(  d : String )  {
         val dd  = solve("dispatch($d,C)", "C")
         val dt = ( Term.createTerm("dispatch($d,$dd)") )
         //println("sysUtil | dispatch $dt  ")
         dispatchMap.put( d, dt as Struct )
     }


	@JvmStatic fun createTheContext(  ctx : String, hostName : String  ) : QakContext?{
		CommUtils.outgreen("sysUtil createTheContext  ctx=$ctx host=$hostName")
		val ctxHost : String?  = solve("getCtxHost($ctx,H)","H")
		//println("               %%% sysUtil | createTheContext $ctx ctxHost=$ctxHost  ")
		//val ctxProtocol : String? = solve("getCtxProtocol($ctx,P)","P")
		val ctxPort     : String? = solve("getCtxPort($ctx,P)","P")
		//println("               %%% sysUtil | $ctx host=$ctxHost port = $ctxPort protocol=$ctxProtocol")
		val portNum = Integer.parseInt(ctxPort)

//		val useMqtt = ctxProtocol!!.toLowerCase() == "mqtt"	//CoAP 2020
		var mqttAddr = ""
//		if( useMqtt ){	//CoAP 2020
			if( mqttBrokerIP != null ){
				mqttAddr = "tcp://$mqttBrokerIP:$mqttBrokerPort"
				println("               %%% sysUtil | context $ctx WORKS ALSO WITH MQTT mqttAddr=$mqttAddr")
			}
			//else{ throw Exception("no MQTT broker declared")  }	//CoAP 2020
//		}
		//CREATE AND MEMO THE CONTEXT
		var newctx : QakContext?  
		if( ! ctxHost.equals(hostName) ){
              println("               %%% sysUtil | createTheContext $ctx for DIFFERENT ctxHost=$ctxHost host=$hostName} ")
			  newctx = QakContext( ctx, "$ctxHost", portNum, "", true) //isa ActorBasic
		}else{
			  println("               %%% sysUtil | createTheContext $ctx FOR host=$hostName  ")
			  newctx = QakContext( ctx, "$ctxHost", portNum, "") //isa ActorBasic
		}
		//val newctx = QakContext( ctx, "$ctxHost", portNum, "") //isa ActorBasic
		newctx.mqttAddr = mqttAddr //!!!!!! INJECTION !!!!!!
		ctxsMap.put(ctx, newctx)
		if( ! ctxHost.equals(hostName) ){
			return null
		}
		ctxOnHost.add(newctx)
		return newctx
 	}//createTheContext

	@JvmStatic fun createTheContext(ctx: String, hostName: String, localContextName: String) : QakContext?{
		//sysUtil.aboutThreads("sysUtil createTheContext $ctx localContextName=$localContextName host=$hostName")
		val ctxHost : String?     = solve("getCtxHost($ctx,H)","H")
		val ctxPort     : String? = solve("getCtxPort($ctx,P)","P")
		//println("               %%% sysUtil | $ctx host=$ctxHost port = $ctxPort protocol=$ctxProtocol")
		val portNum = Integer.parseInt(ctxPort)
		var mqttAddr = ""
		if( mqttBrokerIP != null ){
			mqttAddr = "tcp://$mqttBrokerIP:$mqttBrokerPort"
			sysUtil.aboutThreads("sysUtil | context $ctx WORKS ALSO WITH MQTT mqttAddr=$mqttAddr")
		}
		//CREATE AND MEMO THE CONTEXT
		var newctx : QakContext? = null
		if( ctx != localContextName && hostName != "localhost" ){ //NOV22  //
			sysUtil.aboutThreads("sysUtil | createTheContext $ctx for DIFFERENT node (localContextName=$localContextName host=$ctxHost) ")
			newctx = QakContext( ctx, "$ctxHost", portNum, "", true) //isa ActorBasic
		}else if( ctx == localContextName && hostName == "localhost"){
			sysUtil.aboutThreads("sysUtil | createTheContext $ctx for LOCAL node (host=$hostName)  ")
			newctx = QakContext( ctx, "$ctxHost", portNum, "") //isa ActorBasic
			//CommUtils.outmagenta("sysUtil createTheContext DONE $newctx   host=$ctxHost")
		}else{
			CommUtils.outred("sysUtil createTheContext STRANGE host");
			return null
		}
		//val newctx = QakContext( ctx, "$ctxHost", portNum, "") //isa ActorBasic
		if( newctx != null ){
			newctx.mqttAddr = mqttAddr //!!!!!! INJECTION !!!!!!
		    ctxsMap.put(ctx, newctx)
			ctxOnHost.add(newctx)
			return newctx
		}else return null;
		//if( ctx!=localContextName ){ return null }
	}//createTheContext
/*
	fun addProxyToOtherCtxs( ctxsList : List<String>, hostName : String){
		ctxsList.forEach { ctx ->
			val curCtx = ctxsMap.get("$ctx")
			if( curCtx is QakContext && curCtx.hostAddr != hostName ) {
				val others = solve("getOtherContextNames(OTHERS,$ctx)","OTHERS")
				val ctxs = strRepToList(others!!)
 					//others!!.replace("[", "").replace("]", "").split(",")
				ctxs.forEach {
 					if( it.length==0  ) return
					val ctxOther = ctxsMap.get("$it")
					if (ctxOther is QakContext) {
						println("               %%% sysUtil | FOR ACTIVAT CONTEXT ${ctxOther.name}: ADDING A PROXY to ${curCtx.name} ")
  						ctxOther.addCtxProxy(curCtx)
					}else{  //NEVER??
						if( ctxOther!!.mqttAddr.length > 1 )  return //NO PROXY for MQTT ctx
						println("               %%% sysUtil | WARNING: CONTEXT ${it} NOT ACTIVATED: " +
					 			"WE SHOULD WAIT FOR IT, TO SET THE PROXY in ${curCtx.name}")
						val ctxHost : String?     = solve("getCtxHost($it,H)","H")
 						val ctxProtocol : String? = solve("getCtxProtocol($it,P)","P")
						val ctxPort : String?     = solve("getCtxPort($it,P)","P")
						curCtx.addCtxProxy( it, ctxProtocol!!, ctxHost!!, ctxPort!! )  
					}
				}
			} //else{ println("sysUtil | WARNING: $ctx NOT ACTIVATED ") }
		}
	}//addProxyToOtherCtxs
*/
	fun addProxyToOtherCtxs( ctxsList : List<String>, hostName : String, localContextName: String? = null){
		ctxsList.forEach { ctx ->
			aboutThreads("addProxyToOtherCtxs $ctx in $ctxsList host=$hostName localContextName=$localContextName")
			val curCtx = ctxsMap.get("$ctx")
			//val a : Boolean
			val isLocalContext = if(localContextName==null){
				curCtx!!.hostAddr == hostName
			}else{
				ctx == localContextName
			}
			if( curCtx is QakContext && !isLocalContext ) {
				aboutThreads("sysUtil addProxyToOtherCtxs $localContextName $ctx")
				val others = solve("getOtherContextNames(OTHERS,$ctx)","OTHERS")
				val ctxs   = strRepToList(others!!)
				ctxs.forEach {
					if( it.length==0  ) return
					val ctxOther = ctxsMap.get("$it")
					if (ctxOther is QakContext ) { //&& ctxOther != curCtx Aug2022
						aboutThreads("sysUtil addCtxProxy ${ctxOther.name} , ${curCtx.name}")
 						ctxOther.addCtxProxy(curCtx)
 					}else{  //NEVER??
 						CommUtils.outred("addProxyToOtherCtxs $ctxOther")
						if( ctxOther==null ) return;
						if( ctxOther!!.mqttAddr.length > 1 )  return //NO PROXY for MQTT ctx
						println("               %%% sysUtil | WARNING: CONTEXT ${it} NOT ACTIVATED: " +
								"WE SHOULD WAIT FOR IT, TO SET THE PROXY in ${curCtx.name}")
						val ctxHost : String?     = solve("getCtxHost($it,H)","H")
						val ctxProtocol : String? = solve("getCtxProtocol($it,P)","P")
						val ctxPort : String?     = solve("getCtxPort($it,P)","P")
						curCtx.addCtxProxy( it, ctxProtocol!!, ctxHost!!, ctxPort!! )
					}
				}
			} //else{ println("sysUtil | WARNING: $ctx NOT ACTIVATED ") }
		}
	}//addProxyToOtherCtxs

	@JvmStatic fun getAllActorNames(ctxName: String) : List<String>{
		val actorNames : String? = solve("getActorNames(A,$ctxName)","A" )
		val actorList = strRepToList(actorNames!!)
		return actorList
	}
	@JvmStatic fun getAllActorNames( ) : List<String>{
		val actorNames : String? = solve("getActorNames(A,ANY)","A" )
		val actorList = strRepToList(actorNames!!)
		return actorList
	}

	@JvmStatic fun getNonlocalActorNames( ctx : String ) : List<String>{
		val actorNames : String? = solve("getNonlocalActorNames($ctx,A)","A" )
		val actorList = strRepToList(actorNames!!)
		return actorList
	}

	@JvmStatic  fun strRepToList( liststrRep: String ) : List<String>{
		return liststrRep.replace("[","")
			.replace("]","").split(",")
	}
	@JvmStatic  fun createTheActors( ctx: QakContext, scope : CoroutineScope ){
		val actorList = getAllActorNames(ctx.name)
		aboutThreads("sysUtil | createTheActors in context ${ctx.name} actorList=$actorList "   )
		actorList.forEach{
			if( it.length > 0 ){
				val actorClass = solve("qactor($it,${ctx.name},CLASS)","CLASS")
				//println("sysUtil | CREATE actor=$it in context:${ctx.name}  class=$actorClass"   )
				val className = actorClass!!.replace("'","")
				createActor( ctx, it, className, scope)
			}
		}
	}//createTheActors

	//NOV22 Creazione di attori in nuovo contesto su localhost dopo che un contesto su localhost è stato attivato
	//DEPRECATED
	/*
	@JvmStatic  fun createOtherLocalActors( ctx: String, curCtx: String  ){
		CommUtils.outgreen("sysUtil | createOtherLocalActors ctx=$ctx curCtx:${curCtx}  "   )
		//createTheContext(ctx, "localhost", ctx)
		val actorList = getAllActorNames( ctx )
		actorList.forEach{
			if( it.length > 0 ){
				val actorClass = solve("qactor($it,${ctx},CLASS)","CLASS")
				CommUtils.outgreen("sysUtil | CREATE actor=$it in context:${ctx}  class=$actorClass"   )
				val className = actorClass!!.replace("'","")
				val actorCtx  = getContext(ctx);
				if( actorCtx != null) {
					val a = createActor( actorCtx, it, className, QakContext.scope22)
					if( a != null ) getActorContext(curCtx)!!.actorMap.putIfAbsent(it, a)
				}else CommUtils.outred("sysUtil | createOtherLocalActors - no context found for " + ctx);
			}
		}
	}*/

	@JvmStatic fun createActor( ctx: QakContext, actorName: String,
					 className : String, scope : CoroutineScope = GlobalScope  ) : ActorBasic?{
		/*
		if( className=="external"){
			println("               %%% sysUtil |   actor=$actorName in context:${ctx.name}  is EXTERNAL"   )
			return null
		}
  	 */
		//sysUtil.aboutThreads("%%% sysUtil | CREATE actor=$actorName in context:${ctx.name}  class=$className"   )
		val clazz = Class.forName(className)	//Class<?>
        var actor  : ActorBasic 
        try {
            val ctor = clazz.getConstructor(String::class.java, CoroutineScope::class.java )  //Constructor<?>
            actor = ctor.newInstance(actorName, scope  ) as ActorBasic 
        } catch( e : Exception ){
			//println("sysUtil  | ERROR ${e}" )
            val ctor = clazz.getConstructor( String::class.java )  //Constructor<?>
            actor    = ctor.newInstance( actorName  ) as ActorBasic
			//aboutThreads("sysUtil createActor ${actor.name} in ${ctx.name}" );
		}
		ctx.addActor(actor)
		actor.context = ctx
		//val autoStartMsg = MsgUtil.buildDispatch(actorName, "autoStartSysMsg", "start", actorName)
		//NON faccio partire in modo automatico ActorBasic. Lo farà ActorBasicFsm
		//MEMO THE ACTOR
		ctxActorMap.put(actorName,actor  )
		return actor
	}

	@JvmStatic fun solve( goal: String, resVar: String  ) : String? {
		//println("sysUtil  | solveGoal ${goal} resVar=$resVar" );
		//val sol = pengine.solve( "context(CTX, HOST,  PROTOCOL, PORT)."); //, "CTX"
		val sol = pengine.solve( goal+".");
		if( sol.isSuccess ) {
			if( resVar.length == 0 ) return "success"
			val result = sol.getVarValue(resVar)  //Term
			var resStr = result.toString()
			return  strCleaned( resStr )
		}
		else return null
	}

	@JvmStatic fun loadTheory( path: String ) {
		try {
			//user.dir is typically the directory in which the Java virtual machine was invoked.
			//val executionPath = System.getProperty("user.dir")
			//println("               %%% sysUtil | loadheory executionPath: ${executionPath}" )
			//val resource = classLoader.getResource("/") //URL
			//val cl =  javaClass<ActorBasic> //javaClass does not work
			//println("               %%% sysUtil | loadheory classloader: ${cl}" )
			val worldTh = Theory( FileInputStream(path) )
			pengine.addTheory(worldTh)
		} catch (e: Exception) {
			println("               %%% sysUtil | loadheory WARNING: ${e}" )
			throw e
		}
	}


	@JvmStatic fun strCleaned( s : String) : String{
		if( s.startsWith("'")) return s.replace("'","")
		else return s

	}


	fun traceprintln( msg : String ){
		if( sysUtil.trace ) CommUtils.outyellow(
			msg + " | " + Thread.currentThread().getName() + " n=" + Thread.activeCount())
	}
	
/*
 	MSG LOGS
*/
@JvmStatic fun createFile( fname : String, dir : String = "logs" ){
 		val logDirectory = File("$userDirectory/$dir")
		logDirectory.mkdirs()	//have the object build the directory structure, if needed
		var file = File(logDirectory, fname)
//		println("               %%% sysUtil | createFile file $file in $dir")
		file.writeText("")	//file is created and nothing is written to it
	}

	@JvmStatic fun deleteFile( fname : String, dir  : String ){
		File("$userDirectory/$dir/$fname").delete()
	}
	@JvmStatic fun updateLogfile( fname: String, msg : String, dir : String = "logs" ){
		if( logMsgs ) File("$userDirectory/$dir/$fname").appendText("${msg}\n")
	}
	@JvmStatic  fun aboutThreads(info: String){
		val tname    = Thread.currentThread().getName();
		val nThreads = ""+Thread.activeCount() ;
		CommUtils.outyellow("               %%% $info thread=$tname n=$nThreads"  )
	}
	@JvmStatic  fun waitUser(prompt: String, tout: Long = 2000   ) {
			try {
				print(">>>  $prompt (tout=$tout) >>>  ")
				val input = BufferedReader(InputStreamReader(System.`in`))
				val startTime = System.currentTimeMillis()
				while (System.currentTimeMillis() - startTime < tout && !input.ready() ) { }
				println("")
  			} catch (e: java.lang.Exception) {
				e.printStackTrace()
			}
	}
}//sysUtil
