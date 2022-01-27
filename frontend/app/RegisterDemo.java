import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RegisterDemo extends JFrame implements ActionListener {
   JPanel panel;
   JLabel user_label, password_label, message, confirmPassword_label;
   JTextField userName_text;
   JPasswordField password_text;
   JPasswordField confirmPassword_text;
   JButton submit, cancel;
   
   //final user names and passwords will be stored in here
   String u;
   String p;
   

   public RegisterDemo() {

      SpringLayout layout = new SpringLayout();

      panel = new JPanel(layout);
      
      panel.setBackground(Color.white);
      ImageIcon logo = new ImageIcon("./content/logo.jpg");

      ImageIcon scaledImage = new ImageIcon(logo.getImage().getScaledInstance(logo.getIconWidth() / 3,logo.getIconHeight() / 3, Image.SCALE_SMOOTH));

      JLabel scaledLogo = new JLabel(scaledImage);

      panel.add(scaledLogo);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scaledLogo, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, scaledLogo, -50, SpringLayout.NORTH, panel);
      //scaledLogo.setPreferredSize(new Dimension(1200, 400));
      scaledLogo.setPreferredSize(new Dimension(525, 400));

      // Username Label
      user_label = new JLabel();
      user_label.setText("User Name :");
      userName_text = new JTextField();
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, user_label, 75, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, user_label, 300, SpringLayout.NORTH, panel);
      user_label.setPreferredSize(new Dimension(360, 50));

      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, userName_text, 50, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, userName_text, 300, SpringLayout.NORTH, panel);
      userName_text.setPreferredSize(new Dimension(150, 45));

      // Password Label
      password_label = new JLabel();
      password_label.setText("Password :");
      password_text = new JPasswordField();
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, password_label, 75, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, password_label, 350, SpringLayout.NORTH, panel);
      password_label.setPreferredSize(new Dimension(360, 50));

      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, password_text, 50, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, password_text, 350, SpringLayout.NORTH, panel);
      password_text.setPreferredSize(new Dimension(150, 45));

      // confirm password label
      confirmPassword_label = new JLabel();
      confirmPassword_label.setText("<html>Confirm<br>Password : </html>");
      confirmPassword_text = new JPasswordField();
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, confirmPassword_label, 75, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, confirmPassword_label, 400, SpringLayout.NORTH, panel);
      confirmPassword_label.setPreferredSize(new Dimension(360, 50));

      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, confirmPassword_text, 50, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, confirmPassword_text, 400, SpringLayout.NORTH, panel);
      confirmPassword_text.setPreferredSize(new Dimension(150, 45));


      // Submit
      ImageIcon submitPic = new ImageIcon("./content/submit button.jpg");
      submit = new JButton(submitPic);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, submit, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, submit, 480, SpringLayout.NORTH, panel);
      submit.setPreferredSize(new Dimension(300, 45));
      
      submit.setOpaque(false);
      submit.setContentAreaFilled(false);
      submit.setBorderPainted(false);
      submit.setFocusPainted(false);

      cancel = new JButton();
      
      
      panel.add(user_label);
      panel.add(userName_text);
      panel.add(password_label);
      panel.add(password_text);
      panel.add(confirmPassword_label);
      panel.add(confirmPassword_text);
      
      message = new JLabel();

      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, message, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, message, 445, SpringLayout.NORTH, panel);
      message.setPreferredSize(new Dimension(200, 45));

      panel.add(message);
      panel.add(submit);
      


      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Adding the listeners to components..
      submit.addActionListener(this);
      add(panel, BorderLayout.CENTER);
      setTitle("Please Register Here !");
      setSize(550, 600);
      setVisible(true);
   }
   public static void main(String[] args) {
      new RegisterDemo();
   }
   @Override
   public void actionPerformed(ActionEvent ae) {

      if((JButton)ae.getSource() == submit){
         String userName = userName_text.getText();
         String password = password_text.getText();
         String confirmPassword = confirmPassword_text.getText();
      
         if(userName.trim().equals("")){
            message.setText("Enter a UserName");
         }else if(password.trim().equals("")){
            message.setText("Enter Password");
         }else if(confirmPassword.trim().equals("")){
            message.setText("Please confirm password");
         }else if (confirmPassword.trim().equals(password.trim())) {
            message.setText(" Hello " + userName + "!");
            u = userName;
            p = password;
         
         }else if(confirmPassword.trim() != password.trim()){
            message.setText("Passwords don't match");
         }
      }
      else if((JButton)ae.getSource() == cancel){
         
      }
   }
}