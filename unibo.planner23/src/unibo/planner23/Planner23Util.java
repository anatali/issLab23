package unibo.planner23;  

import aima.core.agent.Action;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.util.datastructure.Pair;
import unibo.basicomm23.utils.CommUtils;
import unibo.planner23.model.Box;
import unibo.planner23.model.RobotAction;
import unibo.planner23.model.RobotState;
import unibo.planner23.model.RoomMap;
import unibo.planner23.model.Functions;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
 

/*
 * Planner23Util dovrebbe essere un servizio stateless ...
 * Lo stato è in RobotState 
 */
public class Planner23Util {
    private RobotState robotState;
    //private List<Action> actions;
    private BreadthFirstSearch search;
    //private Pair<Integer,Integer> curPos  = new Pair<Integer,Integer>(0,0);
    private GoalTest curGoal              = new Functions();
    private	boolean currentGoalApplicable = true;

/*
 * ------------------------------------------------
 * CREATE AND MANAGE PLANS
 * ------------------------------------------------
*/
    public void initAI() {
        robotState = new RobotState(0, 0, RobotState.Direction.DOWN);
        search     = new BreadthFirstSearch(new GraphSearch());
        println("Planner23Util initAI done");
    }
    public void setGoal( Integer x, Integer y) {
        try {
            //CommUtils.outyellow("Planner23Util | setGoal x=" +x + " y=" + y + " while:" + robotPosInfo());
            if( RoomMap.getRoomMap().isObstacle(x,y) ) {
            	CommUtils.outred("Planner23Util | ATTEMPT TO GO INTO AN OBSTACLE ");
                currentGoalApplicable = false;
                 return;
            }else currentGoalApplicable = true;

            RoomMap.getRoomMap().put(x, y, new Box(false, true, false));  //set dirty
            //pg 67
            curGoal = new GoalTest() {
                @Override
                public boolean isGoalState(Object state) {
                    RobotState robotState =  (RobotState)state;
                    return robotState.getX() == x && robotState.getY() == y;
                }
            };
            //showMap();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
    
 

    public List<Action> doPlan() throws Exception {
        //println("Planner23Util doPlan curGoal=" + curGoal)
    	List<Action> actions;
        if( ! currentGoalApplicable ){
        	CommUtils.outred("Planner23Util | doPlan cannot go into an obstacle");
            actions = new ArrayList<Action>();  //TOCHECK
            return actions;		//empty list
        }

        SearchAgent searchAgent;
        Problem problem = new Problem(robotState,
                new Functions(), new Functions(), curGoal, new Functions());
        //println("Planner23Util doPlan newProblem (A) search " );
        searchAgent = new SearchAgent(problem, search);
        actions     = searchAgent.getActions();

        //println("Planner23Util doPlan actions="+actions);

        if (actions == null || actions.isEmpty()) {
            println("Planner23Util doPlan NO MOVES !!! " + actions  );
            if (! RoomMap.getRoomMap().isClean()) RoomMap.getRoomMap().setObstacles();
            return new ArrayList<Action>();
        } else if (actions.get(0).isNoOp() ) {
        	CommUtils.outyellow("Planner23Util | doPlan NoOp");
            return new ArrayList<Action>();
        }
         
        return actions;
    }
    
    public String doPlanCompact() throws Exception {
    	String p = doPlan().toString().replace("[","").replace("]","").replace(",","").replace(" ","");
    	return p;
    }

    public List<Action> planForGoal(  String x ,  String y) throws Exception {
        Integer vx = Integer.parseInt(x);
        Integer vy = Integer.parseInt(y);
        setGoal(vx,vy);
        return doPlan();
    }

    public List<Action> planForNextDirty( ) throws Exception {
        List<Action> actions = new ArrayList<Action>();
        RoomMap rmap = RoomMap.getRoomMap();
        int dimX = getMapDimX( );
        int dimY = getMapDimY( );
//println( "... planForNextDirty dimX="+ dimX + " dimY=" + dimY );
        //showCurrentRobotState();
        for( int i = 0; i<=dimX-1 ; i++ ){
            for( int j= 0; j<=dimY-1;j++ ){
                if( rmap.isDirty(i,j)  ){
CommUtils.outyellow( "... planForNextDirty "+ i + "," + j + " curpos= " + getPosX() + "," + getPosY() );
                    setGoal( i,j ) ;
                    actions = doPlan();
                    return actions;
                }
            }
        }
        return actions;
    }
    
    public String planForNextDirtyCompact( ) throws Exception {
    	return planForNextDirty( ).toString().replace("[","").replace("]","").replace(",","").replace(" ","");
    }
 
 
 
/*
 * ------------------------------------------------
 * MANAGE PLANS AS ACTION SEQUENCES
 * ------------------------------------------------
*/
     
 

 

 
/*
 * ------------------------------------------------
 * INSPECTING ROBOT POSITION AND DIRECTION
 * ------------------------------------------------
*/
    public Pair<Integer,Integer> get_curPos(){
        return new Pair(robotState.getX(),robotState.getY());
    }
    public Integer getPosX(){ return robotState.getX(); }
    public Integer getPosY(){ return robotState.getY(); }
    public RobotState.Direction getDir(){ return robotState.getDirection(); }

    public String getDirection(){
        RobotState.Direction direction = getDir();
        switch( direction ){
            case  UP    : return "upDir";
            case  RIGHT : return "rightDir";
            case  LEFT  : return "leftDir";
            case  DOWN  : return "downDir";
            default : return "unknownDir";
        }
    }

    public boolean atHome() {
    	//Pair<Integer,Integer> curPos = new Pair(robotState.getX(),robotState.getY());
        return robotState.getX() == 0 && robotState.getY() == 0;
    }

    public boolean atPos(  Integer x, Integer y ) {
        return robotState.getX() == x && robotState.getY() == y;
    }

    public void showCurrentRobotState(){
        println("| ===================================================");
        showMap();
        println(robotPosInfo());
        println(" =================================================== |");
    }
    public String robotPosInfo(){
         return "RobotPos=("+ robotState.getX() + "," + robotState.getY() + ") direction="+ robotState.getDirection() ;
    }
/*
 * ------------------------------------------------
 * MANAGING THE ROOM MAP
 * ------------------------------------------------
*/
    public Integer getMapDimX( ) { return RoomMap.getRoomMap().getDimX(); }
    public Integer getMapDimY( ) { return RoomMap.getRoomMap().getDimY(); }
    public boolean mapIsEmpty() {return (getMapDimX( )==0 &&  getMapDimY( )==0 ); }
    public void showMap() { println( RoomMap.getRoomMap().toString() ); }

    public String getMap() { return RoomMap.getRoomMap().toString(); }
    public String getMapOneLine(){
        return  "'"+RoomMap.getRoomMap().toString().replace("\n","@").
                replace("|","").replace(",","") +"'";
    }
    public Pair<Integer,Integer> getMapDims(){
        if( RoomMap.getRoomMap() == null ){
            return new Pair(0,0);
        }
        int dimMapx = RoomMap.getRoomMap().getDimX();
        int dimMapy = RoomMap.getRoomMap().getDimY();
        return new Pair(dimMapx,dimMapy);
    }
    public void loadRoomMap( String fname ){  //TODO
        try{
            ObjectInputStream inps = new ObjectInputStream(new FileInputStream(fname+".bin"));
            RoomMap map  = (RoomMap)inps.readObject();
            println("loadRoomMap DONE from "+fname);
            RoomMap.setRoomMap( map );
        }catch(Exception e){
            println("loadRoomMap FAILURE "+ e.getMessage());
        }
        //mapDims = getMapDims(); //Pair(dimMapx,dimMapy)
    }
    public void saveRoomMap(  String fname  ) throws IOException {
        println("saveMap in "+ fname);
        PrintWriter pw = new PrintWriter( new FileWriter(fname+".txt") );
        pw.print( RoomMap.getRoomMap().toString() );
        pw.close();

        ObjectOutputStream os = new ObjectOutputStream( new FileOutputStream(fname+".bin") );
        os.writeObject(RoomMap.getRoomMap());
        os.flush();
        os.close();
        //mapDims = getMapDims();
    }
/*
 * ------------------------------------------------
 * UPDATING THE ROOM MAP
 * ------------------------------------------------
*/
    public void moveRobotInTheMap(){
        //curPos = new Pair(robotState.getX(),robotState.getY());
        RoomMap.getRoomMap().put(robotState.getX(), robotState.getY(),
                new Box(false, false, true));
    }
    public void doMove(String move) {
        Integer x   = getPosX();
        Integer y   = getPosY();
        RoomMap map = RoomMap.getRoomMap(); 
        //CommUtils.outyellow("Planner23Util: doMove move="+move + " x=" + x + " y=" + y + " dir=" + robotState.getDirection());
        try {
            switch (move) {
                case "w" : {
                    //map.put(x, y, Box(false, false, false)) //clean the cell
                    map.cleanCell(x,y);
                    robotState = (RobotState) new Functions().result(robotState, RobotAction.wAction) ;
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
                    moveRobotInTheMap();
                    return;
                }
                case "s": {
                    robotState = (RobotState) new Functions().result(robotState, RobotAction.sAction) ;
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
                    moveRobotInTheMap();
                    return;
                }
                case "a"  : {
                    robotState = (RobotState) new Functions().result(robotState, RobotAction.lAction);
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
                    moveRobotInTheMap();
                    return;
                }
                case "l" : {
                    robotState = (RobotState) new Functions().result(robotState, RobotAction.lAction) ;
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))                     
                    moveRobotInTheMap();
                    return;
                }
                case "d" : {
                    robotState = (RobotState) new Functions().result(robotState, RobotAction.rAction) ;
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
                    moveRobotInTheMap();
                    return;
                }
                case "r" : {
                    robotState = (RobotState) new Functions().result(robotState, RobotAction.rAction) ;
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
                    moveRobotInTheMap();
                    return;
                }
                //Used by WALL-UPDATING
                //Box(boolean isObstacle, boolean isDirty, boolean isRobot)
                case "rightDir" : {map.put(x + 1, y, new Box(true, false, false));return;}
                case "leftDir"  : {map.put(x - 1, y, new Box(true, false, false));return;}
                case "upDir"    : {CommUtils.outred(" upDir ");map.put(x, y - 1, new Box(true, false, false));return;}
                case "downDir"  : {CommUtils.outred(" downDir ");map.put(x, y + 1, new Box(true, false, false));return;}
            }//when
        } catch (Exception e ) {
            CommUtils.outred("Planner23Util doMove:" + move + " ERROR:" + e.getMessage());
        }
    }
    /*
    public void  setPositionOnMap( ){
        direction     =  getDirection();
        curPos        =  new Pair( getPosX(),getPosY() );
    }*/

