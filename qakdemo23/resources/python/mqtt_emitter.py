##############################################################
# mqtt_emitter.py
# sendDispatch       : sends a command in output
# read               : acquires data from input
##############################################################
import time
import paho.mqtt.client as paho
 
brokerAddr     = "broker.hivemq.com"
topic          = "unibo/qak/events"
 
alarmFire     = "msg(alarm,event,pythonan,none,alarm(firemqtt),1)"
alarmTsunami  = "msg(alarm,event,pythonan,none,alarm(tsunamimqtt),2)"

def emit( message ) :
    print("emit ", message)
    msg = message + "\n"
    #byt=msg.encode()    #required in Python3
    client.publish(topic, msg)

 
def work() :
    emit( alarmFire ) 
    time.sleep(1)
    emit( alarmTsunami ) 

def terminate() :
    client.disconnect()
    print("BYE")
#################################################   
client= paho.Client("sender")      
client.connect(brokerAddr)              #connect
print("connected to broker ", brokerAddr)
work()
terminate()