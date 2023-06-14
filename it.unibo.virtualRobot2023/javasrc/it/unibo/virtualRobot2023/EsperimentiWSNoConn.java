package it.unibo.virtualRobot2023;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class EsperimentiWSNoConn {
    private Session userSession      = null;
    private  JSONParser simpleparser = new JSONParser();

    public EsperimentiWSNoConn(String addr) {
        ColorsOut.out("EsperimentiWS |  CREATING ..." + addr);
        init(addr);
    }

    protected void init(String addr){
        try {
            //ColorsOut.out("EsperimentiWS |  container=" + ContainerProvider.class.getName());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+addr));
        } catch (URISyntaxException ex) {
            CommUtils.outred("EsperimentiWS | URISyntaxException exception: " + ex.getMessage());
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        ColorsOut.out("EsperimentiWS | opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        ColorsOut.out("EsperimentiWS | closing websocket");
        this.userSession = null;
    }
    @OnMessage
    public void onMessage(String message)  {
        //if( walking ) handleMessage(message);
        //else
            try {
                //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
                //CommUtils.outmagenta("EsperimentiWS | onMessage:" + message );
                JSONObject jsonObj = (JSONObject) simpleparser.parse(message);
                CommUtils.outyellow("EsperimentiWS | jsonObj:" + jsonObj);
                if (jsonObj.get("endmove") != null ) {
                    boolean endmove = jsonObj.get("endmove").toString().equals("true");
                    String  move    = (String) jsonObj.get("move") ;
                    CommUtils.outgreen("EsperimentiWS | " + move + " endmove=" + endmove);
                } else if (jsonObj.get("collision") != null ) {
                    String move   = (String) jsonObj.get("collision");
                    String target = (String) jsonObj.get("target");
                    CommUtils.outmagenta("EsperimentiWS | collision move=" + move + " target=" + target);
                } else if (jsonObj.get("sonarName") != null ) {
                    String sonarName = (String) jsonObj.get("sonarName") ;
                    String distance  = jsonObj.get("distance").toString();
                    CommUtils.outgreen("EsperimentiWS |  sonarName=" + sonarName + " distance=" + distance);
                }
            } catch (Exception e) {
                ColorsOut.outerr("onMessage " + message + " " +e.getMessage());
            }

    }

    protected void sendMessage( String msg )   {
        ColorsOut.out("EsperimentiWS | sendMessage " + msg);
        this.userSession.getAsyncRemote().sendText(msg);
//        try {
//        	this.userSession.getBasicRemote().sendText(msg);
//        	 //synch: blocks until the message has been transmitted
//        }catch(Exception e) {}       
    }

    public void doTestCollision() {
        String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"600\"}";
        sendMessage(  forwardcmd  );
        ColorsOut.out("moveForward msg sent"  );
        CommUtils.delay(1500); //To see onMessage before program exit
    }
    /*
    MAIN
     */
    public static void main(String[] args) {
        try{
            CommUtils.aboutThreads("Before start - ");
            EsperimentiWSNoConn appl = new EsperimentiWSNoConn("localhost:8091");
            appl.doTestCollision();
            //appl.doBasicMoves();
            //appl.walkAtBoundary();
            CommUtils.aboutThreads("At end - ");
        } catch( Exception ex ) {
            CommUtils.outred("EsperimentiWS | main ERROR: " + ex.getMessage());
        }
    }
}

/*

WebpageServer sceneSocketInfoHandler  | endmove  PRE index=0
WebpageServer sceneSocketInfoHandler  | endmoveeeee  curMove=moveForward
WebpageServer | updateCallerssss{"endmove":true,"move":"moveForward"} actorObserverClient=undefined
WebpageServer | updateCallers key=0 msgJson={"endmove":true,"move":"moveForward"}

*/