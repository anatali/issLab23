import kotlinx.coroutines.*


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
     "ioBoundFunCallBlocking","ioBoundFunCallnewSingleThreadContext","ioBoundFunCallActivate",
    "demoChannelTestOneSendRec","demoChannelTestMany",
    "manyTypeProducerOnChannel", "manyConsumers",
     "actorsSenderReceiver", "doCounterActor"
       )

var demoTodo : () -> Unit = { println("nothing to do") }
fun readInt() : Int { print(">"); return readLine()!!.toInt() }
fun showChoices(){
    var n=1
    println("-------------------------------------------")
    choiche.forEach{ println("$n:$it"); n=n+1 }
    println("-------------------------------------------")
}

//DEMO user interface
fun doDemo( input : Int ){
    println("BEGINS CPU=$cpus ${curThread()} " )

    when( input ){
        1 ->  demoTodo =  { demoBaseFunzioni() }
        2 ->  demoTodo =  { demoLambda() }
        3 ->  demoTodo =  { demoCps() }
        4 ->  demoTodo =  { demoAsynchCps() }
        5 ->  demoTodo =  { runBlockThread() }
        6 ->  demoTodo =  { runBlockThreadInGlobalScope() }
        7 ->  demoTodo =  { runBlockingLaunchJoin() }
        8 ->  demoTodo =  { runBlockingLaunchNoJoin() }
        10 ->  demoTodo =  { runInNewScope() }
        11 ->  demoTodo =  { manyThreads() }
        12 ->  demoTodo =  { runBlocking{ manyCoroutines()  } }
        13 ->  demoTodo =  { scopeAsyncDemo() }
        14 ->  demoTodo =  { ioBoundFunCallBlocking() }
        15 ->  demoTodo =  { ioBoundFunCallnewSingleThreadContext() }
        16 ->  demoTodo =  { ioBoundFunCallActivate() }
        17 ->  demoTodo =  { kotlindemo.doDemoChannelTestOneSenderOneReceiver() }
        18 ->  demoTodo =  { kotlindemo.doDemoChannelTestMany() }
        19 ->  demoTodo =  { prodCons.manyTypeProducerOnChannel() }
        20 ->  demoTodo =  { prodCons.manyConsumers() }
        21 ->  demoTodo =  { kotlindemo.actorsSenderReceiver() }
        //22 ->  demoTodo =  { kotlindemo.actorsSenderReceiver() }
        22 ->  demoTodo =  { kotlindemo.doCounterActor() }
        else ->  { println("command unknown") }  //Note the block
    }
    println( "work done in time= ${measureTimeMillis(  demoTodo )}"  )
    println("ENDS ${curThread()}")
}

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

/*
-----------------------------------------------------------
Funzioni con le demo
-----------------------------------------------------------
 */

fun demoBaseFunzioni(){
    println("-- DEMOBASE funzioni")
    //println( fun(){ println("Hello-anonymous")}   ) //Function0<kotlin.Unit>
    //println( fun(){ println("Hello-anonymous")}()   ) //Hello-anonymous e poi kotlin.Unit
    val ftgreetCallResult = ftgreet("Hello Greeting")()//side effect: Hello Greeting
    println( "ftgreetCallResult=$ftgreetCallResult" ) //kotlin.Unit
}
fun demoLambda() {
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
    println("v7=$v7")	      // v7=-10

    var arr = arrayOf(1,2,3)
    arr.forEach { print("$it"); println() }	//1 2 3
}

fun demoCps(){
    doReadEvalPrintNormal(10) //myinput:10
    //readCps( { msg -> showAction(msg)  } ) //myinputcps
    //readCps( {showAction(it)  } )//myinputcps | using  lambda  shortcut
    println("------ from normal to cps ")
    doReadEvalPrintCps(10) //myinputcps: 10
}

