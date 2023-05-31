package kotlindemo
//demoSequences
import kotlinx.coroutines.runBlocking

data class APerson(val name: String, val age: Int)

val persons = listOf(
    APerson("Peter", 16),
    APerson("Alice", 23),
    APerson("Anna",  25),
    APerson("Anna",  28),
    APerson("Sonya", 39)
)

val names = persons.asSequence()
    .filter { it.age > 18 }
    .map { it.name }
    .distinct()
    .sorted()
    .toList()

val fibonacciSeq = sequence{
    var a = 0
    var b = 1
    println("fibonacciSeq yield-first: 1 ")
    yield(1)   //first
    while (true) {
        println("fibonacciSeq yield: ${a + b} ")
        yield(a + b)   //next
        val tmp = a + b
        a = b
        b = tmp
    }
}

fun useFibonacciSeq(){
    val v1 = fibonacciSeq.elementAt(1)
    println("element at 1=$v1")

    val v2 = fibonacciSeq.elementAt(2)
    println("element at 2=$v2")

    val firstNums = fibonacciSeq.take(5)  //calculated later
    println("firstNums ... ${firstNums}") //kotlin.sequences.TakeSequence@7006c658

    val v10 = fibonacciSeq.elementAt(10)
    println("element at 10=$v10")

    println("firstNums=${firstNums.joinToString()}")

}

fun testSequence(){
//	println("--- use persons.asSequence")
//	println(names)
//	println("--- use fiboSeq (suspendable)")
 	useFibonacciSeq()
}
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	println( "work done in time= ${measureTimeMillis(  { testSequence() } )}"  )
    println("ENDS ${curThread()}")
}

