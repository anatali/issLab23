package unibo.Robots.common;

import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.CommSystemConfig;
import unibo.basicomm23.utils.CommUtils;
import unibo.webRobot23.PlanCoapObserver;
import unibo.webRobot23.RobotCoapObserver;


public class RobotUtils {
    public static final String applName         = "webgui";
    public static final String robotCmdId       = "move";
    public static final String basicrobotCmdId  = "cmd";
    public static final int robotPort           = 8020;
    private static Interaction tcpconn;
    private static Interaction coapconn;
    private static boolean enaged               = false;

    private static void engageRobot(){
        if( enaged ) return;
        IApplMessage engageReq = CommUtils.buildRequest(applName,
                "engage", "engage("+ applName +")", "basicrobot");
        try {
            Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;
            String answer    = conn.request( engageReq.toString() );
            CommUtils.outmagenta("engage answer=" + answer);
            enaged = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sendMsg("basicrobot", engageReq.toString());
    }

    public static void connectWithRobotUsingTcp(String addr){
         try {
             CommSystemConfig.tracing = true;
             tcpconn = TcpClientSupport.connect(addr, robotPort, 10);
             CommUtils.outyellow("RobotUtils | connect Tcp conn:" + tcpconn);
             engageRobot();
             //sendMsg("basicrobot", "r");
         }catch(Exception e){
            CommUtils.outred("RobotUtils | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
    }
    public static CoapConnection connectWithRobotUsingCoap(String addr){
        try {
            CommSystemConfig.tracing = true;
            String ctxqakdest       = "ctxbasicrobot";
            String qakdestination 	= "basicrobot";
            String path   = ctxqakdest+"/"+qakdestination;  //COAP observable resource => basicrobot
            coapconn                = new CoapConnection(addr+":"+robotPort, path);
            //connToPathexec = new CoapConnection(addr+":"+robotPort, ctxqakdest+"/pathexec" );
            //((CoapConnection)connToPathexec).observeResource( new PlanCoapObserver() );
            CommUtils.outyellow("RobotUtils | connect Coap conn:" + coapconn);
            engageRobot();
            //Coap coon e observer per i delegati
            CoapConnection planexecconn = new CoapConnection(addr+":"+robotPort, ctxqakdest+"/planexec" );
            planexecconn.observeResource( new PlanCoapObserver() );
            CoapConnection robotposconn = new CoapConnection(addr+":"+robotPort, ctxqakdest+"/robotposendosimbiotico" );
            robotposconn.observeResource( new PlanCoapObserver() );
        }catch(Exception e){
            CommUtils.outred("RobotUtils | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
        return (CoapConnection) coapconn;
    }
    public static IApplMessage moveAril(String robotName, String cmd  ) {
        //CommUtils.outblue("HIController | moveAril cmd:" + cmd );
        switch( cmd ) {
            case "w" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(w)", robotName);
            case "s" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(s)", robotName);
            case "turnLeft" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(a)", robotName);
            case "l" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(a)", robotName);
            case "r" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(d)", robotName);
            case "h" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(h)", robotName);
            case "p" : return CommUtils.buildRequest("webgui", "step", "step(345)", robotName);

            case "start"  : return CommUtils.buildDispatch("webgui", robotCmdId, "start",  robotName);
            case "stop"   : return CommUtils.buildDispatch("webgui", "stop", "do",   robotName);
            case "resume" : return CommUtils.buildDispatch("webgui", "resume", "do", robotName);
            default:   return CommUtils.buildDispatch("webgui",   robotCmdId, "h",robotName);
        }
    }
/*
    public static void startRobot( String sender, String robotName ){
        Qak22Util.sendAMsg( SystemData.startSysCmd(sender,robotName) );
    }
*/
    public static void sendMsg(String robotName, String cmd){
        try {
            IApplMessage msg =  moveAril(robotName,cmd);
            CommUtils.outblue("RobotUtilsss | sendMsg msg:" + msg + " conn=" + tcpconn);
            Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;

            if( msg.isRequest() ){
                String answer = conn.request( msg.toString() );
                CommUtils.outblue("RobotUtils | sendMsg req answer:" + answer  );
            }
            else
                conn.forward( msg.toString() );
        } catch (Exception e) {
            CommUtils.outred("RobotUtils | sendMsg on:" + tcpconn + " ERROR:"+e.getMessage());
        }
    }
    public static void doPlan(String path ){
        try {

            String msg = ""+ CommUtils.buildRequest("webgui",
                    "doplan", "doplan("+path+","+ applName + "," + 300 + ")", "basicrobot");
             Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;
            CommUtils.outblue("RobotUtils doPlan |  msg:" + msg + " conn=" + conn);
            String answer = conn.request( msg );
            CommUtils.outmagenta("doPlan answer=" + answer);
        } catch (Exception e) {
            CommUtils.outred("RobotUtils doPlan |  ERROR:"+e.getMessage());
        }
    }
    public static void doRobotPos(String x, String y ){
        try {
            String msg = ""+ CommUtils.buildRequest("webgui",
                    "moverobot", "moverobot("+x+","+ y +")", "basicrobot");
            Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;
            CommUtils.outblue("RobotUtils doRobotPos |  msg:" + msg + " conn=" + conn);
            String answer = conn.request( msg );
            CommUtils.outmagenta("doRobotPos answer=" + answer);
        } catch (Exception e) {
            CommUtils.outred("RobotUtils doRobotPos |  ERROR:"+e.getMessage());
        }
    }
}
