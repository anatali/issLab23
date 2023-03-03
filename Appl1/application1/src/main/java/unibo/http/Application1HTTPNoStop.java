package unibo.http;

import unibo.appl1.common.Appl1ObserverForBoundary;
import unibo.appl1.common.CollisionException;
import unibo.appl1.common.IVrobotMoves;
import unibo.basicomm23.http.HTTPCommApache;
import unibo.basicomm23.utils.CommUtils;
import unibo.common.CollisionException;
import unibo.common.IVrobotMoves;
import unibo.supports.VrobotHLMovesHTTPApache;

/*
Effettua boundary walk
Usa VrobotHLMovesHTTPApache che realizza
    mosse ad alto livello IVrobotMoves (step, turnLeft, ...)
 */
public class Application1HTTPNoStop extends java.util.Observable{
    private IVrobotMoves vr  ;

    public Application1HTTPNoStop(){
        configure();
    }

    protected void configure(){
        String URL = "localhost:8090/api/move";
        //URL potrebbe essere letto da un file di configurazione
        HTTPCommApache httpSupport = new HTTPCommApache(  URL );
        vr = new VrobotHLMovesHTTPApache( httpSupport );
    }

    public void walkAtBoundary() throws Exception {
        updateObservers("robot-athomebegin");
        for( int i=1; i<=4;i++) {
            //walkAheadUntilCollision(i);
            walkByStepping(i);
            vr.turnLeft();
        }
        updateObservers("robot-athomeend");
    }

    public void walkAheadUntilCollision(int n)  throws Exception{
        CommUtils.outyellow("walkAheadUntilCollision n="+n);
        try{
            vr.forward(2300 );
        }catch( CollisionException e){
            return;
        }
        throw new Exception("no collision");
    }

    protected void walkByStepping(int n) throws Exception {
        boolean goon = true;
        CommUtils.outyellow("walkByStepping n="+n);
        if( n == 1 ){ updateObservers("robot-walking-column-sud"); }
        else if( n == 2 ){ updateObservers("robot-walking-row-east"); }
        else if( n == 3 ){ updateObservers("robot-walking-column-north"); }
        else if( n == 4 ){ updateObservers("robot-walking-row-west"); }
        while( goon ) {
            goon =  vr.step(250);
            if( goon ) updateObservers("robot-stepdone");
            CommUtils.delay(200); //to show the steps better
        }
    }

    private void updateObservers(String msg){
        setChanged();
        notifyObservers(msg);
    }

    public static void main( String[] args )  {
        CommUtils.aboutThreads("Before start - ");
        Application1HTTPNoStop appl = new Application1HTTPNoStop();
        try {
            appl.addObserver( new Appl1ObserverForBoundary() );
            appl.walkAtBoundary();
        }catch(Exception e){
            CommUtils.outred("main ERROR:" + e.getMessage());
        }
        CommUtils.aboutThreads("At end - ");
    }
}
