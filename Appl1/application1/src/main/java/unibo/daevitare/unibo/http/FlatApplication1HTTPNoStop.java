package unibo.daevitare.unibo.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.utils.CommUtils;
import unibo.common.VrobotMsgs;

import java.net.URI;

public class FlatApplication1HTTPNoStop {
    private  final String localHostName    = "localhost"; //"localhost"; 192.168.1.7
    private  final int port                = 8090;
    private  final String URL              = "http://"+localHostName+":"+port+"/api/move";
    private JSONParser simpleparser        = new JSONParser();
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private int Nedges = 0;  //For testing

    //Procedura responsabile della business logic
    public void walkAtBoundary() {
        for( int i=1; i<=4;i++) {
            walkAheadUntilCollision(i);
            requestSynch(URL, VrobotMsgs.turnleftcmd); //discard result
            Nedges++;  //For testing
        }
    }

    //Procedura responsabile del movimento in avanti, con collisione
    private void walkAheadUntilCollision(int n) {
        String cmd = VrobotMsgs.forwardlongcmd;
        CommUtils.outyellow("walkAheadUntilCollision requestSynch cmd="+cmd);
        JSONObject result = requestSynch(  URL, cmd  );
        CommUtils.outblue("walkAheadUntilCollision cmd result=" + result + " n="+n);
        if( ! result.toString().contains("collision")  ) {
            CommUtils.outred("fatal error: no collision");
        }
    }

    public int getNedges(){ //For testing
        return Nedges;
    }

    //-----------------------------------------
    protected JSONObject requestSynch( String URL, String crilCmd )  {
        JSONObject jsonEndmove = null;
        try {
            StringEntity entity = new StringEntity(crilCmd);
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(URL))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Accept", "application/json")
                    .setEntity(entity)
                    .build();
            CloseableHttpResponse response = httpclient.execute(httppost);
            String jsonStr = EntityUtils.toString( response.getEntity() );
            jsonEndmove = (JSONObject) simpleparser.parse(jsonStr);
        } catch(Exception e){
            CommUtils.outred("      requestSynch | ERROR:" + e.getMessage());
        }
        return jsonEndmove;
    }

    public static void main( String[] args ){
        CommUtils.aboutThreads("Before start - ");
        FlatApplication1HTTPNoStop appl = new FlatApplication1HTTPNoStop();
        appl.walkAtBoundary();
        CommUtils.aboutThreads("At end - ");
    }
}
