package unibo.supports;

import org.json.simple.JSONObject;
import unibo.basicomm23.http.HTTPCommApache;
import unibo.common.CollisionException;
import unibo.common.IVrobotMoves;
import unibo.common.VrobotMsgs;

public class  VrobotHLMovesHTTPApache implements IVrobotMoves {
    private HTTPCommApache httpSupport  ;

    public VrobotHLMovesHTTPApache(HTTPCommApache httpSupport) {
        this.httpSupport = httpSupport;
    }
    //Implementazione delle operazioni di  IVrobotMoves
    @Override
    public void turnLeft() throws Exception{
        httpSupport.requestSynch(  VrobotMsgs.turnleftcmd  );
    }
    @Override
    public void turnRight() throws Exception{
        httpSupport.requestSynch(  VrobotMsgs.turnrightcmd  );
    }
    @Override
    public void forward(int time) throws Exception{
        JSONObject result = httpSupport.requestSynch(
                VrobotMsgs.forwardcmd.replace("TIME",""+time)  );
        if( result.toString().contains("collision")  ){
            throw new CollisionException(  ) ;
        }
    }
    @Override
    public void backward(int time) throws Exception{
        JSONObject result = httpSupport.requestSynch(
                VrobotMsgs.backwardcmd.replace("TIME",""+time)  );
        if( result.toString().contains("collision")  ){
            throw new CollisionException(  ) ;
        }
    }
    @Override
    public void halt( ) throws Exception {
        httpSupport.requestSynch(  VrobotMsgs.haltcmd  );
    }
    @Override
    public boolean step(int time) throws Exception{
        String cmd = VrobotMsgs.forwardcmd.replace("TIME", ""+time);
        JSONObject result = httpSupport.requestSynch(   cmd  );
        //{"endmove":true,"move":"moveForward"}  OPPURE:
        //{"endmove":"false","move":"moveForward-collision"}
        boolean collision = result.toString().contains("collision");
        return ! collision;
    }
}
