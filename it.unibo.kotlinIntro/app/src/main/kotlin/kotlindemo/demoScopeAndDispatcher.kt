package kotlindemo
//demoScopeAndDispatcher
import kotlinx.coroutines.*

@ObsoleteCoroutinesApi
fun workTodo(i : Int) { println("hello $i ${curThread()}") }

suspend fun runInScope(
	//scope:CoroutineScope=CoroutineScope(Dispatchers.IO)
	scope:CoroutineScope=CoroutineScope(newSingleThreadContext("single"))
){
	var job = mutableListOf<Job>()
	for (i in 1..6){
		job.add( scope.launch{ delay(1000L/i); workTodo(i) } )
	}
	job.forEach { it.join() }
}


@ObsoleteCoroutinesApi
fun main() = runBlocking {
	println("BEGINS CPU=$cpus ${curThread()}")
	//println("Run in current context ")
	//runInScope( this )
	println("Run in new scope ")
	runInScope(   )
	println("ENDS ${curThread()}")
}