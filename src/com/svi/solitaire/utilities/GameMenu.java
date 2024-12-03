package com.svi.solitaire.utilities;

import java.util.ArrayDeque;
import java.util.Scanner;
import com.svi.solitaire.resources.GameInfo;
import com.svi.solitaire.logic.DeckInitializer;
import com.svi.solitaire.logic.ShuffleManager;
import com.svi.solitaire.logic.TurnModeHandler;
import com.svi.solitaire.vo.Card;
import com.svi.solitaire.vo.GameState;

public class GameMenu {
    private Scanner userInput;
    private GameInfo about;
    private DeckInitializer deckInitializer;
    private TurnModeHandler turnModeHandler;
    private boolean shuffleDisplaySetting; // Setting to control shuffle display
    private GameState gameState; // GameState instance

    public GameMenu(Scanner userInput, GameInfo about, DeckInitializer deckInitializer) {
        this.userInput = userInput;
        this.about = about;
        this.deckInitializer = deckInitializer;
        this.turnModeHandler = new TurnModeHandler();
        this.shuffleDisplaySetting = false; // Default is off
        this.gameState = new GameState(); // Initialize GameState without TurnModeHandler
    }

    public void start() {
        boolean running = true;
        while (running) {
            int userChoice = displayMainMenu();
            switch (userChoice) {
                case 1:
                    playGame(); // Handles one game per initialization
                    break;
                case 2:
                    about.displayAboutMenu();
                    break;
                case 3:
                    displaySettingsMenu();
                    break;
                case 4:
                    running = false;
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("\nInvalid Option. Please Try Again!");
            }
        }
    }

    private void playGame() {
        int turnMode = displayPlayMenu(); // Get the selected turn mode from the play menu
        if (turnMode != -1) { // Valid turn mode selected
            turnModeHandler.setTurnMode(turnMode); // Set the turn mode dynamically

            // Set the turn mode in the game state
            gameState.setTurnMode(turnModeHandler.getTurnMode()); // Set turn mode in game state

            int shuffleChoice = displayShuffleMenu(turnMode); // Pass turn mode to shuffle menu
            if (shuffleChoice != -1) { // Valid shuffle option selected
                ArrayDeque<Card> shuffledDeck = initializeShuffle(turnMode, shuffleChoice);

                // Validate deck size before distributing cards
                if (shuffledDeck.size() != 52) {
                    throw new IllegalStateException("Shuffled deck contains " + shuffledDeck.size() + " cards. Expected 52 cards.");
                }

                // Print the shuffled deck if shuffle display is enabled
                handleShuffledDeck(shuffledDeck);

                // Reset the game state before distributing new cards
                gameState.resetGameState();
                ShuffleManager.handleShuffledDeck(shuffledDeck, gameState); // Handle distribution of cards

                // After the game, automatically play the game
                gameState.autoPlayGame(); // Start auto-play

                // After the game, prompt the user for further action
                displayPostGameMenu();
            }
        }
    }

    private void displayPostGameMenu() {
        boolean inPostGameMenu = true;
        while (inPostGameMenu) {
            String prompt = "\n========= GAME COMPLETED =========\n"
                    + "(1) PLAY AGAIN (Return to Main Menu)\n"
                    + "(2) EXIT";
            int userChoice = getValidInput(prompt, 1, 2);

            switch (userChoice) {
                case 1:
                    inPostGameMenu = false; // Return to main menu
                    break;
                case 2:
                    System.out.println("\nGoodbye!");
                    System.exit(0); // Exit the application
                    break;
                default:
                    System.out.println("\nInvalid Option. Please Try Again!");
            }
        }
    }

    public int displayMainMenu() {
        String prompt = "\n============== MAIN MENU ==============\n"
                + "(1) PLAY [KLONDIKE SOLITAIRE]\n"
                + "(2) ABOUT [KLONDIKE SOLITAIRE]\n"
                + "(3) SETTINGS\n"
                + "(4) EXIT";
        return getValidInput(prompt, 1, 4);
    }

