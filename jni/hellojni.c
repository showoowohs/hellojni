#include <string.h>
#include <jni.h>
//add
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <limits.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <pthread.h>

#define EV_SYN	0x00
#define EV_KEY	0x01
#define EV_REL  0x02
#define EV_ABS	0x03


#define _FILE_OUT_ 1

/*
 * Synchronization events.
 */

#define SYN_REPORT		0
#define SYN_CONFIG		1
#define SYN_MT_REPORT	2

#define ABS_X	0x00
#define ABS_Y   0x01
#define ABS_X_2	0x02
#define ABS_Y_2 0x03

#define ABS_RY			0x04
#define ABS_RZ			0x05
#define ABS_THROTTLE		0x06
#define ABS_RUDDER		0x07
#define ABS_WHEEL		0x08
#define ABS_GAS			0x09
#define ABS_BRAKE		0x0a
#define ABS_HAT0X		0x10
#define ABS_HAT0Y		0x11
#define ABS_HAT1X		0x12
#define ABS_HAT1Y		0x13
#define ABS_HAT2X		0x14
#define ABS_HAT2Y		0x15
#define ABS_HAT3X		0x16
#define ABS_HAT3Y		0x17
#define ABS_PRESSURE		0x18
#define ABS_DISTANCE		0x19
#define ABS_TILT_X		0x1a
#define ABS_TILT_Y		0x1b
#define ABS_TOOL_WIDTH		0x1c
#define ABS_VOLUME		0x20
#define ABS_MISC		0x28

#define ABS_MT_TOUCH_MAJOR	0x30	/* Major axis of touching ellipse */
#define ABS_MT_TOUCH_MINOR	0x31	/* Minor axis (omit if circular) */
#define ABS_MT_WIDTH_MAJOR	0x32	/* Major axis of approaching ellipse */
#define ABS_MT_WIDTH_MINOR	0x33	/* Minor axis (omit if circular) */
#define ABS_MT_ORIENTATION	0x34	/* Ellipse orientation */
#define ABS_MT_POSITION_X	0x35	/* Center X ellipse position */
#define ABS_MT_POSITION_Y	0x36	/* Center Y ellipse position */
#define ABS_MT_TOOL_TYPE	0x37	/* Type of touching device */
#define ABS_MT_BLOB_ID		0x38	/* Group a set of packets as a blob */
#define ABS_MT_TRACKING_ID	0x39	/* Unique ID of initiated contact */
#define ABS_MT_PRESSURE		0x3a	/* Pressure on contact area */

/*
   struct timeval {
   long tv_sec;
   long tv_usec;
   };*/

typedef struct tagevent
{
	struct timeval time;
	unsigned short type;
	unsigned short code;
	int value;
}input_event;
typedef struct tag_pointevent
{
	struct timeval time;
	int x;
	int y;
	int point_count;		
}POINT_EVENT;
char ev_type[64];
char ev_code[64];
char tmp_buffer[256];
char out_buffer[256];
char std_buffer[256];
char real_buffer[256];
unsigned char point_counter=0;
unsigned char sys_counter=0;

static pthread_t thread;

//
char* ReturnState(char *message);
void open_devices(int start);

unsigned char front_queue=0;
unsigned char   end_queue=0;
unsigned char  queueCnt=0;

char queue_buffer[256][256];
char return_buffer[256];
char return_state=0;


