//squareProducerKotlin.kt
package prodCons

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
//import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlin.time.milliseconds
/*
produce coroutine builder : can send items to a ReceiveChannel
    - dispatcher : single or Dispatchers.IO
    - capacity   : default (0) or set by the user

 */

fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce(dispatcher) {//capacity=1
    for (x in 1..4) {
        println( "squares PRE ->  $x at ${System.currentTimeMillis()} in  ${curThread()}  ")
        send(x * x)
        println("squares POST -> $x at ${System.currentTimeMillis()}   ")
    }
}

fun consumeSquares( squares: ReceiveChannel<Int> ){
    val myscope = CoroutineScope(dispatcher)
    myscope.launch{
        squares.consumeEach {
            println("consume-squares<- $it at ${System.currentTimeMillis()} in ${curThread()}")
            //delay(500)
        }
    }
}





@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() {
    println("BEGINS CPU=$cpus ${kotlindemo.curThread()}")
    runBlocking {
        println(this)
         val squares = produceSquares()
        consumeSquares(squares)
        println("ENDS runBlocking ${kotlindemo.curThread()}")
    }
    println("ENDS main ${kotlindemo.curThread()}")
}