    public int displayPlayMenu() {
        String prompt = "\n========== KLONDIKE SOLITAIRE ==========\n"
                + "(1) Klondike Turn 1\n"
                + "(2) Klondike Turn 3\n"
                + "(3) RETURN BACK";
        int userChoice = getValidInput(prompt, 1, 3);

        switch (userChoice) {
            case 1:
                return 1; // Set Turn 1
            case 2:
                return 3; // Set Turn 3
            case 3:
                return -1; // Indicate return to main menu
            default:
                return -1; // Fallback to main menu
        }
    }

    public int displayShuffleMenu(int turnMode) {
        String prompt = "\n============ KLONDIKE TURN " + turnMode + " ============\n"
                + "(1) Easy Shuffle (Winnable)\n"
                + "(2) Medium Shuffle (Faro)\n"
                + "(3) Hard Shuffle (Random)\n"
                + "(4) RETURN BACK";
        int userChoice = getValidInput(prompt, 1, 4);

        switch (userChoice) {
            case 1: case 2: case 3:
                return userChoice; // Valid shuffle option
            case 4:
                return -1; // Return back
            default:
                return -1; // Fallback
        }
    }

    private ArrayDeque<Card> initializeShuffle(int turnMode, int shuffleChoice) {
        ArrayDeque<Card> currentDeck = new ArrayDeque<>(deckInitializer.getDeck()); // Convert to ArrayDeque

        switch (shuffleChoice) {
            case 1:
                System.out.println("Starting Easy Shuffle for Turn " + turnMode + "...\n");
                currentDeck = ShuffleManager.easyShuffle(currentDeck);
                break;
            case 2:
                System.out.println("Starting Medium Shuffle for Turn " + turnMode + "...\n");
                currentDeck = ShuffleManager.mediumShuffle(currentDeck);
                break;
            case 3:
                System.out.println("Starting Hard Shuffle for Turn " + turnMode + "...\n");
                currentDeck = ShuffleManager.hardShuffle(currentDeck);
                break;
        }

        return currentDeck;
    }

    public void displaySettingsMenu() {
        boolean inSettingsMenu = true;
        while (inSettingsMenu) {
            String prompt = "\n============= SETTINGS =============\n"
                    + "(1) Show Shuffle Deck [ON/OFF]\n"
                    + "(2) RETURN BACK";
            int userChoice = getValidInput(prompt, 1, 2);

            switch (userChoice) {
                case 1:
                    toggleShuffleDisplaySetting();
                    break;
                case 2:
                    inSettingsMenu = false;
                    break;
            }
        }
    }

    public void toggleShuffleDisplaySetting() {
        shuffleDisplaySetting = !shuffleDisplaySetting; // Toggle between true and false
        String status = shuffleDisplaySetting ? "ON" : "OFF";
        System.out.println("\nShuffle Deck Display is now " + status);
    }

    // This method prints the shuffled deck if the shuffle display is enabled
    private void handleShuffledDeck(ArrayDeque<Card> shuffledDeck) {
        if (shuffleDisplaySetting) { // Check if shuffle display is on
            System.out.println("Shuffled Deck:");
            shuffledDeck.forEach(card -> System.out.print(card + " "));
            System.out.println(); // Newline after printing the deck
        }
    }

    private int getValidInput(String prompt, int min, int max) {
        int userChoice = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.println(prompt);
            System.out.print("\nEnter Key: ");
            try {
                userChoice = userInput.nextInt();
                if (userChoice >= min && userChoice <= max) {
                    validInput = true;
                } else {
                    System.out.println("\nInvalid Option. Please Try Again!");
                }
            } catch (Exception e) {
                System.out.println("\nInvalid Option. Please Try Again!");
                userInput.nextLine();
            }
        }

        return userChoice;
    }
}
