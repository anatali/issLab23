package it.unibo.radarSystem22.sprint3.interpreters;

 
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
 

/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplInterpreter implements IApplInterpreter  {
private ILed led;
 
	public LedApplInterpreter(  ILed led) {
 		this.led = led;
	}

     
 	public String elaborate( String message ) {
		ColorsOut.out("LedApplInterpreter | elaborate String=" + message  + " led="+led, ColorsOut.GREEN);
	 		if( message.equals("getState") ) return ""+led.getState() ;
	 		else if( message.equals("on")) led.turnOn();
	 		else if( message.equals("off") ) led.turnOff();	
 		return message+"_done";
	}
}
