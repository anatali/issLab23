#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>
#include <stdio.h>

// #define TRUE 1   //alredy done in wiringPi
#define in1 9    /* 3 BCM */
#define in2 8    /* 2 BCM */

#define in3 12   /* 10 BCM */
#define in4 13   /*  9 BCM */

using namespace std;

int rotLeftTime  = 310;
int rotRightTime = 310;
int rotStepTime  = 58;


/*
    g++  Motors.c -l wiringPi -o  Motors
 */
void h(){
	digitalWrite(in2, LOW);
	digitalWrite(in1, LOW);
	digitalWrite(in3, LOW);
	digitalWrite(in4, LOW);
}

void w(){
    //print("forward | destro e sinistro avanti")
	digitalWrite(in1, LOW);
	digitalWrite(in2, HIGH);
	digitalWrite(in3, LOW);
	digitalWrite(in4, HIGH);
}
void s(){
    //print("backward | destro e sinistro indietro ")
	digitalWrite(in1, HIGH);
	digitalWrite(in2, LOW);
	digitalWrite(in3, HIGH);
	digitalWrite(in4, LOW);
}

void r(){
    //print("r | destro indietro sinistro avanti ")
	digitalWrite(in1, HIGH);
	digitalWrite(in2, LOW);
	digitalWrite(in3, LOW);
	digitalWrite(in4, HIGH);
	delay(rotRightTime);
	h();
}
void l(){
	digitalWrite(in1, LOW);
	digitalWrite(in2, HIGH);
	digitalWrite(in3, HIGH);
	digitalWrite(in4, LOW);
	delay(rotLeftTime);
	h();
}

//TODO
void aa(){
}
void dd(){
}
void z(){
	dd();
	delay(rotStepTime);
	h();
}
void x(){
	aa();
	delay(rotStepTime);
	h();
}

void configureRotationTime(){
}


void setup() {
	cout << "motorsC setUp STARTS" << endl;
	wiringPiSetup();
	pinMode(in2, OUTPUT);
	pinMode(in1, OUTPUT);
	pinMode(in3, OUTPUT);
	pinMode(in4, OUTPUT);
	h();
 	delay(30);
	cout << "motorsC setUp ENDS" << endl;
}

void remoteCmdExecutor(){
int input = 'h';
        input = getchar( );
        if( input != 10 ){
	        cout << input << endl;        
	        switch( input ){
	          case 99  : configureRotationTime(); break;  //c... | cl0.59 or cr0.59  or cx0.005 or cz0.005
	          case 119 : w(); break;  //w		 
	          case 115 : s(); break;  //s		 
	          case 97  : l(); break;  //a
	          case 122 : z(); break;  //z		 
	          case 120 : x(); break;  //x		 
	          case 100 : r(); break;  //d
	          case 104 : h();  break;  //h
	          case 114 : r();  break;  //r		  
	          case 108 : l(); break;    //l		 
	          case 10  :  break;    //lf
	          //case 102 : break;  //f
	          default  : h();
	        }
        }
}

int main(void) {
  	setup();
	while( 1 ) 	remoteCmdExecutor();
	//return 0;
}
