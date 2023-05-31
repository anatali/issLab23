package prodCons
//prodSequenceKotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce


@kotlinx.coroutines.ObsoleteCoroutinesApi
val prodContext = newSingleThreadContext("myThread")

val fiboSeq = sequence{
    var a = 0
    var b = 1
    yield(1)           //first
    while (true) {
        yield(a + b)   //next
        val tmp = a + b
        a = b
        b = tmp
    }
}

val seqProd  = sequence{
    var v = 1
    for(i in 1..3){
        //println( "seqProd produced $v in ${curThread()}")
        yield( v  )
        //println( "seqProd produced $v in ${curThread()}")
        v++
    }
    //println( "seqProd generateSequence  ")
    yieldAll( generateSequence(2) { println("generate $it"); it * 2  } )
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun seqcons1( scope : CoroutineScope){
    println("seqcons1 STARTS")
    scope.launch {
        for( i in 1 .. 5 ) {
            val v = seqProd.elementAt(i)
            //val vlist = seqProd.take(3).toList()
            println("seqcons1 $i receives $v in ${curThread()}")
            delay(100) //release control
        }
    }
}

suspend fun seqcons2( scope : CoroutineScope ){
    println("seqcons2 STARTS")
    scope.launch {
        for( i in 1 .. 3 ) {
            val vlist = seqProd.take(i*3).filter { it % 2 == 0 }.toList()
            println("seqcons2 receives $vlist in ${curThread()}")
            delay(100) //release control
        }
    }
}


fun main() = runBlocking{
    //seqcons1(this)
    seqcons2(this)
    println( "BYE")
}
