/* Generated by AN DISI Unibo */ 
package it.unibo.sonarobs

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonarobs ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t021",targetState="handleSonarData",cond=whenEvent("sonardata"))
				}	 
				state("handleSonarData") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						forward("stopcmd", "stop(0)" ,"appl" ) 
						delay(2000) 
						forward("resumecmd", "resume(0)" ,"appl" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="ignoreSonarData", cond=doswitch() )
				}	 
				state("ignoreSonarData") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_ignoreSonarData", 
				 	 					  scope, context!!, "local_tout_sonarobs_ignoreSonarData", 500.toLong() )
					}	 	 
					 transition(edgeName="t022",targetState="s0",cond=whenTimeout("local_tout_sonarobs_ignoreSonarData"))   
					transition(edgeName="t023",targetState="ignoreSonarData",cond=whenEvent("sonardata"))
				}	 
			}
		}
}
