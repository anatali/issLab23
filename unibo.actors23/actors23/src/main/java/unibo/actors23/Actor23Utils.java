package unibo.actors23;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.*;

public class Actor23Utils {
    protected static Prolog pengine = new Prolog();
    protected static Hashtable<String, ActorContext23> ctxsMap = new Hashtable<String, ActorContext23>();
    protected static String pfx = "    %%% ";
    public static boolean trace = false;

    public static void showSystemConfiguration() {
        try {
            solve("showSystemConfiguration");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadTheory(String path) {
        try {
            Theory worldTh = new Theory(new FileInputStream(path));
            pengine.addTheory(worldTh);
        } catch (Exception e) {
            CommUtils.outred(pfx + "Actor23Utils | loadTheory ERROR " + e.getMessage());
        }
    }

    public static String solve(String goal, String resVar) throws Exception {
        SolveInfo sol = pengine.solve(goal + ".");
        if (sol.isSuccess()) {
            if (resVar.length() == 0) return "success";
            Term result = sol.getVarValue(resVar); //Term
            String resStr = result.toString();
            return strCleaned(resStr);
        } else return null;
    }

    public static boolean solve(String goal) throws Exception {
        SolveInfo sol = pengine.solve(goal + ".");
        return sol.isSuccess();
    }

    public static String strCleaned(String s) {
        if (s.startsWith("'")) return s.replace("'", "");
        else return s;
    }

    public static List<String> strRepToList(String liststrRep) {
        String[] al = liststrRep.replace("[", "")
                .replace("]", "").split(",");
        List<String> myList = new ArrayList<String>();
        for (String s : al) {
            myList.add(s);
        }
        return myList;
    }

    public static void createContexts(
            String hostName, String desrFilePath, String rulesFilePath) {
        if (trace) CommUtils.outgray(pfx + "Actor23Utils | createContexts   host=" + hostName);
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        loadTheory(desrFilePath);
        loadTheory(rulesFilePath);

        try {
            //Crea tutti i contesti
            String ctxs = solve("getCtxNames( X )", "X");
            List<String> ctxsList = strRepToList(ctxs);
            Iterator<String> iter = ctxsList.iterator();
            while (iter.hasNext()) {
                String ctx = iter.next();
                String goal = "context( " + ctx + ", " + hostName + ", PROTOCOL, PORT )";
                //CommUtils.outred(pfx+"Actor23Utils | createContexts  goal="+ goal);
                boolean res = solve(goal);
                if (res) createTheContext(ctx, hostName);
            }
            //Per ognoi contesto, fissa gli attori remoti
            Iterator<ActorContext23> allCtxs = ctxsMap.values().iterator();
            while (allCtxs.hasNext()) {
                ActorContext23 ctx = allCtxs.next();
                setTheActorsRemote(ctx);
            }
            //Per ognoi contesto, crea gli attori locali
            allCtxs = ctxsMap.values().iterator(); //reset the iterator
            while (allCtxs.hasNext()) {
                ActorContext23 ctx = allCtxs.next();
                createActorsOfContext(ctx);
            }
            //Per ognoi contesto, attiva gli attori locali
            allCtxs = ctxsMap.values().iterator(); //reset the iterator
            while (allCtxs.hasNext()) {
                activateActorsInContext(allCtxs.next());
            }
            //showSystemConfiguration();
            CommUtils.aboutThreads(pfx + "Actor23Utils | At START ");
        } catch (Exception e) {
            CommUtils.outred(pfx + "Actor23Utils | createContexts ERROR " + e.getMessage());
        }
    }



    public static ActorContext23 createTheContext(String ctx, String hostName) throws Exception {
        String ctxHost = solve("getCtxHost(" + ctx + ",H)", "H");
        String ctxPort = solve("getCtxPort(" + ctx + ",P)", "P");
        int portNum = Integer.parseInt(ctxPort);
        ActorContext23 newctx = null;
        if (ctxHost.equals(hostName)) {
            if (trace) CommUtils.outgray(pfx + "Actor23Utils | createTheContext  ctx=" + ctx + " host=" + hostName);
            newctx = new ActorContext23(ctx, ctxHost, portNum);
            ctxsMap.put(newctx.name, newctx);
        } else {
            CommUtils.outred(pfx + "Actor23Utils | createContext for different ");
        }
        return newctx;
    }

    public static void setTheActorsRemote(ActorContext23 ctx) {
        try {
            //if(trace) CommUtils.outgray(pfx+"Actor23Utils | setTheActorsRemote in:"+ ctx.name );
            String actors = solve("getActorNames( A,CTX)", "A");
            List<String> actorsList = strRepToList(actors);
            Iterator<String> iter = actorsList.iterator();
            while (iter.hasNext()) {
                String actorName = iter.next();
                if (solve("qactor( " + actorName + "," + ctx.name + ", _ )")) {
                    //Ho trovato un attore del contesto
                } else {
                    if (trace)
                        CommUtils.outgray(pfx + "Actor23Utils | setTheActorsRemote in " + ctx.name + " found: " + actorName);
                    //Trovo la porta del contesto di actorName
                    String remoteActorCtx = solve("qactor( " + actorName + ",CTX, _ )", "CTX");
                    String query = "context( CTX,HOST,PROTOCOL,PORT )".replace("CTX", remoteActorCtx);
                    String remoteHost = solve(query, "HOST");
                    String remotePort = solve(query, "PORT");
                    ctx.setActorAsRemote(actorName, "" + remotePort, remoteHost, ProtocolType.tcp);
                }
            }
        } catch (Exception e) {
            CommUtils.outred(pfx + "Actor23Utils | setTheActorsRemote ERROR " + e.getMessage());
        }

    }

    public static void createActorsOfContext(ActorContext23 ctx) {
        try {
            if(trace) CommUtils.outgray(pfx+"Actor23Utils | createActorsOfContext in:"+ ctx.name );
            String actors = solve("getActorNames( A," + ctx.name + ")", "A");
            List<String> actorsList = strRepToList(actors);
            Iterator<String> iter = actorsList.iterator();
            while (iter.hasNext()) {
                String actorName = iter.next();
                String actorClass = solve("qactor( " + actorName + "," + ctx.name + ", CLASS )", "CLASS");
                createTheActor(ctx, actorName, actorClass);
            }
        } catch (Exception e) {
            CommUtils.outred(pfx + "Actor23Utils | createActorsOfContext ERROR " + e.getMessage());
        }
    }

    public static void activateActorsInContext(ActorContext23 ctx){
        Vector<String> actors = ctx.getLocalActorNames();
        Iterator<String> iter = actors.iterator();
        while (iter.hasNext()) {
            ActorBasic23 a = ctx.getActor(iter.next());
            if( a.autostart ) a.activateAndStart(); else a.activate();
        }
    }

    public static ActorBasic23 createTheActor(
            ActorContext23 ctx, String actorName, String className) throws Exception {
        if (trace) CommUtils.outgray(
                pfx + "Actor23Utils | createTheActor: " + actorName + " className=" + className + " in " + ctx.name);
        Class<?> clazz = Class.forName(className);
        ActorBasic23 actor;
        Constructor<?> ctor = clazz.getConstructor(String.class, ActorContext23.class);
        actor = (ActorBasic23) ctor.newInstance(actorName, ctx);
        ctx.addActor(actor);
        return actor;
    }

    public static void sendMsg(IApplMessage msg, ActorBasic23 dest) throws Exception {
          dest.msgQueue.put(msg); //attore locale
    }
    public static void sendMsg(IApplMessage msg, ActorContext23 ctx, String dest) throws Exception {
        ActorBasic23 destactor = ctx.getActor(dest);
        if( destactor != null ) sendMsg(msg,destactor);
        else throw new Exception("sendMsg to non local actor:" + dest);
    }

    public static void emitLocalEvent(IApplMessage ev, ActorContext23 ctx){
        ctx.propagateEventToActors(ev); //a tutti
    }
    public static void emitLocalEvent(IApplMessage ev, ActorBasic23 actor){
        actor.emitLocal(ev);  //escluso actor
    }
    public static void emitEvent(IApplMessage ev, ActorBasic23 actor){
        actor.emit(ev); //invia anche a contesti remoti
    }
}