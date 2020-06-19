
import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import processing.serial.Serial;

public class Sound extends PApplet {
	Minim minim;
	AudioPlayer song;
	AudioMetaData meta;
	FFT fft; // Fast Fourier Transform
	Serial port;

	int mode = 1; //LED ���
	int level = 16; //LED �ܰ�
	float color = 0; //information of fft
	float LS = 0; //Left volume Sum
	float RS = 0; //Right volume Sum

	public void settings() {
		size(512, 256);
	}

	public void setup() {
		minim = new Minim(this);
		song = minim.loadFile("Pink Sweat$ - Ride With Me.mp3");
		song.loop();
		song.setGain(-10); // ��������
		meta = song.getMetaData();

		fft = new FFT(song.bufferSize(), song.sampleRate());
		// buffersize ����ī�尡 ������ ������ ó���ϱ�  ���� �Ҵ�� �ð�
		// sampleRate ������ �� ��

		textSize(12);
		println("Available serial ports:");
		println(Serial.list()); //üũ �� ���� �� Serial Port ã�ƾ� �Ѵ�
		port = new Serial(this, Serial.list()[2], 9600);

	}

	public void draw() {
		background(0);
		fill(200);
		
		Music_Player_UI();

		fft.forward(song.mix);// Ǫ������ ���
		KeyPressed();
		Serial_Sender();
	}
	
	public void Serial_Sender() {
		Set_Color();
		Get_Volume();
		
		int c = (int) color + 2; //0~16
		int adul = (int) (LS); //0~level
		int adur = (int) (RS); //0~level
		
		if (adul > level)
			adul = level;
		if (adur > level)
			adur = level;

		String val = String.format("%02X%02X%02X%02X\n", c, adul, adur, mode);
		println(val);
		port.write(val);
	}
		
	public void Set_Color() {
		float fftsum = 0;
		for (int i = 1; i < fft.specSize(); i++) {
			float xi = logB(i, 2); // ����Ʈ���� x��ǥ ��
			fftsum += xi * logB(fft.getBand(i) + 1, 2);
		}
		
		fftsum /= fft.specSize(); // 0~16
		color = (float) (0.8 * color + 0.2 * fftsum);
		if (color > 14)
			color = 14; //0~14
		
		stroke(255/16*(color+2), 0,255-255/16*(color+2));
	}

	public void Get_Volume() {
		float suml = 0;// left sound
		float sumr = 0;// right sound
		float xpos = 25;// �׷��� �¿� ����
		strokeWeight(20);
		for (int i = 0; i < song.bufferSize() - 1; i += 1) {
			suml += abs(song.left.get(i)) * level; // 0~1 -> 0 ~ level
			sumr += abs(song.right.get(i)) * level; // 0~1 -> 0 ~ level
		}
		suml = (float) (suml / song.bufferSize() * 1.8 + 0.8);
		sumr = (float) (sumr / song.bufferSize() * 1.8 + 0.8);

		LS = (float) (0.1 * LS + 0.9 * suml); // �ް��� ��ȭ ����
		RS = (float) (0.1 * RS + 0.9 * sumr);

		int ledl = (int) (LS) * (height / level); //���� ���� ����ȭ
		int ledr = (int) (RS) * (height / level);

		line(xpos, height - 20, xpos, height - 20 - ledl);
		line(width - xpos, height - 20, width - xpos, height - 20 - ledr);
	}

	public void Music_Player_UI() {
		float ps = 5;// play button�� size
		float position = map(song.position(), 0, song.length(), 0, width);

		textSize(15);
		text("Click anywhere to jump to a position in the song.", 70, height - 40);

		stroke(50);// �÷��� ��ư�� ������ ��
		strokeWeight(5);
		line(0, height - 10, width, height - 10);
		
		stroke(255); //�÷��� ��ư ��� �ﰢ�� �׸���
		strokeWeight(2);
		fill(0, 200, 0);
		triangle(position - ps, height - 10 - ps, position - ps, height - 10 + ps, position + ps * 2, height - 10);	
	}
	
	public void mousePressed() {
		int position = (int) (map(mouseX, 0, width, 0, song.length()));
		song.cue(position);
	}

	public void KeyPressed() {
		if (key == 's') { //�뷡 ���
			song.pause();
			mode = 0;
		}
		else if (key == 'p') //�Ͻ� ����
			song.play();
		else if (key == '0')
			mode = 0;
		else if (key == '1')
			mode = 1;
		else if (key == '2')
			mode = 2;
		else if (key == '3')
			mode = 3;
		else if (key == '4')
			mode = 4;
	}

	public void stop() {
		song.close();
		minim.stop();
		super.stop();
	}

	public static float logB(float x, float base) {
		return (float) (Math.log(x) / Math.log(base));
	}

	
	public static void main(String[] args) {
		String[] SoundProcessing = { "Sound Mode" };
		PApplet SoundSketch = new Sound();
		PApplet.runSketch(SoundProcessing, SoundSketch);
	}

}
