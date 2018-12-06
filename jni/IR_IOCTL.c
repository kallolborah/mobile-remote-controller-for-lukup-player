#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <termios.h>
//#include <linux/serial.h>
#include "serial.h"
#include "tatasky.h"
#include "hathway.h"
#include "indigital.h"
#include "act.h"
#include "hc.h"
#include "videocon.h"
#include "sun.h"
#include "dishtv.h"
#include "broadcom.h"
#include <jni.h>
#include <android/log.h>

#define UART_LCR	3
#define UART_LCR_DLAB	0x80
#define UART_LCR_WLEN8	0x03
#define UART_OMAP_MDR1	0x08
#define UART_OMAP_MDR1_CIR_MODE	0x06

#define	UART_OMAP_CFPS	0x18
#define UART_OMAP_CFPS_36	0x69 //0x6f // 0x69

#define UART_DLL	0
#define UART_DLM	1
#define UART_FCR	2
#define UART_FCR_ENABLE_FIFO	0x01
#define UART_OMAP_MDR2	0x09
#define UART_MDR2_25	0x00

#define UART_EFR	2
#define UART_EFR_ECB	0x10

#define UART_SCR	7

#define UART_LCR_CONF_MODE_A	UART_LCR_DLAB
#define UART_MCR	4
#define UART_MCR_TCRTLR	0x40

#define UART_TI752_TLR	7

#define UART_FCR_CLEAR_XMIT	0x04
#define UART_FCR_T_TRIG_00	0x00

# if 1

#define TIOCIRSETREG 0x5438

#define TIOCIRSND 0x5439

#define TIOCIRGETREG 0x5440

#define TIOCIRCLRFFO 0x5441

#endif

#define LOGTAG "lukupIR"


// actir and den remotes are identical
#define LOGD(tag, message) __android_log_print(ANDROID_LOG_DEBUG, tag, message);
#define LOGW(tag, message) __android_log_print(ANDROID_LOG_WARN, tag, message);
#define LOGI(tag, message) __android_log_print(ANDROID_LOG_INFO, tag, message);


int start (int devname, char *vendorname)
{

	int handle = open( "/dev/ttyO4", O_RDWR | O_NOCTTY );

	int ioctl_res;
	int x[2];
	
	if (handle <= 0)
		{
			printf( "Failed to open %s - %d (%s)\n", "/dev/ttyO4", errno, strerror(errno) );
			 LOGD(LOGTAG, "Cannot open device - '/dev/ttyO4'");
			return -1;
		}
		LOGD(LOGTAG, " Open device done  - '/dev/ttyO4'");

	x[0] = UART_LCR;
		x[1] = 0;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		
		
			x[0] = UART_LCR;
		x[1] = UART_LCR_DLAB |UART_LCR_WLEN8;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		
		
		
		
		
		x[0] = UART_LCR;
		x[1] = 0x00;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}


	x[0] = UART_OMAP_MDR1;
		x[1] = UART_OMAP_MDR1_CIR_MODE;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, TIOCIRCLRFFO, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRCLRFFO ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		the CFPS register must be set for the  carrier frequency
		*/

		x[0] = UART_OMAP_CFPS;
		x[1] = UART_OMAP_CFPS_36;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in start() vendorname = %s", vendorname);

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in start() handle=%d", handle);

	return handle;

}


int inputfunc (int func, int handle, char *vendorname)
{
	char key[9] = {0x00,};
		int a[] = {0,};
		int z[] = {0,};
		int index = 1;
		int ioctl_res;
		int selection=0;
		int codeIndex;
		int x[2];
	   // handle = 33, func =1;

	LOGD(LOGTAG, " Starting input functions  - 'lukupIR Remote'");

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc handle=%d", handle);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc() vendorname = %s", vendorname);
	selection = func;

	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <9; index++){
				key[index] = tatasky[selection-1][index];
			}
			LOGD(LOGTAG, "HOME   - 'selected'");
			break;

	case 2:
			codeIndex = selection;
	                for(index =0; index <9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "TV   - 'selected'");
	                break;

	case 3:
			codeIndex = selection;
	                for(index =0; index <9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "POWER   - 'selected'");
	                break;

	case 4:
			codeIndex = selection;
	                for(index =0; index <9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "SHOWCASE   - 'selected'");
	                break;

	case 5:
			codeIndex = selection;
			for(index = 0; index < 9; index++){
				key[index] = tatasky[selection-1][index];
			}
			LOGD(LOGTAG, "ORGANISE   - 'selected'");
			break;
	case 6:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	         LOGD(LOGTAG, "GUIDE   - 'selected'");
	                break;

	case 7:
				codeIndex = selection;
		                for(index = 0; index < 9; index++){
		                        key[index] = tatasky[selection-1][index];
		                }
		         LOGD(LOGTAG, "ACTIVE   - 'selected'");
		                break;


	case 8:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "MUTE   - 'selected'");
	                break;

	case 9:
		 	 codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
             LOGD(LOGTAG, "INFO   - 'selected'");
	                break;

	case 10:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "VOLUMEUP   - 'selected'");
	                break;

	case 11:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "VOLUMEDOWN   - 'selected'");
	                break;

	case 12:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
            LOGD(LOGTAG, "CHANNELUP   - 'selected'");
	                break;

	case 13:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
            LOGD(LOGTAG, "CHANNELDOWN   - 'selected'");
	                break;

	case 14:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
            LOGD(LOGTAG, "UPARROW   - 'selected'");
	                break;

	case 15:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
            LOGD(LOGTAG, "LEFTARROW   - 'selected'");
	                break;

	case 16:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
            LOGD(LOGTAG, "RIGHTARROW   - 'selected'");
	                break;

	case 17:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
            LOGD(LOGTAG, "DOWNARROW   - 'selected'");
	                break;

	case 18:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	         LOGD(LOGTAG, "SELECT   - 'selected'");
	                break;

	case 19:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "FAVOURITE   - 'selected'");
	                break;

	case 20:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "BACK   - 'selected'");
	                break;

	case 21:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "HELP   - 'selected'");
	                break;

	case 22:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	         LOGD(LOGTAG, "RED   - 'selected'");
	                break;

	case 23:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "GREEN   - 'selected'");
	                break;

	case 24:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "YELLOW   - 'selected'");
	                break;

	case 25:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "BLUE   - 'selected'");
	                break;

	case 26:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-ONE   - 'selected'");
	                break;

	case 27:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-TWO   - 'selected'");
	                break;

	case 28:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-THREE   - 'selected'");
	                break;

	case 29:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-FOUR   - 'selected'");
	                break;

	case 30:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-FIVE   - 'selected'");
	                break;

	case 31:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-SIX   - 'selected'");
	                break;

	case 32:
			codeIndex = selection;
					for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
			LOGD(LOGTAG, "KEY-SEVEN   - 'selected'");
	                break;

	case 33:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-EIGHT   - 'selected'");
	                break;

	case 34:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-NINE   - 'selected'");
	                break;

	case 35:
			codeIndex = selection;
	                for(index = 0; index < 9; index++){
	                        key[index] = tatasky[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-ZERO   - 'selected'");
	                break;
	default:
		codeIndex = -1;
		printf("Invalid Choice\n");
		LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
		break;
	}

	if(codeIndex > 0){
		// Writing the Register Settings for TataSky (RC6-2-20)

		x[0] = UART_LCR;
		x[1] = 0;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		LCR registers enable the DLL and DLH registers access --UART_LCR_DLAB
		enables the break_en bit  --UART_LCR_WLEN8 it breaks the bit down
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_DLAB |UART_LCR_WLEN8;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		for theUART to transmit the break bit must be cleared , if I'm assuming that 0xbf will enable the access to CIR registers
		*/

		x[0] = UART_LCR;
		x[1] = 0x00;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		MDR register will set the CIR mode for theUART
		*/

		//TODO : Check write
		x[0] = UART_OMAP_MDR1;
		x[1] = UART_OMAP_MDR1_CIR_MODE;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, TIOCIRCLRFFO, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRCLRFFO ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		the CFPS register must be set for the  carrier frequency
		*/

		x[0] = UART_OMAP_CFPS;
		x[1] = UART_OMAP_CFPS_36;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		/*
		DLL andf DLH registers need to be cleared , just before the tx/rx fifo is enabled
		*/

		x[0] = UART_DLL;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_DLM;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		MDR2 register needs to be set for the duty cycle in CIR mode
		*/
		x[0] = UART_OMAP_MDR2;
		x[1] = UART_MDR2_25;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		/*
		Enhanced fifo register-- it allows yu to set trigger level
		enables acces to Fifo control register for setting the trigger level i.e. bitr 5 & 4
		enables acces to IER register i.e bit 7 & 6 for CTS and RTS interrupt
		enables access to MCR register MODEM control register bit 7:5 the bits are used for accessing the trigger level specially bit no 6
		*/

		x[0] = UART_EFR;
		x[1] = UART_EFR_ECB;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		the SCR=0x00 and TLR =0x00 to set the trigger values in FCR registers

		for the trigger level the Supplementary control register needs to be set , the Modem control register needs to be set for accessing the TLR regsiter
		*/

		x[0] = UART_SCR;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		/*
		for accessing the TLR registers we need to set the MCR - modem control register the bit no 6th should be 1 to access the TLR register
		 for using MCR regsiter I need to  change the LCR register to config mode A
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_CONF_MODE_A;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_MCR;
		x[1] = UART_MCR_TCRTLR;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		/*
		writing onto the TLR register to get the trigger level on  FCR register ,
		TLR register is meant for DMA transmit we r doin it the irq way so 0x00
		*/

		x[0] = UART_TI752_TLR;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		enabling the  transmit fifo and fifo trigger level in  fifo control register

		the transmit fifo  register is enabled

		the trigger level is set for  transmit 32 characters

		*/

		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO |UART_FCR_CLEAR_XMIT;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_FCR;
		x[1] = UART_FCR_T_TRIG_00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_DLL;
		x[1] = 0x35;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_DLM;
		x[1] = 0x05;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		//	serial_out(UART_DLL , cfps & 0xff );

		//	serial_out(UART_DLM , cfps >> 8 );
		//	serial_out(UART_DLL , 0x00 );
		//	serial_out(UART_DLM , 0x00 );

		x[0] = UART_LCR;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
                 
		//LOGD(LOGTAG,"  KeyValue = "+key);
		ioctl_res = ioctl( handle, TIOCIRSND, &key );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSND ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSND ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
	}
		LOGD(LOGTAG, "IR Remote KEY FUNCTION selected    - 'completed successively'");

	return 1;
}

