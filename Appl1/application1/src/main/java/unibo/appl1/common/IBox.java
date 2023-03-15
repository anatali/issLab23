package unibo.appl1.common;

public interface IBox {
    boolean isFree();
    boolean isRobot();
    boolean isObstacle();

    void setFree( );
    void setRobot( );
    void setObstacle();
}
