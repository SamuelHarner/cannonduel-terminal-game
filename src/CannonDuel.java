
/**
 * @author David Ekman, Samuel Härner
 * 
 * Help/inspiration used: https://github.com/nikolaydimov83/SnakeGame1
 */

import java.nio.charset.Charset;
import java.util.*;
import java.io.IOException;
import java.lang.*;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class CannonDuel {
    private final static int fieldCols = 60; // horizontal size of field
    private final static int fieldRows = 20; // vertical size of field
    private char[][] gameField;
    private ArrayList<Item> activeItems;
    private Player p1;
    private Player p2;
    private boolean gameIsOn = true;
    private long previousGameTime;
    private long previousCannonTime;

    public String runGame() throws IOException {
        if (Math.abs(System.currentTimeMillis() - previousGameTime) > 50) {
            previousGameTime = System.currentTimeMillis();

            // Check if a player will be hit with an item, if so game over
            boolean player1Collide = this.checkItemPlayerCollision(p1);
            boolean player2Collide = this.checkItemPlayerCollision(p2);
            if (player1Collide && player2Collide) {
                gameIsOn = false;
                drawGameOverMsg("It's a draw!");
                return gameFieldToString();
            } else if (player1Collide) {
                gameIsOn = false;
                drawGameOverMsg("Player H wins!");
                return gameFieldToString();
            } else if (player2Collide) {
                gameIsOn = false;
                drawGameOverMsg("Player X wins!");
                return gameFieldToString();
            }
            // Move any active items
            this.moveActiveItems();
        }

        if (Math.abs(System.currentTimeMillis() - previousCannonTime) > 5000) {
            previousCannonTime = System.currentTimeMillis();
            this.generateCannonBalls();
        }

        // Draw new game field
        return gameFieldToString();
    }

    public CannonDuel() {
        gameField = new char[fieldRows][fieldCols];
        activeItems = new ArrayList<Item>();

        // Initiate Players
        Random r = new Random();
        int colNumberP1 = r.nextInt((fieldCols - 1) - 1) + 1;
        int rowNumberP1 = r.nextInt((fieldRows - 1) - 1) + 1;
        p1 = new Player(colNumberP1, rowNumberP1, 'X');
        int colNumberP2 = r.nextInt((fieldCols - 1) - 1) + 1;
        int rowNumberP2 = r.nextInt((fieldRows - 1) - 1) + 1;
        p2 = new Player(1, 1, 'H');
        if (colNumberP1 != colNumberP2 || rowNumberP1 != rowNumberP2) {
            p2.setPlayerPos(colNumberP2, rowNumberP2);
        }

        previousGameTime = System.currentTimeMillis();
        previousCannonTime = System.currentTimeMillis();
    }

    public void initiateField() {
        this.generateField();
        this.initiatePlayer(p1);
        this.initiatePlayer(p2);
    }

    public boolean checkItemPlayerCollision(Player player) {
        boolean collisionFlag = false;
        int playerXPos = player.getXPos();
        int playerYPos = player.getYPos();

        // Keep track of collided items
        List<Item> itemsToRemove = new ArrayList<Item>();

        for (Item item : activeItems) {
            if ((item.getXPos() == playerXPos) && (item.getYPos() == playerYPos)) {
                collisionFlag = true;
                drawItemPlayerCollision(item, player);
                itemsToRemove.add(item);
            }
        }
        activeItems.removeAll(itemsToRemove);

        return collisionFlag;
    }

    public void handleItemWallCollision() {
        for (Item item : activeItems) {
            if (gameField[item.getNextYPos()][item.getNextXPos()] == '#') {
                if (item.getDirection().equals("left")) {
                    item.setDirection("right");
                } else if (item.getDirection().equals("right")) {
                    item.setDirection("left");
                } else if (item.getDirection().equals("up")) {
                    item.setDirection("down");
                } else if (item.getDirection().equals("down")) {
                    item.setDirection("up");
                }
            }
        }
    }

    public void drawItemPlayerCollision(Item item, Player player) {
        gameField[player.getYPos()][player.getXPos()] = ' ';
        gameField[item.getYPos()][item.getXPos()] = ' ';
    }

    public void moveActiveItems() {
        handleItemWallCollision();
        for (Item item : activeItems) {
            item.moveItem(gameField);
        }
    }

    public void generateField() {
        // Prepare each position on field as a blankspace
        for (int x = 0; x < fieldCols; x++) {
            for (int y = 0; y < fieldRows; y++) {
                gameField[y][x] = ' ';
            }
        }

        // ADD GAME FIELD WALLS
        // Add characters for left field wall
        for (int i = 0; i < fieldRows; i++) {
            gameField[i][0] = '#';
        }
        // Add characters for right field wall
        for (int i = 0; i < fieldRows; i++) {
            gameField[i][fieldCols - 1] = '#';
        }
        // Add characters for upper field wall
        for (int i = 0; i < fieldCols; i++) {
            gameField[0][i] = '#';
        }
        // Add characters for lower field wall
        for (int i = 0; i < fieldCols; i++) {
            gameField[fieldRows - 1][i] = '#';
        }
    }

    public void generateCannonBalls() {
        Random r = new Random();
        int ballAmount = r.nextInt(10);
        for (int i = 0; i < ballAmount; i++) {
            int bally = r.nextInt(fieldRows);
            int ballx = r.nextInt(fieldCols);
            if (gameField[bally][ballx] == ' ') {
                gameField[bally][ballx] = 'o';
            }
        }
    }

    public void generateRandomWalls() {
        Random r = new Random();
        int walls = r.nextInt(10) + 5;
        for (int i = 0; i < walls; i++) {
            int bally = r.nextInt(fieldRows);
            int ballx = r.nextInt(fieldCols);
            if (gameField[bally][ballx] == ' ') {
                gameField[bally][ballx] = '#';
            }
        }
    }

    public void player1Action(String command) {
        playerAction(p1, command);
    }

    public void player2Action(String command) {
        playerAction(p2, command);
    }

    private void playerAction(Player player, String command) {
        switch (command) {
            case "move down":
                if (gameField[player.getYPos() + 1][player.getXPos()] == 'o') {
                    player.addItem(new Item(fieldRows, fieldCols));
                    gameField[player.getYPos() + 1][player.getXPos()] = ' ';
                }
                if (gameField[player.getYPos() + 1][player.getXPos()] == ' ') {
                    gameField[player.getYPos()][player.getXPos()] = ' ';
                    player.setPlayerPos(player.getXPos(), player.getYPos() + 1);
                    gameField[player.getYPos()][player.getXPos()] = player.getName();
                    player.setDirection("down");
                }
                break;
            case "move up":
                if (gameField[player.getYPos() - 1][player.getXPos()] == 'o') {
                    player.addItem(new Item(fieldRows, fieldCols));
                    gameField[player.getYPos() - 1][player.getXPos()] = ' ';
                }
                if (gameField[player.getYPos() - 1][player.getXPos()] == ' ') {
                    gameField[player.getYPos()][player.getXPos()] = ' ';
                    player.setPlayerPos(player.getXPos(), player.getYPos() - 1);
                    gameField[player.getYPos()][player.getXPos()] = player.getName();
                    player.setDirection("up");
                }
                break;
            case "move right":
                if (gameField[player.getYPos()][player.getXPos() + 1] == 'o') {
                    player.addItem(new Item(fieldRows, fieldCols));
                    gameField[player.getYPos()][player.getXPos() + 1] = ' ';
                }
                if (gameField[player.getYPos()][player.getXPos() + 1] == ' ') {
                    gameField[player.getYPos()][player.getXPos()] = ' ';
                    player.setPlayerPos(player.getXPos() + 1, player.getYPos());
                    gameField[player.getYPos()][player.getXPos()] = player.getName();
                    player.setDirection("right");
                }
                break;
            case "move left":
                if (gameField[player.getYPos()][player.getXPos() - 1] == 'o') {
                    player.addItem(new Item(fieldRows, fieldCols));
                    gameField[player.getYPos()][player.getXPos() - 1] = ' ';
                }
                if (gameField[player.getYPos()][player.getXPos() - 1] == ' ') {
                    gameField[player.getYPos()][player.getXPos()] = ' ';
                    player.setPlayerPos(player.getXPos() - 1, player.getYPos());
                    gameField[player.getYPos()][player.getXPos()] = player.getName();
                    player.setDirection("left");
                }
                break;
            case "use ball":
                if (player.getNumItems() > 0) {
                    Item currentItem = player.getItemList().get(player.getNumItems() - 1);
                    activeItems.add(currentItem);
                    player.removeItem(currentItem);
                    currentItem.useItem(player.getXPos(), player.getYPos(), player.getDirection());
                }
                break;
        }
    }

    /**
     * @return a string that contains the graphical representation of the game field
     */
    public String gameFieldToString() {
        StringBuilder gameFieldStringBuilder = new StringBuilder();

        // Add every character from gameField matrix
        for (int y = 0; y < fieldRows; y++) {
            for (int x = 0; x < fieldCols; x++) {
                gameFieldStringBuilder.append(gameField[y][x]);
                if (x == (fieldCols - 1)) {
                    gameFieldStringBuilder.append('\n');
                }
            }
        }

        return gameFieldStringBuilder.toString();
    }

    /**
     * Draws the desired game over message on the game field
     * 
     * @param msg the game over message to be displayed, 20 char length limit
     */
    private void drawGameOverMsg(String msg) {
        // Set constants use for indentation
        int rowStart = 5;
        int colStart = 5;

        int msgLimit = 20;
        int msgLength = msg.length();

        if (msgLength < 20) {
            msgLimit = msgLength;
        }

        for (int i = 0; i < msgLimit; i++) {
            gameField[rowStart][colStart + i] = msg.charAt(i);
        }
    }

    public void initiatePlayer(Player player) {
        if (player.getName() == 'X')
            gameField[player.getYPos()][player.getXPos()] = 'X';
        else
            gameField[player.getYPos()][player.getXPos()] = 'H';
    }

    public boolean getGameIsOn() {
        return gameIsOn;
    }
}