int inputfuncnec (int func, int handle, char *vendorname)
{
		char key[18] = {0x00,};
		int a[] = {0,};
		int z[] = {0,};
		int index = 1;
		int ioctl_res;
		int selection=0;
		int codeIndex;
		int x[2];
		char hath[20] = "hathway";
		char indigi[20] = "indigital";
		char act[20] = "actir";
		char hc[20] = "hc";
//		char rl[20] = "reliance";
	   // handle = 33, func =1;

	LOGD(LOGTAG, " Starting input functions  - 'lukupIR Remote'");

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec handle=%d", handle);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_nec() vendorname = %s", vendorname);



		if (strncmp(vendorname, hath, 7) == 0)
		{

	selection = func;

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec and hathway.... func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_nec() and hathway..... vendorname = %s", vendorname);


	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = hathway[selection-1][index];
			}
			LOGD(LOGTAG, "POWER   - 'selected'");
			break;

	case 2:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "MUTE   - 'selected'");
	                break;

	case 3:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "CH+   - 'selected'");
	                break;

	case 4:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "CH-   - 'selected'");
	                break;

	case 5:
			codeIndex = selection;
			for(index = 0; index < 18; index++){
				key[index] = hathway[selection-1][index];
			}
			LOGD(LOGTAG, "VOL+   - 'selected'");
			break;
	case 6:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	         LOGD(LOGTAG, "VOL-   - 'selected'");
	                break;

	case 7:
				codeIndex = selection;
		                for(index = 0; index < 18; index++){
		                        key[index] = hathway[selection-1][index];
		                }
		         LOGD(LOGTAG, "PROMO   - 'selected'");
		                break;



	case 8:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "RS   - 'selected'");
	                break;

	case 9:
		 	 codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
             LOGD(LOGTAG, "FAV   - 'selected'");
	                break;

	case 10:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "BLANK   - 'selected'");
	                break;

	case 11:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "MSG   - 'selected'");
	                break;

	case 12:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
            LOGD(LOGTAG, "LANG   - 'selected'");
	                break;

	case 13:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
            LOGD(LOGTAG, "INTERACTIVE   - 'selected'");
	                break;

	case 14:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
            LOGD(LOGTAG, "GAMES   - 'selected'");
	                break;

	case 15:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
            LOGD(LOGTAG, "BACK   - 'selected'");
	                break;

	case 16:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
            LOGD(LOGTAG, "INFO   - 'selected'");
	                break;

	case 17:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
            LOGD(LOGTAG, "EPG   - 'selected'");
	                break;

	case 18:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	         LOGD(LOGTAG, "MENU   - 'selected'");
	                break;

	case 19:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "OK   - 'selected'");
	                break;

	case 20:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "ARROWUP   - 'selected'");
	                break;

	case 21:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "ARROWDOWN   - 'selected'");
	                break;

	case 22:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	         LOGD(LOGTAG, "ARROWRIGHT   - 'selected'");
	                break;

	case 23:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "ARROWLEFT   - 'selected'");
	                break;

	case 24:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "RADIO   - 'selected'");
	                break;

	case 25:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "HELP   - 'selected'");
	                break;

	case 26:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-ZERO   - 'selected'");
	                break;

	case 27:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-ONE   - 'selected'");
	                break;

	case 28:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-TWO   - 'selected'");
	                break;

	case 29:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-THREE   - 'selected'");
	                break;

	case 30:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-FOUR   - 'selected'");
	                break;

	case 31:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-FIVE   - 'selected'");
	                break;

	case 32:
			codeIndex = selection;
					for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
			LOGD(LOGTAG, "KEY-SIX   - 'selected'");
	                break;

	case 33:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-SEVEN   - 'selected'");
	                break;

	case 34:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-EIGHT   - 'selected'");
	                break;

	case 35:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = hathway[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-NINE   - 'selected'");
	                break;
	case 36:
				codeIndex = selection;
		                for(index = 0; index < 18; index++){
		                        key[index] = hathway[selection-1][index];
		                }
		        LOGD(LOGTAG, "KEY-RED   - 'selected'");
		                break;

	case 37:
				codeIndex = selection;
			            for(index = 0; index < 18; index++){
			                   key[index] = hathway[selection-1][index];
			                }
			    LOGD(LOGTAG, "KEY-GREEN   - 'selected'");
			                break;

	case 38:
				codeIndex = selection;
			            for(index = 0; index < 18; index++){
			                   key[index] = hathway[selection-1][index];
			                }
			    LOGD(LOGTAG, "KEY-YELLOW   - 'selected'");
			                break;

	case 39:
				codeIndex = selection;
			            for(index = 0; index < 18; index++){
			                  key[index] = hathway[selection-1][index];
			                }
			     LOGD(LOGTAG, "KEY-BLUE   - 'selected'");
			                break;

	default:
		codeIndex = -1;
		printf("Invalid Choice\n");
		LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
		break;
	}
	} // end of if


	if (strncmp(vendorname, indigi, 9) == 0)
		{

			selection = func;

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec and indigital.... func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_nec() and indigital..... vendorname = %s", vendorname);


	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "TV-standby   - 'selected'");
			break;

	case 2:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "TV-V+   - 'selected'");
			break;
	case 3:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Standby   - 'selected'");
			break;
	case 4:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button0   - 'selected'");
			break;
	case 5:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button1   - 'selected'");
			break;
	case 6:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button2   - 'selected'");
			break;
	case 7:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button3   - 'selected'");
			break;
	case 8:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button4   - 'selected'");
			break;
	case 9:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button5   - 'selected'");
			break;
	case 10:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button6   - 'selected'");
			break;
	case 11:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button7   - 'selected'");
			break;
	case 12:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button8   - 'selected'");
			break;
	case 13:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button9   - 'selected'");
			break;
	case 14:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button?   - 'selected'");
			break;
	case 15:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button BACK   - 'selected'");
			break;
	case 16:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button MENU   - 'selected'");
			break;
	case 17:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button EXIT   - 'selected'");
			break;
	case 18:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button LEFT ARROW   - 'selected'");
			break;
	case 19:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button RIGHT ARROW   - 'selected'");
			break;
	case 20:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button UP ARROW   - 'selected'");
			break;
	case 21:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button DOWN ARROW   - 'selected'");
			break;
	case 22:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button OK   - 'selected'");
			break;
	case 23:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button VOL+   - 'selected'");
			break;
	case 24:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button VOL-   - 'selected'");
			break;
	case 25:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button MUTE   - 'selected'");
			break;
	case 26:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button CH+   - 'selected'");
			break;
	case 27:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button CH-   - 'selected'");
			break;
	case 28:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button INFO   - 'selected'");
			break;
	case 29:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button EPG   - 'selected'");
			break;
	case 30:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button LIST   - 'selected'");
			break;
	case 31:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button FAV   - 'selected'");
			break;
	case 32:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button AUDIO   - 'selected'");
			break;
	case 33:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button PLAY   - 'selected'");
			break;
	case 34:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button STOP   - 'selected'");
			break;
	case 35:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button TV/RADIO   - 'selected'");
			break;
	case 36:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button RECORD   - 'selected'");
			break;
	case 37:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button REWIND   - 'selected'");
			break;
	case 38:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button FWD   - 'selected'");
			break;
	case 39:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = indigital[selection-1][index];
			}
			LOGD(LOGTAG, "Remote Button GOTO   - 'selected'");
			break;
	default:
			codeIndex = -1;
			printf("Invalid Choice\n");
			LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
			break;
		}
		}

	if (strncmp(vendorname, act, 5) == 0)
		{

			selection = func;

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec and actir.... func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_nec() and actir..... vendorname = %s", vendorname);


	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "POWER   - 'selected'");
			break;
	case 2:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON0   - 'selected'");
			break;
	case 3:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON1   - 'selected'");
			break;
	case 4:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON2   - 'selected'");
			break;
	case 5:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON3   - 'selected'");
			break;
	case 6:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON4   - 'selected'");
			break;
	case 7:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON5   - 'selected'");
			break;
	case 8:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON6   - 'selected'");
			break;
	case 9:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON7   - 'selected'");
			break;
	case 10:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON8   - 'selected'");
			break;
	case 11:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BUTTON9   - 'selected'");
			break;
	case 12:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "FAV   - 'selected'");
			break;
	case 13:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "LAST   - 'selected'");
			break;
	case 14:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "CH+   - 'selected'");
			break;

	case 15:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "CH-   - 'selected'");
			break;

	case 16:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "VOL+   - 'selected'");
			break;
	case 17:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "VOL-   - 'selected'");
			break;
	case 18:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "MUTE   - 'selected'");
			break;

	case 19:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "TOP FWD   - 'selected'");
			break;
	case 20:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "TOP RWD   - 'selected'");
			break;
	case 21:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "NAV   - 'selected'");
			break;
	case 22:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "OK   - 'selected'");
			break;
	case 23:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "EXIT   - 'selected'");
			break;
	case 24:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "LEFT ARROW   - 'selected'");
			break;
	case 25:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "RIGHT ARROW   - 'selected'");
			break;
	case 26:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "UP ARROW   - 'selected'");
			break;
	case 27:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "DOWN ARROW   - 'selected'");
			break;
	case 28:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "EPG   - 'selected'");
			break;
	case 29:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "MENU   - 'selected'");
			break;
	case 30:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "OPT   - 'selected'");
			break;
	case 31:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "VOD   - 'selected'");
			break;
	case 32:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BOTTOM REWD   - 'selected'");
			break;
	case 33:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BOTTOM FWD   - 'selected'");
			break;
	case 34:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BOTTOM PLAY   - 'selected'");
			break;
	case 35:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BOTTOM STOP   - 'selected'");
			break;
	case 36:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BOTTOM PAUSE   - 'selected'");
			break;
	case 37:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "TV/RADIO BUTTON   - 'selected'");
			break;
	case 38:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "SETUP BUTTON   - 'selected'");
			break;

	case 39:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "RED   - 'selected'");
			break;
	case 40:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "GREEN   - 'selected'");
			break;
	case 41:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "YELLOW   - 'selected'");
			break;
	case 42:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = actir[selection-1][index];
			}
			LOGD(LOGTAG, "BLUE   - 'selected'");
			break;

	default:
		codeIndex = -1;
		printf("Invalid Choice\n");
		LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
		break;
	}
		}

	if (strncmp(vendorname, hc, 9) == 0)
	{
		selection = func;

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec and homecable.... func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_nec() and homecable..... vendorname = %s", vendorname);


	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "POWER   - 'selected'");
			break;
	case 2:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "REC   - 'selected'");
			break;
	case 3:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "PAUSE   - 'selected'");
			break;
	case 4:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "ASP.   - 'selected'");
			break;
	case 5:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "PLAY   - 'selected'");
			break;
	case 6:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "STOP   - 'selected'");
			break;
	case 7:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "REW   - 'selected'");
			break;
	case 8:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "FWD   - 'selected'");
			break;
	case 9:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "REW END   - 'selected'");
			break;
	case 10:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "FWD END   - 'selected'");
			break;

	case 11:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "ARROW UP (CH+)   - 'selected'");
			break;
	case 12:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "ARROW DOWN (CH-)  - 'selected'");
			break;
	case 13:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "ARROW LEFT (VOL-)  - 'selected'");
			break;
	case 14:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "ARROW RIGHT (VOL+)  - 'selected'");
			break;
	case 15:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "OK   - 'selected'");
			break;
	case 16:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "MENU   - 'selected'");
			break;
	case 17:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "EXIT   - 'selected'");
			break;
	case 18:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "SUBT   - 'selected'");
			break;
	case 19:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "INFO   - 'selected'");
			break;
	case 20:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "TTX   - 'selected'");
			break;
	case 21:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "PVR   - 'selected'");
			break;
	case 22:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "MALL   - 'selected'");
			break;
	case 23:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "FORMAT   - 'selected'");
			break;
	case 24:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "AUDIO   - 'selected'");
			break;
	case 25:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY ONE   - 'selected'");
			break;
	case 26:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY TWO   - 'selected'");
			break;
	case 27:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY THREE   - 'selected'");
			break;
	case 28:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY FOUR   - 'selected'");
			break;
	case 29:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY FIVE  - 'selected'");
			break;
	case 30:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY SIX   - 'selected'");
			break;
	case 31:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY SEVEN   - 'selected'");
			break;
	case 32:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY EIGHT   - 'selected'");
			break;
	case 33:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY NINE   - 'selected'");
			break;

	case 34:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "KEY ZERO   - 'selected'");
			break;
	case 35:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "BACK   - 'selected'");
			break;
	case 36:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = homecable[selection-1][index];
			}
			LOGD(LOGTAG, "TV/RADIO   - 'selected'");
			break;

	default:
		codeIndex = -1;
		printf("Invalid Choice\n");
		LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
		break;

	}
	}

	if(codeIndex > 0){
		// Writing the Register Settings for TataSky (RC6-2-20)

		x[0] = UART_LCR;
		x[1] = 0;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		LCR registers enable the DLL and DLH registers access --UART_LCR_DLAB
		enables the break_en bit  --UART_LCR_WLEN8 it breaks the bit down
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_DLAB |UART_LCR_WLEN8;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		for theUART to transmit the break bit must be cleared , if I'm assuming that 0xbf will enable the access to CIR registers
		*/

		x[0] = UART_LCR;
		x[1] = 0x00;


		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		MDR register will set the CIR mode for theUART
		*/

		//TODO : Check write
		x[0] = UART_OMAP_MDR1;
		x[1] = UART_OMAP_MDR1_CIR_MODE;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, TIOCIRCLRFFO, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRCLRFFO ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		the CFPS register must be set for the  carrier frequency
		*/

		x[0] = UART_OMAP_CFPS;
		x[1] = UART_OMAP_CFPS_36;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		/*
		DLL andf DLH registers need to be cleared , just before the tx/rx fifo is enabled
		*/

		x[0] = UART_DLL;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_DLM;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		MDR2 register needs to be set for the duty cycle in CIR mode
		*/
		x[0] = UART_OMAP_MDR2;
		x[1] = UART_MDR2_25;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		/*
		Enhanced fifo register-- it allows yu to set trigger level
		enables acces to Fifo control register for setting the trigger level i.e. bitr 5 & 4
		enables acces to IER register i.e bit 7 & 6 for CTS and RTS interrupt
		enables access to MCR register MODEM control register bit 7:5 the bits are used for accessing the trigger level specially bit no 6
		*/

		x[0] = UART_EFR;
		x[1] = UART_EFR_ECB;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		the SCR=0x00 and TLR =0x00 to set the trigger values in FCR registers

		for the trigger level the Supplementary control register needs to be set , the Modem control register needs to be set for accessing the TLR regsiter
		*/

		x[0] = UART_SCR;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		/*
		for accessing the TLR registers we need to set the MCR - modem control register the bit no 6th should be 1 to access the TLR register
		 for using MCR regsiter I need to  change the LCR register to config mode A
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_CONF_MODE_A;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_MCR;
		x[1] = UART_MCR_TCRTLR;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		/*
		writing onto the TLR register to get the trigger level on  FCR register ,
		TLR register is meant for DMA transmit we r doin it the irq way so 0x00
		*/

		x[0] = UART_TI752_TLR;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		/*
		enabling the  transmit fifo and fifo trigger level in  fifo control register

		the transmit fifo  register is enabled

		the trigger level is set for  transmit 32 characters

		*/

		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO |UART_FCR_CLEAR_XMIT;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_FCR;
		x[1] = UART_FCR_T_TRIG_00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_DLL;
		x[1] = 0x90; //0x35;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		x[0] = UART_DLM;
		x[1] = 0x06; //0x05;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
		//	serial_out(UART_DLL , cfps & 0xff );

		//	serial_out(UART_DLM , cfps >> 8 );
		//	serial_out(UART_DLL , 0x00 );
		//	serial_out(UART_DLM , 0x00 );

		x[0] = UART_LCR;
		x[1] = 0x00;

		ioctl_res = ioctl( handle, TIOCIRSETREG, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSETREG ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, TIOCIRSND, &key );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSND ioctl: error %d (%s)\n", errno, strerror(errno) );
			LOGD(LOGTAG, " Failed TIOCIRSND ioctl: error - 'lukupIR Remote'");
			close( handle );
			return -1;
		}
	}
		LOGD(LOGTAG, "IR Remote KEY FUNCTION selected    - 'completed successively'");

	return 1;
}


