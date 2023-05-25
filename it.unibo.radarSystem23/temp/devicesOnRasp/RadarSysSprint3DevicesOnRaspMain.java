package it.unibo.radarSystem22.sprint3.main.devicesOnRasp;

 
import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.enablers.EnablerAsServer;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.tcp.TcpServer;
import it.unibo.comm2022.udp.giannatempo.UdpServer;
import it.unibo.comm2022.utils.CommSystemConfig;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.RadarSystemConfig;
import it.unibo.radarSystem22.sprint2a.handlers.LedApplHandler;
import it.unibo.radarSystem22.sprint2a.handlers.SonarApplHandler;
 
 
/*
 * Attiva il TCPServer.
 * 
 */
public class RadarSysSprint3DevicesOnRaspMain implements IApplication{
	private ISonar sonar;
	private ILed  led ;
	private EnablerAsServer ledEnabler ;
	private EnablerAsServer sonarEnabler ;
 

	@Override
	public void doJob(String domainConfig, String systemConfig) {
		setup(domainConfig,   systemConfig);
		configure();
		execute();
	}
	
	public void setup( String domainConfig, String systemConfig )  {
	    BasicUtils.aboutThreads(getName() + " | Before setup ");
	    CommSystemConfig.tracing            = true;
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
	    	DomainSystemConfig.ledGui      = true;		//se siamo su PC	
	
			RadarSystemConfig.tracing           = false;		
			RadarSystemConfig.RadarGuiRemote    = true;		
			RadarSystemConfig.protcolType       = ProtocolType.tcp;		
			RadarSystemConfig.ledPort           = 8010;
		}
 
	}
	protected void configure() {		
	   ProtocolType protocol = RadarSystemConfig.protcolType;
	   led                   = DeviceFactory.createLed();
 	   IApplMsgHandler ledh  = LedApplHandler.create("ledh", led);
	   sonar      = DeviceFactory.createSonar();
 	   IApplMsgHandler sonarh = SonarApplHandler.create("sonarh", sonar);
	   
 	  //Gli enablers
 	  sonarEnabler = new EnablerAsServer("sonarSrv", RadarSystemConfig.sonarPort,
 	              protocol, sonarh );
 	  ledEnabler   = new EnablerAsServer("ledSrv", RadarSystemConfig.ledPort,
 	              protocol, ledh  );
  	}
	
	protected void execute() {		
		ledEnabler.start();
		sonarEnabler.start();
	}
	
	@Override
	public String getName() {
		return this.getClass().getName() ;  
	}

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with NO CONFIG files| ");
		new RadarSysSprint3DevicesOnRaspMain().doJob(null,null);
  	}
}
