package unibo.robotposweb;

import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.CommSystemConfig;
import unibo.basicomm23.utils.CommUtils;



public class RobotUtils {
    public static final String applName         = "webgui";
    public static final String robotCmdId       = "move";
    public static final String basicrobotCmdId  = "cmd";
    public static final int actorCtxPort        = 8111;
    private static Interaction tcpconn;
    private static Interaction coapconn;




    public static void connectActorUsingTcp(String addr){
         try {
             CommSystemConfig.tracing = true;
             tcpconn = TcpClientSupport.connect(addr, actorCtxPort, 10);
             CommUtils.outyellow("RobotUtils | connect Tcp conn:" + tcpconn);

             //sendMsg("worker", "r");
         }catch(Exception e){
            CommUtils.outred("RobotUtils | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
    }
    public static CoapConnection connectActorUsingCoap(String addr){
        try {
            CommSystemConfig.tracing = true;
            String ctxqakdest       = "ctxrobotpos";
            String qakdestination 	= "worker";
            String path   = ctxqakdest+"/"+qakdestination;  //COAP observable resource => worker
            //coapconn                = new CoapConnection(addr+":"+ actorCtxPort, path);
            //connToPathexec = new CoapConnection(addr+":"+robotPort, ctxqakdest+"/pathexec" );
            //((CoapConnection)connToPathexec).observeResource( new PlanCoapObserver() );
            CommUtils.outyellow("RobotUtils | connect Coap conn:" + coapconn);
            coapconn = new CoapConnection(addr+":"+ actorCtxPort, path );
        }catch(Exception e){
            CommUtils.outred("RobotUtils | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
        return (CoapConnection) coapconn;
    }
     /*
    public static void startRobot( String sender, String robotName ){
        Qak22Util.sendAMsg( SystemData.startSysCmd(sender,robotName) );
    }
*/
     public static void sendMsg(String actorName, String cmd){

     }
    /*
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
    }*/
      public static void doRobotPos(String x, String y ){
        try {
            String msg = ""+ CommUtils.buildRequest("webgui",
                    "moverobot", "moverobot("+x+","+ y +")", "worker");
            Interaction conn = (tcpconn != null) ? tcpconn  : coapconn;
            CommUtils.outblue("RobotUtils doRobotPos |  msg:" + msg + " conn=" + conn);
            String answer = conn.request( msg );
            CommUtils.outmagenta("doRobotPos answer=" + answer);
        } catch (Exception e) {
            CommUtils.outred("RobotUtils doRobotPos |  ERROR:"+e.getMessage());
        }
      }
}
