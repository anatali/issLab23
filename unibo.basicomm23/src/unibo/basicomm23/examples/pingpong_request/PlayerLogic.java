package unibo.basicomm23.examples.pingpong_request;

import unibo.basicomm23.utils.CommUtils;

public class PlayerLogic {
    protected int count=0;
    public String hitBall(){
        return "hit_"+count++;
    }
    public String hitBallAsAnswer(String hit){
        CommUtils.delay(1000);
        return "answerto|"+hit;
    }
    //Per simulare battute o risposte errate
    public String hitBallWrong(){
        return "wrong_hit_"+count++;
    }
    public String hitBallWrongAsAnswer(String hit){
        CommUtils.delay(1000);
        return "wrong_answerto|"+hit;
    }
}
