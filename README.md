# Playful Minds: At-Home Early Intervention

## Hackathon Prompt
Reimagine early childhood learning with interactive experiences designed around sound and visuals instead of reading. Use creative game mechanics and playful rewards to make learning engaging and memorable.

## The Pitch: Democratizing Early Intervention
*   **The Problem:** Private Speech-Language and Early Intervention Behavioral Therapy often costs **$150 to $250 per hour**. Worse, waitlists for public evaluations are often 6 to 12 months long. During those months, a child is missing critical neurodevelopmental windows.
*   **The Solution:** *Playful Minds* is a free, at-home, clinically-inspired tool. While parents sit on waitlists and stare down impossible therapy bills, they can use this app to begin reinforcing fundamental cognitive and receptive language skills today.
*   **The Market:** Millions of parents waiting for evaluations or unable to afford private care. By removing all text, the app allows pre-literate and neurodivergent children to build skills independently, without adding "extraneous cognitive overload" (Sweller, 1988).

## Core Mechanic: Clinical Roots, Playful Execution
The core gameplay loop is the digital equivalent of **Discrete Trial Training (DTT)**, a primary technique used by clinical therapists to build receptive language:
1.  **Audio Prompt:** "Find the apple!" (Testing Receptive Vocabulary).
2.  **Visual Selection:** The child sees clear, high-contrast, text-free options. The app features a rich vocabulary of 20 stylized, recognizable everyday objects: **Apple, Tree, Star, Cup, Ball, Car, Shoe, House, Bird, Fish, Cat, Flower, Sun, Moon, Boat, Hat, Sock, Chair, Clock, and Umbrella**.
3.  **Positive Reinforcement:** Immediate auditory success chimes and an expanding visual reward, reinforcing the neural pathway.

### Advanced Clinical Features
*   **Errorless Learning (Auto-Prompting):** If the child is idle for 5 seconds, the app automatically replays the audio instruction and triggers a subtle, guiding visual "pulse" on the correct object.
*   **Token Economy:** A persistent visual token board (5 stars) tracks progress. Earning 5 tokens triggers a massive, full-screen confetti celebration, teaching delayed gratification.
*   **Parent Progress Tracking:** Behind the scenes, the app logs first-try accuracy for each vocabulary word. On exit, it generates a `session_report.txt` file so parents know exactly which concepts need more real-world reinforcement.

## Tech Stack & Constraints
* **Language/UI:** Java / Swing
* **Concurrency:** Strictly single-threaded. **NO explicit multithreading (`Thread`, `Runnable`, `ExecutorService`).** All state mutations and UI updates must happen safely on the Swing Event Dispatch Thread (EDT).
* **Timing/Animations:** Use `javax.swing.Timer` exclusively for game loops, animations, or delayed events to maintain thread safety.

## Project Structure
* `src/com/playfulminds/Main.java`: Application entry point. Initializes the `JFrame` and sets up the main container on the EDT.
* `src/com/playfulminds/GamePanel.java`: The core `JPanel` where custom rendering (`paintComponent`) and interactions (`MouseListener`, `KeyListener`) occur. Includes a toggle for a High-Contrast Sensory Mode.
* `src/com/playfulminds/GameController.java`: Manages the game loop using `javax.swing.Timer`. Generates prompts, handles visual token scoring (no numbers/text), and draws rewards.
* `src/com/playfulminds/AudioManager.java`: Handles auditory prompts and success feedback using thread-safe `Toolkit` beeps and simulated instructions.
* `src/com/playfulminds/VisualAsset.java`: A class representing the interactive objects containing bounding boxes and colors.
* `src/com/playfulminds/ProgressTracker.java`: A background singleton that silently logs first-try accuracy and generates a session report upon application exit.

## Audio Assets & TTS Generation
To fulfill the "no reading" core mechanic, auditory prompts are generated dynamically. We created a bulk generation script (`generate_tts.py`) to create `.wav` audio files for all 20 of our everyday objects. The script utilizes the macOS native `say` utility with the 'Samantha' voice to generate high-quality text-to-speech files (e.g. "Find the apple"), and then converts them from AIFF to standard `44.1kHz UI8 WAVE` format using `afconvert` so they are fully compatible with Java's `javax.sound.sampled.AudioSystem`. These `.wav` files are loaded dynamically at runtime via the `AudioManager` using the `resources` classpath.
