import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class messageInput extends JFrame implements ActionListener {

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

   // convo info 
   String user;
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

         send = new JButton();
         send.setText("Send");
        
         //the first element should have the user you are talking to
         model.addElement("No conversation selected.");
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
                  convoID = convo.get(selectedItem);

                  model.clear();

                  getMsgs(convoID);
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
         getConvos();

         add(panel, BorderLayout.CENTER);
         setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
         setSize(screenSize.width, screenSize.height);
         //! RESIZABLE
         setResizable(false);
         setVisible(true);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   public void addMessage(String user, String message, String cid) {
      if (cid.equals(convoID)) {
         model.addElement(user + ": " + message);
      } else if (!convo.containsValue(cid)) {
         System.out.println("dasjf;lkdsjflkas;jfldsklf;j");
         addConvo(cid);
      }
   }

   public void addConvo(String cid) {
      try {
         String[] users = client.getConvoUsers(cid);
         String name = "";
         for(String u : users) {
            u = u.substring(1, u.length() -1);
            if(!u.equals(user)){
               name += u + ", ";
            }
         }
         name = name.substring(0, name.length() - 2);

         convo.put(name, cid);
         dmodel.addElement(name);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void getMsgs(String convoID){
      try { 
         JSONArray msgs; 
         msgs = client.getConvoMessages(convoID);
         for(int i = 0; i < msgs.size(); i++){
            JSONObject msg = (JSONObject) msgs.get(i);
            String user = (String) msg.get("user");
            String data = (String) msg.get("message");
            System.out.println(user + ": " + data);
            model.addElement(user + ": " + data);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void getConvos(){
      try {
         String[] convos;
         convos = client.getUserConvos();
         for(int i = 0; i < convos.length; i++){
            String convoID = convos[i];
            addConvo(convoID);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
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
         String test1 = JOptionPane.showInputDialog("Please enter a comma separated list of users you would like to chat with:");
         if(!(test1 == null)){
            try {
               dmodel.addElement(test1);
               String test2 = test1 + ", " + user;
               convoID = client.addConvo(test2);
               convo.put(test1, convoID);
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         }
      }

      message.setText("");
   }

private class keyListener implements KeyListener {

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