int inputfuncdish (int func, int handle, char *vendorname)
{
		char key[12] = {0x00,};
		int a[] = {0,};
		int z[] = {0,};
		int index = 1;
		int ioctl_res;
		int selection=0;
		int codeIndex;
		int x[2];
                
                char dt[20] = "dish" ;
	   // handle = 33, func =1;

	LOGD(LOGTAG, " Starting input functions  - 'lukupIR Remote'");

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_rc5 handle=%d", handle);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_rc5 func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_rc5() vendorname = %s", vendorname);



		if (strncmp(vendorname, dt, 4) == 0)
		{

	selection = func;

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc and dish.... func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc() and dish..... vendorname = %s", vendorname);


	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "POWER   - 'selected'");
			break;

	case 2:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "MUTE   - 'selected'");
	                break;

	case 3:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "BuZz   - 'selected'");
	                break;

	case 4:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "FAV   - 'selected'");
	                break;

	case 5:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "PREVCH   - 'selected'");
			break;
	case 6:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	         LOGD(LOGTAG, "MSG   - 'selected'");
	                break;

	case 7:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "FILE   - 'selected'");
		                break;


	case 8:
			codeIndex = selection;
                        for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "TimeShift   - 'selected'");
		                break;

	case 9:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "MOD   - 'selected'");
			break;

	case 10:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "VOL-   - 'selected'");
	                break;

	case 11:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "VOL+   - 'selected'");
	                break;

	case 12:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "CH+   - 'selected'");
	                break;

	case 13:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "CH-   - 'selected'");
			break;
	case 14:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	         LOGD(LOGTAG, "GAMES   - 'selected'");
	                break;

	case 15:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "REW   - 'selected'");
		                break;
        case 16:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "ACTIVE   - 'selected'");
			break;

	case 17:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "FWD   - 'selected'");
	                break;

	case 18:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "PLAY   - 'selected'");
	                break;

	case 19:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "PAUSE   - 'selected'");
	                break;

	case 20:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "STOP   - 'selected'");
			break;
	case 21:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	         LOGD(LOGTAG, "REC   - 'selected'");
	                break;

	case 22:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "REWEND   - 'selected'");
		                break;


	case 23:
			codeIndex = selection;
                        for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "FEWEND   - 'selected'");
		                break;

	case 24:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "RED   - 'selected'");
			break;

	case 25:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "GREEN   - 'selected'");
	                break;

	case 26:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "YELLOW   - 'selected'");
	                break;

	case 27:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "BLUE   - 'selected'");
	                break;

	case 28:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "GUIDE   - 'selected'");
			break;
	case 29:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	         LOGD(LOGTAG, "MYA/C   - 'selected'");
	                break;

	case 30:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "EXIT   - 'selected'");
		                break;
       	case 31:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "MENU   - 'selected'");
			break;

	case 32:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "UPARW   - 'selected'");
	                break;

	case 33:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "DWNARW   - 'selected'");
	                break;

	case 34:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "LFTARW   - 'selected'");
	                break;

	case 35:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-ONE   - 'selected'");
			break;
	case 36:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	         LOGD(LOGTAG, "RGTARW   - 'selected'");
	                break;

	case 37:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY-TWO   - 'selected'");
		                break;


	case 38:
			codeIndex = selection;
                        for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY-THREE   - 'selected'");
		                break;

	case 39:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-FOUR   - 'selected'");
			break;

	case 40:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-FIVE   - 'selected'");
	                break;

	case 41:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY_SIX   - 'selected'");
	                break;

	case 42:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-SEVEN   - 'selected'");
	                break;

	case 43:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-EIGHT   - 'selected'");
			break;
	case 44:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-NINE   - 'selected'");
	                break;

	case 45:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY-ZERO   - 'selected'");
		                break;
        case 46:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "P-   - 'selected'");
			break;

	case 47:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "P+   - 'selected'");
	                break;

	case 48:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "TVBX   - 'selected'");
	                break;

	case 49:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	        LOGD(LOGTAG, "TVBOX   - 'selected'");
	                break;

	case 50:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "CLOCK   - 'selected'");
			break;
	case 51:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = dish[selection-1][index];
	                }
	         LOGD(LOGTAG, "BACK   - 'selected'");
	                break;

	case 52:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "LANG   - 'selected'");
		                break;


	case 53:
			codeIndex = selection;
                        for(index = 0; index < 12; index++){
		                        key[index] = dish[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY - i   - 'selected'");
		                break;

	case 54:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = dish[selection-1][index];
			}
			LOGD(LOGTAG, "KEY - OK   - 'selected'");
			break;
       default:
		codeIndex = -1;
		printf("Invalid Choice\n");
		LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
		break;
	}
}
if(codeIndex > 0){
		// Writing the Register Settings for TataSky (RC6-2-20)

		x[0] = UART_LCR;
		x[1] = 0;
	
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf("In first if case ... \n ");
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		LCR registers enable the DLL and DLH registers access --UART_LCR_DLAB
		enables the break_en bit  --UART_LCR_WLEN8 it breaks the bit down
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_DLAB |UART_LCR_WLEN8;
	
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		for theUART to transmit the break bit must be cleared , if I'm assuming that 0xbf will enable the access to CIR registers  
		*/

		x[0] = UART_LCR;
		x[1] = 0x00;


		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		MDR register will set the CIR mode for theUART 
		*/
		
		//TODO : Check write
		x[0] = UART_OMAP_MDR1;
		x[1] = UART_OMAP_MDR1_CIR_MODE;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, 0x5441, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRCLRFFO ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		the CFPS register must be set for the  carrier frequency 
		*/

		x[0] = UART_OMAP_CFPS;
		x[1] = UART_OMAP_CFPS_36;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/* 
		DLL andf DLH registers need to be cleared , just before the tx/rx fifo is enabled 
		*/ 

		x[0] = UART_DLL;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_DLM;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		MDR2 register needs to be set for the duty cycle in CIR mode  
		*/	
		x[0] = UART_OMAP_MDR2;
		x[1] = UART_MDR2_25;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		Enhanced fifo register-- it allows yu to set trigger level  
		enables acces to Fifo control register for setting the trigger level i.e. bitr 5 & 4 
		enables acces to IER register i.e bit 7 & 6 for CTS and RTS interrupt
		enables access to MCR register MODEM control register bit 7:5 the bits are used for accessing the trigger level specially bit no 6 
		*/

		x[0] = UART_EFR;
		x[1] = UART_EFR_ECB;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		the SCR=0x00 and TLR =0x00 to set the trigger values in FCR registers

		for the trigger level the Supplementary control register needs to be set , the Modem control register needs to be set for accessing the TLR regsiter 
		*/

		x[0] = UART_SCR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		for accessing the TLR registers we need to set the MCR - modem control register the bit no 6th should be 1 to access the TLR register
		 for using MCR regsiter I need to  change the LCR register to config mode A 
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_CONF_MODE_A;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_MCR;
		x[1] = UART_MCR_TCRTLR;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		writing onto the TLR register to get the trigger level on  FCR register ,
		TLR register is meant for DMA transmit we r doin it the irq way so 0x00
		*/

		x[0] = UART_TI752_TLR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		enabling the  transmit fifo and fifo trigger level in  fifo control register

		the transmit fifo  register is enabled

		the trigger level is set for  transmit 32 characters  

		*/

		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO |UART_FCR_CLEAR_XMIT;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_FCR;
		x[1] = UART_FCR_T_TRIG_00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_DLL;
		x[1] = 0x45;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		x[0] = UART_DLM;
		x[1] = 0x0a;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		//	serial_out(UART_DLL , cfps & 0xff );

		//	serial_out(UART_DLM , cfps >> 8 );
		//	serial_out(UART_DLL , 0x00 );
		//	serial_out(UART_DLM , 0x00 );

		x[0] = UART_LCR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, 0x5439, &key );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSND ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	}
               LOGD(LOGTAG, "IR Remote KEY FUNCTION selected    - 'completed successively'");

	return 1;
}

