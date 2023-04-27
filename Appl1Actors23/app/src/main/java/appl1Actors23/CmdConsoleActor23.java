package appl1Actors23;

import appl1Actors23.console.gui.ButtonAsGui;
import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.utils.CommUtils;
import java.util.Observable;

/*
Un attore che crea pulsanti in una GUI JAva cui risponde (come observer POJO)
inviando  dispatch o request ad appl, come de requisito
 */
public class CmdConsoleActor23 extends ActorBasic23 implements IObserver {

private IApplMessage curMsg;
private IApplMessage applRunningRequest, applGetPathRequest, startMsg, stopMsg ;
private String[] buttonLabels  = new String[] {"start", "stop", "resume", "getpath" };

    public CmdConsoleActor23(String name, ActorContext23 ctx) {
        super(name,ctx);
        //ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
        //concreteButton.addObserver( this );
        autostart  = true;
    }

    protected void setUp(){
        ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
        concreteButton.addObserver( this );
        //this.myName       = name;
        applRunningRequest = CommUtils.buildRequest(name, "isrunning", "isrunning", "appl");
        applGetPathRequest = CommUtils.buildRequest(name, "getpath", "getpath", "appl");
    }
    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        if( msg.isEvent()) return;
        CommUtils.outblue(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());

        if( msg.isDispatch() && msg.msgContent().equals("start")) {
            setUp();
            return;
        }

        if( msg.isReply()
                && msg.msgId().startsWith("isrunninganswer")
                && msg.msgContent().equals("true")) {
            request(applGetPathRequest); //asynch from ActorBasic23. See elabMsg
            return;
        }
        if( msg.isReply() && msg.msgId().startsWith("getpathanswer") ) {
            CommUtils.outmagenta(name + " currentPath= " + msg.msgContent());
            return;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        update(arg.toString());
        /*
        try {
             String move = arg.toString();
            if( move.equals("getpath")) {
                request( applRunningRequest ); //asynch from ActorBasic23. See elabMsg
            }else {
                curMsg = CommUtils.buildDispatch(name, move+"cmd", move, "appl");
                CommUtils.outyellow("forward: " + curMsg);
                forward( curMsg );
            }
        } catch (Exception e) {
            CommUtils.outred(" CmdConsoleRemote | update ERROR:" + e.getMessage() );;
        }*/
    }

    @Override
    public void update(String move) {
        try {
            if( move.equals("getpath")) {
                request( applRunningRequest ); //asynch from ActorBasic23. See elabMsg
            }else {
                /*
                curMsg = CommUtils.buildDispatch(name, move+"cmd", move, "appl");
                CommUtils.outyellow("forward: " + curMsg);
                forward( curMsg );
*/
                curMsg = CommUtils.buildEvent(name, move+"cmd", move );
                CommUtils.outyellow(name + " sends: " + curMsg);
                //forward( curMsg );
                emitLocalStreamEvent(curMsg);
            }
        } catch (Exception e) {
            CommUtils.outred(" CmdConsoleRemote | update ERROR:" + e.getMessage() );;
        }
    }
}
