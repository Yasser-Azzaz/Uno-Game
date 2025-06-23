class NumberCard extends Card {
    public NumberCard(String color, int value) {
        this.color = color;
        this.value = value;
        this.type = String.valueOf(value);
    }

    @Override
    public boolean matches(Card topCard) {
        return colorMatches(topCard) || 
               (topCard instanceof NumberCard && this.value == ((NumberCard) topCard).getValue());
    }

    @Override
    public int getPoints() {
        return this.value;
    }

    @Override
    public String toString() {
        String colorBg = getColorBackground(color);
        return colorBg + WHITE_FG + color + " " + value + RESET;
    }

    private String getColorBackground(String color) {
        return switch (color) {
            case "Red" -> RED_BG;
            case "Green" -> GREEN_BG;
            case "Blue" -> BLUE_BG;
            case "Yellow" -> YELLOW_BG;
            default -> RESET;
        };
    }
}