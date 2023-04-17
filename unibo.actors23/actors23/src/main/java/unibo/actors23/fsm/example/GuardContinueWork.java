package unibo.actors23.fsm.example;


import unibo.basicomm23.utils.CommUtils;

public class GuardContinueWork {
	protected static int vn ;
  	
	public static void setValue( int n ) {
		vn = n;
		CommUtils.outgreen("GuardContinueWork setValue="+vn  );
	}
	public static boolean checkValue(   ) {
		return vn < 4 ;
	}
 	public boolean eval( ) {
 		boolean b = checkValue();
 		CommUtils.outgreen("GuardContinueWork eval="+b  );
 		return b;
	}

}
