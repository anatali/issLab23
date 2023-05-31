//simpleReceiverKotlin.kt
package prodCons
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*


@kotlinx.coroutines.ObsoleteCoroutinesApi
fun startAReceiver( scope: CoroutineScope, rec: Channel<Int> ) {
    scope.launch{
        for( i in 1..3 ) {
            val v = rec.receive()
            println("received ${v}  in ${curThread()}")
        }
    }
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun startAProducer( rec: SendChannel<Int>  ){
    for( i in 1..3 ){
        val v = rec.send(i)  //the first
        delay(250)
        //println( "produced ${v} at ${System.currentTimeMillis()} in ${curThread()}" )
    }
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() {
    println("BEGINS CPU=$cpus ${kotlindemo.curThread()}")
    runBlocking {
        val ch = Channel<Int>(2)
        startAReceiver(this, ch)
        startAProducer(ch)
        println("ENDS runBlocking ${kotlindemo.curThread()}")
    }
    println("ENDS main ${kotlindemo.curThread()}")
}
