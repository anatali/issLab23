package it.unibo.radarSystem22.sprint2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.sprint1.RadarSystemSprint1Main;
import it.unibo.radarSystem22.sprint2.main.sysOnRasp.RadarSysSprint2ControllerOnRaspMain;
import it.unibo.radarSystem22.sprint2a.main.devicesOnRasp.RadarSysSprint2aDevicesOnRaspMain;

public class MainSelectOnRasp {
public HashMap<String,IApplication> programs = new HashMap<String,IApplication>();
	
	protected void outMenu() {
		for (String i : programs.keySet()) { //
			  System.out.println( ""+i + "    " + programs.get(i).getName() );
		}
 	}
	public void doChoice() {
		try {
			programs.put("1", new RadarSystemSprint1Main() );				 
			programs.put("2", new RadarSysSprint2ControllerOnRaspMain());  	 
			programs.put("3", new RadarSysSprint2aDevicesOnRaspMain());  	 
  			String i = "";
			outMenu();
			ColorsOut.outappl(">>>   ", ColorsOut.ANSI_PURPLE);
 			BufferedReader inputr = new BufferedReader(new InputStreamReader(System.in));
			i =  inputr.readLine();
 			programs.get( i ).doJob("DomainSystemConfig.json","RadarSystemConfig.json");
 		} catch ( Exception e) {
			 ColorsOut.outerr("ERROR:" + e.getMessage() );
		}
		
	}
	public static void main( String[] args) throws Exception {
		ColorsOut.outappl("---------------------------------------------------", ColorsOut.BLUE);
		ColorsOut.outappl("MainSelectOnRasp: this application uses Config Files", ColorsOut.BLUE);
		ColorsOut.outappl("---------------------------------------------------", ColorsOut.BLUE);
		new MainSelectOnRasp().doChoice();
	}
}