int inputfuncsun (int func, int handle, char *vendorname)
{
		char key[12] = {0x00,};
		int a[] = {0,};
		int z[] = {0,};
		int index = 1;
		int ioctl_res;
		int selection=0;
		int codeIndex;
		int x[2];
               char st[20] = "sun" ;
	   // handle = 33, func =1;

	LOGD(LOGTAG, " Starting input functions  - 'lukupIR Remote'");

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_rc handle=%d", handle);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_rc func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_rc() vendorname = %s", vendorname);



		if (strncmp(vendorname, st, 3) == 0)
		{

	selection = func;

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc and sun.... func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc() and sun..... vendorname = %s", vendorname);


	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "POWER   - 'selected'");
			break;

	case 2:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "AUDIO   - 'selected'");
	                break;

	case 3:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "MUTE   - 'selected'");
	                break;

	case 4:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "LANG   - 'selected'");
	                break;

	case 5:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-ONE   - 'selected'");
			break;
	case 6:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-TWO   - 'selected'");
	                break;

	case 7:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY-THREE   - 'selected'");
		                break;


	case 8:
			codeIndex = selection;
                        for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY-FOUR   - 'selected'");
		                break;

	case 9:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-FIVE   - 'selected'");
			break;

	case 10:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-SIX   - 'selected'");
	                break;

	case 11:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-SEVEN   - 'selected'");
	                break;

	case 12:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-EIGHT   - 'selected'");
	                break;

	case 13:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-NINE   - 'selected'");
			break;
	case 14:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-ZERO   - 'selected'");
	                break;

	case 15:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "REMINDER   - 'selected'");
		                break;
        case 16:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "FAV   - 'selected'");
			break;

	case 17:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "INFO   - 'selected'");
	                break;

	case 18:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "GUIDE   - 'selected'");
	                break;

	case 19:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "GAME   - 'selected'");
	                break;

	case 20:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "MEDIA   - 'selected'");
			break;
	case 21:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	         LOGD(LOGTAG, "MENU   - 'selected'");
	                break;

	case 22:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "EXIT   - 'selected'");
		                break;


	case 23:
			codeIndex = selection;
                        for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "UPARW   - 'selected'");
		                break;

	case 24:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "DWNARW   - 'selected'");
			break;

	case 25:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "RGTARW   - 'selected'");
	                break;

	case 26:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "LFTARW   - 'selected'");
	                break;

	case 27:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "RECALL   - 'selected'");
	                break;

	case 28:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "TV/RADIO   - 'selected'");
			break;
	case 29:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	         LOGD(LOGTAG, "RED   - 'selected'");
	                break;

	case 30:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "YELLOW   - 'selected'");
		                break;
       	case 31:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "BLUE   - 'selected'");
			break;

	case 32:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "GREEN   - 'selected'");
	                break;

	case 33:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "CH+   - 'selected'");
	                break;

	case 34:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        LOGD(LOGTAG, "CH-   - 'selected'");
	                break;

	case 35:
			codeIndex = selection;
			for(index = 0; index < 12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "VOL+   - 'selected'");
			break;
	case 36:
			codeIndex = selection;
	                for(index = 0; index < 12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	         LOGD(LOGTAG, "VOL-   - 'selected'");
	                break;

	case 37:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "REC   - 'selected'");
		                break;


	case 38:
			codeIndex = selection;
                        for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "STOP   - 'selected'");
		                break;

	case 39:
			codeIndex = selection;
			for(index =0; index <12; index++){
				key[index] = sun[selection-1][index];
			}
			LOGD(LOGTAG, "TMS   - 'selected'");
			break;

	case 40:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        	LOGD(LOGTAG, "SLOW   - 'selected'");
	                break;
	case 41:
			codeIndex = selection;
	                for(index =0; index <12; index++){
	                        key[index] = sun[selection-1][index];
	                }
	        	LOGD(LOGTAG, "SELECT   - 'selected'");
	                break;
       default:
		codeIndex = -1;
		printf("Invalid Choice\n");
		LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
		break;
	}
		}
