import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameTester {
    public static void main(String[] args) {
        JFrame gameFrame = new JFrame("Panel");
        gameFrame.setDefaultCloseOperation(3);
        gameFrame.setVisible(true);
        gameFrame.setResizable(false);
        gameFrame.setSize(800, 800);
        ChessBoard mainChessboard = new ChessBoard();
        System.out.println(mainChessboard.toString());
        JPanel gamePanel = new ChessPanel().boardPanel;
        gameFrame.add(gamePanel);
    }
}
