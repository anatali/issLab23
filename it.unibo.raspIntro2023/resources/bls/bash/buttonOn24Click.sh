# -------------------------------------------------------------
# buttonOn24Click.sh
# Key-point: we can manage a device connected on a GPIO pin by
# reading/writing some (virtual) file associated with that pin.
#	sudo bash buttonOn24Click.sh
# -------------------------------------------------------------

echo Unexporting.
echo 24 > /sys/class/gpio/unexport #
echo 24 > /sys/class/gpio/export #
cd /sys/class/gpio/gpio24 #
echo Setting direction to in.
echo in > direction #
echo Reading pin.
cat value 
#schmod +x ledOnOff.sh #
