/*
-------------------------------------
demo2022.kt
-------------------------------------
*/
package demo22
import kotlinx.coroutines.*
import unibo.basicomm23.utils.CommUtils
/*
-------------------------------------------
Utilities
-------------------------------------------
 */
val cpus = Runtime.getRuntime().availableProcessors()

fun curThread() : String {
    return "thread=${Thread.currentThread().name} / nthreads=${Thread.activeCount()}"
}
inline fun measureTimeMillis(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}
/*
-----------------------------------------------------------
User interface per le demo
-----------------------------------------------------------
 */
val choiche = arrayOf("demoBaseFunzioni","demoLambda","demoCps","demoAsynchCps",
    "runBlockThread","runBlockThreadInGlobalScope",
    "runBlockingLaunchJoin", "runBlockingLaunchNoJoin",
    "scopeDemo","runInNewScope",
    "manyThreads","manyCoroutines","scopeAsyncDemo",
     "ioBoundFunCallBlocking","ioBoundFunCallnewSingleThreadContext",
     "ioBoundFunCallActivate",
    "demoChannelTestOneSendRec","demoChannelTestMany",
    "manyTypeProducerOnChannel", "manyConsumers",
     "actorsSenderReceiver", "doCounterActor"
       )

var demoTodo : () -> Unit = { println("nothing to do") }

/*
---------------------------------------------
main
---------------------------------------------
 */
fun main() {
    showChoices()
    var input =  readInt()
    while( input != 0 ){
        doDemo( input )
        demoTodo = 	{ println("nothing to do") }
        showChoices()
        input    =  readInt()
    }
    println( "BYE")
}

fun readInt() : Int { print(">"); return readLine()!!.toInt() }
fun showChoices(){
    var n=1
    println("-------------------------------------------")
    choiche.forEach{ println("$n:$it"); n=n+1 }
    println("-------------------------------------------")
}

//DEMO user interface
fun doDemo( input : Int ){
    println("doDemo BEGINS CPU=$cpus ${curThread()} " )

    when( input ){
        1 ->  demoTodo =  { demoBaseFunzioni() }
        2 ->  demoTodo =  { demoLambda() }          //From demoForLambda
        3 ->  demoTodo =  { demoCps() }             //From demoForCps
        4 ->  demoTodo =  { demoAsynchCps() }
        5 ->  demoTodo =  { runBlockThread() }
        6 ->  demoTodo =  { runBlockThreadInGlobalScope() }
        7 ->  demoTodo =  { runBlockingLaunchJoin() }
        8 ->  demoTodo =  { runBlockingLaunchNoJoin() }
        9 ->  demoTodo =  { scopeDemo() }
        10 ->  demoTodo =  { runInNewScope() }
        11 ->  demoTodo =  { manyThreads() }
        12 ->  demoTodo =  { runBlocking{ manyCoroutines()  } }
        13 ->  demoTodo =  { scopeAsyncDemo() }
        14 ->  demoTodo =  { ioBoundFunCallBlocking() }
        15 ->  demoTodo =  { ioBoundFunCallnewSingleThreadContext() }
        16 ->  demoTodo =  { ioBoundFunCallActivate() }
        //In demoChannels.kt
        17 ->  demoTodo =  { kotlindemo.doDemoChannelTestOneSenderOneReceiver() }
        18 ->  demoTodo =  { kotlindemo.doDemoChannelTestMany() }
        //In prodConsKotlin.kt
        19 ->  demoTodo =  { prodCons.manyTypeProducerOnChannel() }
        20 ->  demoTodo =  { prodCons.manyConsumers() }
        //In demoActors
        21 ->  demoTodo =  { kotlindemo.actorsSenderReceiver() }
        //22 ->  demoTodo =  { kotlindemo.actorsSenderReceiver() }
        //IN demoActorCounter.kt
        22 ->  demoTodo =  { kotlindemo.doCounterActor() }
        else ->  { println("command unknown") }  //Note the block
    }
    println( "doDemo work done in time= ${measureTimeMillis(  demoTodo )}"  )
    println("doDemo ENDS ${curThread()}")
}



/*
-----------------------------------------------------------
Funzioni con le demo
-----------------------------------------------------------
 */

fun demoBaseFunzioni(){     //(1)
    println("-- DEMOBASE funzioni")
    //println( fun(){ println("Hello-anonymous")}   ) //Function0<kotlin.Unit>
    //println( fun(){ println("Hello-anonymous")}()   ) //Hello-anonymous e poi kotlin.Unit
    val ftgreetCallResult = ftgreet("Hello Greeting")()//side effect: Hello Greeting
    println( "ftgreetCallResult=$ftgreetCallResult" ) //kotlin.Unit
}
/*
SCOPO: Mostrare i shortcut delle lambda expr in modo incrementale
 */
