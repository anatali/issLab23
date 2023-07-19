package unibo.webRobot23;

import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.CommSystemConfig;
import unibo.basicomm23.utils.CommUtils;



public class RobotUtils {
    public static final String applName         = "gui23xyz9526";
    public static final String robotCmdId       = "move";
    public static final String basicrobotCmdId  = "cmd";
    public static final int robotPort           = 8020;
    private static Interaction tcpconn;
    private static Interaction coapconn;
    private static boolean enaged               = false;
    private static boolean withalarm            = false;

    private static void engageRobot(){
        if( enaged ) return;
        IApplMessage engageReq = CommUtils.buildRequest(applName,
                "engage", "engage("+ applName +")", "basicrobot");
        try {
            Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;
            //CommUtils.outmagenta("engage engageReq=" + engageReq);
            String answer    = conn.request( engageReq.toString() );
            //CommUtils.outmagenta("engage answer=" + answer);
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
            CoapConnection robotposconn = new CoapConnection(addr+":"+robotPort, ctxqakdest+"/robotpos" );
            robotposconn.observeResource( new PlanCoapObserver() );
        }catch(Exception e){
            CommUtils.outred("RobotUtils | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
        return (CoapConnection) coapconn;
    }
    public static IApplMessage moveAril(String robotName, String cmd  ) {
        //CommUtils.outblue("HIController | moveAril cmd:" + cmd );
        switch( cmd ) {
            case "w" : return CommUtils.buildDispatch(applName, basicrobotCmdId, "cmd(w)", robotName);
            case "s" : return CommUtils.buildDispatch(applName, basicrobotCmdId, "cmd(s)", robotName);
            case "turnLeft" : return CommUtils.buildDispatch(applName, basicrobotCmdId, "cmd(a)", robotName);
            case "l" : return CommUtils.buildDispatch(applName, basicrobotCmdId, "cmd(a)", robotName);
            case "r" : return CommUtils.buildDispatch(applName, basicrobotCmdId, "cmd(d)", robotName);
            case "h" : return CommUtils.buildDispatch(applName, basicrobotCmdId, "cmd(h)", robotName);
            case "p" : return CommUtils.buildRequest(applName, "step", "step("+RobotController.steptime +")", robotName);

            case "start"  : return CommUtils.buildDispatch(applName, robotCmdId, "start",  robotName);
            case "stop"   : return CommUtils.buildDispatch(applName, "stop", "do",   robotName);
            case "resume" : return CommUtils.buildDispatch(applName, "resume", "do", robotName);
            default:   return CommUtils.buildDispatch(applName,   robotCmdId, "h",robotName);
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
    public static String doPlan(String path, String steptime ){
        try {
            String msg = ""+ CommUtils.buildRequest(applName,
                    "doplan", "doplan("+path+ "," + steptime + ")", "basicrobot");
            Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;
            CommUtils.outblue("RobotUtils doPlan |  msg:" + msg + " conn=" + conn);
            if( withalarm ){
                new Thread(){
                    public void run(){
                        try {
                            Thread.sleep(1500);
                            String msg = ""+ CommUtils.buildEvent("webgui", "alarm", "alarm(fromgui)" );
                            CommUtils.outred("RobotUtils doPlan |  alarm msg:" + msg + " conn=" + conn);
                            conn.forward( msg );
                            withalarm = false;
                        } catch ( Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            String answer = conn.request( msg );
            CommUtils.outmagenta("doPlan answer=" + answer);
            withalarm = false;
            IApplMessage reply = new ApplMessage(answer);
            return reply.msgContent();
        } catch (Exception e) {
            CommUtils.outred("RobotUtils doPlan |  ERROR:"+e.getMessage());
            return "doPlanERROR";
        }

    }
    public static void doRobotPos(String x, String y ){
        try {
            String msg = ""+ CommUtils.buildRequest(applName,
                    "moverobot", "moverobot("+x+","+ y +")", "basicrobot");
            Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;
            CommUtils.outblue("RobotUtils doRobotPos |  msg:" + msg + " conn=" + conn);
            String answer = conn.request( msg );
            CommUtils.outmagenta("doRobotPos answer=" + answer);
        } catch (Exception e) {
            CommUtils.outred("RobotUtils doRobotPos |  ERROR:"+e.getMessage());
        }
    }

    public static void setRobotPos(String x, String y, String d ){
        try {
            String msg = ""+ CommUtils.buildDispatch(applName,
                    "setrobotstate", "setpos("+x+","+ y +","+ d +")", "basicrobot");
            Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;
            CommUtils.outblue("RobotUtils setRobotPos |  msg:" + msg + " conn=" + conn);
            conn.forward( msg );
            CommUtils.outmagenta("setRobotPos dispatch sent" );
        } catch (Exception e) {
            CommUtils.outred("RobotUtils doRobotPos |  ERROR:"+e.getMessage());
        }
    }

    public static void setalarm() {
        withalarm=true;
    }
}
