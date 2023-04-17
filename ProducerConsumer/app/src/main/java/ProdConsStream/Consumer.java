package ProdConsStream;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Consumer extends ActorBasic23 {
    public Consumer(String name, ActorContext23 ctx) {
        super(name, ctx);
        //subscribeLocalActor("producer");
        subscribeLocalActor("cleaner");
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        CommUtils.outmagenta(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());

    }
}
