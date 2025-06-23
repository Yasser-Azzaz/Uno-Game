import java.util.*;

class Player {
    private final String name;
    private final List<Card> hand;
    private int score;
    private int roundsWon;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
        this.roundsWon = 0;
    }

    public String getName() { return name; }
    public List<Card> getHand() { return hand; }
    public int getScore() { return score; }
    public int getRoundsWon() { return roundsWon; }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public void incrementRoundsWon() {
        roundsWon++;
    }

    public boolean hasValidMove(Card topCard) {
        return !getValidCards(topCard).isEmpty();
    }

    public List<Card> getValidCards(Card topCard) {
        List<Card> validCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.matches(topCard)) {
                validCards.add(card);
            }
        }
        return validCards;
    }

    public void incrementScore(int points) {
        score += points;
    }

    public int calculateHandPenalty() {
        return hand.stream()
            .mapToInt(Card::getPoints)
            .sum();
    }

    public void clearHand() {
        hand.clear();
    }
}