# -------------------------------------------------------------
# buttonPython24Callbacks.py
# Key-point: manage in python a Button connected on the GPIO pin 24
# using the library RPi.GPIO and  event_detected.
'''
RPi.GPIO runs a second thread for callback functions. 
This means that callback functions can be run at the same time as your
main program, in immediate response to an edge.
'''
#	sudo python buttonPython24Callbacks.py
# -------------------------------------------------------------
import RPi.GPIO as GPIO 
import time

GPIO.setmode(GPIO.BCM)  #BCM or BOARD
GPIO.setup(24, GPIO.IN, pull_up_down=GPIO.PUD_DOWN )
'''
----------------------------------
callback
----------------------------------
'''
def buttonCallback(channel):
	print('---------------------------------------------------------------')
	print('This is a edge event callback detected on channel %s'%channel)
	print('This runs in a different thread to the main program')
	print('---------------------------------------------------------------')

'''
----------------------------------
CONFIGURATION
----------------------------------
The callbacks can be called more than once for each button press. 
This is as a result of what is known as 'switch bounce'.
To debounce using software, add the bouncetime= parameter (milliseconds) to the function 
where you specify a callback function. 
'''
GPIO.add_event_detect(24, GPIO.RISING, callback=buttonCallback, bouncetime=200)  
#GPIO.remove_event_detect(24)  

'''
----------------------------------
main activity
----------------------------------
'''
while True:
	print('Button doing somwthing for 2 sec'   )
	time.sleep(2)

 
     