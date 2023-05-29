package unibo.basicomm23.mqtt;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.IApplMsgHandlerMqtt;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;


/*
 * MqttConnection implementa Interaction e quindi realizza il concetto di connessione nel caso di MQTT.
 * Nel caso di MQTT, una connessione � realizzata usando due topic, la prima di nome t1 
 * e la seconda di nome t1answer.
 * Un EnablerServer fissa il nome t1  (ad es LedServerMqtt)
 * Un proxyclient c1 usa il nome t1 per inviare comandi e richieste al server 
 * e fa una subscribe su t1answer (o meglio su t1c1answer) per ricevere le risposte alle sue request.
 * L'operazione request del proxyclient usa la sua istanza di MqttConnection (mqttProxy) per fare la 
 * publish della richiesta su t1. 
 * Per ricevere la risposta dal server il proxyclient fa un polling (poll)
 * sulla blockingqueue di mqttProxy in modo da permettere la esecuzione 
 * di messageArrived del ClientApplHandlerMqtt che fa l'operazione di put sulla blockingqueue di mqttProxy.
 */
public class MqttConnection extends Connection { //implements Interaction {
 	
protected MqttClient client;
//public static final String topicInput = "topicCtxMqtt";
protected static MqttConnection mqttSup = null ;  //to realize a singleton

protected BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>(10);
protected String clientid;
//protected IContextMsgHandlerMqtt handler;
protected String brokerAddr;
protected boolean isConnected   = false;

protected String topicInput = "";

	public static MqttConnection getSupport() {
		return mqttSup;
	}

	public static synchronized MqttConnection create(String clientName  ) {
		if( mqttSup == null  ) mqttSup = new MqttConnection( clientName );
		return mqttSup;
	}

	public static synchronized MqttConnection create(String clientName, String mqttBrokerAddr, String topic ) {
		if( mqttSup == null  ) mqttSup = new MqttConnection(clientName, mqttBrokerAddr, topic);
		return mqttSup;
	}
	
	public void setTopic(String topic) {
		topicInput = topic;
	}
	
//	public static synchronized MqttConnection createSupport(String clientName, String topicToSubscribe) {
//		if( mqttSup == null  ) mqttSup = new MqttConnection(clientName,topicToSubscribe);
//		return mqttSup;
//	}

    public MqttConnection( String clientName ) {  
    }
    
    protected MqttConnection(String clientName, String mqttBrokerAddr, String topic) { //, String topicToSubscribe
    	setTopic(topic);
    	connectToBroker(clientName, mqttBrokerAddr);	
//		handler = new ContextMqttMsgHandler( "ctxH"  );
//    	subscribe(topicToSubscribe, handler);
    }
     
    public BlockingQueue<String> getQueue() {
    	return blockingQueue;
    }
    
//    public IContextMsgHandlerMqtt getHandler() {
//    	return handler;
//    }
    
    
    public boolean connect(String clientid,  String brokerAddr) {
    	connectToBroker(clientid,brokerAddr);
    	return isConnected;
    }
    public void connectToBroker(String clientid,  String brokerAddr) {
    	if( isConnected ) return;
		try {
			ColorsOut.out("MqttConnection | connectToBroker clientid " + clientid  + " to broker " + brokerAddr );
			this.brokerAddr = brokerAddr;
			MemoryPersistence persistence = new MemoryPersistence();
			//persistence: per evitare see https://github.com/eclipse/paho.mqtt.java/issues/794
			client          = new MqttClient(brokerAddr, clientid, persistence);
			ColorsOut.out("MqttConnection | connectToBroker clientSupportId=" + client.getClientId() );
			MqttConnectOptions connOpts = new MqttConnectOptions();
			
		    connOpts.setCleanSession(true);
		    //connOpts.setUserName(userName);
		    //connOpts.setPassword(passWord.toCharArray());
		    /* 
		     * This value, measured in seconds, defines the maximum time interval the client  
 			 *  will wait for the network connection to the MQTT server to be established
		    */
		    connOpts.setConnectionTimeout(60); 
		    /* 
		     * This value, measured in seconds, defines the maximum time interval between 
		     * messages sent or received
		     */
		    connOpts.setKeepAliveInterval(30); 
		    connOpts.setAutomaticReconnect(true);			
			
//			options.setKeepAliveInterval(480);
//			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);

			client.connect(connOpts);
			this.clientid   = clientid; //client.getClientId();
			isConnected = true;
 			
			ColorsOut.out("MqttConnection | connected client " + client.getClientId() + " to broker " + brokerAddr );
		} catch (MqttException e) {
			isConnected = false;
			ColorsOut.outerr("MqttConnection  | connect Error:" + e.getMessage());
		}    	
    }
 
