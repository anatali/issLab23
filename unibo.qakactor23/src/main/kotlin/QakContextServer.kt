package it.unibo.kactor

import kotlinx.coroutines.*
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.tcp.TcpServer
import unibo.basicomm23.utils.CommSystemConfig
import unibo.basicomm23.utils.CommUtils

class QakContextServer(val ctx: QakContext, scope: CoroutineScope,
                       name:String, val protocol: Protocol ) : ActorBasic( name, scope) {
    protected var hostName: String? = null
 
    init {
        System.setProperty("inputTimeOut", QakContext.workTime.toString() )  //100 min	
        scope.launch(Dispatchers.IO) {
            autoMsg( "start", "startQakContextServer" )
        }
        sysUtil.aboutThreads("QakContextServer $name  | AFTER init   " ); //scope=$scope
    }

    override suspend fun actorBody(msg : IApplMessage){
        sysUtil.aboutThreads("QakContextServer $name | READY TO RECEIVE TCP CONNS on ${ctx.portNum} ")
        waitForConnection()
    }

    suspend protected fun waitForConnection() {
        //We could handle several connections
        GlobalScope.launch(Dispatchers.IO) {
            try {
                //CommUtils.outblue("%%% QakContextServer $name | waitForConnection protocol=${protocol == Protocol.TCP}");
                    if( protocol == Protocol.TCP ){
                        val userDefHandler = ContextMsgHandler("${ctx.name}MsgH", ctx)
                        //CommUtils.outgreen(name + " | waitForConnection $userDefHandler" );
                        val server = TcpServer("tcpSrv",ctx.portNum,userDefHandler)
                        CommSystemConfig.tracing = true
                        server.activate()
                 }
            } catch (e: Exception) {
                 println("      QakContextServer $name | WARNING: ${e.message}")
            }
        }
    }
/*
EACH CONNECTION WORKS IN ITS OWN COROUTINE
 */
 
}

