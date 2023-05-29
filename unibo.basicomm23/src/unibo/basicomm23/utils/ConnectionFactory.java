package unibo.basicomm23.utils;

import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.http.HttpConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.tcp.TcpConnection;
import unibo.basicomm23.udp.UdpClientSupport;
import unibo.basicomm23.udp.UdpConnection;
import unibo.basicomm23.ws.WsConnection;


public class ConnectionFactory {
    public static Interaction createClientSupport23(
            ProtocolType protocol, String host, String entry){
        return (Interaction) createClientSupport(protocol,host,entry);
    }
        public static Interaction createClientSupport(
                ProtocolType protocol, String host, String entry ){
          //CommUtils.outyellow("    --- ConnectionFactory | createClientSupport protocol=" + protocol + " entry=" +entry );
          try {
              switch( protocol ){
                case http  : {
                    Interaction  conn  = HttpConnection.create(  host+":"+entry );
                    CommUtils.outyellow("    --- ConnectionFactory | create htpp conn:" + host+":"+entry);
                    return conn;
                }
                case ws   : {
                    Interaction  conn  =  WsConnection.create( host+":"+entry );
                    CommUtils.outyellow("    --- ConnectionFactory | create ws conn:" + host+":"+entry);
                    return conn;
                }
                case tcp   : {
                    //entry = portNum
                    int port              = Integer.valueOf(entry);
                    Interaction conn  = TcpConnection.create( host, port );
                    CommUtils.outyellow("    --- ConnectionFactory | create tcp port:" + port);
                    return conn;
                }
                case udp   : {
                      //entry = portNum
                      int port              = Integer.valueOf( entry );
                      Interaction conn  = UdpConnection.create(host, port);
                      CommUtils.outyellow("    --- ConnectionFactory | create udp port:" + port);
                      return conn;
                }
                case coap   : {
                      CommUtils.outyellow("    --- ConnectionFactory | create coap conn " + host+entry );
                      Interaction  conn  = CoapConnection.create(host+entry, "");
                      return conn;
                }
                case mqtt   : {
                      CommUtils.outyellow("    --- ConnectionFactory | create mqtt conn TODO"  );
                      //Interaction  conn  =  MqttConnection.create("clientName","mqttBrokerAddr","topic");
                      return null;
                }
                case bluetooth   : {
                      CommUtils.outyellow("    --- ConnectionFactory | create bluetooth conn TODO"  );
                      return null;
                }
                case serial   : {
                    CommUtils.outyellow("    --- ConnectionFactory | create serial conn TODO"  );
                    return null;
                }
                default: return null;
             }
          }catch (Exception e) {  e.printStackTrace(); return null; }
        }//createClientSupport
}
