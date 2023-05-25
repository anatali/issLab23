package it.unibo.radarSystem22.sprint3.interpreters;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
 

public class SonarApplInterpreter implements IApplInterpreter{
private	ISonar sonar;

	public SonarApplInterpreter(ISonar sonar) {
		this.sonar = sonar;
	}

 
	@Override
	public String elaborate(String message) {
		ColorsOut.out("SonarApplInterpreter | elaborate " + message, ColorsOut.BLUE);
			if( message.equals("getDistance")) {
				//ColorsOut.out(name+ " | elaborate getDistance="  , ColorsOut.BLUE);
				return ""+sonar.getDistance().getVal();
 			}else if( message.equals("activate")) {
				ColorsOut.out("SonarApplInterpreter | activate sonar="+sonar , ColorsOut.BLUE);
				sonar.activate();
			}else if( message.equals("deactivate")) {
				sonar.deactivate();
			}else if( message.equals("isActive")) {
				return ""+sonar.isActive();
 			}
 		return message+"_done";
	}
	

}
