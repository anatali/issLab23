#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>

#define TRIG 0
#define ECHO 2

//The pin 25 is physical 22 and Wpi 6.
#define LED 6

#define CLOSE 18
#define MEDIUM 21
#define FAR 60
#define DLIMIT 12

#define POS_LEFT 0.055
#define POS_RIGHT 0.24
#define POS_FORWARD 0.14
using namespace std;
/*
g++  LedSonar.c -l wiringPi -o  LedSonar
*/
void setup() {
	//cout << "setUp " << endl;
	wiringPiSetup();
	pinMode(TRIG, OUTPUT);
	pinMode(ECHO, INPUT);
	//TRIG pin must start LOW
	digitalWrite(TRIG, LOW);
	//
	pinMode(LED, OUTPUT);
	delay(30);
}

int getCM() {
	//Send trig pulse
	digitalWrite(TRIG, HIGH);
	delayMicroseconds(20);
	digitalWrite(TRIG, LOW);

	//Wait for echo start
	while(digitalRead(ECHO) == LOW);

	//Wait for echo end
	long startTime = micros();
	while(digitalRead(ECHO) == HIGH);
	long travelTime = micros() - startTime;

	//Get distance in cm
	int distance = travelTime / 58;

	return distance;
}

void updateTheLed( int cm ) {
 	if( cm < DLIMIT ) digitalWrite(LED, HIGH);
 	else digitalWrite(LED, LOW);
}

int main(void) {
	int cm ;
	setup();
	while(1) {
 		cm = getCM();
 		
 		updateTheLed( cm );
 				
		cout <<  cm   << endl ;  //flush after ending a new line
		delay(30);
	}
 	return 0;
}
