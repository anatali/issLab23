package ProdConsEvents;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Consumer extends ActorBasic23 {
    public Consumer(String name, ActorContext23 ctx) {
        super(name, ctx);
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        CommUtils.outgreen(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());

    }
}
