package unibo.basicomm23.actors23;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;

public class Actor23ForReply extends ActorBasic23 {
    private IApplMsgHandler applMagHandler;
    private Interaction conn;

    public Actor23ForReply(String name,
                         IApplMsgHandler h, Interaction conn) {
        super(name,null);
        this.applMagHandler = h;
        this.conn = conn;
        CommUtils.outred(name + " | Actor23ForReply CREATED " );
    }


    protected void elabMsg(IApplMessage msg) {
        //if( msg.isReply() ) applMagHandler.sendAnswerToClient(msg.toString(), conn);
        //ActorContext23.removeActor(this);
    }

    @Override
    protected void elabMsg(String msg) throws Exception {

    }
}
