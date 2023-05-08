package utils;

import unibo.actor22comm.utils.ColorsOut;

public class basicOps {
	
	public static int fibo(int n) throws Exception{
		if( n < 0) throw new Exception("fibo argument must be >0");
		if( n == 0 || n==1 ) return n;
		int v = fibo(n-1)+fibo(n-2);
		ColorsOut.outappl("fibo=" + v, ColorsOut.CYAN);
		return v;
	}
	
	public static void outMsg(String m){
		ColorsOut.outappl(m, ColorsOut.GREEN);
	}

}
