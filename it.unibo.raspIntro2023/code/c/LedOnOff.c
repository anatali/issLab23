#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>


//The pin 25 is physical 22 and Wpi 6.
#define LED 6

using namespace std;
/*
g++  LedOnOff.c -l wiringPi -o  LedOnOff
*/
void setup() {
	//cout << "setUp " << endl;
	wiringPiSetup();
	pinMode(LED, OUTPUT);
	delay(30);
}


void blinkTheLed( int DT ) {
	while(1) {
	 	digitalWrite(LED, HIGH);
	 	delay(DT);
	 	digitalWrite(LED, LOW);
 	 	delay(DT);
 	}
}

int main(void) {
	setup();
  	blinkTheLed( 50 );
 	return 0;
}
