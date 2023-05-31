package prodCons

val cpus = Runtime.getRuntime().availableProcessors();

fun curThread() : String {
	return "thread=${Thread.currentThread().name} / nthreads=${Thread.activeCount()}"
}
