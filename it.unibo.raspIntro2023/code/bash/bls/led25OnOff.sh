# -------------------------------------------------------------
# led25OnOff.sh
# Key-point: we can manage a device connected on a GPIO pin by
# reading/writing some (virtual) file associated with that pin.
# 	sudo bash led25OnOff.sh
# -------------------------------------------------------------

echo Unexporting.
echo 25 > /sys/class/gpio/unexport #
echo 25 > /sys/class/gpio/export #
cd /sys/class/gpio/gpio25 #

echo Setting direction to out.
echo out > direction #
echo Setting pin high.
echo 1 > value #
sleep 1 #
echo Setting pin low
echo 0 > value #
sleep 1 #
echo Setting pin high.
echo 1 > value #
sleep 1 #
echo Setting pin low
echo 0 > value #
#schmod +x ledOnOff.sh #
