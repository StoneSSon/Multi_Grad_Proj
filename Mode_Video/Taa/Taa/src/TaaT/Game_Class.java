package TaaT;
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

	/////////////////선언/////////////////
	
	static int[] i_array = new int[8]; //초기값 0
	static int[] color = new int[10]; //초기값 0
	static PImage screenshot;
	static final int timeout = 5000; // 5 seconds
	static final int displays[][] = new int[][] { { 0, 7, 5 } };
	static final int leds[][] = new int[][] { 
		
			{ 0, 3, 4 }, { 0, 2, 4 }, { 0, 1, 4 }, { 0, 0, 4 }, // Bottom edge, left half
			{ 0, 0, 3 }, { 0, 0, 2 }, { 0, 0, 1 }, // Left edge
			{ 0, 0, 0 }, { 0, 1, 0 }, { 0, 2, 0 }, { 0, 3, 0 }, { 0, 4, 0 }, // Top edge
			{ 0, 5, 0 }, { 0, 6, 0 }, // More top edge
			{ 0, 6, 1 }, { 0, 6, 2 }, { 0, 6, 3 }, { 0, 6, 4 }, // Right edge
			{ 0, 5, 4 }, { 0, 4, 4 } // Bottom edge, right half

	};
	Serial port;
	byte[] serialData = new byte[6 + leds.length * 3];
	
	
	
	public void setup() {
		  size(300,150);
		}
		 
	public void draw() {
		  if (frameCount % 60 == 0) screenshot();
		  if (screenshot != null) {
			  image(screenshot,0,0,width,height);
			  int main_color=screenshot.get(0, 0);
			  int counter = 0;
			  for(int i=0 ; i <10 ; i++) {
				 color[i] =screenshot.get(i_array[2]/11 * i, 0);
				 if(color[i]==main_color)
				 {
					 counter ++;
				 }
			  }
			  println(counter-1);
			  
		  }
		}
	//체력 퍼센트를 읽는 카운터의 값을 읽는다.
	//0~10 , 10~30 , 30~60 , 60~100
	public void toLED(int counter){
		if(counter<2)
		{
		//LED Grey
			
		}
		else if(counter<4)
		{
		//LED RED
			
		}
		else if(counter<7)
		{
		//LED Yellow
			
		}
		else
		{
		//LED Green
			
		}
	}
	
		//스크린 일부를 읽는 시작x좌표 , 시작y좌표 , 넓이 ,높이 0,0,100,200 은  가로0~100 , 세로0~200 크기 직사각형모양캡쳐
	
	void screenshot() {
		try {
		    screenshot = new PImage(new Robot().createScreenCapture(new Rectangle( i_array[0], i_array[1], i_array[2], i_array[3])));
		  } catch (AWTException e) { }
		}
	//애플릿의 크기
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
	public static void main(String[] args) {
		
		try{
            //파일 객체 생성
            File file = new File("CoordText.txt");
            //입력 스트림 생성
            FileReader filereader = new FileReader(file);
            //입력 버퍼 생성
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            int tmp=0;
            while((line = bufReader.readLine()) != null){
                //System.out.println(line);
         
                i_array[tmp++] = Integer.parseInt(line); 
                //println(i_array[tmp++]);
            }
           
            //.readLine()은 끝에 개행문자를 읽지 않는다.            
            bufReader.close();
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        }catch(IOException e){
            System.out.println(e);
        }
		
		String[] processingArgs = { "MySketch" };
		
		Game_Class mySketch = new Game_Class();
		PApplet.runSketch(processingArgs, mySketch);

		

		
		
		
	}
}