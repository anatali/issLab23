package mapper;



/*
 * Il robot si muove lungo il boundary facendo step pari alla sua lunghezza.
 * Ad ogni step fatto con successo, aggiorna la mappa con "w"
 * Ad una collisione ...
 */
public class RobotUsingMap extends QakActor22FsmAnnot  {
	private Interaction conn;	
 	private String CurrentPlannedMove = "";
	private int NumStep   = 0;
	
 	public RobotUsingMap(String name) {
		super(name);
 	}
 	
	protected void init() {
 		ColorsOut.outappl(getName() + " | ws connecting ...." ,  ColorsOut.BLUE);
		conn = WsConnection.create("localhost:8091" ); 
		IObserver robotMoveObserver = new WsConnApplObserver(getName(), true);  //endMOve after turn
		((WsConnection)conn).addObserver(robotMoveObserver);
 		ColorsOut.outappl(getName() + " | conn:" + conn,  ColorsOut.BLUE);
	} 	
	
	protected void initPlanner() {
		try {
			planner22Util.initAI();
	     	ColorsOut.outappl("INITIAL MAP", ColorsOut.CYAN);
	     	planner22Util.loadRoomMap("mapRoomEmpty");
	 		planner22Util.showMap();
			planner22Util.startTimer();  		
 		} catch (Exception e) {
			ColorsOut.outerr(getName() + " in start ERROR:"+e.getMessage());
 		}		
	} 	 
	
	@State( name = "activate", initial=true)
	@Transition( state = "robotStart",   msgId= SystemData.startSysCmdId  )
	protected void activate( IApplMessage msg ) {
		outInfo(""+msg);
		init();
 	}
	
	@State( name = "robotStart" )
	@Transition( state = "execPlannedMoves"  )
 	protected void robotStart( IApplMessage msg ) {
		initPlanner();
 		planner22Util.showCurrentRobotState();
 		planner22Util.planForGoal("4", "2");
	}
 
 
	
	@State( name = "execPlannedMoves" )
	@Transition( state = "execPlannedMoves",  msgId="endMoveOk", guard="otherMoves"    )
	@Transition( state = "backToHome",        msgId="endMoveOk", guard="noOtherMoves"  )
 	@Transition( state = "endWork",           msgId="endMoveOk", guard="atHome"  )
	protected void execPlannedMoves( IApplMessage msg ) {
		outInfo(""+msg);
		CurrentPlannedMove = planner22Util.getNextPlannedMove();
		outInfo("CurrentPlannedMove ==== "+CurrentPlannedMove);
		if( CurrentPlannedMove.equals( "w" ) ){
			planner22Util.updateMap( "w", "doing w" );
			VRobotMoves.step(getName(), conn );
		}else if( CurrentPlannedMove.equals( "l" )  ){
			planner22Util.updateMap( "l", "doing l" );
			VRobotMoves.turnLeft(getName(), conn );
		}else if(  CurrentPlannedMove.equals( "r" )  ){
			planner22Util.updateMap( "r", "doing r" );
			VRobotMoves.turnRight(getName(), conn );
		}else {
			ColorsOut.outappl("doMove terminated", ColorsOut.MAGENTA);	
			VRobotMoves.turnLeft(getName(), conn );
			planner22Util.updateMap( "l", "doing l" );
		}
	}

	@State( name = "backToHome" )
	@Transition( state = "execPlannedMoves"  )
	protected void backToHome( IApplMessage msg ) {
		outInfo(""+msg);
		CommUtils.waitTheUser("click to return at HOME");
		planner22Util.planForGoal("0", "0");
	} 	
	
 	@State( name = "endWork" )
 	protected void endWork( IApplMessage msg ) {
 		planner22Util.showMap();
		outInfo("BYE" );	
 		System.exit(0);
 	}

//----------------------------------------------
 
		@TransitionGuard
		protected boolean roundNotCompleted() {
			outInfo( "roundNotCompleted  " + NumStep);
			return NumStep < 5;
		}	
		@TransitionGuard
		protected boolean roundCompleted() {
			outInfo( "roundCompleted  " + NumStep);
			return NumStep == 5;
		}	
 	 
 		@TransitionGuard
 		protected boolean otherMoves() {
 			outInfo( "otherMoves  " + CurrentPlannedMove.length());
 			return CurrentPlannedMove.length() > 0;
 		}	
 		
 		@TransitionGuard
 		protected boolean noOtherMoves() {
 			outInfo( "noOtherMoves  " + CurrentPlannedMove.length());
 			return CurrentPlannedMove.length() == 0 ;
 		}	
 		
 		@TransitionGuard
 		protected boolean atHome() {
 			ColorsOut.outappl("atHome:"+planner22Util.atHome(), ColorsOut.GREEN);	
 			return planner22Util.atHome();
 		}	
 		
 		@TransitionGuard
 		protected boolean notAtHome() {
 			return ! planner22Util.atHome();
 		}	

}
