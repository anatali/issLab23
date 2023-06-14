package it.unibo.virtualRobot2023;

import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
 
 
 
//https://hc.apache.org/httpcomponents-client-4.5.x/current/tutorial/pdf/httpclient-tutorial.pdf
public class HttpConnection implements Interaction2021 {
private static HashMap<String, HttpConnection> connMap= new HashMap<String, HttpConnection>();
private HttpClient client =  HttpClients.createDefault();
private  String URL;
//final MediaType JSON_MediaType     = MediaType.get("application/json; charset=utf-8");


public static Interaction2021 create(String addr ){
	if( ! connMap.containsKey(addr)){
		connMap.put(addr, new HttpConnection( addr ) );
	}
	return connMap.get(addr);
}

	public HttpConnection(String url) {
		URL = "http://" +url;
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
	}

	@Override
	public String receiveMsg() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws Exception {
	}
	
	
//----------------------------------------------------------------------

  public String sendHttp( String msgJson){
      try {
          //("HttpConnection | sendHttp msgJson=" + msgJson, ColorsOut.GREEN);
    	  CommUtils.outgreen("sendHttp msgJson:" + msgJson);
          String answer     = "";
//          List<NameValuePair> params = new ArrayList<NameValuePair>();
//          params.add(new BasicNameValuePair("msg", msgJson));
//           params.add(new BasicNameValuePair("\"robotmove\"", "\"turnLeft\""));
//           params.add(new BasicNameValuePair("\"time\"", "\"300\""));
          HttpPost httpPost = new HttpPost( URL );
          httpPost.setEntity(new StringEntity(msgJson));
          HttpResponse response = client.execute(httpPost);          
          //Long res = response.getEntity().getContent().transferTo(System.out);
          CommUtils.outgreen("sendHttp response:" + response);
          answer=EntityUtils.toString( response.getEntity() );
          return answer;
      }catch(Exception e){
    	  CommUtils.outred("sendHttp ERROR:" + e.getMessage());
          return "";
      }
  }

}
