/* Generated by AN DISI Unibo */ 
package it.unibo.callmock

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Callmock ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("callmock READY")
						CommUtils.outgreen("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						request("cmd", "cmd(w)" ,"resourcecore" )  
						request("cmd", "cmd(h)" ,"resourcecore" )  
						emit("alarm", "alarm(on)" ) 
						request("cmd", "cmd(s)" ,"resourcecore" )  
						request("cmd", "cmd(r)" ,"resourcecore" )  
						delay(2000) 
						emit("alarm", "alarm(off)" ) 
						delay(1000) 
						request("cmd", "cmd(l)" ,"resourcecore" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="handleAnswer",cond=whenReply("replytocmd"))
				}	 
				state("handleAnswer") { //this:State
					action { //it:State
						CommUtils.outgreen("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t03",targetState="handleAnswer",cond=whenReply("replytocmd"))
				}	 
			}
		}
}
