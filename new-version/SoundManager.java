import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * Manages all sound effects and music for the game.
 * Handles loading, playing, and stopping of audio resources.
 */
public class SoundManager {
    private static Clip menuMusic;
    private static Clip gameMusic;
    private static Clip buttonSound;
    private static Clip paddleHitSound;
    private static Clip gameOverSound;
    private static Clip ballBounceSound;

    /**
     * Initializes all sound resources for the game.
     * Loads audio files from the sound_files directory using ClassLoader.
     * Automatically starts playing game music upon successful initialization.
     */
    public static void init() {
        try {
            // Get the current working directory and print it for debugging
            String currentPath = new File(".").getAbsolutePath();
            System.out.println("Current working directory: " + currentPath);

            ClassLoader classLoader = SoundManager.class.getClassLoader();

            // Load game music
            File gameMusicFile = new File(classLoader.getResource("sound_files/game_music.wav").getFile());
            AudioInputStream gameMusicStream = AudioSystem.getAudioInputStream(gameMusicFile);
            gameMusic = AudioSystem.getClip();
            gameMusic.open(gameMusicStream);

            // Load ball hit sound
            File ballHitFile = new File(classLoader.getResource("sound_files/ball_hit.wav").getFile());
            AudioInputStream paddleStream = AudioSystem.getAudioInputStream(ballHitFile);
            paddleHitSound = AudioSystem.getClip();
            paddleHitSound.open(paddleStream);

            // Load button sound
            File buttonFile = new File(classLoader.getResource("sound_files/ball_hit.wav").getFile());
            AudioInputStream buttonStream = AudioSystem.getAudioInputStream(buttonFile);
            buttonSound = AudioSystem.getClip();
            buttonSound.open(buttonStream);

            // Load ball bounce sound
            File ballBounceFile = new File(classLoader.getResource("sound_files/ball_bounce.wav").getFile());
            AudioInputStream bounceStream = AudioSystem.getAudioInputStream(ballBounceFile);
            ballBounceSound = AudioSystem.getClip();
            ballBounceSound.open(bounceStream);

            // Start playing game music immediately
            playGameMusic();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound files: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Starts playing menu background music in a loop.
     */
    public static void playMenuMusic() {
        if (menuMusic != null) {
            menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops the currently playing menu music.
     */
    public static void stopMenuMusic() {
        if (menuMusic != null) {
            menuMusic.stop();
        }
    }

    /**
     * Starts playing game background music in a loop.
     * Resets to beginning before playing.
     */
    public static void playGameMusic() {
        if (gameMusic != null) {
            gameMusic.setFramePosition(0);
            gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops the currently playing game music.
     */
    public static void stopGameMusic() {
        if (gameMusic != null) {
            gameMusic.stop();
        }
    }

    /**
     * Plays the button click sound effect.
     * Used for menu navigation and UI interactions.
     */
    public static void playButtonSound() {
        if (buttonSound != null) {
            buttonSound.setFramePosition(0);
            buttonSound.start();
        }
    }

    /**
     * Plays the paddle hit sound effect.
     * Used when the ball collides with a paddle.
     */
    public static void playPaddleHitSound() {
        if (paddleHitSound != null) {
            try {
                paddleHitSound.setFramePosition(0);
                paddleHitSound.start();
            } catch (Exception e) {
                System.err.println("Error playing paddle hit sound: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Paddle hit sound not loaded. Check if sound_files/ball_hit.wav exists.");
        }
    }

    /**
     * Plays the game over sound effect.
     * Used when a player wins the game.
     */
    public static void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }

    /**
     * Plays the ball bounce sound effect.
     * Used when transitioning from menu to game state.
     */
    public static void playBallBounceSound() {
        if (ballBounceSound != null) {
            ballBounceSound.setFramePosition(0);
            ballBounceSound.start();
        }
    }
}