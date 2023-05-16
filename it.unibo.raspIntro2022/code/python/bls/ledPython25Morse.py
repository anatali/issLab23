# -------------------------------------------------------------
# ledPython25Morse.py
# Key-point: 
#	sudo python ledPython25Morse.py
#	oppure
#   chmod ugo+x ledPython25Morse.py 
#   sudo ./ledPython25Morse.py SOS
# -------------------------------------------------------------
import sys
import RPi.GPIO as GPIO 
import time

'''
----------------------------------
CONFIGURATION
----------------------------------
'''
GPIO.setmode(GPIO.BCM)
GPIO.setup(25,GPIO.OUT)

#Pin del led
ledPin=25

#TurnOn duration (in secondi) for Point and Line 
durPoint = 0.5
durLine  = 1.5

MORSE = {"'": '.----.','(': '-.--.-',')': '-.--.-',',': '--..--','-': '-....-','.': '.-.-.-','/': '-..-.','0': '-----','1': '.----','2': '..---','3': '...--','4': '....-','5': '.....','6': '-....','7': '--...','8': '---..','9': '----.',':': '---...',';': '-.-.-.','?': '..--..','A': '.-','B': '-...','C': '-.-.','D': '-..','E': '.','F': '..-.','G': '--.','H': '....','I': '..','J': '.---','K': '-.-','L': '.-..','M': '--','N': '-.','O': '---','P': '.--.','Q': '--.-','R': '.-.','S': '...','T': '-','U': '..-','V': '...-','W': '.--','X': '-..-','Y': '-.--','Z': '--..','_': '..--.-'}

'''
----------------------------------
OPERATIONS
----------------------------------
'''
#TurnOn/of the led for point
def Point():
    GPIO.output(ledPin, 1)
    time.sleep(durPoint)
    GPIO.output(ledPin,0)
    time.sleep(durPoint)

#TurnOn/of the led for Line
def Line():
    GPIO.output(ledPin, 1)
    time.sleep(durLine)
    GPIO.output(ledPin,0)
    time.sleep(durPoint)
 
def morseout(word):
	#For each letter in word
	print( word )	
	for letter in word:
		#For each MORSE sign
		for sign in MORSE[letter.upper()]:
			if sign == '-':
				Line();
			else:
				Point();
	
def main(argv):
 	#For each word in argv
	for i in range(1,3): 
		print('repeat '+str(i)  )
		for word in argv:
			morseout(word)					
		time.sleep(2)
	GPIO.cleanup()

'''
----------------------------------
main activity
----------------------------------
'''
main(sys.argv[1:])		#discard the first argument (program name)
 
#SOS = ... --- ...
 