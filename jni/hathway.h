

#define POWER 		1                     
#define MUTE 		2
#define CHANNELUP 	3
#define CHANNELDOWN 	4
#define	VOLUMEUP 	5
#define VOLUMEDOWN 	6
#define PROMO 		7
#define RS 		8
#define FAVOURITE 	9
#define BLANK 		10
#define MESSAGE		11
#define LANGUAGE	12
#define INTERACTIVE	13
#define GAMES 		14 
#define BACK 		15 
#define INFO 		16 
#define EPG 		17 
#define MENU 		18
#define OK 		19
#define ARROWUP 	20
#define ARROWDWN 	21
#define ARROWRGT 	22
#define ARROWLFT 	23
#define RADIO 		24
#define HELP 		25
#define BUT0 		26
#define KEY_ONE 	27
#define KEY_TWO 	28
#define KEY_THREE 	29
#define KEY_FOUR 	30
#define KEY_FIVE 	31
#define KEY_SIX 	32
#define KEY_SEVEN 	33
#define KEY_EIGHT 	34
#define KEY_NINE 	35
#define RED 		36
#define GREEN 		37
#define YELLOW 		38
#define BLUE 		39
                       

#if 0
char hathway[38][5] = {
                       //{0x15, 0x0a, 0x08, 0xf7, 0xb3, 0x4c}, // power ON button
                       //{0x15, 0x0a, 0x08, 0xf7, 0x93, 0x6c}, // MUTE button
			 
			{0xf7, 0x80, 0xbf, 0x3b, 0xc4}, // power ON button 
			//562.5×(24+16×4+16×2) = 67.5 mS.
			{0x0f, 0x08, 0xfb, 0xb3, 0x4c}, // power ON button 
			{ 0x80, 0xff, 0xff, 0x00, 0x00, 0x04, 0xfb, 0xb3, 0x4c, 0x01}, // power ON button
			//{0xff, 0xff, 0x00, 0x80, 0x7f, 0x3b, 0xc4, 0x01, 0x00, 0x0}, // power ON button 
			{ 0xff, 0xff, 0x00, 0x00, 0x04, 0xfb, 0xb3, 0x4c, 0x01, 0x00}, // power ON button
			//{0xff, 0xff, 0x00, 0x08, 0xf7, 0x93, 0x6c, 0x01, 0x00, 0x0}, // MUTE button
			{0x08, 0xf7, 0x94, 0x6b}, // Key-ONE button
                       {0x08, 0xf7, 0x9c, 0x63}, // key-TWO button
                       {0x08, 0xf7, 0x33, 0xcc}, // key-three button
                       {0x08, 0xf7, 0x17, 0xe8}, // key-four button
                       {0x08, 0xf7, 0x1f, 0xe0}, // key-five button
                       {0x08, 0xf7, 0x31, 0xce}, // key-six button
                       {0x08, 0xf7, 0x15, 0xea}, // key-seven button
                       {0x08, 0xf7, 0x1d, 0xe2}, // key-eight button
                       {0x08, 0xf7, 0x32, 0xcd}, // key-nine button
                       {0x08, 0xf7, 0x1e, 0xe1}, // key-zero button
                       {0x08, 0xf7, 0x1a, 0xe5}, // key-help button
                       {0x08, 0xf7, 0x18, 0xe7}, // key-menu button
                       {0x08, 0xf7, 0x16, 0xe9}, // key-EPG button
                       {0x08, 0xf7, 0x35, 0xca}, // key-up button
                       {0x08, 0xf7, 0xb4, 0x4b}, // key-down button
                       {0x08, 0xf7, 0x99, 0x66}, // key-left button
                       {0x08, 0xf7, 0x38, 0xc7}, // key-right button
                       {0x08, 0xf7, 0x14, 0xed}, // key-info button
                       {0x08, 0xf7, 0x34, 0xcd}, // key-back button
                       {0x08, 0xf7, 0x95, 0x6a}, // key-game button
                       {0x08, 0xf7, 0x9d, 0x62}, // key-interactive button
                       {0x08, 0xf7, 0x9b, 0x64}, // key-language button
                       {0x08, 0xf7, 0x97, 0x68}, // key-messages button
                       {0x08, 0xf7, 0x1c, 0xe3}, // key-account(red) button
                       {0x08, 0xf7, 0xb5, 0x4a}, // key-green  button
                       {0x08, 0xf7, 0x3b, 0xc4}, // key-yellow button
                       {0x08, 0xf7, 0x30, 0xcf}, // key-blue button
                       {0x08, 0xf7, 0x3c, 0xc3}, // key-Rs button
                       {0x08, 0xf7, 0x39, 0xc6}, // key-Promo-ch button
                       {0x08, 0xf7, 0xbb, 0x44}, // key-VOL+ button
                       {0x08, 0xf7, 0x91, 0x6e}, // key-CH+ button
                       {0x08, 0xf7, 0x3a, 0xc5}, // key-FAV button
                       {0x08, 0xf7, 0x36, 0xc9}, // key-Blank button
                       {0x08, 0xf7, 0x13, 0xec}, // key-VOL- button
                       {0x08, 0xf7, 0x9e, 0x61}   // key-CH- button
};
//{0x08, 0xff, 0xff, 0x00, 0x01, 0xf7, 0xb3, 0x4c }, // power ON button
                       //{0x08, 0xff, 0xff, 0x00, 0x95, 0x55, 0x4a, 0x49, 0x22, 0xa9, 0x25, 0x24, 0x95, 0x4a }, // power ON button
                       
                       //{0x08, 0xff, 0xff, 0x00, 0xa8, 0xaa, 0xa2, 0x88, 0x88, 0x88, 0x8a, 0x88, 0xa8, 0x22, 0x22, 0x8a, 0xa0 }, // power ON button
