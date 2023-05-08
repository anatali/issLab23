/* Generated by AN DISI Unibo */ 
package it.unibo.resourcecore

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Resourcecore ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var ready = true  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("$name waiting ... ")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handleRequestCmd",cond=whenRequestGuarded("cmd",{ ready  
					}))
					transition(edgeName="t01",targetState="handleAlarm",cond=whenEvent("alarm"))
				}	 
				state("handleAlarm") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("alarm(V)"), Term.createTerm("alarm(on)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outred("$name alarm on")
								 ready = false  
						}
						if( checkMsgContent( Term.createTerm("alarm(V)"), Term.createTerm("alarm(off)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outred("$name alarm off")
								 ready = true  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("handleRequestCmd") { //this:State
					action { //it:State
						CommUtils.outblue("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("cmd(X)"), Term.createTerm("cmd(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val arg = payloadArg(0)
								      	 val ANSW = "answerFor_${arg}" 
								answer("cmd", "replytocmd", "replytocmd($ANSW)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
