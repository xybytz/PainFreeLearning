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
2.  **Visual Selection:** The child sees clear, high-contrast, text-free options. The app features a rich vocabulary of 10 stylized, recognizable everyday objects: **Apple, Tree, Star, Cup, Ball, Car, Shoe, House, Bird, and Fish**.
3.  **Positive Reinforcement:** Immediate auditory success chimes and an expanding visual reward (golden tokens), reinforcing the neural pathway.

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

## Judging Rubric Alignment
*   **Impact & Practicality (10/10):** Solves a massive, real-world financial burden by offering a high-quality free alternative to $200/hr therapies.
*   **Innovation & Creativity:** Translates clinical DTT therapy into an intuitive, child-led game by removing all text and focusing on cross-modal learning.
*   **Technical Complexity & Execution:** Efficient and strict adherence to single-threaded Swing constraints, utilizing `javax.swing.Timer` for non-blocking asynchronous events.
*   **Functionality & Usability:** Fully functional, deeply intuitive UI designed specifically for toddlers to use independently. Includes an accessible High-Contrast mode.
*   **Relevance:** Directly answers the prompt to reimagine early learning without reading.

## Agent Instructions

As an agent working on this project, you must adhere to very high quality standards.
Follow these guidelines:
	- Be critical!! Do not blindly go along with anything. If there is even a small issue, POINT IT OUT!
	- Do not be lazy. Make sure to run tests and ensure new code is up to standards.