import javax.swing.JFrame;

public class PongGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("B.Tech Final Project: Pong");
        GamePanel panel = new GamePanel();
        
        frame.add(panel);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // Centers window on screen
    }
}