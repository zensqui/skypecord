import javax.swing.*;
import java.awt.*;
public class ChessPanel extends JPanel{
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
    public JPanel boardPanel = new JPanel();
    private JButton[][] boardButtons = new JButton[8][8];
    private void initialize(){
        boardPanel.setBackground(Color.getHSBColor(.2f, .2f, .9f));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardButtons[i][j].setBackground(Color.getHSBColor(0, 0, (i + j) % 2));
                boardPanel.add(boardButtons[i][j]);
            }
        }
    }
    
}