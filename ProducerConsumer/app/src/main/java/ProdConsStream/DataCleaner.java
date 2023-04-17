package ProdConsStream;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class DataCleaner extends ActorBasic23 {
    public DataCleaner(String name, ActorContext23 ctx) {
        super(name, ctx);
        subscribeLocalActor("producer");
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        //CommUtils.outgray(name + "msg=" + msg);
        int n = Integer.parseInt( msg.msgContent() );
        if( n % 2 == 0 ) { //elimino i dispari
            IApplMessage  event = CommUtils.buildEvent(name, "data", "" + n);
            emitLocalStreamEvent(event);
        }
    }
}
