package game.schoolescape.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    private Music music;
    public Sound jump, shoot, collect5, collect2, hit, levelUp;

    public boolean isMusicOn = true;
    public boolean isSoundOn = true;

    public SoundManager() {
        try {
            music = Gdx.audio.newMusic(Gdx.files.internal(FilePaths.MUSIC));
            music.setLooping(true);
            music.setVolume(0.7f);
        } catch (Exception e) {
            System.out.println("Music not loaded: " + e.getMessage());
        }

        try {
            jump = Gdx.audio.newSound(Gdx.files.internal(FilePaths.JUMP));
            shoot = Gdx.audio.newSound(Gdx.files.internal(FilePaths.SHOOT));
            collect5 = Gdx.audio.newSound(Gdx.files.internal(FilePaths.COLLECT_5));
            collect2 = Gdx.audio.newSound(Gdx.files.internal(FilePaths.COLLECT_2));
            hit = Gdx.audio.newSound(Gdx.files.internal(FilePaths.HIT_BOOK));
            levelUp = Gdx.audio.newSound(Gdx.files.internal(FilePaths.LEVEL_UP));
        } catch (Exception e) {
            System.out.println("Sounds not loaded: " + e.getMessage());
        }

        playMusic();
    }

    public void playMusic() {
        if (isMusicOn && music != null) {
            music.play();
        }
    }

    public void stopMusic() {
        if (music != null) {
            music.stop();
        }
    }

    public void dispose() {
        if (music != null) music.dispose();
        if (jump != null) jump.dispose();
        if (shoot != null) shoot.dispose();
        if (collect5 != null) collect5.dispose();
        if (collect2 != null) collect2.dispose();
        if (hit != null) hit.dispose();
        if (levelUp != null) levelUp.dispose();
    }
}
