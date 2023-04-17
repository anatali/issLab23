package unibo.actors23.fsm.example;

import org.json.simple.JSONObject;
import unibo.actors23.Actor23Utils;
import unibo.actors23.ActorBasic23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;

import java.util.concurrent.BlockingQueue;

public class VrobotHLMovesInteractionAsynch extends ApplAbstractObserver implements  IVrobotMovesAsynch {
    protected Interaction wsCommSupport;
    protected int elapsed = 0; //modified by update
    protected String moveResult = null;  //for observer part
    protected BlockingQueue<String> msgQueue;
    boolean doingStepAsynch = false;  //vedi update
    protected int threadCount = 1;
    protected boolean tracing = Actor23Utils.trace;
    protected ActorBasic23 owner;
    //protected String msgToApplStr = "msg(sysinfo, disptach, SENDER, RECEIVER, CONTENT, SEQNUM)";
    //protected IApplMessage msgToAppl = CommUtils.buildDispatch("");

    public VrobotHLMovesInteractionAsynch(Interaction wsCommSupport, ActorBasic23 owner) {
        this.wsCommSupport = wsCommSupport;
        this.owner         = owner;

        ((WsConnection) wsCommSupport).addObserver(this);
        CommUtils.aboutThreads("     VrobotHLMovesInteractionAsynch |");
        if( tracing ) CommUtils.outblue(
                "     VrobotHLMovesInteractionAsynch | CREATED in "
                        + Thread.currentThread().getName());
    }

    public void setMsgQueue(BlockingQueue<String> msgQueue) {
        this.msgQueue = msgQueue;
    }
    public void setTracing( boolean v ){
        tracing = v;
    }
    public Interaction getConn() {
        return wsCommSupport;
    }

    @Override
    public boolean step(int time) throws Exception {
        doingStepAsynch = false;
        if( tracing ) CommUtils.outyellow("     VrobotHLMovesInteractionAsynch | step time="
                + time + " " + Thread.currentThread().getName());
        String cmd = VrobotMsgs.forwardcmd.replace("TIME", "" + time);
        String result = request(cmd);
        //CommUtils.outgreen("     VrobotHLMovesInteractionAsynch | step result="+result);
        //result=true elapsed=... OPPURE collision elapsed=...
        return result.contains("true");
    }

    @Override
    public void turnLeft() throws Exception {
        //CommUtils.outred("     VrobotHLMovesInteractionAsynch | turnLeft " );
        //doingStepAsynch = false;
        //request(VrobotMsgs.turnleftcmd);
        wsCommSupport.forward(VrobotMsgs.turnleftcmd);
    }

    @Override
    public void turnRight() throws Exception {
        //doingStepAsynch = false;
        //request(VrobotMsgs.turnrightcmd);
        wsCommSupport.forward(VrobotMsgs.turnrightcmd);
    }

    @Override
    public void forward(int time) throws Exception {
        startTimer();
        wsCommSupport.forward(VrobotMsgs.forwardcmd.replace("TIME", "" + time));
    }

    @Override
    public void backward(int time) throws Exception {
        startTimer();
        wsCommSupport.forward(VrobotMsgs.forwardcmd.replace("TIME", "" + time));
    }

    @Override
    public void halt() throws Exception {
        if( tracing ) CommUtils.outgreen("     VrobotHLMovesInteractionAsynch | halt");
        wsCommSupport.forward(VrobotMsgs.haltcmd);
        CommUtils.delay(150); //wait for halt completion since halt on ws does not send answer
        //CommUtils.outgreen("     VrobotHLMovesInteractionAsynch | halt done " + moveResult );
    }
// Observer part

    public String request(String msg) throws Exception {
        moveResult = null;
        //Invio fire-and.forget e attendo modifica di  moveResult da update
        startTimer();
        //if( tracing )
            CommUtils.outblue("     VrobotHLMovesInteractionAsynch | request " + msg);
        wsCommSupport.forward(msg);
        synchronized (this) {
            while (moveResult == null) {
                wait();
            }
            return moveResult;
        }
    }

    @Override
    public void update(String info) {
        if (doingStepAsynch) updateForAsynch(info);
        else updateBasic(info);
    }

    protected void updateBasic(String info) {
        try {
            elapsed = getDuration();

            //if( tracing )
                CommUtils.outyellow("     VrobotHLMovesInteractionAsynch | updateBasic:"
                    +info + " elapsed=" + elapsed + " " + Thread.currentThread().getName());
            JSONObject jsonObj = CommUtils.parseForJson(info);
            if (jsonObj == null) {
                CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateBasic ERROR Json:" + info);
                return;
            }
            if (info.contains("_notallowed")) {
                CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateBasic WARNING!!! _notallowed unexpected in " + info);
                halt();  //stop running current move
                return;
            }
            if (jsonObj.get("collision") != null) {
                //Alla collision non faccio nulla: attendo moveForward-collision
                if( tracing ) CommUtils.outyellow("     VrobotHLMovesInteractionAsynch | updateBasic collision detected ");
                return;
            }
            if (jsonObj.get("endmove") != null) {
                //{"endmove":"true/false ","move":"..."}
                boolean endmove = jsonObj.get("endmove").toString().contains("true");
                String move = jsonObj.get("move").toString();
                //CommUtils.outred("     VrobotHLMovesInteractionAsynch | update move=" + move);
                synchronized (this) {
                    moveResult = "" + endmove;
                    notifyAll();
                }
                return;
            }
        } catch (Exception e) {
            CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateBasic ERROR:" + e.getMessage());
        }
    }

