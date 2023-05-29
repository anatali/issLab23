package unibo.basicomm23.ws;

import org.json.simple.parser.JSONParser;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import javax.websocket.ClientEndpoint;
import javax.websocket.Session;
import java.util.Observable;

@ClientEndpoint
public class EsperimentiWS implements IObserver {
    private String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"2000\"}";
    private WsConnection connWs;
    private Session userSession      = null;
    private JSONParser simpleparser = new JSONParser();
    
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
        connWs.sendMessage(  forwardcmd  );
        ColorsOut.out("moveForward msg sent"  );
        CommUtils.delay(1500); //To see onMessage before program exit
    }

    public void doTestInterrupted() throws Exception{
        String haltcmd  = "{\"robotmove\":\"alarm\" , \"time\": \"10\"}";
        String answer;
        new Thread() {
            public void run() {
                CommUtils.delay(500);
                try {
                    connWs.forward(haltcmd);
                    CommUtils.outred("halted");
                } catch (Exception e) {
                    CommUtils.outred("forward error:"+e.getMessage());
                }
            }
        }.start();

        Long starttime   = System.currentTimeMillis();
        connWs.forward( forwardcmd );
        Long endtime     = System.currentTimeMillis();
        //CommUtils.outblue("moveHttpInterrupted | forwardcmd answer:" + answer + " time=" +(endtime-starttime)/1000.0 );
        CommUtils.delay(2000); //To see the observer messages
    }

    /* MAIN */
    public static void main(String[] args) {
        try{
            CommUtils.aboutThreads("Before start - ");
            EsperimentiWS appl = new EsperimentiWS("localhost:8091");
            //appl.doTestCollision();
            appl.doTestInterrupted();
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