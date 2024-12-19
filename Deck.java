import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] colors = {"red", "yellow", "green", "blue"};

        for (String color : colors) {
            for (int i = 0; i <= 9; i++) {
                cards.add(new Card(color, String.valueOf(i))); // 0-9 cards
            }
            cards.add(new Card(color, "skip")); // Skip card
            cards.add(new Card(color, "reverse")); // Reverse card
            cards.add(new Card(color, "draw two")); // Draw Two card
        }

        for (int i = 0; i < 4; i++) {
            cards.add(new Card("wild", "wild")); // Wild card
            cards.add(new Card("wild", "draw four")); // Draw Four card
        }

        Collections.shuffle(cards);
    }

    public Card draw() {
        return cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
