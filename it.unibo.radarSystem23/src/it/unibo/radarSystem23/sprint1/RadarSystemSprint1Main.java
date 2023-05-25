package it.unibo.radarSystem23.sprint1;
 
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem23.IApplication;


/*
 *  
 */

public class RadarSystemSprint1Main implements IApplication{
private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	@Override
	public String getName() {	 
		return this.getClass().getName();
	}

	public void setup( String domainConfig, String systemConfig  )  {
		if( domainConfig != null ) {
			DomainSystemConfig.setTheConfiguration(domainConfig);
		}
		if( systemConfig != null ) {
			RadarSystemConfig.setTheConfiguration(systemConfig); 
		}
		if( domainConfig == null && systemConfig == null) {
  			DomainSystemConfig.testing      	= false;			
 			DomainSystemConfig.tracing      	= false;			
			DomainSystemConfig.sonarDelay       = 200;
			//Su PC
			DomainSystemConfig.simulation   	= true;
			DomainSystemConfig.DLIMIT      		= 70;  
			DomainSystemConfig.ledGui           = true;
			RadarSystemConfig.RadarGuiRemote    = false; //se true non attiva radarGui
		//Su Raspberry (nei file di configurazione)
//			DomainSystemConfig.simulation   	= false;
//			DomainSystemConfig.DLIMIT      		= 12;  
//			DomainSystemConfig.ledGui           = false;
//			RadarSystemConfig.RadarGuiRemote    = true;
		}
 	}
	
 	
	@Override
	public void doJob( String domainConfig, String systemConfig ) {
		setup(domainConfig, systemConfig);
		configure();
		//start
	    ActionFunction endFun = (n) -> { 
	    	System.out.println(n); 
	    	terminate(); 
	    };
		controller.start(endFun, 30);
	}
	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
	    radar      = RadarSystemConfig.RadarGuiRemote ? null : DeviceFactory.createRadarGui();
		BasicUtils.aboutThreads("Before Controller creation | ");
	    //Controller
	    controller = Controller.create(led, sonar, radar);	 
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		BasicUtils.aboutThreads("Before deactivation | ");
		sonar.deactivate();
		System.exit(0);
	}

//Get the system components 
 	public IRadarDisplay getRadarGui() { return radar; }
 	public ILed getLed() { return led; }
 	public ISonar getSonar() { return sonar; }
 	public Controller getController() { return controller; }
	
	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with NO CONFIG files| ");
		//new RadarSystemSprint1Main().doJob(null,null);
 		BasicUtils.aboutThreads("At INIT with  CONFIG files| ");
 		new RadarSystemSprint1Main().doJob("DomainSystemConfig.json","RadarSystemConfig.json");
	    /*
	    //su Rasp:
	    new RadarSystemSprint1Main().doJob(
	           "DomainSystemConfig.json","RadarSystemSprint1Main");
	    */		
 	}

}
