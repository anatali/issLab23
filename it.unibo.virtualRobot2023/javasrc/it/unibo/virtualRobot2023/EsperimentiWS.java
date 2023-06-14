package it.unibo.virtualRobot2023;
import javax.websocket.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;

@ClientEndpoint
public class EsperimentiWS implements IObserver {
    private WsConnection connWs;
    private Session userSession      = null;
    private  JSONParser simpleparser = new JSONParser();
    
    public EsperimentiWS(String addr) {
        ColorsOut.out("EsperimentiWS |  CREATING ..." + addr);
        connWs   = WsConnection.create( addr );
        connWs.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        update(""+arg);

    }
    @Override
    public void update(String msg) {
        CommUtils.outmagenta("observerd "+msg);
    }

    public void doTestCollision() throws Exception {
        String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"1000\"}";
        connWs.sendMessage(  forwardcmd  );
        ColorsOut.out("moveForward msg sent"  );
        CommUtils.delay(1500); //To see onMessage before program exit
    }
    /*
    MAIN
     */
    public static void main(String[] args) {
        try{
            CommUtils.aboutThreads("Before start - ");
            EsperimentiWS appl = new EsperimentiWS("localhost:8091");
            appl.doTestCollision();
            //appl.doBasicMoves();
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