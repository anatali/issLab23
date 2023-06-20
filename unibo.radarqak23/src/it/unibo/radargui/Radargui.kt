/* Generated by AN DISI Unibo */ 
package it.unibo.radargui

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Radargui ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var DoReply     = false
		  var DistanceStr  = "0"
		  var Distance     = 0
		  var Angle        = "0"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("radargui start")
						radarPojo.radarSupport.setUpRadarGui(  )
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitForDataToShow", cond=doswitch() )
				}	 
				state("waitForDataToShow") { //this:State
					action { //it:State
						CommUtils.outblack("radargui waiting ... ")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="showSpotReply",cond=whenRequest("polar"))
					transition(edgeName="t01",targetState="showSpotNoReply",cond=whenDispatch("polar"))
					transition(edgeName="t02",targetState="showSpotNoReply",cond=whenEvent("polar"))
					transition(edgeName="t03",targetState="showSpot2023",cond=whenEvent("sonardata"))
				}	 
				state("showSpotNoReply") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("polar(D,A)"), Term.createTerm("polar(D,A)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												DistanceStr = payloadArg(0)
											    Distance    = DistanceStr.toInt()
												Angle       = payloadArg(1) 
								                DoReply     = false
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="showSpot", cond=doswitch() )
				}	 
				state("showSpotReply") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("polar(D,A)"), Term.createTerm("polar(D,A)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												DistanceStr = payloadArg(0)
											    Distance    = DistanceStr.toInt()
												Angle       = payloadArg(1) 
								                DoReply     = true
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="showSpot", cond=doswitch() )
				}	 
				state("showSpot") { //this:State
					action { //it:State
						if(  Distance <= 90  
						 ){radarPojo.radarSupport.update( DistanceStr, Angle  )
						if(  DoReply  
						 ){answer("polar", "fromRadarGui", "fromRadarGui(done($DistanceStr))"   )  
						}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitForDataToShow", cond=doswitch() )
				}	 
				state("showSpot2023") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("distance(V)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 DistanceStr = payloadArg(0); Distance = DistanceStr.toInt()  
								if(  Distance <= 90  
								 ){radarPojo.radarSupport.update( DistanceStr, "0"  )
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="showSpot2023",cond=whenEvent("sonardata"))
				}	 
			}
		}
}
