# -------------------------------------------------------------
# buttonPython24.py
# Key-point: manage in python a Button connected on the GPIO pin 24
#  using polling.
#	sudo python buttonLedPython.py
# -------------------------------------------------------------
import RPi.GPIO as GPIO 
import time

'''
----------------------------------
CONFIGURATION
----------------------------------
'''
GPIO.setmode(GPIO.BCM)  #BCM or BOARD
GPIO.setup(24, GPIO.IN, pull_up_down=GPIO.PUD_DOWN )

GPIO.setmode(GPIO.BCM)
GPIO.setup(25,GPIO.OUT)

'''
----------------------------------
main activity
----------------------------------
'''
while True:
    if GPIO.input(24):
    	print('Button input HIGH')
    	GPIO.output(25,GPIO.HIGH)
    else:
    	print('Button input LOW')
    	GPIO.output(25,GPIO.LOW)
    time.sleep(0.1)	#wait 0.1sec

#while GPIO.input(24) == GPIO.LOW:
#     time.sleep(0.01) #wait 10msec
     