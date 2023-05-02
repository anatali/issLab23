/* Generated by AN DISI Unibo */ 
package it.unibo.console

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Console ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						 gui.GuiUtils.createGui(myself)  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitevents", cond=doswitch() )
				}	 
				state("waitevents") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t027",targetState="handleGuicmd",cond=whenEvent("guicmd"))
					transition(edgeName="t028",targetState="handleGuigetpath",cond=whenEvent("getpath"))
				}	 
				state("handleGuicmd") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("guicmd(ARG)"), Term.createTerm("guicmd(start)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CommUtils.outmagenta("console start")  
								forward("startcmd", "startcmd(console)" ,"appl" ) 
						}
						if( checkMsgContent( Term.createTerm("guicmd(ARG)"), Term.createTerm("guicmd(stop)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  CommUtils.outmagenta("console stop")  
								forward("stopcmd", "stopcmd(console)" ,"appl" ) 
						}
						if( checkMsgContent( Term.createTerm("guicmd(ARG)"), Term.createTerm("guicmd(resume)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  CommUtils.outmagenta("console resume")  
								forward("resumecmd", "resumecmd(console)" ,"appl" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitevents", cond=doswitch() )
				}	 
				state("handleGuigetpath") { //this:State
					action { //it:State
						request("getpath", "getpath(gui)" ,"appl" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t029",targetState="handlepathanswer",cond=whenReply("pathamswer"))
				}	 
				state("handlepathanswer") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("pathamswer(ARG)"), Term.createTerm("pathamswer(A)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("PATH= ${payloadArg(0)}")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitevents", cond=doswitch() )
				}	 
			}
		}
}