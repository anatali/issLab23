package kotlindemo
//demoDispatcherPatterns

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.newSingleThreadContext
//import kotlinx.coroutines.Dispatchers.ExecutorCoroutineDispatcher


@ObsoleteCoroutinesApi
val th = newSingleThreadContext("My Thread")

fun action(i : Int) { println("hello $i ${curThread()}") }

suspend fun launchWithDefault(i:Int, scope:CoroutineScope){
    scope.launch( Dispatchers.Default ){ action(i) }
}
suspend fun launchWithIO(i:Int,scope:CoroutineScope){
    scope.launch( Dispatchers.IO ){ action(i) }
}

@ObsoleteCoroutinesApi
suspend fun launchWithSingle(i:Int,scope:CoroutineScope){
    scope.launch( th ){ action(i) }
}

@ObsoleteCoroutinesApi
fun main() = runBlocking {
	println("BEGINS CPU=$cpus ${curThread()}")

 	//println(" --------- launchWithDefault")
     //for (i in 1..6) launchWithDefault(i,this)
    /*
 	println(" --------- launchWithIO")
      for (i in 1..10) launchWithIO(i,this)
*/
 	println(" --------- launchWithSingle")
     for (i in 1..6) launchWithSingle(i,this) 

	println("ENDS ${curThread()}")
}