fun demoLambda() {     //(2)
    println("-- DEMOLAMBDA")
    val v1 = exec23( "no shortcut", { x:Int, y:Int -> x-y } ) //1) no shortcut
    println("v1=$v1")	      //no shortcut v1=-1
    val v2 = exec23("lambda last arg"){ x:Int, y:Int -> x-y }  //2) lambda last arg
    println("v2=$v2")	      //lambda last arg v2=-1
    val v3 = exec23{ x:Int, y:Int -> x-y } //3) () can be removed
    println("v3=$v3")	      //allOk v3=-1
    val v4 = exec23{ x,y -> x-y } //4) arg types inferred
    println("v4=$v4")	      //allOk v4=-1

    val v5 = p2{ x -> x - 18 / 9 } //4) arg types inferred
    println("v5=$v5")	      // v5=0
    val v6 = p2{ it - 18 / 9 } //4) USING it
    println("v6=$v6")	      // v6=0
    val v7 = p2{ it + 18 } / 2  //4) arg types inferred
    println("v7=$v7")	      // v7=10

    var arr = arrayOf(1,2,3)
    arr.forEach { print("$it"); println() }	//1 2 3
}

fun demoCps(){   //(3)
    doReadEvalPrintNormal(10) //myinput:10
    //readCps( { msg -> showAction(msg)  } ) //myinputcps
    //readCps( {showAction(it)  } )//myinputcps | using  lambda  shortcut
    println("------ from normal to cps ")
    doReadEvalPrintCps(10) //myinputcps: 10
}

fun demoAsynchCps() {  //(4)
    doJobAsynchCps( 10  )
}
//ATTENZIONE al risultato di demoAsynchCps
/*
doDemo BEGINS CPU=12 thread=main / nthreads=2
doJobAsynchCps | Here I can do other jobs ...
readCpsAsynch  ... | thread=Thread-0 / nthreads=3
doDemo work done in time= 2
doDemo ENDS   thread=main / nthreads=3  (3 e non 2)
----------------------------------------------
....
----------------------------------------------
>readCpsAsynch done
evalCps working with v=10 | thread=Thread-0 / nthreads=3
myinputasynchcps: 10
*/

/*
-----------------------------------------------------------
Funzioni usate nelle demo
-----------------------------------------------------------
 */
/* Funzione  */
fun fsum(a:Int, b:Int) : Int {  return a+b  }
/* Funzione one line */
fun fsquare(v: Int) = v * v
/* Function type inizializzato da Lambda Expr*/
val ftaction : () -> Unit = { println("ftaction") }
//Funzione che restituisce una funzione
val ftgreet: (String ) -> ()->Unit = {  m: String -> { println(m)}   }
//Funzione che riceve funzione
fun exec23( msg : String="allOk", op:(Int,Int) -> Int ) : Int {
    println(msg);
    return op(2,3)
}
//Funzione che riceve funzione a un solo argomento
fun p2( op:( Int ) -> Int) : Int {
    //println(op);
    return op(2)
}
//Funzione usate come chiusura lessicale
fun counterCreate()  : ( cmd : String ) -> Int {
    var localCounter = 0
    return { msg ->
        when (msg) {
            "inc" -> ++localCounter
            "dec" -> --localCounter
            "val" -> localCounter
            else -> throw Exception( "unknown" )
        }
    }
}

/*
--------------------------------
Funzioni read-eval-print
--------------------------------
 */

fun showAction( msg: String ){  println( msg ) }
fun readAction() : String{
    println("readaction  ... | ${curThread()}")
    return "myinput"	//SIMULATE to acquire input
}
fun evalAction( v: Int, msg: String ) : String{
    println("evalaction ... | v=$v ${curThread()}")
    return "$msg:$v"
}
fun doReadEvalPrintNormal(n:Int){	//1) read 2) eval 3) print
    showAction( evalAction( n,readAction() )  )
}
/*
---------------------------------------------
Funzioni rread-eval-print in stile CPS
---------------------------------------------
 */
fun readCps( callback:( String )-> Unit ) :Unit {
    println("readCps  ... | ${curThread()}")
    callback( "myinputcps" )
}
fun evalCps(v:Int, msg:String, callback:(String)-> Unit ){
    println("evalCps working with v=$v  | ${curThread()}")
    callback( "$msg: $v" )
}
fun doReadEvalPrintCps( n: Int  ){ //using lambda shortcut
    readCps{ evalCps( n, it) { showAction( it )} }  //read-and-after-do
    //read works, than calls eval that works and calls showAction
}

