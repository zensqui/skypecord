import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.json.simple.JSONObject;

public class LoginDemo extends JFrame implements ActionListener {
   JPanel panel;
   JLabel user_label, password_label, message;
   JTextField userName_text;
   JPasswordField password_text;
   JButton submit, cancel;

   Client client;
   
   //final user name and passwords will be stored in here
   String u;  
   String p;
   
   public LoginDemo(Client client) {
      this.client = client;

      SpringLayout layout = new SpringLayout();

      //panel = new JPanel(new GridLayout(3, 1));
      panel = new JPanel(layout);

      panel.setBackground(Color.white);
      ImageIcon logo = new ImageIcon("./client/app/content/logo.jpg");

      ImageIcon scaledImage = new ImageIcon(logo.getImage().getScaledInstance(logo.getIconWidth() / 3,logo.getIconHeight() / 3, Image.SCALE_SMOOTH));

      JLabel scaledLogo = new JLabel(scaledImage);

      panel.add(scaledLogo);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scaledLogo, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, scaledLogo, -50, SpringLayout.NORTH, panel);
      //scaledLogo.setPreferredSize(new Dimension(1200, 400));
      scaledLogo.setPreferredSize(new Dimension(525, 400));

      user_label = new JLabel();
      user_label.setText("User Name :");
      panel.add(user_label);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, user_label, 75, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, user_label, 300, SpringLayout.NORTH, panel);
      user_label.setPreferredSize(new Dimension(360, 50));
      
      userName_text = new JTextField();
      panel.add(userName_text);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, userName_text, 50, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, userName_text, 300, SpringLayout.NORTH, panel);
      userName_text.setPreferredSize(new Dimension(150, 45));
      userName_text.addKeyListener(new keyListener());

      password_label = new JLabel();
      password_label.setText("Password :");
      panel.add(password_label);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, password_label, 75, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, password_label, 350, SpringLayout.NORTH, panel);
      password_label.setPreferredSize(new Dimension(360, 50));

      password_text = new JPasswordField();
      panel.add(password_text);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, password_text, 50, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, password_text, 350, SpringLayout.NORTH, panel);
      password_text.setPreferredSize(new Dimension(150, 45));
      password_text.addKeyListener(new keyListener());

      message = new JLabel();

      panel.add(message);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, message, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, message, 400, SpringLayout.NORTH, panel);
      message.setPreferredSize(new Dimension(200, 45));


      ImageIcon submitPic = new ImageIcon("./client/app/content/submitbutton.jpg");
      
      submit = new JButton(submitPic);
      panel.add(submit);
      
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, submit, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, submit, 450, SpringLayout.NORTH, panel);
      submit.setPreferredSize(new Dimension(300, 45));

      submit.setOpaque(false);
      submit.setContentAreaFilled(false);
      submit.setBorderPainted(false);
      submit.setFocusPainted(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Adding the listeners to components..
      submit.addActionListener(this);
      add(panel, BorderLayout.CENTER);
      setTitle("Skypecord | Login");
      setSize(550, 600);
      setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      String userName = userName_text.getText();
      String password = password_text.getText();        
      u = userName.trim();
      p = password.trim();
      try {
         JSONObject res = client.login(u, p);
         String exit = res.get("data").toString();
         switch (exit) {
            case "0":
               message.setText("Login successful.");
               setVisible(false);
               new messageInput(client);
               break;
            case "1":
               message.setText("Invalid user.");
               break;
            case "2":
               message.setText("Invalid password.");
               break;
            case "3":
               message.setText("Login failed. Please try again.");
               break;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private class keyListener implements KeyListener {

      @Override
      public void keyTyped(KeyEvent e) {
         
      }
   
      @Override
      public void keyPressed(KeyEvent e) {
         if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String userName = userName_text.getText();
            String password = password_text.getText();        
            u = userName.trim();
            p = password.trim();
            try {
               JSONObject res = client.login(u, p);
               String exit = res.get("data").toString();
               switch (exit) {
                  case "0":
                     message.setText("Login successful.");
                     setVisible(false);
                     new messageInput(client);
                     break;
                  case "1":
                     message.setText("Invalid user.");
                     break;
                  case "2":
                     message.setText("Invalid password.");
                     break;
                  case "3":
                     message.setText("Login failed. Please try again.");
                     break;
               }
            } catch (Exception ae) {
               ae.printStackTrace();
            }
         }
      }
   
      @Override
      public void keyReleased(KeyEvent e) {
         
      }
   }
}