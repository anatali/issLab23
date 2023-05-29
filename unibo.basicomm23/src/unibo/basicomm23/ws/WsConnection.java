package unibo.basicomm23.ws;

import java.net.URI;
import java.util.HashMap;
import java.util.Vector;
import javax.websocket.*;
import unibo.basicomm23.interfaces.IObservable;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.utils.*;

@ClientEndpoint
public class WsConnection  extends Connection implements  IObservable{ //implements Interaction2021, IObservable{
	private static HashMap<String,WsConnection> connMap= new HashMap<String,WsConnection>();
	private Vector<IObserver> observers                = new Vector<IObserver>();
    private boolean opened         = false;
    private Session userSession    = null;


	public static WsConnection create(String addr ){
        //ColorsOut.outappl("WsConnection | wsconnect addr=" + addr + " " + connMap.containsKey(addr), ColorsOut.GREEN );
		if( ! connMap.containsKey(addr)){
			connMap.put(addr, new WsConnection( addr ) );
		}else {
		    CommUtils.outyellow("WsConnection | ALREADY connected to addr=" + addr  );
		}
		return connMap.get(addr);
	}
	public static WsConnection create(String addr, Vector<IObserver> externalObs ){
		WsConnection conn = create(addr);
		externalObs.forEach( obs -> conn.addObserver(  obs  ));
		return conn;
	}


	private WsConnection( String addr  ) {
		wsconnect(addr);
		//systemObserver = new WsConnSysObserver(this);
		//addObserver( systemObserver );

//		addMessageHandler( new IWsMsgHandler() {
//			@Override
//			public void handleMessage(String message) {
//				updateObservers(message);
//			}
//		});
 	}

 	//register message handler
 //   private void addMessageHandler(IWsMsgHandler msgHandler) {  this.messageHandler = msgHandler; }

    private void wsconnect(String wsAddr){    // localhost:8091
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI("ws://"+wsAddr);
			CommUtils.outyellow("WsConnection | wsconnect to uri=" + uri  );
            //CommUtils.outyellow("WsConnection | wsconnect container=" + container + " uri=" + uri );
            container.connectToServer(this, uri);
            CommUtils.outyellow("WsConnection | wsconnected to wsAddr=" + wsAddr  );
        } catch ( Exception ex) {
        	CommUtils.outred("WsConnection | wsconnect ERROR: " + ex.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
    	CommUtils.outyellow("WsConnection | opening websocket" ); //userSession="+userSession.getRequestURI()
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
    	CommUtils.outyellow("WsConnection | closing. userSession="+userSession);
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message)   {
    	//CommUtils.outyellow("WsConnection | onMessage:" + message);
		updateObservers(message);
    }

    public void sendMessage(String message ) throws Exception {
		//CommUtils.outmagenta("WsConnection | sendMessage " + message + " userSession=" + userSession);
//        this.userSession.getAsyncRemote().sendText(message);
			this.userSession.getBasicRemote().sendText(message);    //synch: blocks until the message has been transmitted
    }

//Since Interaction2021
	@Override
	public void forward( String msg) throws Exception {
    	//try {
		    ColorsOut.out("WsConnection | sendALine "  + msg, ColorsOut.CYAN);
		    sendMessage(msg);
			//observers.forEach( v -> { if( v instanceof WsConnSysObserver) ((WsConnSysObserver)v).startMoveTime() ; });
    	//}catch( Exception e ) {ColorsOut.outerr("WsConnection | ERROR "  + e.getMessage()  );}
 	}

	@Override
	public String request(String msg) throws Exception {
		//We could call forward(msg) but the answer? It is is related to updateObservers
		throw new Exception("WsConnection: request not allowed");
		//return systemObserver.dorequest(msg); //NOT VERY USEFUL, since asynch
	}

	@Override
	public void reply(String msgJson) throws Exception {
		forward(msgJson);
	}

	@Override
	public String receiveMsg() throws Exception {
		//ColorsOut.out("WsConnection | receiveMsg, perhaps onMessage"  , ColorsOut.MAGENTA);
		throw new Exception("WsConnection: receiveMsg not allowed");
	}

	@Override
	public void close() throws Exception {
	     //boolean gracefulShutdown = myWs.close(1000, "close");
	     //ColorsOut.out("WsConnection | close gracefulShutdown=" + gracefulShutdown, ColorsOut.YELLOW);
	}

//Since IObservable
	@Override
	public void addObserver(IObserver obs) {
        CommUtils.outyellow("WsConnection | addObserver " + obs  );
		observers.add( obs);
	}
	@Override
	public void deleteObserver(IObserver obs) {
		observers.remove( obs);
	}

    protected void updateObservers( String msg ){
		//CommUtils.outyellow("WsConnection | updateObservers " + observers.size()  );
        observers.forEach( v -> v.update(null, msg) );
    }

    
 }
