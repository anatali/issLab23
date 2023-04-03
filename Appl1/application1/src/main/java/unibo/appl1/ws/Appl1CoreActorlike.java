package unibo.appl1.ws;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import unibo.appl1.common.IApplAction;
import unibo.appl1.http.Appl1Core;
import unibo.basicomm23.utils.CommUtils;
import unibo.supports.VrobotHLMovesInteractionAsynch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/*
 */
public class Appl1CoreActorlike extends Appl1Core {
    protected int NSteps = 0;
    protected int NEdges = 0;
    protected BlockingQueue<String> msgQueue = new LinkedBlockingDeque<>();

    public Appl1CoreActorlike() {
        //CommUtils.outblue("Appl1CoreActorlike | className " + this.getClass().getName());
    }


    /*
    Behavior
     */
    //Per vedere l'asincronismo delle call a stepok e stepfail
    protected void backgroundJob() throws Exception {
            CommUtils.outblue("--- backgroundJob " + NEdges + " currentpath=" + getCurrentPath()
                    + " " + Thread.currentThread().getName());
            //CommUtils.delay(2000);
            if(  isRunning  ) {
                CommUtils.delay(500); //To avoid too-many background messages
                msgQueue.put("startBackground");
            }
    }

    protected IApplAction stepok   = (msg -> {
        try{ //msg = stepdone(373)
            CommUtils.outmagenta("%%% stepok:" + msg + " " + Thread.currentThread().getName());
            updateObservers("robot-stepdone");
            checkStop();
            NSteps++;
            CommUtils.delay(300); //to view steps better
            doStepAsynch();
        }catch(Exception e){ e.printStackTrace();}
    });

    protected IApplAction stepfail = (msg -> {
        try{ //msg=stepfailed(362, collision )
            CommUtils.outmagenta("%%% stepfail:" + msg);
            Struct t = (Struct)Term.createTerm(msg);
            String cause = t.getArg(1).toString();
            CommUtils.outmagenta("%%% stepfail:" + msg + " cause=" + cause + " " + Thread.currentThread().getName() );
            if( cause.contains("collision")) {
                updateObservers("robot-collision");
                NEdges += 1;
                vr.turnLeft();
                updateObservers("robot-turnLeft");
                checkStop();
                if (NEdges < 4) doStepAsynch();
                else {
                    isRunning = false;
                    NEdges    = 0;
                    NSteps    = 0;
                    CommUtils.outblue("stepfail ENDS NEdges="+ NEdges + " NSteps="+NSteps);
                    robotMustBeAtHome("END");
                }
            }
        }catch(Exception e){ e.printStackTrace();}
    });

    protected void mainLoop(){
        try {
            CommUtils.outmagenta("%%% mainLoop STARTS:" + " thname=" + Thread.currentThread().getName() );
            while( true ) {
                String msg = msgQueue.take();
                elabMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void elabMsg( String msg ) throws Exception {
        if( msg.equals("activateAppl")){
            doStepAsynch();
            return;
        }
        if( msg.equals("startBackground")) {
            backgroundJob();
            return;
        }
        CommUtils.outmagenta("%%% elab:" + msg + " " + Thread.currentThread().getName() );
        //Struct t       = (Struct)Term.createTerm(msg);
        //String functor = t.getName();
        if( msg.startsWith("stepdone")){
            stepok.handle(msg);
            return;
        }
        if( msg.startsWith("stepfailed") ){
            stepfail.handle(msg);
            return;
        }
        if( msg.startsWith("collision")){
            CommUtils.outred("%%% elab COLLISION");
            return;
        }
        if( msg.startsWith("sonar")){
            CommUtils.outred("%%% elab SONAR");
            return;
        }

    }

    /*
    @Override
    protected void walkAtBoundary() throws Exception {
        if( ! robotMustBeAtHome("START") ) throw new Exception("Robot not in HOME");
        //stepTime  = 350;
        isRunning = true;
        ((VrobotHLMovesInteractionAsynch)vr).setMsgQueue(this.msgQueue);
        walkBySteppingAsynchWithStop( );
        //ESPERiMENTO: lancio un ciclo (SENZA THREAD) come Job di backgorund
        //backgroundJob();
    }
*/

    protected void doStepAsynch( ) throws Exception {
        CommUtils.outblue("walkBySteppingAsynchWithStop NEdges="+ NEdges + " NSteps="+NSteps);
        checkStop();
        updateObservers("robot-moving");
        ((VrobotHLMovesInteractionAsynch)vr).stepAsynch( stepTime );
    }

     protected void checkStop() throws Exception {
        if( stopped ) {
            CommUtils.beep();
            updateObservers("robot-stopped");
            waitResume();
        }
    }
    @Override
    public void start(   ) throws Exception {
        if( ! started || ! isRunning){
            isRunning = true;
            started   = true;
            stopped   = false;
            configure();
            ((VrobotHLMovesInteractionAsynch)vr).setMsgQueue(this.msgQueue);
            msgQueue.put("activateAppl");
            msgQueue.put("startBackground");


            new Thread(){
                public void run(){
                    mainLoop();
                }
            }.start();

        }else CommUtils.outred("Appl1Core |  already started");
    }

}