   //Introduced for unibo.qakactor22
    public void setCallback( MqttCallback h ) {
    	client.setCallback(h);
    }
    //Introduced for unibo.qakactor22
    public void subscribe( String t ) throws MqttException {
    	client.subscribe(t);
    }
   
	public void disconnect() {
		try {
			client.disconnect();
			client.close();
			isConnected = false;
			ColorsOut.out(clientid + " | disconnect ", ColorsOut.CYAN);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttConnection  | disconnect Error:" + e.getMessage());
		}
	}
	
	
	public void unsubscribe( String topic ) {
		try {
			client.unsubscribe(topic);
			ColorsOut.out("unsubscribed " + clientid + " topic=" + topic + " blockingQueue=" + blockingQueue, ColorsOut.CYAN);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttConnection  | unsubscribe Error:" + e.getMessage());
		}
	}
	

	//To receive and handle a message (command or request)
	public void subscribe ( String topic, IApplMsgHandlerMqtt handler) {
		//this.handler = handler;
		subscribe(clientid, topic, handler);    
		ColorsOut.outappl(clientid + " | MqttConnection handler="+
				handler + " subscribed to topic:" + topic, ColorsOut.MAGENTA);
	}
	
	protected void subscribe(String clientid, String topic, MqttCallback callback) {
		try {
			client.setCallback( callback );
			client.subscribe( topic );
			ColorsOut.outappl("subscribed " + client + " topic=" + topic
					+ " callback=" + callback, ColorsOut.MAGENTA);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttConnection  | subscribe Error:" + e.getMessage());
		}
	}
	
	//To receive and handle an answer
	public void subscribe(String clientid, String answertopic) {
		subscribe( clientid, answertopic, new MqttConnectionCallback(client.getClientId() , blockingQueue));
	}

	public void publish(String topic, String msg ) {
		publish( topic, msg, 2, false );
	}
	
