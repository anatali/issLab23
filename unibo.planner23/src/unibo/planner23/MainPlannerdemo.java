package unibo.planner23;

import java.util.List;
import aima.core.agent.Action;


public class MainPlannerdemo {
private Planner23Util pu = new Planner23Util();

	protected void println(String m){
		System.out.println(m);
	}

	public void doJob1() {
		try {
			pu.initAI();
 			pu.showMap();
			pu.startTimer();
			for( int i=1; i<=4; i++) {
				List<Action> actions = pu.doPlan();
				println("===== plan actions for next move: " + actions);
				executeMoves();
				println("===== map after plan");
				;
				pu.showMap();
			}
			pu.getDuration();
		}catch ( Exception e) {
			 e.printStackTrace();
		}
	}
	public void doJob() {
 		try {
			pu.initAI();
			println("===== map at start");
 			pu.showMap();
			pu.startTimer();
			doSomeMOve();
			println("===== map after some move");
			pu.showMap();


			//Planner22Util.cell0DirtyForHome()
			pu.setGoal(0,0);
			pu.doPlan();
			executeMoves( );
			println("===== map after plan for home");;
			pu.showMap();

			pu.getDuration();

		} catch ( Exception e) {
			//e.printStackTrace()
		}

	}

	protected void doSomeMOve(){
        pu.doMove("w");
        pu.doMove("a");
        pu.doMove("w");
        pu.doMove("w");
        pu.doMove("d");
        pu.doMove("w");
        pu.doMove("a");
        pu.doMove("obstacleOnRight");

	}
	protected void executeMoves(){/*
        String move = pu.getNextPlannedMove();
        while ( move.length() > 0 ) {
            pu.doMove( move );
			move = pu.getNextPlannedMove();
        }*/
	}
	public static void main( String[] args) throws Exception {
 		MainPlannerdemo appl = new MainPlannerdemo( );
		appl.doJob();
		//appl.terminate();
	}

}
