class ActionCard extends Card {
    public ActionCard(String color, String actionType) {
        this.color = color;
        this.type = actionType;
        this.value = 20;
    }

    @Override
    public boolean matches(Card topCard) {
        return colorMatches(topCard) || 
               (topCard instanceof ActionCard && this.type.equals(((ActionCard) topCard).getType()));
    }

    @Override
    public int getPoints() {
        return this.value;
    }

    @Override
    public String toString() {
        String colorBg = getColorBackground(color);
        return colorBg + WHITE_FG + color + " " + type + RESET;
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