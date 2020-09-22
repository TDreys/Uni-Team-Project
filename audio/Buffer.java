package audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.system.MemoryUtil;
import org.newdawn.slick.openal.WaveData;;


public class Buffer
{
	
	private static List<Integer> buffers = new ArrayList<Integer>();
	static long device;
	static long context;
	static ALCCapabilities alcCapabilities;
	static ALCapabilities alCapabilities;
	
	public static int bgBuffer;
	public static int movementBuffer;
	public static int clickButtonBuffer;
	public static int fireBuffer;
	public static int pickUpBuffer;
	public static int explosionBuffer;
	public static int outoffuelBuffer;
	
	
	/**
	 * initial buffers setting
	 */
	public static void init() {
		String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		device = ALC10.alcOpenDevice(defaultDeviceName);
		alcCapabilities = ALC.createCapabilities(device);

		context = ALC10.alcCreateContext(device, (IntBuffer) null);
		ALC10.alcMakeContextCurrent(context);

		alCapabilities = AL.createCapabilities(alcCapabilities);
	}
	
	
	/**
	 * loading all wav file to buffers
	 * @throws FileNotFoundException
	 */
	public static void loadALData() throws FileNotFoundException {
		movementBuffer = Buffer.loadSound("src/res/tank_movement.wav");
		bgBuffer= Buffer.loadSound("src/res/backgrand.wav");
		clickButtonBuffer = Buffer.loadSound("src/res/click.wav");
		fireBuffer = Buffer.loadSound("src/res/tank-firing.wav");
		pickUpBuffer = Buffer.loadSound("src/res/Upper01.wav");
		explosionBuffer = Buffer.loadSound("src/res/explode.wav");
		outoffuelBuffer = Buffer.loadSound("src/res/outoffuel.wav");
		
	}

	
	 /**
     * this method is called to load sound effect files to buffer
     * called with glow callback
     * @param file the file you want to load
     */
	public static int loadSound(String file) throws FileNotFoundException {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
		WaveData waveFile = WaveData.create(is);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
		
	}
	

	/**
	 * destroy all data in buffer
	 */
	public static void cleanup() {
		ALC10.alcMakeContextCurrent(MemoryUtil.NULL);
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
}
