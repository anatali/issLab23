package unibo.robotposweb;
//https://www.baeldung.com/websockets-spring
//https://www.toptal.com/java/stomp-spring-boot-websocket

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.utils.CommUtils;


//---------------------------------------------------
//import unibo.Robots.common.RobotUtils;


@Controller 
public class RobotHIController {
    public final static String actorName  = "robotpos";
    protected String mainPage             = "robotposwebNaivegui";
    //protected String mainPage             = "robotposwebGui";


    //Settaggio degli attributi del modello
    @Value("${robotposweb.robotip}")  //vedi application.properties
    String robotip;



    protected String buildThePage(Model viewmodel) {
        setConfigParams(viewmodel);
        return mainPage;
    }

    protected void setConfigParams(Model viewmodel){
        viewmodel.addAttribute("robotip",  robotip);
    }

  @GetMapping("/") 		 
  public String entry(Model viewmodel) {
        //Connection.trace = true;
      return buildThePage(viewmodel);
  }

    @PostMapping("/setrobotip")
    public String setrobotip(Model viewmodel, @RequestParam String ipaddr  ){
        robotip = ipaddr;
        System.out.println("RobotHIController | setrobotip:" + ipaddr );
        viewmodel.addAttribute("robotip", robotip);
        RobotUtils.connectActorUsingTcp(ipaddr);
        //Attivo comunque una connessione CoAP (per osservare la risorsa-attore)
        CoapConnection conn = RobotUtils.connectActorUsingCoap(ipaddr);
        conn.observeResource( new RobotposCoapObserver() );
        return buildThePage(viewmodel);
    }

    @PostMapping("/dorobotpos")
    public String dorobotpos(Model viewmodel , @RequestParam String x, @RequestParam String y ){
        CommUtils.outblue("RobotHIController | dorobotpos x:" + x + " robotName=" + actorName);
        CommUtils.outblue("RobotHIController | dorobotpos y:" + y + " robotName=" + actorName);
        RobotUtils.doRobotPos( x,y );
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

