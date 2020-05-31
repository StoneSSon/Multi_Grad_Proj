package TaaT;

import java.awt.*;
import java.awt.image.*;
import processing.serial.*;
import processing.serial.Serial;
import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet {

	static final short minBrightness = 120;
	static final short fade = 75;
	static final int pixelSize = 50;
	static final boolean useFullScreenCaps = true;
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
		if (useFullScreenCaps == true) {
			screenData = new int[displays.length][];
		} else {

			ledBounds = new Rectangle[leds.length];
		}

		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getScreenDevices();
		if (nDisplays > gd.length)
			nDisplays = gd.length;
		totalWidth = maxHeight = 0;
		for (d = 0; d < nDisplays; d++) { // For each display...
			try {
				bot[d] = new Robot(gd[displays[d][0]]);   // 화면을 캠쳐 할수 있는 봇 만들기 
			} catch (AWTException e) {
				//System.out.println("new Robot() failed");
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

	
				for (row = 0; row < 16; row++) {
					for (col = 0; col < 16; col++) {
						pixelOffset[i][row * 16 + col] = y[row] * dispBounds[d].width + x[col];
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
		int d, i, j, o, c = 0, weight, rb, g, sum, deficit, s2;
		int[] pxls, offs;

		if (useFullScreenCaps == true) {
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

			if (useFullScreenCaps == true) {
				// Get location of source data from prior full-screen capture:
				pxls = screenData[d];
			} 
					// y*n + x
			offs = pixelOffset[i];
			rb = g = 0;
			//System.out.println(pxls.length);
			for (o = 0; o < 256; o++) {   //화면에서 받은 256개의 픽셀값 
				int maxc=0;
				int minc=9999;
				for(int x = -1; x<1;++x) {
					for(int y = -1;y<1;++y) {
						c = pxls[offs[o]+(x*dispBounds[d].width+y)];
						if(maxc<c)maxc =c;
						if(minc>c)minc =c;
						rb += c & 0x00ff00ff; //red and blue 는 중첩해서 사용 가능 
						g += c & 0x0000ff00;
					}
				}
				//c /= 0x00ff;			//
				//c = pxls[offs[o]];     //c: 픽셀의 색 
				//rb += c & 0x00ff00ff; //red and blue 는 중첩해서 사용 가능 
				//g += c & 0x0000ff00; 
				rb -= ((maxc/256)) & 0x00ff00ff; 
				g -= ((maxc/256)) & 0x0000ff00;
				rb -= ((minc/256)) & 0x00ff00ff; 
				g -= ((maxc/256)) & 0x0000ff00;
			}

			ledColor[i][0] = (short) ((((rb >> 24) & 0xff) * weight + prevColor[i][0] * fade) >> 8);
			ledColor[i][1] = (short) ((((g >> 16) & 0xff) * weight + prevColor[i][1] * fade) >> 8);
			ledColor[i][2] = (short) ((((rb >> 8) & 0xff) * weight + prevColor[i][2] * fade) >> 8);

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

		if (port != null)
			port.write(serialData); // Issue data to Arduino

		scale(pixelSize);

		for (i = d = 0; d < nDisplays; d++) {
			preview[d].updatePixels();
			image(preview[d], i, 0);
			i += displays[d][1] + 1;
		}

		//println(frameRate); // How are we doing?
		
		// Copy LED color data to prior frame array for next pass
		arraycopy(ledColor, 0, prevColor, 0, ledColor.length);
	}


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
		
		String[] processingArgs = { "MySketch" };
		
		Main mySketch = new Main();
		PApplet.runSketch(processingArgs, mySketch);

	}

}