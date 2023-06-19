package consoles

import connQak.ConnectionType
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import connQak.connQakBase
import consolegui.ButtonAsGui
import it.unibo.kactor.MsgUtil
import consolegui.Utils
import unibo.basicomm23.msg.ApplMessage
import unibo.basicomm23.utils.CommUtils


class consoleGuiSimple( ) : IObserver {
val stepTime = 350
lateinit var connQakSupport : connQakBase
//val buttonLabels = arrayOf("e","w", "s", "l", "r", "z", "x", "b", "p", "h")
val buttonLabels = arrayOf( "w", "s", "l", "r",  "p", "h")
val myname = "gui23xyz9526"

    init{
		println("consoleGuiSimple $myname")
		createTheGui( connQak.connprotocol )
		Utils.showSystemInfo("after create")
    }

	
	fun createTheGui( connType:ConnectionType  ){
			 var guiName = ""
			 when( connType ){
				 ConnectionType.COAP -> { guiName="GUI-COAP"}
				 ConnectionType.MQTT -> { guiName="GUI-MQTT"}
				 ConnectionType.TCP  -> { guiName="GUI-TCP" }
				 ConnectionType.HTTP -> { guiName="GUI-HTTP"}
			 }
 		val concreteButton = ButtonAsGui.createButtons( guiName, buttonLabels )
 	    concreteButton.addObserver( this )
		connQakSupport = connQakBase.create( connType )
		connQakSupport.createConnection()
		
		engageTheRobot()
	}
	
	fun engageTheRobot() {
		val msg = MsgUtil.buildRequest(myname, "engage", "engage($myname,330)", connQak.qakdestination )
	    val answer = connQakSupport.request( msg )
	    val m = ApplMessage(answer)
		CommUtils.outred("console answer :  $m");
		if( m.msgId() == "engagerefused") {
			CommUtils.outred("WARNING: console not able to work");
		}
	}
	override fun update(o: Observable, arg: Any) {
		CommUtils.outblue("update :  $arg");

		var move = arg as String
 		  if( move == "p" ){
			  val msg = MsgUtil.buildRequest(myname, "step", "step(345)", connQak.qakdestination )
			  val answer = connQakSupport.request( msg )
			  CommUtils.outblue("step answer :  $answer");
 		  }
		  /* else if( move == "e" ){
			  val msg = MsgUtil.buildEvent(myname,"alarm","alarm(fire)" )
			  connQakSupport.emit( msg )
		  }*/
 		  else{
			  val msg = MsgUtil.buildDispatch(myname, "cmd", "cmd($move)", connQak.qakdestination )
			  connQakSupport.forward( msg )
		  }
	}
}

fun main(){
	val appl = consoleGuiSimple(   )
}