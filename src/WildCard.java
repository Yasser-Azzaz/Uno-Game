class WildCard extends Card {
    private String chosenColor;

    public WildCard() {
        this.type = "Wild";
        this.value = 50;
        this.chosenColor = null;
    }

    @Override
    public int getPoints() {
        return this.value;
    }

    public String getChosenColor() {
        return chosenColor;
    }

    @Override
    public boolean matches(Card topCard) {
        return true;
    }

    @Override
    public String getColor() {
        return chosenColor != null ? chosenColor : "Wild";
    }

    public void setChosenColor(String color) {
        this.chosenColor = color;
    }

    @Override
    public String toString() {
        if (chosenColor != null) {
            String colorBg = getColorBackground(chosenColor);
            return colorBg + WHITE_FG + "Wild (" + chosenColor + ")" + RESET;
        }
        return "Wild Card";
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