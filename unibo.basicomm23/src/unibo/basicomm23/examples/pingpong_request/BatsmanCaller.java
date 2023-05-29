package unibo.basicomm23.examples.pingpong_request;

import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.examples.ActorNaiveCaller;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;

/*
BatsmanCaller è il batsman
 */
public class BatsmanCaller extends ActorNaiveCaller {
    protected PlayerLogic playLogic = new PlayerLogic();
     protected ServerFactory server;

    public BatsmanCaller(String name, ProtocolType protocol, String hostAddr, String entry) {
        super(name, protocol, hostAddr, entry);
    }

    protected void playUsingRequest(String destName, int i){
        String m = playLogic.hitBall();
        IApplMessage req = CommUtils.buildRequest( name, "hit", m, destName);
        CommUtils.outblue(name + " | sends: " + req);
        //request con timeout per simulare risposte errate
        try {
            IApplMessage answer;
            if( i < 3 ) answer = connSupport.request( req );  //Il receiver risponde bene
            else answer = connSupport.request(req, 300);  //Il receiver risponde male
            CommUtils.outblue(name + " | playUsingRequest answer=" + answer);
        }catch( Exception e ){
            CommUtils.outred(name + " | playUsingRequest ERROR: " + e.getMessage());
        }
        CommUtils.delay(500);
    }

    @Override
    public void body() throws Exception {
        String destName =  name.equals("ping") ? "pong" : "ping";
        for( int i=1; i<=3; i++ ) {
            playUsingRequest(destName,i);
            CommUtils.delay(500);
        }
    }
}
