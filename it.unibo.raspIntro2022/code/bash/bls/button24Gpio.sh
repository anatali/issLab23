#!/bin/bash
# WARNING: document class text UNIX
# --------------------------------------------------------------------------
# button24Gpio.sh
# Key-point: we can manage a device connected on a GPIO pin by using the GPIO shell library. 
# The pin 24 (button) is physical 18 and Wpi 5.
# The pin 25 (led)    is physical 22 and Wpi 6.
# 	sudo bash button24Gpio.sh
# --------------------------------------------------------------------------
But=5
Led=6
LedBCM=25

gpio mode $But in
gpio -g mode $LedBCM out
gpio readall 
echo "start"

while true
do 
	gpio read $But > vgpio24.txt
 	V1=`cat vgpio24.txt`
	if [ $V1 == "1" ] ; then echo "on" ; fi
	#echo $V1
	gpio write $Led $V1  #command the led
	sleep 0.1 
done