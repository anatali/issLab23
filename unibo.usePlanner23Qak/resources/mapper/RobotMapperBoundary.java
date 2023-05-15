package mapper;


import unibo.actors23.ActorContext23;
import unibo.actors23.annotations.State;
import unibo.actors23.annotations.Transition;
import unibo.actors23.annotations.TransitionGuard;
import unibo.actors23.fsm.ActorBasicFsm23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.tcp.TcpConnection;
import unibo.basicomm23.utils.CommUtils;
import unibo.planner23.Planner23Util;

import java.io.IOException;

/*
 * Il robot si muove lungo il boundary facendo step pari alla sua lunghezza.
 * Ad ogni step fatto con successo, aggiorna la mappa con "w"
 * Ad una collisione ...
 */
public class RobotMapperBoundary extends ActorBasicFsm23 {
	private Interaction conn;	
 	private int NumStep   = 0;
 	private Planner23Util planner = new Planner23Util();

	public RobotMapperBoundary(String name, ActorContext23 ctx) {
		super(name, ctx);
	}
 	
	protected void init() throws Exception { //called by activate
 		CommUtils.outblue(getName() + " | connecting to the basicrobot...." + name );
		conn = TcpConnection.create("localhost", 8020 );
		//conn = WsConnection.create("localhost:8091" );
		//IObserver robotMoveObserver = new WsConnApplObserver(getName(), true);  //endMOve after turn
		//((WsConnection)conn).addObserver(robotMoveObserver);
 		CommUtils.outblue(getName() + " | conn:" + conn);
		IApplMessage engageReq = CommUtils.buildRequest(name, "engage", "engage("+name+")","basicrobot");
		IApplMessage answer = conn.request(engageReq);
		CommUtils.outblue(getName() + " | engage answer:" + answer);
		if( answer.msgContent().contains("engagedone") ) initPlanner();
	} 	
	
	protected void initPlanner() {
		try {
			planner.initAI();
	     	CommUtils.outblue("INITIAL MAP" );
	 		planner.showMap();
			//IApplMessage startMsg = CommUtils.buildDispatch(name, "start", "start("+name+")","mapper");
			//CommUtils.outyellow("initPlanner ->"+startMsg);

 		} catch (Exception e) {
			CommUtils.outred(getName() + " in start ERROR:"+e.getMessage());
 		}		
	} 	 
	
	@State( name = "activate", initial=true)
	@Transition( state = "robotStart" ) //,   msgId= "start"
	protected void activate( IApplMessage msg ) throws Exception {
		CommUtils.outyellow(""+msg);
		init();
 	}
	
	@State( name = "robotStart" )
	@Transition( state = "doAheadMove"  ) //detectBoundary
 	protected void robotStart( IApplMessage msg ) {
		initPlanner();
		//NumStep++ ;
		planner.showCurrentRobotState();

	}
 
	@State( name = "doAheadMove" )
	@Transition( state = "stepDone",   msgId="endMoveOk"     )
	@Transition( state = "stepFailed", msgId="endMoveKo"     )
  	protected void doAheadMove( IApplMessage msg ) throws Exception {
		CommUtils.outyellow(""+msg);
		//VRobotMoves.step(getName(), conn );
		IApplMessage stepReq = CommUtils.buildRequest(name, "step", "step(345)","basicrobot");
		CommUtils.outyellow("doAheadMove ->"+stepReq);
		IApplMessage stepRes = conn.request(stepReq);
		CommUtils.outyellow("doAheadMove stepRes:"+stepRes);
		if( stepRes.msgContent().contains("stepdone")){
			IApplMessage okMsg = CommUtils.buildDispatch(name, "endMoveOk", "endMoveOk("+name+")","mapper");
			CommUtils.outyellow("doAheadMove ->"+okMsg);
			this.autoMsg(okMsg);
		}else{
			IApplMessage koMsg = CommUtils.buildDispatch(name, "endMoveKo", "endMoveOk("+name+")","mapper");
			CommUtils.outyellow("doAheadMove ->"+koMsg);
			this.autoMsg(koMsg);
		}
	}
	@State( name = "stepDone" )
	@Transition( state = "doAheadMove"  )
  	protected void stepDone( IApplMessage msg ) {
		CommUtils.outyellow(""+msg);
		planner.updateMap(  "w", name+ " stepDone" );
		CommUtils.delay(500);
	}
	@State( name = "stepFailed" )
	@Transition( state = "doAheadMove", msgId="goon"  )
	@Transition( state = "endOfWork",     msgId="endwork"  )
  	protected void stepFailed( IApplMessage msg ) throws Exception {
		CommUtils.outyellow("" + msg);
		NumStep++;
		CommUtils.outblue("FOUND A WALL - atHome=" + planner.atHome() + " NumStep=" + NumStep);
		IApplMessage turnMsg = CommUtils.buildDispatch(name, "cmd", "cmd(l)", "basicrobot");
		CommUtils.outyellow("stepFailed ->" + turnMsg   );
		conn.forward(turnMsg);
		planner.updateMap("l", name+ " turn");
		planner.showCurrentRobotState();
		CommUtils.waitTheUser("stepFailed athome=" + planner.atHome() + " NumStep=" + NumStep );
 		if ( NumStep < 4 ) {
			IApplMessage goon = CommUtils.buildDispatch(name, "goon", "goon(" + name + ")", "mapper");
			CommUtils.outyellow("stepFailed ->" + goon);
			//CommUtils.waitTheUser("turned - hit");
			this.autoMsg(goon);
		}else{
			IApplMessage endwork = CommUtils.buildDispatch(name, "endwork", "endwork(" + name + ")", "mapper");
			CommUtils.outyellow("stepFailed ->" + endwork);
			this.autoMsg(endwork);
		}
	}
	
 	
 	@State( name = "endOfWork" )
 	protected void endOfWork( IApplMessage msg ) {
 		planner.showMap();
		IApplMessage disengMsg = CommUtils.buildDispatch(name, "disengage", "disengage(mapper)", "basicrobot");
		CommUtils.outyellow("stepFailed ->" + disengMsg   );
		try {
			conn.forward(disengMsg);
			planner.saveRoomMap("mapRoomEmpty");
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommUtils.outyellow("BYE" );	
 		System.exit(0);
 	}

//----------------------------------------------
 
		@TransitionGuard
		protected boolean roundNotCompleted() {
			CommUtils.outyellow( "roundNotCompleted  " + (NumStep < 5) );
			return NumStep < 5;
		}	
		@TransitionGuard
		protected boolean roundCompleted() {
			CommUtils.outyellow( "roundCompleted  " + (NumStep == 5) );
			return NumStep == 5;
		}



}
