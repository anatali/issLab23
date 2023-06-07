/*
-------------------------------------
demoBasic.kt
-------------------------------------
*/
package kotlindemo
 val base : Int = 10 			//Immutable reference.

 var counter = 0				//Mutable reference. The Type Int is inferred

 val name = "Bob"		
 val st   ="hello$name"			//String template
 //val st = "hello"+name


 val data : String? = null		//Nullable type
 val v = data?.length ?: 0		//with safe null access

 val data1 : String? = "124"		 
 val v1 = data?.length ?: 0		//with safe null access


 val v01 : Any = 23				//Any->root of the Kotlin class hierarchy

 val v02 : Any = "Bob"
 val sv02 = v02 is String			//true
 val nv02 = v02 is Int				//false

 val n02 = (v02 as String).length	//Explicit casting
 val x02 = v02 as? Int				//Safe type casting
 val t02 : Int = v02 as? Int ?: 100


  val s1 = "a"
  var s2 = "a"

  val fa = java.io.File("a")
  val fb = java.io.File("a")


  val aToz = "a".."z"
  val q    = "q"

  var arr = arrayOf(1,2,3)

 fun main() {
    println("BEGINS CPU=$cpus ${curThread()}")
    println( st ) //Hello Bob
     println("v=$v")	//v=0
    println( "v01 is String=${v01 is String}") //v01 is String=false
    println( "v01 is Int=${v01 is Int}")	//v01 is Int=true
	println( "v02 is String=${sv02}")
	println( "v02 is Int=${nv02}")
     //val n = v02.length
	if (v02 is String) println("vo2isString length=${v02.length}") // Smart cast: v02 is automatically cast to String
    println( n02 )					//3 from Explicit type casting
    // println( "${v02 as Int}")   //java.lang.ClassCastException
	println( "$x02			")		//null  from Safe type casting
	println( "$t02")				//100 from as? ?:

	 
	println( "s1 === s2 : ${s1 === s2} ")	//true
	println( "s1 == s2  : ${s1 == s2} ")    //true
	s2 = "b" 
	println( "s1 === s2 : ${s1 === s2} ")	//false
	println( "s1 == s2  : ${s1 == s2} ")    //false
    s2 = "a"
     println( "s1 === s2 : ${s1 === s2} ")	//true

     println( "fa === fb : ${fa === fb}") 	//false
	println( "fa == fb  : ${fa == fb}") 	//true
	 
	println( "q in aToz=${q in aToz}")		 //true
	println( "1 in aToz=${"""1""" in aToz}") //false 

	 
	println( "arr size=${arr.size}")
	println( "firstEl=${arr[0]}  lastEl=${arr[arr.size-1]}")
	 
	arr.forEach { v -> print("arr $v ") }; println()
	arr.forEach {  print("$it ") }; println()		//??? works! We will introduce it later
	  	  
    println("ENDS ${curThread()}")
 }
 