void * threadJobRun()
{
	char *strPtr = NULL;
	char *ptrTest = NULL;
	
        while( 1 )
		{
			usleep(1);
			return_state=0;
	     	strPtr = ReturnState(ptrTest);
	        if( ( strlen (strPtr) >2) && (queueCnt <250))
	       {
	           if(return_state)
	           {
             
	       	   //strcpy (queue_buffer[front_queue],strPtr);
	       	   sprintf (queue_buffer[front_queue] ,"%s",strPtr);
	       	 //sprintf (queue_buffer[front_queue] ,"[%d] %d : %s [%d]",queueCnt,strlen (strPtr),strPtr,return_state);
	       	   front_queue++;
	       	   queueCnt++;
	       	   }
	        } 	
	        else
	        {
	          
	            if (queueCnt >=200)
	            {
	              front_queue=0;
                  end_queue=0;  
                  queueCnt=0;
           	      sprintf (queue_buffer[front_queue],"ERROR %d",queueCnt);
	       	      front_queue++;
	       	      queueCnt++;
	            }
	         
	        }
		}

/*	JNIEnv *env;
	int isAttached;

	isAttached = 0;
	env = NULL;
	LOGI("IN threadJobRun");
	if( jvm )
	{
		LOGI("IN threadJobRun with JVM");

		if( ( (*jvm)->GetEnv( jvm, (void**) &env, JNI_VERSION_1_6 ) ) < 0 )
		{
			LOGI( "Unable to get env at threadJobRun" );
			if( ( (*jvm)->AttachCurrentThread( jvm, &env, NULL) ) < 0 )
			{
				LOGE( "Unalbe to attach current thread at threadJobRun" );
				return NULL;
			}
			isAttached = 1;
		}

		while( 1 )
		{
			sleep(1);
	
			// --- jobs begin
			if( mClass && mMethodID )
			{
				LOGI( "call mClass & mMehtodID" );
				(*env)->CallStaticVoidMethod( env, mClass , mMethodID , (int)getpid() );
			}
			// --- end of jobs
		}

		if( isAttached )
		{
			(*jvm)->DetachCurrentThread( jvm );
		}
	}
	
*/	
	return NULL;
}

	jstring
Java_demo_ooieueioo_hellojni_stringFromJNI( JNIEnv* env,
		jobject thiz, jint start)
{
	//int i = 0;
	//return i;
	//    return (*env)->NewStringUTF(env, "Hello from JNI !");
	open_devices(start);
	char *ptrTest = NULL;
	char test[] = "my name po";
	ptrTest = test;
	char *ptrRtn = NULL;
	
	if (queueCnt )
    {
    	 strcpy ( return_buffer,queue_buffer[end_queue]);
	     end_queue++;
	     queueCnt--;
    }
    else
    {
      strcpy  ( return_buffer,"");		
    }
     ptrRtn=return_buffer;
	return (*env)->NewStringUTF(env, ptrRtn);
}

char bufmessage[1024];
int open_switch = 0;
int fd;
//add
void open_devices(int start){
	if(start == 1 && open_switch == 0){
		fd = open("/dev/input/event1", O_RDONLY );
		//////// thread
		pthread_create( &thread, NULL, threadJobRun, NULL);
		open_switch = 1;
	}
}

	void convert_evcode(char evcode, int evvalue){
		switch (evcode)
		{	
			case ABS_X :
				strcpy (ev_code, "ABS_X");
			//	sprintf(bufmessage, ev_code );
				break;
			case ABS_Y :
				strcpy (ev_code, "ABS_Y");
			//	sprintf(bufmessage, ev_code );
				break;
			case ABS_X_2 :
				strcpy (ev_code, "ABS_X_2");
			//	sprintf(bufmessage, ev_code );
				break;
			case ABS_Y_2 :
				strcpy (ev_code, "ABS_Y_2");
			//	sprintf(bufmessage, ev_code );
				break;
				/////////////////////////////////////////////
			case ABS_MT_TOUCH_MAJOR :
				//strcpy (ev_code, "ABS_MT_TOUCH_MAJOR");
				point_counter++; /// point to next x,y 
				break;
			case ABS_MT_TOUCH_MINOR :
				strcpy (ev_code, "ABS_MT_TOUCH_MINOR");
			//	sprintf(bufmessage, ev_code );
				break;
			case ABS_MT_WIDTH_MAJOR :
				strcpy (ev_code, "ABS_MT_WIDTH_MAJOR");
			//	sprintf(bufmessage, ev_code );
				break;
			case ABS_MT_WIDTH_MINOR :
				strcpy (ev_code, "ABS_MT_WIDTH_MINOR");
			//	sprintf(bufmessage, ev_code );
				break;
			case ABS_MT_ORIENTATION :
				strcpy (ev_code, "ABS_MT_ORIENTATION");
			//	sprintf(bufmessage, ev_code );
				break;
			case ABS_MT_POSITION_X :
				//strcpy (ev_code, "ABS_MT_POSITION_X");
				sprintf ( tmp_buffer,"X%02d[%05d]-",point_counter,evvalue);
				strcat  (out_buffer,tmp_buffer);
				#ifdef _FILE_OUT_
				sprintf ( tmp_buffer,"X;%02d;%05d;",point_counter,evvalue);
				strcat  ( std_buffer,tmp_buffer);
				#endif
			//	sprintf(bufmessage, out_buffer );
				break;
			case ABS_MT_POSITION_Y :
				//strcpy (ev_code, "ABS_MT_POSITION_Y");
				sprintf ( tmp_buffer,"Y%02d[%05d]-",point_counter,evvalue);
				strcat  (out_buffer,tmp_buffer);
			    #ifdef _FILE_OUT_
				sprintf ( tmp_buffer,"Y;%02d;%05d;",point_counter,evvalue);
				strcat  ( std_buffer,tmp_buffer);
				#endif
			
			//	sprintf(bufmessage, out_buffer );
				break;
			case ABS_MT_TOOL_TYPE :
				strcpy (ev_code, "ABS_MT_TOOL_TYPE");
			//	sprintf(bufmessage, ev_code );
				break;
			case  ABS_MT_BLOB_ID :
				strcpy (ev_code, " ABS_MT_BLOB_ID");
			//	sprintf(bufmessage, ev_code );
				break;
			case  ABS_MT_TRACKING_ID :
				strcpy (ev_code, " ABS_MT_TRACKING_ID");
			//	sprintf(bufmessage, ev_code );
				break;
			case  ABS_MT_PRESSURE :
				strcpy (ev_code, " ABS_MT_PRESSURE");
			//	sprintf(bufmessage, ev_code );
				break;
		} //// end switch EV_ABS
		//sprintf(bufmessage, ev_code );
	}

