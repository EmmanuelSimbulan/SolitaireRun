package com.svi.solitaire.utilities;

import com.svi.solitaire.vo.Card;
import com.svi.solitaire.logic.TurnModeHandler; // Import TurnModeHandler
import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;

public class CardRenderer {

    // Render the tableau
    public static void renderTableau(List<ArrayDeque<Card>> tableau) {
        System.out.println("\nTableau:");
        for (int i = 0; i < tableau.size(); i++) {
            System.out.print("Column " + (i + 1) + ": ");
            ArrayDeque<Card> column = tableau.get(i);
            if (column.isEmpty()) {
                System.out.print("[Empty]");
            } else {
                for (Card card : column) {
                    System.out.print(card.isFaceUp() ? card + " " : "[XX] ");
                }
            }
            System.out.println();
        }
    }

    // Render the foundation piles
    public static void renderFoundation(List<ArrayDeque<Card>> foundation) {
        System.out.println("\nFoundation Piles:");
        for (int i = 0; i < foundation.size(); i++) {
            System.out.print("Foundation " + (i + 1) + ": ");
            ArrayDeque<Card> pile = foundation.get(i);
            if (pile.isEmpty()) {
                System.out.print("[Empty]");
            } else {
                // Print all cards in the foundation pile
                for (Card card : pile) {
                    System.out.print(card + " ");
                }
            }
            System.out.println();
        }
    }

    // Render the talon pile based on the turn mode from TurnModeHandler
    public static void renderTalon(ArrayDeque<Card> talon, TurnModeHandler turnModeHandler) {
        System.out.print("\nTalon Pile: ");
        if (talon.isEmpty()) {
            System.out.print("[Empty]");
        } else {
            int turnMode = turnModeHandler.getTurnMode(); // Always get the current turn mode directly

            if (turnMode == 1) {  // Turn 1 mode
                Card talonCard = talon.peek();  // Get the top card from the talon
                System.out.print(talonCard + " ");
            } else if (turnMode == 3) {  // Turn 3 mode
                // First, print '[...]' to indicate there are more cards
                System.out.print("[...] ");

                // Render the first 3 cards in the talon
                int count = 0;
                ArrayDeque<Card> remainingCards = new ArrayDeque<>(talon);

                // Collect the last 3 cards in reverse order
                Stack<Card> lastThreeCards = new Stack<>();
                while (count < 3 && !remainingCards.isEmpty()) {
                    Card talonCard = remainingCards.poll();  // Get the next card
                    lastThreeCards.push(talonCard);  // Push it to a stack (reverse order)
                    count++;
                }

                // Now, print the last 3 cards in reverse order (since Stack pops in reverse order)
                while (!lastThreeCards.isEmpty()) {
                    System.out.print(lastThreeCards.pop() + " ");  // Print the cards in reverse order
                }
            }
        }
        System.out.println();
    }

    // Render the waste pile
    public static void renderWastePile(ArrayDeque<Card> wastePile) {
        System.out.println("\nWaste Pile:");
        if (wastePile.isEmpty()) {
            System.out.println("[Empty]");
        } else {
            for (Card card : wastePile) {
                System.out.print(card + " ");
            }
        }
        System.out.println();
    }
}
