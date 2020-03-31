package control;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioUtil {
	
	public static final int SFX_COUNT = 3;
	public static final int SFX_WOW = 0;
	public static final int SFX_BRUH = 1;	
	public static final int SFX_KHALED = 2;
	
	private static MediaPlayer mediaPlayer;
	private static AudioClip[] sfx;
	private static MediaPlayer die;
	
	
	public static void init() {
		sfxInit();
		musicInit();
	}
	
	public static void musicInit() {
		Media h = new Media(ClassLoader.getSystemResource("musics/bgm.mp3").toString());
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setVolume(0.25);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	mediaPlayer.seek(Duration.ZERO);
	        	mediaPlayer.play();
	        }
	    });
		mediaPlayer.play();
	}
	public static void dyingFX()
	{
		Media j  =  new Media(ClassLoader.getSystemResource("musics/sfx4.mp3").toString());
		die = new MediaPlayer(j);
		die.isAutoPlay();
	}
	public static void sfxInit() {
		sfx = new AudioClip[3];
    	for(int i = 0; i < SFX_COUNT; i++) {
    		String url = ClassLoader.getSystemResource("musics/sfx" + i + ".mp3").toString();
    		sfx[i] = new AudioClip(url);
//    		sfx[i] = new AudioClip(("musics/sfx" + i + ".mp3"));
//    		sfx[i] = new AudioClip("musics/sfx" + i + ".mp3");
    		sfx[i].setVolume(1);
    	}
//		sfx[0] = new AudioClip("musics/sfx" + 0 + ".mp3");
//		sfx[0].play();
		
	}
	
	public static void musicStop() {
		mediaPlayer.stop();		
	}
	
	public static void playSFX(int type) {
		sfx[type].play();
	}
	
	public static void stopSFX() {
		for(AudioClip a : sfx) {
			a.stop();
		}
	}
	
	public static void stopAudio() {
		musicStop();
		stopSFX();
	}
}
