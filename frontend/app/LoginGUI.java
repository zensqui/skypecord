//Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI {

    public static void main(String[] args) {

        // Gets Size of screen so we can set correct size and center the GUI
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame("LoginGUI");

        // Creates a container (practicaly the a panel) that is used to set up the GUI
        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        
        JLabel serverLabel = new JLabel("Enter Server Address");
        contentPane.add(serverLabel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, serverLabel, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
        layout.putConstraint(SpringLayout.NORTH, serverLabel, 200, SpringLayout.NORTH, contentPane);
        serverLabel.setPreferredSize(new Dimension(800, 50));
        

        JTextField serverAdd = new JTextField();
        contentPane.add(serverAdd);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, serverAdd, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
        layout.putConstraint(SpringLayout.NORTH, serverAdd, 300, SpringLayout.NORTH, contentPane);
        serverAdd.setPreferredSize(new Dimension(800, 50));

        //contains server address
        String s = serverAdd.getText();

        // Login Button setup
        JButton Login = new JButton("Login");
        contentPane.add(Login);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, Login, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
        layout.putConstraint(SpringLayout.NORTH, Login, 400, SpringLayout.NORTH, contentPane);
        Login.setPreferredSize(new Dimension(800, 50));
        Login.addActionListener(new LoginPressed());

        // Register Button setup
        JButton Register = new JButton("Register");
        contentPane.add(Register);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, Register, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
        layout.putConstraint(SpringLayout.NORTH, Register, 500, SpringLayout.NORTH, contentPane);
        Register.setPreferredSize(new Dimension(800, 50));
        Register.addActionListener(new RegisterPressed());

        // Finish Frame
        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setSize(screenSize.width - 100, screenSize.height - 100);
        frame.setVisible(true);
        frame.setLocation(50, 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // login button actions
    private static class LoginPressed implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            LoginDemo login = new LoginDemo();
            login.setLocation(400, 100);
        }
    }

    // login Register actions
    private static class RegisterPressed implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            RegisterDemo register = new RegisterDemo();
            register.setLocation(400, 100);
        }
    }
}
