public class Card {
    private String color;
    private String value;

    public Card(String color, String value) {
        this.color = color;
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    public boolean canBePlayedOn(Card topCard) {
        return color.equals(topCard.getColor()) || value.equals(topCard.getValue()) || isWildCard() || isDrawFour();
    }

    public boolean isWildCard() {
        return value.equals("wild");
    }

    public boolean isDrawFour() {
        return value.equals("draw four");
    }

    public void setColor(String color) {
        this.color = color;
    }
}