if(codeIndex > 0){
		// Writing the Register Settings for TataSky (RC6-2-20)

		x[0] = UART_LCR;
		x[1] = 0;
	
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf("In first if case ... \n ");
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		LCR registers enable the DLL and DLH registers access --UART_LCR_DLAB
		enables the break_en bit  --UART_LCR_WLEN8 it breaks the bit down
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_DLAB |UART_LCR_WLEN8;
	
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		for theUART to transmit the break bit must be cleared , if I'm assuming that 0xbf will enable the access to CIR registers  
		*/

		x[0] = UART_LCR;
		x[1] = 0x00;


		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		MDR register will set the CIR mode for theUART 
		*/
		
		//TODO : Check write
		x[0] = UART_OMAP_MDR1;
		x[1] = UART_OMAP_MDR1_CIR_MODE;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, 0x5441, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRCLRFFO ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		the CFPS register must be set for the  carrier frequency 
		*/

		x[0] = UART_OMAP_CFPS;
		x[1] = UART_OMAP_CFPS_36;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/* 
		DLL andf DLH registers need to be cleared , just before the tx/rx fifo is enabled 
		*/ 

		x[0] = UART_DLL;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_DLM;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		MDR2 register needs to be set for the duty cycle in CIR mode  
		*/	
		x[0] = UART_OMAP_MDR2;
		x[1] = UART_MDR2_25;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		Enhanced fifo register-- it allows yu to set trigger level  
		enables acces to Fifo control register for setting the trigger level i.e. bitr 5 & 4 
		enables acces to IER register i.e bit 7 & 6 for CTS and RTS interrupt
		enables access to MCR register MODEM control register bit 7:5 the bits are used for accessing the trigger level specially bit no 6 
		*/

		x[0] = UART_EFR;
		x[1] = UART_EFR_ECB;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		the SCR=0x00 and TLR =0x00 to set the trigger values in FCR registers

		for the trigger level the Supplementary control register needs to be set , the Modem control register needs to be set for accessing the TLR regsiter 
		*/

		x[0] = UART_SCR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		for accessing the TLR registers we need to set the MCR - modem control register the bit no 6th should be 1 to access the TLR register
		 for using MCR regsiter I need to  change the LCR register to config mode A 
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_CONF_MODE_A;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_MCR;
		x[1] = UART_MCR_TCRTLR;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		writing onto the TLR register to get the trigger level on  FCR register ,
		TLR register is meant for DMA transmit we r doin it the irq way so 0x00
		*/

		x[0] = UART_TI752_TLR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		enabling the  transmit fifo and fifo trigger level in  fifo control register

		the transmit fifo  register is enabled

		the trigger level is set for  transmit 32 characters  

		*/

		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO |UART_FCR_CLEAR_XMIT;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_FCR;
		x[1] = UART_FCR_T_TRIG_00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_DLL;
		x[1] = 0x45;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		x[0] = UART_DLM;
		x[1] = 0x05;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		//	serial_out(UART_DLL , cfps & 0xff );

		//	serial_out(UART_DLM , cfps >> 8 );
		//	serial_out(UART_DLL , 0x00 );
		//	serial_out(UART_DLM , 0x00 );

		x[0] = UART_LCR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, 0x5439, &key );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSND ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	}
                   LOGD(LOGTAG, "IR Remote KEY FUNCTION selected    - 'completed successively'");

	return 1;
}

