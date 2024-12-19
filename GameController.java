import java.util.ArrayList;
import java.util.List;

public class GameController {
    private List<Player> players;
    private Deck deck;
    private Card currentCard;
    private int currentPlayerIndex;

    public GameController() {
        players = new ArrayList<>();
        deck = new Deck();
        currentPlayerIndex = 0;

        // Add players
        players.add(new Player("Player 1")); // User
        players.add(new Player("Computer 1")); // Computer player
        players.add(new Player("Computer 2")); // Computer player
    }

    public void startGame() {
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(deck);
            }
        }
        currentCard = deck.draw(); // Draw initial card
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getNextPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }
}
