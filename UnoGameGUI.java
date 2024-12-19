import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Collections;

public class UnoGameGUI extends JFrame {
    private final GameController gameController;
    private final JLabel topCardLabel;
    private final JPanel playerHandPanel;
    private final JButton drawButton;
    private final JTextArea gameLog;
    private final JLabel currentPlayerLabel;
    private String playerName;

    public UnoGameGUI() {
        gameController = new GameController();
        setTitle("UNO Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set a colorful themed background
        setContentPane(new JPanel() {
            {
                setBackground(new Color(135, 206, 250)); // Light blue background
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
            }
        });

        // Welcome message
        JOptionPane.showMessageDialog(this, "Welcome to UNO!", "Welcome", JOptionPane.INFORMATION_MESSAGE);

        // Prompt for player name
        playerName = JOptionPane.showInputDialog(this, "Enter your name:", "Player 1");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player 1";
        }

        // Top card display
        topCardLabel = new JLabel();
        topCardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topCardLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        topCardLabel.setForeground(Color.WHITE);
        add(topCardLabel, BorderLayout.NORTH);

        // Player hand display
        playerHandPanel = new JPanel();
        playerHandPanel.setLayout(new FlowLayout());
        add(playerHandPanel, BorderLayout.CENTER);

        // Draw button with enhanced style
        drawButton = new JButton("Draw Card");
        drawButton.setBackground(new Color(255, 215, 0));
        drawButton.setForeground(Color.BLACK);
        drawButton.setFont(new Font("Poppins", Font.BOLD, 16));
        drawButton.setBorder(BorderFactory.createRaisedBevelBorder());
        drawButton.setFocusPainted(false);
        drawButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                drawButton.setBackground(new Color(255, 200, 0)); // Darker on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                drawButton.setBackground(new Color(255, 215, 0)); // Original color
            }
        });
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player currentPlayer = gameController.getCurrentPlayer();
                currentPlayer.drawCard(gameController.getDeck());
                gameLog.append(playerName + " drew a card.\n");
                updateDisplay();
                gameController.nextTurn();
                updateDisplay();
            }
        });

        // Add draw button to a panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(drawButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Game log
        gameLog = new JTextArea();
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Arial", Font.PLAIN, 14));
        gameLog.setBackground(new Color(240, 248, 255));
        add(new JScrollPane(gameLog), BorderLayout.EAST);

        // Current player display
        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(currentPlayerLabel, BorderLayout.NORTH);

        startGame();
        setVisible(true);
    }

    private void startGame() {
        gameController.startGame();
        Card initialCard = gameController.getCurrentCard();
        JOptionPane.showMessageDialog(this, "The initial card is: " + initialCard.getColor() + " " + initialCard.getValue(), "Initial Card", JOptionPane.INFORMATION_MESSAGE);
        updateDisplay();
    }

    private void updateDisplay() {
        // Update top card display
        Card topCard = gameController.getCurrentCard();
        topCardLabel.setText("Top Card: " + topCard.getColor() + " " + topCard.getValue());

        // Update player hand display
        playerHandPanel.removeAll();
        List<Card> playerHand = gameController.getCurrentPlayer().getHand();
        boolean hasPlayableCard = false;
        for (Card card : playerHand) {
            // Load card image
            String cardImagePath = "C:/Users/Rohan/OneDrive/Desktop/app - copy/game/";

            // Special handling for power cards based on your naming convention
            if (card.isWildCard()) {
                cardImagePath += "wild.png"; // Wild card image
            } else if (card.isDrawFour()) {
                cardImagePath += "pick4.png"; // Draw Four image
            } else if (card.getValue().equals("skip")) {
                cardImagePath += "yellowskip.png"; // Skip image
            } else if (card.getValue().equals("reverse")) {
                cardImagePath += "yellowrev.png"; // Reverse image
            } else if (card.getValue().equals("draw two")) {
                cardImagePath += "yellowpick2.png"; // Draw Two image
            } else {
                // Regular card
                cardImagePath += card.getColor() + card.getValue() + ".png"; // Regular card image path
            }

            ImageIcon cardImage = new ImageIcon(cardImagePath);

            // Resize the image to fit within the button
            Image scaledImage = cardImage.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            ImageIcon scaledCardImage = new ImageIcon(scaledImage);

            JButton cardButton = new JButton(scaledCardImage);
            cardButton.setPreferredSize(new Dimension(100, 150)); // Set size for buttons
            cardButton.setFocusPainted(false);
            cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border
            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleCardPlay(card);
                }
            });
            playerHandPanel.add(cardButton);

            if (card.canBePlayedOn(gameController.getCurrentCard())) {
                hasPlayableCard = true;
            }
        }

        // Enable or disable the draw button based on whether the player has a playable card
        drawButton.setEnabled(!hasPlayableCard);

        // Refresh the panel
        playerHandPanel.revalidate();
        playerHandPanel.repaint();

        // Update current player display
        String currentPlayerName = gameController.getCurrentPlayerIndex() == 0 ? playerName : "Computer";
        currentPlayerLabel.setText("Current Player: " + currentPlayerName);

        // Update game log
        gameLog.append(currentPlayerName + "'s turn.\n");

        // Handle computer players' turns
        while (gameController.getCurrentPlayerIndex() != 0) {
            Player currentPlayer = gameController.getCurrentPlayer();
            Card cardToPlay = null;
            for (Card card : currentPlayer.getHand()) {
                if (card.canBePlayedOn(gameController.getCurrentCard())) {
                    cardToPlay = card;
                    break;
                }
            }
            if (cardToPlay != null) {
                currentPlayer.playCard(cardToPlay);
                gameController.setCurrentCard(cardToPlay);
                String computerName = gameController.getCurrentPlayerIndex() == 1 ? "Pushkal" : "Akshat";
                gameLog.append(computerName + " played " + cardToPlay.getColor() + " " + cardToPlay.getValue() + ".\n");
            } else {
                currentPlayer.drawCard(gameController.getDeck());
                String computerName = gameController.getCurrentPlayerIndex() == 1 ? "Pushkal" : "Akshat";
                gameLog.append(computerName + " drew a card.\n");
            }
            gameController.nextTurn();
            updateDisplay();
        }
    }

    private void handleCardPlay(Card card) {
        if (card.canBePlayedOn(gameController.getCurrentCard())) {
            // Play the card and update game state
            gameController.getCurrentPlayer().playCard(card);
            gameController.setCurrentCard(card);
            gameLog.append(playerName + " played " + card.getColor() + " " + card.getValue() + ".\n");

            // Handle special card effects
            switch (card.getValue()) {
                case "skip":
                    gameLog.append("Next player's turn is skipped!\n");
                    gameController.nextTurn();
                    break;
                case "reverse":
                    gameLog.append("Turn order reversed!\n");
                    Collections.reverse(gameController.getPlayers());
                    break;
                case "draw two":
                    Player nextPlayer = gameController.getNextPlayer();
                    nextPlayer.drawCard(gameController.getDeck());
                    nextPlayer.drawCard(gameController.getDeck());
                    gameLog.append(nextPlayer.getName() + " drew two cards!\n");
                    break;
                case "wild":
                case "draw four":
                    String newColor = JOptionPane.showInputDialog(this, "Choose a color (red, yellow, green, blue):");
                    if (newColor != null && (newColor.equals("red") || newColor.equals("yellow") || newColor.equals("green") || newColor.equals("blue"))) {
                        card.setColor(newColor);
                        gameLog.append(playerName + " changed the color to " + newColor + ".\n");
                    } else {
                        gameLog.append(playerName + " did not choose a valid color. The color remains " + card.getColor() + ".\n");
                    }
                    break;
            }
            gameController.nextTurn();
            updateDisplay();
        } else {
            gameLog.append("Cannot play that card!\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UnoGameGUI::new);
    }
}
