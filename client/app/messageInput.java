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
      
      //true if chat is selected
      chatSelected = false;

      this.client = client;
      client.setMessageUi(this);
      
      //stores name and id of conversations
      convo = new HashMap<String, String>();

      //gets screensize
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      SpringLayout layout = new SpringLayout();

      panel = new JPanel(layout);
      panel.setBackground(Color.WHITE);

      //Sending and Viewing messages***********************************************************************************
         //stores all the messages
         model = new DefaultListModel<>();

         //gets your user
         user = client.getUser();
        
         //adds what the user sees on start up
         welcomeText("./client/app/welcome.txt");

         //scrolling for messages
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

         //place and size of message JTextField
         message = new JTextField();
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, message, 35, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, message, 625, SpringLayout.NORTH, panel);
         message.setPreferredSize(new Dimension(725, 50));
         message.addKeyListener(new keyListener());
         panel.add(message);

         //place and size of send JButton
         send = new JButton("Send");
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, send, 435, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, send, 625, SpringLayout.NORTH, panel);
         send.setPreferredSize(new Dimension(75, 50));
         send.addActionListener(this);
         panel.add(send);
         
         //directory**************************************************************************************
         //list for directory
         dmodel = new DefaultListModel<>();

         dList = new JList<>(dmodel);
         
         //sets size of each indevidual cell in list
         dList.setFont(dList.getFont().deriveFont(18.0f));
         dList.setFixedCellHeight(40);

         dScrollPane = new JScrollPane();
         dScrollPane.setViewportView(dList);
         dList.setLayoutOrientation(JList.VERTICAL);

         //mouse listener for when you click on a item in the directory
         MouseListener mousedListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               if (e.getClickCount() == 1) {
                  String selectedItem = (String) dList.getSelectedValue();
                  System.out.println(selectedItem);
                  convoID = convo.get(selectedItem);
                  model.clear();
                  model.addElement("Conversation with " + selectedItem);
                  model.addElement(" ");
                  getMsgs(convoID);
                  
                  chatSelected = true;
               }
            }
         };

         dList.addMouseListener(mousedListener);

         //size and place of the directory
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, dScrollPane, -550, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, dScrollPane, 10, SpringLayout.NORTH, panel);
         dScrollPane.setPreferredSize(new Dimension(250, 600));
         panel.add(dScrollPane);
         
         //size and shape of the create JButton
         create = new JButton("create");
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, create, -550, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, create, 625, SpringLayout.NORTH, panel);
         create.setPreferredSize(new Dimension(250, 50));
         create.addActionListener(this);
         panel.add(create);

         //size and shape of the editConvos JButton
         editConvos = new JButton("Edit Conversation");
         layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, editConvos, -550, SpringLayout.HORIZONTAL_CENTER, panel);
         layout.putConstraint(SpringLayout.NORTH, editConvos, 675, SpringLayout.NORTH, panel);
         editConvos.setPreferredSize(new Dimension(250, 50));
         editConvos.addActionListener(this);
         panel.add(editConvos);

         //gets all the conversations on your account and puts them in directory
         getConvos();

         //frame set up
         add(panel, BorderLayout.CENTER);
         setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
         setSize(screenSize.width, screenSize.height);
         setResizable(false);
         setVisible(true);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   //adds message to conversation
   public void addMessage(String user, String data, String cid){
      if (cid.equals(convoID)) {
         model.addElement(user + ": " + data);
      } else if (!convo.containsValue(cid)) {
         System.out.println("dasjf;lkdsjflkas;jfldsklf;j");
         addConvo(cid);
      }
   }

   //adds conversations to the directory
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

   //returns the chat selected
   public boolean getChatSelected(){
      return this.chatSelected;
   }

   //adds all the messages to the message panel
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
   // "//" at start of line = don't show
   // "/ + first letter of a color" = make line that color
   public void welcomeText(String fileName){
      BufferedReader in;
      try {
         in = new BufferedReader(new FileReader(fileName));

         String line = in.readLine();
         while(line != null)
         {  
            if(line.length() < 2) {
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/r")){    //red
               line = "<html><font color=\"red\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/g")){    //green
               line = "<html><font color=\"green\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/b")){    //blue
               line = "<html><font color=\"blue\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/o")){    //orange
               line = "<html><font color=\"orange\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/y")){    //yellow
               line = "<html><font color=\"yellow\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(line.substring(0, 2).equals("/p")){    //purple
               line = "<html><font color=\"purple\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            }else if(!line.substring(0, 2).equals("//")){   //don't show line if it starts with "//"
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

   //gets all the convos user is a part of
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

   //Action Listener for buttons
   @Override
   public void actionPerformed(ActionEvent e){

      //sends message to target user
      //sets message JTextField = to "";
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

      //creates new conversation
      if((JButton)e.getSource() == create){
         String test1 = "";
         while(test1.equals("")){
            test1 = JOptionPane.showInputDialog("Please enter a comma separated list of users you would like to chat with:");
         }
         if(!(test1.equals(""))){
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
      
      //pops up edit convo window
      //user selects the convo they want to edit
      if((JButton)e.getSource() == editConvos){
         String chatInput = "";
         String[] choices = new String[dmodel.size()];

         for(int i = 0; i < dmodel.size(); i++){
            choices[i] = dmodel.get(i);
         }  

         //list of conversations to edit
         //chat input = conversation the user chooses
         chatInput = (String) JOptionPane.showInputDialog(null, "Choose Conversation you want to edit", 
            "Edit Conversations", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]); 

         //asks you what you want to edit
         //options the user gets
         if(!chatInput.equals("")){
            String[] options = {"Delete Convorsation", "Add User", "Remove User"};
            //choice = index of options array the user chooses
            int choice = JOptionPane.showOptionDialog(null, "Please choose one",
               "Edit COnversations",
               JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            
            //deletes a conversation
            if(choice == 0){
               String cid = convo.remove(chatInput);
               try {
                  client.delConvo(cid);
                  
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
               //updates the list of messages the user sees if the chat selected was the one deleted
               if(chatInput.equals((String) dList.getSelectedValue())){
                  model.clear();
                  welcomeText("./client/app/welcome.txt");
                  convo.remove(chatInput);
               }
               //updates directory
               for(int i = 0; i < dmodel.size(); i++){
                  if(dmodel.elementAt(i).equals(chatInput)){
                     dmodel.remove(i);
                     System.out.print(dmodel.get(i));
                  }  
               }
               
               System.out.println("Chat Deleted");
            }

            //adds a user to the conversation
            else if(choice == 1){
               String addUser = "";
               while(addUser.equals("")){
                  addUser = JOptionPane.showInputDialog("Enter User you want to add to the conversation");
               }
               String cid = convo.get(chatInput);

               try {
                  client.addConvoUser(cid, addUser);
               } catch (IOException e1) {
                  e1.printStackTrace();
               }

               //updates directory
               for(int i = 0; i < dmodel.size(); i++){
                  if(dmodel.elementAt(i).equals(chatInput)){
                     String test = dmodel.remove(i);
                     dmodel.add(i, test + ", " + addUser);
                     convo.remove(test);
                     convo.put(test + ", " + addUser, cid);
                     break;
                  }  
               }

               //adds a update message in the conversation for the users to see who was added
               String chatUpdate = "Added " + addUser + " to the conversation.";
               try {
                  client.message(cid, chatUpdate);
               } catch (Exception ex) {
                  ex.printStackTrace();
               }
               
               System.out.println("User Added");
            } 

            //removes user from a conversation
            else if(choice == 2){
               String userRemoved = "";
               String cid = convo.get(chatInput);
               String[] selected = chatInput.split(", ");
               
               //list of conversations to edit
               userRemoved = (String) JOptionPane.showInputDialog(null, "Choose Conversation you want to edit", 
                  "Edit Conversations", JOptionPane.QUESTION_MESSAGE, null, selected, selected[0]);

               try {
                  client.delConvoUser(cid, userRemoved.trim());
                  System.out.println("it worked maybe");
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
               System.out.println("User Removed");

            }
         }
      }
      
   }

   //keylistener
   private class keyListener implements KeyListener {

      @Override
      public void keyTyped(KeyEvent e) {
         
      }

      @Override
      public void keyPressed(KeyEvent e) {
         //when enter is pressed it sends a message
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
