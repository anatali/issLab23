package unibo.appl1.http;
import org.json.simple.JSONObject;
import unibo.appl1.common.IAppl1Core;
import unibo.appl1.common.IVrobotMoves;
import unibo.appl1.observer.Appl1ObserverForRoomModel;
import unibo.appl1.observer.Appl1ObserverForpath;
import unibo.basicomm23.http.HTTPCommApache;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.utils.CommUtils;
import unibo.supports.VrobotHLMovesHTTPApache;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Appl1CoreSprint2 extends java.util.Observable implements IAppl1Core {
    protected boolean started    = false;
    protected boolean stopped    = false;
    protected boolean isRunning  = false;
    protected int stepTime       = 370;
    private Appl1ObserverForpath obsForpath;
    protected Appl1ObserverForRoomModel obsForroom;
    protected IVrobotMoves vr ;
    protected String vitualRobotIp = "";

    public Appl1CoreSprint2() { }

    @Override
    public void addObserver(IObserver o) {
        super.addObserver(o);
    }

    protected void configure() throws Exception{
        stopped    = false;
        readConfigFromFile();
    }

    protected void readConfigFromFile() throws Exception{
        File cfgfile          = new File("sprint3Config.json");
        BufferedReader reader = new BufferedReader(new FileReader(cfgfile));
        String currentLine    = reader.readLine();
        CommUtils.outblue("Appl1Core currentLine=" + currentLine);

        JSONObject cj         = CommUtils.parseForJson(currentLine);
        vitualRobotIp         = cj.get("virtualrobotip").toString();
        String vrconn         = cj.get("virtualrobotconn").toString();
        String pathobs        = cj.get("pathobs").toString();
        String robotstateobs  = cj.get("robotstateobs").toString();
        CommUtils.outblue("Appl1Core vrconn=" + vrconn
                + " vitualRobotIp=" + vitualRobotIp
                + " pathobs=" + pathobs + " robotstateobs="+robotstateobs);

        if( vrconn.equals("ws")) configureUsingWS();
        else if( vrconn.equals("http"))  configureUsingHTTP(vitualRobotIp);

        stepTime = Integer.parseInt( cj.get("steptime").toString() ) ;
        CommUtils.outblue("Appl1Core stepTime=" + stepTime);
        if( pathobs.equals("true")   ){
            if( obsForpath == null ) {
                obsForpath = new Appl1ObserverForpath();
                addObserver(obsForpath);
            }
        }else obsForpath = null;
        if( robotstateobs.equals("true") ){
            if(   obsForroom == null ) {
                obsForroom = new Appl1ObserverForRoomModel();
                addObserver(obsForroom);
            }
        }else obsForroom = null;
    }

    protected void configureUsingHTTP(String vitualRobotIp){
        String URL = vitualRobotIp+":8090/api/move";
        HTTPCommApache httpSupport = new HTTPCommApache(  URL );
        vr         = new VrobotHLMovesHTTPApache( httpSupport );
    }
    protected void configureUsingWS() throws Exception{
        throw new Exception("configureUsingWS not yet implemented");
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
            stepOk =  vr.step(stepTime);
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
                CommUtils.outred("Appl1CoreSprint2 | waitResume ERROR:" + e.getMessage());
            }
        }
        updateObservers("robot-resumed");
        //CommUtils.outyellow("   RESUMED  "   );
    }

    public String getCurrentPath(){
        if( obsForpath != null ) return obsForpath.getCurrentPath();
        else return "no path";
    }



    public String getPath(){
        if( obsForpath != null ) return obsForpath.getPath();
        else return "no path";
    }
    public boolean evalBoundaryDone(){
        if( obsForpath != null ) return obsForpath.evalBoundaryDone();
        else return true;
    }

    @Override
    public boolean isRunning(){
        return  isRunning;
    }

    @Override
    public void start(   ) throws Exception {
        if( ! started ){
            started = true;
            configure();
            walkAtBoundary();
        }else CommUtils.outred("Appl1CoreSprint2 |  already started");
    }


    @Override
    public synchronized void stop(  ) {
        try {
            stopped = true;
            CommUtils.outblue( "Appl1CoreSprint2 | stopped");
            notifyAll();
        } catch (Exception e) {
            CommUtils.outred("Appl1CoreSprint2 | halt error:" + e.getMessage());
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
        //CommUtils.outblue("robotMustBeAtHome " + msg + " " + b);
        if( msg.equals("START") ){
            /*
            while(  ! b ){
                CommUtils.outblue("Please put robot at HOME");
                CommUtils.delay(3000);
                b = checkRobotAtHome( );
            }*/
            if( !b ) throw new Exception("Robot NOT in HOME");
            else updateObservers("robot-athomebegin");
        }
        if( msg.equals("END")   ) {
            if( b ) updateObservers("robot-athomeend");
            else updateObservers("robot-NOTathomeend");
        }
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
            CommUtils.outred("Appl1CoreSprint2 | checkRobotAtHome ERROR:" + e.getMessage());
            return false;
        }
    }

}
