package TaaT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import processing.core.PApplet;

public class Main_Class extends JFrame {

   BufferedImage backImg = null;
   JTextField loginTextField;
   JPasswordField passwordField;
   JButton btn0;
   JButton btn1;
   JButton btn2;
   JButton btn3;
   JButton btn4;
   JButton btn5;
   JButton btn5_2;

   // 메인
   public static void main(String[] args) {
      new Main_Class();
   }

   // 생성자
   public Main_Class() {
      setTitle("졸작 화이팅입니다!");
      setSize(500, 500);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      // 레이아웃 설정
      setLayout(null);
      JLayeredPane layeredPane = new JLayeredPane();
      layeredPane.setBounds(0, 0, 500, 500);
      layeredPane.setLayout(null);

      // 패널1
      // 이미지 받아오기
      try {
         backImg = ImageIO.read(new File("img/circle_light2.png"));
      } catch (IOException e) {
         System.out.println("이미지 불러오기 실패");
         System.exit(0);
      }

      MyPanel panel = new MyPanel();
      panel.setBounds(0, 0, 1600, 900);

      // 로그인버튼 추가
      JButton btn0 = new JButton(new ImageIcon("img/btLogin_hud.png"));// "Video"
      JButton btn1 = new JButton(new ImageIcon("img/video.png"));
      JButton btn2 = new JButton(new ImageIcon("img/sound.png"));
      JButton btn3 = new JButton(new ImageIcon("img/game.png"));
      JButton btn4 = new JButton(new ImageIcon("img/settings.png"));
      JButton btn5 = new JButton(new ImageIcon("img/exit.png"));
      JButton btn5_2 = new JButton("exit");//exit
      //

      // ★ 버튼 위치와 크기 설정
      btn0.setBounds(200, 10, 1, 10);
      btn1.setBounds(200, 160, 100, 30);
      btn2.setBounds(200, 200, 100, 30);
      btn3.setBounds(200, 240, 100, 30);
      btn4.setBounds(200, 280, 100, 30);
      btn5.setBounds(200, 320, 100, 30);
      btn5_2.setBounds(200, 270, 100, 20);

      // 버튼 투명처리
      btn0.setPressedIcon(new ImageIcon("img/exit.png"));
      btn1.setPressedIcon(new ImageIcon("img/video_p.png"));
      btn2.setPressedIcon(new ImageIcon("img/sound_p.png"));
      btn3.setPressedIcon(new ImageIcon("img/game_p.png"));
      btn4.setPressedIcon(new ImageIcon("img/exit_p.png"));
      btn5.setFocusPainted(false);
      btn5_2.setContentAreaFilled(false);
      
      btn1.setBorderPainted(true);
      btn1.setFocusPainted(false);
      btn1.setContentAreaFilled(false);
   
      btn2.setBorderPainted(true);
      btn2.setFocusPainted(false);
      btn2.setContentAreaFilled(false);
      //btn2.setBackground(new Color(255, 255, 0, 255));    //투명
      
      btn3.setBorderPainted(true);
      btn3.setFocusPainted(false);
      btn3.setContentAreaFilled(false);
      
      btn4.setBorderPainted(true);
      btn4.setFocusPainted(false);
      btn4.setContentAreaFilled(false);

      btn5.setBorderPainted(true);
      btn5.setFocusPainted(false);
      btn5.setContentAreaFilled(false);
      
      btn5_2.setBorderPainted(true);
      btn5_2.setFocusPainted(true);
      btn5_2.setContentAreaFilled(true);
      
<<<<<<< HEAD
      layeredPane.add(btn0);
      layeredPane.add(btn1);
      layeredPane.add(btn2);
      layeredPane.add(btn3);
      layeredPane.add(btn4);
      layeredPane.add(btn5);
      //layeredPane.add(btn5_2);

      btn1.addActionListener(event -> {

         /*
          * Runtime rt = Runtime.getRuntime(); Process pc = null;
          * 
          * try { pc =rt.exec("C:\\ard-night\\arduino-nightly\\arduino.exe");
          * 
          * System.out.println("Executing Program");
          * 
          * 
          * } catch (IOException e) { // TODO Auto-generated catch block
          * e.printStackTrace(); } finally {
          * 
          * //if (port != null) //port.write(serialData);
          * 
          * }
          */
=======
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
           
           Runtime rt = Runtime.getRuntime();
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
             
          }
           
           
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
             
            Main_Class MainDup = new  Main_Class();
            String[] processingArgs = { "Sound_Class" };
            Main_Class mySketch = new  Main_Class();
            PApplet.runSketch(processingArgs, mySketch);
            MainDup.setup();
           
           
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
>>>>>>> 64c8ac166486250667fb25725c455eceedf506bc

      });
      btn1.addActionListener(event -> {
    	  
    	  
    	  String[] processingArgs = { "Video_Class" };
          Main_Class SoundSketch = new Main_Class();
          PApplet.runSketch(processingArgs, SoundSketch);

      });
      btn2.addActionListener(event -> {
    	  
    	  String[] processingArgs = { "Music_Class" };
          Main_Class SoundSketch = new Main_Class();
           PApplet.runSketch(processingArgs, SoundSketch);

      });
      btn3.addActionListener(event -> {
    	 
    	  String[] processingArgs = { "Game_Class" };
          Main_Class SoundSketch = new Main_Class();
           PApplet.runSketch(processingArgs, SoundSketch);


      });
      btn4.addActionListener(event -> {
         Runtime rt = Runtime.getRuntime();
         Process pc = null;

         //frm2.setVisible(true);
      });
      btn5.addActionListener(event -> {
         Runtime rt = Runtime.getRuntime();
         Process pc = null;

         System.exit(0);

      });
      btn5_2.addActionListener(event -> {
         Runtime rt = Runtime.getRuntime();
         Process pc = null;

         /*if (frm2.isActive()) {
            frm2.dispose();
         }*/
      });
      
      // 마지막 추가들
      layeredPane.add(panel);
      add(layeredPane);
      setVisible(true);
      
   }

   class MyPanel extends JPanel {
      public void paint(Graphics g) {
         g.drawImage(backImg, 0, 0, null);
      }
   }

   
}