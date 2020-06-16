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
        // ������ ũ�� ����
        frm.setSize(200, 300);
 
        // �������� ȭ�� ����� ��ġ
        frm.setLocationRelativeTo(null);
 
        // �������� �ݾ��� �� �޸𸮿��� ���ŵǵ��� ����
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        // ���̾ƿ� ����
        frm.getContentPane().setLayout(null);
 
        /////////////////////////////////////////////////////////
        // ������ ����
        frm2 = new JFrame("Express Terminal Settings");
 
        // ������ ũ�� ����
        frm2.setSize(300, 500);
 
        // �������� ȭ�� ����� ��ġ
        frm2.setLocationRelativeTo(null);
 
        // ���̾ƿ� ����
        frm2.getContentPane().setLayout(null);   
        //////////////////////////////////////////////////////////
        // ��ư ����
        JButton btn0 = new JButton("Video");
        JButton btn1 = new JButton("Video");
        JButton btn2 = new JButton("Sound");
        JButton btn3 = new JButton("Game");
        JButton btn4 = new JButton("Settings");
        JButton btn5 = new JButton("Exit");
        JButton btn5_2 = new JButton("Exit");
        // �� ��ư ��ġ�� ũ�� ����
        btn0.setBounds(50, 30, 100, 20);
        btn1.setBounds(50, 60, 100, 20);
        btn2.setBounds(50, 90, 100, 20);
        btn3.setBounds(50, 120, 100, 20);
        btn4.setBounds(50, 150, 100, 20);
        btn5.setBounds(50, 180, 100, 20);
        btn5_2.setBounds(300, 650, 100, 20);
        // �� �����ӿ��ٰ� ��ư �߰�
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
 
        // �������� ���̵��� ����
        frm.setVisible(true);


      
   }
}