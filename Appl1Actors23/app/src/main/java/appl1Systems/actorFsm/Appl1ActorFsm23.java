package appl1Systems.actorFsm;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import appl1Actors23.Appl1StateObject;
import appl1Actors23.interfaces.IApplAction;
import unibo.actors23.ActorContext23;
import unibo.actors23.annotations.State;
import unibo.actors23.annotations.Transition;
import unibo.actors23.annotations.TransitionGuard;
import unibo.actors23.fsm.ActorBasicFsm23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

/*
sonardata gestito da SonarObserverActor23 => stop

La gestione delle richieste isrunning e getpath complica gli stati
 */
public class Appl1ActorFsm23 extends ActorBasicFsm23 {
private IApplMessage restart;
    public Appl1ActorFsm23(String name, ActorContext23 ctx) {
        super(name, ctx);
        //memoNonHandledMessages = false;  //perdo stepdone / stepfailed
        //memoNonHandledMessages = true;   //DEFAULT
        //this.subscribeLocalActor("console");  //TOO EARLY
        restart = CommUtils.buildDispatch(name,"restart","ok", name);
    }

    @State( name = "s0", initial=true)
    @Transition( state = "init",  msgId = "startcmd"   )
    protected void s0( IApplMessage msg ) {
        CommUtils.outblue(name + " | s0 " + msg);
        this.subscribeLocalActor("console");
    }
    
    @State( name = "init" )
    //@Transition( state = "stopped",  msgId = "stopcmd" )
    @Transition( state = "startboundary")
    protected void init( IApplMessage msg ) {
        CommUtils.outblue(name + " | init "+msg  );
        if( Appl1StateObject.getStarted() ){
            CommUtils.outred(name + " |  ALREADY STARTED " + Thread.currentThread().getName() );
            return;
        }
        try {

            Appl1StateObject.readConfigFromFile(this);
            //Appl1StateObject.reset();
            Appl1StateObject.setIsRunning(true);
            Appl1StateObject.setStarted(true);
            //Appl1StateObject.setPath("nopath");

            //Appl1State.getVr().setTrace(true);

            IApplMessage event = CommUtils.buildEvent(name, "startobs", "ok");
            emitLocalStreamEvent(event);

            if (!Appl1StateObject.robotMustBeAtHome("START",this)) {
                CommUtils.outred(name + " | elabMsg: NOT AT HOME " + Thread.currentThread().getName());
                throw new Exception("Robot must be at home");
            }

            this.delegate("getpath","obsforpath");
            this.delegate("isrunning","obsforpath");

            //Appl1StateObject.doStepAsynch();
        }catch(Exception e){e.printStackTrace();}
    }
    @State( name="waitstartcmd")
    @Transition( state = "startboundary",  msgId = "startcmd"   )
    protected void waitstartcmd( IApplMessage msg )  {

    }
    @State( name="startboundary")
    @Transition( state = "stopped",  msgId = "stopcmd" )
    @Transition( state = "stepok",   msgId = "stepdone" )
    @Transition( state = "error",    msgId = "stepfailed" )
    protected void startboundary( IApplMessage msg )  {
        try {
            Appl1StateObject.doStepAsynch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @State( name = "stepok")
    @Transition( state = "stopped",   msgId = "stopcmd" )
    @Transition( state = "stepok",    msgId = "stepdone" )
    @Transition( state = "stepko",    msgId = "stepfailed" )
    protected void stepok( IApplMessage msg )  {
        CommUtils.outmagenta("%%% stepok:" + msg);
        if( Appl1StateObject.getIsRunning() && ! Appl1StateObject.getStopped()  )
            stepok.handle(msg.msgContent());
    }

    @State( name = "stepko")
    @Transition( state = "stopped", msgId = "stopcmd" )
    @Transition( state = "stepok",  msgId = "stepdone" )
    @Transition( state = "stepko",  msgId = "stepfailed" )
    @Transition( state = "startboundary",  msgId = "restart" )
    protected void stepko( IApplMessage msg ) {
        stepfail.handle(msg.msgContent());
        //if (Appl1StateObject.getNEdges() == 0) goto stepok
    }

    @TransitionGuard
    protected boolean completed() {
        return Appl1StateObject.getNSteps() == 0;
    }
    @TransitionGuard
    protected boolean notcompleted() {
        return Appl1StateObject.getNSteps() > 0;
    }
/*
Attenzione: c'è un messaggio stepdone/stepfailed non ricevuto => memoNonHandledMessages = true

Inoltre un click su resume prima di stop lascia un msg in coda che
provoca una immediata ripartenza =>
- NON devo fare memo di resume  =>  console fa emitLocalStreamEvent con appl come subscriber )
  PROBLEMA: CmdConsoleRemote non supera il resume-click-premature problem
- uso una guardia ma è prolisso e difficile
- inibisco il pulsante resume e lo attivo solo dopo stop
 */


    @State( name = "stopped")
    @Transition( state = "resumed",  msgId = "resumecmd"  )
    protected void stopped( IApplMessage msg ) {
        //stopped = true;
        CommUtils.outmagenta("%%% stopped:" + msg);
    }

    @State( name = "resumed")
    @Transition( state = "stepok",  msgId = "stepdone"  )
    @Transition( state = "stepko",  msgId = "stepfailed" )
    protected void resumed( IApplMessage msg ) {
        //stopped = false;
        CommUtils.outmagenta("%%% resumed:" + msg);
    }


    @State( name = "error")
    protected void error( IApplMessage msg ) {
        CommUtils.outred("error");
    }


/*

 */
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
                if (Appl1StateObject.getNEdges() < 4) Appl1StateObject.doStepAsynch();
                else {
                    CommUtils.outblue("stepfail ENDS NEdges="+ Appl1StateObject.getNEdges() + " NSteps="+ Appl1StateObject.getNSteps());
                    Appl1StateObject.reset();
                    Appl1StateObject.robotMustBeAtHome("END",this);
                    CommUtils.outblue("stepfail automsg "+ restart  );

                    autoMsg(restart);
                }
            }
        }catch(Exception e){ e.printStackTrace();}
    });


}
