package it.unibo.virtualRobot2023.clients;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import unibo.basicomm23.utils.CommUtils;
import java.net.URI;

public class AutomaticTestHTTP {
    private  final String localHostName    = "localhost"; //"localhost"; 192.168.1.7
    private  final int port                = 8090;
    private  final String URL              = "http://"+localHostName+":"+port+"/api/move";
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private JSONParser simpleparser = new JSONParser();
    private  String turnrightcmd  = "{\"robotmove\":\"turnRight\"    , \"time\": \"300\"}";
    private  String turnleftcmd  = "{\"robotmove\":\"turnLeft\"     , \"time\": \"300\"}";
    private  String forwardcmd   = "{\"robotmove\":\"moveForward\"  , \"time\": \"1000\"}";  //long ...
    private  String backwardcmd  = "{\"robotmove\":\"moveBackward\" , \"time\": \"1000\"}";
    private  String haltcmd      = "{\"robotmove\":\"alarm\" ,        \"time\": \"10\"}";

    protected JSONObject callHTTP(String crilCmd )  {
        JSONObject jsonEndmove = null;
        try {
            StringEntity entity = new StringEntity(crilCmd);
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(URL))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Accept", "application/json")
                    .setEntity(entity)
                    .build();
            long startTime                 = System.currentTimeMillis() ;
            CloseableHttpResponse response = httpclient.execute(httppost);
            long duration  = System.currentTimeMillis() - startTime;
            String answer  = EntityUtils.toString(response.getEntity());
            CommUtils.outyellow( Thread.currentThread() + " callHTTP | answer= " + answer + " duration=" + duration );

            jsonEndmove = (JSONObject) simpleparser.parse(answer);
            CommUtils.outyellow("callHTTP | jsonEndmove=" + jsonEndmove + " duration=" + duration);
        } catch(Exception e){
            CommUtils.outred("callHTTP | " + crilCmd + " ERROR:" + e.getMessage());
        }
        return jsonEndmove;
    }
    protected void sendAlarmAfter( int time ){
        new Thread(){
            protected  JSONObject mycallHTTP(String crilCmd )  {
                CommUtils.outgreen( Thread.currentThread() + " mycallHTTP starts" );
                JSONObject jsonEndmove  = null;
                JSONParser mysimpleparser = new JSONParser();
                try {
                    StringEntity entity = new StringEntity(crilCmd);
                    HttpUriRequest httppost = RequestBuilder.post()
                            .setUri(new URI(URL))
                            .setHeader("Content-Type", "application/json")
                            .setHeader("Accept", "application/json")
                            .setEntity(entity)
                            .build();
                    long startTime                 = System.currentTimeMillis() ;
                    CloseableHttpResponse response = httpclient.execute(httppost);
                    long duration  = System.currentTimeMillis() - startTime;
                    String answer  = EntityUtils.toString(response.getEntity());
                    //CommUtils.outgreen( Thread.currentThread() + " mycallHTTP | answer= " + answer + " duration=" + duration );
                    jsonEndmove = (JSONObject) mysimpleparser.parse(answer);
                    CommUtils.outgreen(Thread.currentThread() + " mycallHTTP | jsonEndmove=" + jsonEndmove + " duration=" + duration);
                } catch(Exception e){
                    CommUtils.outred(Thread.currentThread() + " mycallHTTP | " + crilCmd + " ERROR:" + e.getMessage());
                }
                return jsonEndmove;
            }
            public void run(){
                CommUtils.delay(time);
                CommUtils.outgreen(Thread.currentThread() + " send alarm"  );
                JSONObject result = mycallHTTP(  haltcmd  );
                //if( result != null )
                CommUtils.outgreen(Thread.currentThread() + " sendAlarmAfter result=" + result);
            }
        }.start();
    }

    @Before
    public void init(){
        //httpclient = HttpClients.createDefault();
        CommUtils.outmagenta("AutomaticTestHTTP INIT");
    }

    @Test
    public void doForward() {
        CommUtils.outmagenta("doForward "  );
        JSONObject result = callHTTP(  forwardcmd  );
        CommUtils.outblue("moveForward result=" + result);
        assert( result.get("endmove").equals("true") && result.get("move").equals("moveForward")) ;
        //BACK TO HOME
        JSONObject result1 = callHTTP(  backwardcmd  );
        CommUtils.outblue("moveBackward result1=" + result1);
        assert( result1.get("move").toString().contains("moveBackward")) ;
    }

    @Test
    public void doHalt() {
        CommUtils.outmagenta("doHalt "  );
        sendAlarmAfter(500);
        JSONObject result = callHTTP(  forwardcmd  );
        CommUtils.outblue("doHalt result=" + result);
        assert( result.get("endmove").equals("false") && result.get("move").equals("moveForward-interrupted")) ;
        //BACK TO HOME after move completion
        CommUtils.delay(1000);
        JSONObject result1 = callHTTP(  backwardcmd  );
        CommUtils.outblue("doHalt result1=" + result1);
        assert( result1.get("move").toString().contains("moveBackward")) ;

    }

}
