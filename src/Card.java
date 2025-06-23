abstract class Card {
    protected String color;
    protected String type;
    protected int value;

    public abstract boolean matches(Card topCard);
    public abstract int getPoints();

    public boolean colorMatches(Card topCard) {
        if (this instanceof WildCard) return true;
        return this.color.equals(topCard.getColor());
    }

    public String getColor() { return color; }
    public String getType() { return type; }
    public int getValue() { return value; }

    public static final String RESET = "\u001B[0m";
    public static final String RED_BG = "\u001B[41m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String WHITE_FG = "\u001B[37m";
}