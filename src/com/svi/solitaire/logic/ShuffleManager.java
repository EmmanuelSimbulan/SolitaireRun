package com.svi.solitaire.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.svi.solitaire.utilities.CardRenderer;
import com.svi.solitaire.vo.Card;
import com.svi.solitaire.vo.GameState;

public class ShuffleManager {

    // Easy Shuffle: Ensures the deck is winnable by arranging cards strategically.
    public static ArrayDeque<Card> easyShuffle(ArrayDeque<Card> deck) {
        ArrayList<Card> deckList = new ArrayList<>(deck);

        // Basic algorithm to create a "winnable" setup:
        Collections.sort(deckList, (card1, card2) -> {
            if (card1.getSuit() == card2.getSuit()) {
                return card1.getRank().ordinal() - card2.getRank().ordinal();
            }
            return card1.getSuit().compareTo(card2.getSuit());
        });

        // Shuffle the second half to introduce some randomness.
        List<Card> randomHalf = deckList.subList(deckList.size() / 2, deckList.size());
        Collections.shuffle(randomHalf);

        return new ArrayDeque<>(deckList);
    }

    // Medium Shuffle: Implements a Faro shuffle.
    public static ArrayDeque<Card> mediumShuffle(ArrayDeque<Card> deck) {
        ArrayList<Card> deckList = new ArrayList<>(deck);
        ArrayList<Card> shuffledDeck = new ArrayList<>();

        int mid = deckList.size() / 2;
        List<Card> firstHalf = deckList.subList(0, mid);
        List<Card> secondHalf = deckList.subList(mid, deckList.size());

        for (int i = 0; i < firstHalf.size(); i++) {
            shuffledDeck.add(firstHalf.get(i));
            if (i < secondHalf.size()) {
                shuffledDeck.add(secondHalf.get(i));
            }
        }

        return new ArrayDeque<>(shuffledDeck);
    }

    // Hard Shuffle: Completely random shuffle for maximum difficulty.
    public static ArrayDeque<Card> hardShuffle(ArrayDeque<Card> deck) {
        ArrayList<Card> deckList = new ArrayList<>(deck);
        Collections.shuffle(deckList); 
        return new ArrayDeque<>(deckList);
    }

    // Handle shuffled deck display and initialization
    public static void handleShuffledDeck(ArrayDeque<Card> shuffledDeck, GameState gameState) {
        // Only shuffle and distribute, no rendering
        gameState.distributeCards(shuffledDeck);
    }
}


