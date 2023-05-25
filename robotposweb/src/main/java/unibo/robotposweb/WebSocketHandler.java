package unibo.robotposweb;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import unibo.basicomm23.utils.CommUtils;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/*
In Spring we can create a customized handler by extends abstract class
AbstractWebSocketHandler or one of it's subclass,
either TextWebSocketHandler or BinaryWebSocketHandler:
 */
public class WebSocketHandler extends AbstractWebSocketHandler implements IWsHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private JSONParser jsonparser = new JSONParser();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        CommUtils.outblue("WebSocketHandler | Added the session:" + session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        CommUtils.outblue("WebSocketHandler | Removed the session:" + session);
        super.afterConnectionClosed(session, status);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String movecmd = message.getPayload();
        CommUtils.outblue("WebSocketHandler | handleTextMessage Received: " + movecmd);
        //Gestione di comandi remoti via Ws come RobotController
        //sendToAll("webRobot22 WebSocketHandler echo: "+cmd);
        try {
            JSONObject json = (JSONObject) jsonparser.parse(movecmd);
            String move = json.get("robotmove").toString();
            CommUtils.outblue("WebSocketHandler | handleTextMessage doing: " + move);
            RobotUtils.sendMsg(RobotHIController.actorName,move);
        } catch (Exception e) {
            CommUtils.outred("WebSocketHandler | handleTextMessage ERROR:"+e.getMessage());
        }

    }
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("WebSocketHandler | handleBinaryMessage Received " );
        //session.sendMessage(message);
        //Send to all the connected clients
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }

    public void sendToAll(String message)  {
        try{
            CommUtils.outblue("WebSocketHandler | sendToAll String: " + message);
            //JSONObject jsm = new JSONObject(message);
            //IApplMessage mm = new ApplMessage(message);
            //String mstr    = mm.msgContent();//.replace("'","");
            sendToAll( new TextMessage(message)) ;
        }catch( Exception e ){
            CommUtils.outred("WebSocketHandler | sendToAll String ERROR:"+e.getMessage());
        }
    }
    public void sendToAll(TextMessage message) {
        //CommUtils.outblue("WebSocketHandler | sendToAll " + message.getPayload() + " TextMessage sessions:" + sessions.size());
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            try{
                WebSocketSession session = iter.next();
                CommUtils.outblue("WebSocketHandler | sendToAll " +
                        message.getPayload() + " for session " + session.getRemoteAddress() );
                //synchronized(session){
                    session.sendMessage(message);
                //}
            }catch(Exception e){
                CommUtils.outred("WebSocketHandler | TextMessage " + message + " ERROR:"+e.getMessage());
            }
        }
    }

}
