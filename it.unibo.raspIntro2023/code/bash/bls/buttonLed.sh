#!/bin/bash
# --------------------------------------------------------------------------
# buttonLed.sh
# Key-point: the BLS system implemented by
# reading/writing some (virtual) file associated with the device pins.
# This is more 'advanced' version than led25OnOff.sh or buttonOn24Click.sh
# 	sudo bash buttonLed.sh
# --------------------------------------------------------------------------

led=25
but=24

if [ -d /sys/class/gpio/gpio25 ] 
then 
	echo "led gpio${led} exist"
	echo out > /sys/class/gpio/gpio${led}/direction  
else 
	echo "creating led gpio${led}"
	echo ${led} > /sys/class/gpio/export  
	echo out > /sys/class/gpio/gpio${led}/direction  
	
fi

if [ -d /sys/class/gpio/gpio24 ] 
then 
	echo "button gpio${but} exist"
	echo in > /sys/class/gpio/gpio${but}/direction
else 
	echo "creating button gpio${but}"
	echo ${but} > /sys/class/gpio/export  
	echo in > /sys/class/gpio/gpio${but}/direction  
fi

while true
do 
	b=`cat /sys/class/gpio/gpio${but}/value`
	echo $b > /sys/class/gpio/gpio25/value
	sleep 0.1 
done