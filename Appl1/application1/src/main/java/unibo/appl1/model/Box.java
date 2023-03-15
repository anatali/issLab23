package unibo.appl1.model;

public class Box {
    private boolean isObstacle;
    private boolean isDirty;
    private boolean isRobot;

    public Box(){
        this(false,true,false);
    }

    public Box(boolean isObstacle, boolean isDirty, boolean isRobot) {
        this.isObstacle = isObstacle;
        this.isDirty    = isDirty;
        this.isRobot    = isRobot;
    }


    public boolean isRobot() {
        return this.isRobot;
    }
    public boolean isObstacle() {
        return this.isObstacle;
    }
    public boolean isDirty() {
        return this.isDirty;
    }

    public void setRobot(boolean isRobot) {
        this.isRobot = isRobot;
    }
    public void setObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }
    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }


}
