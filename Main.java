package TaaT;

 

import java.awt.*;
import java.awt.image.*;
import java.util.Random;
//import processing.serial.*;
import processing.serial.Serial;
import processing.core.PApplet;
import processing.core.PImage;

 

public class Main extends PApplet {


   static final short minBrightness = 120;
   static final short fade = 75;
   static final int pixelSize = 20;
   static final boolean useFullScreenCaps = true;
   static final int timeout = 5000; // 5 seconds
   static final int displays[][] = new int[][] { { 0, 11, 7 } };
   static final int leds[][] = new int[][] { 
	   
	   {0,0,6}, {0,1,6} , {0,2,6}, {0,3,6}, {0,4,6}, {0,5,6}, {0,6,6}, {0,7,6}, {0,8,6},
	   {0,9,6},  {0,10,6},//
	   
	   {0,10,5}, {0,10,4}, {0,10,3}, {0,10,2}, {0,10,1}, {0,10,0},//
	   
	   {0,9,0}, {0,8,0}, {0,7,0}, {0,6,0}, {0,5,0}, {0,4,0}, {0,3,0}, {0,2,0}, {0,1,0}, {0,0,0},//
	   
	   {0,0,1},  {0,0,2},  {0,0,3},  {0,0,4},  {0,0,5}
	   
   };

 

   byte[] serialData = new byte[6 + leds.length * 3];
   short[][] ledColor = new short[leds.length][3], prevColor = new short[leds.length][3];
   byte[][] gamma = new byte[256][3];
   int nDisplays = displays.length;
   Robot[] bot = new Robot[displays.length];
   Rectangle[] dispBounds = new Rectangle[displays.length], ledBounds; // Alloc'd only if per-LED captures
   int[][] pixelOffset = new int[leds.length][256], screenData; // Alloc'd only if full-screen captures
   PImage[] preview = new PImage[displays.length];

   Serial port;

   Main dh; // For disabling LEDs on exit

 

   public void setup() {

      GraphicsEnvironment ge;

      GraphicsConfiguration[] gc;

      GraphicsDevice[] gd;

      int d, i, totalWidth, maxHeight, row, col, rowOffset;

      int[] x = new int[16], y = new int[16];

      float f, range, step, start;

      

      // dh = new Main(this); // Init DisposeHandler ASAP --주석2

      // port = new Serial(this, Serial.list()[2], 115200);21 --주석1

 

      dispBounds = new Rectangle[displays.length];

      if (useFullScreenCaps) {

         screenData = new int[displays.length][];

      } else {

 

         ledBounds = new Rectangle[leds.length];

      }

 

      ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

      gd = ge.getScreenDevices();

      if (nDisplays > gd.length)

         nDisplays = gd.length;

      totalWidth = 0;

      maxHeight = 0;

      for (d = 0; d < nDisplays; d++) { // For each display...

         try {

            bot[d] = new Robot(gd[displays[d][0]]);

         } catch (AWTException e) {

            System.out.println("new Robot() failed");

            continue;

         }

 

         gc = gd[displays[d][0]].getConfigurations();

         dispBounds[d] = gc[0].getBounds();

         dispBounds[d].x = dispBounds[d].y = 0;

         preview[d] = createImage(displays[d][1], displays[d][2], RGB);

         preview[d].loadPixels();

         totalWidth += displays[d][1];

         if (d > 0)

            totalWidth++;

 

         if (displays[d][2] > maxHeight)

            maxHeight = displays[d][2];

      }

 

      for (i = 0; i < leds.length; i++) { // For each LED...

 

         d = leds[i][0];

         range = (float) dispBounds[d].width / (float) displays[d][1];

         step = (float) (range / 16.0);

         start = (float) (range * (float) leds[i][1] + step * 0.5);

         for (col = 0; col < 16; col++)

            x[col] = (int) (start + step * (float) col);

         range = (float) dispBounds[d].height / (float) displays[d][2];

         step = (float) (range / 16.0);

         start = (float) (range * (float) leds[i][2] + step * 0.5);

         for (row = 0; row < 16; row++)

            y[row] = (int) (start + step * (float) row);

 

         if (useFullScreenCaps) {

 

            for (row = 0; row < 16; row++) {

               for (col = 0; col < 16; col++) {

                  pixelOffset[i][row * 16 + col] = y[row] * dispBounds[d].width + x[col];

               }

            }

         }

 

         else {

            ledBounds[i] = new Rectangle(x[0], y[0], x[15] - x[0] + 1, y[15] - y[0] + 1);

            for (row = 0; row < 16; row++) {

               for (col = 0; col < 16; col++) {

                  pixelOffset[i][row * 16 + col] = (y[row] - y[0]) * ledBounds[i].width + x[col] - x[0];

               }

            }

         }

      }

 

      for (i = 0; i < prevColor.length; i++) {

         prevColor[i][0] = prevColor[i][1] = prevColor[i][2] = minBrightness / 3;

      }

 

      // Preview window shows all screens side-by-side

      size(totalWidth * pixelSize, maxHeight * pixelSize, JAVA2D);

      noSmooth();

 

      serialData[0] = 'A'; // Magic word

      serialData[1] = 'd';

      serialData[2] = 'a';

      serialData[3] = (byte) ((leds.length - 1) >> 8); // LED count high byte

      serialData[4] = (byte) ((leds.length - 1) & 0xff); // LED count low byte

      serialData[5] = (byte) (serialData[3] ^ serialData[4] ^ 0x55); // Checksum

 

      for (i = 0; i < 256; i++) {

         f = (float) Math.pow((float) i / 255.0, 2.8);

 

         gamma[i][0] = (byte) (f * 255.0);

         gamma[i][1] = (byte) (f * 240.0);

         gamma[i][2] = (byte) (f * 220.0);

 

      }

 

      

      

      

      println("Available serial ports:");

      println(Serial.list());

      port = new Serial(this, "COM6", 9600);

 

      

   }

 

