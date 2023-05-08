##############################
# coapdemoCaller..py
# Tanganelli
##############################

from coapthon.client.helperclient import HelperClient

 
host = "localhost"
port = 8037
path ="ctxcoapdemo/actorcoap"
fullpath = "coap://localhost:8037/ctxcoapdemo/actorcoap"
client = HelperClient(server=(host, port))
print( "client" + str(client)  )
response = client.get(fullpath)
#print( "response" + response  )
#client.stop()       