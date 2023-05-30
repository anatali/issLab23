#File: LedDevice.py
import RPi.GPIO as GPIO
import sys

GPIO.setmode(GPIO.BCM)
GPIO.setup(25,GPIO.OUT)
 
for line in sys.stdin:
   print("LedDevice receives:", line)
   try:
	   if 'on' in line :
	      ## print('LedDevice on')
	      GPIO.output(25,GPIO.HIGH)
	   else:
	      ## print('LedDevice off')
	      GPIO.output(25,GPIO.LOW)
   except:
  		print("LedDevice | An exception occurred")	