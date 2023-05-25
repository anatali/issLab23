package kotlincodedemo

import kotlinx.coroutines.*
//import kotlinx.coroutines.channels.actor 
//import java.io.BufferedReader
//import java.io.InputStreamReader

fun curThread() : String { 
	return "thread=${Thread.currentThread().name}" 
}

data class SonarData( val distance: Int)

object sonarSimulator{
	val data = listOf(
	    SonarData( 0),  SonarData( 10), SonarData( 20), SonarData( 30),
		SonarData( 40),
		SonarData( 50), SonarData( 60), SonarData( 70), SonarData( 80) 
	)
	suspend fun produce(){
		var dataCounter = 1
        data.forEach{
              println("data ${dataCounter++} = $it | ${curThread()}" )
              delay(500)
        }	
	}
}

fun main(  ) = runBlocking{
	sonarSimulator.produce()
}
