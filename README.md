# CannonDuel
CannonDuel is a 2-player terminal game.

## Game design:
- Game field char matrix with dimension 20x60
- Game field with walls represented by '#'
- Two players (clients) represented by 'X' and 'H'
- Cannon ball ammunition represented by 'o'
- Active fired cannon balls represented by '*'
- Players can pick up unlimited amount of cannon ball ammunition
- A player can win by shooting and hitting the other player with a cannon ball
- Players shoot balls in direction of their latest movement
- Balls bounce off of walls

## Game controls:
### Player 1 (X)
**Move up:** up arrow  
**Move left:** left arrow  
**Move down:** down arrow  
**Move right:** right arrow  
**Shoot ball:** space bar  

### Player 2 (H)
**Move up:** W  
**Move left:** A  
**Move down:** S  
**Move right:** D  
**Shoot ball:** E  

## How to run:
### macOS/Linux
Open a terminal and navigate into the `src` folder and use following command to compile:
```
javac -cp "../lib/lanterna-3.1.1.jar:." *.java
```
Then use following command to run game:
```
java -cp "../lib/lanterna-3.1.1.jar:." Main
```

### Windows
Open a terminal and navigate into the `src` folder and use following command to compile:
```
javac -cp "..\lib\lanterna-3.1.1.jar;." *.java
```
Then use following command to run game:
```
java -cp "..\lib\lanterna-3.1.1.jar;." Main
```
