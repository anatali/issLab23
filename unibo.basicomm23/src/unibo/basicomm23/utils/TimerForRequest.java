package unibo.basicomm23.utils;

public class TimerForRequest extends Thread {
    private int tout;
    private boolean toutExpired = false;
    private String answer       = null;

    public TimerForRequest(int tout) {
        this.tout=tout;
    }
    public synchronized String waitTout() throws InterruptedException {
        while ( ! toutExpired ) wait();
        return answer;
    }
    public synchronized void setExpired( ) {
        toutExpired = true;
        notifyAll();
    }
    public synchronized void setExpiredSinceAnswer( String answer ) {
        toutExpired = true;
        this.answer = answer;
        notifyAll();
    }

    public void run() {
        CommUtils.delay(tout );
        //CommUtils.outmagenta("TIMEOUT");
        setExpired();
    }
}

