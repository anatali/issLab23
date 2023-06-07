/*
-------------------------------------
prodManyConsKotlin.kt
-------------------------------------
*/
package prodCons


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce


@kotlinx.coroutines.ObsoleteCoroutinesApi
val aProducer : ReceiveChannel<Int> = GlobalScope.produce{
    for( i in 1..3 ){
        println( "aProducer produces $i in ${curThread()}")
        send( i )
    }
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
fun consumer1(scope: CoroutineScope){
    scope.launch{
        delay(100)
        val v = aProducer.receive()
        println( "consumer1 receives ${v} in ${curThread()}" )
     }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
fun consumer2(scope: CoroutineScope){
    scope.launch{
        for( i in 1..2 ) {
            val v = aProducer.receive()
            println("consumer2 receives ${v} in ${curThread()}")
            delay(100)
        }
    }
}

fun manyConsumers(){
    runBlocking {
        consumer1(this)
        consumer2(this)
    }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() {
    println("BEGINS CPU=${kotlindemo.cpus} ${kotlindemo.curThread()}")
    manyConsumers()
    println("ENDS main ${kotlindemo.curThread()}")
}
