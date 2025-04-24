import javax.sound.sampled.*;
import java.io.*;

// Wrote by Yi Jie
// Model and Singleton
public class SoundManager {
    public static void playSound(String soundFile) {
        try {
            File file = new File(soundFile); // Load the sound file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip(); // Create a Clip object to play the sound 
            clip.open(audioStream); // Open the audio stream
            clip.start(); // Start playing the sound
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Handle errors and print the exception detail
        }
    }
}