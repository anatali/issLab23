package it.unibo.kactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader

class TimerActor(name: String, scope: CoroutineScope, val ctx : QakContext,
                 val ev: String, val tout : Long) : Thread() { //: ActorBasic(name,scope)
    var terminated = false;
    var starttime = 0L
    var Duration  = 0L

    lateinit var myactor : ActorBasic
    init{
        //Refactored July 2021, in order to avoid the usage of delay(tout)
        var ownerName = ev.replace("local_tout_","")
        ownerName     =  ownerName.substringBefore("_") // .replace("_doStep","")
        try{
            myactor       = QakContext.getActor(ownerName)!!
            start()
        }catch(e:Exception){
            println("WARNING: TimerActor time=$tout does not find its owner:${ownerName}")
        }

    }

    override fun run(){
        //println("TimerActor $name SLEEP tout=$tout ct=${System.currentTimeMillis()} ")
        starttime     = System.currentTimeMillis()
        Thread.sleep(tout)
        val dd = System.currentTimeMillis() - starttime
        //Runtime.getRuntime().exec("sudo ./sleepcrono")
        //println("TimerThread  RESUMES after:${dd} ")
        if( ! terminated ){
            val msgEv = MsgUtil.buildEvent("timer",ev,ev)
            runBlocking {
                myactor.actor.send(msgEv)
            }
            //MsgUtil.sendMsg("timer",ev,ev,"basicrobot", null)
            //println("TimerActor $name has EMITTED :${ev} ")
        }else{
            //println("TimerActor $name ENDS without emitting since terminated after: $Duration")
        }
    }

    /*


   override suspend fun actorBody(msg : ApplMessage){
        //sysUtil.trace
    println("TimerActor using delay RECEIVES  ${msg} tout=$tout")
        if( msg.msgId() == "start") {
            /*
            The thread is returned to the pool while the coroutine is waiting,
            and when the waiting is done, the coroutine resumes on a free thread in the pool.
             */
            delay(tout)
                // Thread.sleep(tout)
            //delayUsingLinux()
            if( ! terminated ){
                emit(ev, ev)
                println("TimerActor terminated=$terminated EMITTED :${ev} ")
            }
            this.actor.close()
            //println("TimerActor $tout ENDS ")
        }
    }
*/
    fun delayUsingLinux(){
        val p      = Runtime.getRuntime().exec("sudo ./cronoLinux")
        val reader = BufferedReader(  InputStreamReader(p.getInputStream() ))
        println(" startReaddddddddddddddddddddddddddddddd ")
        //GlobalScope.launch{
        while( true ){
            var data = reader.readLine()
            println("crono data = $data"   )
            if( data == null ) break
        }
        //}

    }
    fun endTimer(){
        Duration = System.currentTimeMillis() - starttime
        //println("TimerActor $name TERMINATED duration=$Duration")
        terminated = true;
    }


}