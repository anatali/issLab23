package kotlindemo
//demoLambda.kt
fun add( x:Int, y:Int ) : Int { return x+y }

fun exec23( op:(Int,Int) -> Int ) : Int { return op(2,3) }
fun p2( op: ( Int ) -> Int )      : Int { return op(2) }

fun modulo(k:Int): 	(Int) -> Int     //SIGNATURE 
            = { it % k  }               //BODY 

fun sToN( s: String, base: Int=10 ) : Int{
    var v = 0
    for( i in 0..s.length-1 ) {
        v = ( s[i].toInt()-48 ) + v*base
    }
    return v
}

fun mirror(v: Int) : Pair<Int,Int> {
	return Pair(v, -v)
}
  
fun testRunFunction() {
    val logo = "Starting"
    val v1 = run{ 
        println("First start:$logo  ${curThread()}")
        val logo = "First"	//local
        println("First ended : $logo")
    }
    val v2 = run {
        println("Second start:$logo ${curThread()}")
        val logo = "Second"	//local
        println("Second ended: $logo ")
    }
    println("logo=$logo v1=$v1 v2=$v2" )
}

//===================================================================
fun lambdaDemoWork() {
	val v1 = exec23( { x:Int, y:Int -> x-y } ) //no shortcut
    println("v1=$v1")   //-1
	
	val v2 = exec23() { x:Int, y:Int -> x-y } //lamda is last arg
    println("v2=$v2")
	
	val v3 = exec23{ x:Int, y:Int -> x-y } //() can be removed
    println("v3=$v3")
	
	val v4 = exec23{ x,y -> x-y } //arg types inferred
    println("v4=$v4")

println("it    -------------- ")
    println( p2{ it -> it  } )      //2
    println( p2{ it  } )   			 //2
	println( p2{ it -> it * it } )   //4
    println( p2{ it - 18 / 9 } )     //0
    println( p2{ it - 18 }  / 9 )     //-1



    println("modulo -------------- ")
	println(  modulo(3)  ) 	 //Function1<java.lang.Integer, java.lang.Integer>
	println(  modulo(3)(5) ) 		//2
	println(  modulo(5)(3) )		//3

println("params -------------- ")	
	println( "${ sToN( s="123") }" ) 		    //123
    println( "${ sToN( s="123", 8) }" ) 	//83
	val v = mirror(2)
	println("${v.first}, ${v.second} ")  	//2,-2
	val(pos,neg) = mirror(3)
	println( "pos=$pos neg=$neg " ) 		//pos=3 neg=-3

println("function ref -------- ")	
	val sToNref = ::sToN
	println( sToNref )	//fun sToN(kotlin.String, kotlin.Int): kotlin.Int
	println( exec23( ::add ) )				//5
	val x = (::add)(5,6)		//call using a funref
	println( x ) 				//11

println("let   -------------- ")
	var str = "Hello World"
 	str.let { println("$it!!") }		//Hello World!!
 	137.let { println("$it!!") }		//137!!
	
println("run   -------------- ")
	testRunFunction()
}

fun main() {
    println("BEGINS CPU=$cpus ${curThread()}")
	
    println( "work done in time= ${measureTimeMillis(  { lambdaDemoWork() } )}"  )
	
    println("ENDS ${curThread()}")
}