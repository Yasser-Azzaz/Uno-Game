import java.util.*;

class Deck {
    private List<Card> cards;
    private Random random = new Random();

    public Deck() {
        this.cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        String[] colors = {"Red", "Green", "Blue", "Yellow"};
        
        for (String color : colors) {
            cards.add(new NumberCard(color, 0));
            for (int i = 1; i <= 9; i++) {
                cards.add(new NumberCard(color, i));
                cards.add(new NumberCard(color, i));
            }
            
            String[] actions = {"Skip", "Reverse", "Draw Two"};
            for (String action : actions) {
                cards.add(new ActionCard(color, action));
                cards.add(new ActionCard(color, action));
            }
        }
        
        for (int i = 0; i < 4; i++) {
            cards.add(new WildCard());
        }
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card draw() {
        return cards.isEmpty() ? null : cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void resetDeck(List<Card> discardPile) {
        this.cards.clear();
        Card topCard = discardPile.get(discardPile.size() - 1);
        discardPile.remove(discardPile.size() - 1);
        
        this.cards.addAll(discardPile);
        shuffle();
        
        discardPile.add(topCard);
    }

    public int size() {
        return cards.size();
    }
}