    public void updateMap( String move , String msg ){
        doMove( move );
        //setPositionOnMap( );
        if( msg.length() > 0 ) println(msg);
    }
    public void  updateMapObstacleOnCurrentDirection(   ){
    	CommUtils.outyellow("updateMapObstacleOnCurrentDirection " + robotState.getDirection());
		doMove( getDirection() );
		//setPositionOnMap( );
	}
/*
 * ------------------------------------------------
 * UPDATING THE ROOM MAP FOR OBSTACLES
 * ------------------------------------------------
*/
    public void setObstacleUp(){
        if( robotState.getY() > 0 )
        RoomMap.getRoomMap().put(robotState.getX(), robotState.getY()-1,
                new Box(true, false, false));
    }
    public void setObstacleDown(){
        RoomMap.getRoomMap().put(robotState.getX(), robotState.getY()+1,
                new Box(true, false, false));
    }
    public void setObstacleLeft(){
        if( robotState.getX() > 0 )
        RoomMap.getRoomMap().put(robotState.getX()-1, robotState.getY() ,
                new Box(true, false, false));
    }
    public void setObstacleRight(){
        RoomMap.getRoomMap().put(robotState.getX()+1, robotState.getY(),
                new Box(true, false, false));
    }
    public void setObstacleWall(  RobotState.Direction dir, Integer x , Integer y ){
        if( dir == RobotState.Direction.DOWN  ){
            RoomMap.getRoomMap().put(x, y + 1, new Box(true, false, false));
        }else if( dir == RobotState.Direction.RIGHT ){
            RoomMap.getRoomMap().put(x + 1, y, new Box(true, false, false));
        }
    }
    public void setWallDown( int dimMapx, int y  ){
        int k   = 0;
        while( k < dimMapx ) {
            RoomMap.getRoomMap().put(k, y+1, new Box(true, false, false));
            k++;
        }
    }
    public void setWallRight( int dimMapy, int x){
        int k   = 0;
        while( k < dimMapy ) {
            RoomMap.getRoomMap().put(x+1, k, new Box(true, false, false) );
            k++;
        }
    }
    public void wallFound(){
        int dimMapx = RoomMap.getRoomMap().getDimX();
        int dimMapy = RoomMap.getRoomMap().getDimY();
        RobotState.Direction dir     = getDir();
        Integer x       = getPosX();
        Integer y       = getPosY();
        setObstacleWall( dir,x,y );
        CommUtils.outyellow("Planner23Util wallFound dir="+dir );
        doMove( dir.toString() );  //set cell
        if( dir == RobotState.Direction.RIGHT) setWallDown(dimMapx,y);
        if( dir == RobotState.Direction.UP)    setWallRight(dimMapy,x);
    }

/*
 * ------------------------------------------------
 * UTILITIES
 * ------------------------------------------------
*/
    private void println(String msg ){
        System.out.println( msg );
    }

 
   
    
   
   public void doPathOnMap(String pathlist) {
	   String Path = pathlist.replace("[","").replace("]","").replace(",","").replace(" ","");
	   //CommUtils.outblue("doPathOnMap " + Path);
	   while( Path.length() > 0 )  {
		   String curMove =  ""+Path.charAt(0);
		   //CommUtils.outblue("doPathOnMap curMove=" + curMove + " Path=" + Path);
		   doMove( curMove );
		   //showCurrentRobotState();
		   Path = Path.substring(1,Path.length());
		   //if( Path.length() > 1) Path = Path.substring(1,Path.length());
		   //else Path = "";		     
	   }
	   
   }
 
}
