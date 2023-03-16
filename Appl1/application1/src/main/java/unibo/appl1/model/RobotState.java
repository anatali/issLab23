package unibo.appl1.model;

import unibo.appl1.common.IRobotState;
import unibo.basicomm23.utils.CommUtils;

public class RobotState implements IRobotState {
    private int x;
    private int y;
    private Direction direction;
    private RoomModel room = RoomModel.getRoomModel();
    private static RobotState singletonRoomModel;

    public static RobotState getRobotState() {
        if( singletonRoomModel == null ){
            CommUtils.outred("RobotState not set");
            throw new IllegalArgumentException(); //Non richiede handling
        }
        return singletonRoomModel;
    }
    // public enum Direction { UP, RIGHT, DOWN, LEFT; }
    public RobotState(int x, int y, Direction direction) {
        if (x<0 || y<0 || (direction != Direction.UP &&
                direction != Direction.RIGHT && direction != Direction.DOWN &&
                direction != Direction.LEFT))
            throw new IllegalArgumentException();
        this.x = x;
        this.y = y;
        this.direction = direction;
        room.put(x,y, new Box(false,false,true));
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    //public Pair<Integer,Integer> getPos(){
    //return new Pair<Integer,Integer>(this.x, this.y);
    //}
    public Direction getDirection() {
        return this.direction;
    }

    //Pericolosi
    protected void changePos(int x, int y, Direction dir, RoomModel room){
        //TODO
    }
    protected void changePosInRoom( int x, int y ){
        this.x = x;
        this.y = y;
        room.put(x,y, new Box(false,false,true));
    }
    protected void clearCurrentPos(){
        //CommUtils.outyellow( "RobotState clearCurrentPos: x=" + x + " y="+y );
        room.put(x,y, new Box(false,true,false));
    }

    protected void changeDirectionInRoom(  Direction dir ){
      //TODO
   }

    @Override
    public boolean equals(Object o) {
        if (o == null)  return false;
        if (this.getClass() != o.getClass())  return false;
        RobotState state = (RobotState) o;
        return this.x == state.x && this.y == state.y && this.direction == state.direction;
    }

    public Direction getBackwardDirection() {
        switch(direction) {
            case UP:    return Direction.DOWN;
            case RIGHT: return Direction.LEFT;
            case DOWN:  return Direction.UP;
            case LEFT:  return Direction.RIGHT;
            default: return direction;
        }
    }

    public void turnRight() {
        switch(direction) {
            case UP: direction    = Direction.RIGHT; break;
            case RIGHT: direction = Direction.DOWN; break;
            case DOWN: direction  = Direction.LEFT; break;
            case LEFT: direction  = Direction.UP; break;
            default: return ;
        }
    }

    public void turnLeft() {
        //TODO
    }

    public void forward() {
        clearCurrentPos();
        //TODO
        changePosInRoom(x,y);
    }

    public void backward() {
        clearCurrentPos();
        //TODO
        changePosInRoom(x,y);
    }

    public String toString() {
        return room.toString()+"RobotPos=("+x+","+y+") direction="+direction;
    }
}
