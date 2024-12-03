package com.svi.solitaire.logic;

import com.svi.solitaire.vo.Card;
import com.svi.solitaire.vo.Rank;
import com.svi.solitaire.vo.Suit;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardMovementHandler {

    // Ensure the shuffled deck is not null or empty
    public static void validateDeck(ArrayDeque<Card> shuffledDeck) {
        if (shuffledDeck == null || shuffledDeck.isEmpty()) {
            throw new IllegalArgumentException("Shuffled deck cannot be null or empty.");
        }
        if (shuffledDeck.size() < 52) {
            throw new IllegalStateException("Shuffled deck contains less than 52 cards.");
        }
    }

    // Validate the card distribution process
    public static void validateCardDistribution(Card card) {
        if (card == null) {
            throw new IllegalStateException("Unexpected null card encountered while distributing.");
        }
    }

    // Validate adding a card to the foundation
    public static boolean validateAddToFoundation(Card card, List<ArrayDeque<Card>> foundation) {
        for (ArrayDeque<Card> pile : foundation) {
            if (pile.isEmpty()) {
                if (card.getRank() == Rank.ACE) {
                    logCardMovement(card, "Foundation", foundation.indexOf(pile) + 1); // Log the move
                    return true;
                }
            } else {
                Card topCard = pile.peek();
                int topRankValue = Card.getRankValue(topCard.getRank());
                int cardRankValue = Card.getRankValue(card.getRank());

                if (topCard.getSuit().equals(card.getSuit()) &&
                    cardRankValue == topRankValue + 1) {
                    logCardMovement(card, "Foundation", foundation.indexOf(pile) + 1); // Log the move
                    return true;
                }
            }
        }
        return false; // Card cannot be added to any foundation pile
    }

    // Validate if the game is won (all foundation piles are complete)
    public static boolean validateGameWon(List<ArrayDeque<Card>> foundation) {
        for (ArrayDeque<Card> pile : foundation) {
            if (pile.size() != 13) {
                return false;
            }
        }
        return true;
    }

    // Validate if a tableau move can be made (card from tableau to foundation or tableau)
    public static boolean validateTableauMove(Card cardToMove, List<ArrayDeque<Card>> tableau) {
        for (ArrayDeque<Card> column : tableau) {
            // Check if the card can be placed in this column
            if (column.isEmpty()) {
                // Only Kings can be placed in an empty column
                if (cardToMove.getRank() == Rank.KING) {
                    logCardMovement(cardToMove, "Column", tableau.indexOf(column) + 1);
                    return true;
                }
            } else {
                // Get the top card of the column
                Card topColumnCard = column.peek();
                
                // Check if the card follows the Solitaire tableau placement rules:
                // 1. Descending rank (top card's rank is one higher)
                // 2. Alternating colors
                if (topColumnCard.getRank().ordinal() == cardToMove.getRank().ordinal() + 1 && 
                    topColumnCard.getSuit().getColor() != cardToMove.getSuit().getColor()) {
                    logCardMovement(cardToMove, "Column", tableau.indexOf(column) + 1);
                    return true;
                }
            }
        }
        return false;
    }

    // Validate if a talon card can be placed in the tableau or foundation
    public static boolean validateTalonMove(Card topCard, List<ArrayDeque<Card>> tableau, List<ArrayDeque<Card>> foundation) {
        // If the talon card can be added to the foundation, return true
        if (validateAddToFoundation(topCard, foundation)) {
            return true;
        }

        // Otherwise, check if the talon card can be placed in the tableau
        return validateTableauMove(topCard, tableau);
    }

    // Check if two cards have alternating colors (red/black)
    private static boolean isAlternatingColor(Card card1, Card card2) {
        boolean card1Red = card1.getSuit() == Suit.HEARTS || card1.getSuit() == Suit.DIAMONDS;
        boolean card2Red = card2.getSuit() == Suit.HEARTS || card2.getSuit() == Suit.DIAMONDS;

        return card1Red != card2Red; // One should be red, the other black
    }

    // Validate movement of a card to an empty tableau column (only a King can go in an empty column)
    public static boolean validateEmptyTableauColumnMove(Card card) {
        return card.getRank() == Rank.KING; // Only Kings can be moved to empty tableau columns
    }

    // Validate that only the top card of the tableau column can be moved
    public static boolean validateTopCardMove(ArrayDeque<Card> column) {
        return column.size() > 0 && column.peek().isFaceUp(); // The top card must be face-up to move
    }

    // Validate if a move from tableau to tableau is valid based on the descending order and alternating colors
    public static boolean validateTableauToTableauMove(Card cardToMove, ArrayDeque<Card> destinationColumn) {
        // If destination column is empty, only a King can be placed
        if (destinationColumn.isEmpty()) {
            return cardToMove.getRank() == Rank.KING;
        }
        
        // Validate the top card in the destination tableau column
        Card topCardInDestination = destinationColumn.peek();

        // Check if the move is in descending order and alternating colors
        return (topCardInDestination.getRank().ordinal() == cardToMove.getRank().ordinal() + 1) && 
               isAlternatingColor(topCardInDestination, cardToMove);
    }

    public static boolean canMoveToTableau(Card talonCard, ArrayDeque<Card> tableauColumn) {
        if (tableauColumn.isEmpty()) {
            // Only a King can be placed on an empty tableau column
            return talonCard.getRank() == Rank.KING;
        }
        // Get the top card of the tableau column
        Card topCard = tableauColumn.peek();
        // Check descending order and alternating color
        return talonCard.getRank().getValue() == topCard.getRank().getValue() - 1 &&
               talonCard.getSuit().getColor() != topCard.getSuit().getColor();
    }

    
    // Method to log card movements
    public static void logCardMovement(Card card, String destinationType, int destinationIndex) {
        System.out.println("Card (" + card.getRank() + card.getSuit().getSymbol() + 
                           ") is moved to " + destinationType + " " + destinationIndex);
    }

    // New method to reshuffle the waste pile back into the talon
    public static void shuffleTalon(ArrayDeque<Card> talon) {
        List<Card> tempList = new ArrayList<>(talon); // Convert to list for shuffling
        Collections.shuffle(tempList); // Shuffle the list
        talon.clear(); // Clear the talon
        talon.addAll(tempList); // Add shuffled cards back to the talon
    }

}
