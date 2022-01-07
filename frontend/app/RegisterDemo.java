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
   
      // Username Label
      user_label = new JLabel();
      user_label.setText("User Name :");
      userName_text = new JTextField();
      
      // Password Label
      password_label = new JLabel();
      password_label.setText("Password :");
      password_text = new JPasswordField();
      
      // confirm password label
      confirmPassword_label = new JLabel();
      confirmPassword_label.setText("Confirm Password :");
      confirmPassword_text = new JPasswordField();
      
      // Submit
      submit = new JButton("SUBMIT");
      panel = new JPanel(new GridLayout(4, 1));
      panel.add(user_label);
      panel.add(userName_text);
      panel.add(password_label);
      panel.add(password_text);
      panel.add(confirmPassword_label);
      panel.add(confirmPassword_text);
      
      message = new JLabel();
      panel.add(message);
      panel.add(submit);
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Adding the listeners to components..
      submit.addActionListener(this);
      add(panel, BorderLayout.CENTER);
      setTitle("Please Register Here !");
      setSize(450,350);
      setVisible(true);
   }
   public static void main(String[] args) {
      new RegisterDemo();
   }
   @Override
   public void actionPerformed(ActionEvent ae) {
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
}