	public void publish(String topic, String msg, int qos, boolean retain) {
		//ColorsOut.outappl("publish " + msg + " on " + topic, ColorsOut.BLUE);
		MqttMessage message = new MqttMessage();
		if (qos == 0 || qos == 1 || qos == 2) {
			//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(qos);
		}
		try {
			ColorsOut.outappl("MqttConnection  | publish topic=" + topic + " msg=" + msg + " client=" + client, ColorsOut.BLUE);
			message.setPayload(msg.toString().getBytes());		 
			client.publish(topic, message);
			ColorsOut.outappl("MqttConnection  | publish-DONE on topic=" + topic, ColorsOut.CYAN );
		} catch (MqttException e) {
			ColorsOut.outerr("MqttConnection  | publish Error "  + e.getMessage());
		}
	}
	
//----------------------------------------------------	
	@Override
	public void forward(String msg) throws Exception {
		ColorsOut.outappl("forward " + msg , ColorsOut.BLUE);
		try{
			new ApplMessage(msg); //no exception => we can publish
		}catch( Exception e ) { //The message is not structured
			ColorsOut.outappl("forward ERROR:" + e.getMessage() , ColorsOut.BLUE);
			IApplMessage msgAppl = CommUtils.buildDispatch("mqtt", "cmd", msg, "unknown");
		}				
		publish(topicInput, msg, 2, false);	
	}

	
	protected MqttClient setupConnectionForAnswer(String answerTopicName) {
		try{
			MemoryPersistence persistence = new MemoryPersistence();
			//persistence: per evitare see https://github.com/eclipse/paho.mqtt.java/issues/794
			MqttClient clientAnswer    = new MqttClient(brokerAddr, "clientAnswer", persistence);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setKeepAliveInterval(480);
			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
			clientAnswer.connect(options);
	 		//Colors.out("MqttConnection | connected clientAnswer to " + brokerAddr  , Colors.CYAN);
			IApplMsgHandler handler = null;  //TODO
	 		MqttAnswerHandler ah = new MqttAnswerHandler( "replyH", handler, blockingQueue );	
	 		clientAnswer.setCallback(ah);
	 		clientAnswer.subscribe(answerTopicName);		
	 		return clientAnswer;
		}catch( Exception e ) { //The message is not structured
			ColorsOut.outerr("MqttConnection | setupConnectionFroAnswer ERROR:" + e.getMessage());
			return null;
		}
	}
	
	@Override
	public String request(String msg) throws Exception { //msg should contain the name of the sender
		ColorsOut.outappl("... request " + msg + " by clientid=" + clientid + " support=" + this, ColorsOut.CYAN);
		//INVIO RICHIESTA su topic
//		MqttAnswerHandler ah = null;
		IApplMessage requestMsg;
		try{
			 requestMsg = new ApplMessage(msg); //no exception => we can publish
		}catch( Exception e ) { //The message is not structured
			ColorsOut.outerr("MqttConnection | request warning:" + e.getMessage());
			requestMsg = CommUtils.buildRequest("mqtt", "request", msg, "unknown");
		}			
		
//Preparo per ricevere la risposta
		String sender = requestMsg.msgSender();
		String reqid  = requestMsg.msgId();
		String answerTopicName = "answ_"+reqid+"_"+sender;
		ColorsOut.outappl("request answerTopicName="+answerTopicName, ColorsOut.RED);
//		ah = new MqttAnswerHandler( "replyH", blockingQueue );	
		MqttClient clientAnswer = setupConnectionForAnswer(answerTopicName);

//Invio la richiesta 		
		publish(topicInput, requestMsg.toString(), 2, false);	
 		
 		//ATTESA RISPOSTA su answerTopic. See MqttConnectionCallback
		String answer = receiveMsg();
		clientAnswer.disconnect();
		clientAnswer.close();
		return answer;
		
 	}
	
	protected String waitFroAnswerPolling(MqttClient clientAnswer ) {
		String answer = null;
		while( answer== null ) {
			answer=blockingQueue.poll() ;
			ColorsOut.out("MqttConnection | blockingQueue-poll answer=" + answer, ColorsOut.CYAN  );
			CommUtils.delay(200); //il client ApplMsgHandler dovrebbe andare ...
		}	
		ColorsOut.out("MqttConnection | request-answer=" + answer + " blockingQueue=" + blockingQueue, ColorsOut.CYAN);
 		try {
 			ApplMessage msgAnswer = new ApplMessage(answer); //answer is structured
 			answer = msgAnswer.msgContent(); 		
 			//Disconnect ancd close the answer client
 			clientAnswer.disconnect();
 			clientAnswer.close();
   		}catch(Exception e) {
 			ColorsOut.outerr("MqttConnection | request-answer ERROR: " + e.getMessage()   ); 			
 		}
		return answer;
	}
	
