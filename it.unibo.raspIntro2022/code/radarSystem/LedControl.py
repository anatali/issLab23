#File: LedControl.py
import RPi.GPIO as GPIO
import sys

GPIO.setmode(GPIO.BCM)
GPIO.setup(25,GPIO.OUT)

for line in sys.stdin:
   print(line)
   try:
	   v = float(line)
	   if v <= 10 :
	      GPIO.output(25,GPIO.HIGH)
	   else:
	      GPIO.output(25,GPIO.LOW)
   except:
  		print("An exception occurred")	