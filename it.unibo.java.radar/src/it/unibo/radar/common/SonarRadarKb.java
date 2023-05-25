package it.unibo.radar.common;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SonarRadarKb {
 
	public static final int winWith=600;
	public static final int radarRange = 100000; //80000 = 80 cmm;
	
	public static int connP2PPort = 8010;  
	public static int connPythonPort = 8044;  
 	public static final String sensorEvent = "sensorEvent";
	public static final String sonarEvent = "sonarEvent";
	
 
	public static final ScheduledExecutorService  executor 	= Executors.newScheduledThreadPool(8);
}
