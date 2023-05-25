package kotlincode

import it.unibo.kactor.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage

data class SonarData( val distance: Int)

val data = listOf(
    SonarData( 16),
    SonarData( 23),
    SonarData( 25),
    SonarData( 28),
    SonarData( 39)
)

class sonarShow( name : String, scope: CoroutineScope) : ActorBasic( name, scope ) {

     override suspend fun actorBody(msg: IApplMessage) {
        when( msg.msgId() ){
            "start" -> scope.launch{  simulateInputData() }  //run in parallel
            else -> println("   sonarShow $name |  receives $msg ")
        }
    }

    suspend fun simulateInputData(){
        val numData = 8
        var dataCounter = 1
        data.forEach{
              println("data ${dataCounter++} = $it " )
                val m = MsgUtil.buildEvent(name, "sonar", "sonar($dataCounter, ${it.distance})")
                emitLocalStreamEvent( m  )
                emit(m.msgId(), m.msgContent())
                delay(100)
        }
    }

}

fun main() = runBlocking {

    QakContext.createContexts(
        "localhost",this,
        "sonarSysDescr.pl",
        "sysRules.pl"
    )
	println("main | STARTS ")
    val sonar = QakContext.getActor("sonarshow")
	println("sonarShow | sonar $sonar ")
    val sink = Sink("sink", this)
    sonar!!.subscribe(sink)
    MsgUtil.sendMsg("start", "start", sonar!!)
}