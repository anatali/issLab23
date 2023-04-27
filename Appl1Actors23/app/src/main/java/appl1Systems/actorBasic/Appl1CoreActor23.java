package appl1Systems.actorBasic;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import appl1Actors23.Appl1StateObject;
import appl1Actors23.interfaces.IApplAction;
import unibo.actors23.Actor23Utils;
import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

/*
A causa della erediterità singola, include il condice di Appl1Core
E' Coap-observable ma non come POJO

KEY-POINT: appl(Appl1CoreForActors23) observable in quanto emitLocalStreamEvent percepito da obs(ObserverActorForPath)
KEY-POINT: Appl1State
KEY-POINT: obs(ObserverActorForPath) responsabile dell'aggiornamento di Appl1State


*/
public class Appl1CoreActor23 extends ActorBasic23 {
    protected boolean sonarEvent  = false;

    public Appl1CoreActor23(String name, ActorContext23 ctx) {
        super(name, ctx);
        autostart = true;
    }

     /*
    Behavior
     */
    @Override
    protected void elabMsg(IApplMessage message) throws Exception {
        CommUtils.outgray(name + " | elabMsg:" + message + " " + Thread.currentThread().getName() );
        if( message.msgId().equals("sysstartcmd")){
            this.subscribeLocalActor("console");
            return;
        }
        String payload = message.msgContent();
         if( message.msgId().equals("startcmd")){
             if( Appl1StateObject.getStarted() ){
                 CommUtils.outred(name + " |  ALREADY STARTED " + Thread.currentThread().getName() );
                 return;
             }
             Appl1StateObject.readConfigFromFile(this);
             Appl1StateObject.reset();
             /*
              Appl1State.setPath("nopath");*/
             Appl1StateObject.setIsRunning(true);
             Appl1StateObject.setStarted(true);



             IApplMessage event = CommUtils.buildEvent(name,"startobs", "ok");
             emitLocalStreamEvent(event);
             if( ! Appl1StateObject.robotMustBeAtHome("START",this) ){
                CommUtils.outred(name + " | elabMsg: NOT AT HOME " + Thread.currentThread().getName() );
                throw new Exception("Robot must be at home");
             };
             Appl1StateObject.doStepAsynch();
            return;
        }
        if( message.msgId().equals("stopcmd")){
            Appl1StateObject.setStopped(true);
            return;
        }
        if( message.msgId().equals("resumecmd")){
            Appl1StateObject.setStopped(false);
            Appl1StateObject.doStepAsynch();
            return;
        }
        if( message.msgId().equals("getpath")){
            /*
            //ELABORAZIONE DIRETTA
            String curPath = Appl1StateObject.getPath();
            CommUtils.outred(name + " | elabMsg: getpath " + curPath );
            IApplMessage answer = CommUtils.buildReply(
                    name,"getpathanswer", "'"+curPath+"'", message.msgSender());
            reply(answer, message);
            */
            //ELABORAZIONE DELEGATA
            Actor23Utils.sendMsg(message, ctx, "obsforpath");
            return;
        }
        if( message.msgId().equals("isrunning")){
            /*
            //ELABORAZIONE DIRETTA
           CommUtils.outred(name + " | elabMsg: isrunning " + Appl1StateObject.getIsRunning() );
            IApplMessage answer = CommUtils.buildReply(
                    name,"isrunninganswer", ""+ Appl1StateObject.getIsRunning() , message.msgSender());
            reply(answer, message);
            */
            //ELABORAZIONE DELEGATA
            Actor23Utils.sendMsg(message, ctx, "obsforpath");
            return;
        }
        if( payload.startsWith("stepdone")){
             if( sonarEvent ){
                 Appl1StateObject.getVr().step( Appl1StateObject.getStepTime() ); //mi tolgo dal sonar
                //stopped = true;
                CommUtils.delay(2000);
                 sonarEvent = false;
             }
            if( Appl1StateObject.getIsRunning() && ! Appl1StateObject.getStopped()  ) stepok.handle(payload);
            return;
        }
        if( payload.startsWith("stepfailed") ){
            if( Appl1StateObject.getIsRunning() && ! Appl1StateObject.getStopped() )  stepfail.handle(payload);
            return;
        }
        if( payload.startsWith("collision")){
            CommUtils.outred(name + " | elab COLLISION");
            return;
        }
        if( payload.contains("sonar")){ //'sonar(-21)'
            /*
            IApplMessage sonarevent = CommUtils.buildEvent(
                    name,"sonardata", payload  );
            CommUtils.outred(name + " | elab SONAR " + sonarevent);
            //this.emit(sonarevent);  //tranne a sè stesso
            //emitLocalStreamEvent(sonarevent);  //TODO / check

             */
            return;
        }
    }


    protected IApplAction stepok   = (msg -> {
        try{ //msg = stepdone(373)
            CommUtils.outmagenta("%%% stepok:" + msg + " " + Thread.currentThread().getName());
            Appl1StateObject.updateObservers("robot-stepdone",this);
            //checkStop();
            Appl1StateObject.incNSteps();
            CommUtils.delay(300); //to view steps better
            Appl1StateObject.doStepAsynch();
        }catch(Exception e){ e.printStackTrace();}
    });

    protected IApplAction stepfail = (msg -> {
        try{ //msg=stepfailed(362, collision )
            CommUtils.outmagenta("%%% stepfail:" + msg);
            Struct t = (Struct)Term.createTerm(msg);
            String cause = t.getArg(1).toString();
            CommUtils.outmagenta("%%% stepfail:" + msg + " cause=" + cause + " " + Thread.currentThread().getName() );
            if( cause.contains("collision")) {
                Appl1StateObject.updateObservers("robot-collision",this);
                Appl1StateObject.incNEdges();
                Appl1StateObject.getVr().turnLeft();
                Appl1StateObject.updateObservers("robot-turnLeft",this);
                //checkStop();
                if (Appl1StateObject.getNEdges() < 4) Appl1StateObject.doStepAsynch();
                else {
                    CommUtils.outblue("stepfail ENDS NEdges="+ Appl1StateObject.getNEdges() + " NSteps="+ Appl1StateObject.getNSteps());
                    Appl1StateObject.reset();
                    Appl1StateObject.robotMustBeAtHome("END",this);
                }
            }
        }catch(Exception e){ e.printStackTrace();}
    });


}