	protected String waitFroAnswerBlocking(MqttClient clientAnswer ) {
		String answer = null;
		try {
			answer =   receiveMsg();
//			answer=blockingQueue.take() ;
//			ColorsOut.out("MqttConnection | request-answer=" + answer + " blockingQueue=" + blockingQueue, ColorsOut.CYAN);
//		 	ApplMessage msgAnswer = new ApplMessage(answer); //answer is structured
//		 	answer = msgAnswer.msgContent(); 		
		 	//Disconnect ancd close the answer client
		 	clientAnswer.disconnect();
		 	clientAnswer.close();
		}catch(Exception e) {
		 	ColorsOut.outerr("MqttConnection | request-answer ERROR: " + e.getMessage()   ); 			
		}
		return answer;
	}

	@Override
	public String receiveMsg()   {
		//ColorsOut.outappl("MqttConnection | receiveMsg ... blockingQueue=" + blockingQueue.size() , ColorsOut.CYAN);
		String answer = "data unknown ...";

		try {
			answer = blockingQueue.take();
			ColorsOut.outappl("MqttConnection | receiveMsg answer="+answer + blockingQueue.size() , ColorsOut.CYAN);
			IApplMessage msgAnswer = new ApplMessage(answer); //answer is structured
			answer = msgAnswer.toString();
			ColorsOut.outappl("MqttConnection | receiveMsg answer=" + answer , ColorsOut.CYAN);
		}catch( Exception e){
			answer = e.getMessage().toString();
			ColorsOut.outerr(e.getMessage());
		}
		return answer;
	}
	
	public void reply(String msg) throws Exception {
		try {
			ApplMessage m = new ApplMessage(msg);
			//Colors.out("MqttConnection | reply  msg="+msg, Colors.CYAN);
			//TODO: Si potrebbe tenere traccia della richiesta e del caller
			String dest  = m.msgReceiver();
			String reqid = m.msgId();
			String answerTopicName = "answ_"+reqid+"_"+dest;
			publish(answerTopicName,msg,2,false); //"xxx"
			//Colors.out("MqttConnection | reply  DONE", Colors.CYAN );
 		}catch(Exception e) {
			ColorsOut.outerr("MqttConnection | reply msg not structured " + msg);
			//publish(topic+"Answer",msg,0,false);
		}
	}
	
//	protected String receiveMsg(String topic, BlockingQueue<String> bq) throws Exception{
//		ColorsOut.out("MqttConnection | receiveMsg2 topic=" + topic + " blockingQueue=" + bq, ColorsOut.CYAN);
//  		String answer = bq.take();
//		ColorsOut.out("MqttConnection | receiveMsg2 answer=" + answer + " blockingQueue=" + bq, ColorsOut.CYAN);
// 		try {
// 			ApplMessage msg = new ApplMessage(answer); //answer is structured
// 			answer = msg.msgContent(); 			
// 		}catch(Exception e) {
// 			ColorsOut.outerr("MqttConnection | receiveMsg2 " + answer + " not structured"   ); 
//  		}
//		client.unsubscribe(topic);
//		return answer;		 
//	}
	
//	protected String receiveMsg(String topic) throws Exception{
//		ColorsOut.out("MqttConnection | receiveMsg topic=" + topic + " blockingQueue=" + blockingQueue, ColorsOut.CYAN);
//		//subscribe("MqttConnection",topic);
// 		String answer = blockingQueue.take();
//		//Colors.out("MqttConnection | receiveMsg answer=" + answer + " blockingQueue=" + blockingQueue, Colors.CYAN);
// 		try {
// 			ApplMessage msg = new ApplMessage(answer); //answer is structured
// 			answer = msg.msgContent(); 			
// 		}catch(Exception e) {
// 			ColorsOut.outerr("MqttConnection | receiveMsg " + answer + " not structured"   ); 			
// 		}
//		client.unsubscribe(topic);
//		return answer;		 
//	}




	@Override
	public void close()   {
		try {
			client.disconnect();
			client.close();
			ColorsOut.outappl("MqttConnection | client disconnected and closed ", ColorsOut.CYAN);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttConnection  | close Error:" + e.getMessage());
 		}
	}

 

 
	
}
