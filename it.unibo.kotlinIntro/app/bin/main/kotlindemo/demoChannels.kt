package kotlindemo
//demoChannels.kt
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.CoroutineScope


suspend fun channelTest( scope : CoroutineScope ){
val n = 5
val channel = Channel<Int>(1)
		println( channel )	//ArrayChannel capacity=2 size=0
	
        val sender = scope.launch {
            repeat( n ) {
                channel.send( it )
                println("SENDER | sent $it in ${curThread()}")
            }
        }
        
	delay(500) //The receiver starts after a while ...
        
		val receiver = scope.launch {
            for( i in 1..n ) {
                val v = channel.receive()
                delay(50)
                println("RECEIVER | receives $v in ${curThread()}")
            }
        }
}

suspend fun channelTestMany( scope : CoroutineScope ){
    val n = 5
    val channel = Channel<String>(2)
    println( channel )	//ArrayChannel capacity=2 size=0

    val sender1 = scope.launch {
        repeat( n ) {
            channel.send( "sender1_$it" )
            println("SENDER1 | sent $it in ${curThread()}")
        }
    }

    val sender2 = scope.launch {
        repeat( n ) {
            channel.send( "sender2_$it" )
            println("SENDER2 | sent $it in ${curThread()}")
        }
    }

    //delay(500) //The receiver starts after a while ...

    val receiver1 = scope.launch {
        for( i in 1..n ) {
            val v = channel.receive()
            println("RECEIVER1 |   $v in ${curThread()}")
            delay(100)  //a litle slower than receiver2
        }
    }

    val receiver2 = scope.launch {
        for( i in 1..n ) {
            val v = channel.receive()
            println("RECEIVER2 |  $v in ${curThread()}")
        }
    }
}


fun doDemoChannelTestOneSenderOneReceiver(){
    runBlocking {
        channelTest(this)
    }
}
fun doDemoChannelTestMany(){
    runBlocking {
        channelTestMany(this)
    }
}
fun main() {
    println("BEGINS CPU=$cpus ${curThread()}")
    doDemoChannelTestMany()
    println("ENDS main ${curThread()}")
}