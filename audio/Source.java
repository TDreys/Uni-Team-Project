package audio;

import org.lwjgl.openal.*;

public class Source
{
	private int sourceId;
	
	public static Source bgSou;
	public static Source movSou;
	public static Source clickSou;
	public static Source fireSou;
	public static Source picSou;
	public static Source expSou;
	public static Source oofSou;
	public static int bgcount = 0;
	public static boolean on = true;
	
	
	/**
	 * set buffer data as source
	 */
	public Source() {
		sourceId = AL10.alGenSources();
	}
	
	/**
	 * play sound effect
	 * @param buffer the buffer you want to input
	 */
	public void play(int buffer) {
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	/**
	 * pause audio
	 */
	public void pause() {
		AL10.alSourcePause(sourceId);
	}
	
	/**
	 * continue playing audio
	 */
	public void continuePlaying() {
		AL10.alSourcePlay(sourceId);
	}
	
	/**
	 * stop playing audio
	 */
	public void stop() {
		AL10.alSourceStop(sourceId);
	}
	
	/**
	 * destroy audio data
	 */
	public void delete() {
		AL10.alDeleteSources(sourceId);
	}
	
	/**
	 * set audio volume
	 * @param volume the audio volume value
	 */
	public void setVolume(float volume) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}
	
	/*
	 * set audio pitch
	 * @param pitch the audio pitch value
	 */
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	/**
	 * looping audio play again and again if it's set true
	 * @param loop <code>Boolean.TURN</code> if want to set audio looping, else <code>Boolean.FALSE</code>
	 */
	public void setLopping(boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	/**
	 * check is current audio playing
	 * @return the state of playing audio currently (<code>Boolean.TURN</code> if it's Playing, else <code>Boolean.FALSE</code>)
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	

	
	public static void init() {
		bgSou = new Source();
		bgSou.setVolume(0.1f);
		bgSou.setLopping(true);
		
		movSou = new Source();
		movSou.setVolume(0.05f);
		movSou.setLopping(true);
		
		clickSou = new Source();
		clickSou.setVolume(0.3f);
		clickSou.setLopping(false);
		
		fireSou = new Source();
		fireSou.setVolume(0.2f);
		fireSou.setLopping(false);
		
		picSou = new Source();
		picSou.setVolume(0.5f);
		picSou.setLopping(false);
		
		expSou = new Source();
		expSou.setVolume(0.3f);
		expSou.setLopping(false);
		
		oofSou = new Source();
		oofSou.setVolume(0.3f);
		oofSou.setLopping(false);
	}
	
	public static void destroy() {
		bgSou.delete();
		movSou.delete();
		clickSou.delete();
		fireSou.delete();
		picSou.delete();
		expSou.delete();
		oofSou.delete();
	}



}
