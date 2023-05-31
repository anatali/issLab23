package kotlindemo
//demoClasses.kt

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty

object SingleCounter {
    private var counter = 0
    fun value(): Int { return counter }
    fun inc() { counter++ }
    fun dec() { counter-- }
    fun reset() { counter = 0 }
}

class Person(val name: String) {
    var age : Int = 0     //public
      set( value ){
	      if(value < 0) println("ERROR: age value wrong")
		  else field = value	//backing field
      }
    var married = false   //public
      set( value ){
      if(value && age < 14) println("WARNING:too young for marriage")
      		//else married = value  //Stack overflow
		  field = value		//backing field
      }
    val isAdult: Boolean
    	get(){ return age >= 18} //custom getter

}

class ExampleDelegate {
    var p: String  by Delegate()  //property
}
class Delegate {
	private var myval : String = "delegateInitialValue"
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		println("... object $thisRef delegates '${property.name}' to $this!")
        return myval
    } 
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("... $this assigns $value to '${property.name}' in $thisRef.")
		myval = value
    }
}


data class PersonData(val name: String) {
    var age : Int = 0     //public
    var married = false   //public
    val isAdult: Boolean
    get(){ return age >= 18} //custom getter
}


class PersonCO private constructor( val name: String ){
    var age : Int = 0
    var married = false
    val isAdult: Boolean
        get(){ return age >= 18} //custom getter
  
	companion object {
	val personList = mutableListOf<PersonCO>()
	fun createPerson( name: String ) : PersonCO {
		val p = PersonCO( name.toUpperCase() )
		personList.add(p)
		return p
	}
	fun showAllPersons(){
	personList.forEach {
		println( "name=${it.name}, age=${it.age},married=${it.married} ") }
	}
  }//companion
	
  object Info {
        fun showAllAdults(){
//            val p = Person("aa")
            personList.forEach {
                if( it.isAdult )
                println( "ADULT ${it.name} of age=${it.age}  ") }
        }
        fun showOrderedByName(){
            println( personList.sortedWith( NameComparator ) )
        }

    }//Info
  object NameComparator : Comparator<PersonCO>{
        override fun compare(p1:PersonCO, p2:PersonCO):Int =
            p1.name.compareTo(p2.name)
    }//NameComparator
}//PersonCO

enum class Color( //properties
    var r: Int, val g: Int, val b: Int){
//Property values for each constant
    RED(255,0,0),
    YELLOW(255,255,0) //declares its own anonymous class
    { override fun toString():String{
				return "YELLOW_COLOR"} },
    GREEN(0,255,0), BLUE(0,0, 255)
    ; //semicolon is is mandatory if define methods

    fun rgb() = (r * 256 + g) * 256 + b
    override fun toString() : String { 
        return "${super.toString()}($r,$g,$b)" }
}

enum class Origin{
    asia, africa, europa, america, australia
}

open class PersonILL(val name:String,	//Init,Late,Lazy
		val nickname: String = "rambo") {//default value
    var age      = 0
    var married   = false
    val isAdult: Boolean by lazy{ println("lazy fired");age>18 }
    lateinit var country  : Origin  //visible
    protected var  voter  : Boolean //not visible
        get(){ return isAdult }
    init{
	    //country = Origin.europa	//makes lateinit unnecessary
        voter  =  (age > 18)   //expression
    }
    //custom accessor
    fun voter():Boolean{ return voter }
}

class Student(name: String,
	nickname: String="nerd") : PersonILL(name, nickname) {
	override fun toString() : String{
		return "student(name($name),age($age),married($married),"+
		"adult($isAdult),nickname($nickname),"+
		"country($country),voter($voter))" 
	}
}


sealed class Expr{
    class Num( val value:Int):Expr()
    class Add( val left:Expr, val right:Expr):Expr()
    fun eval():Int{
        when( this ){
            is Num -> return value
            is Add -> return left.eval() + right.eval()
            //no deafult branch
        }
    }
}

//=======================================================================
fun p2( c:SingleCounter ) : Int { 
	return c.value()*c.value() }

