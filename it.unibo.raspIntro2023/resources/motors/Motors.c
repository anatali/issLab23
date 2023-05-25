#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>

// #define TRUE 1   //alredy done in wiringPi
#define inp1m1 8
#define inp2m1 9

#define inp1m2 12
#define inp2m2 13


using namespace std;

/*
g++  Motors.c -l wiringPi -o  Motors
 */

void w(){
	digitalWrite(inp1m1, HIGH);
	digitalWrite(inp2m1, LOW);
	digitalWrite(inp1m2, HIGH);
	digitalWrite(inp2m2, LOW);
	cout << "w ENDS" << endl;
}
void s(){
	digitalWrite(inp1m1, LOW);
	digitalWrite(inp2m1, HIGH);
	digitalWrite(inp1m2, LOW);
	digitalWrite(inp2m2, HIGH);
	cout << "s ENDS" << endl;
}

void h(){
	digitalWrite(inp1m1, LOW);
	digitalWrite(inp2m1, LOW);
	digitalWrite(inp1m2, LOW);
	digitalWrite(inp2m2, LOW);
}

void setup() {
	cout << "setUp STARTS" << endl;
	wiringPiSetup();
	pinMode(inp1m1, OUTPUT);
	pinMode(inp2m1, OUTPUT);
	pinMode(inp1m2, OUTPUT);
	pinMode(inp2m2, OUTPUT);
	h();
 	delay(30);
	cout << "setUp ENDS" << endl;
}

int main(void) {
 	setup();
 	w();
 	delay(1000);
 	h();
 	s();
 	delay(1000);
 	h();
 	cout << "main BYE" << endl;
 	return 0;
}
