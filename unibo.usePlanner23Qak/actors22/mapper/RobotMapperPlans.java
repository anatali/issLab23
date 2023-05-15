package mapper;

 

public class RobotMapperPlans extends QakActor22FsmAnnot  {
	private Interaction2021 conn;	
  	private int lastX    = 6;
 	private int lastY    = 5; 
	private String CurrentPlannedMove = "";
 	
 	public RobotMapperPlans(String name) {
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
	@Transition( state = "doMove"  )
 	protected void robotStart( IApplMessage msg ) {
		initPlanner();
		planner22Util.planForGoal(""+lastX,""+lastY);		
	}
 

	
	//TODO: introduce an executor
	@State( name = "doMove" )
	@Transition( state = "doMove",      msgId="endMoveOk", guard="otherMoves"    )
 	@Transition( state = "hitWall",     msgId="endMoveKo"     )
 	//@Transition( state = "backToHome",  msgId="endMoveOk", guard="noOtherMoves"  )
 	@Transition( state = "backToHome",  guard="noOtherMoves"  )
	protected void doMove( IApplMessage msg ) {
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
			//planner22Util.showMap();
			planner22Util.showCurrentRobotState();
		}
		CommUtils.delay(500);
	}
	
	@State( name = "hitWall" )
	//@Transition( state = "backDone", msgId="endMoveOk")
	@Transition( state = "doMove")
	protected void hitWall( IApplMessage msg ) {
		outInfo(""+msg);
		ColorsOut.outerr("hitWall not expected"  );		
	} 
	

 	
	
	@State( name = "backToHome" )
	@Transition( state = "doMove",  guard="notAtHome"  )
	@Transition( state = "endWork", guard="atHome"  )
	protected void backToHome( IApplMessage msg ) {
		boolean alreadyAtHome = planner22Util.atHome();
		outInfo("alreadyAtHome="+alreadyAtHome);
		if( ! alreadyAtHome ) planner22Util.planForGoal("0" ,"0" );	
		//CommUtils.waitTheUser("going to home");
	}	
	
 	@State( name = "endWork" )
 	protected void endWork( IApplMessage msg ) {
 		//VRobotMoves.turnLeft(getName(), conn);
		outInfo("BYE" );	
 		System.exit(0);
 	}

//----------------------------------------------
 
 	 
 		@TransitionGuard
 		protected boolean otherMoves() {
 			outInfo( "otherMoves  " + CurrentPlannedMove.length());
 			return CurrentPlannedMove.length() > 0;
 		}	
 		
 		@TransitionGuard
 		protected boolean noOtherMoves() {
 			outInfo( "noOtherMoves  " + CurrentPlannedMove.length());
 			return CurrentPlannedMove.length() == 0;
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
