package gui;

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.MsgUtil;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.utils.CommUtils;

import java.util.Observable;
import java.util.Observer;

public class GuiObsForQak implements IObserver {
    private ActorBasic owner;

    public GuiObsForQak(ActorBasic owner){
        this.owner = owner;
    }

    @Override
    public void update(String move) {
        IApplMessage curMsg ;
        if( move.equals("getpath") ){
            curMsg = CommUtils.buildEvent("gui", "getpath", "getpath(gui)" );
        }else {
            curMsg = CommUtils.buildEvent("gui",  "guicmd", "guicmd("+move+")" );
        }
        //MsgUtil.emitLocalStreamEvent(curMsg, owner, null);
        owner.sendMsgToMyself(curMsg);
        CommUtils.outyellow("GuiObsForQak emits: " + curMsg);
    }

    @Override
    public void update(Observable o, Object arg) {
        update( arg.toString() );
    }
}
