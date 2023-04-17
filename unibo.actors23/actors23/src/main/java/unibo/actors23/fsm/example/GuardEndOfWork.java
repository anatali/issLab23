package unibo.actors23.fsm.example;


import unibo.basicomm23.utils.CommUtils;

public class GuardEndOfWork {
	protected static int vn ;
  	
	public static void setValue( int n ) {
		vn = n;
		CommUtils.outgreen("GuardEndOfWork setValue="+vn  );
	}
	public static boolean checkValue(   ) {
		return vn == 4 ;
	}
 	public boolean eval( ) {
 		boolean b = checkValue();
 		CommUtils.outblue("GuardEndOfWork eval="+b  );
 		return b;
	}

}
