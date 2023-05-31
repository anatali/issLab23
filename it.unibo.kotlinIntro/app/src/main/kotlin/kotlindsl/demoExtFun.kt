package kotlindsl
//demoExtFun.kt

class Counter{
var v = 0
    fun inc(): Int{ return ++v }
}
//Extension functions on Counter
fun Counter.show() : String{ return "counter($v)" }
fun Counter.dec(): Int{
    this.v--
    return v
}

//Utility function
fun showutil( c: Counter ): String { return "counter(${c.v})" }

//Extensions on a property of Counter
var Counter.value : Int
    get() = v
    set( x : Int ){ v = x }


//Extension function on String
fun String.lastChar() : Char = this.get(this.length-1)

//Extension property on String
val String.lastCharProp : Char
	get() = get( length-1 )

fun main() {
	println("-------- Counter ")
    val c1 = Counter()
	println( showutil( c1 )	)   // counter(0)
    println( c1.show() ) 		// counter(0)
    c1.inc();c1.inc();c1.dec()
    println( c1.show() ) 		// counter(1)
	
	println("-------- Using extension on Counter property value")
	c1.value = 10
	println( c1.value ) //counter(10)
	
	println("-------- Using extensions on String ")
	println( "hello!".lastChar() ) 		// !	
}