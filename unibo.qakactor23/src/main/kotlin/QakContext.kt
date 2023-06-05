package it.unibo.kactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.newSingleThreadContext
import org.eclipse.californium.core.CoapServer
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.utils.CommUtils
import java.net.InetAddress

open class QakContext(name: String, val hostAddr: String, val portNum: Int, var mqttAddr : String = "",
                      val external: Boolean=false, val gui : Boolean = false   ) : ActorBasic(name){

    internal val actorMap : MutableMap<String, ActorBasic> = mutableMapOf<String, ActorBasic>()
    internal val proxyMap:  MutableMap<String, NodeProxy>  = mutableMapOf<String, NodeProxy>()  //cannot be static
    //lateinit private var serverCoap  :  CoapServer      //CoAP: Jan2020
    lateinit var resourceCtx : CoapResourceCtx
	lateinit var ctxserver  : QakContextServer
	lateinit var serverCoap : CoapServer
 	lateinit var ctxLogfileName : String
	
    companion object {
        val workTime = 1000L * 6000 //100 min
        lateinit var scope22 : CoroutineScope
        enum class CtxMsg { attach, remove }

        fun getActor( actorName : String ) : ActorBasic? {
            return sysUtil.getActor(actorName)
        }


        fun createScope(): CoroutineScope{
            if( ! this::scope22.isInitialized ) {
                //println("               %%% QakContext | createScope  ++++++++++++++++++++++++++++++++++  ")
                val d = newSingleThreadContext("single");
                scope22 = CoroutineScope(d);
            }
            return scope22;
        }

        fun createContexts(hostName: String, desrFilePath: String, rulesFilePath: String) {
        }

        //Called by generated code main of ctx
        //ARGUMENT localContextName by Loris Giannatempo, August 2022
         fun createContexts(hostName: String, scope: CoroutineScope ,
                           desrFilePath: String, rulesFilePath: String,
                            localContextName: String? = null) {

            sysUtil.createContexts(hostName, desrFilePath, rulesFilePath,localContextName)
             if( sysUtil.ctxOnHost.size == 0 ){
                val ip = InetAddress.getLocalHost().getHostAddress()
                sysUtil.traceprintln("               %%% QakContext | CREATING NO ACTORS on $hostName ip=${ip.toString()}")
            }
            else{
                 sysUtil.aboutThreads("QakContext scope=$scope BEFORE createContexts on $hostName " );
                 //println("               %%% QakContext | CREATING THE ACTORS on $hostName ")
            }
//			runBlocking {

            sysUtil.ctxOnHost.forEach {
                    ctx ->
                if( ctx.name == localContextName ){
                    //sysUtil.traceprintln("QakContext createTheActors for ${ctx.name} while $localContextName");
                    sysUtil.createTheActors(ctx, scope)
                }
            }

            //Avoid premature termination
//            scope.launch{
//                println("               %%% QakContext |  $hostName CREATED. I will terminate after $workTime msec")
//                delay( workTime )
//            }
//			(scope as Job).join()
//			}
            sysUtil.aboutThreads("QakContext AFTER createContexts on $hostName " );
 			//println("               %%% QakContext | createContexts on $hostName ENDS " )
        }
    }
    init{

         ctxLogfileName    = "${name}_MsLog.txt"	//APR2020
        //OCT2019 --> NOV2019 Create a QakContextServer also when we use MQTT
        resourceCtx = CoapResourceCtx( name, this )   //must be ininitialized here  ENABLE
        if( ! external ){

            sysUtil.aboutThreads("QakContext $hostAddr:$portNum AFTER CoapResourceCtx  " );
            //println("               %%% QakContext |  $hostAddr:$portNum INIT ")
            ctxserver = QakContextServer( this, createScope(), "server$name", Protocol.TCP ) //
            //CoAP: Jan2020

              try{
                  //sysUtil.waitUser("Starting CoapServer", 20000);
                  //sysUtil.aboutThreads("QakContext $hostAddr:$portNum BEFORE CoapServer " );
                  val coapPort    =  portNum
                  serverCoap      =  CoapServer(coapPort) //from org.eclipse.californium.core
                  serverCoap.add(  resourceCtx )
                  serverCoap.start()

                  //println("%%% serverCoap started "  )

                  sysUtil.aboutThreads("QakContext $hostAddr:$portNum AFTER CoapServer on port: $coapPort " );
                                    //println( "               %%% QakContext $name |  serverCoap started on port: $coapPort" )
            }catch(e : Exception){
                println( "               %%% QakContext $name |  serverCoap error: ${e.message}" )
            }
          }
}

	fun terminateTheContext(){
		serverCoap.stop()
		ctxserver.actor.close()
	}

    override suspend fun actorBody(msg : IApplMessage){
        sysUtil.traceprintln( "               %%% QakContext $name |  receives $msg " )
    }


    fun addCtxProxy(ctxName: String, protocol: String, hostAddr: String, portNumStr: String) {
        val p = MsgUtil.strToProtocol(protocol)
        val portNum = Integer.parseInt(portNumStr)
        //sysUtil.traceprintln("               %%% QakContext $name | addCtxProxy ${ctxName}, $hostAddr, $portNum")\
        val proxy = NodeProxy("proxy${ctxName}", this, p, hostAddr, portNum)
        proxyMap.put(ctxName, proxy)
    }
    /*
    Aug2022: gli attori vanno creati prima di lanciare il QakContextServer
     */
    fun addActor( actor: ActorBasic) {
        //println("%%%  addActor in coapctx   "  )
        actor.context = this    //injects the context
 		actor.createMsglogFileInContext()
        actorMap.put( actor.name, actor )
        actor.checkMqtt()
        //sysUtil.traceprintln("               %%% QakContext $name | addActor ${actor.name}")
        if( this::resourceCtx.isInitialized  ) resourceCtx.addActorResource( actor )   //CoAP: Jan2020
    }

    fun addInternalActor( actor: ActorBasic) {
        actor.context = this    //injects the context
// 		actor.createMsglogFileInContext()	//internal actors have no context !
        actorMap.put( actor.name, actor )
    }



    fun removeInternalActor( actor: ActorBasic){
        actorMap.remove( actor.name )
 		actor.terminate()
     }

    fun hasActor( actorName: String ) : ActorBasic? {
        return actorMap.get(actorName)
    }

    fun addCtxProxy( ctx : QakContext ){
        if( ctx.mqttAddr.length > 1 ) return
        sysUtil.traceprintln("               %%% QakContext $name | addCtxProxy ${ctx.name}")
        val proxy = NodeProxy("proxy${ctx.name}", this, Protocol.TCP, ctx.hostAddr, ctx.portNum)
        proxyMap.put( ctx.name, proxy )
		//APR2020: we should remove the active connection from
    }

    fun addCtxProxy( ctxName :String, hostAddr: String, portNum : Int  ){
        sysUtil.traceprintln("               %%% QakContext $name | addCtxProxy host=$hostAddr portNum=${portNum}")
        val proxy = NodeProxy("proxy${ctxName}", this, Protocol.TCP, hostAddr, portNum)
        proxyMap.put( ctxName, proxy )
    }

}