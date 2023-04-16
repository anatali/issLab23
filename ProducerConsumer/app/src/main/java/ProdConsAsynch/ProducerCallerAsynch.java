package ProdConsAsynch;


import ProdConsInteraction.ActorNaiveCaller;
import ProdConsInteraction.ProducerLogic;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;


public class ProducerCallerAsynch extends ActorNaiveCaller {
    private ProducerLogic prodLogic  ;

    public ProducerCallerAsynch(String name, ProducerLogic prodLogic, ProtocolType protocol, String hostAddr, String entry){
        super(name, protocol,   hostAddr,   entry);
        //CommUtils.outmagenta("ProducerCaller " + hostAddr + " entry=" + entry);
        this.prodLogic = prodLogic;
    }

    @Override
    protected void body() throws Exception{
        createRceiver();
        for( int i=1; i<=3; i++ ) {
            String d         = prodLogic.getDistance(   );
            IApplMessage req = CommUtils.buildRequest(
                    name, "distance", d, "consumer");
            CommUtils.outblue(name + " | sends request " + i + " " + connSupport);
            //IApplMessage answer =
                    this.connSupport.forward(req);  //raise exception
            //CommUtils.outblue(name + " | answer=" + answer);
            CommUtils.delay(2000);
        }
    }

    protected void createRceiver(){
        new Thread(){
            public void run(){
                while( true ){
                    try {
                        IApplMessage msg = connSupport.receive();
                        CommUtils.outmagenta(name + " | receives " + msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
