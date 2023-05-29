package unibo.basicomm23.http;

import java.net.URI;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;
import unibo.basicomm23.utils.SystemTimer;


//https://hc.apache.org/httpcomponents-client-4.5.x/current/tutorial/pdf/httpclient-tutorial.pdf
public class HttpConnection extends Connection {// implements Interaction2021 {
private static HashMap<String, HttpConnection> connMap= new HashMap<String, HttpConnection>();
private HttpClient client =  HttpClients.createDefault();
private  String URL;
private SystemTimer timerHttp   = new SystemTimer();
private JSONParser simpleparser = new JSONParser();
//final MediaType JSON_MediaType     = MediaType.get("application/json; charset=utf-8");


public static Interaction create(String addr ){
	if( ! connMap.containsKey(addr)){
		connMap.put(addr, new HttpConnection( addr ) );
	}
	return connMap.get(addr);
}

	public HttpConnection(String url) {
		URL = "http://" +url;
		CommUtils.outyellow("HttpConnection | create with URL=" + URL );
    }

//Since inherits from Interaction2021 
	@Override
	public void forward( String msg) throws Exception {
      String answer = sendHttp( msg );
      CommUtils.outyellow("HttpConnection | forward answer:" + answer + " DISCARDED");
	}

	@Override
	public String request(String msg) throws Exception {
		return sendHttp( msg );
	}

	@Override
	public void reply(String msgJson) throws Exception {
		CommUtils.outred("SORRY: not connected for ws");
		throw new Exception("HttpConnection does not implement reply");
	}

	@Override
	public String receiveMsg() throws Exception {
		throw new Exception("HttpConnection does not implement receiveMsg");
	}

	@Override
	public void close() throws Exception {
	}
	
	
//----------------------------------------------------------------------

  public String sendHttp( String msgJson){
      try {
		  CommUtils.outyellow("HttpConnection | sendHttp msgJson=" + msgJson);
		  timerHttp.startTime();
          String answer     = "";
//          List<NameValuePair> params = new ArrayList<NameValuePair>();
//          params.add(new BasicNameValuePair("msg", msgJson));
//           params.add(new BasicNameValuePair("\"robotmove\"", "\"turnLeft\""));
//           params.add(new BasicNameValuePair("\"time\"", "\"300\""));
          HttpPost httpPost = new HttpPost( URL );
          httpPost.setEntity(new StringEntity(msgJson));
          HttpResponse response = client.execute(httpPost);          
//          Long res = response.getEntity().getContent().transferTo(System.out);
		  //CommUtils.delay(1000) ; //per permettere alla gui di finire la rotazione ???
		  timerHttp.stopTime();
		  answer=EntityUtils.toString( response.getEntity() );
		  CommUtils.outyellow("HttpConnection | sendHttp answer="+answer+" elapsed="+timerHttp.getDuration());
          return answer;
      }catch(Exception e){
    	  CommUtils.outred("sendHttp ERROR:" + e.getMessage());
          return "";
      }
  }

	public JSONObject callHTTP(String crilCmd )  {
		CommUtils.outyellow("HttpConnection | callHTTP crilCmd=" + crilCmd);
		JSONObject jsonEndmove = null;
		try {
			StringEntity entity = new StringEntity(crilCmd);
			HttpUriRequest httppost = RequestBuilder.post()
					.setUri(new URI(URL))
					.setHeader("Content-Type", "application/json")
					.setHeader("Accept", "application/json")
					.setEntity(entity)
					.build();
			long startTime                 = System.currentTimeMillis() ;
			HttpResponse response = client.execute(httppost);
			long duration  = System.currentTimeMillis() - startTime;
			String answer  = EntityUtils.toString(response.getEntity());
			CommUtils.outyellow( Thread.currentThread() + " callHTTP | answer= " + answer + " duration=" + duration );

			jsonEndmove = (JSONObject) simpleparser.parse(answer);
			CommUtils.outyellow("callHTTP | jsonEndmove=" + jsonEndmove + " duration=" + duration);
		} catch(Exception e){
			CommUtils.outred("callHTTP | " + crilCmd + " ERROR:" + e.getMessage());
		}
		return jsonEndmove;
	}
}
