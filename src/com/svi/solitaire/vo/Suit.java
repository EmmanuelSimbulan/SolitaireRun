package com.svi.solitaire.vo;

public enum Suit {
    HEARTS("♥", "RED"), DIAMONDS("♦", "RED"),
    CLUBS("♣", "BLACK"), SPADES("♠", "BLACK");

    private final String symbol;
    private final String color; // Added color attribute

    Suit(String symbol, String color) {
        this.symbol = symbol;
        this.color = color; // Initialize color
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color; // Getter for color
    }
}
