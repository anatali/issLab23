package ProdConsFsm23;

import unibo.actors23.Actor23Utils;
import unibo.actors23.ActorContext23;
import unibo.actors23.annotations.State;
import unibo.actors23.annotations.Transition;
import unibo.actors23.fsm.ActorBasicFsm23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class ConsumerFsm23 extends ActorBasicFsm23 {
    String data = "";
    public ConsumerFsm23(String name, ActorContext23 ctx) {
        super(name, ctx);
    }

    @State( name = "s0", initial=true)
    @Transition( state = "consume", msgId = "prodinfo"   )
    protected void s0( IApplMessage msg ) {
        CommUtils.outgreen(name + " | s0 "+msg  );
        //Actor23Utils.showSystemConfiguration();
    }

    @State( name = "consume" )
    @Transition( state = "consume", msgId = "prodinfo"   )
    protected void consume( IApplMessage msg ) {
        CommUtils.outgray(name + " | consume "+msg  );
        data = data + "|" + msg.msgContent();
        CommUtils.outgreen(name + " | data="+data  );
    }

}
