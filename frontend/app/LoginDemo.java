import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class LoginDemo extends JFrame implements ActionListener {
   JPanel panel;
   JLabel user_label, password_label, message;
   JTextField userName_text;
   JPasswordField password_text;
   JButton submit, cancel;
   
   //final user name and passwords will be stored in here
   String u;  
   String p;
   
   public LoginDemo() {
      // Username Label
      user_label = new JLabel();
      user_label.setText("User Name :");
      userName_text = new JTextField();
      // Password Label
      password_label = new JLabel();
      password_label.setText("Password :");
      password_text = new JPasswordField();
      // Submit
      submit = new JButton("SUBMIT");
      panel = new JPanel(new GridLayout(3, 1));
      panel.add(user_label);
      panel.add(userName_text);
      panel.add(password_label);
      panel.add(password_text);
      message = new JLabel();
      panel.add(message);
      panel.add(submit);
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Adding the listeners to components..
      submit.addActionListener(this);
      add(panel, BorderLayout.CENTER);
      setTitle("Please Login Here !");
      setSize(450,350);
      setVisible(true);
   }
   public static void main(String[] args) {
      new LoginDemo();
   }
   @Override
   public void actionPerformed(ActionEvent ae) {
      String userName = userName_text.getText();
      String password = password_text.getText();
      
      if(userName.trim().equals("")){
         message.setText("Enter a UserName");
      }else if(password.trim().equals("")){
         message.setText("Enter Password");
      }else if (userName.trim().equals("admin") && password.trim().equals("admin")) { //would search bank of user names and passwords
         message.setText(" Hello " + userName + "");
         u = userName;
         p = password;
      }else {                                      //if it can't find user name and password in bank 
         message.setText(" Invalid user.. ");
      }
   }
}