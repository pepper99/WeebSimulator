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

	public static final int BGM_COUNT = 2;
	public static final int BGM_GAME = 0;
	public static final int BGM_MENU = 1;
	
	public static MediaPlayer[] bgm;
	private static AudioClip[] sfx;
	
	public static void init() {
		sfxInit();
		musicInit();
	}
	
	public static void musicInit() {
		bgm = new MediaPlayer[BGM_COUNT];
    	for(int i = 0; i < BGM_COUNT; i++) {
    		bgm[i] = new MediaPlayer(new Media(ClassLoader.getSystemResource("musics/bgm" + i + ".mp3").toString()));
    		bgm[i].setVolume(0.5);
    	}
	}
	
	public static void sfxInit() {
		sfx = new AudioClip[SFX_COUNT];
    	for(int i = 0; i < SFX_COUNT; i++) {
    		String url = ClassLoader.getSystemResource("musics/sfx" + i + ".mp3").toString();
    		sfx[i] = new AudioClip(url);
    		sfx[i].setVolume(1);
    	}
	}
	
	public static void playMusic(int type) {
		stopMusic();
		bgm[type].setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	if(type == BGM_MENU) {
	        		bgm[type].seek(Duration.seconds(22));
	        	}
	        	else {
	        		bgm[type].seek(Duration.seconds(0));	        		
	        	}
	        }
	    });
    	bgm[type].play();
	}
	
	public static void stopMusic() {
		for(MediaPlayer m : bgm) {
			m.stop();
		}
	}
	
	public static void playSFX(int type) {
		sfx[type].play();
	}
	
	public static void stopSFX() {
		for(AudioClip s : sfx) {
			s.stop();
		}
	}
	
	public static void stopAudio() {
		stopMusic();
		stopSFX();
	}
}