    //Eseguito nel Thread della libreria invia msg alla Appl usando msgQueue
    protected void updateForAsynch(String info) {
        try {
            elapsed = getDuration();
            //if( tracing )
                CommUtils.outyellow(
                    "     VrobotHLMovesInteractionAsynch | updateForAsynch:" + info + " elapsed=" + elapsed +
                            " " + Thread.currentThread().getName());
            JSONObject jsonObj = CommUtils.parseForJson(info);
            if (jsonObj == null) {
                CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateForAsynch ERROR Json:" + info);
                return;
            }
            if (info.contains("_notallowed")) {
                CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateForAsynch WARNING!!! _notallowed unexpected in " + info);
                return;
            }
            if (jsonObj.get("sonarName") != null) {
                long d = (long) jsonObj.get("distance");
                //msgQueue.put("sonar(" + d + " )");  //Solo per Appl1CoreActorlike
                return;
            }
            if (jsonObj.get("collision") != null) {
                //msgQueue.put("collision(" + elapsed + " )"); //Solo per Appl1CoreActorlike
                return;
            }
            if (jsonObj.get("endmove") != null) {
                //{"endmove":"true/false ","move":"..."}
                boolean endmove = jsonObj.get("endmove").toString().contains("true");
                String move = jsonObj.get("move").toString();
                //CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateForAsynch move=" + move);
                //move moveForward-collision or moveBackward-collision
                if (endmove) {
                    String content="stepdone(" + elapsed + ")";
                    IApplMessage msgToAppl = CommUtils.buildDispatch( "support","endMoveOk",content,owner.getName());
                    //msgQueue.put(msgToAppl.toString() );
                    Actor23Utils.sendMsg(msgToAppl,owner);
                } else if (move.contains("interrupted")) {
                    String content = "stepfailed(" + elapsed + ", interrupt )";
                    IApplMessage msgToAppl = CommUtils.buildDispatch( "support","endMoveKo",content,owner.getName());
                    //msgQueue.put(msgToAppl.toString());
                    Actor23Utils.sendMsg(msgToAppl,owner);
                }else if (move.contains("collision")) {
                    String content="stepfailed(" + elapsed + ", collision )";
                    IApplMessage msgToAppl = CommUtils.buildDispatch( "support","endMoveKo",content,owner.getName());
                    //msgQueue.put(msgToAppl.toString() );
                    Actor23Utils.sendMsg(msgToAppl,owner);
                }
                //CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateForAsynch END move=" + move);
                return;
            }
        } catch (Exception e) {
            CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateForAsynch ERROR:" + e.getMessage());
        }
    }


    //  Timer part
    private Long timeStart = 0L;

    public void startTimer() {
        elapsed = 0;
        timeStart = System.currentTimeMillis();
    }

    public int getDuration() {
        long duration = (System.currentTimeMillis() - timeStart);
        return (int) duration;
    }


//Added to introduce continuations (callbacks)


    //Eseguito dall'applicazione
    public void stepAsynch(int time, IApplAction stepok, IApplAction stepko) {
        new Thread() {
            public void run() {
                doingStepAsynch = true;
                try {
                    //CommUtils.outred("stepAsynch Thread starts " + Thread.currentThread().getName());
                    if( tracing ) CommUtils.aboutThreads("stepAsynch new Thread | ");
                    startTimer(); //per getDuration()
                    wsCommSupport.forward(VrobotMsgs.forwardcmd.replace("TIME", "" + time));
                    String msg = msgQueue.take();  //Appl si blocca
                    if( tracing ) CommUtils.outmagenta("%%% stepAsynch msg:" + msg);
                    if (msg.contains("stepdone")) stepok.handle(msg);
                    else stepko.handle(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //CommUtils.outred("stepAsynch Thread ends " + Thread.currentThread().getName());
            }
        }.start();
    }

    public void stepAsynch(int time) {
        doingStepAsynch = true;
        try {
            if( tracing ) CommUtils.aboutThreads("stepAsynchActorLike | ");
            startTimer(); //per getDuration()
            wsCommSupport.forward(VrobotMsgs.forwardcmd.replace("TIME", "" + time));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        //Uso Method - NON VA: object is not an instance of declaring class
    /*
    public void stepAsynch(int time, Method stepOk, Method stepFail) throws Exception {
            //startTimer();
            long startTime = System.currentTimeMillis();
            String result  = request(VrobotMsgs.forwardcmd.replace("TIME",""+time));
            long elapsed   = System.currentTimeMillis() - startTime;
            if (result.contains("true")) stepOk.invoke(this, "w ok duration:" + elapsed);
                //return "w ok duration:" + elapsed;
            else stepFail.invoke(this, " duration:" + elapsed);
                //return "w fail duration:" + elapsed;
    }*/


}

