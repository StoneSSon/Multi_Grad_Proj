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
import java.awt.event.*;

public class Main_Class extends PApplet {
   
   static JFrame frm = new JFrame("Express Terminal");      
   static JFrame frm2 = new JFrame("Express Terminal Settings");
   static final short minBrightness = 120;
   static final short fade = 75;
   static final int pixelSize = 50;
 

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
    
   
   public static void main(String[] args) {
      
      
      
      //////////////////////////////////////////////////////// 
        // 프레임 크기 설정
        frm.setSize(200, 300);
 
        // 프레임을 화면 가운데에 배치
        frm.setLocationRelativeTo(null);
 
        // 프레임을 닫았을 때 메모리에서 제거되도록 설정
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        // 레이아웃 설정
        frm.getContentPane().setLayout(null);
 
        /////////////////////////////////////////////////////////
        // 프레임 생성
        frm2 = new JFrame("Express Terminal Settings");
 
        // 프레임 크기 설정
        frm2.setSize(300, 500);
 
        // 프레임을 화면 가운데에 배치
        frm2.setLocationRelativeTo(null);
 
        // 레이아웃 설정
        frm2.getContentPane().setLayout(null);   
        //////////////////////////////////////////////////////////
        // 버튼 생성
        JButton btn0 = new JButton("Video");
        JButton btn1 = new JButton("Video");
        JButton btn2 = new JButton("Sound");
        JButton btn3 = new JButton("Game");
        JButton btn4 = new JButton("Settings");
        JButton btn5 = new JButton("Exit");
        JButton btn5_2 = new JButton("Exit");
        // ★ 버튼 위치와 크기 설정
        btn0.setBounds(50, 30, 100, 20);
        btn1.setBounds(50, 60, 100, 20);
        btn2.setBounds(50, 90, 100, 20);
        btn3.setBounds(50, 120, 100, 20);
        btn4.setBounds(50, 150, 100, 20);
        btn5.setBounds(50, 180, 100, 20);
        btn5_2.setBounds(300, 650, 100, 20);
        // ★ 프레임에다가 버튼 추가
        frm.getContentPane().add(btn1);
        frm.getContentPane().add(btn2);
        frm.getContentPane().add(btn3);
        frm.getContentPane().add(btn4);
        frm.getContentPane().add(btn5);
        frm2.getContentPane().add(btn5_2);
        
        btn1.addActionListener(event -> {
           
           /*Runtime rt = Runtime.getRuntime();
          Process pc = null;
           
           try {
             pc =rt.exec("C:\\ard-night\\arduino-nightly\\arduino.exe");
          
             System.out.println("Executing Program");
             
             
          } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
          } finally {
             
             //if (port != null)
                //port.write(serialData);
             
          }*/
           
           
        });
        btn1.addActionListener(event -> {
           Runtime rt = Runtime.getRuntime();
          Process pc = null;
           
          Main_Class MainDup = new  Main_Class();
          String[] processingArgs = { "Video_Class" };
          Main_Class mySketch = new  Main_Class();
          PApplet.runSketch(processingArgs, mySketch);
          MainDup.setup();
          
           
        });
        btn2.addActionListener(event -> {
        	 Runtime rt = Runtime.getRuntime();
             Process pc = null;
              
             Main_Class MainDup = new  Main_Class();
             String[] processingArgs = { "Music_Class" };
             Main_Class mySketch = new  Main_Class();
             PApplet.runSketch(processingArgs, mySketch);
             MainDup.setup();
        });
        btn3.addActionListener(event -> {
           Runtime rt = Runtime.getRuntime();
          Process pc = null;
           
           
        });
        btn4.addActionListener(event -> {
           Runtime rt = Runtime.getRuntime();
          Process pc = null;
          
           frm2.setVisible(true);
        });
        btn5.addActionListener(event -> {
           Runtime rt = Runtime.getRuntime();
          Process pc = null;
           
          System.exit(0);
           
        });
        btn5_2.addActionListener(event -> {
           Runtime rt = Runtime.getRuntime();
          Process pc = null;
          
           if(frm2.isActive()) {
          frm2.dispose();
           }
        });
 
        // 프레임이 보이도록 설정
        frm.setVisible(true);


      
   }
}