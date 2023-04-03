package unibo.supports;

import org.json.simple.JSONObject;
import unibo.appl1.common.IApplAction;
import unibo.appl1.common.IVrobotMoves;
import unibo.appl1.common.VrobotMsgs;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;

import java.util.concurrent.BlockingQueue;

public class VrobotHLMovesInteractionAsynch extends ApplAbstractObserver implements  IVrobotMoves {
    protected Interaction wsCommSupport;
    protected int elapsed = 0; //modified by update
    protected String moveResult = null;  //for observer part
    protected BlockingQueue<String> msgQueue;
    boolean doingStepAsynch = false;  //vedi update
    protected int threadCount = 1;


    public VrobotHLMovesInteractionAsynch(Interaction wsCommSupport) {
        this.wsCommSupport = wsCommSupport;
        ((WsConnection) wsCommSupport).addObserver(this);
        CommUtils.aboutThreads("     VrobotHLMovesInteractionAsynch |");
        CommUtils.outblue(
                "     VrobotHLMovesInteractionAsynch | CREATED in " + Thread.currentThread().getName());
    }

    public void setMsgQueue(BlockingQueue<String> msgQueue) {
        this.msgQueue = msgQueue;
    }

    public Interaction getConn() {
        return wsCommSupport;
    }

    @Override
    public boolean step(int time) throws Exception {
        doingStepAsynch = false;
        CommUtils.outgreen("     VrobotHLMovesInteractionAsynch | step time=" + time);
        String cmd = VrobotMsgs.forwardcmd.replace("TIME", "" + time);
        String result = request(cmd);
        //CommUtils.outgreen("     VrobotHLMovesInteractionAsynch | step result="+result);
        //result=true elapsed=... OPPURE collision elapsed=...
        return result.contains("true");
    }

    @Override
    public void turnLeft() throws Exception {
        //CommUtils.outred("     VrobotHLMovesInteractionAsynch | turnLeft " );
        doingStepAsynch = false;
        request(VrobotMsgs.turnleftcmd);
    }

    @Override
    public void turnRight() throws Exception {
        doingStepAsynch = false;
        request(VrobotMsgs.turnrightcmd);
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
        CommUtils.outgreen("     VrobotHLMovesInteractionAsynch | halt");
        wsCommSupport.forward(VrobotMsgs.haltcmd);
        CommUtils.delay(150); //wait for halt completion since halt on ws does not send answer
        //CommUtils.outgreen("     VrobotHLMovesInteractionAsynch | halt done " + moveResult );
    }
// Observer part

    public String request(String msg) throws Exception {
        moveResult = null;
        //Invio fire-and.forget e attendo modifica di  moveResult da update
        startTimer();
        //CommUtils.outgreen("     VrobotHLMovesInteractionAsynch | request " + msg);
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
            //if( info.contains("turn"))
            //CommUtils.outyellow("     VrobotHLMovesInteractionAsynch | updateBasic:"+info + " elapsed=" + elapsed);
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
                CommUtils.outyellow("     VrobotHLMovesInteractionAsynch | updateBasic collision detected ");
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
            //if( info.contains("turn"))
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
                msgQueue.put("sonar(" + d + " )");  //Solo per Appl1CoreActorlike
                return;
            }
            if (jsonObj.get("collision") != null) {
                msgQueue.put("collision(" + elapsed + " )"); //Solo per Appl1CoreActorlike
                return;
            }
            if (jsonObj.get("endmove") != null) {
                //{"endmove":"true/false ","move":"..."}
                boolean endmove = jsonObj.get("endmove").toString().contains("true");
                String move = jsonObj.get("move").toString();
                //CommUtils.outred("     VrobotHLMovesInteractionAsynch | updateForAsynch move=" + move);
                //move moveForward-collision or moveBackward-collision
                if (endmove) {
                    msgQueue.put("stepdone(" + elapsed + ")");
                } else if (move.contains("interrupted"))
                    msgQueue.put("stepfailed(" + elapsed + ", interrupt )");
                else if (move.contains("collision"))
                    msgQueue.put("stepfailed(" + elapsed + ", collision )");
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


    public void stepAsynch(int time) {
        doingStepAsynch = true;
        try {
            CommUtils.aboutThreads("stepAsynchActorLike | ");
            startTimer(); //per getDuration()
            wsCommSupport.forward(VrobotMsgs.forwardcmd.replace("TIME", "" + time));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

