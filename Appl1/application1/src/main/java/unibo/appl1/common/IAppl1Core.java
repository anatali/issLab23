package unibo.appl1.common;

public interface IAppl1Core {
    public void start() throws Exception;
    public void stop();
    public void resume();
    public boolean isRunning();
    //public void addObserver(IObserver o);
}
