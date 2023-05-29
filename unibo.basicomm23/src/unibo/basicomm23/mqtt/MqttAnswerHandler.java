package unibo.basicomm23.mqtt;

import java.util.concurrent.BlockingQueue;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import unibo.basicomm23.interfaces.*;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.ColorsOut;

 
public class MqttAnswerHandler  implements IApplMsgHandlerMqtt{
//private static int n = 0; 
private  IApplMsgHandler handler ;
	private BlockingQueue<String> blockingQueue = null;

	public MqttAnswerHandler( String name, IApplMsgHandler handler, BlockingQueue<String> blockingQueue ) {
		//super( name );
		this.handler       = handler;
 		this.blockingQueue = blockingQueue;
 		//Colors.out(name+" | MqttAnswerHandler CREATED blockingQueue=" + blockingQueue, Colors.ANSI_PURPLE);		
	}

//	public MqttAnswerHandler( BlockingQueue<String> blockingQueue ) {
//		this("mqttAnswHandler"+ n++, blockingQueue);
//	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message)   {
 		ColorsOut.outappl(" | messageArrived:" + message + " on topic="+topic, ColorsOut.YELLOW );
// 		Colors.out(name + " | msgId=" + 
// 				message.getId() + "  Qos="+ message.getQos() + " isDuplicate=" 
// 				+ message.isDuplicate() + " payload=" + message.getPayload().length, 
// 				Colors.ANSI_PURPLE );
 		if( message.getPayload().length == 1 ) {
 			elaborate("sorry", MqttConnection.getSupport() );
 			return;  //perch� RICEVO 0 ???
 		}
		try { //Perhaps we receive a structured message
			IApplMessage msgInput = new ApplMessage(message.toString());
			elaborate(msgInput, MqttConnection.getSupport() );
		}catch( Exception e) {
			ColorsOut.out(" ApplMsgHandler | messageArrived WARNING:"+ e.getMessage(), ColorsOut.ANSI_YELLOW );
 		}
	}
 		@Override
		public void elaborate(IApplMessage message, Interaction conn) {
			try {
				blockingQueue.put(message.toString());
				ColorsOut.outappl(" | elaborate IApplMessage:" + message + " blockingQueue=" + blockingQueue.size(), ColorsOut.YELLOW);
			} catch (Exception e) {
				ColorsOut.outerr(" | elaborate IApplMessage ERROR " + e.getMessage());
			}			
		}

		 
		public void elaborate(String message, Interaction conn) {
			ColorsOut.outappl(" | elaborate String: " + message, ColorsOut.ANSI_YELLOW);
			try {
				blockingQueue.put(message.toString());
			} catch (Exception e) {
				ColorsOut.outerr(" | elaborate ERROR " + e.getMessage());
			}			
			
		}
		@Override
		public void deliveryComplete(IMqttDeliveryToken token){
			ColorsOut.out(" ApplMsgHandler | deliveryComplete token=" + token.getMessageId(), ColorsOut.ANSI_YELLOW);
		}

		@Override
		public void connectionLost(Throwable cause) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}
/*
		@Override
		public void sendMsgToClient(String message, Interaction  conn) {
			if( handler != null ) handler.sendMsgToClient(message, conn);
			
		}

		@Override
		public void sendAnswerToClient(String message, Interaction  conn) {
			if( handler != null ) handler.sendAnswerToClient(message, conn);
			
		}	*/
}
