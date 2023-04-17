package ProdConsStream;


import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Producer extends ActorBasic23 {
    private ArrayList<Integer> data = new ArrayList<Integer>(  Arrays.asList(1,2,3,4) );
    public Producer(String name, ActorContext23 ctx) {
        super(name, ctx);
        autostart = true;
    }

    protected void emitValue(int v){
        IApplMessage event = CommUtils.buildEvent(name,"data", ""+v);
        CommUtils.outblue(name + " emitLocalStreamEvent: " + event);
        emitLocalStreamEvent(event);
        CommUtils.delay(1000);
    }
    protected void produceData(){
        //Attendo che il sistema sia attivato, prima di produrre
        CommUtils.delay(2000);
       data.forEach( v -> emitValue(v));
       CommUtils.aboutThreads("Produce end");
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        CommUtils.outgray(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());
        if (msg.msgId().equals("startcmd") && msg.msgContent().equals("start")) {
            produceData();
        }
    }
}
