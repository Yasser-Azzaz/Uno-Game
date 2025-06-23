import java.util.*;
import java.util.concurrent.TimeUnit;

public class UNOGame {
    private static final int WINNING_SCORE = 500;
    private static final int INITIAL_HAND_SIZE = 7;

    private List<Player> players;
    private Deck deck;
    private Card topCard;
    private List<Card> discardPile;
    private int currentPlayerIndex;
    private int direction;
    private Scanner scanner;
    private Player overallGameWinner;

    public void start() {
        scanner = new Scanner(System.in);
        displayUnoLogo();
        players = createPlayers();
        playMultipleRounds();
        scanner.close();
    }

    private void displayUnoLogo() {
        String[] unoBanner = {
            "\n" + Card.RED_BG + "                                                " + Card.RESET,
            "\n" + Card.GREEN_BG + "                 UNO GAME                      " + Card.RESET,
            "\n" + Card.BLUE_BG + "                                                " + Card.RESET
        };

        for (String line : unoBanner) {
            System.out.println(line);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("\n🃏 WELCOME TO UNO! 🃏");
    }

    private List<Player> createPlayers() {
        List<Player> gamePlayers = new ArrayList<>();
        int numPlayers;
        
        while (true) {
            try {
                System.out.println("\n🎮 Enter number of players (2-4):");
                numPlayers = Integer.parseInt(scanner.nextLine());
                if (numPlayers >= 2 && numPlayers <= 4) break;
                System.out.println("❌ Invalid number. Please enter 2-4 players.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }

        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("👤 Enter name for Player " + i + ":");
            String name = scanner.nextLine();
            gamePlayers.add(new Player(name));
        }
        return gamePlayers;
    }

    private void playMultipleRounds() {
        while (overallGameWinner == null) {
            playRound();
            
            for (Player player : players) {
                if (player.getScore() >= WINNING_SCORE) {
                    overallGameWinner = player;
                    break;
                }
            }
        }

        displayGameWinner();
    }

    private void playRound() {
        initializeRound();
        
        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            displayGameState(currentPlayer);
    
            handlePlayerTurn(currentPlayer);
    
            if (currentPlayer.getHand().isEmpty()) {
                handleRoundEnd(currentPlayer);
                break;
            }
    
            advanceToNextPlayer();
        }
    }

    private void initializeRound() {
        deck = new Deck();
        discardPile = new ArrayList<>();
        currentPlayerIndex = 0;
        direction = 1;

        for (Player player : players) {
            player.clearHand();
            for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
                player.addCard(deck.draw());
            }
        }

        do {
            topCard = deck.draw();
        } while (topCard instanceof WildCard);
        discardPile.add(topCard);
    }

    private void handlePlayerTurn(Player currentPlayer) {
        boolean turnCompleted = false;
        while (!turnCompleted) {
            if (currentPlayer.hasValidMove(topCard)) {
                playTurn(currentPlayer);
                turnCompleted = true;
            } else {
                Card drawnCard = drawCard(currentPlayer);
                if (drawnCard != null && drawnCard.matches(topCard)) {
                    System.out.println("✅ Drawn card can be played!");
                    topCard = drawnCard;
                    currentPlayer.removeCard(drawnCard);
                    discardPile.add(drawnCard);
                    turnCompleted = true;
                } else {
                    turnCompleted = true;
                }
            }
        }
    }

    private void playTurn(Player currentPlayer) {
        if (topCard instanceof WildCard && ((WildCard) topCard).getChosenColor() == null) {
            handleWildCard(currentPlayer);
        }
        
        Card playedCard = selectAndPlayCard(currentPlayer);
        if (playedCard != null) {
            topCard = playedCard;
            currentPlayer.removeCard(playedCard);
            discardPile.add(playedCard);
            
            if (playedCard instanceof ActionCard) {
                handleActionCard((ActionCard) playedCard);
            }
        }
    }

    private Card selectAndPlayCard(Player currentPlayer) {
        List<Card> validCards = currentPlayer.getValidCards(topCard);
        if (validCards.isEmpty()) return null;

        System.out.println("\nSelect a card to play:");
        for (int i = 0; i < validCards.size(); i++) {
            System.out.printf("[%d] %s\n", i, validCards.get(i));
        }

        while (true) {
            try {
                System.out.print("Enter card index: ");
                int index = Integer.parseInt(scanner.nextLine());
                
                if (index >= 0 && index < validCards.size()) {
                    return validCards.get(index);
                }
                System.out.println("❌ Invalid index.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }

    private void handleActionCard(ActionCard actionCard) {
        int nextPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        Player nextPlayer = players.get(nextPlayerIndex);

        switch (actionCard.getType()) {
            case "Skip":
                System.out.println("⏸️ " + nextPlayer.getName() + " is skipped!");
                currentPlayerIndex = nextPlayerIndex;
                break;
            case "Reverse":
                direction *= -1;
                System.out.println("🔄 Play direction reversed!");
                break;
            case "Draw Two":
                System.out.println("🎴 " + nextPlayer.getName() + " must draw two cards!");
                drawCard(nextPlayer);
                drawCard(nextPlayer);
                currentPlayerIndex = nextPlayerIndex;
                break;
        }
    }

    private void handleWildCard(Player currentPlayer) {
        System.out.println("🌈 " + currentPlayer.getName() + " played a Wildcard!");
        System.out.println("🎨 Choose a color: [Red, Green, Blue, Yellow]");
        
        String[] validColors = {"Red", "Green", "Blue", "Yellow"};
        while (true) {
            String chosenColor = scanner.nextLine().trim();
            if (Arrays.asList(validColors).contains(chosenColor)) {
                ((WildCard) topCard).setChosenColor(chosenColor);
                System.out.println("✅ Color set to " + chosenColor);
                break;
            }
            System.out.println("❌ Invalid color. Try again.");
        }
    }

    private Card drawCard(Player player) {
        if (deck.isEmpty()) {
            deck.resetDeck(discardPile);
        }
        
        Card drawnCard = deck.draw();
        if (drawnCard == null) {
            System.out.println("❌ No cards left in the deck.");
            return null;
        }
        player.addCard(drawnCard);
        System.out.println("🎴 " + player.getName() + " drew a card: " + drawnCard);
        return drawnCard;
    }

    private void handleRoundEnd(Player currentPlayer) {
        System.out.println("\n🎉 " + currentPlayer.getName() + " has won the round!");
        currentPlayer.incrementRoundsWon();
        
        for (Player player : players) {
            if (player != currentPlayer) {
                int penaltyPoints = player.calculateHandPenalty();
                currentPlayer.incrementScore(penaltyPoints);
            }
        }
        
        System.out.println("\nRound Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore() + " points");
        }
    }

    private void displayGameState(Player currentPlayer) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🃏 GAME STATE 🃏");
        System.out.println("=".repeat(40));
        
        System.out.println("Top Card: " + topCard);
        System.out.println("-".repeat(40));
        System.out.println("Current Player: " + currentPlayer.getName());
        
        System.out.println("\nYour Hand: ");
        List<Card> hand = currentPlayer.getHand();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            System.out.printf("[%d] %s\n", i, card);
        }
    }

    private void advanceToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }

    private void displayGameWinner() {
        System.out.println("\n🏆 GAME OVER 🏆");
        System.out.println(overallGameWinner.getName() + " wins the entire game!");
        System.out.println("\nFinal Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore() + " points");
        }
    }

    public static void main(String[] args) {
        new UNOGame().start();
    }
}