package unibo.actors23.annotations;


public class GuardAlwaysTrue implements IGuard{
	@Override
  	public boolean eval( ) {
 		//ColorsOut.outappl("GuardAlwaysTrue eval" , ColorsOut.ANSI_YELLOW);
 		return true;
	}

}