fun demoAsynchCps() {
    doJobAsynchCps( 10  )
    /*
    BEGINS CPU=12 thread=main / nthreads=2
    Here I can do other jobs ...
    -- demo22 END
    readCpsAsynch  ... | thread=Thread-0 / nthreads=3
    work done in time= 3
    ENDS thread=main / nthreads=3
    readCpsAsynch done
    evalCps ... | thread=Thread-0 / nthreads=3
    myinputasynchcps: 10
     */
}

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
fun exec23( msg : String="allok", op:(Int,Int) -> Int ) : Int {
    println(msg); return op(2,3) }
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
//Funzioni rread-eval-print
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
//Funzioni rread-eval-print in stile CPS
fun readCps( callback:( String )-> Unit ) :Unit {
    println("readCps  ... | ${curThread()}")
    callback( "myinputcps" )
}
fun evalCps(v:Int, msg:String, callback:(String)-> Unit ){
    println("evalCps ... | ${curThread()}")
    callback( "$msg: $v" )
}
fun doReadEvalPrintCps( n: Int  ){ //using lambda shortcut
    readCps{ evalCps( n, it) { showAction( it )} }  //read-and-after-do
    //read works, than calls eval that works and calls showAction
}
//ASYNCHRONOUS PROGRAMMING WITH CPS
fun readCpsAsynch( callback:(String)-> Unit ) : Unit{
    //SAM: when an object implements a SAM interface, we can pass a lambda instead.
    kotlin.concurrent.thread(start = true) {
        println("readCpsAsynch  ... | ${curThread()} ")
        Thread.sleep(3000)	//Long-term action
        println("readCpsAsynch done")
        callback( "myinputasynchcps" )
    }
}
fun doJobAsynchCps( n: Int  ){
    readCpsAsynch{ evalCps( n, it) { showAction( it )}}
    println("Here I can do other jobs ... ")
}
//Coroutines
var thcounter=0
fun runBlockThread( delay : Long = 1000L ){
    Thread.sleep(delay)
    thcounter++  //thcounter = thcounter + 1 NON ATOMICA
    //println("thread ends : ${curThread()} thcounter=${thcounter}")
}
fun runBlockThreadInGlobalScope(){
    GlobalScope.launch{ runBlockThread() }
}
fun runBlockingLaunchJoin(){
    runBlocking {
        thcounter = 0;
        println("Before run2  ${curThread()}")
        val job =  launch{ runBlockThread(2000)  }
        println("Just after launch ${curThread()}"  )
        job.join()
        println("After job ${curThread()} thcounter=$thcounter")
    }
    //the coroutine is launched in the scope of the outer runBlocking coroutine.7
    println("Ends runBlockingLaunchJoin ${curThread()}")
}
fun runBlockingLaunchNoJoin(){	//user-option 7
    runBlocking {
        println("Before run1 ${curThread()}")
        launch{  runBlockThread(2000)  }
        println("Just after launch ${curThread()}")
    }
    //The runBlocking won't complete before all of its child coroutines finish.
    println("Ends runBlockingLaunchNoJoin ${curThread()}")
}

fun scopeDemo (){	//user-option 3
    thcounter=0
    val scope = CoroutineScope( Dispatchers.Default )
    println( scope.coroutineContext )
    val job = scope.launch{
        println("start coroutine 1 ${curThread()}")
        runBlockThread(3000)
        println("end coroutine 1 ${curThread()}")
    }
    //job.join()
    // should be called only from a coroutine or another suspend function
    scope.launch{
        println("start coroutine 2 ${curThread()}")
        job.join()
        println("end coroutine 2 ${curThread()}")
    }

}

fun workTodo(i : Int) { println("hello $i ${curThread()}") }

suspend fun runInScope(
    //scope:CoroutineScope=CoroutineScope(Dispatchers.IO)
    scope:CoroutineScope=CoroutineScope(newSingleThreadContext("single"))){
    var job = mutableListOf<Job>()
    for (i in 1..6){
        job.add( scope.launch{ delay(1000L/i); workTodo(i) } )
    }
    job.forEach { it.join() }
}

fun runInNewScope() {
    runBlocking {
        println("Run in new scope ")
        runInScope()
    }
}


//
val n=10000	//number of Thread or Coroutines to launch
val k=1000	//times an action is repeated by each Thread or Coroutine
//n*k = 10000000
var maxNumThread = 0;
fun incGlobalCounter(   ){
    val nt =  Thread.activeCount()
    if( maxNumThread < nt )  maxNumThread = nt
    thcounter++
}