fun testObject(){
	println("------ testObject ")
    val c = SingleCounter
    val d = SingleCounter
    for( i in 1..3 ) c.inc()
    val v = p2( SingleCounter )
    println("testObject | c=${c.value()} d=${d.value()} obj=${SingleCounter.value()} v=$v")
    SingleCounter.reset()
    println("testObject | c=${c.value()} d=${d.value()} obj=${SingleCounter.value()}")	
}

fun testClass(){
	println("------ testClass ")
    val p1 = Person("Bob")		//no new
    p1.age=20
    println( "name=${p1.name}, age=${p1.age}, "+
  	  " married=${p1.married} adult=${p1.isAdult} ")
    val p2 = Person("Alice")	//no new
    p2.age = 15
    println( "name=${p2.name}, age=${p2.age}, " +
	" married=${p2.married} adult=${p2.isAdult} ")
    val p3 = Person("Bob")		//no new
    p3.age= p1.age
    println( "equals:  ${p1.equals(p3)}" )	//false
    println( "==:      ${p1 == p3}" )	//false
    println( "===:     ${p1 === p3}" )	//false
	
	println("------ testClass set")
	p1.married = true
    println( "name=${p1.name}, age=${p1.age},married=${p1.married} adult=${p1.isAdult} ")
	p1.age     = 18
	p1.married = true
    println( "name=${p1.name}, age=${p1.age},married=${p1.married} adult=${p1.isAdult} ")
}

fun testPropertyDelegate(){
	println("------ testPropertyDelegate")
	val v = ExampleDelegate()
	println(v.p)
	v.p = "Bob"
	println(v.p)
}
fun testDataClass(){
    val p1 = PersonData("Bob")
    p1.age=20
    val p3 = PersonData("Bob")
    p3.age = p1.age
    println( "p1=${p1}, p3=$p3 ")		//toString generated
    println( "equals:  ${p1.equals(p3)}" )
}


fun testCompanion(){
   //val p = PersonCO("Bob") //ERROR: init is private in PersonCO
    val p1 = PersonCO.createPerson("Bob")
    p1.age=20
    val p2 = PersonCO.createPerson("Alice")
    p2.age = 15
    PersonCO.createPerson("Adam")	//age=0
    PersonCO.showAllPersons()
    PersonCO.Info.showAllAdults()	
}

fun testEnum(){
    val y = Color.YELLOW
    val b = Color.BLUE
    println("$y = ${y.rgb()} | $b = ${b.rgb()} ")	
}


fun testILL(){
 val p = PersonILL("Bob")
  println("name=${p.name} age=${p.age} married=${p.married} " +
     " nickname=${p.nickname}  ") // p.country not initialized
  p.age     = 22
  p.married = true
  p.country = Origin.europa
  println("Bob property-set done ")
  println("Bob is adult=${p.isAdult} - lazy fired")  
  println("name=${p.name} age=${p.age} married=${p.married}")
  println("	adult=${p.isAdult} nickname=${p.nickname}")
  println("	country=${p.country} voter=${p.voter()}")	
}

fun testInheritance(){
  val p = Student("alice", "batterfly")
  p.age     = 24
  p.country = Origin.asia
  println( p )
}


fun testSealedClass(){
    val v1 = Expr.Num(10)
    val v2 = Expr.Num(20)
    val sum = Expr.Add(v1,v2)
    println("${v1.eval()} + ${v2.eval()} = ${sum.eval()}")	
}


fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	
//    println( "work done in time= ${measureTimeMillis(  { testObject() } )}"  )
//     println( "work done in time= ${measureTimeMillis(  { testClass() } )}"  )
//     println( "work done in time= ${measureTimeMillis(  { testPropertyDelegate() } )}"  )
//    println( "work done in time= ${measureTimeMillis(  { testDataClass() } )}"  )
//    println( "work done in time= ${measureTimeMillis(  { testCompanion() } )}"  )
//     println( "work done in time= ${measureTimeMillis(  { testEnum() } )}"  )
//     println( "work done in time= ${measureTimeMillis(  { testILL() } )}"  )
//    println( "work done in time= ${measureTimeMillis(  { testInheritance() } )}"  )
	println( "work done in time= ${measureTimeMillis(  { testSealedClass() } )}"  )
    println("ENDS ${curThread()}")
}