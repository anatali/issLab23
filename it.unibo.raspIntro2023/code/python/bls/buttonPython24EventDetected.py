# -------------------------------------------------------------
# buttonPython24EventDetected.py
# Key-point: manage in python a Button connected on the GPIO pin 24
# using the library RPi.GPIO and  event_detected.
'''
The event_detected() function is designed to be used in a loop with other things, 
but unlike polling it is not going to miss the change
in state of an input while the CPU is busy working on other things.
'''
#	sudo python buttonPython24EventDetected.py
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
GPIO.add_event_detect(24, GPIO.RISING) 			# add rising edge detection on pin 24
#GPIO.remove_event_detect(24) 
'''
----------------------------------
main activity
----------------------------------
'''
while True:
 	print('Button doing somwthing for 2 sec ... '   )
	time.sleep(2)
	if GPIO.event_detected(24):
		print('WARNING: Button has been pressed '   )
  
 
     