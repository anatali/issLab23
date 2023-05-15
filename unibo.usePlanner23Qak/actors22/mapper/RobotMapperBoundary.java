package mapper;

 


/*
 * Il robot si muove lungo il boundary facendo step pari alla sua lunghezza.
 * Ad ogni step fatto con successo, aggiorna la mappa con "w"
 * Ad una collisione ...
 */
public class RobotMapperBoundary extends QakActor22FsmAnnot  {
	private Interaction2021 conn;	
 	private int NumStep   = 0;
	
 	public RobotMapperBoundary(String name) {
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
	@Transition( state = "doAheadMove"  ) //detectBoundary
 	protected void robotStart( IApplMessage msg ) {
		initPlanner();
		NumStep++ ;
		planner22Util.showCurrentRobotState();
	}
 
	@State( name = "doAheadMove" )
	@Transition( state = "stepDone",   msgId="endMoveOk"     )
	@Transition( state = "stepFailed", msgId="endMoveKo"     )
  	protected void doAheadMove( IApplMessage msg ) {
		outInfo(""+msg);
		VRobotMoves.step(getName(), conn );
	}
	@State( name = "stepDone" )
	@Transition( state = "doAheadMove"  )
  	protected void stepDone( IApplMessage msg ) {
		outInfo(""+msg);
		planner22Util.updateMap(  "w", "stepDone" );
		CommUtils.delay(500);
	}
	@State( name = "stepFailed" )
	@Transition( state = "backPosDone", msgId="endMoveOk"  )
	//@Transition( state = "endWork",     guard="roundCompleted"   )
  	protected void stepFailed( IApplMessage msg ) {
		outInfo(""+msg  );
		ColorsOut.outappl("FOUND A WALL - atHome="+planner22Util.atHome(), ColorsOut.MAGENTA);
		//planner22Util.showMap();
		String map = planner22Util.getMap();
		ColorsOut.outappl(map, ColorsOut.GREEN);
		updateResourceRep( map  );
		//if( ! planner22Util.atHome() ) {
			JSONObject json = new JSONObject(msg.msgContent().replace("'", ""));
			int duration = json.getInt("duration") ;  //TODO
			if( duration > 100 ) duration = duration -50;  //TUNING ....
			ColorsOut.outappl("duration=" + duration, ColorsOut.YELLOW);
			VRobotMoves.moveBackward(getName(), conn, duration);
		//}
	}
	
	@State( name = "backPosDone" )
	@Transition( state = "doAheadMove",  msgId="endMoveOk", guard="roundNotCompleted"   ) //"detectBoundary"
	@Transition( state = "endWork",     guard="roundCompleted"   )
	protected void backPosDone( IApplMessage msg ) {
		outInfo(""+msg);
		NumStep++ ;
		planner22Util.updateMap(  "l","turn" );
		VRobotMoves.turnLeft(getName(), conn);
	}	
	
 	
 	@State( name = "endWork" )
 	protected void endWork( IApplMessage msg ) {
 		planner22Util.showMap();
 		planner22Util.saveRoomMap("mapRoomEmpty");
		outInfo("BYE" );	
 		//System.exit(0);
 	}

//----------------------------------------------
 
		@TransitionGuard
		protected boolean roundNotCompleted() {
			outInfo( "roundNotCompleted  " + (NumStep < 5) );
			return NumStep < 5;
		}	
		@TransitionGuard
		protected boolean roundCompleted() {
			outInfo( "roundCompleted  " + (NumStep == 5) );
			return NumStep == 5;
		}	
 	 
  		
 
}
