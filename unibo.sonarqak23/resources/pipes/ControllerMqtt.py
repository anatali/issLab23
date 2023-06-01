# File: ControllerMqtt.py
import time
import paho.mqtt.client as paho
import sys

n         = 1
brokerAddr="mqtt.eclipseprojects.io"  #"broker.hivemq.com"   ##"mqtt.eclipseprojects.io"
msg       = "msg(sonardata,event,sonar,none,distance(D),N)"
    
client    = paho.Client("controller")    
client.connect(brokerAddr, 1883, 60) 

## print ('Mqtt client connected ')
##time.sleep(2)

for line in sys.stdin:
	## print("ControllerMqtt RECEIVES:", line)
	try:
		vf = float(line)
		v  = int( vf )
		if v <= 10 :
			n = n + 1
			client.publish("unibo/sonar/events", msg.replace("D",str(v)).replace("N", str(n)))
			print ( 'on', flush=True ) 
		else:	
			print ( 'off', flush=True ) 
	except:
		print("ControllerMqtt | An exception occurred")	


### USAGE
### python sonar.py | python ControllerMqtt.py | python LedDevice.py


