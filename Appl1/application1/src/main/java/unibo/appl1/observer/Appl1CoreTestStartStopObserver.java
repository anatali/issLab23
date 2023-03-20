package unibo.appl1.observer;

import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Appl1CoreTestStartStopObserver extends ApplAbstractObserver {
private Vector<String> moveHistory = new Vector<String>();
private Set<String> moveCmds       = new HashSet<String>();
private boolean stopped = false;
private boolean resumed = true;

    public Appl1CoreTestStartStopObserver(){
        moveCmds.add("robot-athomebegin");
        moveCmds.add("robot-moving");
        moveCmds.add("robot-stepdone");
        moveCmds.add("robot-stopped");
        moveCmds.add("robot-resumed");
        moveCmds.add("robot-athomeend");
    }
    //permette di riusare un observer in fase di testing
    public void init(){
        moveHistory      = new Vector<String>();
    }

    @Override
    public void update(String msg) {
        //CommUtils.outgreen("         Appl1CoreTestStartStopObserver | " + msg  );
        if( moveCmds.contains(msg)){
            CommUtils.outyellow("         Appl1CoreTestStartStopObserver | " + msg  );
            moveHistory.add(msg);
            if( msg.equals("robot-stopped")) { gotStopped(); }
            if( msg.equals("robot-resumed")) { gotResumed(); }
        }
    }

    private synchronized  void gotStopped(){
        stopped = true;
        resumed = false;
        notifyAll();
    }
    private synchronized  void gotResumed(){
        resumed = true;
        stopped = false;
        notifyAll();
    }

    public Vector<String> getMoveHistory(){
        return moveHistory;
    }

    public synchronized Vector<String> getMoveHistoryAfterStop(){
        while( ! stopped    ){ //! moveHistory.lastElement().equals("robot-stopped")
            CommUtils.outmagenta("         Appl1CoreTestStartStopObserver | wait for stop");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return moveHistory;
    }

    public synchronized void waitUntilResume(){
        while( ! (resumed )  ){
            CommUtils.outmagenta("         Appl1CoreTestStartStopObserver | wait for resume");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
