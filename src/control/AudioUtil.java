package control;

import exception.AudioUtilException;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioUtil {
	public static final int SFX_COUNT = 6;
	public static final int SFX_WOW = 0;
	public static final int SFX_BRUH = 1;	
	public static final int SFX_KHALED = 2;
	public static final int SFX_CLICK = 3;	
	public static final int SFX_HOVER = 4;
	public static final int SFX_SELECT = 5;

	public static final int MUSIC_COUNT = 2;
	public static final int MUSIC_GAME = 0;
	public static final int MUSIC_MENU = 1;
	
	private static MediaPlayer[] music;
	private static AudioClip[] sfx;
	private static int currentMusic;
	
	public static void init() {
		sfxInit();
		musicInit();
		currentMusic = -1;
	}
	
	public static void musicInit() {
		music = new MediaPlayer[MUSIC_COUNT];
    	for(int i = 0; i < MUSIC_COUNT; i++) {
    		music[i] = new MediaPlayer(new Media(ClassLoader.getSystemResource("musics/music" + i + ".mp3").toString()));
    		music[i].setVolume(0.5);
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
		music[type].setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	if(type == MUSIC_MENU) {
	        		music[type].seek(Duration.seconds(22));
	        	}
	        	else {
	        		music[type].seek(Duration.seconds(0));	        		
	        	}
	        }
	    });
		music[type].play();
		currentMusic = type;
	}
	
	public static void stopMusic() {
		try {
			if(isPlayingMusic()) {
				music[currentMusic].stop();
				currentMusic = -1;
			}
		} catch (AudioUtilException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPlayingMusic() throws AudioUtilException {
		if(currentMusic == -1) return false;
		else if(currentMusic >= 0 && currentMusic < MUSIC_COUNT) return true;
		else {
			throw new AudioUtilException("Current music is nonexistent");
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
