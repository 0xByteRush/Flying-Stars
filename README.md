# Flying Stars Game

[![Build](https://github.com/0xByteRush/Flying-Stars/actions/workflows/build.yml/badge.svg)](https://github.com/0xByteRush/Flying-Stars/actions/workflows/build.yml)

## Project Overview

Flying Stars Game is an interactive JavaFX arcade-style game where players navigate a colored ball to touch stars with matching colors while avoiding incorrect color interactions.

## Game Mechanics

- **Objective**: Collect stars with colors matching your ball's color
- **Scoring**:
  - Regular color match: +1 point
  - Rainbow star match: +5 points
- **Lives**: Start with 3 lives
- **Penalty**: Touching a star with a non-matching color loses a life

## Features

- Dynamic star generation
- Color-based scoring system
- Player score tracking
- Leaderboard functionality
- Persistent high score storage

## Game Screens

1. **Start Page**
   - Enter player name
   - Access leaderboard
   - Start game

2. **Game Screen**
   - Drag ball to move
   - Collect matching color stars
   - Track score and reaction time
   - Limited lives

3. **Leaderboard**
   - Display top 5 players
   - Sorted by highest scores

4. **End Screen**
   - Show final score
   - Options to restart or view leaderboard

## Technical Details

- **Language**: Java 21
- **Framework**: JavaFX 21 (pulled in by Maven, no separate SDK install)
- **Data Persistence**: CSV leaderboard at `~/.flying-stars/scores.csv`, created on first save
- **Key Classes** (`src/main/java/flyingstars/`):
  - `FlyingStars`: application entry point, screens and game loop
  - `Star`: star generation and growth
  - `Ball`: player-controlled ball
  - `Player`: name/score record, sorted by score
  - `ScoreFile`: loading and saving the leaderboard

## Setup and Running

### Prerequisites
- JDK 21 or newer
- Maven 3.9+ (or open the project in IntelliJ IDEA, which bundles Maven)

### Run

```bash
mvn javafx:run
```

### Test

```bash
mvn verify
```

## Contributors
- Abdulaaziz AlKhatib
- Yaseen AlYaseen
- Hamza Ammous

## License

[MIT](LICENSE)