fun manyThreads(){  //user-option
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

suspend fun manyCoroutines(){	//user-option 5
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

fun scopeAsyncDemo(){
    val scope = CoroutineScope( Dispatchers.Default )
    val res : Deferred<String>   = scope.async{
        println("async starts")
        delay(2000) //See delay
        "hello from async"
    }
    scope.launch{
        println("starts to wait result")
        val r = res.await();
        //must be called only from a coroutine or a suspend function
        println("result= ${r}")
    }
}

//Dispatechers (TODO)
fun testDispatchers(n : Int, scope: CoroutineScope) {
    if( n== 1 ){
        runBlocking {
            launch { //context of the parent runBlocking
                delay(500)
                println("1_a) runBlocking | ${curThread()}")
            }
            launch { //context of the parent runBlocking
                println("1_b) runBlocking | ${curThread()}")
            }
        }
    }
    if( n== 2 ) {
        val dispatcher = Dispatchers.Default
        scope.launch(dispatcher) {
            delay(500)
            println("2_a) Default | ${curThread()}")
        }
        scope.launch(dispatcher) {
            println("2_b) Default | ${curThread()}")
        }
    }
    if( n== 3 ){
        val dispatcher = newSingleThreadContext("MyThr")
        scope.launch( dispatcher ) {
            delay(500)
            println("3-a) newSingleThreadContext | ${curThread()}")
        }
        scope.launch( dispatcher ) {
            println("3-b) newSingleThreadContext | ${curThread()}")
        }
    }
    if( n== 4 ) {
        val dispatcher = Dispatchers.IO
        scope.launch(dispatcher) {
            delay(500)
            println("4_a) Dispatchers.IO | ${curThread()}")
        }
        scope.launch(dispatcher) {
            println("4_b) Dispatchers.IO | ${curThread()}")
        }
    }
    if( n== 5 ) {
        val dispatcher = Dispatchers.Unconfined
        scope.launch(dispatcher) {
            delay(500)
            println("5_a) Unconfined | ${curThread()}")
        }
        scope.launch(dispatcher) {
            println("5_b) Unconfined | ${curThread()}")
        }
    }
    if( n== 6 ) { //Working in a new scope and in the given one
        val myscope = CoroutineScope(Dispatchers.Default)
        scope.launch { delay(1000); println("just to avoid premature main end") }
        val job1 = myscope.launch {
            delay(500)
            println("2_a) Default | ${curThread()}")
        }
        myscope.launch {
            //job1.join()
            println("2_b) Default | ${curThread()}")
        }
    }
    if( n== 7 ) {  //Working in a new scope only
        val myscope = CoroutineScope(Dispatchers.Default)
        myscope.launch{
            delay(500)
            println("2_a) Default | ${curThread()}")
        }
        myscope.launch{
            println("2_b) Default | ${curThread()}")
        }
    }
}

//Suspending functions
suspend fun ioBoundFun(dt: Long = 1000L) : Long{
    val time =  measureTimeMillis{
        println("ioBoundFun | dt=$dt STARTS in ${curThread()}")
        delay(dt)
    }
    val res = dt/10
    println("ioBoundFun | dt=$dt res=$res ${curThread()} TIME=$time")
    return res
}
fun ioBoundFunCallBlocking(){ runBlocking { ioBoundFun() } }
fun ioBoundFunCallnewSingleThreadContext(){
    val myScope=CoroutineScope(newSingleThreadContext("single"))
    myScope.launch{ ioBoundFun(500L) }
    runBlocking { ioBoundFun() }
    myScope.launch{ ioBoundFun(300L) }
}

/*
The async coroutine builder creates new coroutine and returns a promise,
(of type Deferred in Kotlin): it promises to compute a value which
we can wait for or request at any time.
 */
fun activate(mainscope : CoroutineScope){
    val myscope = CoroutineScope( newSingleThreadContext("t1"))
    val job1 =  myscope.async { ioBoundFun(500L) }
    val job2 =  myscope.async{  ioBoundFun(300L) }
    mainscope.launch {
        if (!job1.isCompleted || !job2.isCompleted) println("Waiting for completion")
        val end1 = job1.await() //only from a coroutine or another suspend
        val end2 =  job2.await()
        println("All jobs done; end1=$end1 end2=$end2")
    }
}
fun ioBoundFunCallActivate(){ runBlocking{ activate(this)  }}


//CHANNEL test : in demoChannels.kt
//CHANNEL ProdCons : in simpleProducerKotlin.kt
//CHANNEL Many-type producer : in prodConsKotlin.kt

//ACTORS sender-receiver: in  demoActors.kt
//ACTORS actorcounter: in  demoActorCounter.kt.