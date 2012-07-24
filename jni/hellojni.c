#include <string.h>
#include <jni.h>
//add
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <limits.h>
#include <sys/types.h>
#include <sys/stat.h>

#define EV_SYN	0x00
#define EV_KEY	0x01
#define EV_REL  0x02
#define EV_ABS	0x03

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
unsigned char point_counter=0;
unsigned char sys_counter=0;


//
char* ReturnState(char *message);
void open_devices(int start);

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
	ptrRtn = ReturnState(ptrTest);
	return (*env)->NewStringUTF(env, ptrRtn);
}

char bufmessage[1024];
int open_switch = 0;
int fd;
//add
void open_devices(int start){
	if(start == 1 && open_switch == 0){
		fd = open("/dev/input/event1", O_RDONLY );
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
			//	sprintf(bufmessage, out_buffer );
				break;
			case ABS_MT_POSITION_Y :
				//strcpy (ev_code, "ABS_MT_POSITION_Y");
				sprintf ( tmp_buffer,"Y%02d[%05d]-",point_counter,evvalue);
				strcat  (out_buffer,tmp_buffer);
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
	
//	sprintf(out_buffer, "");
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
							//strcpy ( bufmessage,out_buffer);
							//return bufmessage;
						}else if (SYN_REPORT==ev.code){   
							//printf("%s", out_buffer );  
							point_counter=0; 
							//printf("\n");
							
						    strcpy ( bufmessage,out_buffer);
							sprintf ( out_buffer,"P%03d:",sys_counter);
							sys_counter++;
							//printf("Read X:%d, Y:%d\n, Btn_Touch:%d",tmpP.x,tmpP.y,tmpP.btn_touch);
							//Read a event
							// Start read

							//po add
						//	sprintf(bufmessage, out_buffer );
                           
							return bufmessage;
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
				//	convert_evcode(ev.code, ev.value);
				//	return bufmessage;
					break; ///// EV_ABS


				default:
					//strcpy (ev_type, "unknown data");
					//sprintf(ev_type,"unknown data type = %04x",ev.type);
					//po add
					sprintf(bufmessage, ev_type );
					return bufmessage;
			}
			//return bufmessage;
			//printf("%s, code= %s, Value =%d\n", ev_type, ev_code, ev.value ); 
			//	} /// REMOVE EV_SYN	
			
	}
	else
	{
		printf(" Nothing read \n" );
		sprintf(bufmessage, " Nothing read \n" );
		return bufmessage;
	} // if select
	
	}
} // while
//}	
//} else {

//}
return out_buffer;
}
