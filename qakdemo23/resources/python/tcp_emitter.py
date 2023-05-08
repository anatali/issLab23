##############################################################
# tcp_emitter.py
##############################################################
import socket
import time

port = 8055
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

alarmFire     = "msg(alarm,event,python,none,alarm(firetcp),1)"
alarmTsunami  = "msg(alarm,event,python,none,alarm(tsunamitcp),2)"

def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED " , server_address)

def emit( message ) :
    print("emit ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    emit( alarmFire ) 
    time.sleep(1)
    emit( alarmTsunami )


def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(port)
work()
##terminate()  
