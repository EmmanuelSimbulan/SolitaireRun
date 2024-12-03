package com.svi.solitaire.main;

import java.util.Scanner;

import com.svi.solitaire.logic.DeckInitializer;
import com.svi.solitaire.resources.GameInfo;
import com.svi.solitaire.utilities.GameMenu;

public class Main {

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        try {
            // Initialize the game components
            GameInfo about = new GameInfo(userInput);
            DeckInitializer deckInitializer = new DeckInitializer();
            GameMenu menu = new GameMenu(userInput, about, deckInitializer);

            // Start the main menu
            menu.start();

        } finally {
            userInput.close(); // Ensure scanner is closed
        }
    }
}