//{0x08, 0xff, 0xff, 0x00, 0x82, 0x22, 0x22, 0x22, 0x82, 0x88, 0x88, 0x88, 0x22, 0x88, 0x82, 0x88, 0x88, 0x22, 0x28, 0x22 }, // power ON button


{0x09, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11} ---> common code for all Hathway commands



#endif


#if 1
char hathway[40][18] = {
		       //{0x08, 0xf7, 0xb3, 0x4c, 0x01, 0x00, 0x00, 0x00, 0x00}, // power ON button
                       //{0x80, 0x7f, 0x3b, 0xc4, 0x01, 0x00, 0x00, 0x00, 0x00}, // power ON button
                       
                       
                       
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x15, 0x11, 0x45, 0x44, 0x54, 0x51, 0x01, 0x00 },//pwr                        
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x15, 0x11, 0x15, 0x11, 0x55, 0x44, 0x01, 0x00}, // MUTE button
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x55, 0x44, 0x45, 0x44, 0x54, 0x44, 0x01, 0x00}, //Chn.up But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x11, 0x51, 0x54, 0x54, 0x51, 0x44, 0x01, 0x00}, //Chn.dwn But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x44, 0x14, 0x51, 0x54, 0x51, 0x01, 0x00}, //VOLUP But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x15, 0x51, 0x45, 0x44, 0x45, 0x44, 0x01, 0x00}, //VOLDOWN But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x51, 0x11, 0x45, 0x14, 0x51, 0x01, 0x00}, //Promo. But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x11, 0x55, 0x11, 0x15, 0x11, 0x51, 0x01, 0x00}, //Rs. But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x54, 0x11, 0x45, 0x11, 0x51, 0x01, 0x00}, //FAV But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x54, 0x11, 0x51, 0x11, 0x51, 0x01, 0x00}, //BLANK But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x44, 0x54, 0x44, 0x55, 0x44, 0x01, 0x00}, //MSG But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x44, 0x54, 0x14, 0x55, 0x44, 0x01, 0x00}, //LNG But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x11, 0x45, 0x54, 0x54, 0x54, 0x44, 0x01, 0x00}, //INT But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x11, 0x15, 0x51, 0x54, 0x44, 0x01, 0x00}, //GAMES But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x55, 0x44, 0x14, 0x11, 0x51, 0x01, 0x00}, //BACK But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x55, 0x11, 0x45, 0x44, 0x44, 0x01, 0x00}, //INFO But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x54, 0x45, 0x54, 0x44, 0x44, 0x01, 0x00}, //EPG But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x55, 0x54, 0x11, 0x11, 0x45, 0x44, 0x01, 0x00}, //MENU But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x44, 0x45, 0x44, 0x15, 0x51, 0x01, 0x00}, //OK But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x51, 0x11, 0x51, 0x14, 0x51, 0x01, 0x00}, //ARWUP But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x45, 0x11, 0x51, 0x44, 0x51, 0x01, 0x00}, //ARWDWN But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x55, 0x44, 0x11, 0x11, 0x51, 0x01, 0x00}, //ARWRGT But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x11, 0x15, 0x45, 0x54, 0x44, 0x01, 0x00}, //ARWLFT But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x55, 0x51, 0x44, 0x44, 0x44, 0x01, 0x00}, //RADIO But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x54, 0x45, 0x51, 0x44, 0x44, 0x01, 0x00}, //HELP But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x11, 0x51, 0x15, 0x55, 0x44, 0x44, 0x01, 0x00}, //BUT0 But
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x45, 0x45, 0x14, 0x51, 0x44, 0x01, 0x00}, //1st But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x11, 0x15, 0x15, 0x15, 0x51, 0x44, 0x01, 0x00}, //2nd But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x15, 0x51, 0x11, 0x11, 0x15, 0x51, 0x01, 0x00}, //3rd But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x45, 0x45, 0x14, 0x51, 0x44, 0x01, 0x00}, //4th But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x11, 0x11, 0x55, 0x54, 0x45, 0x44, 0x01, 0x00}, //5th But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x55, 0x54, 0x44, 0x44, 0x14, 0x51, 0x01, 0x00}, //6th But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x51, 0x45, 0x14, 0x45, 0x44, 0x01, 0x00}, //7th But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x11, 0x45, 0x15, 0x15, 0x45, 0x44, 0x01, 0x00}, //8th But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x15, 0x55, 0x44, 0x44, 0x11, 0x51, 0x01, 0x00}, //9th But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x11, 0x55, 0x45, 0x45, 0x44, 0x44, 0x01, 0x00}, //RED But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x45, 0x11, 0x45, 0x44, 0x51, 0x51, 0x01, 0x00}, //GREEN But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x51, 0x44, 0x45, 0x14, 0x15, 0x51, 0x01, 0x00}, //YELLOW But.
                       {0x11, 0xff, 0xff, 0x00, 0x51, 0x55, 0x45,  0x11, 0x11, 0x11, 0x55, 0x44, 0x45, 0x44, 0x54, 0x44, 0x01, 0x00}, //BLUE But.
                       
                      // {0x08, 0xff, 0xff, 0x3f, 0x00, 0x95, 0xac, 0xaa, 0xaa, 0x80, 0x7f, 0x3b, 0xc4 }, 
                       //{0xf0, 0x80, 0x7f, 0x3b, 0xc4}, // power ON button
                       
                       
                       
                       //{ 0x80, 0xff, 0xff, 0x00, 0x00, 0x04, 0xfb, 0xb3, 0x4c}, // power ON button
		       
		       //{ 0xff, 0xff, 0x00, 0x00, 0x04, 0xfb, 0xb3, 0x4c, 0x01}, // power ON button
                       
         /*              
                       
                       {0x08, 0xf7, 0xb3, 0x4c}, // power ON button
                       {0x80, 0x7f, 0x3b, 0xc4}, // power ON button
                       
                       
                       
                       
                       {0x80, 0x7f, 0x39, 0xc6}, // MUTE button
                       {0x80, 0x7f, 0x49, 0xB6}, // Key-ONE button
                       {0x80, 0x7f, 0xc9, 0x36}, // key-TWO button
                       {0x80, 0x7f, 0x33, 0xcc}, // key-three button
                       {0x80, 0x7f, 0x71, 0x8e}, // key-four button
                       {0x80, 0x7f, 0xf1, 0x0e}, // key-five button
                       {0x80, 0x7f, 0x13, 0xec}, // key-six button
                       {0x80, 0x7f, 0x51, 0xae}, // key-seven button
                       {0x80, 0x7f, 0xd1, 0x2e}, // key-eight button
                       {0x80, 0x7f, 0x23, 0xdc}, // key-nine button
                       {0x80, 0x7f, 0xe1, 0x1e}, // key-zero button
                       {0x80, 0x7f, 0xa1, 0x5e}, // key-help button
                       {0x80, 0x7f, 0x81, 0x7e}, // key-menu button
                       {0x80, 0x7f, 0x61, 0x9e}, // key-EPG button
                       {0x80, 0x7f, 0x53, 0xac}, // key-up button
                       {0x80, 0x7f, 0x4b, 0xb4}, // key-down button
                       {0x80, 0x7f, 0x99, 0x66}, // key-left button
                       {0x80, 0x7f, 0x83, 0x7c}, // key-right button
                       {0x80, 0x7f, 0x41, 0xde}, // key-info button
                       {0x80, 0x7f, 0x43, 0xdc}, // key-back button
                       {0x80, 0x7f, 0x59, 0xa6}, // key-game button
                       {0x80, 0x7f, 0xd9, 0x26}, // key-interactive button
                       {0x80, 0x7f, 0xb9, 0x46}, // key-language button
                       {0x80, 0x7f, 0x79, 0x86}, // key-messages button
                       {0x80, 0x7f, 0xc1, 0x3e}, // key-account(red) button
                       {0x80, 0x7f, 0x5b, 0xa4}, // key-green  button
                       {0x80, 0x7f, 0xb3, 0x4c}, // key-yellow button
                       {0x80, 0x7f, 0x03, 0xfc}, // key-blue button
                       {0x80, 0x7f, 0xc3, 0x3c}, // key-Rs button
                       {0x80, 0x7f, 0x93, 0x6c}, // key-Promo-ch button
                       {0x80, 0x7f, 0xbb, 0x44}, // key-VOL+ button
                       {0x80, 0x7f, 0x19, 0xe6}, // key-CH+ button
                       {0x80, 0x7f, 0xa3, 0x5c}, // key-FAV button
                       {0x80, 0x7f, 0x63, 0x9c}, // key-Blank button
                       {0x80, 0x7f, 0x31, 0xce}, // key-VOL- button
                       {0x80, 0x7f, 0xe9, 0x16}  // key-CH- button
*/

};
#endif
