import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void drawCard(Deck deck) {
        if (!deck.isEmpty()) {
            hand.add(deck.draw());
        }
    }

    public void playCard(Card card) {
        hand.remove(card);
    }

    public boolean hasPlayableCard(Card topCard) {
        for (Card card : hand) {
            if (card.canBePlayedOn(topCard)) {
                return true;
            }
        }
        return false;
    }
}
