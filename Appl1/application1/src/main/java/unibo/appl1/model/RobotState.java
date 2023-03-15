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
	public Direction getDirection() {
		return this.direction;
	}

	//Pericolosi
	protected void changePos(int x, int y, Direction dir, RoomModel room){
		//TODO
	}
	protected void changePosInRoom( int x, int y ){
		//TODO
	}
	protected void clearCurrentPos(){
		//TODO
	}

	protected void changeDirectionInRoom(  Direction dir ){
		//TODO
	}

	@Override  //Object
	public boolean equals(Object o) {
		if (o == null)  return false;
		if (this.getClass() != o.getClass())  return false;
		RobotState state = (RobotState) o;
		return this.x == state.x && this.y == state.y && this.direction == state.direction;
	}

	@Override
	public Direction getBackwardDirection() {
		switch(direction) {
			case UP:    return Direction.DOWN;
			case RIGHT: return Direction.LEFT;
			case DOWN:  return Direction.UP;
			case LEFT:  return Direction.RIGHT;
			default: return direction;
		}
	}

	@Override
	public void turnRight() {
		//TODO
	}

	@Override
	public void turnLeft() {
		//TODO
	}

	@Override
	public void forward() {
		clearCurrentPos();
		//TODO
		changePosInRoom(x,y);
	}

	@Override
	public void backward() {
		clearCurrentPos();
		//TODO
		changePosInRoom(x,y);
	}

	public String toString() {
		return room.toString()+"RobotPos=("+x+","+y+") direction="+direction;
	}
}
