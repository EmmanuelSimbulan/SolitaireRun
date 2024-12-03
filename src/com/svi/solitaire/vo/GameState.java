package com.svi.solitaire.vo;

import com.svi.solitaire.utilities.CardRenderer;
import com.svi.solitaire.logic.CardMovementHandler;
import com.svi.solitaire.logic.TurnModeHandler;

import java.util.List;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class GameState {
    private List<ArrayDeque<Card>> tableau; // 7 tableau columns
    private List<ArrayDeque<Card>> foundation; // 4 foundation piles
    private ArrayDeque<Card> talon; // Stock of undealt cards
    private ArrayDeque<Card> waste; // Cards dealt from the talon

    private TurnModeHandler turnModeHandler; // Instance of TurnModeHandler

    // Tracking fields
    private int moves; // Tracks the number of moves
    private int passesThroughTalon; // Tracks how many times the talon has been passed through
    private boolean isInitialStateRendered = false; // Track whether the initial game state has been rendered

    // Constructor now initializes the tracking fields and TurnModeHandler
    public GameState() {
        this.turnModeHandler = new TurnModeHandler(); // Initialize TurnModeHandler
        this.moves = 0; // Initialize move counter
        this.passesThroughTalon = 0; // Initialize passes through talon counter
        resetGameState(); // Keep TurnModeHandler intact, only reset other game state components
    }

    // Method to set the turn mode after the GameState is created
    public void setTurnMode(int turnMode) {
        this.turnModeHandler.setTurnMode(turnMode); // Set the turn mode dynamically
    }

    // Reset the entire game state to start a new game
    public void resetGameState() {
        tableau = new ArrayList<>(7); // Initialize 7 tableau columns
        for (int i = 0; i < 7; i++) {
            tableau.add(new ArrayDeque<>());
        }

        foundation = new ArrayList<>(4); // Initialize 4 foundation piles
        for (int i = 0; i < 4; i++) {
            foundation.add(new ArrayDeque<>());
        }

        talon = new ArrayDeque<>();
        waste = new ArrayDeque<>();
        // Reset moves and passes through talon
        this.moves = 0;
        this.passesThroughTalon = 0;
    }

    // Distribute cards into tableau and talon
    public void distributeCards(ArrayDeque<Card> shuffledDeck) {
        // Reset the game state if necessary
        CardMovementHandler.validateDeck(shuffledDeck); // Validate the shuffled deck

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j <= i; j++) {
                Card card = shuffledDeck.poll(); // Take the next card from the shuffled deck

                CardMovementHandler.validateCardDistribution(card); // Validate card distribution

                if (j == i) {
                    card.setFaceUp(true); // The top card in the column is face-up
                } else {
                    card.setFaceUp(false); // All other cards are face-down
                }
                tableau.get(i).add(card); // Add the card to the current tableau column
            }
        }

        talon.addAll(shuffledDeck); // Remaining cards go to the talon

        // Print the initial game state after distribution (rendering occurs only once)
        if (!isInitialStateRendered) {
            // Only render the game state once after cards are distributed
            CardRenderer.renderTableau(tableau);
            CardRenderer.renderFoundation(foundation);
            CardRenderer.renderTalon(talon, turnModeHandler); // Pass the TurnModeHandler instance to renderer
            isInitialStateRendered = true; // Mark the state as rendered
        }

        // Display MOVES, STOCK, and PASSTHURS only after the initial setup
        printGameStatus();
    }

    // Auto-play the game by automatically making valid moves
    public void autoPlayGame() {
        boolean gameOver = false;

        while (!gameOver) {
            boolean moved = false;

            // Try moving cards from tableau to foundation
            for (ArrayDeque<Card> column : tableau) {
                if (!column.isEmpty()) {
                    // Check the top card in the tableau column (it must be face-up)
                    Card topCard = column.peek();

                    if (topCard.isFaceUp() && addToFoundation(topCard)) { // Move top face-up card to foundation if possible
                        column.pop(); // Remove the card from tableau after moving it
                        moved = true;
                        moves++; // Increment moves
                        // Log the move
                        CardMovementHandler.logCardMovement(topCard, "Foundation", foundation.indexOf(column) + 1); // Foundation index starts from 1
                        break; // Exit the loop to check if a move was made
                    }
                }
            }

            // Try moving cards from talon to tableau or foundation
            if (!moved && !talon.isEmpty()) {
                Card topCard = talon.peek();

                if (addToFoundation(topCard)) { // Try adding talon card to foundation
                    talon.pop(); // Remove the card from talon after moving it
                    moved = true;
                    moves++; // Increment moves
                    // Log the move
                    CardMovementHandler.logCardMovement(topCard, "Foundation", foundation.indexOf(topCard) + 1);
                } else {
                    for (ArrayDeque<Card> column : tableau) { // Try placing it in tableau
                        // Check if the tableau is empty or the card can be placed in descending order with alternating colors
                        if (column.isEmpty()) {
                            // Only Kings can be placed in an empty tableau column
                            if (topCard.getRank() == Rank.KING) {
                                column.push(talon.pop()); // Add the talon card to tableau
                                moved = true;
                                moves++; // Increment moves
                                // Log the move
                                CardMovementHandler.logCardMovement(topCard, "Column", tableau.indexOf(column) + 1); // Column index starts from 1
                                break;
                            }
                        } else {
                            // If the column is not empty, check if the card can be placed in descending order with alternating colors
                            Card topColumnCard = column.peek();
                            if (topColumnCard.getRank().ordinal() == topCard.getRank().ordinal() + 1 && 
                                topColumnCard.getSuit().getColor() != topCard.getSuit().getColor()) {
                                column.push(talon.pop()); // Add the talon card to tableau
                                moved = true;
                                moves++; // Increment moves
                                // Log the move
                                CardMovementHandler.logCardMovement(topCard, "Column", tableau.indexOf(column) + 1);
                                break;
                            }
                        }
                    }
                }
            }

            // If no moves were made, the game is over
            if (!moved) {
                gameOver = true;
            }

            // Check if the game is won
            if (CardMovementHandler.validateGameWon(foundation)) {
                gameOver = true;
                System.out.println("Congratulations! You won!");
            }

            // Only update game state if a move was made
            if (moved) {
                updateGameState();
            }

            // Display MOVES, STOCK, and PASSTHURS only if a move was made
            if (moved) {
                printGameStatus();
            }

         // Redeal cards from talon if no valid moves and the talon is empty
            if (!moved && talon.isEmpty() && !waste.isEmpty() && passesThroughTalon < 2) {
                System.out.println("Redealing cards from waste to talon...");
                redealTalon();
            }

        }
    }

 // Redeals cards from waste to talon and resets the waste
    private void redealTalon() {
        talon.addAll(waste); // Add all cards from waste back to talon
        waste.clear(); // Clear the waste pile
        CardMovementHandler.shuffleTalon(talon); // Shuffle the talon
        passesThroughTalon++; // Increment passes through talon count
        printGameStatus(); // Display updated game status
    }


    // Add a card to the foundation pile
    public boolean addToFoundation(Card card) {
        if (CardMovementHandler.validateAddToFoundation(card, foundation)) {
            for (ArrayDeque<Card> pile : foundation) {
                if (pile.isEmpty() && card.getRank() == Rank.ACE) {
                    pile.push(card);
                    return true;
                } else if (!pile.isEmpty()) {
                    Card topCard = pile.peek();
                    int topRankValue = Card.getRankValue(topCard.getRank());
                    int cardRankValue = Card.getRankValue(card.getRank());

                    if (topCard.getSuit().equals(card.getSuit()) && cardRankValue == topRankValue + 1) {
                        pile.push(card);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Update the game state (e.g., for debugging or tracking state changes)
    private void updateGameState() {
        // Only render if a move has occurred
        CardRenderer.renderTableau(tableau);
        CardRenderer.renderFoundation(foundation);
        CardRenderer.renderTalon(talon, turnModeHandler); // Pass the TurnModeHandler instance to renderer
    }

    // Increment passes through talon (if talon is redealt)
    public void incrementPassesThroughTalon() {
        passesThroughTalon++;
    }

    // Print the current game status: MOVES, STOCK, and PASSTHURS
    private void printGameStatus() {
        System.out.println("MOVES: " + moves);
        System.out.println("STOCK: " + talon.size());
        System.out.println("PASSTHURS: " + passesThroughTalon);
    }
}
