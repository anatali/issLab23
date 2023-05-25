package it.unibo.radarSystem22.sprint2.main.sysOnRasp;

import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.tcp.TcpServer;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.sprint1.RadarSystemConfig;
import it.unibo.radarSystem22.sprint2.handlers.RadarApplHandler;

/*
 * Attiva il Controller (vedi sprint1) e il RadarDisplay (vedi domain)
 * e due proxy al Led e al Sonar.
 * 
 */
public class RadarSysSprint2RadarOnPcMain implements IApplication{
	private IRadarDisplay radar;
	private TcpServer server ;
	
	@Override
	public void doJob(String domainConfig, String systemConfig ) {
		setup( );
		configure();
		execute();
	}
	
	public void setup(  )  {	
 		RadarSystemConfig.serverPort        = 8080;		
 		RadarSystemConfig.hostAddr          = "localhost";
	}
	
	public void configure(  )  {	
 		int port = RadarSystemConfig.serverPort;		
   		radar  	 = DeviceFactory.createRadarGui();
  	    IApplMsgHandler radarh = new RadarApplHandler("radarh", radar);
 	    server  = new TcpServer("pcServer",port,radarh );
	}
	public void execute() {
		server.activate();
	}
	public void terminate() {
 		BasicUtils.aboutThreads("Before deactivation | ");
		System.exit(0);
	}	
	
	@Override
	public String getName() {
		return this.getClass().getName() ; //"RadarSystemSprint2OnPcMain";
	}

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with NO CONFIG files| ");
		new RadarSysSprint2RadarOnPcMain().doJob( null,null );
  	}	
}
