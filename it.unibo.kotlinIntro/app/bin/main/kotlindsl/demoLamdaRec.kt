package kotlindsl
//demoLamdaRec.kt


//Function types with receiver
val sum: Int.(Int) -> Int = {  arg -> plus(arg) } //plus is called on the receiver object
val sub: Int.(Int) -> Int = {  v -> println("sub this=$this"); minus(v) }

//The anonymous function syntax allows you to specify the receiver type of a function literal directly
val mul = fun Int.(arg: Int): Int  { println("mul this=$this"); return this * arg }
val div = fun Int.(arg: Int): Int = this / arg

//Conventional function type
fun buildString0( builderAction: (StringBuilder) -> Unit ): String {
        val sb = StringBuilder()
        builderAction(sb)
        return sb.toString()
}

fun buildSomething( sb: StringBuilder) : Unit {
    sb.append("Hello").append(", "); sb.append("World!")
}

fun run0() {
    val s = buildString0( ::buildSomething ) //function ref. operator 
    println(s)     //Hello, World!
}


//Extension function type
fun buildString1(  builderAction: StringBuilder.() -> Unit ) : String {
        val sb = StringBuilder()
        sb.builderAction()   //oo-like notation
        return sb.toString()
}



fun main() {
	
	println(" --------  Function types with receiver ")
	val v = 7.sum(3)
	println("7.sum(3)=$v")  //10

	val b = 7.sub(3)
	println("7.sub(3)=$b")  //4

	val t = 7.mul(3)
	println("7.mul(3)=$t")  //21

	val d = 7.div(3)
	println("7.div(3)=$d")  //2
	
	println(" --------  Using run0   ")
	run0()

	println(" --------  Using a lambda  ")
	val s = buildString0 { //it: StringBuilder
        it.append("Hello").append(", ")
        it.append("World!")
    }
    println(s)     //Hello, World!
	
	println(" --------  Using Extension function type ")
    val s1 = buildString1 { //this: StringBuilder
        this.append("Hello, ")
        append("World!")
    }
    println(s1)  //Hello, World!	
}