/*
---------------------------------------------
ASYNCHRONOUS PROGRAMMING WITH CPS
---------------------------------------------
 */
//Attiva un Thread che simula una Long-term action
fun readCpsAsynch( callback:(String)-> Unit ) : Unit{
    //SAM: when an object implements a SAM interface,
    //we can pass a lambda instead.
    kotlin.concurrent.thread(start = true) {
        println("readCpsAsynch  ... | ${curThread()} ")
        Thread.sleep(3000)	//Long-term action
        println("readCpsAsynch done")
        callback( "readCpsAsynch done" )
    }
}

fun doJobAsynchCps( n: Int  ){
    readCpsAsynch{ evalCps( n, it) { showAction( it )}}
    println("doJobAsynchCps | Here I can do other jobs ... ")
}

/*
---------------------------------------------
Coroutines
---------------------------------------------
 */
var thcounter=0

//Esegue una delay all'interno del Thread corrente
fun runBlockThread( delay : Long = 1000L ){  //(5)
    CommUtils.outyellow("runBlockThread in ${curThread()} STARTS thcounter=${thcounter}")
    Thread.sleep(delay)
    thcounter++  //thcounter = thcounter + 1 NON ATOMICA
    CommUtils.outyellow("runBlockThread in ${curThread()} ENDS thcounter=${thcounter}")
}
//Si noti: runBlockThread in thread=DefaultDispatcher-worker-1

//Lancia una delay all'interno di una coroutine
fun runBlockThreadInGlobalScope(){  //(6)
    GlobalScope.launch { runBlockThread() }
}
/* RISULTATO di  runBlockThreadInGlobalScope CON o SENZA runBlocking
doDemo BEGINS CPU=8 thread=main / nthreads=2
doDemo work done in time= 65
doDemo ENDS thread=main / nthreads=5
------------------------------
...
------------------------------
>runBlockThread in thread=DefaultDispatcher-worker-1 / nthreads=5 STARTS thcounter=0
runBlockThread in thread=DefaultDispatcher-worker-1 / nthreads=5 ENDS thcounter=1
 */

fun runBlockingLaunchJoin(){  //(7)
    runBlocking {
        thcounter = 0;
        CommUtils.outblue("runBlockingLaunchJoin Before launch  ${curThread()}")
        val job =  launch{ runBlockThread(2000)  }
        CommUtils.outblue("runBlockingLaunchJoin Just after launch ${curThread()}"  )
        job.join()
        CommUtils.outblue("runBlockingLaunchJoin After job ${curThread()} thcounter=$thcounter")
    }
    //the coroutine is launched in the scope of the outer runBlocking coroutine
    CommUtils.outblue("runBlockingLaunchJoin After runBlocking ${curThread()}")
}
fun runBlockingLaunchNoJoin(){	//(8)
    //runBlocking {
        val scope = CoroutineScope( Dispatchers.Default )
        CommUtils.outblue("runBlockingLaunchNoJoin Before run ${curThread()}")
        val job = scope.launch{  runBlockThread(2000)  }
        CommUtils.outblue("runBlockingLaunchNoJoin Just after launch ${curThread()}")
        //job.join()
        //CommUtils.outblue("runBlockingLaunchJoin After job ${curThread()} thcounter=$thcounter")
   // }
    //The runBlocking won't complete before all of its child coroutines finish.
    CommUtils.outblue("runBlockingLaunchNoJoin AT END ${curThread()}")
}
//runBlocking non influisce se usiamo un altro scope

/*
---------------------------------------------
Scope
---------------------------------------------
 */
fun scopeDemo (){  //(19)
    thcounter=0
    val scope = CoroutineScope( Dispatchers.Default )
    CommUtils.outmagenta( "scopeDemo ${scope.coroutineContext}" )
    runBlocking {
        val job = scope.launch {
            CommUtils.outblue("start coroutine 1 ${curThread()}")
            runBlockThread(2000)
            //delay( 2000 )
            CommUtils.outblue("end coroutine 1 ${curThread()}")
        }
    CommUtils.outcyan("scopeDemo job $job")
        //job.join() should be called only from a coroutine or another suspend function
        scope.launch {
            job.join()
            CommUtils.outgreen("start coroutine 2 ${curThread()}")
            CommUtils.outgreen("end coroutine 2 ${curThread()}")
        }
    }//runBlocking
}

fun workTodo(i : Int) { println("hello $i ${curThread()}") }

suspend fun runInScope(  //(10)
    //scope:CoroutineScope=CoroutineScope(Dispatchers.IO)
    scope:CoroutineScope=CoroutineScope(newSingleThreadContext("single"))){
    var job = mutableListOf<Job>()
    for (i in 1..6){
        job.add( scope.launch{ delay(1000L*i); workTodo(i) } )
    }
    job.forEach { it.join() } //Provare a togliere questo join
}

