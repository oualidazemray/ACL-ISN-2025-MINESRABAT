# Lo3ba Game - Devil Evil 2 Style

A challenging 2D platformer game inspired by Devil Evil 2, featuring precise platforming mechanics, deadly spikes, and 10 increasingly difficult levels.

## Features

- **10 Challenging Levels**: Each level introduces new obstacles and requires precise timing
- **Simple Controls**:
  - Arrow Keys or A/D - Move left/right
  - Space/W/Up Arrow - Jump
- **Physics-Based Movement**: Gravity, momentum, and jump mechanics
- **Death Counter**: Track your deaths as you master each level
- **Sound Effects**: Jump, death, and level completion sounds
- **Auto-Respawn**: Automatically respawn after death

## How to Build and Run

### Prerequisites

- Java 11 or higher
- Maven

### Build

```bash
mvn clean compile
```

### Run

```bash
mvn exec:java -Dexec.mainClass="com.lo3ba.Main"
```

Or compile and run with:

```bash
mvn clean package
java -cp target/classes com.lo3ba.Main
```

## Game Mechanics

- **Platforms**: Gray blocks you can land on
- **Spikes**: Red triangular hazards that kill on contact
- **Door**: Green exit - reach it to complete the level
- **Death**: Fall off screen or touch spikes to die and respawn

## Level Progression

1. **Level 1**: Tutorial - Basic platforming
2. **Level 2**: Gaps and timing
3. **Level 3**: Vertical challenges
4. **Level 4**: Narrow platforms
5. **Level 5**: Multiple paths
6. **Level 6**: Precision descent
7. **Level 7**: Maze navigation
8. **Level 8**: Extreme precision
9. **Level 9**: Ultimate timing
10. **Level 10**: Final boss level - Everything combined!

## Tips

- Take your time on narrow platforms
- Watch for spikes on platforms
- Use momentum for longer jumps
- Practice makes perfect - don't give up!

## Project Structure

```
src/main/java/com/lo3ba/
├── Main.java                 # Entry point
├── core/
│   ├── GameLoop.java        # Main game loop and rendering
│   ├── Player.java          # Player physics and controls
│   ├── InputHandler.java    # Keyboard input handling
│   └── LevelManager.java    # Level progression management
└── levels/
    ├── Level.java           # Abstract base level class
    ├── Level1.java          # Level implementations
    ├── Level2.java
    └── ... (Level3-10)
```

## Credits

Created as a tribute to Devil Evil 2's challenging platformer gameplay.
