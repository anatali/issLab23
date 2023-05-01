package utils;

 
import it.unibo.kactor.ActorBasicFsm;
import org.json.simple.JSONObject;
import it.unibo.kactor.*;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Appl1StateObject {
    protected static String curPath     = "nopath";
    protected static int NSteps         = 0;
    protected static int NEdges         = 0;
    protected static boolean started    = false;
    protected static boolean stopped    = false;
    protected static boolean isRunning  = false;
    protected static int stepTime       = 370;
    protected static String vitualRobotIp = "";
    protected static ProtocolType protocol;
    protected static VrobotHLMovesActors23 vr ;
    protected static String configFilePath = "unknown";

    public static void initappl(ActorBasicFsm appl, IApplMessage msg){
        CommUtils.outblue(appl.getName() + " | init "+msg  );
        if( started ){
            CommUtils.outred(appl.getName() + " |  ALREADY STARTED " + Thread.currentThread().getName() );
            return;
        }
        try {

            readConfigFromFile(appl);
            reset();
            //setIsRunning(true);
            //setStarted(true);


            vr.setTrace(true);

            IApplMessage event = CommUtils.buildEvent(appl.getName(), "startobs", "ok");

            //TODO appl.emitLocalStreamEvent(event);

            if (!Appl1StateObject.robotMustBeAtHome("START",appl)) {
                CommUtils.outred(appl.getName() + " | elabMsg: NOT AT HOME " + Thread.currentThread().getName());
                throw new Exception("Robot must be at home");
            }


        }catch(Exception e){e.printStackTrace();}

    }

    public static void setPath(String path){
        curPath = path;
    }
    public static String getPath(){
        return curPath;
    }
    /*
    public static void setVr(IVrobotMovesAsynch vrsupport){
        vr = vrsupport;
    }*/
    public static void setConfigFilePath(String fpath){
        configFilePath = fpath;
    }
    public static String getConfigFilePath(){
        return configFilePath;
    }
    public static IVrobotMovesAsynch getVr( ){
        return vr;
    }
    public static void setNSteps(int n){
        NSteps = n;
    }
    public static void incNSteps( ){
        NSteps++;
    }
    public static int getNSteps( ){
        return NSteps;
    }
    public static void setNEdges(int n){
        NEdges = n;
    }
    public static void incNEdges( ){
        NEdges++;
    }
    public static int getNEdges( ){
        return NEdges;
    }
    public static void setStepTime(int v){
        stepTime = v;
    }
    public static int getStepTime( ){
        return stepTime;
    }
    public static void setStarted(boolean v){
        started = v;
    }
    public static boolean getStarted( ){
        return started;
    }
    public static void setStopped(boolean v){
        stopped = v;
    }
    public static boolean getStopped( ){
        return stopped;
    }

    public static void setIsRunning(boolean v){
        isRunning = v;
    }
    public static boolean getIsRunning( ){
        return isRunning;
    }

    /*
    
     */
    
    public static void readConfigFromFile(ActorBasicFsm appl ) throws Exception{
        CommUtils.outblue("Appl1State | Working Directory:" + System.getProperty("user.dir"));
        if( vr != null ){
            CommUtils.outmagenta("Appl1State | readConfigFromFile already done" );
            return;
        }
        File cfgfile          = new File( configFilePath ); //"app/robotConfig.json"
        //C:/Didattica2023/issLab2023/Appl1Actors23/app/robotConfig.json
        BufferedReader reader = new BufferedReader(new FileReader(cfgfile));
        String currentLine    = reader.readLine();
        CommUtils.outblue("Appl1State | configure currentLine=" + currentLine);

        JSONObject cj         = CommUtils.parseForJson(currentLine);
        vitualRobotIp         = cj.get("virtualrobotip").toString();
        String vrconn         = cj.get("virtualrobotconn").toString();
        String pathobs        = cj.get("pathobs").toString();
        String robotstateobs  = cj.get("robotstateobs").toString();
        CommUtils.outblue("Appl1State | configure vrconn=" + vrconn
                + " vitualRobotIp=" + vitualRobotIp
                + " pathobs=" + pathobs + " robotstateobs="+robotstateobs);

        if( vrconn.equals("ws")) {
            protocol = ProtocolType.ws;
            Interaction connToWEnv = WsConnection.create(vitualRobotIp+":8091");
            vr = new VrobotHLMovesActors23( connToWEnv, appl );
        }else if( vrconn.equals("http")) {
            throw new Exception("Appl1StateForActors23 does not support interaction with WEnv via HTTP ");
        }
        stepTime = Integer.parseInt( cj.get("steptime").toString() ) ;
        CommUtils.outblue("Appl1State | configure stepTime=" + stepTime);

    }

    public static void updateObservers(String msg,ActorBasicFsm appl){
        IApplMessage event = CommUtils.buildEvent(appl.getName(),"info", msg);
        CommUtils.outyellow(appl.getName() + " | updateObservers " + event);
        appl.updateResourceRep(msg);   //Per abilitare Coap-observability
        //TODO appl.emitLocalStreamEvent(event);
    }

    public static void doStepAsynch( ) throws Exception {
        CommUtils.outblue("doStepAsynch NEdges="+ NEdges + " NSteps="+NSteps);
        //checkStop();
        //updateObservers("robot-moving");
        vr.stepAsynch( stepTime );
    }

    public static boolean robotMustBeAtHome(String msg,ActorBasicFsm appl)  {
        boolean b = checkRobotAtHome( );
        //CommUtils.outblue("robotMustBeAtHome "  );
        if( b ){
            if( msg.equals("START") ) updateObservers("robot-athomebegin",appl);
            if( msg.equals("END")   ) {
                started   = false;
                isRunning = false;
                updateObservers("robot-athomeend",appl);
                CommUtils.aboutThreads("At home END - ");
            }
        }
        //else throw new Exception("Appl1State | Robot must be at HOME");
        return b;
    }

    public static boolean checkRobotAtHome( ) {
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
            CommUtils.outred("Appl1State | checkRobotAtHome ERROR:" + e.getMessage());
            return false;
        }
    }

    public static void reset(){
        setIsRunning(true);
        setStarted(false);
        setPath("nopath");
        setNEdges(0);
        setNSteps(0);
    }

}
