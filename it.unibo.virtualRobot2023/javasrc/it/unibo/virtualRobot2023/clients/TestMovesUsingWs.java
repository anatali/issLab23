/**
 * TestMovesUsingWs
 ===============================================================
 * Technology-dependent application
 * TODO. eliminate the communication details from this level
 ===============================================================
 */

package it.unibo.virtualRobot2023.clients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.utils.CommUtils;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//See https://www.baeldung.com/java-websockets

@ClientEndpoint
public class TestMovesUsingWs {
    private Session userSession      = null;
    private  JSONParser simpleparser = new JSONParser();
	 private  String turnrightcmd  = "{\"robotmove\":\"turnRight\"    , \"time\": \"300\"}";
	 private  String turnleftcmd  = "{\"robotmove\":\"turnLeft\"     , \"time\": \"300\"}";
	 private  String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"1200\"}";
	 private  String backwardcmd  = "{\"robotmove\":\"moveBackward\" , \"time\": \"1300\"}";
	 private  String haltcmd      = "{\"robotmove\":\"alarm\" , \"time\": \"10\"}";

	 private  String forwardlongcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"3000\"}";
	 private int count = 0;

	 long startTime ;
	 
    public TestMovesUsingWs(String addr) {
            CommUtils.outblue("TestMovesUsingWs |  CREATING ..." + addr);
            init(addr);
    }

    protected void init(String addr){
        try {
            //CommUtils.outblue("TestMovesUsingWs |  container=" + ContainerProvider.class.getName());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+addr));
        } catch (URISyntaxException ex) {
            System.err.println("TestMovesUsingWs | URISyntaxException exception: " + ex.getMessage());
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
    	CommUtils.outblue("TestMovesUsingWs | opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
    	CommUtils.outblue("TestMovesUsingWs | closing websocket");
        this.userSession = null;
    }

    /*
     * 
     */
    @OnMessage
    public void onMessage(String message)  {
        long duration = System.currentTimeMillis() - startTime;
        try {
            //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
        	CommUtils.outmagenta("TestMovesUsingWs | onMessage:" + message + " duration="+duration);
            JSONObject jsonObj = (JSONObject) simpleparser.parse(message);
            //CommUtils.outblue("TestMovesUsingWs | jsonObj:" + jsonObj);
            if (jsonObj.get("endmove") != null ) {
                boolean endmove = jsonObj.get("endmove").toString().equals("true");
                String  move    = (String) jsonObj.get("move") ;
                //CommUtils.outgreen("TestMovesUsingWs | endmove:" + endmove + " move="+move);
                /*
                if( count++ == 0 ) { //TO DO A TEST on notallowed
                    callWS(  turnleftcmd  );CommUtils.delay(350);
                    callWS(  turnrightcmd  );
                }*/
            } else if (jsonObj.get("collision") != null ) {
                String move   = (String) jsonObj.get("collision");
                String target = (String) jsonObj.get("target");
                //halt();
                //senza halt il msg {"endmove":"false","move":"moveForward-collision"} arriva dopo 3000
             } else if (jsonObj.get("sonarName") != null ) { //JUST TO SHOW ...
                String sonarName = (String) jsonObj.get("sonarName") ;
                String distance  = jsonObj.get("distance").toString();
            }
        } catch (Exception e) {
        	CommUtils.outred("onMessage " + message + " " +e.getMessage());
        }
    }

    protected void callWS(String msg )   {
        CommUtils.outyellow("TestMovesUsingWs | callWS " + msg);
        if( ! msg.contains("alarm")) startTime = System.currentTimeMillis() ;
        this.userSession.getAsyncRemote().sendText(msg);
//        try {
//        	this.userSession.getBasicRemote().sendText(msg);
//        	 //synch: blocks until the message has been transmitted
//        }catch(Exception e) {}       
    }
    protected void halt(){
        callWS( haltcmd );CommUtils.delay(30);
    }
/*
BUSINESS LOGIC
*/
    public void doForward() {
		String forwardcmd   = "{\"robotmove\":\"moveForward\",\"time\": \"1000\"}";
		CommUtils.waitTheUser("doForward (WS): PUT ROBOT in HOME  and hit (forward 1000)");
		startTime = System.currentTimeMillis();
		callWS(  forwardcmd  );
		CommUtils.waitTheUser("Hit to terminate doForward");
		//Per vedere il msg di stato collision e endmove
	}
    
    public void  doCollision() {
    	CommUtils.waitTheUser("doCollision (WS): PUT ROBOT near a wall and hit (forward 3000)");
        //halt(); //To remove pending notallowed
        String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"3000\"}";
        startTime = System.currentTimeMillis();
        callWS(  forwardcmd  );
        CommUtils.waitTheUser("Hit to terminate doCollision");
        //Per vedere il msg di stato collision e endmove
    }

    public void doNotAllowed() {
        CommUtils.waitTheUser("doNotAllowed (WS): PUT ROBOT in HOME and hit (forward 1200 and turnLeft after 400)");
        String forwardcmd   = "{\"robotmove\":\"moveForward\", \"time\":\"1200\"}";
        startTime = System.currentTimeMillis();
        callWS(  forwardcmd  );
        CommUtils.outblue("doNotAllowed (WS): moveForward msg sent"  );
        CommUtils.delay(400);
        CommUtils.outblue("doNotAllowed (WS): Now call turnLeft"  );
        callWS(  turnleftcmd  );
        CommUtils.waitTheUser("doHalt (WS): Hit to terminate doNotAllowed");
    }

    public void doHalt() {
        CommUtils.waitTheUser("doHalt (WS): PUT ROBOT in HOME and hit (forward 3000 and alarm after 1000)");
        String forwardcmd   = "{\"robotmove\":\"moveForward\", \"time\":\"3000\"}";
        callWS(  forwardcmd  );
        CommUtils.outblue("doHalt (WS): moveForward msg sent"  );
        CommUtils.delay(1000);
        callWS(  haltcmd  );
        CommUtils.waitTheUser("doHalt (WS): Hit to terminate doHalt");
    }

    public void doBasicMoves() {
        callWS(  haltcmd ) ; //halt asynch non manda enmove
        CommUtils.delay(20);
     CommUtils.waitTheUser("hit to turn");
 	
		callWS(  turnleftcmd ) ;
		CommUtils.outblue("turnLeft msg sent"  );		
		CommUtils.delay(500);
		
		callWS(  turnrightcmd ) ;
		CommUtils.outblue("turnRight msg sent"  );
		CommUtils.delay(500);

	CommUtils.waitTheUser("hit to forward");
//		//Now the value of endmove depends on the position of the robot
		callWS(  forwardcmd  );
		CommUtils.outblue("moveForward msg sent"  );
		CommUtils.delay(1300);
    CommUtils.waitTheUser("hit to backwardcmd");
		callWS(  backwardcmd );
		CommUtils.outblue("moveBackward msg sent"  );
		CommUtils.delay(1300);
		 //Give time to receive msgs from WEnv
}

/*
MAIN
 */
    public static void main(String[] args) {
        try{
    		CommUtils.aboutThreads("Before start - ");
            TestMovesUsingWs appl = new TestMovesUsingWs("localhost:8091");
            appl.doForward();
            appl.doCollision();
            appl.doNotAllowed();
            appl.doHalt();
       		CommUtils.aboutThreads("At end - ");
        } catch( Exception ex ) {
            CommUtils.outred("TestMovesUsingWs | main ERROR: " + ex.getMessage());
        }
    }
}

