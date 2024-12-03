package com.svi.solitaire.logic;

import java.util.ArrayList;
import com.svi.solitaire.vo.Card;
import com.svi.solitaire.vo.Rank;
import com.svi.solitaire.vo.Suit;

public class DeckInitializer {
    private ArrayList<Card> deck;

    public DeckInitializer() {
        // Initialize and populate the deck with cards
        deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {  // Use Suit enum from Suit.java
            for (Rank rank : Rank.values()) {  // Use Rank enum from Rank.java
                deck.add(new Card(rank, suit)); // Create a new card with rank and suit
            }
        }
    }

    // Get the initialized deck (used in other classes like GameState)
    public ArrayList<Card> getDeck() {
        return deck;
    }
}
