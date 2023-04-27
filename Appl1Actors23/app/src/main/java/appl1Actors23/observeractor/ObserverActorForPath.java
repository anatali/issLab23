package appl1Actors23.observeractor;

import appl1Actors23.Appl1StateObject;
import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class ObserverActorForPath extends ActorBasic23 {
    private Vector<String> moveHistory = new Vector<String>();
    private Set<String> moveCmds       = new HashSet<String>();


    public ObserverActorForPath(String name, ActorContext23 ctx) {
        super(name, ctx);
        moveCmds.add("robot-stepdone");
        moveCmds.add("robot-collision");
        moveCmds.add("robot-turnLeft");
        moveCmds.add("robot-athomeend");

        subscribeLocalActor("appl");
    }

    protected void updatePath(String newMOve){
        moveHistory.add(newMOve);
        Appl1StateObject.setPath(getCurrentPath());
    }

    public String getCurrentPath(){
        if( moveHistory.isEmpty()) return "nopath";
        String hflat = moveHistory.toString()
                .replace("[","")
                .replace("]","")
                .replace(",","")
                .replace(" ","");
        //CommUtils.outyellow("ObserverActorForPath: hflat=" + hflat);
        return hflat;
    }
    @Override
    protected void elabMsg(IApplMessage inputmsg) throws Exception {
        //

        if( inputmsg.msgId().equals("startobs")){
            CommUtils.outred(name + " | STARTS "  );
            Appl1StateObject.setPath("nopathinit");
            moveHistory.removeAllElements();
            return;
        }

        if( inputmsg.isRequest() && inputmsg.msgId().equals("getpath")){//getpath
            CommUtils.outgreen(name + " | msg= " + inputmsg);
            IApplMessage answer = CommUtils.buildReply(
                    name,"getpathanswer", "'"+getCurrentPath()+"'", inputmsg.msgSender());
            reply(answer, inputmsg);
            return;
        }
        if( inputmsg.isRequest() && inputmsg.msgId().equals("isrunning")){
            CommUtils.outgreen(name + " | msg= " + inputmsg);
            IApplMessage answer = CommUtils.buildReply(
                    name,"isrunninganswer", "true", inputmsg.msgSender());
            reply(answer, inputmsg);
            return;
        }
        if( ! inputmsg.isEvent() ) return;  //percepisce gli eventi
        String msg = inputmsg.msgContent();
        if( moveCmds.contains(msg) )
            CommUtils.outgreen("   ObserverActorForPath:" + msg + " " + getCurrentPath());
        if( msg.contains("robot-stepdone")){ updatePath("w");  }
        else if( msg.contains("robot-turnLeft")){
            //moveHistory.add("l");
            updatePath("l");
        }
        else if( msg.contains("robot-collision")){
            //moveHistory.add("|");
            updatePath("|");
        }
        else if( msg.equals("robot-athomeend") ){  }
        //CommUtils.outgreen( getPath() );
    }
}
