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

        contentPane.setBackground(Color.white);

        ImageIcon logo = new ImageIcon("./client/app/content/logo.jpg");

        ImageIcon scaledImage = new ImageIcon(logo.getImage().getScaledInstance(logo.getIconWidth() / 2,logo.getIconHeight() / 2, Image.SCALE_SMOOTH));

        JLabel scaledLogo = new JLabel(scaledImage);

        frame.add(scaledLogo);
        //layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scaledLogo, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
        //layout.putConstraint(SpringLayout.NORTH, scaledLogo, 200, SpringLayout.NORTH, contentPane);
        //scaledLogo.setPreferredSize(new Dimension(1200, 400));
        scaledLogo.setPreferredSize(new Dimension(525, 400));


        // Login Button setup
        ImageIcon loginPic = new ImageIcon("./client/app/content/loginbutton.jpg");

        JButton login = new JButton(loginPic);
        
        login.setOpaque(false);
        login.setContentAreaFilled(false);
        login.setBorderPainted(false);
        login.setFocusPainted(false);

        contentPane.add(login);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, login, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
        layout.putConstraint(SpringLayout.NORTH, login, 400, SpringLayout.NORTH, contentPane);
        login.setPreferredSize(new Dimension(360, 50));
        login.addActionListener(new LoginPressed());

        // Register Button setup

        ImageIcon registerPic = new ImageIcon("./client/app/content/registerbutton.jpg");

        JButton Register = new JButton(registerPic);
        
        Register.setOpaque(false);
        Register.setContentAreaFilled(false);
        Register.setBorderPainted(false);
        Register.setFocusPainted(false);

        contentPane.add(Register);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, Register, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
        layout.putConstraint(SpringLayout.NORTH, Register, 500, SpringLayout.NORTH, contentPane);
        Register.setPreferredSize(new Dimension(360, 50));
        Register.addActionListener(new RegisterPressed());

        // Finish Frame
        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        //frame.setSize(screenSize.width - 100, screenSize.height - 100);
        frame.setSize(550, 600);
        frame.setLocation(screenSize.width/2-frame.getSize().width/2, screenSize.height/2-frame.getSize().height/2);
        frame.setVisible(true);
        //frame.setLocation(400, 50);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // login button actions
    private static class LoginPressed implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            LoginDemo login = new LoginDemo();
            login.setLocation(screenSize.width / 2 - login.getSize().width / 2, screenSize.height / 2 - login.getSize().height / 2);
        }
    }

    // login Register actions
    private static class RegisterPressed implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            RegisterDemo register = new RegisterDemo();
            register.setLocation(screenSize.width / 2 - register.getSize().width / 2, screenSize.height / 2 - register.getSize().height / 2);
            
        }
    }
}
