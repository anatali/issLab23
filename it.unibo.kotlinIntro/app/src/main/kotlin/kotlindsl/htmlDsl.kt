package kotlindsl
//htmlDsl

open class Tag(val id: String, val body: String = "") {
    private val children = mutableListOf<Tag>()
    //private var body = ""

    protected fun <T : Tag> doInit(child: T, init: T.() -> Unit) {
        child.init()
        children.add(child)
    }
    fun addTag( tag : Tag ){
         children.add( tag )
    }

    override fun toString() =
        "<$id>$body${children.joinToString("")}</$id>"
}

fun table(init: TABLE.() -> Unit) = TABLE().apply(init)



class TABLE : Tag("table") {
    fun tr(init: TR.() -> Unit) = doInit(TR(), init)
    fun td(init: TD.() -> Unit) = doInit(TD(), init)

    //fun tbody( tag : Tag   ) =  addTag( tag )
}
class TR : Tag("tr") {
    fun td(init: TD.() -> Unit) = doInit(TD(), init)
}

class TD : Tag("td", "hello")
/*
In the lambda passed to the table function, we can use
the tr function to create the <tr> tag.
Outside the lambda, the tr function would be unresolved.
The lambda passed to table has a receiver of type TABLE
which defines the tr method
 */
fun createTable() =
    table {
        tr {
            td {
            }
        }
    }
/*
EXPLIT RECEIVER
To access this from an outer scope (a class, or extension function, or labeled function literal with receiver)
we write this@label where @label is a label on the scope this is meant to be from:

 */
fun createTable1() =
    table {
        (this@table).tr {
            (this@tr).td {
            }
        }
    }

fun createAnotherTable() = table {
    for (i in 1..2) {
        tr {
            td {
            }
        }
    }
}

 

fun main(args: Array<String>) {

 
//
//    val table1 = TABLE()
//    println( table1 )   //<table></table>
//    table1.td {  }
//    println( table1 )  //<table><td>hello</td></table>
//
//    val table2 = table{
//        td{ }
//    }
//    println( table2 )  //<table><td>hello</td></table>
//
//    val tag1 = Tag("tr")
//    println( tag1 )     //<tr></tr>
//    tag1.addTag( Tag("b", "hello") )
//    println( tag1 )
//    var t1 = table{   }
//    println( t1 )  //<table></table>
//    println( Tag("b", "hello") )
//    println( TABLE().apply{ tbody( Tag("b", "hello ... ") ) } )
	 
 //tag1.doInit( tag1, {})		//WRONG

    //println(createTable())
    //println(createTable1())
    //println(createAnotherTable())
}
