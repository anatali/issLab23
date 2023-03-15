package unibo.appl1.daevitare.unibo.http;

import unibo.appl1.common.IAppl1Core;
import unibo.appl1.common.IVrobotMoves;
import unibo.basicomm23.http.HTTPCommApache;
import unibo.basicomm23.http.HttpConnection;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.utils.CommUtils;
import unibo.supports.VrobotHLMovesHTTPApache;
import unibo.supports.VrobotHLMovesInteractionSynch;


public class Appl1Core extends java.util.Observable implements IAppl1Core {
    protected boolean started    = false;
    protected boolean stopped    = false;
    protected IVrobotMoves vr ;

    public Appl1Core(){
        stopped = false;
        configure();
        //configureSprint3();
    }

    protected void configure(){
        String URL = "localhost:8090/api/move";
        //URL potrebbe essere letto da un file di configurazione
        HTTPCommApache httpSupport = new HTTPCommApache(  URL );
        vr = new VrobotHLMovesHTTPApache( httpSupport );
    }
    protected void configureSprint3(){
        String URL = "localhost:8090/api/move";
        //URL potrebbe essere letto da un file di configurazione
        Interaction2021 httpConn = HttpConnection.create(URL);
        HTTPCommApache httpSupport = new HTTPCommApache(  URL );
        vr = new VrobotHLMovesInteractionSynch( httpConn );
    }
    private void walkAtBoundary() throws Exception {
        if( checkRobotAtHome() )
            updateObservers("robot-athomebegin");
        else throw new Exception("START: Robot must be at HOME");
        for( int i=1; i<=4;i++) {
            walkBySteppingWithStop(i);
            //CommUtils.outblue("done border:" + i);
            vr.turnLeft();
        }
        if(checkRobotAtHome())
            updateObservers("robot-athomeend");
        else throw new Exception("END: Robot must be at HOME ");
    }

    public void walkBySteppingWithStop(int n) throws Exception {
        boolean stepOk = true;
        CommUtils.outyellow("walkBySteppingWithStop n="+ n );//+ " vr=" + vr.getClass().getName()
        if( n == 1 ){ updateObservers("robot-walking-column-sud"); }
        else if( n == 2 ){ updateObservers("robot-walking-row-east"); }
        else if( n == 3 ){ updateObservers("robot-walking-column-north"); }
        else if( n == 4 ){ updateObservers("robot-walking-row-west"); }
        while( stepOk  ) {
            updateObservers("robot-moving");
            stepOk =  vr.step(350);
            if( ! stepOk ) updateObservers("robot-collision");// updateObservers("robot-collision" );
            else{
                updateObservers("robot-stepdone");
                updateObservers("robot-stable");
            }
            CommUtils.delay(300); //to show the steps better
            //CommUtils.outgreen( "Application1Core stopped=" + stopped);
            if( stopped ) {
                updateObservers("robot-stopped");
                waitResume();
            }
        }
        return;
    }

    public synchronized void waitResume(){
        while( stopped ){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                CommUtils.outred("waitResume " + e.getMessage());
            }
        }
        updateObservers("robot-resumed");
        //CommUtils.outyellow("   RESUMED  "   );
    }

    public void start(   ) throws Exception {
        if( ! started ){
            started = true;
            walkAtBoundary();
         }else CommUtils.outred("Application already started");
    }

    public  void stop(  ) {
        stopped = true;
        try {
            vr.halt();
            CommUtils.outblue( "Application1Core | stopped");
        } catch (Exception e) {
            CommUtils.outred("Application halt error:" + e.getMessage());
        }
    }
    public synchronized void resume(  ){
        stopped = false;
        notifyAll();
    }


    private void updateObservers(String msg){
        setChanged();
        notifyObservers(msg);
    }

    private boolean checkRobotAtHome() {
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
            //e.printStackTrace();
            return false;
        }
    }
}
