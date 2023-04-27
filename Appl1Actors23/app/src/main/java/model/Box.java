package model;


import appl1Actors23.interfaces.IBox;

public class Box implements IBox {
    private boolean isObstacle;
    private boolean isFree;
    private boolean isRobot;

    public Box(){
        this(false,false,false);
    }

    public Box(boolean isObstacle, boolean isFree, boolean isRobot) {
        this.isObstacle = isObstacle;
        this.isFree     = isFree;
        this.isRobot    = isRobot;
    }

    public boolean isRobot() {
        return this.isRobot;
    }
    public boolean isObstacle() {
        return this.isObstacle;
    }
    public boolean isFree() {
        return this.isFree;
    }

    public void setRobot( ) {
        this.isRobot = true;
    }
    public void setObstacle() {
        this.isObstacle = true;
    }
    public void setFree( ) {
        this.isFree = true;
    }


}
