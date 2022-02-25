import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
   JButton editConvos;
   JList<String> dList;
   JScrollPane dScrollPane;
   DefaultListModel<String> dmodel;

   //true if a chat has been selected
   boolean chatSelected;
   // convo info 
   String user;
   Client client;
   String convoID;
   HashMap<String, String> convo;
   

    public messageInput(Client client) throws IOException {

      chatSelected = false;

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
         //model.addElement("No conversation selected.");
         //model.addElement("   ");
         welcomeText("./client/app/welcome.txt");

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
                  convoID = convo.get(selectedItem);

                  model.clear();
                  
                  getMsgs(convoID);
                  
                  chatSelected = true;
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

         editConvos = new JButton("Edit Conversation");
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, editConvos, -550, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, editConvos, 675, SpringLayout.NORTH, panel);
         editConvos.setPreferredSize(new Dimension(250, 50));
         editConvos.addActionListener(this);
         panel.add(editConvos);
      //////////////////************************************************************************************* */
         getConvos();

         add(panel, BorderLayout.CENTER);
         setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
         setSize(screenSize.width, screenSize.height);
         setResizable(false);
         setVisible(true);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   public void addMessage(String user, String data, String cid){
      if (cid.equals(convoID)) {
         model.addElement(user + ": " + data);
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

   public boolean getChatSelected(){
      return this.chatSelected;
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

   //text that shows up on start up
   public void welcomeText(String fileName){
      BufferedReader in;
      try {
         in = new BufferedReader(new FileReader(fileName));

         String line = in.readLine();
         while(line != null)
         {  
            if(line.length() < 2) {
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/r")){
               line = "<html><font color=\"red\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/g")){
               line = "<html><font color=\"green\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/b")){
               line = "<html><font color=\"blue\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/o")){
               line = "<html><font color=\"orange\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/y")){
               line = "<html><font color=\"yellow\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/p")){
               line = "<html><font color=\"purple\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(!line.substring(0, 2).equals("//")){  
               model.addElement(line);
            }
            line = in.readLine();
         }
         in.close();
      }   
      catch (Exception e) {
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
         if(!message.getText().equals("") && chatSelected){
            model.addElement(user + ": " + message.getText());
            try { 
               client.message(convoID, message.getText());
               } catch (Exception ex) {
                  ex.printStackTrace();
               }
         }
         //System.out.println(message.getText());
         message.setText("");
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
      
      if((JButton)e.getSource() == editConvos){
         String chatInput = "";
         String[] choices = new String[dmodel.size()];

         for(int i = 0; i < dmodel.size(); i++){
            choices[i] = dmodel.get(i);
         }

         chatInput = (String) JOptionPane.showInputDialog(null, "Choose Conversation you want to edit", 
            "Edit Conversations", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]); 

         if(!chatInput.equals("")){
            String[] options = {"Delete Convorsation", "Add User", "Remove User"};
        
            int choice = JOptionPane.showOptionDialog(null, "Please choose one",
               "Edit COnversations",
               JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            
            if(choice == 0){
               String cid = convo.remove(chatInput);
               try {
                  client.delConvo(cid);
               } catch (IOException e1) {
                  
                  e1.printStackTrace();
               }

               for(int i = 0; i < dmodel.size(); i++){
                  if(dmodel.elementAt(i).equals(chatInput)){
                     dmodel.remove(i);
                     System.out.println(dmodel.get(i));
                  }  
               }
               
               System.out.println("Chat Deleted");
            }
            else if(choice == 1){
               System.out.println("User Added");
            } 
            else if(choice == 2){
               System.out.println("User Removed");
            }
         }
      }
      
   }

private class keyListener implements KeyListener {

   @Override
   public void keyTyped(KeyEvent e) {
      
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         if(!message.getText().equals("") && chatSelected){
            model.addElement(user + ": " + message.getText());
            try { 
               client.message(convoID, message.getText());
               } catch (Exception ex) {
                  ex.printStackTrace();
               }
         }  
         //System.out.println(message.getText());
         message.setText("");
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      
   }
}
}
