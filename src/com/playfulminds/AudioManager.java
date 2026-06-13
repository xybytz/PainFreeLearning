package com.playfulminds;

import java.awt.Toolkit;

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
        if (soundId.equals("success")) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    /**
     * Plays an instruction sound.
     * @param instructionId The identifier of the instruction.
     */
    public void playInstruction(String instructionId) {
        System.out.println("Audio: Playing instruction -> " + instructionId);
    }
}