/* Generated by AN DISI Unibo */ 
package it.unibo.caller

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Caller ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						CommUtils.outblack("$name  request r1(10)")
						request("r1", "r1(10)" ,"called" )  
						delay(1000) 
						request("r1", "r1(20)" ,"called" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handleReply",cond=whenReply("a1"))
					transition(edgeName="t01",targetState="handleAskFromCalled",cond=whenRequest("r2"))
				}	 
				state("handleReply") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleAskFromCalled") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("r2(X)"), Term.createTerm("r2(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("$name receives ask from called r2(${payloadArg(0)})")
								CommUtils.outblue("$name  replies with a1(90)")
								answer("r2", "a1", "a1(90)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
