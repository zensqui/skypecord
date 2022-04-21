package deprecated;

import javax.swing.*;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*;

public class e3 extends JPanel {

    public e3() {

        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JPanel Mainpanel = new JPanel();
        SpringLayout layout = new SpringLayout();

        Mainpanel.setLayout(layout);

        // How the HELL DO I DO THIS AAAAAAAAAAAAA
        // JTextPane Pane = new JTextPane();
        // How the HELL DO I DO THIS AAAAAAAAAAAAA

        String[] data = { "e", "ee", "ee", "eer", "ere", "eae", "ee", "efe", "ee", "ee", "ee", "eer", "ere", "eae",
                "ee", "efe", "ee", "ee", "ee", "eer", "ere", "eae", "ee", "efe", "ee", "ee", "ee", "eer", "ere", "eae",
                "ee", "efe", "ee", "ee", "efe", "ee", "ee", "efe", "ee", "ee", "efe", "ee", };

        JList list = new JList(data);

        list.setFont(new Font("serif", Font.PLAIN, 24));
        JScrollPane listScroller = new JScrollPane(list);

        layout.putConstraint(SpringLayout.WEST, listScroller, 5, SpringLayout.WEST, Mainpanel);
        layout.putConstraint(SpringLayout.NORTH, listScroller, 5, SpringLayout.NORTH, Mainpanel);
        layout.putConstraint(SpringLayout.SOUTH, listScroller, 5, SpringLayout.SOUTH, Mainpanel);
        listScroller.setPreferredSize(new Dimension(200, 100));

        JTextField label = new JTextField("e");

        label.setFont(new Font("serif", Font.PLAIN, 24));

        layout.putConstraint(SpringLayout.EAST, label, -25, SpringLayout.EAST, Mainpanel);
        layout.putConstraint(SpringLayout.WEST, label, 55, SpringLayout.EAST, listScroller);
        layout.putConstraint(SpringLayout.SOUTH, label, 5, SpringLayout.SOUTH, Mainpanel);
        layout.putConstraint(SpringLayout.NORTH, label, -55, SpringLayout.SOUTH, Mainpanel);

        JTextField name = new JTextField("Kyle Kramer");
        name.setFont(new Font("serif", Font.PLAIN, 24));

        layout.putConstraint(SpringLayout.NORTH, name, -5, SpringLayout.NORTH, Mainpanel);
        layout.putConstraint(SpringLayout.WEST, name, 0, SpringLayout.WEST, label);
        layout.putConstraint(SpringLayout.EAST, name, 0, SpringLayout.EAST, label);

        JTextField name1 = new JTextField("MESSAGES");
        name1.setFont(new Font("serif", Font.PLAIN, 24));

        layout.putConstraint(SpringLayout.NORTH, name1, 50, SpringLayout.NORTH, name);
        layout.putConstraint(SpringLayout.WEST, name1, 0, SpringLayout.WEST, label);
        layout.putConstraint(SpringLayout.EAST, name1, 0, SpringLayout.EAST, label);
        layout.putConstraint(SpringLayout.SOUTH, name1, -75, SpringLayout.SOUTH, label);

        Mainpanel.add(name1);
        Mainpanel.add(name);
        Mainpanel.add(label);
        Mainpanel.add(listScroller);
        setLayout(new BorderLayout());
        add(Mainpanel, BorderLayout.CENTER);

    }

}
