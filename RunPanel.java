import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.BorderLayout;

//frame
public class RunPanel
{
   public static void main(String[] args)
   {
      JFrame frame = new JFrame("Panel");
      
      frame.setDefaultCloseOperation(3);
      frame.setLocation(0, 0);
      
      Panel panel = new Panel();
      Panel1 panel1 = new Panel1();
      Panel2 panel2 = new Panel2();
      
      
      //add
      frame.add(panel);
      frame.add(panel1);
      frame.add(panel2);
      
      //orgonize order
      frame.add(panel, BorderLayout.WEST);
      frame.add(panel1, BorderLayout.CENTER);
      frame.add(panel2, BorderLayout.EAST);
      
      frame.pack();
      frame.setVisible( true );
      frame.setResizable(false);

   }
}