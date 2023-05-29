package unibo.basicomm23.examples.pingpong_dispatch;

import unibo.basicomm23.examples.ActorNaiveCaller;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;

public class PlayerCaller extends ActorNaiveCaller {


    public PlayerCaller(String name, ProtocolType protocol, String hostAddr, String entry) {
        super(name, protocol, hostAddr, entry);
    }

    public void sendMsg(IApplMessage m) throws Exception {
        CommUtils.outgreen(name + " sendMsg:" + m);
        connSupport.forward(m);
    }
    @Override
    protected void body() throws Exception {
         this.connect();
        CommUtils.outgreen(name + " CONNECTEDDDDDDDDDDDDDDDDDD"  );

    }
}
