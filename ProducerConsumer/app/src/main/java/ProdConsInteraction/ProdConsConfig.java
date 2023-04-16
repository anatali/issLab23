package ProdConsInteraction;

import unibo.basicomm23.msg.ProtocolType;

public class ProdConsConfig {
    public static final String host   = "localhost";
    public static final int port      = 8888;

    public static ProtocolType protocol  = ProtocolType.tcp;

    public static   String hostAddr =
            protocol==ProtocolType.coap?(host+":"+port+"/basicomm23/consumer/"):host;
    public static   String entry =
            protocol==ProtocolType.coap?"/basicomm23/consumer":(""+port);


    public static void setProtocol(ProtocolType p){
        protocol = p;
        hostAddr = protocol==ProtocolType.coap?(host+":"+port):host;
        entry    = protocol==ProtocolType.coap?"/basicomm23/consumer":(""+port);
    }
}
