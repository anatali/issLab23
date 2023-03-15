package unibo.appl1.model;

import unibo.basicomm23.utils.CommUtils;

import java.util.ArrayList;
import java.util.List;

/*
  0  1  2
  ------------------------------  Y
0
1
  ------------------------------
X
 */
public class RoomModel {

    private static RoomModel singletonRoomModel;

    private List<ArrayList<Box>> roomMap = new ArrayList<ArrayList<Box>>();
    //Un array (Y) di array di Box (X)

    public static RoomModel getRoomModel() {
        if (singletonRoomModel == null)
            singletonRoomModel = new RoomModel();
        return singletonRoomModel;
    }

    private RoomModel() {
        super();
        for (int i=0; i<1; i++) {
            roomMap.add(new ArrayList<Box>());
            for (int j=0; j<1; j++) {
                roomMap.get(i).add(null);
            }
        }
        this.put(0, 0, new Box( ));
    }

    public int getLf() { return getDimX(); }
    public int getLu() { return getDimY(); }

    public int getDimY() {
        return roomMap.size();
    }

    public int getDimX() {
        int result=0;
        for (int i=0; i<roomMap.size(); i++) {
            if (result<roomMap.get(i).size())
                result = roomMap.get(i).size();
        }
        return result;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ArrayList<Box> a : roomMap) {
            builder.append("|");
            for (Box b : a) {
                if (b == null)
                    break;
                if (b.isRobot())
                    builder.append("r, ");
                else if (b.isObstacle())
                    builder.append("X, ");
                else if (b.isDirty())
                    builder.append("0, ");
                else
                    builder.append("1, ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }


    public void put(int x, int y, Box box) {
        try {
            roomMap.get(y);
        } catch (IndexOutOfBoundsException e) {
            for (int i=roomMap.size(); i<y; i++) {
                roomMap.add(new ArrayList<Box>());
            }
            roomMap.add(y, new ArrayList<Box>());
        }
        try {
            roomMap.get(y).get(x);
            roomMap.get(y).remove(x);
            roomMap.get(y).add(x, box);
        } catch (IndexOutOfBoundsException e) {
            for (int j=roomMap.get(y).size(); j<x; j++) {
                roomMap.get(y).add(new Box( ));
            }
            roomMap.get(y).add(x, box);
        }
    }

    public static void main(String[] args){
        RoomModel room = RoomModel.getRoomModel();
        CommUtils.outblue( room.toString() );
/*
        for(int i=1; i<=3; i++) room.put(i,0, new Box() );  //Y
        for(int i=1; i<=3; i++) room.put(0,i, new Box() );  //X
        for(int i=4; i<=5; i++) room.put(0,i, new Box(true,false,false) );  //X
        CommUtils.outblue( room.toString() );
*/
        room.put(5,3, new Box(false,true,false));
        CommUtils.outblue( room.toString() );
        CommUtils.outblue( "Lungh lf=" + room.getLf() );
        CommUtils.outblue( "Lungh lu=" + room.getLu() );
    }
}
