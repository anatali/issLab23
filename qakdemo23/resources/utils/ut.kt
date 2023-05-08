import unibo.basicomm23.utils.CommUtils


object ut {
	
	fun fibo(n: Int) : Int {
		if( n < 0 ) throw Exception("fibo argument must be >0")
		if( n == 0 || n==1 ) return n
		var v = fibo(n-1)+fibo(n-2)
		//outMsg("fibo=" + v );
		return v
	}
	
	fun outMsg( m: String ){
		CommUtils.outgreen( m );
	}
	
	fun showcNumCpus() {
	    val cpus = Runtime.getRuntime().availableProcessors()
	    outMsg("NUMERO DI CPU=$cpus")
	}
}