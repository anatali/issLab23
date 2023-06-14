package it.unibo.virtualRobot2023;


//import unibo.basicomm23.http.HttpConnectionApache;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;
 
//https://hc.apache.org/httpcomponents-client-4.5.x/current/tutorial/pdf/httpclient-tutorial.pdf
public class Esperimenti    {
	 private Interaction2021 connHttp, connWs;
	 private String turnrightcmd  = "{\"robotmove\":\"turnRight\"    , \"time\": \"300\"}";
	 private  String turnleftcmd  = "{\"robotmove\":\"turnLeft\"     , \"time\": \"300\"}";
	 private  String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"1600\"}";
	 private  String backwardcmd  = "{\"robotmove\":\"moveBackward\" , \"time\": \"2300\"}";
	 private  String haltcmd      = "{\"robotmove\":\"alarm\" , \"time\": \"10\"}";
	
	 public Esperimenti(){
		 connHttp = HttpConnectionApache.create("localhost:8090" );
		 connWs   = WsConnection.create("localhost:8091" );
	 }
	public void moveHTTPOnly() throws Exception {
		 String answer;
		 	  
		  //TurnLeft
		  answer = connHttp.request( turnleftcmd );
		  CommUtils.outblue("moveHTTPOnly | turnleftcmd answer=" + answer  );
	 	  
		  //Forward (2 times)
		  Long starttime   = System.currentTimeMillis();
		  answer = connHttp.request( forwardcmd );  //si blocca per il tempo della mossa o a una collision
		  Long endtime     = System.currentTimeMillis();
		  CommUtils.outblue("moveHTTPOnly | forwardcmd answer:" + answer + " time=" +(endtime-starttime)/1000.0 );
 
		  starttime   = System.currentTimeMillis();
		  answer = connHttp.request( forwardcmd );
		  endtime     = System.currentTimeMillis();
		  CommUtils.outblue("moveHTTPOnly | forwardcmd answer:" + answer + " time=" +(endtime-starttime)/1000.0 );
		  
	      //if( answer.contains("collision")) CommUtils.delay(2000);  
	 	  
		  //Backward (for a long time)
		  starttime   = System.currentTimeMillis();
		  answer      = connHttp.request( backwardcmd );  //si blocca per il tempo della mossa
		  endtime     = System.currentTimeMillis();
		  CommUtils.outblue("moveHTTPOnly | backwardcmd answer:" + answer + " time=" +(endtime-starttime)/1000.0 );
	    
	       
		  //TurnRight
	      answer = connHttp.request( turnrightcmd );
	      CommUtils.outblue("moveHTTPOnly | turnrightcmd answer=" + answer  );
	}
	
	public void moveHttpInterrupted() throws Exception{
		String answer;
		((WsConnection)connWs).addMessageHandler( new WsMessageHandler() );
		  new Thread() {
			  public void run() {
				  CommUtils.delay(1000);
				  try {
					connWs.forward(haltcmd);
				} catch (Exception e) {
					CommUtils.outred("forward error:"+e.getMessage());
				}				  
			  }
		  }.start();
		  
		  Long starttime   = System.currentTimeMillis();
		  answer = connHttp.request( forwardcmd );   
		  Long endtime     = System.currentTimeMillis();
		  CommUtils.outblue("moveHttpInterrupted | forwardcmd answer:" + answer + " time=" +(endtime-starttime)/1000.0 );
		  //Apro NaiveGui e premo halt oppure ...


		  
	}
  
  public static void main(String [] args) throws Exception {
	  System.out.println("Esperimenti | START"  );
	  Esperimenti appl = new Esperimenti();
	  //appl.moveHTTPOnly();
	  appl.moveHttpInterrupted();
      //CommUtils.delay(2000);
      System.out.println("Esperimenti | BYE"  );
  }
}
