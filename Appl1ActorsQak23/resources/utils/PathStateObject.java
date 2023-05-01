package utils;

import unibo.basicomm23.utils.CommUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class PathStateObject {
    private Vector<String> moveHistory = new Vector<String>();
    private Set<String> moveCmds       = new HashSet<String>();

    public PathStateObject(){
        moveCmds.add("robotstepdone");
        moveCmds.add("robotcollision");
        moveCmds.add("robotturnLeft");
        moveCmds.add("robotathomeend");
    }

    public void updatePath(String newMove){
        CommUtils.outyellow("ObserverActorForPath: newMove=" + newMove);
        if( newMove.contains("robotstepdone")){ moveHistory.add("w");  }
        else if( newMove.contains("robotturnLeft")){
              moveHistory.add("l");
        }
        else if( newMove.contains("robotcollision")){
             moveHistory.add("|");
        }
        else if( newMove.equals("robotathomeend") ){  }

    }

    public String getCurrentPath(){
        if( moveHistory.isEmpty()) return "nopath";
        String hflat = moveHistory.toString()
                .replace("[","")
                .replace("]","")
                .replace(",","")
                .replace(" ","");
        //CommUtils.outyellow("ObserverActorForPath: hflat=" + hflat);
        return "'"+hflat+"'";
    }

}
