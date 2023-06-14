/**
 * ClientNaiveUsingWs
 ===============================================================
 * Technology-dependent application
 * TODO. eliminate the communication details from this level
 ===============================================================
 */

package it.unibo.virtualRobot2023;
import javax.websocket.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//See https://www.baeldung.com/java-websockets

@ClientEndpoint
public class ClientNaiveUsingWs {
    private Session userSession      = null;
    private  JSONParser simpleparser = new JSONParser();
    
    private boolean walking          = false;

	 private String turnrightcmd  = "{\"robotmove\":\"turnRight\"    , \"time\": \"300\"}";
	 private  String turnleftcmd  = "{\"robotmove\":\"turnLeft\"     , \"time\": \"300\"}";
	 private  String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"1200\"}";
	 private  String backwardcmd  = "{\"robotmove\":\"moveBackward\" , \"time\": \"1300\"}";
	 private  String haltcmd      = "{\"robotmove\":\"alarm\" , \"time\": \"10\"}";

	 private  String forwardlongcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"2500\"}";

	 
    public ClientNaiveUsingWs(String addr) {
            ColorsOut.out("ClientNaiveUsingWs |  CREATING ..." + addr);
            init(addr);
    }

    protected void init(String addr){
        try {
            //ColorsOut.out("ClientNaiveUsingWs |  container=" + ContainerProvider.class.getName());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+addr));
        } catch (URISyntaxException ex) {
            System.err.println("ClientNaiveUsingWs | URISyntaxException exception: " + ex.getMessage());
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        ColorsOut.out("ClientNaiveUsingWs | opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        ColorsOut.out("ClientNaiveUsingWs | closing websocket");
        this.userSession = null;
    }

    /*
     * 
     */
    @OnMessage
    public void onMessage(String message)  {
    	if( walking ) handleMessage(message);
    	else
        try {
            //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
        	CommUtils.outmagenta("ClientNaiveUsingWs | onMessage:" + message );
            JSONObject jsonObj = (JSONObject) simpleparser.parse(message);
            //ColorsOut.out("ClientNaiveUsingWs | jsonObj:" + jsonObj);
            if (jsonObj.get("endmove") != null ) {
                boolean endmove = jsonObj.get("endmove").toString().equals("true");
                String  move    = (String) jsonObj.get("move") ;
                CommUtils.outgreen("ClientNaiveUsingWs |  " + move + " endmove=" + endmove);
            } else if (jsonObj.get("collision") != null ) {                 
                String move   = (String) jsonObj.get("collision");
                String target = (String) jsonObj.get("target");
                CommUtils.outgreen("ClientNaiveUsingWs |  collision move=" + move + " target=" + target);
             } else if (jsonObj.get("sonarName") != null ) {
                String sonarName = (String) jsonObj.get("sonarName") ;
                String distance  = jsonObj.get("distance").toString();
                CommUtils.outgreen("ClientNaiveUsingWs |  sonarName=" + sonarName + " distance=" + distance);
            }
        } catch (Exception e) {
        	ColorsOut.outerr("onMessage " + message + " " +e.getMessage());
        }

    }

    protected void sendMessage( String msg )   {
        ColorsOut.out("ClientNaiveUsingWs | sendMessage " + msg);
        this.userSession.getAsyncRemote().sendText(msg);
//        try {
//        	this.userSession.getBasicRemote().sendText(msg);
//        	 //synch: blocks until the message has been transmitted
//        }catch(Exception e) {}       
    }
    
/*
BUSINESS LOGIC
*/

    public void doTestCollision() {
        String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"300\"}";
        sendMessage(  forwardcmd  );
        ColorsOut.out("moveForward msg sent"  );

    }
        public void doBasicMoves() {
		walking = false;
		
		sendMessage(  turnleftcmd ) ;
		ColorsOut.out("turnLeft msg sent"  );		
		CommUtils.delay(500);
		
		sendMessage(  turnrightcmd ) ;
		ColorsOut.out("turnRight msg sent"  );
		CommUtils.delay(500);

		//
//		//Now the value of endmove depends on the position of the robot
		sendMessage(  forwardcmd  );
		ColorsOut.out("moveForward msg sent"  );
		CommUtils.delay(1300);

		sendMessage(  backwardcmd );
		ColorsOut.out("moveBackward msg sent"  );
		CommUtils.delay(1300);
		 //Give time to receive msgs from WEnv
}
	
	/*
	 * Dopo il primo messaggio, la business logic � entro onMessage -> handleMessage
	 */
	
	public void walkAtBoundary() {
		walking = true;
		count   = 1;
		//Robot at HOME
		sendMessage( haltcmd );CommUtils.delay(30);
		sendMessage(  forwardlongcmd  );
		CommUtils.delay(10000);
 	}
	
 
	private int count=0;

    public void handleMessage(String message)  {
        try {
        	CommUtils.outmagenta("handleMessage | message:" + message );
            JSONObject jsonObj = (JSONObject) simpleparser.parse(message);
            //ColorsOut.out("ClientNaiveUsingWs | jsonObj:" + jsonObj);
            if (jsonObj.get("endmove") != null ) {
                boolean endmove = jsonObj.get("endmove").toString().equals("true");
                String  move    = (String) jsonObj.get("move") ;
                CommUtils.outyellow("handleMessage |  " + move + " endmove=" + endmove + " count=" + count);
                
                if( ! endmove ) return;
                
                if( count < 4 && endmove ) {
                	sendMessage(  forwardlongcmd  );
                }else sendMessage( haltcmd );CommUtils.delay(30);
                
            } else if (jsonObj.get("collision") != null ) {                 
                String move   = (String) jsonObj.get("collision");
                String target = (String) jsonObj.get("target");
                CommUtils.outgreen("handleMessage |  collision move=" + move + " target=" + target);
                
                sendMessage( haltcmd );CommUtils.delay(30);
                count++;
                sendMessage(  turnleftcmd  );
                CommUtils.delay(300);
              
             } else if (jsonObj.get("sonarName") != null ) {
                String sonarName = (String) jsonObj.get("sonarName") ;
                String distance  = jsonObj.get("distance").toString();
                CommUtils.outgreen("handleMessage |  sonarName=" + sonarName + " distance=" + distance);
            }
        } catch (Exception e) {
        	ColorsOut.outerr("onMessage " + message + " " +e.getMessage());
        }

    }


/*
MAIN
 */
    public static void main(String[] args) {
        try{
    		CommUtils.aboutThreads("Before start - ");
            ClientNaiveUsingWs appl = new ClientNaiveUsingWs("localhost:8091");
            //appl.doBasicMoves();
            //appl.walkAtBoundary();
      		CommUtils.aboutThreads("At end - ");
        } catch( Exception ex ) {
            ColorsOut.outerr("ClientNaiveUsingWs | main ERROR: " + ex.getMessage());
        }
    }
}

/*
ClientNaiveUsingWs | main start n_Threads=1
ClientNaiveUsingWs |  CREATING ...
ClientNaiveUsingWs | opening websocket
ClientNaiveUsingWs | onMessage turnRight endmove=notallowed
ClientNaiveUsingWs | onMessage moveForward endmove=notallowed
ClientNaiveUsingWs | onMessage moveBackward endmove=notallowed
ClientNaiveUsingWs | onMessage turnLeft endmove=true
ClientNaiveUsingWs | onMessage turnLeft endmove=true
ClientNaiveUsingWs | onMessage turnRight endmove=true
ClientNaiveUsingWs | onMessage moveForward endmove=true
ClientNaiveUsingWs | appl  n_Threads=4
ClientNaiveUsingWs | onMessage moveBackward endmove=true
 */