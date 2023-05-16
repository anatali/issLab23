#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/resource.h>
#include <time.h>
#include <wait.h>
#include <wiringPi.h>

#define TRIGPIN 0
#define ECHOPIN 2

#define SERIES 50  // 100 how many measurements 
#define SERIES1 5  // discard this count of the smallest and largest measurements 
#define SPEEDDIV 58

/*
cc OtherSonar.c -o OtherSonar -lwiringPi
*/

int compare_float (const void *a, const void *b);
struct tm *loctime;
time_t czas;
char date[50];
void Delay();
int impuls;
struct timespec tim, tim2,tim3;
int pid;

void int_impuls(void) {
      if (digitalRead (ECHOPIN) == 1)
       {
clock_gettime(CLOCK_REALTIME, &tim);
       }
      else 
       {
clock_gettime(CLOCK_REALTIME, &tim2);
        impuls=0;
       }
}

int debug = 0;
void catch_USR(int signal_num)
{
fflush(0);
if(signal_num==SIGUSR1)
debug=1;
if(signal_num==SIGUSR2)
debug=0;
}


int main (void)
{
  float usec[SERIES];
  float usectmp;
  int i,j,k;
  int max;
  int sum1, sum2;
  float low, middle, high; 
  int size = 100; //10000
  int tab_rob[size];
  pid=getpid();
  printf("Start %d\n", pid);fflush(0);
  setpriority(0,0,-20);
  signal(SIGUSR1, catch_USR);
  signal(SIGUSR2, catch_USR);

  if (wiringPiSetup () == -1)
  exit (1) ;

    pinMode (ECHOPIN, INPUT) ;
    wiringPiISR (ECHOPIN, INT_EDGE_BOTH, &int_impuls);
    pinMode (TRIGPIN, OUTPUT) ;


  for(;;)
 {
  for(i=0;i<SERIES;i++)
   {
    digitalWrite (TRIGPIN,0) ; //LOW
    Delay(2);
    digitalWrite (TRIGPIN,1) ; //HIGH
    Delay(10);
    digitalWrite (TRIGPIN,0) ; //LOW
    impuls=1;
    for(k=1;k<9000;k++)
     { if (impuls==0) break;
       Delay(1);
     } 
    if (impuls==1) 
      {
       printf("Lost pulse\n"); fflush(0);
       tim2.tv_nsec=tim.tv_nsec+1;
      }

    if(tim2.tv_nsec < tim.tv_nsec) 
     tim2.tv_nsec= tim2.tv_nsec+1000000000;

    usec[i]=(tim2.tv_nsec -tim.tv_nsec)/1000;
    usectmp=usec[i];
if(debug)
printf("%4.1f microsec\n",usectmp);


    Delay(90000);
   }

//calculate the average, reject the measurements with error 
qsort (usec, SERIES, sizeof (float), compare_float);
  usectmp=0;
if (debug)
  for (i=0;i<SERIES;i++)
   { printf(" %d: %3.0f \n", i ,usec[i]); fflush(0);}


  for(i=0;i<size; i++) tab_rob[i]=0;
   
  for (i=SERIES1;i<SERIES-SERIES1;i++)
   {
     j=usec[i]/10;

// If no echo is detected, the index will go outside the array 
     if(j<size) tab_rob[j]++;
   }

// Find the maximum 
  max=0 ;
  for (i=0;i<size;i++)
   if (tab_rob[i]>max)
    max=tab_rob[i]; 


// Reject measurements less than 50% of the max
  for (i=0;i<size;i++)
   if (tab_rob[i]<max*0.5)
    tab_rob[i]=0; 
if(debug) for(i=0;i<size;i++) if (tab_rob[i]>0) printf("%d %d\n", i, tab_rob[i]);
// Calculate the average
  sum1=0; sum2=0;
  for (i=0;i<size;i++)
    if (tab_rob[i] >0)
      {
       sum1=sum1+tab_rob[i];
       sum2=sum2+i*tab_rob[i];
      }   
  usectmp=(float)sum2/sum1;   
  time (&czas);
  loctime = localtime (&czas);
  strftime (date, 50, "%Y-%m-%d %H:%M:%S", loctime);
  printf("%s %4.1f cm\n",date, usectmp/SPEEDDIV*10);
 }
}

int compare_float (const void *a, const void *b)
{
  const float *da = (const float *) a;
  const float *db = (const float *) b;

  return (*da > *db) - (*da < *db);
}

void Delay(int microsec)
{

 tim3.tv_sec = 0;
 tim3.tv_nsec = microsec * 1000;
     while(nanosleep(&tim3,&tim3)==-1)
          continue;

}