//https://github.com/arduino/Arduino/blob/master/build/shared/manpage.adoc
import java.awt.*;
import java.awt.image.*;
import processing.serial.*;
import processing.serial.Serial;
import processing.core.PApplet;
import processing.core.PImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Game_Class extends PApplet {

   static int[] i_array = {570,615,300,50}; //�ʱⰪ 0
   static int[] color = new int[10]; //�ʱⰪ 0
   static PImage screenshot;
   static final int timeout = 5000; // 5 seconds
   
   Serial port;
   
   int counter;
   
   
   
   public void setup() {
//        size(300,150);

//        String portName = Serial.list()[3];
 //       println(Serial.list());
     //    port = new Serial(this, "COM10", 9600);
      }
       
   public void draw() {
	   counter=0;
        if (frameCount % 30 == 0) screenshot();
        if (screenshot != null) {
           image(screenshot,0,0,width,height);
           int main_color=screenshot.get(0, 1);
           int sub_color=screenshot.get(10, 1);
          
           //println(sub_color);
           for(int i=0 ; i <10 ; i++) {
             color[i] =screenshot.get(i_array[2]/11 * i, 0);	 
        	  // println("Main"+main_color);
        	  // println("Sub"+color[i]);
        	  // println(abs(color[i]-main_color));
             if(color[i]<-1000)
             {
                counter ++;
             }
           }
           println(counter-1);
          // port.write(counter-1);
          // toLED(counter-1);
        }
      }
   //ü�� �ۼ�Ʈ�� �д� ī������ ���� �д´�.
   //0~10 , 10~30 , 30~60 , 60~100
   public void toLED(int counter){
      if(counter<2)
      {
      //LED Grey
          setRGB(0,0,0,0);
      }
      else if(counter<4)
      {
      //LED RED
          setRGB(255,0,0,0);
      }
      else if(counter<7)
      {
      //LED Yellow
          setRGB(255,255,0,0);
      }
      else
      {
      //LED Green
          setRGB(0,255,0,0);
      }
   }
   
      //��ũ�� �Ϻθ� �д� ����x��ǥ , ����y��ǥ , ���� ,���� 0,0,100,200 ��  ����0~100 , ����0~200 ũ�� ���簢�����ĸ��
   
   void screenshot() {
      try {
          screenshot = new PImage(new Robot().createScreenCapture(new Rectangle( i_array[0], i_array[1], i_array[2], i_array[3])));
        } catch (AWTException e) { }
      }
   //���ø��� ũ��
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

   public void mousePressed() {
      background(64);
   }
   /*public Serial openPort() {
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
   }*/

   void setRGB (int r, int g, int b, int led) {
    //   port.write(counter);
//       port.write(r);
//       port.write(g);
//       port.write(b);
 //      port.write(led);
      }
   
   
   public static void main(String[] args) {
      
      /*try{
            //���� ��ü ����
            File file = new File("CoordText.txt");
            //�Է� ��Ʈ�� ����
            FileReader filereader = new FileReader(file);
            //�Է� ���� ����
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            int tmp=0;
            while((line = bufReader.readLine()) != null){
                //System.out.println(line);
         
                i_array[tmp++] = Integer.parseInt(line); 
                //println(i_array[tmp++]);
            }
           
            //.readLine()�� ���� ���๮�ڸ� ���� �ʴ´�.            
            bufReader.close();
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        }catch(IOException e){
            System.out.println(e);
        }*/
	   
      String[] processingArgs = { "MySketch" };
      
      Game_Class mySketch = new Game_Class();
      PApplet.runSketch(processingArgs, mySketch);      
   }
}