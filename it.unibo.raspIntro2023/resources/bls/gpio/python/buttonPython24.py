# -------------------------------------------------------------
# buttonPython24.py
# Key-point: manage in python a Button connected on the GPIO pin 24
#  using polling.
#	sudo python buttonPython24.py
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


'''
----------------------------------
main activity
----------------------------------
'''
while True:
    if GPIO.input(24):
    	print('Button input HIGH')
    else:
    	print('Button input LOW')
    time.sleep(0.1)	#wait 0.1sec

#while GPIO.input(24) == GPIO.LOW:
#     time.sleep(0.01) #wait 10msec
     