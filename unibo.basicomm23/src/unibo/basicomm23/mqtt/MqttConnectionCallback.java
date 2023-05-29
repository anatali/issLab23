package unibo.basicomm23.mqtt;

import java.util.concurrent.BlockingQueue;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import unibo.basicomm23.utils.ColorsOut;
 
 


public class MqttConnectionCallback implements MqttCallback{
 	private BlockingQueue<String> blockingQueue = null;

 	public MqttConnectionCallback( BlockingQueue<String> blockingQueue ) {
 		this.blockingQueue = blockingQueue;
	}

		public MqttConnectionCallback(String clientName, BlockingQueue<String> blockingQueue ) {
			//this.clientName    = clientName;
			this.blockingQueue = blockingQueue;
		}
		@Override
		public void connectionLost(Throwable cause) {
			ColorsOut.outerr("MqttSupportCallback | connectionLost cause="+cause);
	 	}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			ColorsOut.outappl("MqttSupportCallback | messageArrived:" + message, ColorsOut.ANSI_PURPLE );
			if( blockingQueue != null ) blockingQueue.put( message.toString() );	
//			else if( handler != null ) handler.elaborate(message.toString(), null);  //TODO 
//			Colors.outappl("MqttSupportCallback | messageArrived:"+message + " for " + clientName 
//					+ " topic=" + topic + " bqsize="+blockingQueue.size(), Colors.ANSI_PURPLE);
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			try {
//				Colors.outappl("MqttSupportCallback | deliveryComplete token=" 
//			       + token.getMessage() + " client=" + token.getClient().getClientId() , Colors.ANSI_YELLOW);
			} catch (Exception e) {
				ColorsOut.outerr("MqttSupportCallback | deliveryComplete Error:"+e.getMessage());		
			}
	 	}
		
}
