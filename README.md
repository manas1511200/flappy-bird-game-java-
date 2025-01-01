# Flappy Bird Game in Java

This is a simple Flappy Bird game designed for beginners. The project is structured with two main files:

- **App.java**: The main class that initiates and runs the game.
- **FlappyBird.java**: Contains the game logic and is built on top of `JPanel`.

## Features

1. **Score Saving**: The game saves scores and attempt numbers in a folder using the `Files` class.
2. **Bird Rotation**: The bird rotates when you press the "Up" arrow key or the space bar.

The code includes comments for better understanding and is built using Java's Swing and Graphics classes. This project was created for fun and may contain bugs.

## Usage

To run the program, execute the following commands in your terminal:

```bash
[ -d "flappy-bird-game-java-" ] && rm -rf "flappy-bird-game-java-"
git clone https://github.com/manas1511200/flappy-bird-game-java-.git
cd flappy-bird-game-java- || exit
chmod +x ./run.sh && ./run.sh
```

## Improvements

Currently, the game does not include sound effects. Contributions are welcome to add sound or improve other aspects of the game. Feel free to fork the repository and submit a pull request!

---

### Notes
- The game is built for educational purposes and may not be fully optimized.
- If you encounter any issues or have suggestions, please open an issue in the repository.
