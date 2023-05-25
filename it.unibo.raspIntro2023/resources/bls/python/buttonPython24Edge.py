# -------------------------------------------------------------
# buttonPython24Edge.py
# Key-point: manage in python a Button connected on the GPIO pin 24
# using the library RPi.GPIO and wait_for_edge.
#	sudo python buttonPython24Edge.py
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
	GPIO.wait_for_edge(24, GPIO.RISING) #GPIO.RISING, GPIO.FALLING or GPIO.BOTH
	print('Button edge GPIO.RISING ' + str(GPIO.RISING) )
  
 
     