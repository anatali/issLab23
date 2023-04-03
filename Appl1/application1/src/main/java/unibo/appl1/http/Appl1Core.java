package unibo.appl1.http;

import org.json.simple.JSONObject;
import unibo.appl1.common.IAppl1Core;
import unibo.appl1.common.IVrobotMoves;
import unibo.appl1.observer.Appl1ObserverForRoomModel;
import unibo.appl1.observer.Appl1ObserverForpath;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.supports.VrobotHLSupportFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Appl1Core extends java.util.Observable implements IAppl1Core {
    protected boolean started    = false;
    protected boolean stopped    = false;
    protected boolean isRunning  = false;
    protected int stepTime       = 370;
    protected Appl1ObserverForpath obsForpath;
    protected Appl1ObserverForRoomModel obsForroom;
    protected IVrobotMoves vr ;
    protected String vitualRobotIp = "";
    protected ProtocolType protocol;

    public Appl1Core() {
    }


    protected void configure() throws Exception{
        CommUtils.outmagenta("Appl1Core |  configure " + Thread.currentThread().getName());
        readConfigFromFile();
    }

    protected void readConfigFromFile() throws Exception{
        File cfgfile          = new File("sprint3Config.json");
        BufferedReader reader = new BufferedReader(new FileReader(cfgfile));
        String currentLine    = reader.readLine();
        CommUtils.outblue("Appl1Core | configure currentLine=" + currentLine);

        JSONObject cj         = CommUtils.parseForJson(currentLine);
        vitualRobotIp         = cj.get("virtualrobotip").toString();
        String vrconn         = cj.get("virtualrobotconn").toString();
        String pathobs        = cj.get("pathobs").toString();
        String robotstateobs  = cj.get("robotstateobs").toString();
        CommUtils.outblue("Appl1Core | configure vrconn=" + vrconn
                + " vitualRobotIp=" + vitualRobotIp
                + " pathobs=" + pathobs + " robotstateobs="+robotstateobs);

        if( vrconn.equals("ws")) {
            protocol = ProtocolType.ws;
            vr = VrobotHLSupportFactory.supportForWS(vitualRobotIp);
        }else if( vrconn.equals("http")) {
            protocol = ProtocolType.http;
            vr = VrobotHLSupportFactory.supportForHTTP(vitualRobotIp);
        }
        stepTime = Integer.parseInt( cj.get("steptime").toString() ) ;
        CommUtils.outblue("Appl1Core | configure stepTime=" + stepTime);
        if( pathobs.equals("true")   ){
            if( obsForpath == null ) {
                obsForpath = new Appl1ObserverForpath();
                //if( obsForpath != null )
                obsForpath.init();
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

    @Override
    public void addObserver(IObserver o) {
        super.addObserver(o);
    }

    //Per osservare i messaggi su WSconnection: lo fa già VrobotHLMovesInteractionAsynch
    /*
    public void addAnObserverOnWsconn(){
        try {
            CommUtils.outred("addAnObserverOnWsconn");
            IObserver obs = new ObserverForWs();
            Interaction conn = ((VrobotHLMovesInteractionAsynch) vr).getConn();
            ((WsConnection) conn).addObserver(obs);
        }catch(Exception e){
            CommUtils.outred("addAnObserverOnWsconn fails:" + e.getMessage());
        }
    }*/

    /*
    Behavior
     */
    protected void walkAtBoundary() throws Exception {
        while( ! robotMustBeAtHome("START") ){
            CommUtils.outblue("Please put robot at HOME");
            CommUtils.delay(3000);
        }
        //updateObservers("robot-athomebegin"); //Per evitore piccoli discostamenti
        isRunning = true;
        for( int k=0; k<4;k++) {
             walkBySteppingWithStop(k);
             vr.turnLeft();
             //CommUtils.delay(300);
             updateObservers("robot-turnLeft");
        }
        isRunning = false;
        robotMustBeAtHome("END"); //updateObservers("robot-athomeend");
        //CommUtils.outmagenta("RESULT=" + evalBoundaryDone() );
    }



    protected void walkBySteppingWithStop(int n) throws Exception {
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
        CommUtils.outmagenta( " waitResume " + Thread.currentThread().getName()  );
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
        if( obsForpath==null) return "Sorry: no observer for path";
        //if( this.isRunning )
            return obsForpath.getCurrentPath();
        /*
        else {
            String rs     = RobotState.getRobotState().toString();
            String answer = obsForpath.getCurrentPath() + "\n" + rs;
            return answer;
        }*/
    }

    public String getPath(){ //bloccante
        if( obsForpath==null) return "Sorry: no observer for path";
        return obsForpath.getPath();
    }
    public boolean evalBoundaryDone(){
        if( obsForpath==null) return true;
        return obsForpath.evalBoundaryDone();
    }

    @Override
    public boolean isRunning(){
        return   isRunning;
    }
    @Override
    public void start(   ) throws Exception {
        if( ! started || ! isRunning){
            isRunning = true;
            started   = true;
            stopped   = false;
            configure();
            CommUtils.outmagenta( "Appl1Core | starts " + Thread.currentThread().getName()  );
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
        CommUtils.outmagenta( "App1Core | resume " + Thread.currentThread().getName()   );
        stopped = false;
        notifyAll();  //Altro Thread ...
    }


    protected void updateObservers(String msg){
        setChanged();
        notifyObservers(msg);
    }

    protected boolean robotMustBeAtHome(String msg)  {
        boolean b = checkRobotAtHome( );
        //CommUtils.outblue("robotMustBeAtHome "  );
        if( b ){
            if( msg.equals("START") ) updateObservers("robot-athomebegin");
            if( msg.equals("END")   ) {
                started   = false;
                isRunning = false;
                updateObservers("robot-athomeend");
                CommUtils.aboutThreads("At home END - ");
            }
        }
        //else throw new Exception("Appl1Core | Robot must be at HOME");
        return b;
    }

    protected boolean checkRobotAtHome( ) {
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
