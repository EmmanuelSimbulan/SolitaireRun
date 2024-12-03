package com.svi.solitaire.vo;

public class Card {
    private final Rank rank; // Use Rank enum
    private final Suit suit; // Use Suit enum
    private boolean isFaceUp;

    // Constructor
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.isFaceUp = false;  // Default to face-down
    }

    // Getters
    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    // Set face up or face down
    public void setFaceUp(boolean isFaceUp) {
        this.isFaceUp = isFaceUp;
    }

    // String representation of the card
    @Override
    public String toString() {
        return rank.getSymbol() + suit.getSymbol();
    }

    // Static method to get numerical rank values
    public static int getRankValue(Rank rank) {
        return rank.getValue();
    }

    // Method to determine if the card is red (hearts or diamonds)
    public boolean isRed() {
        return suit == Suit.HEARTS || suit == Suit.DIAMONDS;
    }

    // Method to determine if the card is black (clubs or spades)
    public boolean isBlack() {
        return suit == Suit.CLUBS || suit == Suit.SPADES;
    }

    // Method to check if the card can be placed on top of another card in tableau
    public boolean canBePlacedOn(Card card) {
        // Cards can only be placed on top if they are one rank lower and of opposite color
        return this.getRank().ordinal() == card.getRank().ordinal() - 1 &&
               this.getSuit().getColor() != card.getSuit().getColor();
    }
}