char* ReturnState(char *message){
	//add





	//printf(" Get Key = %d\n", 897899 );
	sprintf(bufmessage, "");
	//return bufmessage;


	char ev_type[64];
	char ev_code[64];

	//fd = open("/dev/event2", O_RDONLY );

	if( fd < 0 ){

		printf("Can not open device node.\n");
		sprintf(bufmessage, "Can not open device node.  %d(random).\n", (rand() % 100) );
		return bufmessage;
	}
	//	if( fd >= 0 ){	

	int testcnts = 300;
	char buf[]="123456";
	int ret;
	fd_set readfds;
	FD_ZERO( &readfds );
	FD_SET( fd , &readfds );
	printf(" open device node ok\n" );
	
	while( 1 )	{
	if( select( fd+1, &readfds, NULL, NULL, NULL ) >0 )
	{
		input_event ev;
		if( sizeof( input_event ) == read( fd, &ev, sizeof( input_event ) ) )
		{

			switch (ev.type )
			{   
				case EV_SYN :
					{

						if (SYN_MT_REPORT==ev.code)
						{	 
							// MT_Sync : Multitouch event end
							//printf("%s", out_buffer ); 
							strcpy ( bufmessage,real_buffer);
							return bufmessage;
						}else if (SYN_REPORT==ev.code){   
							printf("%s", out_buffer );  
							point_counter=0; 
							printf("\n");
							
							strcpy (real_buffer, out_buffer);
							
							
							return_state=1;
							#ifdef  _FILE_OUT_
	           	             FILE* file_desc; 
                  			 file_desc = fopen("/mnt/sdcard/std.txt", "ab+");
                              if (file_desc != NULL)
                                {	  
                                    fprintf( file_desc, "%s\n",std_buffer);
                                    fclose (file_desc);
                                 }
                                 
                             strcpy ( std_buffer,"");
							    
                            #endif       
							sprintf ( out_buffer,"P%03d:",sys_counter);
							sys_counter++;
							//printf("Read X:%d, Y:%d\n, Btn_Touch:%d",tmpP.x,tmpP.y,tmpP.btn_touch);
							//Read a event
							// Start read

							//po add
						//	sprintf(bufmessage, out_buffer );

						}

					}
					break; 
				case EV_KEY :
					//strcpy (ev_type, "EV_KEY");
					break;
				case EV_REL :
					// strcpy (ev_type, "EV_REL");
					break;

				case EV_ABS :
					convert_evcode(ev.code, ev.value);
				
					break; ///// EV_ABS


				default:
					//strcpy (ev_type, "unknown data");
					sprintf(ev_type,"unknown data type = %04x",ev.type);
					//po add
					sprintf(bufmessage, ev_type );
					//return bufmessage;
			}
			
	}
	else
	{
		printf(" Nothing read \n" );
		sprintf(bufmessage, " Nothing read \n" );
		//return bufmessage;
	} // if select
	
	}
} // while
//}	
//} else {

//}
return NULL;
}
