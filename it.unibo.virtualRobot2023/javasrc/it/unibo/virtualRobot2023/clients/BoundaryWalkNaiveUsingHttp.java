/*
BoundaryWalkNaiveUsingHttp.java
===============================================================
Technology-dependent application
TODO. eliminate the communication details from this level
===============================================================
*/
package it.unibo.virtualRobot2023.clients;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.net.URI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.utils.CommUtils;

public class BoundaryWalkNaiveUsingHttp {
	private  final String URL              = "http://localhost:8090/api/move";
	private  CloseableHttpClient httpclient ;
	private  JSONParser simpleparser = new JSONParser();
	private  long startTime ;
	
	 private String turnrightcmd  = "{\"robotmove\":\"turnRight\"    , \"time\": \"300\"}";
	 private  String turnleftcmd  = "{\"robotmove\":\"turnLeft\"     , \"time\": \"300\"}";
	 private  String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"4000\"}";  //long ...
	 private  String backwardcmd  = "{\"robotmove\":\"moveBackward\" , \"time\": \"2300\"}";
	 private  String haltcmd      = "{\"robotmove\":\"alarm\" ,        \"time\": \"10\"}";
	
	public BoundaryWalkNaiveUsingHttp() {
		httpclient = HttpClients.createDefault();
		CommUtils.outblue("BoundaryWalkNaiveUsingHttp | CREATED" );
	}


  	protected JSONObject callHTTP( String crilCmd )  {
		//boolean endmove = false;
  		JSONObject jsonEndmove = null;
		try {
 			StringEntity entity = new StringEntity(crilCmd);
			HttpUriRequest httppost = RequestBuilder.post()
					.setUri(new URI(URL))
					.setHeader("Content-Type", "application/json")
					.setHeader("Accept", "application/json")
					.setEntity(entity)
					.build();
			//CommUtils.outyellow("BoundaryWalkNaiveUsingHttp | callHTTP:"+crilCmd );
			CloseableHttpResponse response = httpclient.execute(httppost);
			//CommUtils.outblue( "ClientUsingPost | sendCmd response= " + response );
			String jsonStr = EntityUtils.toString( response.getEntity() );
			jsonEndmove = (JSONObject) simpleparser.parse(jsonStr);
			CommUtils.outyellow("callHTTP | jsonEndmove=" + jsonEndmove);
		} catch(Exception e){
			CommUtils.outred("callHTTP | ERROR:" + e.getMessage());
		}
		return jsonEndmove;
	}

/*
	BUSINESS LOGIC
*/ 	

	public void walkAtBoundary() {
		CommUtils.waitTheUser("PUT ROBOT in HOME and hit");
		boolean answer;
		startTime = System.currentTimeMillis();
		for( int i=1; i<=4;i++) {
			answer = walkAheadUntilCollision();
			long duration  = System.currentTimeMillis() - startTime;
			CommUtils.outgreen("walkAtBoundary done edge:" + i + " duration=" + duration);
			if( !answer ) break;
		}
		long duration  = System.currentTimeMillis() - startTime;
		CommUtils.outblue("walkAtBoundary DONE  total duration=" + duration);
 	}
	
	public boolean walkAheadUntilCollision() {

		JSONObject result = callHTTP(  forwardcmd  );
		CommUtils.outblue("walkAheadUntilCollision forwardcmd result=" + result);
		if( result.toString().contains("collision")) {
			callHTTP(  turnleftcmd  );
			return true;
		}else{
			CommUtils.outred("fatal error: no collision");
			return false;
		}
	}
	
/*
MAIN
 */
	public static void main(String[] args)   {
		CommUtils.aboutThreads("Before start - ");
		BoundaryWalkNaiveUsingHttp appl = new BoundaryWalkNaiveUsingHttp();
		appl.walkAtBoundary();
		CommUtils.aboutThreads("At end - ");
	}
	
 }
