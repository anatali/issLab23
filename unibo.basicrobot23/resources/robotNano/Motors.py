## Adattato da
## https://www.electronicshub.org/raspberry-pi-l298n-interface-tutorial-control-dc-motor-l298n-raspberry-pi/
import RPi.GPIO as GPIO
import sys
from time import sleep

GPIO.setmode(GPIO.BCM)
#MOTORE DESTRO
in1 = 3        # 9 wpi
in2 = 2        # 8 wpi
#MOTORE SINISTRO
in3 = 10       # 12 wpi
in4 = 9        # 13 wpi
## en  = 18

## GPIO.setup(en,GPIO.OUT)

GPIO.setmode(GPIO.BCM)
GPIO.setup(in1,GPIO.OUT)
GPIO.setup(in2,GPIO.OUT)
GPIO.output(in1,GPIO.LOW)
GPIO.output(in2,GPIO.LOW)

GPIO.setup(in3,GPIO.OUT)
GPIO.setup(in4,GPIO.OUT)
GPIO.output(in3,GPIO.LOW)
GPIO.output(in4,GPIO.LOW)

## p=GPIO.PWM(en,100)
## p.start(0)
print("\n")
print("The default speed & direction of motor is LOW & Forward.....")
print("w-forward s-backward l-low m-medium h-high e-exit")
print("\n")

def ww():
    print("forward | destro e sinistro avanti")
    GPIO.output(in1,GPIO.LOW)
    GPIO.output(in2,GPIO.HIGH)
    GPIO.output(in3,GPIO.LOW)
    GPIO.output(in4,GPIO.HIGH)

def ss():
    print("backward | destro e sinistro indietro ")
    GPIO.output(in1,GPIO.HIGH)
    GPIO.output(in2,GPIO.LOW)
    GPIO.output(in3,GPIO.HIGH)
    GPIO.output(in4,GPIO.LOW)

def w(t):
    print("forward | destro e sinistro avanti")
    GPIO.output(in1,GPIO.LOW)
    GPIO.output(in2,GPIO.HIGH)
    GPIO.output(in3,GPIO.LOW)
    GPIO.output(in4,GPIO.HIGH)
    sleep(t)
    halt()

def s(t):
    print("backward | destro e sinistro indietro ")
    GPIO.output(in1,GPIO.HIGH)
    GPIO.output(in2,GPIO.LOW)
    GPIO.output(in3,GPIO.HIGH)
    GPIO.output(in4,GPIO.LOW)
    sleep(t)
    halt()

def l(t):
    print("l | destro avanti sinistro indietro ")
    GPIO.output(in1,GPIO.LOW)
    GPIO.output(in2,GPIO.HIGH)
    GPIO.output(in3,GPIO.HIGH)
    GPIO.output(in4,GPIO.LOW)
    sleep(t)
    halt()

def r(t):
    print("r | destro indietro sinistro avanti ")
    GPIO.output(in1,GPIO.HIGH)
    GPIO.output(in2,GPIO.LOW)
    GPIO.output(in3,GPIO.LOW)
    GPIO.output(in4,GPIO.HIGH)
    sleep(t)
    halt()

def halt():
    print("halt")
    GPIO.output(in1,GPIO.LOW)
    GPIO.output(in2,GPIO.LOW)
    GPIO.output(in3,GPIO.LOW)
    GPIO.output(in4,GPIO.LOW)


l(0.3)
r(0.3)

while(1):

    x=sys.stdin.read(1)   #raw_input() in python2

    print('Motors.py input', x)
    if x=='w':
        #### w(0.5)
        ww()
        x='z'

    elif x=='s':
        #### s(0.5)
        ss()
        x='z'

    elif x=='l':
        l(0.3)
        x='z'
    elif x=='a':
        l(0.3)
        x='z'
    elif x=='r':
        r(0.3)
        x='z'
    elif x=='d':
        r(0.3)
        x='z'

    elif x=='lold':
        print("left")
        GPIO.output(in1,GPIO.LOW)
        GPIO.output(in2,GPIO.HIGH)
        GPIO.output(in3,GPIO.LOW)
        GPIO.output(in4,GPIO.HIGH)
        sleep(0.5)
        halt()
        x='z'


    elif x=='rold':
        print("right")
        GPIO.output(in1,GPIO.LOW)
        GPIO.output(in2,GPIO.HIGH)
        GPIO.output(in3,GPIO.LOW)
        GPIO.output(in4,GPIO.HIGH)
        sleep(0.5)
        halt()
        x='z'

    elif x=='h':
        halt()
        x='z'

    elif x=='e':
        GPIO.cleanup()
        break

    #else:
        #print("<<<  wrong data  >>>")
        #print("please enter the defined data to continue.....")