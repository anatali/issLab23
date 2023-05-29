package unibo.basicomm23.msg;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommSystemConfig;

public abstract class ApplMsgHandler  implements IApplMsgHandler {  
protected String name;
   
 	public ApplMsgHandler( String name  ) {  
		this.name = name;
	}
 	
 	public String getName() {
		return name;
	}	 
 	public abstract void elaborate(IApplMessage message, Interaction  conn) ;
 	
}
