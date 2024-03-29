package unibo.basicomm23.actors23.example;

import unibo.basicomm23.actors23.ActorBasic23;
import unibo.basicomm23.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class ActorConsole extends ActorBasic23 {

    public ActorConsole(String name, ActorContext23 ctx) {
        super(name,ctx);
        autostart = true;
        //CommUtils.outblue(name + " " + autoStartMsg);
        //sendMsg( this.autoStartMsg );  //NON QUI, ma in activate
    }
/*
    @Override
    protected void elabMsg(String msg) throws Exception {
        CommUtils.outblue(name + " | elabMsg Sfring " + msg + " in:" + Thread.currentThread().getName());
    }
*/
    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        CommUtils.outblue(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());
        IApplMessage startMsg = CommUtils.buildDispatch(name, "cmd", "start", "a1");
        IApplMessage stopMsg  = CommUtils.buildDispatch(name, "cmd", "stop", "a1");
        CommUtils.delay(1000 );
        CommUtils.outblack("----------------------------------------------------------------- " );
        sendMsg(startMsg);
        CommUtils.delay(2000 );
        CommUtils.outmagenta(name + " | elabMsg send STOP in " + Thread.currentThread().getName());
        sendMsg(stopMsg);
    }
}
