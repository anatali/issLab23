package unibo.actors23.fsm.example;
import unibo.actors23.ActorContext23;
import unibo.actors23.annotations.State;
import unibo.actors23.annotations.Transition;
import unibo.actors23.annotations.TransitionGuard;
import unibo.actors23.fsm.ActorBasicFsm23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;

public class BoundaryWalkerAnnot extends ActorBasicFsm23 {
	public static final String endMoveOkId   = "endMoveOk";
	public static final String endMoveKoId   = "endMoveKo";
	private String guardContinue  = GuardContinueWork.class.getName();
	private String guardEndOfWork = GuardEndOfWork.class.getName();

	private Interaction conn;	
 	private int ncorner  = 0;
	private VrobotHLMovesInteractionAsynch vr;



 	public BoundaryWalkerAnnot(String name, ActorContext23 ctx) {
		super(name,ctx);
		this.autostart = true;
 	}


	@State( name = "robotInit", initial=true)
	@Transition( state = "robotMoving" ,  msgId =  endMoveOkId )
	@Transition( state = "wallDetected" , msgId =  endMoveKoId )
	protected void robotStart( IApplMessage msg ) {
		CommUtils.outblue(""+msg + " connecting (blocking all the actors ) ... ");
 		conn = WsConnection.create("localhost:8091" ); 	 
 		CommUtils.outblue("connected "+conn);
		vr = new VrobotHLMovesInteractionAsynch( conn, this  );
   		//((WsConnection)conn).addObserver( new WsConnWEnvObserver(getName()) );
		try {
			vr.stepAsynch(350);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
 	@State( name = "robotMoving" )
 	@Transition( state = "robotMoving" ,  msgId =  endMoveOkId)
  	@Transition( state = "wallDetected" , msgId =  endMoveKoId )
	protected void robotMoving( IApplMessage msg ) {
		CommUtils.outblue("robotMoving "+msg);
		try {
			vr.stepAsynch( 350 );
			CommUtils.delay(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 	
 	@State( name = "wallDetected" )
	@Transition( state = "robotMoving" , msgId =  endMoveOkId, guard="notCompleted"   )
	@Transition( state = "endWork" ,     msgId =  endMoveOkId, guard="completed" )
	protected void wallDetected( IApplMessage msg ) {
		CommUtils.outblue("wallDetected ncorner="+ ncorner + " " + msg);
		ncorner++;
		try {
			vr.turnLeft( );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@State( name = "turnLeft" )
	@Transition( state = "robotMoving" , msgId =  endMoveOkId, guard="notCompleted"   )
	@Transition( state = "endWork" ,     msgId =  endMoveOkId, guard="completed" )
	protected void turnLeft(){
		try {
			vr.turnLeft( );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



 	@State( name = "endWork" )
 	protected void endWork( IApplMessage msg ) {
 		//VRobotMoves.turnLeft(getName(), conn);
		CommUtils.outblue("endWork BYE" );
 		System.exit(0);
 	}

//----------------------------------------------
	@TransitionGuard
	protected boolean completed() {
		CommUtils.outblue("GUARD completed "+ (ncorner == 4 ) );
		return ncorner == 4 ;
	}
	@TransitionGuard
	protected boolean notCompleted() {
		CommUtils.outblue("GUARD notCompleted "+ (ncorner < 4 ) );
		return ncorner < 4 ;
	}	
}
