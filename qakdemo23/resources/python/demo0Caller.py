##############################################################
# demo0Caller.py
##############################################################
import socket
import time

port = 8095
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

msg1  = "msg(msg1,dispatch,python,demo,msg1(1),1)"
msg2  = "msg(msg2,dispatch,python,demo,msg2(1),2)"
alarm = "msg(alarm,event,python,none,alarm(fire),3)"

def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED " , server_address)

def forward( message ) :
    print("forward ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def emit( event ) :
    print("emit ", event)
    msg = event + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    forward( msg1 ) 
    time.sleep(1)
    emit( alarm )
    forward( msg2 ) 


def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(port)
work()
##terminate()  
