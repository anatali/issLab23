# File: sonar.py
import RPi.GPIO as GPIO
import time
import paho.mqtt.client as paho

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
TRIG = 17
ECHO = 27

GPIO.setup(TRIG,GPIO.OUT)
GPIO.setup(ECHO,GPIO.IN)

GPIO.output(TRIG, False)   #TRIG parte LOW

n         = 1
brokerAddr="mqtt.eclipseprojects.io"  #"broker.hivemq.com"   ##"mqtt.eclipseprojects.io"
msg       = "msg(sonardata,event,sonar,none,distance(D),N)"
    
client    = paho.Client("sender")    
client.connect(brokerAddr, 1883, 60) 

print ('Mqtt client connected ')
##time.sleep(2)

while True:
   GPIO.output(TRIG, True)    #invia impulsoTRIG
   time.sleep(0.00001)
   GPIO.output(TRIG, False)

   #attendi che ECHO parta e memorizza tempo
   while GPIO.input(ECHO)==0:
      pulse_start = time.time()

   # register the last timestamp at which the receiver detects the signal.
   while GPIO.input(ECHO)==1:
      pulse_end = time.time()

   pulse_duration = pulse_end - pulse_start
   distance = pulse_duration * 17165   #distance = vt/2
   #distance = round(distance, 1)
   distance  = int( distance )
   #print ('Distance:',distance,'cm')
   print ( distance, flush=True ) 
   if( distance > 2 and distance < 90 ) :
      n = n + 1
      client.publish("unibo/sonar/events", msg.replace("D",str(distance)).replace("N", str(n)))
   
   time.sleep(0.25)


#GPIO.cleanup()