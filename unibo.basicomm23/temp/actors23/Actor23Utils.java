package unibo.basicomm23.actors23;

import alice.tuprolog.*;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class Actor23Utils {
protected static Prolog pengine = new Prolog();
protected static Hashtable<String, ActorContext23> ctxsMap= new Hashtable<String, ActorContext23>();
protected static String pfx = "    %%% ";
public static boolean trace = false;

    public static void showSystemConfiguration(){
        try {
            solve("showSystemConfiguration");
        }catch( Exception e ){ e.printStackTrace();}
    }
    public static void loadTheory( String path ){
        try {
            Theory worldTh = new Theory( new FileInputStream(path) );
            pengine.addTheory(worldTh);
        }catch(Exception e){
            CommUtils.outred(pfx+"ActorUtils | loadTheory ERROR " +e.getMessage());
        }
    }

    public static String solve(String goal, String resVar ) throws Exception {
        SolveInfo sol = pengine.solve( goal+".");
        if( sol.isSuccess() ) {
            if( resVar.length() == 0 ) return "success";
            Term result   = sol.getVarValue(resVar); //Term
            String resStr = result.toString();
            return  strCleaned( resStr );
        }
        else return null;
    }

    public static boolean solve( String goal  ) throws Exception {
        SolveInfo sol = pengine.solve( goal+".");
        return sol.isSuccess();
    }

        public static String strCleaned(String s){
        if( s.startsWith("'")) return s.replace("'","");
        else return s;
    }

    public static List<String> strRepToList(String liststrRep){
        String[] al = liststrRep.replace("[","")
                .replace("]","").split(",");
        List<String> myList = new ArrayList<String>();
        for(String s : al){ myList.add(s); }
        return myList;
    }

    public static void createContexts(
            String hostName, String desrFilePath , String rulesFilePath ){
        if(trace) CommUtils.outgray(pfx+"ActorUtils | createContexts   host="+ hostName);

        loadTheory( desrFilePath );
        loadTheory( rulesFilePath );

        try {
            String ctxs = solve("getCtxNames( X )", "X");
            List<String> ctxsList = strRepToList(ctxs);
            Iterator<String> iter = ctxsList.iterator();
            while( iter.hasNext() ){
                String ctx = iter.next();
                String goal = "context( "+ ctx + ", "+ hostName +", PROTOCOL, PORT )";
                //CommUtils.outred(pfx+"ActorUtils | createContexts  goal="+ goal);
                boolean res = solve( goal );
                if( res ) createTheContext(ctx, hostName );
            }
            Iterator<ActorContext23> allCtxs = ctxsMap.values().iterator();
                while( allCtxs.hasNext() ){
                    ActorContext23 ctx = allCtxs.next();
                    setTheActorsRemote(ctx);
                 }
            allCtxs = ctxsMap.values().iterator(); //reset the iterator
            while( allCtxs.hasNext() ){
                ActorContext23 ctx = allCtxs.next();
                createActorsOfContext( ctx );
            }
            showSystemConfiguration();
        } catch (Exception e) {
            CommUtils.outred(pfx+"ActorUtils | createContexts ERROR " +e.getMessage());
        }
    }

    public static ActorContext23 createTheContext(String ctx, String hostName ) throws Exception {
        String ctxHost = solve("getCtxHost("+ctx+",H)","H");
        String ctxPort = solve("getCtxPort("+ctx+",P)","P");
        int portNum    = Integer.parseInt(ctxPort);
        ActorContext23 newctx = null;
        if(   ctxHost.equals(hostName) ){
            if(trace) CommUtils.outgray(pfx+"ActorUtils | createTheContext  ctx="+ ctx+ " host="+ hostName);
            newctx= new ActorContext23( ctx, ctxHost, portNum );
            ctxsMap.put(newctx.name, newctx);
            //createActorsOfContext( newctx );
            //setTheActorsRemote(newctx);
        } else{  CommUtils.outred(pfx+"ActorUtils | createContext for different "  ); }
        return newctx;
    }

    public static void setTheActorsRemote(ActorContext23 ctx){
        try {
            //if(trace) CommUtils.outgray(pfx+"ActorUtils | setTheActorsRemote in:"+ ctx.name );
             String actors = solve("getActorNames( A,CTX)", "A");
            List<String> actorsList = strRepToList(actors);
            Iterator<String> iter   = actorsList.iterator();
            while( iter.hasNext() ){
                String actorName = iter.next();
                if ( solve("qactor( "+ actorName + "," + ctx.name + ", _ )" ) ){
                    //Ho trovato un attore del contesto
                }else {
                    if(trace) CommUtils.outgray(pfx+"ActorUtils | setTheActorsRemote in " + ctx.name + " found: "+ actorName );
                    //Trovo la porta del contesto di actorName
                    String remoteActorCtx = solve("qactor( "+ actorName + ",CTX, _ )", "CTX" );
                    //if(trace) CommUtils.outgray(pfx+"ActorUtils | setTheActorsRemote "+ actorName + " in remoteActorCtx:" + remoteActorCtx );

                    String query = "context( CTX,HOST,PROTOCOL,PORT )".replace("CTX",remoteActorCtx);
                        String remoteHost = solve(query, "HOST" );
                        String remotePort = solve(query, "PORT" );
                        ctx.setActorAsRemote(actorName, "" + remotePort, remoteHost, ProtocolType.tcp);
                }
            }
        } catch (Exception e) {
            CommUtils.outred(pfx+"ActorUtils | setTheActorsRemote ERROR " +e.getMessage());
        }

    }

    public static void createActorsOfContext(ActorContext23 ctx){
        try {
            //if(trace) CommUtils.outgray(pfx+"ActorUtils | createTheActors in:"+ ctx.name );
            String actors = solve("getActorNames( A," + ctx.name +")", "A");
            List<String> actorsList = strRepToList(actors);
            Iterator<String> iter = actorsList.iterator();
            while( iter.hasNext() ){
                String actorName = iter.next();
                String actorClass = solve("qactor( "+ actorName + "," + ctx.name + ", CLASS )","CLASS");
                createTheActor(ctx, actorName, actorClass);
             }
        } catch (Exception e) {
            CommUtils.outred(pfx+"ActorUtils | createTheActorsInContext ERROR " +e.getMessage());
        }

    }

    public static ActorBasic23 createTheActor(
            ActorContext23 ctx, String actorName,String className ) throws Exception{
        if(trace) CommUtils.outgray(
            pfx+"ActorUtils | createTheActor: "+ actorName + " className=" + className + " in " + ctx.name);
        Class<?> clazz = Class.forName(className);
        ActorBasic23 actor;
        Constructor<?> ctor = clazz.getConstructor(String.class, ActorContext23.class);
        actor = (ActorBasic23) ctor.newInstance(actorName,ctx);
        ctx.addActor(actor);
        return actor;
    }

}
