# Flying Stars Game

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

- **Language**: Java
- **Framework**: JavaFX
- **Data Persistence**: CSV file for player scores
- **Key Classes**:
  - `Star`: Star generation and interaction
  - `Ball`: Player-controlled object
  - `Player`: Player data management
  - `Project`: Main game logic

## Setup and Running

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- JavaFX SDK

### Steps
1. Clone the repository
2. Ensure JavaFX is configured in your IDE
3. Set up the `namesScores.csv` file in the specified path
4. Run the `Project` class

## Contributors
- Abdulaaziz AlKhatib
- Yaseen AlYaseen
- Hamza Ammous

## Note
The game's file paths are currently hardcoded and may need adjustment based on your local environment.
