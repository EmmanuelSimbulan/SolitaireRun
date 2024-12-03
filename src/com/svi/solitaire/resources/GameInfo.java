package com.svi.solitaire.resources;

//Displays information about Klondike Solitaire

import java.util.Scanner;

public class GameInfo {
    private Scanner userInput;

    public GameInfo(Scanner userInput) {
    	this.userInput = userInput;
    }

    public void displayAboutMenu() {
    	String prompt = "\nABOUT KLONDIKE SOLITAIRE:\n"
    					+ "Klondike Solitaire is a classic card game that involves strategy, skill, and a bit of luck. It is played using a standard deck of 52 cards.\n"
    					+ "\nOBJECTIVE:\n"
    					+ "The goal of Klondike Solitaire is to move all cards to the foundation piles, sorted by suit in ascending order (from Ace to King).\n"
    					+ "\nGAMEPLAY RULES:\n"
    					+ "1. The game starts with 28 cards distributed into seven tableau columns. The first column has one card, the second has two, and so on.\n"
    					+ "2. The remaining cards form the stock pile, which can be drawn upon.\n"
    					+ "3. Only Kings or sequences starting with Kings can be moved to empty tableau columns.\n"
    					+ "4. Cards on the tableau must alternate in color (red and black) and descend in rank (e.g., a black 7 can be placed on a red 8).\n"
    					+ "5. The foundation piles must be built up by suit starting from Ace (e.g., Ace of Hearts, then 2 of Hearts, and so on).\n"
    					+ "\nTURN 1 vs. TURN 3:\n"
    					+ "1. **Klondike Turn 1:**\n"
    					+ "   - In this mode, you draw one card at a time from the stock pile.\n"
    					+ "   - This makes the game easier, as you have more control over which cards are available.\n"
    					+ "2. **Klondike Turn 3:**\n"
    					+ "   - In this mode, you draw three cards at a time from the stock pile.\n"
    					+ "   - This adds a layer of complexity and increases the challenge, as only the top card of the three drawn is accessible.\n"
    					+ "\nSTRATEGY TIPS:\n"
    					+ "1. Always prioritize uncovering hidden cards in the tableau.\n"
    					+ "2. Try to avoid filling empty tableau spaces unless you have a King ready to place.\n"
    					+ "3. Plan moves carefully to avoid getting stuck!\n"
    					+ "\nEnjoy playing Klondike Solitaire and sharpening your problem-solving skills!\n";
    	
    	System.out.println(prompt);

        // Option to return to the Main Menu
        System.out.println("\nEnter any key to return to the Main Menu...");
        
        if (userInput.hasNextLine()) {
        	userInput.nextLine();
        }
        
        userInput.nextLine(); // Waits for the user to press Enter
        
    }
}
