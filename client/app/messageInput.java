import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
public class messageInput extends JFrame implements ActionListener, KeyListener{

    JPanel msgInpt;
    JButton send;
    JTextField message;
    JList<String> list;
    JScrollPane scrollPane;
    ArrayList<String> data;
    DefaultListModel<String> model;

    public messageInput(){

        SpringLayout layout = new SpringLayout();

        msgInpt = new JPanel(layout);

        send = new JButton();
        send.setText("Send");
        
        
        model = new DefaultListModel<>();
        model.addElement("New Conversation");
        model.addElement("   ");
        list = new JList<>(model);
        
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scrollPane, 90, SpringLayout.HORIZONTAL_CENTER, msgInpt);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, msgInpt);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        msgInpt.add(scrollPane);
        
        message = new JTextField();
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, message, 65, SpringLayout.HORIZONTAL_CENTER, msgInpt);
        layout.putConstraint(SpringLayout.NORTH, message, 400, SpringLayout.NORTH, msgInpt);
        message.setPreferredSize(new Dimension(250, 50));
        msgInpt.add(message);
        

        msgInpt.addKeyListener(this);

        send = new JButton("Send");
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, send, 215, SpringLayout.HORIZONTAL_CENTER, msgInpt);
        layout.putConstraint(SpringLayout.NORTH, send, 400, SpringLayout.NORTH, msgInpt);
        send.setPreferredSize(new Dimension(50, 50));
        msgInpt.add(send);
        send.addActionListener(this);
        


        add(msgInpt, BorderLayout.CENTER);
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        new messageInput();
    }


    @Override
   public void actionPerformed(ActionEvent e) {

      if((JButton)e.getSource() == send){
         if(!(message.getText() == null)){
            model.addElement(message.getText());
         }

      }
        System.out.println(message.getText());
        message.setText("");
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if(e.getKeyCode() == KeyEvent.VK_ENTER) {
         System.out.println("dkfja;kaldsfksjaf.l");
      }
   }
   public void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {
      if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
          System.out.println("dkfja;kaldsfksjaf.l");
       }
    } 
   @Override
   public void keyTyped(KeyEvent e) {
      
      
   }
   @Override
   public void keyReleased(KeyEvent e) {
      
      
   }
}
