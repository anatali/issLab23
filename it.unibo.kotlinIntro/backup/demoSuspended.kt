package kotlindemo
//demoSuspended.kt
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.Job
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope

//------------------SUSPEND -----------------------------
suspend fun ioBoundFun(dt: Long=1000L) : Long{
    val timeElapsed = measureTimeMillis {
        println("ioBoundFun | dt=$dt STARTS in ${curThread()}")
        delay(dt)
    }
	val res = dt/10
    println("ioBoundFun | dt=$dt res=$res ${curThread()} TIME=$timeElapsed")
	return res
}

//------------------ASYNC -----------------------------
 fun activate(mainscope : CoroutineScope){
	val myscope = CoroutineScope( newSingleThreadContext("t1"))
	val job1 =  myscope.async {
        ioBoundFun(500L)
    }
    val job2 = myscope.async{
        ioBoundFun(300L)
    }
	mainscope.launch {
		if (!job1.isCompleted || !job2.isCompleted) println("Waiting for completion")
		val end1 = job1.await()    //only from a coroutine or another suspend
		val end2 = job2.await()
		println("All jobs done; end1=$end1 end2=$end2")
	}
}
 
//------------------ Wrap async calls with coroutineScope  ------------
//val job: Job = Job()
val job   = SupervisorJob()
val scope = CoroutineScope(Dispatchers.Default + job) 	//

fun asynchWork() : Deferred<String> =
	scope.async { //(1) launches new coroutine that may throw an unhandled exception
		println("asynchWork starts"); Thread.sleep(1000)
		 1/0 //force an exception
		"hello"
	}  
suspend fun asynchWorkWrapped() : String = coroutineScope{
	async { //(1) launches new coroutine that may throw an unhandled exception
		println("asynchWork starts"); Thread.sleep(1000)
		 1/0 //force an exception
		"hello"
	}.await()  
}

fun loadData() = scope.launch { 
	    try {
			println("loadData starts");
	        asynchWork().await()                               // (2) it will still crash
			println("loadData ends");
	    } catch (e: Exception) { println("loadData ERROR $e"); }
}

fun loadDataWrapped() = scope.launch { 
	    try {
			println("loadDataWrapped starts");
	        asynchWorkWrapped()                                // (2) it will still crash
			println("loadDataWrapped ends");
	    } catch (e: Exception) { println("loadData ERROR $e"); }
}
//the failure of any of the job�s children leads to an immediate failure of its parent

fun nowrapDemo(){
	asynchWork()
	loadData() 
}

suspend fun wrapDemo(){
	asynchWorkWrapped()
	loadDataWrapped() 
}


//suspend fun demoJoin(){
//	val job =  launch{ runBlockThread()  }
//}
//=================================================================

fun run1(){
	runBlocking {
		ioBoundFun()
	}
}

fun run2(){
	val myScope= CoroutineScope(newSingleThreadContext("single"))
	myScope.launch{ ioBoundFun(500L) }
	runBlocking { ioBoundFun() }
	myScope.launch{ ioBoundFun(300L) }
}

fun run3(){  runBlocking { activate(this)  }  }
fun main() {
	println("BEGINS CPU=$cpus ${curThread()}")
	//run1()
	//run2()
	run3()
	println("ENDS ${curThread()}")
}
     
      
