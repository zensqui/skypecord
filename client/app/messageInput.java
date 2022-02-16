import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.*;

import org.json.simple.JSONObject;

import java.util.*;
public class messageInput extends JFrame implements ActionListener{

    JPanel panel;
    
    //for sending messages
    JButton send;
    JTextField message;
    JList<String> list;
    JScrollPane scrollPane;
    DefaultListModel<String> model;
    int maxSize;

    //directory
    JButton create;
    JList<String> dList;
    JScrollPane dScrollPane;
    DefaultListModel<String> dmodel;

    String user;
    String targetUser;
    Client client;
    String convoID;
    HashMap<String, String> convo;

    public messageInput(Client client) throws IOException {
      this.client = client;
      client.setMessageUi(this);
      
      convo = new HashMap<String, String>();

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      SpringLayout layout = new SpringLayout();

      panel = new JPanel(layout);
      panel.setBackground(Color.WHITE);

      //Sending and Viewing messages***********************************************************************************
         model = new DefaultListModel<>();

         user = client.getUser();

         targetUser = "bradyap";

         send = new JButton();
         send.setText("Send");
        
         //the first element should have the user you are talking to
         model.addElement("Chat with " + targetUser);
         model.addElement("   ");
         list = new JList<>(model);
         list.setFont(list.getFont().deriveFont(16.0f));


         scrollPane = new JScrollPane();
         scrollPane.setViewportView(list);
         list.setLayoutOrientation(JList.VERTICAL);


         //scroll pane scrolls to the bottem when model is updated
         maxSize = scrollPane.getVerticalScrollBar().getMaximum();
         scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            if ((maxSize - e.getAdjustable().getMaximum()) == 0)  return;

            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            maxSize = scrollPane.getVerticalScrollBar().getMaximum();
         });
      
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scrollPane, 75, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel);
         scrollPane.setPreferredSize(new Dimension(800, 600));
         panel.add(scrollPane);
        
         message = new JTextField();
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, message, 35, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, message, 625, SpringLayout.NORTH, panel);
         message.setPreferredSize(new Dimension(725, 50));
         message.addKeyListener(new keyListener());
         panel.add(message);
         
         

         send = new JButton("Send");
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, send, 435, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, send, 625, SpringLayout.NORTH, panel);
         send.setPreferredSize(new Dimension(75, 50));
         send.addActionListener(this);
         panel.add(send);
         
         //directory**************************************************************************************
         dmodel = new DefaultListModel<>();

         create = new JButton();
         create.setText("create");
        
         dList = new JList<>(dmodel);
        
         dList.setFont(dList.getFont().deriveFont(18.0f));
         dList.setFixedCellHeight(40);

         dScrollPane = new JScrollPane();
         dScrollPane.setViewportView(dList);
         dList.setLayoutOrientation(JList.VERTICAL);

         MouseListener mousedListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               if (e.getClickCount() == 1) {
      
                  String selectedItem = (String) dList.getSelectedValue();
                  System.out.println(selectedItem);
                  convoID = convo.get(selectedItem + ", ");
                  model.clear();
                  model.addElement("Chat With " + selectedItem);
                  
                  getMsg(selectedItem);
                  
               }
            }
         };

         dList.addMouseListener(mousedListener);

         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, dScrollPane, -550, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, dScrollPane, 10, SpringLayout.NORTH, panel);
         dScrollPane.setPreferredSize(new Dimension(250, 600));
         panel.add(dScrollPane);
         
         create = new JButton("create");
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, create, -550, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, create, 625, SpringLayout.NORTH, panel);
         create.setPreferredSize(new Dimension(250, 50));
         create.addActionListener(this);
         panel.add(create);
      //////////////////************************************************************************************* */


         add(panel, BorderLayout.CENTER);
         setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
         setSize(screenSize.width, screenSize.height);
         setResizable(false);
         setVisible(true);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addMessage(String input){
      if(message.getText() == null){
         model.addElement(targetUser + ": " + input);
      }
    }

    public void getMsg(String fileName){
      fileName = fileName + ".txt";
      
      //client.getConvoMessages(convoID);
         //while(hasNext()){
            
         //   model.addElement();
         //}
   }


    @Override
   public void actionPerformed(ActionEvent e){

      if((JButton)e.getSource() == send){
         if(!(message.getText() == null)){
            model.addElement(user + ": " + message.getText());
            try { 
               client.message(convoID, message.getText());
               } catch (Exception ex) {
                  ex.printStackTrace();
               }
         }
      }

      if((JButton)e.getSource() == create){
         String test1 = JOptionPane.showInputDialog("UserName of Person you want to chat with");
         if(!(test1 == null)){
             try {
               dmodel.addElement(test1);
               test1+= ", " + user;
               JSONObject json = client.addConvo(test1);
               convoID = (String)json.get("cid");
               convo.put(test1, convoID);
            } catch (IOException e1) {
               e1.printStackTrace();
            }
             
         }
       }

      System.out.println(message.getText());
      message.setText("");
   }

private class keyListener implements KeyListener{

   @Override
   public void keyTyped(KeyEvent e) {
      
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         if(!(message.getText().equals(""))){
            model.addElement(user + ": " + message.getText());
            try { 
               client.message(convoID, message.getText());
               } catch (Exception ex) {
                  ex.printStackTrace();
               }
         }  
         System.out.println(message.getText());
         message.setText("");

     }
     
   }

   @Override
   public void keyReleased(KeyEvent e) {
      
   }
 }

}
