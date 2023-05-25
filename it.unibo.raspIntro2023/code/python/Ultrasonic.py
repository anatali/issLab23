import time
###  sudo apt update
###  sudo apt upgrade
###  sudo apt install python3-rpi.gpio   

import RPi.GPIO as GPIO			
class Ultrasonic:
    def __init__(self):
        GPIO.setwarnings(False)
        self.trigger_pin =  17	#27	#0
        self.echo_pin    = 	27	#22	#2
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.trigger_pin,GPIO.OUT)
        GPIO.setup(self.echo_pin,GPIO.IN)
    def send_trigger_pulse(self):
        GPIO.output(self.trigger_pin,True)
        time.sleep(0.00015)
        GPIO.output(self.trigger_pin,False)

    def wait_for_echo(self,value,timeout):
        count = timeout
        while GPIO.input(self.echo_pin) != value and count>0:
            count = count-1
     
    def get_distance(self):
        distance_cm=[0,0,0,0,0]
        for i in range(3):
            self.send_trigger_pulse()
            self.wait_for_echo(True,10000)
            start = time.time()
            self.wait_for_echo(False,10000)
            finish = time.time()
            pulse_len = finish-start
            distance_cm[i] = pulse_len/0.000058
        distance_cm=sorted(distance_cm)
        return int(distance_cm[2])
                 
    def run(self):
    	print ('Ultrasonic is starting ... ')
    	L = self.get_distance()
    	print ('Ultrasonic L=' + str(L) )
          
            
        
ultrasonic=Ultrasonic()              
# Main program logic follows:
if __name__ == '__main__':
    ### print ('Program is starting ... ')
    try:
        ultrasonic.run()
    except KeyboardInterrupt:  # When 'Ctrl+C' is pressed, the child program destroy() will be  executed.
        print ('Program BYE ... ')
