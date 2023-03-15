package unibo.appl1.observer;

import unibo.appl1.model.RobotState;
import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;
import java.util.HashSet;
import java.util.Set;

public class Appl1ObserverForRoomModel extends ApplAbstractObserver {
    private Set<String> moveCmds       = new HashSet<String>();
    private RobotState robotState;

    public Appl1ObserverForRoomModel(){ 
        moveCmds.add("robot-athomebegin");
        moveCmds.add("robot-stepdone");
        moveCmds.add("robot-turnLeft");
        moveCmds.add("robot-athomeend");
    }


    @Override
    public void update(String msg) {
        if( moveCmds.contains(msg) ) CommUtils.outyellow("         Appl1ObserverForRoomModel:" + msg);
        if( msg.contains("robot-athomebegin")){
            robotState = new RobotState(0,0, RobotState.Direction.DOWN);
        }else if( msg.contains("robot-stepdone")){
            robotState.forward();
        }else if( msg.contains("robot-turnLeft")) {
            robotState.turnLeft();
        }else if( msg.equals("robot-athomeend") ){   }
        String s1 = robotState.toString();
        CommUtils.outblue(s1);

    }


}


