package deprecated;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class mainFrame {
    public static void main(String[] args) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame("main");
        frame.setSize(screenSize);

        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        contentPane.setBackground(Color.white);
        // Finish Frame

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setSize(screenSize.width, screenSize.height);

        frame.setLocation(screenSize.width / 2 - frame.getSize().width / 2,
                screenSize.height / 2 - frame.getSize().height / 2);
        // frame.setLocation(0,0);
        frame.setVisible(true);

        // frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
