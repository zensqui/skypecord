package deprecated.drawing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Panel1 extends JPanel {
   // PROPERTIES
   private final int DEFAULT_WIDTH = 100;
   private final int DEFAULT_HEIGHT = 400;
   private final Color BACK_COLOR = Color.BLACK;
   private static Graphics g;

   JButton send;
   JButton print;
   JButton erase;

   JPanel no;

   // CONSTRUCTOR
   public Panel1() {
      setBackground(BACK_COLOR);
      setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

      send = new JButton();
      send.setText("SEND");
      send.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      send.addActionListener(new ButtonListener());
      send.setFont(new Font("Arial", Font.BOLD, 30));
      add(send);

      print = new JButton();
      print.setText("PRINT");
      print.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      print.addActionListener(new ButtonListener());
      print.setFont(new Font("Arial", Font.BOLD, 30));
      add(print);

      erase = new JButton();
      erase.setText("ERASE");
      erase.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      erase.addActionListener(new ButtonListener());
      erase.setFont(new Font("Arial", Font.BOLD, 30));
      add(erase);
   }

   // action listener for button
   private class ButtonListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if ((JButton) e.getSource() == send) {
            Panel.printArray(Panel.getArray());
         } else if ((JButton) e.getSource() == print) {
            Panel2.buildImage(Panel.getArray());
         } else if ((JButton) e.getSource() == erase) {
            // JOptionPane.showMessageDialog(no,"There are no mistakes in art.");

            Panel.eraseAll();

         }
      }
   }
}