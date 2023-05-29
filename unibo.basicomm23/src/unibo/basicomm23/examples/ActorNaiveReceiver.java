package unibo.basicomm23.examples;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMsgHandler;
import unibo.basicomm23.utils.CommUtils;

public abstract class ActorNaiveReceiver extends ApplMsgHandler {

    public ActorNaiveReceiver(String name ){
        super( name );
    }

    //protected abstract void body() throws Exception;
    public abstract void elaborate(IApplMessage message, Interaction conn);

    /*
    public void activate(){
        new Thread(){
            public void run(){
                try {
                    body();
                  } catch (Exception e) {
                    CommUtils.outred("");
                }
            }
        }.start();
    }*/

}