fun runInNewScope() {
    runBlocking {
        CommUtils.outmagenta("Run in new scope ")
        runInScope()
    }
}

/*
The async builder returns a promise (also known as future),
(which is of type Deferred in Kotlin): it promises to compute a value which
we can wait for or request at any time.
The method await on the promise allows us to get the value.

Una coroutine attivata con async produce un valore (res)
sul quale attende un'altra coroutine
 */
fun scopeAsyncDemo(){ //(13)
    val scope = CoroutineScope( Dispatchers.Default )
    val res : Deferred<String>   = scope.async{
        CommUtils.outcyan("async starts")
            delay(2000)
            CommUtils.outcyan("async produces the output")
            "hello from async"
    }
    scope.launch{
        CommUtils.outblue("receiver starts to wait result")
        val r = res.await();
        //must be called only from a coroutine or a suspend function
        CommUtils.outblue("receiver result= ${r}")
    }
}
/*
---------------------------------------------
Many threads vs. many coroutines
---------------------------------------------
 */
val n=10000	//number of Thread or Coroutines to launch
val k=1000	//times an action is repeated by each Thread or Coroutine
//n*k = 10000000
var maxNumThread = 0;
fun incGlobalCounter(   ){
    val nt =  Thread.activeCount()
    if( maxNumThread < nt )  maxNumThread = nt
    thcounter++
}

fun manyThreads(){  //(11)
    thcounter=0
    maxNumThread = 0
    val time = measureTimeMillis{
        val jobs = List(n){
            kotlin.concurrent.thread(start = true) {
                repeat( k ){ incGlobalCounter() }
            }
        }
        jobs.forEach{it.join()} //wait for termination of all threads
    }
    println("manyThreads time= $time thcounter=$thcounter maxNumThread=$maxNumThread")
}

suspend fun manyCoroutines(){	//(12)
    val d = newSingleThreadContext("single")
    //val d = newFixedThreadPoolContext(10,"d")
    //val d = Dispatchers.Default
    val scope = CoroutineScope( d )
    thcounter=0
    maxNumThread=0
    val time = measureTimeMillis {
        val jobs = List(n) {
            scope.launch{ repeat(k) { incGlobalCounter() } }
        }
        jobs.forEach { it.join() } //wait for termination of all coroutines
    }
    println("manyCoroutines time= $time counter=$thcounter maxNumThread=$maxNumThread")
}

/*
---------------------------------------------
Suspending functions
---------------------------------------------
 */
//To be used later
//Simula una funzione che fa io e quindi ci mette tempo
suspend fun ioBoundFun(dt: Long = 1000L) : Long{
    val time =  measureTimeMillis{
        CommUtils.outblue("ioBoundFun | dt=$dt STARTS in ${curThread()}")
        delay(dt)
    }
    val res = dt/10
    CommUtils.outblue("ioBoundFun | dt=$dt res=$res ${curThread()} TIME=$time")
    return res
}
fun ioBoundFunCallBlocking(){ //(14)
    runBlocking { ioBoundFun() }
}


fun ioBoundFunCallnewSingleThreadContext(){   //(15)
    val myScope=CoroutineScope(newSingleThreadContext("single"))
    myScope.launch{ ioBoundFun(1500L) }
    runBlocking { ioBoundFun() }
    myScope.launch{ ioBoundFun(300L) }
}

/*
The async coroutine builder creates new coroutine and returns a promise,
(of type Deferred in Kotlin): it promises to compute a value which
we can wait for or request at any time.
 */
//To be used later
fun activate(mainscope : CoroutineScope){
    CommUtils.outmagenta("uso un mainscope ricevuto come arg")
    CommUtils.outmagenta("creo un myscope con newSingleThreadContext ")
    val myscope = CoroutineScope( newSingleThreadContext("t1"))
    CommUtils.outcyan("creo due coroutine con async in myscope ")
    val job1    =  myscope.async { ioBoundFun(500L) }
    val job2    =  myscope.async{  ioBoundFun(300L) }
    CommUtils.outcyan("le due coroutine vanno in sequenza")
    CommUtils.outblue("lancio una nuova coroutine usando il mainscope")
    CommUtils.outblue("la coroutine attende i risultati ")
    mainscope.launch {
        if (!job1.isCompleted || !job2.isCompleted) println("Waiting for completion")
        val end1 = job1.await() //only from a coroutine or another suspend
        val end2 =  job2.await()
        CommUtils.outmagenta("All jobs done; end1=$end1 end2=$end2")
    }
}
fun ioBoundFunCallActivate(){   //(16)
    runBlocking{ activate(this)  }
}

