import time
import paho.mqtt.client as paho


duration = 60
d        = []
brokerAddr="mqtt.eclipseprojects.io"  #"mqtt.eclipseprojects.io"  #"broker.hivemq.com"    
#msg      = "msg(sonardata,event,sonar,none,distance(10),1)"


    
def on_message(client, userdata, message) :   #define callback
    evMsg   = str( message.payload.decode("utf-8")  )
    print("RECEIVED ", evMsg)
    msgitems = evMsg.split(",")
    vd = float( msgitems[4].split('(')[1].split(')')[0] )
    print("RECEIVED distance: ", vd)
    ## d.append( vd )
    ## print("RECEIVED d=", d, msgitems[4] )
    ##diagram()



    
client= paho.Client("receiver")      
client.on_message=on_message            # Bind function to callback

client.connect(brokerAddr)              #connect
print("connected to broker ", brokerAddr)
print("subscribing to unibo/sonar/events")
client.subscribe("unibo/sonar/events")      #subscribe


print("collecting values; please wait ..." )
client.loop_start()             #start loop to process received messages
time.sleep(duration)
client.disconnect()             #disconnect
print("bye")
client.loop_stop()              #stop loop        
