package kotlindemo

import kotlinx.coroutines.runBlocking

//------------------ CLOSURE -----------------------------
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

//------------------ NORMAL -----------------------------
fun showAction( msg: String ){
    println( msg )
}

fun readAction() : String{
    println("readaction  ... | ${curThread()}")
    return "myinput"
}
fun evalAction( v: Int, msg: String ) : String{
    println("evalaction ... | v=$v ${curThread()}")
    return "$msg:$v"
}

fun doJobNormal(n:Int){	//print- eval-read pattern
    showAction( evalAction( n, readAction() )  )
}

//------------------ CPS -----------------------------
fun readCps( callback: (String)->Unit ):Unit{
    println("readCps  ... | ${curThread()}")
    callback( "myinputcps" )
}
//------------------ CPS -----------------------------
fun evalCps(v:Int, msg:String, callback:(String)->Unit){
    println("evalCps ... | ${curThread()}")
    callback( "$msg:$v" )
}
fun doJobCpsNoShortcut( n: Int  ){
    readCps( //lambda
        { input : String-> evalCps( n, input, {  
              msg ->  showAction( msg )  
            } 
        )}//evalCps
     )//readCps	
}
fun doJobCps( n: Int  ){
     readCps{ evalCps( n, it) { showAction( it )}}   //lambda shortcut
} 

 

//=========================================================================
fun closureDemo(){
    val c1 = counterCreate()
	println("closureDemo --------------------- ")
	println( c1 )			 //(kotlin.String) -> kotlin.Int
	println( c1("val") ) 	//0
	val c2 = counterCreate()
    for( i in 1..3 ) c1("inc")
    println("c1=${c1("val")}")		  //c1=3
    for( i in 1..3 ) c2("dec")
	println("c2=${c2("val")}")	     //c2=-3
    println("doJobNormal --------------------------")
    doJobNormal( 10 )			//output : myinput:100

	println("callback      ----------------------- ") 
	//readCps( { msg -> showAction(msg) } )		//output : myinputcps
	readCps( { showAction(it) } )		//output : myinputcps
	
	println("doJobCpsNoShortcut      ----------------------- ")
    doJobCpsNoShortcut( 10 )
    //println("doJobCps      ----------------------- ")
    //doJobCps( 10  )
	
 }

fun main() {
    println("BEGINS CPU=$cpus ${curThread()}")
	
    println( "work done in time= ${measureTimeMillis(  { closureDemo() } )}"  )
	
    println("ENDS ${curThread()}")
}

