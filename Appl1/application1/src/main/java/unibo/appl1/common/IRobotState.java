package unibo.appl1.common;
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
