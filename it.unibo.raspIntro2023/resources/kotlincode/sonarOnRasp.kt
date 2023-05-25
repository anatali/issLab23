package kotlincode

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.runBlocking
  
class sonarOnRasp( val name : String )  {
 	
    fun readInputData(){
        val numData = 5
        var dataCounter = 1
        val p : Process = Runtime.getRuntime().exec("sudo ./SonarAlone")   
        val reader = BufferedReader(InputStreamReader(p.getInputStream()))
		
        while( true ){
             var data = reader.readLine()    //blocking
			 //println("data ${dataCounter} => $data " )
			 try{
				 var v    = data.toInt()
				 if( v <= 400 && (dataCounter++ % numData == 0) ){
					 //println("data ${dataCounter} = $data " )
	                 println("sonar data= : $v"  )
	             }
			 }catch( e : Exception){}
        }
    }
}
	
fun main() = runBlocking {
 	println("sonarOnRasp | START")
	val appl = sonarOnRasp("sonarOnRasp")
 	appl.readInputData()
 }