package appl1Actors23.interfaces;
//import javafx.util.Pair;

public interface IRobotState {
    enum Direction {
        UP, RIGHT, DOWN, LEFT;
    }
    Direction getDirection();
    //Pair<Integer,Integer> getPos();
    void turnRight();
    void turnLeft();
    void forward();
    void backward();
    Direction getBackwardDirection();
}
