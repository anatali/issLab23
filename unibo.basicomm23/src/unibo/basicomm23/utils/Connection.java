package unibo.basicomm23.utils;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;

public abstract class Connection implements Interaction {
public static boolean trace = false;

    @Override
    public void forward(IApplMessage msg) throws Exception {
        forward( msg.toString() );
    }

    @Override
    public IApplMessage request(IApplMessage msg) throws Exception {
        String answer = request(msg.toString());
        return new ApplMessage(answer);
    }

    @Override
    public IApplMessage request(IApplMessage msg, int tout) throws Exception {
        TimerForRequest t = new TimerForRequest(tout);
        t.start();

        new Thread(){
            public void run(){
                try {
                    String answer = request(msg.toString());
                    CommUtils.outmagenta("request with tout answer:" + answer);
                    t.setExpiredSinceAnswer(answer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        String answer = t.waitTout();
        if( answer == null ) throw new Exception("request timeout");
        else  return new ApplMessage(answer);
    }

    @Override
    public IApplMessage receive() throws Exception{
        String msg = receiveMsg();
        return new ApplMessage(msg);
    }
    @Override
    public void reply(IApplMessage msg) throws Exception {
        reply(msg.toString());
    }
    @Override
    public abstract void forward(String msg) throws Exception;

    @Override
    public abstract String request(String msg) throws Exception;

    @Override
    public abstract void reply(String reqid) throws Exception;

    @Override
    public abstract String receiveMsg() throws Exception;

    @Override
    public abstract void close() throws Exception;
}
