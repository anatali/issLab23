package kotlindsl
//withApply.kt

fun alphabet0(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("\nNow I know the alphabet!")
    return result.toString()
}

/*
WITH
return the result of the lambda
*/
fun alphabet1(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            this.append(letter)
        }
        append("\nNow I know the alphabet!")
        this.toString()
    }
}

fun alphabet2() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
    toString()
}
/*
APPLY
return the object passed to it as argument
 */
fun alphabet3() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
}.toString()

//buildString : standard library function
//creates a StringBuilder and calls to String
fun alphabet4() = buildString {
    for (letter in 'A'..'Z') {
        append(letter)
    }

}


fun main(args: Array<String>) {
    println(alphabet0())
    /*
    println(alphabet1())
    println(alphabet2())
    println(alphabet3())
    println(alphabet4())
    */
}

/*
OUTPUT is always
ABCDEFGHIJKLMNOPQRSTUVWXYZ
Now I know the alphabet!
 */