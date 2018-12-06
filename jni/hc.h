

#define POWER  		1
#define RECORD 		2
#define PAUSE  		3
#define ASP.  		4
#define PLAY  		5
#define STOP  		6
#define REWIND  	7
#define FORWARD  	8
#define REWINDEND  	9
#define FORWARDEND 	10
#define CHANNELUP  	11
#define CHANNELDOWN  	12
#define VOLUMEDOWN  	13
#define VOLUMEUP  	14
#define KEY_OK  	15
#define MENU  		16
#define EXIT  		17
#define SUBT  		18
#define INFO  		19
#define TTX  		20
#define PVR  		21
#define MALL  		22
#define FORMAT  	23
#define AUDIO  		24
#define KEY_ONE  	25
#define KEY_TWO  	26
#define KEY_THREE  	27
#define KEY_FOUR  	28
#define KEY_FIVE  	29
#define KEY_SIX  	30
#define KEY_SEVEN  	31
#define KEY_EIGHT  	32
#define KEY_NINE  	33
#define KEY_ZERO  	34
#define BACK  		35
#define TVRADIO  	36
#define MUTE  		37
#define FAVOURITE  	38
#define EPG  		39



char homecable[42][18] = {

		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x54, 0x51, 0x51, 0x44, 0x14, 0x01, 0x00}, //PWR button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x15, 0x45, 0x15, 0x51, 0x14, 0x11, 0x01, 0x00}, //REC button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x51, 0x51, 0x14, 0x45, 0x14, 0x01, 0x00}, //PAUSE button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x15, 0x11, 0x55, 0x44, 0x15, 0x11, 0x01, 0x00}, //ASP. button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x51, 0x51, 0x11, 0x45, 0x14, 0x01, 0x00}, //PLAY button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x44, 0x45, 0x51, 0x45, 0x14, 0x01, 0x00}, //STOP button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x45, 0x51, 0x14, 0x51, 0x14, 0x01, 0x00}, //REW button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x15, 0x45, 0x15, 0x51, 0x14, 0x01, 0x00}, //FWD button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x45, 0x51, 0x11, 0x51, 0x14, 0x01, 0x00}, //REW END button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x55, 0x51, 0x14, 0x11, 0x51, 0x14, 0x01, 0x00}, //FWD END button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x55, 0x55, 0x11, 0x11, 0x11, 0x01, 0x00}, //ARRW UP(CH+) button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x55, 0x45, 0x11, 0x11, 0x11, 0x01, 0x00}, //ARRW DWN(CH-) button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x44, 0x55, 0x54, 0x11, 0x11, 0x01, 0x00}, //ARRW LFT(VOL-) button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x11, 0x55, 0x14, 0x15, 0x11, 0x01, 0x00}, //ARRW RGHT(VOL+) button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x51, 0x55, 0x15, 0x11, 0x11, 0x01, 0x00}, //OK button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x11, 0x15, 0x55, 0x45, 0x14, 0x01, 0x00}, //MENU button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x15, 0x11, 0x45, 0x11, 0x55, 0x14, 0x01, 0x00}, //EXIT button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x15, 0x55, 0x45, 0x14, 0x11, 0x01, 0x00}, //SUBT button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x55, 0x51, 0x45, 0x44, 0x14, 0x11, 0x01, 0x00}, //INFO button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x54, 0x15, 0x15, 0x11, 0x11, 0x01, 0x00}, //TTX button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x55, 0x55, 0x11, 0x11, 0x11, 0x11, 0x01, 0x00}, //PVR button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x55, 0x54, 0x45, 0x44, 0x11, 0x11, 0x01, 0x00}, //MALL button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x51, 0x15, 0x45, 0x11, 0x11, 0x01, 0x00}, //FORMAT button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x15, 0x51, 0x15, 0x51, 0x11, 0x11, 0x01, 0x00}, //AUDIO button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x51, 0x55, 0x44, 0x11, 0x11, 0x01, 0x00}, //KEY ONE button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x44, 0x54, 0x45, 0x15, 0x11, 0x01, 0x00}, //KEY TWO button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x11, 0x51, 0x55, 0x15, 0x11, 0x01, 0x00}, //KEY THREE button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x44, 0x55, 0x51, 0x11, 0x11, 0x01, 0x00}, //KEY FOUR button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x11, 0x55, 0x11, 0x15, 0x11, 0x01, 0x00}, //KEY FIVE button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x45, 0x54, 0x15, 0x15, 0x11, 0x01, 0x00}, //KEY SIX button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x45, 0x55, 0x44, 0x14, 0x11, 0x01, 0x00}, //KEY SEVEN button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x14, 0x55, 0x51, 0x14, 0x11, 0x01, 0x00}, //KEY EIGHT button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x51, 0x54, 0x55, 0x14, 0x11, 0x01, 0x00}, //KEY NINE button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x45, 0x15, 0x45, 0x14, 0x11, 0x01, 0x00}, //KEY ZERO button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x45, 0x14, 0x45, 0x51, 0x51, 0x14, 0x01, 0x00}, //BACK button
		{0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x11, 0x51, 0x14, 0x55, 0x51, 0x14, 0x01, 0x00}, //TV/RADIO button
                {0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x51, 0x55, 0x54, 0x44, 0x44, 0x14, 0x01, 0x00},  // MUTE
                {0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x55, 0x44, 0x15, 0x11, 0x15, 0x11, 0x01, 0x00},  // FAV
                {0x11, 0xff, 0xff, 0x00, 0x55, 0x14, 0x15,  0x11, 0x45, 0x11, 0x55, 0x54, 0x14, 0x11, 0x45, 0x14, 0x01, 0x00}  // EPG
		
		
		
};