int inputfuncvideocon (int func, int handle, char *vendorname)
{
		char key[18] = {0x00,};
		int a[] = {0,};
		int z[] = {0,};
		int index = 1;
		int ioctl_res;
		int selection=0;
		int codeIndex;
		int x[2];
               char vc[20] = "videocon" ;
	   // handle = 33, func =1;

	LOGD(LOGTAG, " Starting input functions  - 'lukupIR Remote'");

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec handle=%d", handle);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_nec() vendorname = %s", vendorname);



		if (strncmp(vendorname, vc, 8) == 0)
		{

	selection = func;

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc and videocon.... func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc() and videocon..... vendorname = %s", vendorname);


	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "TV STNBI   - 'selected'");
			break;

	case 2:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "LUSYM   - 'selected'");
	                break;

	case 3:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "ACTIVE   - 'selected'");
	                break;

	case 4:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "RADIO   - 'selected'");
	                break;

	case 5:
			codeIndex = selection;
			for(index = 0; index < 18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "VOD   - 'selected'");
			break;
	case 6:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	         LOGD(LOGTAG, "MOSAIC   - 'selected'");
	                break;

	case 7:
				codeIndex = selection;
		                for(index = 0; index < 18; index++){
		                        key[index] = videocon[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY - X   - 'selected'");
		                break;


	case 8:
			codeIndex = selection;
                        for(index = 0; index < 18; index++){
		                        key[index] = videocon[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY-?   - 'selected'");
		                break;

	case 9:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "RWD   - 'selected'");
			break;

	case 10:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "FWD   - 'selected'");
	                break;

	case 11:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "FWDEND   - 'selected'");
	                break;

	case 12:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "RWDEND   - 'selected'");
	                break;

	case 13:
			codeIndex = selection;
			for(index = 0; index < 18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "BACK   - 'selected'");
			break;
	case 14:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-dot   - 'selected'");
	                break;

	case 15:
				codeIndex = selection;
		                for(index = 0; index < 18; index++){
		                        key[index] = videocon[selection-1][index];
		                }
		         LOGD(LOGTAG, "VOL+   - 'selected'");
		                break;
        case 16:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "VOL-   - 'selected'");
			break;

	case 17:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "CH+   - 'selected'");
	                break;

	case 18:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "CH-   - 'selected'");
	                break;

	case 19:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-i   - 'selected'");
	                break;

	case 20:
			codeIndex = selection;
			for(index = 0; index < 18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "MUTE   - 'selected'");
			break;
	case 21:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	         LOGD(LOGTAG, "SWAP   - 'selected'");
	                break;

	case 22:
				codeIndex = selection;
		                for(index = 0; index < 12; index++){
		                        key[index] = sun[selection-1][index];
		                }
		         LOGD(LOGTAG, "RED   - 'selected'");
		                break;


	case 23:
			codeIndex = selection;
                        for(index = 0; index < 18; index++){
		                        key[index] = videocon[selection-1][index];
		                }
		         LOGD(LOGTAG, "GREEN   - 'selected'");
		                break;

	case 24:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "YALLOW   - 'selected'");
			break;

	case 25:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "BLUE   - 'selected'");
	                break;

	case 26:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-ONE   - 'selected'");
	                break;

	case 27:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-TWO   - 'selected'");
	                break;

	case 28:
			codeIndex = selection;
			for(index = 0; index < 18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-THREE   - 'selected'");
			break;
	case 29:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY-FOUR   - 'selected'");
	                break;

	case 30:
				codeIndex = selection;
		                for(index = 0; index < 18; index++){
		                        key[index] = videocon[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY-FIVE   - 'selected'");
		                break;
       	case 31:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-SIX   - 'selected'");
			break;

	case 32:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-SEVEN   - 'selected'");
	                break;

	case 33:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-EIGHT   - 'selected'");
	                break;

	case 34:
			codeIndex = selection;
	                for(index =0; index <18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	        LOGD(LOGTAG, "KEY-NINE   - 'selected'");
	                break;

	case 35:
			codeIndex = selection;
			for(index = 0; index < 18; index++){
				key[index] = videocon[selection-1][index];
			}
			LOGD(LOGTAG, "KEY-ZERO   - 'selected'");
			break;
	case 36:
			codeIndex = selection;
	                for(index = 0; index < 18; index++){
	                        key[index] = videocon[selection-1][index];
	                }
	         LOGD(LOGTAG, "KEY - *   - 'selected'");
	                break;

	case 37:
				codeIndex = selection;
		                for(index = 0; index < 18; index++){
		                        key[index] = videocon[selection-1][index];
		                }
		         LOGD(LOGTAG, "KEY - #   - 'selected'");
		                break;
	case 38:
					codeIndex = selection;
			                for(index = 0; index < 18; index++){
			                        key[index] = videocon[selection-1][index];
			                }
			         LOGD(LOGTAG, "KEY - OK   - 'selected'");
			                break;

       default:
		codeIndex = -1;
		printf("Invalid Choice\n");
		LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
		break;
	}
		}
if(codeIndex > 0){
		// Writing the Register Settings for TataSky (RC6-2-20)

		x[0] = UART_LCR;
		x[1] = 0;
	
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf("In first if case ... \n ");
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		LCR registers enable the DLL and DLH registers access --UART_LCR_DLAB
		enables the break_en bit  --UART_LCR_WLEN8 it breaks the bit down
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_DLAB |UART_LCR_WLEN8;
	
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		for theUART to transmit the break bit must be cleared , if I'm assuming that 0xbf will enable the access to CIR registers  
		*/

		x[0] = UART_LCR;
		x[1] = 0x00;


		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		MDR register will set the CIR mode for theUART 
		*/
		
		//TODO : Check write
		x[0] = UART_OMAP_MDR1;
		x[1] = UART_OMAP_MDR1_CIR_MODE;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, 0x5441, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRCLRFFO ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		the CFPS register must be set for the  carrier frequency 
		*/

		x[0] = UART_OMAP_CFPS;
		x[1] = UART_OMAP_CFPS_36;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/* 
		DLL andf DLH registers need to be cleared , just before the tx/rx fifo is enabled 
		*/ 

		x[0] = UART_DLL;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_DLM;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		MDR2 register needs to be set for the duty cycle in CIR mode  
		*/	
		x[0] = UART_OMAP_MDR2;
		x[1] = UART_MDR2_25;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		Enhanced fifo register-- it allows yu to set trigger level  
		enables acces to Fifo control register for setting the trigger level i.e. bitr 5 & 4 
		enables acces to IER register i.e bit 7 & 6 for CTS and RTS interrupt
		enables access to MCR register MODEM control register bit 7:5 the bits are used for accessing the trigger level specially bit no 6 
		*/

		x[0] = UART_EFR;
		x[1] = UART_EFR_ECB;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		the SCR=0x00 and TLR =0x00 to set the trigger values in FCR registers

		for the trigger level the Supplementary control register needs to be set , the Modem control register needs to be set for accessing the TLR regsiter 
		*/

		x[0] = UART_SCR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		for accessing the TLR registers we need to set the MCR - modem control register the bit no 6th should be 1 to access the TLR register
		 for using MCR regsiter I need to  change the LCR register to config mode A 
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_CONF_MODE_A;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_MCR;
		x[1] = UART_MCR_TCRTLR;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		writing onto the TLR register to get the trigger level on  FCR register ,
		TLR register is meant for DMA transmit we r doin it the irq way so 0x00
		*/

		x[0] = UART_TI752_TLR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		enabling the  transmit fifo and fifo trigger level in  fifo control register

		the transmit fifo  register is enabled

		the trigger level is set for  transmit 32 characters  

		*/

		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO |UART_FCR_CLEAR_XMIT;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_FCR;
		x[1] = UART_FCR_T_TRIG_00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_DLL;
		x[1] = 0x90;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		x[0] = UART_DLM;
		x[1] = 0x06;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		//	serial_out(UART_DLL , cfps & 0xff );

		//	serial_out(UART_DLM , cfps >> 8 );
		//	serial_out(UART_DLL , 0x00 );
		//	serial_out(UART_DLM , 0x00 );

		x[0] = UART_LCR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, 0x5439, &key );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSND ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	}
                  LOGD(LOGTAG, "IR Remote KEY FUNCTION selected    - 'completed successively'");

	return 1;
}