   public void settings() {

      size(500, 500);

   }

 

   /*

    * 

    * public void draw(){

    * 

    * ellipse(mouseX, mouseY, 50, 50);

    * 

    * }

    * 

    */

 

   public void draw() {

 

      BufferedImage img;

      int d, i, j, o, c, weight, rb, r, b, g, sum, deficit, s2, r16, b16, g16;

      int[] pxls, offs;

 

      if (useFullScreenCaps) {

         // Capture each screen in the displays array.

         for (d = 0; d < nDisplays; d++) {

            img = bot[d].createScreenCapture(dispBounds[d]);

            // Get location of source pixel data

            screenData[d] = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

         }

      }

 

      weight = 257 - fade; // 'Weighting factor' for new frame vs. old

      j = 6; // Serial led data follows header / magic word

 

      for (i = 0; i < leds.length; i++) { // For each LED...

 

         d = leds[i][0]; // Corresponding display index

 

         if (useFullScreenCaps) {

            // Get location of source data from prior full-screen capture:

            pxls = screenData[d];

         } else {

            img = bot[d].createScreenCapture(ledBounds[i]);

            pxls = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

         }

 

         offs = pixelOffset[i];

         rb = g = r = b = r16 = g16 = b16 = 0;

         

         //float step_x =0, step_y = 0, range_x, range_y = 0;

         //range_x = (float) dispBounds[d].width / (float) displays[d][1];

         //range_y = (float) dispBounds[d].height / (float) displays[d][2];

         //step_x = (float) (range_x / 16.0);

         //step_y = (float) (range_y / 16.0);

         

         int step_x =0, step_y = 0, range_x=0, range_y = 0;

         range_x = dispBounds[d].width /displays[d][1];

         range_y = dispBounds[d].height /  displays[d][2];

         step_x =  (range_x / 16);

         step_y = (range_y / 16);

         

         for (o = 0; o < 256; o++) {

        	 r = 0;

        	 b = 0;

        	 g = 0;

        	 //System.out.println("pxls: "+pxls.length);

        	 for(int x = -(step_x/2); x<=step_x/2; ++x) {

        		 for(int y = -(step_y/2); y<=step_y/2; ++y) {

        	            c = pxls[offs[o]+y*dispBounds[d].width+x];

        	            //rb += (c & 0x00ff00ff) >> 24; // Bit trickery: R+B can accumulate in one var

        	            r += ((c & 0x00ff0000) >> 16);

        	            b += c & 0x000000ff;

        	            g += (c & 0x0000ff00) >> 8;

        	            //System.out.println("r: "+r+" g: "+g+" b: "+b);

        		 }

        	 }

        	 r /= (step_x*step_y);

        	 b /= (step_x*step_y);

        	 g /= (step_x*step_y);

        	 r16 += r;

        	 g16 += g;

        	 b16 += b;

 

         }

         r16 /= 256;

         g16 /= 256;

         b16 /= 256;

         ledColor[i][0] = (short) ((r16 * weight + prevColor[i][0] * fade) >> 8);

         ledColor[i][1] = (short) ((g16 * weight + prevColor[i][1] * fade) >> 8);

         ledColor[i][2] = (short) ((b16 * weight + prevColor[i][2] * fade) >> 8);

 
         System.out.println(ledColor[i][0] + " "+ledColor[i][1]+" "+ledColor[i][2]);
         ledColor[i][0] = ledColor[i][0]>255 ? 255:ledColor[i][0];
         ledColor[i][1] = ledColor[i][1]>255 ? 255:ledColor[i][1];
         ledColor[i][2] = ledColor[i][2]>255 ? 255:ledColor[i][2];
         
         sum = ledColor[i][0] + ledColor[i][1] + ledColor[i][2];

         if (sum < minBrightness) {

 

            if (sum == 0) { // To avoid divide-by-zero

 

               deficit = minBrightness / 3; // Spread equally to R,G,B

               ledColor[i][0] += deficit;

               ledColor[i][1] += deficit;

               ledColor[i][2] += deficit;

            } else {

 

               deficit = minBrightness - sum;

               s2 = sum * 2;

 

               ledColor[i][0] += deficit * (sum - ledColor[i][0]) / s2;

               ledColor[i][1] += deficit * (sum - ledColor[i][1]) / s2;

               ledColor[i][2] += deficit * (sum - ledColor[i][2]) / s2;
               
            }

         }

 

 

         serialData[j++] = gamma[ledColor[i][0]][0];

         serialData[j++] = gamma[ledColor[i][1]][1];

         serialData[j++] = gamma[ledColor[i][2]][2];

 

         preview[d].pixels[leds[i][2] * displays[d][1] + leds[i][1]] = (ledColor[i][0] << 16) | (ledColor[i][1] << 8) | ledColor[i][2];

 

      }

 

 

      scale(pixelSize);

 

      for (i = d = 0; d < nDisplays; d++) {

         preview[d].updatePixels();

         image(preview[d], i, 0);

         i += displays[d][1] + 1;

      }

 

      //println(frameRate); // How are we doing?

 

      // Copy LED color data to prior frame array for next pass

      arraycopy(ledColor, 0, prevColor, 0, ledColor.length);

      

      

    //  int adul =0;

      for(int k = 0; k<ledColor.length;++k)

      {//	adul = k*10000000+ledColor[k][0]*1000000+ledColor[k][1]*1000+ledColor[k][2];   	

    	  //this.out.write(k*10000000+ledColor[k][0]*1000000+ledColor[k][1]*1000+ledColor[k][2]);

      //port.write(k*10000000+ledColor[k][0]*1000000+ledColor[k][1]*1000+ledColor[k][2]);

      String val = String.format("%02X%02X%02X%02X\n",k,ledColor[k][0],ledColor[k][1],ledColor[k][2]);

      //지정된 형식에 따라 개체의 값을 문자열로 변환하여 다른 문자열에 삽입 합니다.

     // System.out.println(String.format("%02x%02x", ledColor[2][0],ledColor[2][1] ));

      //System.out.println(String.format("%10x%02x", ledColor[2][0],ledColor[2][1] ));

     // System.out.println(String.format("%02x", ledColor[2][1]));

     // System.out.println(String.format("%10x", ledColor[2][1]));

      //System.out.println("Test: val"+val);

      //System.out.println(val);

      port.write(val);
      //System.out.println(val);
      //port.wait(1);
      try {
		Thread.sleep(5);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
      System.out.println("port: "+port.readString());

     // port.write((int)(Math.random() *255));

      //System.out.println(port.readString());

    	  

    	 // println("FFFFFFFF");

      }

      

   }

 

   public void mousePressed() {

      background(64);

   }

   

/*

   public Serial openPort() {

      String[] ports;

      String ack;

      int i, start;

      Serial s;

      ports = Serial.list(); // List of all serial ports/devices on system.

      for (i = 0; i < ports.length; i++) { // For each serial port...

         System.out.format("Trying serial port %s\n", ports[i]);

         try {

            s = new Serial(this, ports[i], 115200);

         }

 

         catch (Exception e) {

 

            // Can't open port, probably in use by other software.

            continue;

         }

 

         // Port open...watch for acknowledgement string...

 

         start = millis();

 

         while ((millis() - start) < timeout) {

            if ((s.available() >= 4) &&

                  ((ack = s.readString()) != null) &&

                  ack.contains("Ada\n")) {

               return s; // Got it!

 

            }

         }

 

         // Connection timed out. Close port and move on to the next.

         s.stop();

      }

 

      return new Serial(this, ports[0], 115200);

   }

 

   

 

   

   public void dispose() {

 

      // Fill serialData (after header) with 0's, and issue to Arduino...

 

//    Arrays.fill(serialData, 6, serialData.length, (byte)0);

 

      java.util.Arrays.fill(serialData, 6, serialData.length, (byte) 0);

      

      if (port != null)

         port.write(serialData);

 

   }

   

     */

 

   public static void main(String[] args) {

 

      String[] processingArgs = { "MySketch" };

      Main mySketch = new Main();

      PApplet.runSketch(processingArgs, mySketch);

 

   }

 

}