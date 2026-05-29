import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    // Screen Constants
    static final int WIDTH = 800;
    static final int HEIGHT = 500;
    
    // Game Objects
    int ballX = WIDTH / 2, ballY = HEIGHT / 2;
    int ballXDir = -5, ballYDir = 5;
    int p1Y = 200, p2Y = 200;
    int p1Score = 0, p2Score = 0;

    // States: 0=Menu, 1=VS AI, 2=2 Player, 3=Game Over
    int gameState = 0;
    boolean up, down, w, s;

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(15, 15, 20)); // Deep dark theme
        this.setFocusable(true);

        // Key Listener for Controls
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (gameState == 0) {
                    if (key == KeyEvent.VK_1) gameState = 1;
                    if (key == KeyEvent.VK_2) gameState = 2;
                }
                if (gameState == 3 && key == KeyEvent.VK_SPACE) {
                    resetGame();
                }
                if (key == KeyEvent.VK_W) w = true;
                if (key == KeyEvent.VK_S) s = true;
                if (key == KeyEvent.VK_UP) up = true;
                if (key == KeyEvent.VK_DOWN) down = true;
            }
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) w = false;
                if (key == KeyEvent.VK_S) s = false;
                if (key == KeyEvent.VK_UP) up = false;
                if (key == KeyEvent.VK_DOWN) down = false;
            }
        });
        new Thread(this).start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gameState == 0) drawMenu(g2);
        else if (gameState == 3) drawGameOver(g2);
        else drawGame(g2);
    }

    void drawMenu(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Consolas", Font.BOLD, 50));
        g2.drawString("JAVA PONG", 260, 150);
        g2.setFont(new Font("Consolas", Font.PLAIN, 20));
        g2.drawString("Press [1] for vs COMPUTER", 260, 250);
        g2.drawString("Press [2] for 2 PLAYERS", 260, 300);
    }

    void drawGame(Graphics2D g2) {
        // Center Line
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

        // Scores
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Consolas", Font.BOLD, 60));
        g2.drawString(String.valueOf(p1Score), WIDTH / 2 - 100, 70);
        g2.drawString(String.valueOf(p2Score), WIDTH / 2 + 50, 70);

        // Paddles & Ball
        g2.setColor(Color.CYAN);
        g2.fillOval(ballX, ballY, 20, 20); // Ball
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(20, p1Y, 15, 100, 10, 10); // Player 1
        g2.fillRoundRect(WIDTH - 35, p2Y, 15, 100, 10, 10); // Player 2/AI
    }

    void drawGameOver(Graphics2D g2) {
        g2.setColor(Color.ORANGE);
        g2.setFont(new Font("Consolas", Font.BOLD, 50));
        String winner = (p1Score >= 5) ? "PLAYER 1 WINS!" : "PLAYER 2 WINS!";
        g2.drawString(winner, 200, 200);
        g2.setFont(new Font("Consolas", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("Press [SPACE] to Play Again", 250, 300);
    }

    public void move() {
        if (gameState == 0 || gameState == 3) return;

        // Player 1 Movement
        if (w && p1Y > 0) p1Y -= 7;
        if (s && p1Y < HEIGHT - 100) p1Y += 7;

        // Player 2 or AI Movement
        if (gameState == 1) { // AI logic
            if (p2Y + 50 < ballY) p2Y += 5;
            if (p2Y + 50 > ballY) p2Y -= 5;
        } else { // Human Player 2
            if (up && p2Y > 0) p2Y -= 7;
            if (down && p2Y < HEIGHT - 100) p2Y += 7;
        }

        // Ball Physics
        ballX += ballXDir;
        ballY += ballYDir;

        if (ballY <= 0 || ballY >= HEIGHT - 20) ballYDir *= -1;

        // Collision Logic
        Rectangle ballRect = new Rectangle(ballX, ballY, 20, 20);
        Rectangle p1Rect = new Rectangle(20, p1Y, 15, 100);
        Rectangle p2Rect = new Rectangle(WIDTH - 35, p2Y, 15, 100);

        if (ballRect.intersects(p1Rect) || ballRect.intersects(p2Rect)) {
            ballXDir *= -1;
            // Slightly increase speed on each hit for difficulty
            ballXDir += (ballXDir > 0) ? 1 : -1;
        }

        // Scoring & Win Condition
        if (ballX < 0) {
            p2Score++;
            if (p2Score >= 5) gameState = 3;
            else resetBall();
        }
        if (ballX > WIDTH) {
            p1Score++;
            if (p1Score >= 5) gameState = 3;
            else resetBall();
        }
    }

    void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        ballXDir = (ballXDir > 0) ? -5 : 5;
    }

    void resetGame() {
        p1Score = 0; p2Score = 0;
        gameState = 0;
        resetBall();
    }

    public void run() {
        while (true) {
            move();
            repaint();
            try { Thread.sleep(10); } catch (Exception e) {}
        }
    }
}