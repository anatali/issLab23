import java.net.URL
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.io.BufferedReader
import java.io.InputStreamReader
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.auth.AuthScope
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import org.apache.http.client.HttpClient
import java.io.InputStream

lateinit var client : HttpClient 
val hostAddr ="http://192.168.1.8:8000"

fun createClient(){
 val provider    =  BasicCredentialsProvider() //CredentialsProvider
 val credentials =  UsernamePasswordCredentials("webiopi", "raspberry") //UsernamePasswordCredentials
 provider.setCredentials(AuthScope.ANY, credentials)
 client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build() //HttpClient
 println("client created")
}

fun testGet(){
 val path = "GPIO/17/value"
 val response = client.execute(  HttpGet(hostAddr+"/$path") ) //HttpResponse
 showResponse( "GET $path", response.getEntity().getContent() )
}

fun testPut( v : Int){
 val path = "GPIO/17/value/$v"
 val response = client.execute( HttpPost(hostAddr+"/$path")) //HttpResponse
 showResponse( "PUT $path", response.getEntity().getContent() )
}

fun testCmd( v : Int){
 val path = "GPIO/17/value/$v"
 val response = client.execute( HttpPost(hostAddr+"/$path")) //HttpResponse
 showResponse( "PUT $path", response.getEntity().getContent() )
}

fun showResponse(  msg : String, inps : InputStream ){
	inps.bufferedReader().use {
		val response = StringBuffer()
		var inputLine = it.readLine()
		while (inputLine != null) {
			response.append(inputLine)
			inputLine = it.readLine()
		}
		it.close()
		println("Response $msg: $response")
	}	
}

suspend fun testLed(){
	for( i in 1..5){
		delay(500)
		testGet()
		delay(500)
		testPut(1)
		delay(500)
		testGet()
		delay(500)
		testPut(0)
	}	
}

suspend fun testRobotCmd(){
 var response = client.execute( HttpPost(hostAddr+"/macros/do_r/")) //HttpResponse
 showResponse( "POST answer=", response.getEntity().getContent() )
 delay( 1000 )
 response = client.execute( HttpPost(hostAddr+"/macros/do_l/")) //HttpResponse
 showResponse( "POST answer=", response.getEntity().getContent() )	
}

fun main() = runBlocking {
	createClient()
	testRobotCmd()
}


/*
fun sendGet( userName:String ="webiopi", password:String="raspberry" ) {
	
//     var reqParam = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")
//     reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")

	 var reqParam = URLEncoder.encode("", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")
     reqParam += "&" + URLEncoder.encode("", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")

	
	 val url = URL("http://192.168.1.5:8000/GPIO/17/value?" + reqParam)
	 val conn=url.openConnection()
     with(conn as HttpURLConnection) {
		 conn.setDoOutput(true)
        requestMethod = "GET"  // optional default is GET

        println("GET to URL : $url | Response Code : $responseCode")
        if( responseCode != 401 ){
//			getResponse( conn )
        	if( responseCode == 400 ){ println("BAD REQUEST"); return }
	        inputStream.bufferedReader().use {
	            it.lines().forEach { line ->
	                println(line)
	            }
	        }
		}else{
			println("UNAUTHORIZED")
		}
    }
}



fun sendPost() {
    val url = URL("http:///192.168.1.5:8000/GPIO/17/value")

    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"  // optional default is GET

        println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

        inputStream.bufferedReader().use {
            it.lines().forEach { line ->
                println(line)
            }
        }
    }
}
*/
