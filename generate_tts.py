import os
import subprocess

objects = ["APPLE", "TREE", "STAR", "CUP", "BALL", "CAR", "SHOE", "HOUSE", "BIRD", "FISH",
           "CAT", "FLOWER", "SUN", "MOON", "BOAT", "HAT", "SOCK", "CHAIR", "CLOCK", "UMBRELLA"]

os.makedirs("src/com/playfulminds/resources", exist_ok=True)

for obj in objects:
    lower_obj = obj.lower()
    filename = f"find_{lower_obj}"
    text = f"Find the {lower_obj}"
    print(f"Generating: {text} -> {filename}.wav")
    
    # Run 'say' to generate an AIFF file
    subprocess.run(["say", "-v", "Samantha", text, "-o", "temp.aiff"], check=True)
    # Run 'afconvert' to convert it to a WAV file compatible with Java
    subprocess.run(["afconvert", "-f", "WAVE", "-d", "UI8@44100", "temp.aiff", f"src/com/playfulminds/resources/{filename}.wav"], check=True)

if os.path.exists("temp.aiff"):
    os.remove("temp.aiff")

print("All TTS audio files generated successfully.")
