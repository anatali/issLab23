/*
-------------------------------------
simpleProducerKotlin.kt
-------------------------------------
*/
package prodCons
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

/*
produce coroutine builder : can send items to a ReceiveChannel
    - dispatcher : single or Dispatchers.IO
    - capacity   : default (0) or set by the user
 */

val dispatcher  = newSingleThreadContext("myThread") //Dispatchers.IO //
//var simpleProducer : ReceiveChannel<Int>? = null
lateinit var simpleProducer : ReceiveChannel<Int>

@kotlinx.coroutines.ObsoleteCoroutinesApi
fun startProducer( scope: CoroutineScope ) {
    simpleProducer = scope.produce(dispatcher, capacity=2){ //capacity=1
        for (i in 1..4) {
            println("producer PRE -> $i  in  ${curThread()}  ")
            send(i)
            println("producer POST-> $i at ${System.currentTimeMillis()}   ")
            //delay( 500)
        }//for
    }
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun consume( ){
    val v = simpleProducer.receive()  //the first
    delay(250)
    println( "consume- first ${v} at ${System.currentTimeMillis()} in ${curThread()}" )
    simpleProducer.consumeEach {
        //delay(50)
        println( "consume- $it at ${System.currentTimeMillis()} in ${curThread()}" )
    }
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
fun startProducer1( scope: CoroutineScope ) : ReceiveChannel<Int> {
    return scope.produce(dispatcher, capacity=0){ //capacity=1
        for (i in 1..4) {
            println("producer1 PRE -> $i  in  ${curThread()}  ")
            send(i)
            println("producer1 POST-> $i at ${System.currentTimeMillis()}   ")
        }//for
    }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun consume1( scope: CoroutineScope ){
    startProducer1(scope).consumeEach {
        println( "consume1- $it at ${System.currentTimeMillis()} in ${curThread()}" )
    }
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() {
    println("BEGINS CPU=$cpus ${kotlindemo.curThread()}")
    runBlocking {
        startProducer(this)
        consume()
//MORE FUNCTIONAL STYLE
        /*
        val myscope = CoroutineScope(dispatcher)
        consume1(myscope)*/
        println("ENDS runBlocking ${kotlindemo.curThread()}")
    }
    println("ENDS main ${kotlindemo.curThread()}")
}
