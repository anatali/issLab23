package console
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import consolegui.ButtonAsGui
import it.unibo.kactor.MsgUtil
import consolegui.Utils
import unibo.basicomm23.msg.ApplMessage
import unibo.basicomm23.utils.CommUtils


class consoleGuiSimple( ) : IObserver {
val stepTime = 350
val connQakSupport = connQakTcp()
val buttonLabels = arrayOf( "w", "s", "l", "r",  "p", "h")
	
    init{
		createTheGui(   )
		Utils.showSystemInfo("after create")
    }

	
	fun createTheGui(    ){
			 var guiName = "GUI-TCP"

 		val concreteButton = ButtonAsGui.createButtons( guiName, buttonLabels )
 	    concreteButton.addObserver( this )
		connQakSupport.createConnection()
		
		engageTheRobot()
	}
	
	fun engageTheRobot() {
		val msg = MsgUtil.buildRequest("console", "engage", "engage(console)", "basicrobot" )
	    val answer = connQakSupport.request( msg )
	    val m = ApplMessage(answer)
		CommUtils.outred("console answer :  $m");
		if( m.msgId() == "engagerefused") {
			CommUtils.outred("WARNING: console not able to work");
		}
	}
	override fun update(o: Observable, arg: Any) {
		var move = arg as String
 		  if( move == "p" ){
			  val msg = MsgUtil.buildRequest("console", "step", "step(345)", "basicrobot" )
			  connQakSupport.request( msg )
		  } 
		  /* else if( move == "e" ){
			  val msg = MsgUtil.buildEvent("console","alarm","alarm(fire)" )
			  connQakSupport.emit( msg )
		  }*/
 		  else{
			  val msg = MsgUtil.buildDispatch("console", "cmd", "cmd($move)", "basicrobot" )
			  connQakSupport.forward( msg )
		  }
	}
}

fun main(){
	println("consoleGuiSimple")
	val appl = consoleGuiSimple(   )
}