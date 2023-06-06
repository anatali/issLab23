/* Generated by AN DISI Unibo */ 
package it.unibo.called

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Called ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var RequestArg = "0"  
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="handleRequest",cond=whenRequest("r1"))
				}	 
				state("handleRequest") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("r1(X)"), Term.createTerm("r1(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 RequestArg = payloadArg(0)  
								CommUtils.outgreen("$name receives the request r1($RequestArg)")
								CommUtils.outmagenta("$name askfor r2(theta)")
								replyreq("r2", "r1", "r2(theta)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_handleRequest", 
				 	 					  scope, context!!, "local_tout_called_handleRequest", 1000.toLong() )
					}	 	 
					 transition(edgeName="t03",targetState="init",cond=whenTimeout("local_tout_called_handleRequest"))   
					transition(edgeName="t04",targetState="answerAfterAsk",cond=whenReply("a1"))
				}	 
				state("answerAfterAsk") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("a1(X)"), Term.createTerm("a1(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outmagenta("$name receives answer to askFor a1(${payloadArg(0)})")
								 val R = "${RequestArg}_${payloadArg(0)}"  
								CommUtils.outgreen("$name replies to orginal request with a1($R)")
								answer("r1", "a1", "a1($R)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="init", cond=doswitch() )
				}	 
			}
		}
}