package unibo.appl1.common;

import unibo.basicomm23.interfaces.IObserver;

public interface IAppl1Core {
    public void start() throws Exception;
    public void stop();
    public void resume();
    public boolean isRunning();
    public String getCurrentPath();
    public void addObserver(IObserver o);
}
