package it.unibo.radarSystem23.sprint2.main.sysOnRasp;

 
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint2.proxy.RadarGuiClient;
import it.unibo.radarSystem23.IApplication;
import it.unibo.radarSystem23.sprint1.ActionFunction;
import it.unibo.radarSystem23.sprint1.Controller;
import it.unibo.radarSystem23.sprint1.RadarSystemConfig;
import it.unibo.radarSystem23.sprint1.RadarSystemSprint1Main;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.tcp.TcpServer;
import unibo.basicomm23.utils.CommUtils;
 
 
/*
 * Attiva il sistema su Raspberry, Controller compreso.
 * Accede al RadarDisplay su PC tramite proxy.
 * 
 */
public class RadarSysSprint2ControllerOnRaspMain implements IApplication{
	private ISonar sonar;
	private ILed  led ;
	private IRadarDisplay radar;
	private Controller controller;

	@Override
	public void doJob(String domainConfig, String systemConfig) {
		setup(domainConfig,   systemConfig);
		configure();
		execute();
	}
	
	public void setup( String domainConfig, String systemConfig )  {
	    BasicUtils.aboutThreads(getName() + " | Before setup ");
		if( domainConfig != null ) {
			DomainSystemConfig.setTheConfiguration(domainConfig);
		}
		if( systemConfig != null ) {
			RadarSystemConfig.setTheConfiguration(systemConfig);
		}
		if( domainConfig == null && systemConfig == null) {
			DomainSystemConfig.simulation  = true;
	    	DomainSystemConfig.testing     = false;			
	    	DomainSystemConfig.tracing     = false;			
			DomainSystemConfig.sonarDelay  = 200;
	    	DomainSystemConfig.ledGui      = false;			
	    	
			RadarSystemConfig.RadarGuiRemote    = true;		
			RadarSystemConfig.serverPort        = 8080;		
			RadarSystemConfig.hostAddr          = "192.168.1.132";
	    	RadarSystemConfig.DLIMIT            = 75;
		}
	}
	protected void configure() {		
 	    sonar      = DeviceFactory.createSonar();
 	    led        = DeviceFactory.createLed();
	    radar      = new  RadarGuiClient("radarClient", 
	    		      RadarSystemConfig.hostAddr, 
		              ""+RadarSystemConfig.serverPort, 
		              ProtocolType.tcp);
	    //Controller
	    controller = Controller.create(led, sonar, radar);	 		
 	
	}
	
	protected void execute() {
		//start
	    ActionFunction endFun = (n) -> { 
	    	System.out.println(n); 
	    	terminate(); 
	    };
		//int d = radar.getCurDistance();
		//CommUtils.outmagenta(getName() + " | CURRENT DISTANCE answer=" + d  );
		controller.start(endFun, 30);		
	}
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		int d = radar.getCurDistance();
		CommUtils.outmagenta(getName() + " |CURRENT DISTANCE answer=" + d  );		
		sonar.deactivate();
		System.exit(0);
	}	
	@Override
	public String getName() {
		return this.getClass().getName() ;  
	}

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with NO CONFIG files| ");
		//new RadarSysSprint2ControllerOnRaspMain().doJob(null,null);
		new RadarSysSprint2ControllerOnRaspMain().doJob(null,null);
  	}
}
