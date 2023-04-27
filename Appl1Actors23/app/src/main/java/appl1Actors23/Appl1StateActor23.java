package appl1Actors23;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

/*
Introdotta in una prima fase e poi sostituita da ObserverActorForPath
 */
public class Appl1StateActor23 extends ActorBasic23 {

    public Appl1StateActor23(String name, ActorContext23 ctx) {
        super(name, ctx);
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        if( msg.isRequest()  ){
            if( msg.msgId().equals("getpath")){
                String curPath = Appl1StateObject.getPath();
                CommUtils.outyellow(name + " | elabMsg: getpath " + curPath );
                IApplMessage answer = CommUtils.buildReply(
                        name,"getpathanswer", "'"+curPath+"'", msg.msgSender());
                reply(answer, msg);
            }else if( msg.msgId().equals("isrunning")){
                CommUtils.outyellow(name + " | elabMsg: isrunning " + Appl1StateObject.getIsRunning() );
                IApplMessage answer = CommUtils.buildReply(
                        name,"isrunninganswer", ""+ Appl1StateObject.getIsRunning() , msg.msgSender());
                reply(answer, msg);
            }
        }
    }
}
