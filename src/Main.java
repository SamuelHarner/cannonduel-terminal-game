import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.nio.charset.Charset;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            // Create terminal
            Terminal terminal = new DefaultTerminalFactory(System.out, System.in, Charset.forName("UTF8"))
                    .createTerminal();
            terminal.enterPrivateMode();
            TerminalSize terminalSize = terminal.getTerminalSize();
            terminal.setCursorPosition(0, 0);
            terminal.setCursorVisible(false);

            // Prepare the game
            CannonDuel game = new CannonDuel();

            game.initiateField();
            game.generateRandomWalls();

            String graphicsString = game.runGame();

            // Run game loop
            KeyStroke keyStroke;
            String player1Command = "";
            String player2Command = "";
            while (game.getGameIsOn()) {
                // Read input
                keyStroke = terminal.pollInput();
                // Check if players want to make a command
                if (keyStroke != null) {
                    player1Command = getPlayer1Command(keyStroke);
                    player2Command = getPlayer2Command(keyStroke);
                }

                if (!player1Command.equals("")) {
                    game.player1Action(player1Command);
                    player1Command = "";
                }

                if (!player2Command.equals("")) {
                    game.player2Action(player2Command);
                    player2Command = "";
                }

                // Update game graphics string
                graphicsString = game.runGame();

                // Print game graphics
                terminal.setCursorPosition​(0, 0);
                Write(graphicsString, terminal);
            }

            // Print game graphics
            terminal.setCursorPosition​(0, 0);
            Write(graphicsString, terminal);
            Thread.sleep(5000); // Sleep for 5 seconds

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getPlayer1Command(KeyStroke keyStroke) {
        String input = "";

        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                input = "move up";
                break;
            case ArrowLeft:
                input = "move left";
                break;
            case ArrowDown:
                input = "move down";
                break;
            case ArrowRight:
                input = "move right";
                break;
            case Character:
                if (keyStroke.getCharacter() == ' ') {
                    input = "use ball";
                }
                break;
        }
        return input;
    }

    private static String getPlayer2Command(KeyStroke keyStroke) {
        String input = "";

        switch (keyStroke.getKeyType()) {
            case Character:
                switch (keyStroke.getCharacter()) {
                    case 'w':
                        input = "move up";
                        break;
                    case 'a':
                        input = "move left";
                        break;
                    case 's':
                        input = "move down";
                        break;
                    case 'd':
                        input = "move right";
                        break;
                    case 'e':
                        input = "use ball";
                        break;
                }
        }
        return input;
    }

    /**
     * Helper method for printing a string to the lanterna terminal
     * Author/source: https://github.com/nikolaydimov83/SnakeGame1
     * 
     * @param print
     * @param terminal
     */
    private static void Write(String print, Terminal terminal) throws IOException {
        char[] printToChar = print.toCharArray();
        for (int i = 0; i < print.length(); i++) {
            terminal.putCharacter(printToChar[i]);
        }
    }
}
