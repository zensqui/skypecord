package deprecated;

//Imports
import javax.swing.*;

import java.awt.*;

public class e {

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(200, 200);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setSize(screenSize.width - 100, screenSize.height - 100);
        frame.setContentPane(new e3());
        frame.setVisible(true);
        // frame.setLocation(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
