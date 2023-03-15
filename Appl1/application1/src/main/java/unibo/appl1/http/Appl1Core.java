package unibo.appl1.http;

import unibo.appl1.common.IAppl1Core;
import unibo.appl1.common.IVrobotMoves;
import unibo.appl1.observer.Appl1ObserverForpath;
import unibo.basicomm23.http.HTTPCommApache;
import unibo.basicomm23.utils.CommUtils;
import unibo.supports.VrobotHLMovesHTTPApache;

public class Appl1Core extends java.util.Observable implements IAppl1Core {
    protected boolean started    = false;
    protected boolean stopped    = false;
    protected boolean isRunning  = false;
    private Appl1ObserverForpath obsForpath;
    protected IVrobotMoves vr ;

    public Appl1Core() throws Exception{
        stopped = false;
        configure();
    }
    protected void configure() throws Exception{
        String URL = "localhost:8090/api/move";
        //URL potrebbe essere letto da un file di configurazione
        HTTPCommApache httpSupport = new HTTPCommApache(  URL );
        vr         = new VrobotHLMovesHTTPApache( httpSupport );
        obsForpath = new Appl1ObserverForpath();
        addObserver(  obsForpath );
    }
    private void walkAtBoundary() throws Exception {
        robotMustBeAtHome("START");  //Evito piccoli discostamenti
        //updateObservers("robot-athomebegin");
        isRunning  = true;
        for( int k=0; k<4;k++) {
            walkBySteppingWithStop(k);
            vr.turnLeft();
            //CommUtils.delay(300);
            updateObservers("robot-turnLeft");
        }
        isRunning  = false;
        //updateObservers("robot-athomeend");
        robotMustBeAtHome("END");
    }

    public void walkBySteppingWithStop(int n) throws Exception {
        boolean stepOk = true;
        CommUtils.outyellow("walkBySteppingWithStop n="+ n );
        while( stepOk  ) {
            if( stopped ) {
                CommUtils.beep();
                updateObservers("robot-stopped");
                waitResume();
            }
            updateObservers("robot-moving");
            stepOk =  vr.step(370);
            if( ! stepOk ) updateObservers("robot-collision");
            else updateObservers("robot-stepdone");
            //CommUtils.outgreen( "Application1Core stopped=" + stopped);
            CommUtils.delay(300); //to show the steps better
        }
    }

    public synchronized void waitResume(){
        while( stopped ){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                CommUtils.outred("Appl1Core | waitResume ERROR:" + e.getMessage());
            }
        }
        updateObservers("robot-resumed");
        //CommUtils.outyellow("   RESUMED  "   );
    }

    public String getCurrentPath(){
        return obsForpath.getCurrentPath();
    }
    public String getPath(){
        return obsForpath.getPath();
    }
    public boolean evalBoundaryDone(){
        return obsForpath.evalBoundaryDone();
    }

    @Override
    public boolean isRunning(){
        return  isRunning;
    }

    @Override
    public void start(   ) throws Exception {
        if( ! started ){
            started = true;
            walkAtBoundary();
        }else CommUtils.outred("Appl1Core |  already started");
    }


    @Override
    public synchronized void stop(  ) {
        try {
            stopped = true;
            //vr.halt(); //NO: produce errori in quanto 'interrompe' mosse
            CommUtils.outblue( "Appl1Core | stopped");
        } catch (Exception e) {
            CommUtils.outred("Appl1Core | halt error:" + e.getMessage());
        }
    }

    @Override
    public synchronized void resume(  ){
        stopped = false;
        notifyAll();
    }


    private void updateObservers(String msg){
        setChanged();
        notifyObservers(msg);
    }

    private void robotMustBeAtHome(String msg) throws Exception{
        boolean b = checkRobotAtHome( );
        CommUtils.outblue("robotMustBeAtHome " + msg + " " + b);
        if( b ){
            if( msg.equals("START") ) updateObservers("robot-athomebegin");
            if( msg.equals("END")   ) updateObservers("robot-athomeend");
        }
        else throw new Exception("Appl1Core | Robot must be at HOME");
    }

    private boolean checkRobotAtHome( ) {
        try {
            vr.turnRight();
            boolean res = vr.step(200);
            if (res) return false;
            vr.turnRight();
            res = vr.step(200);
            if (res) return false;
            vr.turnLeft();
            vr.turnLeft();
            return true;
        } catch (Exception e) {
            CommUtils.outred("Appl1Core | checkRobotAtHome ERROR:" + e.getMessage());
            return false;
        }
    }

}
