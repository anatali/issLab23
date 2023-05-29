package unibo.basicomm23.actors23;


import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

public class ActorSupport23 {
//private static ActorContext23 ctx;

    //public ActorSupport23(ActorContext23 ctx){  this.ctx = ctx; }

    public static void sendMsg( IApplMessage msg, ActorContext23 ctx  )   {
        try {
            String destActorName = msg.msgReceiver();
            ActorBasic23 dest    = ctx.getActor(destActorName);
            //CommUtils.outyellow(  "ActorSupport23 | sendMsg to " + dest.name );
            if (dest != null) {
                //CommUtils.outyellow( "ActorSupport23 | sendMsg to " + dest.name);
                dest.msgQueue.put(msg.toString()); //attore locale
            } else {
                sendMsgToRemoteActor(msg, ctx);  //attore non locale
            }
        }catch(Exception e){
            CommUtils.outred(  " | sendMsg ERROR  " + e.getMessage() );
        }
    }

    protected static void sendMsgToRemoteActor( IApplMessage msg, ActorContext23 ctx ) {
        String destActorName = msg.msgReceiver();
        if(Connection.trace) CommUtils.outyellow( "ActorSupport23 | sendMsg " + msg + " to REMOTE " + destActorName );
        //Chiedo al contesto un proxy per l'attore
        Proxy pxy    = ctx.getProxy(destActorName);
        if( pxy == null ) {
            CommUtils.outred("ActorSupport23 | Perhaps no setActorAsRemote for " + destActorName );
            return;
        }
        pxy.sendMsgOnConnection(msg.toString());
    }

}
