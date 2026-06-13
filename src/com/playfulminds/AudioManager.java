package com.playfulminds;

import java.awt.Toolkit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

/**
 * A utility class to play sounds for auditory feedback and instructions.
 */
public class AudioManager {

    public AudioManager() {
        // Initialize audio system if necessary
    }

    /**
     * Plays a sound effect based on the soundId.
     * @param soundId The identifier of the sound to play.
     */
    public void playSound(String soundId) {
        System.out.println("Audio: Playing sound effect -> " + soundId);
        try {
            URL url = getClass().getResource("resources/" + soundId + ".wav");
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } else {
                System.err.println("Audio file not found for: " + soundId);
                if (soundId.equals("success")) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (soundId.equals("success")) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    
    /**
     * Plays an instruction sound.
     * @param instructionId The identifier of the instruction.
     */
    public void playInstruction(String instructionId) {
        System.out.println("Audio: Playing instruction -> " + instructionId);
        try {
            URL url = getClass().getResource("resources/" + instructionId + ".wav");
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } else {
                System.err.println("Instruction audio file not found for: " + instructionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}