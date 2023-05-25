# -------------------------------------------------------------
# led25Gpio.sh
# Key-point: we can manage a device connected on a GPIO pin by
# using the GPIO shell library. 
# The pin 25 is physical 22 and Wpi 6.
#	sudo bash led25Gpio.sh
# -------------------------------------------------------------
gpio readall #
echo Setting direction to out
gpio mode 6 out #
echo Write 1
gpio write 6 1 #
sleep 1 #
echo Write 0
gpio write 6 0 #