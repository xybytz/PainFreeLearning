package com.playfulminds;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Tracks parent progress behind the scenes.
 * Logs first-try accuracy per object type.
 */
public class ProgressTracker {
    private static ProgressTracker instance = new ProgressTracker();
    
    private Map<VisualAsset.ObjectType, Integer> firstTrySuccesses = new EnumMap<>(VisualAsset.ObjectType.class);
    private Map<VisualAsset.ObjectType, Integer> firstTryAttempts = new EnumMap<>(VisualAsset.ObjectType.class);

    private ProgressTracker() {}

    public static ProgressTracker getInstance() {
        return instance;
    }

    public void recordResult(VisualAsset.ObjectType type, boolean firstTrySuccess) {
        firstTryAttempts.put(type, firstTryAttempts.getOrDefault(type, 0) + 1);
        if (firstTrySuccess) {
            firstTrySuccesses.put(type, firstTrySuccesses.getOrDefault(type, 0) + 1);
        }
    }

    public void generateSessionReport() {
        System.out.println("\n--- Parent Session Report ---");
        try (PrintWriter out = new PrintWriter(new FileWriter("session_report.txt"))) {
            out.println("=== Playful Minds Session Report ===");
            for (VisualAsset.ObjectType type : VisualAsset.ObjectType.values()) {
                int attempts = firstTryAttempts.getOrDefault(type, 0);
                if (attempts > 0) {
                    int successes = firstTrySuccesses.getOrDefault(type, 0);
                    int percentage = (int) ((successes / (double) attempts) * 100);
                    String line = String.format("%-10s : %d%% accuracy (%d/%d first-try successes)", 
                        type.name(), percentage, successes, attempts);
                    System.out.println(line);
                    out.println(line);
                }
            }
            System.out.println("-----------------------------\nReport saved to session_report.txt");
        } catch (IOException e) {
            System.err.println("Failed to write session report.");
        }
    }
}