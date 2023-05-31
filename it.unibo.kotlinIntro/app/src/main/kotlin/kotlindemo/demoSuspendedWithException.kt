package kotlindemo
//demoSuspendedWithException.kt
import kotlinx.coroutines.*

//------------------ Wrap async calls with coroutineScope  ------------
val job   = SupervisorJob()
val scope = CoroutineScope(Dispatchers.Default + job) 	//

fun asynchWork(scope: CoroutineScope) : Deferred<String> =
	scope.async { //(1) launches new coroutine that may throw an unhandled exception
		println("asynchWork starts");
		Thread.sleep(1000)
		 1/0 //force an exception
		"hello"
	}  
suspend fun asynchWorkWrapped() : String = coroutineScope{
	async { //(1) launches new coroutine that may throw an unhandled exception
		println("asynchWork starts");
		delay(1000)
		 1/0 //force an exception
		"hello"
	}.await()  
}

fun loadData(scope: CoroutineScope) =
	scope.launch {
	    try {
			println("loadData starts");
	        asynchWork(scope).await()  // (2) it will still crash
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

fun nowrapDemo(scope:CoroutineScope){
	asynchWork(scope)
	loadData( scope )
}

suspend fun wrapDemo(){
	asynchWorkWrapped()
	loadDataWrapped() 
}


fun main() {
	println("BEGINS CPU=$cpus ${curThread()}")
	runBlocking {
		nowrapDemo(this)
	}
	println("ENDS ${curThread()}")
}
