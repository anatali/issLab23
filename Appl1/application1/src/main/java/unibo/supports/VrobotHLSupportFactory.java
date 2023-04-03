package unibo.supports;

import unibo.appl1.common.IVrobotMoves;
import unibo.basicomm23.http.HttpConnection;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.ws.WsConnection;


public class VrobotHLSupportFactory {

    public static IVrobotMoves create(String hostIp, ProtocolType protocol) throws Exception {
        if( protocol == ProtocolType.http ) return supportForHTTP(hostIp);
        else if( protocol == ProtocolType.ws ) return supportForWS(hostIp);
        else throw new Exception("VrobotHLSupportFactory protocol not allowed");
    }
    public static IVrobotMoves supportForHTTP(String hostIp){
        Interaction connToWEnv     = HttpConnection.create(hostIp+":8090/api/move");
        return new VrobotHLMovesHTTP( connToWEnv  );
    }

    public static IVrobotMoves supportForWS(String hostIp){
        Interaction connToWEnv = WsConnection.create(hostIp+":8091");
         return new VrobotHLMovesInteractionAsynch( connToWEnv  );
    }

    public static IVrobotMoves supportForWSPlanned(String hostIp, IObserver obs, boolean fastPc){
        Interaction connToWEnv     = WsConnection.create(hostIp+":8091");
        //Planner23Util planner          = new Planner23Util();
        //planner.initAI();
        //return new VrobotHLMovesWSPlanned((WsConnection) connToWEnv, planner, obs, fastPc );
        return null;
    }
}
