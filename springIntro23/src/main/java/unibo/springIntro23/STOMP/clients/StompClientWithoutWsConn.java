package unibo.springIntro23.STOMP.clients;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StompClientWithoutWsConn {

    private static final String URL = "ws://localhost:8085/unibo";

    private static WebSocketStompClient stompClient;
/*
    protected static void connectForSockJs(){
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        SockJsClient sockjsClient = new SockJsClient(transports);
        stompClient               = new WebSocketStompClient(sockjsClient);

    }
*/
    protected static void connectForWebSocket(){
        WebSocketClient client  = new StandardWebSocketClient();
         stompClient            = new WebSocketStompClient(client);
    }
    public static void main(String[] args) {
        //connectForSockJs();  //To be used when the server is based
        connectForWebSocket();
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(URL, sessionHandler);

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
}
