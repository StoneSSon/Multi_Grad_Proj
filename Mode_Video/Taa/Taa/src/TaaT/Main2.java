package TaaT;

import java.awt.*;
import java.awt.image.*;
import processing.serial.*;
import processing.serial.Serial;
import processing.core.PApplet;
import processing.core.PImage;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Main2 extends PApplet {
	
	Serial Port;
	static final int timeout  = 5000;  // 5 seconds
	 
	
	
	 static void runCommand(String[] cmd){

		    String s = null;

		    try {

		      Process p = Runtime.getRuntime().exec(cmd);
		      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		      // read the output from the command
		      System.out.println("command out:\n");
		      while ( (s = stdInput.readLine ()) != null) System.out.println(s);
		      System.out.println("errors (if any):\n");
		      while ( (s = stdError.readLine ()) != null) System.out.println(s);

		    }catch (IOException e) {
		      System.out.println("something went wrong: \n");
		      e.printStackTrace();
		    }

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

	 
	public static void main(String[] args) {
		
		
		
		// 프레임 생성
        JFrame frm = new JFrame("Express Terminal");
 
        // 프레임 크기 설정
        frm.setSize(500, 800);
 
        // 프레임을 화면 가운데에 배치
        frm.setLocationRelativeTo(null);
 
        // 프레임을 닫았을 때 메모리에서 제거되도록 설정
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        // 레이아웃 설정
        frm.getContentPane().setLayout(null);
 
        // 버튼 생성
        JButton btn1 = new JButton("Video");
        JButton btn2 = new JButton("Sound");
        JButton btn3 = new JButton("Game");
        JButton btn4 = new JButton("Settings");
        JButton btn5 = new JButton("Exit");
 
        // ★ 버튼 위치와 크기 설정
        btn1.setBounds(100, 100, 300, 80);
        btn2.setBounds(100, 300, 300, 80);
        btn3.setBounds(100, 500, 300, 80);
        btn4.setBounds(100, 650, 100, 30);
        btn5.setBounds(300, 650, 100, 30);
 
        // ★ 프레임에다가 버튼 추가
        frm.getContentPane().add(btn1);
        frm.getContentPane().add(btn2);
        frm.getContentPane().add(btn3);
        frm.getContentPane().add(btn4);
        frm.getContentPane().add(btn5);
       
        btn1.addActionListener(event -> {
        	
        	Runtime rt = Runtime.getRuntime();
    		Process pc = null;
        	
        	try {
    			pc =rt.exec("C:\\ard-night\\arduino-nightly\\arduino.exe");
    		
    			System.out.println("Executing Program");
    			
    			
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} finally {
    			
    			if (port != null)
    				port.write(serialData);
    			
    		}
        });
        
        btn2.addActionListener(event -> {
        	Runtime rt = Runtime.getRuntime();
    		Process pc = null;
        	
        	
        });

        
 
        // 프레임이 보이도록 설정
        frm.setVisible(true);


		
	}
}