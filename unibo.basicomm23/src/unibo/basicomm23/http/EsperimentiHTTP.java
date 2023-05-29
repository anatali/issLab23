package unibo.basicomm23.http;


import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;


 public class EsperimentiHTTP  {
	 private Interaction connHttp ;
	 private String turnrightcmd  = "{\"robotmove\":\"turnRight\"    , \"time\": \"300\"}";
	 private  String turnleftcmd  = "{\"robotmove\":\"turnLeft\"     , \"time\": \"300\"}";
	 private  String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"1600\"}";
	 private  String backwardcmd  = "{\"robotmove\":\"moveBackward\" , \"time\": \"2300\"}";
	 private  String haltcmd      = "{\"robotmove\":\"alarm\" ,         \"time\": \"10\"}";
	
	 public EsperimentiHTTP(){
		 connHttp = HttpConnection.create("localhost:8090/api/move" );
	 }

	
	public void doTestForward() throws Exception {
		connHttp = HttpConnection.create("localhost:8090/api/move" );
		String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"1000\"}";
		Long starttime   = System.currentTimeMillis();
		String answer    = connHttp.request( forwardcmd );  //si blocca per il tempo della mossa o a una collision
		double elapsed   = (System.currentTimeMillis()-starttime)/1000.0;
		CommUtils.outblue("doTestForwardOk | forwardcmd answer:" + answer + " time=" +elapsed);
	}

	public static void main(String [] args) throws Exception {
	  System.out.println("Esperimenti | START"  );
	  EsperimentiHTTP appl = new EsperimentiHTTP();
 	  appl.doTestForward();
      System.out.println("Esperimenti | BYE"  );
    }

}
