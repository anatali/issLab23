package unibo.basicomm23.http;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.utils.ColorsOut;
 

public class HttpConnection  implements Interaction2021 {
	private static HashMap<String,HttpConnection> connMap=
			new HashMap<String,HttpConnection>();
private OkHttpClient okHttpClient  = new OkHttpClient();
final MediaType JSON_MediaType     = MediaType.get("application/json; charset=utf-8");

private String httpAddr ; 

public static Interaction2021 create(String addr ){
	if( ! connMap.containsKey(addr)){
		connMap.put(addr, new HttpConnection( addr ) );
	}
	return connMap.get(addr);
}


public HttpConnection(String addr  ) {
	httpconnect(addr);		
}

//Since inherits from Interaction2021 
	@Override
	public void forward( String msg) throws Exception {
      String answer = sendHttp( msg );
      ColorsOut.out("HttpConnection | forward answer:" + answer + " DISCARDED");
	}

	@Override
	public String request(String msg) throws Exception {
		return sendHttp( msg );
	}

	@Override
	public void reply(String msgJson) throws Exception {
		ColorsOut.outerr("SORRY: not connected for ws");
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

  public void httpconnect(String httpaddr){    //localhost:8090/api/move
      this.httpAddr = httpaddr; 
  }

  public String sendHttp( String msgJson){
      try {
          ColorsOut.out("HttpConnection | sendHttp httpAddr=" + httpAddr, ColorsOut.GREEN);
          RequestBody body = RequestBody.create(JSON_MediaType, msgJson);
          Request request = new Request.Builder()
                  .url( "http://"+httpAddr+"/api/move" )   //TODO
                  .post(body)
                  .build();
          Response response = okHttpClient.newCall(request).execute(); //a stream
          String answer     = ((RealResponseBody) response.body()).string();
          ColorsOut.out("HttpConnection | response body=" + answer, ColorsOut.GREEN);
          return answer;
      }catch(Exception e){
          return "";
      }
  }

  public static void main(String [] args) throws Exception {
	  Interaction2021 connHttp = HttpConnection.create("localhost:8090" );
	  String turnrightcmd = "{\"robotmove\":\"turnRight\" , \"time\": \"300\"}";
	  String turnleftcmd  = "{\"robotmove\":\"turnLeft\" , \"time\": \"300\"}";
	  String forwardcmd  = "{\"robotmove\":\"moveForward\" , \"time\": \"1600\"}";
	  String answer = connHttp.request( turnleftcmd );
	  ColorsOut.out("HttpConnection | turnleftcmd answer:" + answer  );
	  Thread.sleep(300);
	  answer = connHttp.request( forwardcmd );
	  ColorsOut.out("HttpConnection | forwardcmd answer:" + answer  );
	  Thread.sleep(1600);
	  answer = connHttp.request( forwardcmd );
	  ColorsOut.out("HttpConnection | forwardcmd answer:" + answer  );
	  
  }
}
