package kotlincode

import it.unibo.kactor.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage
import java.io.BufferedReader
import java.io.InputStreamReader

class sonarOnRaspEmit( name : String, scope: CoroutineScope) : ActorBasic( name, scope ) {

     override suspend fun actorBody(msg: IApplMessage) {
        when( msg.msgId() ){
            "start" -> scope.launch{  readInputData() }
            else -> println("   sonarShow $name |  receives $msg ")
        }
    }

    suspend fun readInputData(){
        val numData = 8
        var dataCounter = 1
        val p : Process = machineExec("sudo ./SonarAlone")
        val reader = BufferedReader(InputStreamReader(p.getInputStream()))
        while( true ){
             var data = reader.readLine()    //blocking
              println("data ${dataCounter++} = $data " )
             if( dataCounter % numData == 0 ) { //every numData ...
                val m = MsgUtil.buildEvent(name, "sonar", "sonar($dataCounter, $data)")
                println("EMIT $m"  )
                emitLocalStreamEvent( m  )
                //emit(m.msgId(), m.msgContent())
            }
        }

    }

}

fun main() = runBlocking {

    QakContext.createContexts(
        "localhost",this,
        "sonarSysDescr.pl",
        "sysRules.pl"
    )

    val sonar = QakContext.getActor("sonarShow")
 	println("sonarShow | sonar $sonar ")
 
	val sink = Sink("sink", this)
    sonar!!.subscribe(sink)
	
	println("sonarShow | sink $sink subscribed")
    
	MsgUtil.sendMsg("start", "start", sonar!!)
}