package unibo.actors23;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.msg.ApplMsgHandler;
import unibo.basicomm23.utils.CommUtils;

public class ContextMsgHandler  extends ApplMsgHandler implements IApplMsgHandler {
    protected String pfx = "    --- ";
    
    private ActorContext23 ctx;
    public ContextMsgHandler(String name, ActorContext23 ctx) {
        super(name);
        this.ctx=ctx;
    }

    @Override
    public void elaborate(IApplMessage msg, Interaction conn) {
        try {
            //if( Actor23Utils.trace)
                CommUtils.outblack(pfx+ name + " | elaborate " + msg);
            if (msg.isRequest()) elabRequest(msg, conn);
            else  elabNonRequest(msg, conn);
        }catch( Exception e){
            CommUtils.outred(name + " | elaborate ERROR " + e.getMessage());
        }
    }

    protected void elabNonRequest( IApplMessage msg, Interaction conn ) throws Exception {
        if (msg.isDispatch()) {
            ActorBasic23 a = ctx.getActor(msg.msgReceiver());
            if (a != null) {  //Qak22Util.sendAMsg( msg );
                a.msgQueue.put(msg);
            } else {
                String errorMsg = name + " | actor unknown:" + msg.msgReceiver();
                CommUtils.outred(errorMsg);
                throw new Exception(errorMsg);
            }
        }else if (msg.isEvent()){
            //Invio usando tutti i proxy
            ctx.propagateEventToActors(msg);
        }
    }

    protected void elabRequest( IApplMessage msg, Interaction conn ) throws Exception {
        String senderName    = msg.msgSender();
        msg.setConn(conn);
        ActorBasic23 a       = ctx.getActor(msg.msgReceiver());
        if (a != null) {  //Qak22Util.sendAMsg( msg );
            a.msgQueue.put(msg);
        } else {
            String errorMsg = name + " | actor unknown:" + msg.msgReceiver();
            CommUtils.outred(errorMsg);
            throw new Exception(errorMsg);
        }
    }
}
