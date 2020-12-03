
import javax.swing.JFrame;
import javax.swing.JPannel;
import javax.swing.BorderFactory;


public class GUI {

    public GUI() {

        JFrame frame = new JFrame();

        
        JPannel pannel = new JPannel();
        pannel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        pannel.setLayout(new Gridlayout(0, 1));

        frame.add(pannel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Our GUI");
        frame.pack();
        frame.setVisible(true);
        
    }


}
