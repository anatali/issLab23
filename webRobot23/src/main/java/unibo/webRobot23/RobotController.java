package unibo.webRobot23;
//https://www.baeldung.com/websockets-spring
//https://www.toptal.com/java/stomp-spring-boot-websocket
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.utils.CommUtils;



//---------------------------------------------------
//import unibo.Robots.common.RobotUtils;


@Controller 
public class RobotController {
    public final static String robotName  = "basicrobot";
    protected String mainPage             = "basicrobot23EssentialGui"; //"basicrobot23Gui";
    protected String noVrPage             = "basicrobot23NoVrGui"; //

    protected boolean usingTcp            = true;

    //Settaggio degli attributi del modello
    //@Value("${robot23.protocol}")
    String protocol="tcp";
    //@Value("${robot23.webcamip}")
    String webcamip;
    //@Value("${robot23.robotip}")
    String robotip="basicrobot23";
    @Value("${robot23.plan}")
    String plantodo;
    @Value("${robot23.plandone}")
    String plandone;
    //@Value("${robot23.stepTime}")
    public static String steptime = "330";


    protected String buildThePage(Model viewmodel) {
        setConfigParams(viewmodel);
        return mainPage;
    }
    protected String buildTheNoVrPage(Model viewmodel) {
        setConfigParams(viewmodel);
        return noVrPage;
    }
    protected void setConfigParams(Model viewmodel){
        viewmodel.addAttribute("protocol", protocol);
        viewmodel.addAttribute("webcamip", webcamip);
        viewmodel.addAttribute("robotip",  robotip);
        viewmodel.addAttribute("pathtodo", plantodo);
        viewmodel.addAttribute("plandone", plandone);
        viewmodel.addAttribute("steptime", steptime);
    }

  @GetMapping("/") 		 
  public String entry(Model viewmodel) {
        //Connection.trace = true;

        return buildThePage(viewmodel);
  }

    @PostMapping("/setprotocol")
    public String setprotocol(Model viewmodel, @RequestParam String protocol  ){
        this.protocol = protocol;
        usingTcp      = protocol.equals("tcp");
        System.out.println("RobotController | usingTcp:" + usingTcp );
        viewmodel.addAttribute("protocol", protocol);
        return buildThePage(viewmodel);
    }
    @PostMapping("/setwebcamip")    
    public String setwebcamip(Model viewmodel, @RequestParam String ipaddr  ){
        webcamip = ipaddr;
        System.out.println("RobotHIController | setwebcamip:" + ipaddr );
        viewmodel.addAttribute("webcamip", webcamip);
        return buildThePage(viewmodel);
    }
    @PostMapping("/setsteptime")
    public String setsteptime(Model viewmodel, @RequestParam String steptime  ){
        RobotController.steptime = steptime;
        System.out.println("RobotHIController | setsteptime:" + RobotController.steptime);
        return buildThePage(viewmodel);
    }
    @PostMapping("/setrobotip")
    public String setrobotip(Model viewmodel, @RequestParam String ipaddr  ){
        robotip = ipaddr;
        System.out.println("RobotHIController | setrobotip:" + ipaddr );
        viewmodel.addAttribute("robotip", robotip);
        if( usingTcp ) RobotUtils.connectWithRobotUsingTcp(ipaddr);
        //Attivo comunque una connessione CoAP (per osservare basicrobot e doplan)
        CoapConnection conn = RobotUtils.connectWithRobotUsingCoap(ipaddr);
        conn.observeResource( new RobotCoapObserver() );
        return buildThePage(viewmodel);
    }

    @PostMapping("/robotmove")
    public String doMove(Model viewmodel  , @RequestParam String move ){
        CommUtils.outblue("RobotController | doMove:" + move + " robotName=" + robotName);
        //WebSocketConfiguration.wshandler.sendToAll("RobotController | doMove:" + move); //disappears
        try {
              RobotUtils.sendMsg(robotName,move);
        } catch (Exception e) {
            CommUtils.outred("RobotController | doMove ERROR:"+e.getMessage());
        }
        return buildThePage(viewmodel);
    }

    @PostMapping("/doplan")
    public String dopath(Model viewmodel , @RequestParam String plan ){
        CommUtils.outblue("RobotController | doplan:" + plan + " robotName=" + robotName);
        if( plan == null ) plantodo="ll";
        else plantodo =  plan;
        viewmodel.addAttribute("plantodo", ""+plantodo);
        try {
              //plandone = RobotUtils.doPlan( plan, steptime );
              RobotUtils.sendPlanMsg( plan, steptime );
        } catch (Exception e) {
              CommUtils.outred("RobotController | doplan ERROR:"+e.getMessage());
        }
        viewmodel.addAttribute("plandone", ""+plandone);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buildThePage(viewmodel);
    }

    @PostMapping("/alarm")
    public String alarm(Model viewmodel   ){
        CommUtils.outmagenta("RobotController | alarm robotName=" + robotName);
        viewmodel.addAttribute("plantodo", ""+plantodo);

            RobotUtils.setalarm(  )  ;

        return buildThePage(viewmodel);
    }


    @PostMapping("/dorobotpos")
    public String dorobotpos(Model viewmodel  , @RequestParam String x, @RequestParam String y ){
        //CommUtils.outblue("RobotController | dorobotpos x:" + x + " robotName=" + robotName);
        //CommUtils.outblue("RobotController | dorobotpos y:" + y + " robotName=" + robotName);
        try {
            RobotUtils.doRobotPos( x,y );
        } catch (Exception e) {
            CommUtils.outred("RobotController | dorobotpos ERROR:"+e.getMessage());
        }
        return buildThePage(viewmodel);
    }
    @PostMapping("/setrobotpos")
    public String setrobotpos(Model viewmodel,
        @RequestParam String x, @RequestParam String y, @RequestParam String dir ){
        //CommUtils.outblue("RobotController | setrobotpos x:" + x + " robotName=" + robotName);
        //CommUtils.outblue("RobotController | setrobotpos y:" + y + " robotName=" + robotName);
        //CommUtils.outblue("RobotController | setrobotpos d:" + d + " robotName=" + robotName);
        try {
            RobotUtils.setRobotPos( x,y,dir );
        } catch (Exception e) {
            CommUtils.outred("RobotController | setrobotpos ERROR:"+e.getMessage());
        }
        return buildThePage(viewmodel);
    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "BaseController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }
 
/*
 * curl --location --request POST 'http://localhost:8080/move' --header 'Content-Type: text/plain' --form 'move=l'	
 * curl -d move=r localhost:8080/move
 */
}

