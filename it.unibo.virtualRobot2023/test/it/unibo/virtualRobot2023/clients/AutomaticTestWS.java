package it.unibo.virtualRobot2023.clients;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import unibo.basicomm23.utils.CommUtils;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.fail;

@ClientEndpoint
public class AutomaticTestWS {
    private Session userSession      = null;
    private JSONParser simpleparser = new JSONParser();
    private  String turnrightcmd  = "{\"robotmove\":\"turnRight\"    , \"time\": \"300\"}";
    private  String turnleftcmd  = "{\"robotmove\":\"turnLeft\"     , \"time\": \"300\"}";
    private  String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"1000\"}";
    private  String backwardcmd  = "{\"robotmove\":\"moveBackward\" , \"time\": \"1000\"}";
    private  String haltcmd      = "{\"robotmove\":\"alarm\" , \"time\": \"10\"}";
    private  String forwardlongcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"3000\"}";
    private long startTime ;
    private BlockingQueue<JSONObject> blockingQueue = new LinkedBlockingDeque<>();


    @Before
    public void init(){
        CommUtils.outmagenta("AutomaticTestWS INIT");
        initConn("localhost:8091");
    }

    @After
    public void end(){
        //CommUtils.delay(3000);
        CommUtils.outmagenta("AutomaticTestWS END");
    }
    protected void initConn(String addr){
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+addr));
        } catch (URISyntaxException ex) {
            CommUtils.outred("TestMovesUsingWs | URISyntaxException exception: " + ex.getMessage());
            fail("initConn");
        } catch (DeploymentException e) {
            CommUtils.outred("TestMovesUsingWs | DeploymentException exception: " + e.getMessage());
            fail("initConn");
        } catch (IOException e) {
            CommUtils.outred("TestMovesUsingWs | IOException exception: " + e.getMessage());
            fail("initConn");
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
            CommUtils.outblue("TestMovesUsingWs | jsonObj:" + jsonObj);
            blockingQueue.add( jsonObj );
        } catch (Exception e) {
            CommUtils.outred("onMessage " + message + " " +e.getMessage());
        }
    }

    protected void callWS(String msg )   {
        CommUtils.outyellow("TestMovesUsingWs | callWS " + msg);
        //if( ! msg.contains("alarm")) startTime = System.currentTimeMillis() ;
        this.userSession.getAsyncRemote().sendText(msg);
    }
    protected void halt(){
        callWS( haltcmd );CommUtils.delay(30);
    }



    @Test
    public void doForward() {
        String forwardcmd   = "{\"robotmove\":\"moveForward\",\"time\": \"1000\"}";
        //CommUtils.waitTheUser("doForward (WS): PUT ROBOT in HOME  and hit (forward 1000)");
        startTime = System.currentTimeMillis();
        callWS(  forwardcmd  );
        try {
            JSONObject msg = blockingQueue.take();
            assert( msg.get("endmove").equals("true") && msg.get("move").equals("moveForward")) ;
            callWS(  backwardcmd  );
            JSONObject result1 = blockingQueue.take();
            CommUtils.outblue("moveBackward result1=" + result1);
            assert( result1.get("move").toString().contains("moveBackward")) ;
        } catch (Exception e) {
            fail("doForward");
        }
        //CommUtils.waitTheUser("Hit to terminate doForward");
        //Per vedere il msg di stato collision e endmove
    }
}
