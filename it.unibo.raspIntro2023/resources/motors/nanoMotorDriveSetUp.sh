#!/bin/bash
# -----------------------------------------------------------------
# nanoMotorDrive.sh
# test for nano0
# Key-point: we can manage a GPIO pin  by using the GPIO library.
# On a PC, edit this file as UNIX
# -----------------------------------------------------------------

in1m1=2 #WPI 8 BCM 2  PHYSICAL 3
in2m1=3 #WPI 9 BCM 3  PHYSICAL 5
inwp1m1=8   
inwp2m1=9   


 
if [ -d /sys/class/gpio/gpio2 ]
then
 echo "in1m1 gpio${in1m1} exist"
 gpio export ${in1m1} out
else
 echo "creating in1m1 gpio${in1m1}"
 gpio export ${in1m1} out
fi

if [ -d /sys/class/gpio/gpio3 ]
then
 echo "in2m1 gpio${in2m1} exist"
 gpio export ${in2m1} out
else
 echo "creating in2m1  gpio${in2m1}"
 gpio export ${in2m1} out
fi


in1m2=10 #WPI 12  BCM 10 PHYSICAL 19
in2m2=9  #WPI 13  BCM 9  PHYSICAL 21

inwp1m2=12  #WPI 12  BCM 10 PHYSICAL 19
inwp2m2=13  #WPI 13  BCM 9  PHYSICAL 21

 
if [ -d /sys/class/gpio/gpio10 ]
then
 echo "in1m2 gpio${in1m2} exist"
 gpio export ${in1m2} out
else
 echo "creating in1m2 gpio${in1m2}"
 gpio export ${in1m2} out
fi

if [ -d /sys/class/gpio/gpio9 ]
then
 echo "in2m2 gpio${in2m2} exist"
 gpio export ${in2m2} out
else
 echo "creating in2m2  gpio${in2m2}"
 gpio export ${in2m2} out
fi

gpio readall