int inputfuncbroadcom (int func, int handle, char *vendorname)
{
		char key[18] = {0x00,};
		int a[] = {0,};
		int z[] = {0,};
		int index = 1;
		int ioctl_res;
		int selection=0;
		int codeIndex;
		int x[2];
               char bc[20] = "broadcom" ;
	   // handle = 33, func =1;

	LOGD(LOGTAG, " Starting input functions  - 'lukupIR Remote'");

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec handle=%d", handle);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc_nec func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc_nec() vendorname = %s", vendorname);



		if (strncmp(vendorname, bc, 8) == 0)
		{

	selection = func;

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "inputfunc and broadcom.... func=%d", func);
	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in inputfunc() and broadcom..... vendorname = %s", vendorname);


	switch(selection){
	case 1:
			codeIndex = selection;
			for(index =0; index <18; index++){
				key[index] = broadcom[selection-1][index];
			}
			LOGD(LOGTAG, "MP PWR OFF   - 'selected'");
			break;
                default:
		codeIndex = -1;
		printf("Invalid Choice\n");
		LOGD(LOGTAG, " Invalid choice of input functions  - 'lukupIR Remote'");
		break;
 }
		}
if(codeIndex > 0){
		

		x[0] = UART_LCR;
		x[1] = 0;
	
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf("In first if case ... \n ");
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		LCR registers enable the DLL and DLH registers access --UART_LCR_DLAB
		enables the break_en bit  --UART_LCR_WLEN8 it breaks the bit down
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_DLAB |UART_LCR_WLEN8;
	
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		for theUART to transmit the break bit must be cleared , if I'm assuming that 0xbf will enable the access to CIR registers  
		*/

		x[0] = UART_LCR;
		x[1] = 0x00;


		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		MDR register will set the CIR mode for theUART 
		*/
		
		//TODO : Check write
		x[0] = UART_OMAP_MDR1;
		x[1] = UART_OMAP_MDR1_CIR_MODE;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, 0x5441, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRCLRFFO ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		the CFPS register must be set for the  carrier frequency 
		*/

		x[0] = UART_OMAP_CFPS;
		x[1] = UART_OMAP_CFPS_36;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/* 
		DLL andf DLH registers need to be cleared , just before the tx/rx fifo is enabled 
		*/ 

		x[0] = UART_DLL;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_DLM;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		MDR2 register needs to be set for the duty cycle in CIR mode  
		*/	
		x[0] = UART_OMAP_MDR2;
		x[1] = UART_MDR2_25;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		Enhanced fifo register-- it allows yu to set trigger level  
		enables acces to Fifo control register for setting the trigger level i.e. bitr 5 & 4 
		enables acces to IER register i.e bit 7 & 6 for CTS and RTS interrupt
		enables access to MCR register MODEM control register bit 7:5 the bits are used for accessing the trigger level specially bit no 6 
		*/

		x[0] = UART_EFR;
		x[1] = UART_EFR_ECB;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		
		/*
		the SCR=0x00 and TLR =0x00 to set the trigger values in FCR registers

		for the trigger level the Supplementary control register needs to be set , the Modem control register needs to be set for accessing the TLR regsiter 
		*/

		x[0] = UART_SCR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		for accessing the TLR registers we need to set the MCR - modem control register the bit no 6th should be 1 to access the TLR register
		 for using MCR regsiter I need to  change the LCR register to config mode A 
		*/

		x[0] = UART_LCR;
		x[1] = UART_LCR_CONF_MODE_A;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_MCR;
		x[1] = UART_MCR_TCRTLR;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		/*
		writing onto the TLR register to get the trigger level on  FCR register ,
		TLR register is meant for DMA transmit we r doin it the irq way so 0x00
		*/

		x[0] = UART_TI752_TLR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		/*
		enabling the  transmit fifo and fifo trigger level in  fifo control register

		the transmit fifo  register is enabled

		the trigger level is set for  transmit 32 characters  

		*/

		x[0] = UART_FCR;
		x[1] = UART_FCR_ENABLE_FIFO |UART_FCR_CLEAR_XMIT;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_FCR;
		x[1] = UART_FCR_T_TRIG_00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	
		x[0] = UART_DLL;
		x[1] = 0x99;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		x[0] = UART_DLM;
		x[1] = 0x06;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
		//	serial_out(UART_DLL , cfps & 0xff );

		//	serial_out(UART_DLM , cfps >> 8 );
		//	serial_out(UART_DLL , 0x00 );
		//	serial_out(UART_DLM , 0x00 );

		x[0] = UART_LCR;
		x[1] = 0x00;
	
		ioctl_res = ioctl( handle, 0x5438, &x );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSETREG ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}

		ioctl_res = ioctl( handle, 0x5439, &key );
		if (ioctl_res < 0)
		{
			printf( "Failed TIOCIRSND ioctl: error %d (%s)\n", errno, strerror(errno) );
			close( handle );
			return -1;
		}
	}
                  LOGD(LOGTAG, "IR Remote KEY FUNCTION selected    - 'completed successively'");

	return 1;
}
int stop(int handle)
{
	//int handle;
	handle = close(handle);              //"/dev/ttyO4");

	__android_log_print(ANDROID_LOG_INFO, "LOGTAG", "in stop() handle=%d", handle);
			if (handle <= 0)
			{
				printf( "Failed to close %s - %d (%s)\n", "/dev/ttyO4", errno, strerror(errno) );
				LOGD(LOGTAG, "Cannot close device - '/dev/ttyO4'");

				return -1;
			}
			LOGD(LOGTAG, " Close device done  - '/dev/ttyO4'");
	return 1;
}

