#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>

#include <pthread.h>


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
g++  LedBlinkSonar.c -l wiringPi -lpthread -o  LedBlinkSonar
 
*/

//Dato globale
	int cm ;
	int doblink;


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

//TODO: attivare un processo: vedi blinkTheLedInThread
void blinkTheLed( int cm ) {
  if( cm < DLIMIT ){
	  for (int i=1; i<=5; i++){
	    digitalWrite (LED, HIGH) ;  // On
	    delay (250 ) ;              // mS
	    digitalWrite (LED, LOW) ;   // Off
	    delay (250 ) ;
	  } 
  }
}

void *blink(void *arg){
  int result = 1;
  int dt = (int)arg;
  printf ("Thread %d \n", dt );
  while ( doblink )
  {
    digitalWrite (LED, HIGH) ;  // On
    delay (dt ) ;              // mS
    digitalWrite (LED, LOW) ;   // Off
    delay (dt ) ;
  } 
  printf ("Thread EXIT   \n"  );
  return NULL;
  //pthread_exit ((void*)result);
} 

// standard Posix

void blinkTheLedInThread( int cm ){
	pthread_t th1;
	int dt = 250;
	int ret;
	void *retval;
	//printf("blinkTheLedInThread cm= %d ", cm);
	if( cm > DLIMIT ){  
 		digitalWrite(LED, LOW);
 // int pthread_join (pthread_t tid, void **status);
 // altrimenti rimangono allocate le aree di memoria assegnate al thread
 		if( doblink == 1 ){
 			doblink = 0;
 			//delay ( 1000 ) ;  
	  		ret = pthread_join(th1,  &retval);
	  		printf("ret = %d \n", ret);
	  		//if (ret != 0) printf ("join fallito  \n " ); 
	        if (retval == PTHREAD_CANCELED)
	            printf("The thread was canceled - ");
	        else
	            printf("End of thread. Returned value= %d \n", (int*)retval);
 	 		 
 		}
	}else{ 
		if( doblink == 0 ){
	 		doblink=1;
// int pthread_create (pthread_t *tid, const p_thread_attr*attr, (void *(*func)(void *), void *arg);
	 	 int rc = pthread_create(&th1, NULL, blink, (void *)dt );
         if (rc) {
            printf("ERORR; return code from pthread_create() is %d\n", rc);
            exit(EXIT_FAILURE);
        }
 		}
   	}
}



int main(void) {
	setup();
	while(1) {
 		cm = getCM();
 		
 		//blinkTheLed( cm ); //blocca il flusso
 		//cm potrebbe essere > 3000
 		if( cm < 100 ){
 			blinkTheLedInThread( cm );
 			cout <<  cm   << endl ;  //flush after ending a new line
			delay(30);
		}
	}
 	return 0;
}
