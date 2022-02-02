package deprecated;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;


public class GUI {

    public GUI() {

        JFrame frame = new JFrame();

        
        JPanel pannel = new JPanel();
        pannel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        //pannel.setLayout(new Gridlayout(0, 1));

        //frame.add(pannel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Our GUI");
        frame.pack();
        frame.setVisible(true);
        
    }


}
