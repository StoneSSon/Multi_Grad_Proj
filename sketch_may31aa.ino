#include "Adafruit_WS2801.h"
#include "SPI.h" // Comment out this line if using Trinket or Gemma
#ifdef __AVR_ATtiny85__
 #include <avr/power.h>
#endif

/*****************************************************************************
Example sketch for driving Adafruit WS2801 pixels!


  Designed specifically to work with the Adafruit RGB Pixels!
  12mm Bullet shape ----> https://www.adafruit.com/products/322
  12mm Flat shape   ----> https://www.adafruit.com/products/738
  36mm Square shape ----> https://www.adafruit.com/products/683

  These pixels use SPI to transmit the color data, and have built in
  high speed PWM drivers for 24 bit color per pixel
  2 pins are required to interface

  Adafruit invests time and resources providing this open source code, 
  please support Adafruit and open-source hardware by purchasing 
  products from Adafruit!

  Written by Limor Fried/Ladyada for Adafruit Industries.  
  BSD license, all text above must be included in any redistribution

*****************************************************************************/

// Choose which 2 pins you will use for output.
// Can be any valid output pins.
// The colors of the wires may be totally different so
// BE SURE TO CHECK YOUR PIXELS TO SEE WHICH WIRES TO USE!
uint8_t dataPin  = 2;    // Yellow wire on Adafruit Pixels
uint8_t clockPin = 3;    // Green wire on Adafruit Pixels

// Don't forget to connect the ground wire to Arduino ground,
// and the +5V wire to a +5V supply

//LED strip객체 생성. LED의 개수에 따라 앞 인자값을 변경한다
Adafruit_WS2801 strip = Adafruit_WS2801(32, dataPin, clockPin);

// Optional: leave off pin numbers to use hardware SPI
// (pinout is then specific to each board and can't be changed)
//Adafruit_WS2801 strip = Adafruit_WS2801(25);

// For 36mm LED pixels: these pixels internally represent color in a
// different format.  Either of the above constructors can accept an
// optional extra parameter: WS2801_RGB is 'conventional' RGB order
// WS2801_GRB is the GRB order required by the 36mm pixels.  Other
// than this parameter, your code does not need to do anything different;
// the library will handle the format change.  Examples:
//Adafruit_WS2801 strip = Adafruit_WS2801(25, dataPin, clockPin, WS2801_GRB);
//Adafruit_WS2801 strip = Adafruit_WS2801(25, WS2801_GRB);

void setup() {
#if defined(__AVR_ATtiny85__) && (F_CPU == 16000000L)
  clock_prescale_set(clock_div_1); //clock을 16Mhz로 사용
#endif

  strip.begin();

  //LEDstrip loop를 실행시키기 위해 모든 LED를 off시키며 초기화
  strip.show();
    Serial.setTimeout(5);
    Serial.begin(9600);
}





//rbg
void loop() {
  //rainbow(0);
  Volume();
   
   
   /*
   strip.setPixelColor(0, Color(255,0,0));   
   strip.setPixelColor(1, Color(255,0,0));
   strip.setPixelColor(2, Color(255,0,0));
   strip.setPixelColor(3, Color(0,255,0));
   strip.setPixelColor(4, Color(0,0,255));
   strip.show();
  */

}

void Volume(){
   String string;
  
   string = Serial.readStringUntil('\n');
   String idx = string.substring(0, 2);
   String r = string.substring(2,4);   
   String g = string.substring(4,6);
   String b = string.substring(6,8);
   int rInt = hex_to_ascii(r[0],r[1]);
   int bInt = hex_to_ascii(b[0],b[1]);
   int gInt = hex_to_ascii(g[0],g[1]);
   int iInt = hex_to_ascii(idx[0], idx[1]);

    Serial.println(string);
   
    strip.setPixelColor(iInt, Color(rInt,bInt,gInt));   //R
    strip.show();
    delay(0); 
}





int hex_to_int(char c){
  int first;
  int second;
  int value;
 
  if (c >= 97) {
    c -= 32;
  }
  first = c / 16 - 3;
  second = c % 16;
  value = first * 10 + second;
  if (value > 9) {
    value--;
  }
  return value;
}


int hex_to_ascii(char c, char d){
  int high = hex_to_int(c) * 16;
  int low = hex_to_int(d);
  return high+low;
}

//RBG



void rainbow(uint8_t wait) {
  int i, j;
   
  for (j=0; j < 256; j++) { //LEDstrip의 모든 LED pixel을 256가지의 색으로 한번 반복한다
    for (i=0; i < strip.numPixels(); i++) {
      strip.setPixelColor(i, 0,255,0);
    }  
    strip.show(); 
    delay(wait);
  }
}


void rainbowCycle(uint8_t wait) {
  int i, j;
  
  for (j=0; j < 256 * 5; j++) {     // LED각각을 각기 다른색으로 시작해서 256가지의 색으로 5번 반복한다
    for (i=0; i < strip.numPixels(); i++) {
      strip.setPixelColor(i, Wheel( ((i * 256 / strip.numPixels()) + j) % 256) );
    }  
    strip.show();  
    delay(wait);
  }
}

//LED pixel을 입력한 색으로 차례대로 나타낸다
void colorWipe(uint32_t c, uint8_t wait) {
  int i;
  
  for (i=0; i < strip.numPixels(); i++) {
      strip.setPixelColor(i, c);
      strip.show();
      delay(wait);
  }
}

//8비트의 r값 g값 b값을 받아서 여러가지의 색을 만드는 함수
/*
uint32_t Color(byte r, byte g, byte b)
{
  uint32_t c;
  c = r;
  c <<= 8;
  c |= g;
  c <<= 8;
  c |= b;
  return c;
}
*/

uint32_t Color(byte r, byte g, byte b)
{
  uint32_t c;
  c = r;
  c <<= 8;
  c |= g;
  c <<= 8;
  c |= b;
  return c;
}
//256이하의 숫자를 받았을 때 정해진 조건에 따라 특정색깔로 분리하여 나타내는 함수 
uint32_t Wheel(byte WheelPos)
{
  if (WheelPos < 85) {
   return Color(WheelPos * 3, 255 - WheelPos * 3, 0);
  } else if (WheelPos < 170) {
   WheelPos -= 85;
   return Color(255 - WheelPos * 3, 0, WheelPos * 3);
  } else {
   WheelPos -= 170; 
   return Color(0, WheelPos * 3, 255 - WheelPos * 3);
  }
}
