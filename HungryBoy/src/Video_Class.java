

import java.awt.*;
import java.awt.image.*;
import java.util.Random;
import processing.serial.Serial;
import processing.core.PApplet;
import processing.core.PImage;
 
public class Video_Class extends PApplet {
   
   
   static final int ledList[][] = new int[][] { 
 {0,10,6},{0,9,6},{0,8,6},{0,7,6},{0,6,6},{0,5,6},{0,4,6},{0,3,6},{0,2,6},{0,1,6},{0,0,6},//
      {0,0,5},{0,0,4},{0,0,3},{0,0,2},{0,0,1},{0,0,0},//
      {0,1,0},{0,2,0},{0,3,0},{0,4,0},{0,5,0},{0,6,0},{0,7,0},{0,8,0},{0,9,0},{0,10,0},//
      {0,10,1},{0,10,2},{0,10,3},{0,10,4},{0,10,5}//        
   };
   
   static final int displayList[][] = new int[][] { { 0, 11, 7 } };
   static final short fade = 75;
   static final int pSize = 20;
   static final short min_Brightness = 120;
   static final int tOut = 5000; 
   short[][] ledColor = new short[ledList.length][3], prevColor = new short[ledList.length][3];   
   int nDisplays = displayList.length;
   Rectangle[] dBound = new Rectangle[displayList.length], ledBounds; 
   int[][] pOffset = new int[ledList.length][256], screenData;
   byte[][] gamma = new byte[256][3];
   byte[] sData = new byte[6 + ledList.length * 3];
   Robot[] bot = new Robot[displayList.length];
   PImage[] preview = new PImage[displayList.length];
   Serial port;
   Video_Class dh;
   
   public static void Main(String[] args) {
      String[] processingArgs = { "MySketch" };
      Video_Class mySketch = new Video_Class();
      PApplet.runSketch(processingArgs, mySketch);
   }
 
   public void settings() {
         size(500, 500);
      }
 
   
   public void draw() {
      BufferedImage img;
      int d, i, j, o, c, weight, rb, r, b, g, sum, deficit, s2, r16, b16, g16;
      int[] pxls, offs;
     
      for (d = 0; d < nDisplays; d++) {
         img = bot[d].createScreenCapture(dBound[d]);
         screenData[d] = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
      }
      
      weight = 257 - fade;
      j = 6;
      for (i = 0; i < ledList.length; i++) {
         d = ledList[i][0]; 

         pxls = screenData[d];

         int step_x =0, step_y = 0, range_x=0, range_y = 0;         
         
         offs = pOffset[i];
         rb = g = r = b = r16 = g16 = b16 = 0;
         range_x = dBound[d].width /displayList[d][1];
         range_y = dBound[d].height /  displayList[d][2];
         step_x =  (range_x / 16);
         step_y = (range_y / 16);
         for (o = 0; o < 256; o++) {
            r = 0;
            b = 0;
            g = 0;
            for(int x = -(step_x/2); x<=step_x/2; ++x) {
               for(int y = -(step_y/2); y<=step_y/2; ++y) {
                       c = pxls[offs[o]+y*dBound[d].width+x];
                       r += ((c & 0x00ff0000) >> 16);
                       b += c & 0x000000ff;
                       g += (c & 0x0000ff00) >> 8;
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
         if (sum < min_Brightness) {
            if (sum == 0) { 
               deficit = min_Brightness / 3; 
               ledColor[i][0] += deficit;
               ledColor[i][1] += deficit;
               ledColor[i][2] += deficit;
            } else {
               deficit = min_Brightness - sum;
               s2 = sum * 2;
               ledColor[i][0] += deficit * (sum - ledColor[i][0]) / s2;
               ledColor[i][1] += deficit * (sum - ledColor[i][1]) / s2;
               ledColor[i][2] += deficit * (sum - ledColor[i][2]) / s2;
               
            }
         }
         sData[j++] = gamma[ledColor[i][0]][0];
         sData[j++] = gamma[ledColor[i][1]][1];
         sData[j++] = gamma[ledColor[i][2]][2];
         preview[d].pixels[ledList[i][2] * displayList[d][1] + ledList[i][1]] = (ledColor[i][0] << 16) | (ledColor[i][1] << 8) | ledColor[i][2];
      }
      scale(pSize);
      for (i = d = 0; d < nDisplays; d++) {
         preview[d].updatePixels();
         image(preview[d], i, 0);
         i += displayList[d][1] + 1;
      }
      arraycopy(ledColor, 0, prevColor, 0, ledColor.length);
      for(int k = 0; k<ledColor.length;++k)
      {
      String val = String.format("%02X%02X%02X%02X\n",k,ledColor[k][0],ledColor[k][1],ledColor[k][2]);
      port.write(val);
         try {
            Thread.sleep(5);
         } catch (InterruptedException e) {
      
            e.printStackTrace();
         }
   
      System.out.println("port: "+port.readString());
      }
   }
 
   public void setup() {
      GraphicsEnvironment ge;
      GraphicsConfiguration[] gc;
      GraphicsDevice[] gd;
      int d, i, totalWidth, maxHeight, row, col, rowOffset;
      int[] x = new int[16], y = new int[16];
      float f, range, step, start;
      dBound = new Rectangle[displayList.length];

      screenData = new int[displayList.length][];
      
      ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      gd = ge.getScreenDevices();
      if (nDisplays > gd.length)
         nDisplays = gd.length;
      totalWidth = 0;
      maxHeight = 0;
      for (d = 0; d < nDisplays; d++) {
         try {
            bot[d] = new Robot(gd[displayList[d][0]]);
         } catch (AWTException e) {
            System.out.println("new Robot() failed");
            continue;
         }
 
         gc = gd[displayList[d][0]].getConfigurations();
         dBound[d] = gc[0].getBounds();
         dBound[d].x = dBound[d].y = 0;
         preview[d] = createImage(displayList[d][1], displayList[d][2], RGB);
         preview[d].loadPixels();
         totalWidth += displayList[d][1];
         if (d > 0)
            totalWidth++;
 
         if (displayList[d][2] > maxHeight)
            maxHeight = displayList[d][2];
      }
 
      for (i = 0; i < ledList.length; i++) { 
 
         d = ledList[i][0];
         range = (float) dBound[d].width / (float) displayList[d][1];
         step = (float) (range / 16.0);
         start = (float) (range * (float) ledList[i][1] + step * 0.5);
         for (col = 0; col < 16; col++)
            x[col] = (int) (start + step * (float) col);
         range = (float) dBound[d].height / (float) displayList[d][2];
         step = (float) (range / 16.0);
         start = (float) (range * (float) ledList[i][2] + step * 0.5);
         for (row = 0; row < 16; row++)
            y[row] = (int) (start + step * (float) row);
 
         for (row = 0; row < 16; row++) {
            for (col = 0; col < 16; col++) {
               pOffset[i][row * 16 + col] = y[row] * dBound[d].width + x[col];
            }
         }

      }
 
      for (i = 0; i < prevColor.length; i++) {
         prevColor[i][0] = prevColor[i][1] = prevColor[i][2] = min_Brightness / 3;
      }

      size(totalWidth * pSize, maxHeight * pSize, JAVA2D);

      noSmooth();
 
      sData[0] = 'A';
      sData[1] = 'd';
      sData[2] = 'a';
      sData[3] = (byte) ((ledList.length - 1) >> 8); 
      sData[4] = (byte) ((ledList.length - 1) & 0xff); 
      sData[5] = (byte) (sData[3] ^ sData[4] ^ 0x55);
 
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
   
   public void mousePressed() {
      background(64);
   }
}