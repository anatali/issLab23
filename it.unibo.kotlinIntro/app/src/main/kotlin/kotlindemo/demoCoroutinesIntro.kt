//demoCoroutinesIntro.kt

package kotlindemo

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


//import kotlinx.coroutines.io.parallelis.IO_PARALLELISM_PROPERTY_NAME
var thcounter=0
var maxNumThread = 0;

fun runBlockThread( delay : Long = 0L ){
//     run { //Calls a function block; returns its result
//       println("thread sleeps ... : ${curThread()}")
         Thread.sleep( delay )
		 val nt =  Thread.activeCount()
		  if( maxNumThread < nt )  maxNumThread = nt
         thcounter++
         //println("thread ends : ${curThread()} thcounter=${thcounter}")
//     } 
}

fun scopeDemo (){
	thcounter=0
	val scope = CoroutineScope( Dispatchers.Default )
	println( scope.coroutineContext )
	val job = scope.launch{
		println("start coroutine 1 ${curThread()}")
		runBlockThread(3000)
		println("end coroutine 1 ${curThread()}")
	}
	//job.join()  // should be called only from a coroutine or another suspend function
	scope.launch{
		println("start coroutine 2 ${curThread()}")
		job.join();
		println("end coroutine 2 ${curThread()}")
	}
}

val n=10000		//number of Thread or Coroutines to launch
val k=1000		//times an action is repeated by each Thread or Coroutine

fun manyThreads(){  
 	thcounter=0
 	val time = measureTimeMillis{
		val jobs = List(n){
			kotlin.concurrent.thread(start = true) { 
  				repeat( k ){ runBlockThread() }
			}
		}			
		jobs.forEach{ it.join()  }  //wait for termination of all threads
 	}
  	println("manyThreads time= $time thcounter=$thcounter maxNumThread=$maxNumThread ")
}

//Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()

fun manyCoroutines(){
	val d = newSingleThreadContext("single")
	//val d = newFixedThreadPoolContext(10,"d")
	//val d = Dispatchers.Default
	val scope = CoroutineScope( d  ) 
	thcounter=0
 	scope.launch{ 
	    val time = measureTimeMillis {
 	        val jobs = List(n) {
				//println("coroutine ${iter++} starts ")
 				scope.launch { repeat(k) { runBlockThread() } }
 			} 
			//println("manyCoroutines ENDS LANUCH ")
			jobs.forEach { it.join() }
			//println("manyCoroutines END ALL JOBS")
	    }
	    println("manyCoroutines time= $time  thcounter=$thcounter  maxNumThread=$maxNumThread ")
	}
}



fun demoRunBlocking1(){
	runBlocking {
		println("Before run2  ${curThread()}")
		val job =  launch{ runBlockThread(2000)  }
		println("Just after launch ${curThread()}")
		job.join()
		println("After job ${curThread()}")
	}
	println("Ends run2 ${curThread()}")
}

fun demoRunBlocking2(){
	runBlocking {
		println("Before run1 ${curThread()}")
		launch{  runBlockThread(2000)  }
		println("Just after launch ${curThread()}")
	}
	println("Ends run1 ${curThread()}")
}

fun scopeAsyncDemo (){
	val scope = CoroutineScope( Dispatchers.Default )
	val res = scope.async{	//type: Deferred<String>
		println("async starts")
		//Thread.sleep(3000)
		delay(2000)
		//println("async ends")
		"hello from async"
	}
	scope.launch{
		println("starts to wait result")
		val r = res.await(); //must be called only from a coroutine or a suspend function
		println("result= ${r}")
	}
}
//=================================================================
var demoTodo : () -> Unit = { println("nothing to do") }

fun readInt() : Int { print(">"); return readLine()!!.toInt() }

fun doDemo( input : Int ) = runBlocking{  
	println("BEGINS CPU=$cpus ${curThread()}")
	when( input ){
		1 ->  demoTodo =  { runBlockThread(1500 ) 	    }
		2 ->  demoTodo =  { thcounter=0;
			GlobalScope.launch{ runBlockThread(1500) }	}
		3 ->  demoTodo =  { scopeDemo()						    }
 		4 ->  demoTodo =  { maxNumThread = 0; manyThreads() 	} //9533085 9533634
  		5 ->  demoTodo =  { maxNumThread = 0; manyCoroutines()	}
		6 ->  demoTodo =  { demoRunBlocking1()             	    }
		7 ->  demoTodo =  { demoRunBlocking2()             	    }
		8 ->  demoTodo =  { scopeAsyncDemo()             	    }
		else ->  { println("command unknown") }  //Note the block
	} 			
	println( "work done in time= ${measureTimeMillis(  demoTodo )}"  )
	println("ENDS   ${curThread()}")
}

fun main() {
		var input =  readInt()
		while( input != 0 ){
			doDemo( input )
			demoTodo = 	{ println("nothing to do") }    
			input    =  readInt()
		}
  	    println( "BYE") 
}

     
