/* Generated by AN DISI Unibo */ 
package it.unibo.ctx1
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "appl1.pl", "sysRules.pl","ctx1"
	)
}
