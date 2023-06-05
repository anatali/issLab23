package it.unibo.kactor

import unibo.basicomm23.interfaces.Interaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.msg.ApplMessage


class NodeProxy( name: String, val ctx: QakContext, val protocol: Protocol,
                 val hostName: String,  val portNum: Int ) : ActorBasic( name ) {

    protected var conn: Interaction? = null

    init {
        configure()
    }



    fun configure() {
        while (conn == null) {
            when (protocol) {
                Protocol.TCP, Protocol.UDP ->
                    conn = MsgUtil.getConnection(protocol, hostName, portNum, name)
                Protocol.SERIAL -> conn = MsgUtil.getConnectionSerial("", 9600)
                else -> println("               %%% NodeProxy $name | WARNING: protocol unknown")
            }
            if (conn == null) {
                println("               %%% NodeProxy $name | WAIT/RETRY TO SET PROXY TO $hostName:$portNum ")
                Thread.sleep(500)
            } else {
                sysUtil.traceprintln("               %%% NodeProxy $name | in ${ctx.name} PROXY ACTIVATED TO $hostName:$portNum ")
                handleConnection(conn!!)
            }
        }
    }

    //Routes each message to the connected server
    override suspend fun actorBody(msg: IApplMessage) {
        //sysUtil.traceprintln("       NodeProxy $name receives $msg conn=$conn ") // conn=$conn"
        try {
            conn?.forward("$msg")
        } catch (e: Exception) {
            println("               %%% NodeProxy $name  | sendALine error $e ")
            conn = null;
            configure() //ritento la connessione NOV22
        }
    }


    //Oct2019 : handle answers sent on this connections


   protected fun handleConnection( conn: Interaction ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                //sysUtil.traceprintln("               %%% NodeProxy $name  | handling new input from :$conn")
                while (true) {
                    val msg = conn.receiveMsg()       //BLOCKING ???
                    sysUtil.traceprintln("               %%% NodeProxy $name  | receives: $msg  ")
                    if( msg == null ){
                        break
                    }
                    sysUtil.traceprintln("               %%% NodeProxy $name  | receives: $msg")
                    val inputmsg = ApplMessage(msg)
                    if( inputmsg.isEvent() ){  //Oct2019
                        //println("               %%% NodeProxy $name | handling event")
                        emit( ctx, inputmsg )
                    }else {
                        val dest = inputmsg.msgReceiver()
                        val actor = ctx.hasActor(dest)
                        if (actor is ActorBasic) {
                            try {
                                //if( inputmsg.msgType() == ApplMessageType.reply.toString() ) { //Oct2019
                                MsgUtil.sendMsg(inputmsg, actor)
                                //}
                            } catch (e1: Exception) {
                                println("               %%% NodeProxy $name  | WARNING: ${e1.message}")
                            }
                        } else println("               %%% NodeProxy $name  | WARNING!! no local actor ${dest} in ${ctx.name}")
                    }
                }
            }catch (e: Exception) {
                println("               %%% NodeProxy $name  | error $e ")
            }//try
        }//scope
    }
}