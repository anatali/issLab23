package it.unibo.kactor

import unibo.basicomm23.mqtt.MqttConnection



class MqttUtils(val owner: String ) {
	protected var eventId: String? = "mqtt"
	protected var eventMsg: String? = ""
	protected var mqtttraceOn = false
	//protected lateinit var client: MqttClient
	protected lateinit var workActor: ActorBasic
	protected var isConnected = false

	protected val RETAIN = false;

	protected val mqtt = MqttConnection(owner)

	fun trace(msg: String) {
		if (mqtttraceOn) println("$msg")
	}

	fun connect(clientid: String, brokerAddr: String): Boolean {
		return mqtt.connect(clientid, brokerAddr)
	}

	fun connectDone(): Boolean {
		return isConnected
	}

	fun disconnect() {
		try {
			mqtt.disconnect()
		} catch (e: Exception) {
			println("       %%% MqttUtils $owner | disconnect ERROR ${e}")
		}
	}


	fun subscribe(actor: ActorBasic, topic: String) {
		//println("	%%% MqttUtils ${actor.name} subscribe to topic=$topic client=$client "  )
		try {
			this.workActor = actor //actor implements MqttCallback
			//client.setCallback(this)
			mqtt.setCallback(actor)
			mqtt.subscribe(topic)
		} catch (e: Exception) {
			println("       %%% MqttUtils $owner | ${actor.name} subscribe topic=$topic ERROR=$e ")
		}
	}

	fun publish(topic: String, msg: String?, qos: Int = 2, retain: Boolean = false) {
		mqtt.publish(topic, msg, qos, retain)
	}
}



