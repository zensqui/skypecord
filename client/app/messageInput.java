import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;

import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javax.speech.synthesis.SpeakableAdapter;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class messageInput extends JFrame implements ActionListener {

   JPanel panel;

   // for sending messages
   JButton send;
   JTextField message;
   JList<String> list;
   JScrollPane scrollPane;
   DefaultListModel<String> model;
   int maxSize;

   // directory
   JButton create;
   JButton deleteConvo;
   JButton addToConvo;
   JButton removeFromConvo;
   JList<String> dList;
   JScrollPane dScrollPane;
   DefaultListModel<String> dmodel;

   // settings
   JLabel settings;
   JButton logout;
   JLabel goodBye;
   JLabel about;

   JButton textToSpeak;
   boolean muted = false;
   Synthesizer synthesizer;

   JButton play;
   boolean paused = true;

   Boolean isDark;
   JButton darkMode;

   // true if a chat has been selected
   boolean chatSelected;
   // convo info
   String user;
   Client client;
   String convoID;
   HashMap<String, String> convo;

   // skypcord colors
   Color lightBlue;
   Color purple;
   Color blueish;
   Color darkDarkGray;

   public messageInput(Client client) throws IOException {

      // true if chat is selected
      chatSelected = false;

      // if dark mode is on it is true
      isDark = false;

      this.client = client;
      client.setMessageUi(this);

      // Skypcord colors
      lightBlue = new Color(154, 217, 234);
      purple = new Color(63, 72, 204);
      blueish = new Color(0, 162, 232);
      darkDarkGray = new Color(20, 20, 20);
      // stores name and id of conversations
      convo = new HashMap<String, String>();

      // gets screensize
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      SpringLayout layout = new SpringLayout();

      panel = new JPanel(layout);
      panel.setBackground(Color.WHITE);

      textSpeech("Welcome to Skypecord!");
      // Sending and Viewing
      // messages***********************************************************************************
      // stores all the messages
      model = new DefaultListModel<>();

      // gets your user
      user = client.getUser();

      // adds what the user sees on start up
      welcomeText("./client/app/welcome.txt");

      // scrolling for messages
      list = new JList<>(model);
      list.setFont(list.getFont().deriveFont(16.0f));

      scrollPane = new JScrollPane();
      scrollPane.setViewportView(list);
      list.setLayoutOrientation(JList.VERTICAL);
      // list.setBorder(BorderFactory.createEmptyBorder());

      // mouse listener for when you click on the text
      MouseListener mouseListener = new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
               String selectedItem = (String) list.getSelectedValue();
               // textSpeech(selectedItem);
               play.setVisible(true);
               synthesizer.cancelAll();
            } else if (e.getClickCount() == 2) {
               list.clearSelection();
               play.setVisible(false);
               synthesizer.cancelAll();
            }
         }
      };

      list.addMouseListener(mouseListener);

      // scroll pane scrolls to the bottem when model is updated
      maxSize = scrollPane.getVerticalScrollBar().getMaximum();
      scrollPane.getVerticalScrollBar().addAdjustmentListener(
            e -> {
               if ((maxSize - e.getAdjustable().getMaximum()) == 0)
                  return;

               e.getAdjustable().setValue(e.getAdjustable().getMaximum());
               maxSize = scrollPane.getVerticalScrollBar().getMaximum();
            });

      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scrollPane, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel);
      scrollPane.setPreferredSize(new Dimension(800, 600));
      panel.add(scrollPane);
      // list.setBackground(lightBlue);
      // list.setSelectionBackground(lightBlue);
      list.setSelectionBackground(Color.LIGHT_GRAY);

      // place and size of message JTextField
      message = new JTextField();
      // message.setBorder(BorderFactory.createEmptyBorder());
      message.setFont(new Font("SansSerif", Font.PLAIN, 18));
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, message, -37, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, message, 625, SpringLayout.NORTH, panel);
      message.setPreferredSize(new Dimension(725, 50));
      message.addKeyListener(new keyListener());
      panel.add(message);

      // place and size of send JButton
      ImageIcon sendPic = new ImageIcon("./client/app/content/sendbutton.jpg");

      send = new JButton(sendPic);
      send.setOpaque(false);
      send.setContentAreaFilled(false);
      send.setBorderPainted(false);
      send.setFocusPainted(false);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, send, 363, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, send, 625, SpringLayout.NORTH, panel);
      send.setPreferredSize(new Dimension(75, 50));
      send.addActionListener(this);
      panel.add(send);

      // directory**************************************************************************************
      // list for directory
      dmodel = new DefaultListModel<>();

      dList = new JList<>(dmodel);

      // sets size of each indevidual cell in list
      dList.setFont(dList.getFont().deriveFont(18.0f));
      dList.setFixedCellHeight(40);
      // dList.setBackground(lightBlue);
      // dList.setSelectionBackground(lightBlue);
      // dList.setSelectionForeground(purple);

      dScrollPane = new JScrollPane();
      dScrollPane.setViewportView(dList);
      dList.setLayoutOrientation(JList.VERTICAL);

      // mouse listener for when you click on a item in the directory
      MouseListener mousedListener = new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
               String selectedItem = (String) dList.getSelectedValue();
               convoID = convo.get(selectedItem);
               model.clear();
               deleteConvo.setVisible(true);
               addToConvo.setVisible(true);
               removeFromConvo.setVisible(true);
               model.addElement("Chat with " + selectedItem);
               model.addElement(" ");
               getMsgs(convoID);
               chatSelected = true;
            } else if (e.getClickCount() == 2) {
               dList.clearSelection();
               model.clear();
               welcomeText("./client/app/welcome.txt");
               deleteConvo.setVisible(false);
               addToConvo.setVisible(false);
               removeFromConvo.setVisible(false);
               chatSelected = false;
            }
         }
      };

      dList.addMouseListener(mousedListener);

      // size and place of the directory
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, dScrollPane, -550, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, dScrollPane, 10, SpringLayout.NORTH, panel);
      dScrollPane.setPreferredSize(new Dimension(250, 600));
      panel.add(dScrollPane);
      dList.setSelectionBackground(Color.LIGHT_GRAY);

      // size and shape of the create JButton
      ImageIcon createPic = new ImageIcon("./client/app/content/createbutton.jpg");
      create = new JButton(createPic);
      create.setOpaque(false);
      create.setContentAreaFilled(false);
      create.setBorderPainted(false);
      create.setFocusPainted(false);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, create, -550, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, create, 625, SpringLayout.NORTH, panel);
      create.setPreferredSize(new Dimension(250, 50));
      create.addActionListener(this);
      panel.add(create);

      // size and shape of the editConvos JButton
      ImageIcon deletePic = new ImageIcon("./client/app/content/deletebutton.jpg");

      deleteConvo = new JButton(deletePic);
      deleteConvo.setOpaque(false);
      deleteConvo.setContentAreaFilled(false);
      deleteConvo.setBorderPainted(false);
      deleteConvo.setFocusPainted(false);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, deleteConvo, -275, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, deleteConvo, 675, SpringLayout.NORTH, panel);
      deleteConvo.setPreferredSize(new Dimension(250, 50));
      deleteConvo.addActionListener(this);
      panel.add(deleteConvo);
      deleteConvo.setVisible(false);

      // size and shape of the editConvos JButton
      ImageIcon addPic = new ImageIcon("./client/app/content/addbutton.jpg");

      addToConvo = new JButton(addPic);
      addToConvo.setOpaque(false);
      addToConvo.setContentAreaFilled(false);
      addToConvo.setBorderPainted(false);
      addToConvo.setFocusPainted(false);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, addToConvo, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, addToConvo, 675, SpringLayout.NORTH, panel);
      addToConvo.setPreferredSize(new Dimension(250, 50));
      addToConvo.addActionListener(this);
      panel.add(addToConvo);
      addToConvo.setVisible(false);

      // size and shape of the editConvos JButton
      ImageIcon removePic = new ImageIcon("./client/app/content/removebutton.jpg");

      removeFromConvo = new JButton(removePic);
      removeFromConvo.setOpaque(false);
      removeFromConvo.setContentAreaFilled(false);
      removeFromConvo.setBorderPainted(false);
      removeFromConvo.setFocusPainted(false);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, removeFromConvo, 275, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, removeFromConvo, 675, SpringLayout.NORTH, panel);
      removeFromConvo.setPreferredSize(new Dimension(250, 50));
      removeFromConvo.addActionListener(this);
      panel.add(removeFromConvo);
      removeFromConvo.setVisible(false);

      // settings**************************************************************************************

      settings = new JLabel("Settings");
      settings.setFont(new Font("SansSerif", Font.BOLD, 20));
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, settings, 550, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, settings, 5, SpringLayout.NORTH, panel);
      settings.setPreferredSize(new Dimension(250, 50));
      panel.add(settings);

      ImageIcon logoutPic = new ImageIcon("./client/app/content/logoutbutton.jpg");
      logout = new JButton(logoutPic);
      logout.setOpaque(false);
      logout.setContentAreaFilled(false);
      logout.setBorderPainted(false);
      logout.setFocusPainted(false);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, logout, 550, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, logout, 100, SpringLayout.NORTH, panel);
      logout.setPreferredSize(new Dimension(250, 50));
      logout.addActionListener(this);
      panel.add(logout);

      goodBye = new JLabel("Good-Bye!");
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, goodBye, 0, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, goodBye, 300, SpringLayout.NORTH, panel);
      goodBye.setPreferredSize(new Dimension(500, 100));
      panel.add(goodBye);
      goodBye.setVisible(false);

      about = new JLabel();
      about.setFont(new Font("font", Font.PLAIN, 20));
      about.setText("<html>This project was created by<br>" +
            "Brady Pettengill and Morgan Wagner<br>" +
            "as a revolutionary replacement for<br>" +
            "your current communication technology.</html>");
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, about, 550, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, about, 300, SpringLayout.NORTH, panel);
      about.setPreferredSize(new Dimension(250, 300));
      panel.add(about);

      ImageIcon darkModePic = new ImageIcon("./client/app/content/darkmodebutton.jpg");
      darkMode = new JButton(darkModePic);
      darkMode.setOpaque(false);
      darkMode.setContentAreaFilled(false);
      darkMode.setBorderPainted(false);
      darkMode.setFocusPainted(false);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, darkMode, 550, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, darkMode, 175, SpringLayout.NORTH, panel);
      darkMode.setPreferredSize(new Dimension(250, 50));
      darkMode.addActionListener(this);
      panel.add(darkMode);

      ImageIcon tts = new ImageIcon("./client/app/content/unmuted.jpg");
      textToSpeak = new JButton(tts);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textToSpeak, 500, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, textToSpeak, 275, SpringLayout.NORTH, panel);
      textToSpeak.setPreferredSize(new Dimension(75, 50));
      textToSpeak.addActionListener(this);
      panel.add(textToSpeak);
      textToSpeak.setOpaque(false);
      textToSpeak.setContentAreaFilled(false);
      textToSpeak.setBorderPainted(false);
      textToSpeak.setFocusPainted(false);

      ImageIcon PLAY = new ImageIcon("./client/app/content/play.jpg");
      play = new JButton(PLAY);
      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, play, 600, SpringLayout.HORIZONTAL_CENTER, panel);
      layout.putConstraint(SpringLayout.NORTH, play, 275, SpringLayout.NORTH, panel);
      play.setPreferredSize(new Dimension(75, 50));
      play.addActionListener(this);
      play.setVisible(false);
      panel.add(play);
      play.setOpaque(false);
      play.setContentAreaFilled(false);
      play.setBorderPainted(false);
      play.setFocusPainted(false);

      // gets all the conversations on your account and puts them in directory
      getConvos();

      // frame set up
      add(panel, BorderLayout.CENTER);
      setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
      setSize(screenSize.width, screenSize.height);
      setResizable(false);
      setVisible(true);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   // adds message to conversation
   public void addMessage(String user, String data, String cid) {
      if (cid.equals(convoID)) {

         // the first line should have the username
         boolean lineStarted = false;

         FontMetrics metrics = getFontMetrics(new Font("SansSerif", Font.PLAIN, 18));

         // makes an indent of a certain size
         String userSpace = "";
         for (int i = 0; i < (user.length() + 2) * 2 + 1; i++) {
            userSpace += " ";
         }

         // splits the sentance in to an array of words split by spaces
         String[] words = data.trim().split(" ");
         // for(int i = 0; i < words.length;i++)
         // System.out.println(words[i]);
         if (metrics.stringWidth(user + ": " + data) > 785) {

            // loops through all the words in the message
            String curr = "";
            for (int i = 0; i < words.length; i++) {

               // if the word is bigger than the field can hold it splits it up
               if (metrics.stringWidth(user + ": " + words[i]) > 785) {
                  for (int j = words[i].length() - 1; j >= 0; j--) {
                     if (metrics.stringWidth(user + ": " + words[i].substring(0, j + 1)) < 785) {
                        if (!lineStarted) {
                           model.addElement(user + ": " + words[i].substring(0, j + 1).trim());
                           words[i] = words[i].substring(j + 1);
                           j = words[i].length();
                           lineStarted = true;
                        } else {
                           model.addElement(userSpace + words[i].substring(0, j + 1).trim());
                           words[i] = words[i].substring(j + 1);
                           j = words[i].length();
                        }
                     }
                  }
               }
               // if the message is to long for the field it splits it up
               else if (metrics.stringWidth(user + ": " + curr.trim() + " " + words[i]) > 785) {
                  if (!lineStarted) {
                     model.addElement(user + ": " + curr.trim());
                     // System.out.println(curr);
                     lineStarted = true;
                  } else
                     model.addElement(userSpace + curr.trim());
                  curr = "";
               }
               curr = curr + " " + words[i].trim();
            }
            if (curr != "")
               model.addElement(userSpace + curr.trim());
            // System.out.println(curr);
         }
         // if it is not to big it just adds it
         else
            model.addElement(user + ": " + data);

      } else if (!convo.containsValue(cid)) {
         System.out.println("CONVO DOESN'T EXIST YET, TRYING TO CREATE..");
         addConvo(cid);
         dmodel.clear();
         getConvos();
      }
   }

   // adds conversations to the directory
   public void addConvo(String cid) {
      try {
         System.out.println("REQUESTING CONVO INFO FOR " + cid);
         String[] users = client.getConvoUsers(cid);
         String name = "";
         for (String u : users) {
            u = u.substring(1, u.length() - 1);
            if (!u.equals(user)) {
               name += u + ", ";
            }
         }
         if (name.length() >= 2)
            name = name.substring(0, name.length() - 2);

         convo.put(name, cid);
         dmodel.addElement(name);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   // returns the chat selected
   public boolean getChatSelected() {
      return this.chatSelected;
   }

   // adds all the messages to the message panel
   public void getMsgs(String convoID) {
      try {
         JSONArray msgs;
         msgs = client.getConvoMessages(convoID);
         for (int i = 0; i < msgs.size(); i++) {
            JSONObject msg = (JSONObject) msgs.get(i);
            String user = (String) msg.get("user");
            String data = (String) msg.get("message");
            addMessage(user, data, convoID);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // reads out the string you input
   public void textSpeech(String str) {
      try {
         // Set property as Kevin Dictionary
         System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

         // VoiceManager vm = VoiceManager.getInstance();
         /*
          * Voice voice = vm.getVoice("kevin16");
          * voice.allocate();
          * if(!muted){
          * voice.speak(str);
          * }//else
          * //voice.cancelAll();
          */

         // Register Engine
         Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

         // Voice voice = vm.getVoice("kevin16");

         // Create a Synthesizer
         synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
         // synthesizer.getSynthesizerProperties().setVoice(voice);
         // Allocate synthesizer
         synthesizer.allocate();
         // Resume Synthesizer
         synthesizer.resume();

         // Speaks the given text
         // until the queue is empty.
         if (!muted) {
            synthesizer.speakPlainText(str, null);
         } else
            synthesizer.cancelAll();
         // stops program until you are done with the tts
         // synthesizer.waitEngineState(
         // Synthesizer.QUEUE_EMPTY);

         // Deallocate the Synthesizer.
         // synthesizer.deallocate();*/
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // text that shows up on start up
   // "//" at start of line = don't show
   // "/ + first letter of a color" = make line that color
   public void welcomeText(String fileName) {
      BufferedReader in;
      try {
         in = new BufferedReader(new FileReader(fileName));

         String line = in.readLine();
         while (line != null) {
            if (line.length() < 2) {
               model.addElement(line);

            } else if (line.substring(0, 2).equals("/r")) { // red
               line = "<html><font color=\"red\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            } else if (line.substring(0, 2).equals("/g")) { // green
               line = "<html><font color=\"green\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            } else if (line.substring(0, 2).equals("/b")) { // blue
               line = "<html><font color=\"blue\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            } else if (line.substring(0, 2).equals("/o")) { // orange
               line = "<html><font color=\"orange\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            } else if (line.substring(0, 2).equals("/y")) { // yellow
               line = "<html><font color=\"yellow\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            } else if (line.substring(0, 2).equals("/p")) { // purple
               line = "<html><font color=\"purple\">" + line.substring(2) + "</font></html>";
               model.addElement(line);

            } else if (!line.substring(0, 2).equals("//")) { // don't show line if it starts with "//"
               model.addElement(line);
            }
            line = in.readLine();
         }
         in.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // gets all the convos user is a part of
   public void getConvos() {
      try {
         String[] convos;
         convos = client.getUserConvos();
         for (int i = 0; i < convos.length; i++) {
            String convoID = convos[i];
            addConvo(convoID);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // Action Listener for buttons
   @Override
   public void actionPerformed(ActionEvent e) {

      // sends message to target user
      // sets message JTextField = to "";
      if ((JButton) e.getSource() == send) {
         if (!message.getText().equals("") && chatSelected) {
            // model.addElement(user + ": " + message.getText());
            addMessage(user, message.getText(), convoID);
            try {
               client.message(convoID, message.getText());
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }
         // System.out.println(message.getText());
         message.setText("");
      }

      // creates new conversation
      if ((JButton) e.getSource() == create) {
         String test1 = "";
         while (test1.equals("")) {
            test1 = JOptionPane
                  .showInputDialog("Please enter a comma separated list of users you would like to chat with:");
            if (test1 == null)
               return;
         }

         String[] names = test1.split(", ");
         for (int i = 0; i < names.length; i++) {
            try {
               if (client.userExists(names[i]).equals("1")) {
                  JOptionPane.showMessageDialog(null, "Please enter valid user names.",
                        "User doesn't exist", JOptionPane.QUESTION_MESSAGE, null);
                  return;
               }
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         }

         try {
            dmodel.addElement(test1);
            String test2 = test1 + ", " + user;
            convoID = client.addConvo(test2);
            convo.put(test1, convoID);
         } catch (IOException e1) {
            e1.printStackTrace();
         }
      }

      // deletes conversation
      if ((JButton) e.getSource() == deleteConvo) {
         String chatInput = dList.getSelectedValue();

         String[] options = { "Yes", "No" };

         // choice = index of options array the user chooses
         int n = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this conversation",
               "Edit Conversations",
               JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

         if (n != 0) {
            return;
         }

         String cid = convo.remove(chatInput);
         try {
            client.delConvo(cid);

         } catch (IOException e1) {
            e1.printStackTrace();
         }
         // updates the list of messages the user sees if the chat selected was the one
         // deleted
         if (chatInput.equals((String) dList.getSelectedValue())) {
            model.clear();
            welcomeText("./client/app/welcome.txt");
            convo.remove(chatInput);
            chatSelected = false;
         }
         deleteConvo.setVisible(false);
         addToConvo.setVisible(false);
         removeFromConvo.setVisible(false);
         // updates directory
         for (int i = 0; i < dmodel.size(); i++) {
            if (dmodel.elementAt(i).equals(chatInput)) {
               dmodel.remove(i);
               System.out.print(dmodel.get(i));
            }
         }

         System.out.println("Chat Deleted");
      }

      if ((JButton) e.getSource() == addToConvo) {
         String chatInput = dList.getSelectedValue();

         String addUser = "";
         while (addUser.equals("")) {
            addUser = JOptionPane.showInputDialog("Enter User you want to add to the conversation");

            if (addUser == null) {
               return;
            }
         }

         try {
            if (client.userExists(addUser).equals("1")) {
               JOptionPane.showMessageDialog(null, "Please enter valid user names.",
                     "User doesn't exist", JOptionPane.QUESTION_MESSAGE, null);
               return;
            }
         } catch (IOException e1) {
            e1.printStackTrace();
         }

         String cid = convo.get(chatInput);

         try {
            client.addConvoUser(cid, addUser);
         } catch (IOException e1) {
            e1.printStackTrace();
         }

         // updates directory
         for (int i = 0; i < dmodel.size(); i++) {
            if (dmodel.elementAt(i).equals(chatInput)) {
               String test = dmodel.remove(i);
               dmodel.add(i, test + ", " + addUser);

               convo.remove(test);
               convo.put(test + ", " + addUser, cid);

               dList.setSelectedIndex(i);
               break;
            }
         }

         // adds a update message in the conversation for the users to see who was added
         String chatUpdate = "Added " + addUser + " to the conversation.";
         try {
            client.message(cid, chatUpdate);
         } catch (Exception ex) {
            ex.printStackTrace();
         }

         String selectedItem = (String) dList.getSelectedValue();
         convoID = convo.get(selectedItem);
         model.clear();
         model.addElement("Chat with " + selectedItem);
         model.addElement(" ");
         getMsgs(convoID);
      }

      if ((JButton) e.getSource() == removeFromConvo) {

         String chatInput = dList.getSelectedValue();

         if (chatInput == null) {
            return;
         }

         String userRemoved = "";
         String cid = convo.get(chatInput);
         String[] selected = chatInput.split(", ");

         // list of conversations to edit
         userRemoved = (String) JOptionPane.showInputDialog(null, "Choose user you want to remove.",
               "Edit Conversations", JOptionPane.QUESTION_MESSAGE, null, selected, selected[0]);

         if (userRemoved == null) {
            return;
         }

         try {
            //// System.out.println("it tried");
            client.delConvoUser(cid, userRemoved);
            // System.out.println("it worked maybe");
         } catch (IOException e1) {
            e1.printStackTrace();
         }
         // System.out.println("User Removed");

         String finalName = "";

         for (int i = 0; i < selected.length; i++) {
            if (!selected[i].equals(userRemoved)) {
               finalName = finalName + selected[i] + ", ";
            }
         }

         // updates directory
         for (int i = 0; i < dmodel.size(); i++) {
            if (dmodel.elementAt(i).equals(chatInput)) {
               dmodel.remove(i);
               dmodel.add(i, finalName.substring(0, finalName.length() - 2));

               convo.remove(chatInput);
               convo.put(finalName.substring(0, finalName.length() - 2), cid);

               dList.setSelectedIndex(i);
               break;
            }
         }
         // adds a update message in the conversation for the users to see who was added
         String chatUpdate = "Removed " + userRemoved + " from the conversation.";

         try {
            client.message(cid, chatUpdate);
         } catch (Exception ex) {
            ex.printStackTrace();
         }

         String selectedItem = (String) dList.getSelectedValue();
         convoID = convo.get(selectedItem);
         model.clear();
         model.addElement("Chat with " + selectedItem);
         model.addElement(" ");
         getMsgs(convoID);
      }

      if ((JButton) e.getSource() == logout) {

         System.exit(0);
      }

      if ((JButton) e.getSource() == darkMode) {

         isDark = !isDark;

         if (isDark) {
            panel.setBackground(Color.BLACK);

            dList.setBackground(darkDarkGray);
            dList.setForeground(Color.WHITE);
            dList.setSelectionBackground(Color.BLACK);
            dList.setSelectionForeground(Color.WHITE);

            list.setBackground(darkDarkGray);
            list.setForeground(Color.WHITE);
            list.setSelectionBackground(Color.BLACK);
            list.setSelectionForeground(Color.WHITE);

            message.setBackground(darkDarkGray);
            message.setForeground(Color.WHITE);
            message.setCaretColor(Color.WHITE);

            ImageIcon sendPic = new ImageIcon("./client/app/content/sendbuttondark.jpg");
            send.setIcon(sendPic);

            ImageIcon logoutPic = new ImageIcon("./client/app/content/logoutbuttondark.jpg");
            logout.setIcon(logoutPic);

            ImageIcon createPic = new ImageIcon("./client/app/content/createbuttondark.jpg");
            create.setIcon(createPic);

            ImageIcon deletePic = new ImageIcon("./client/app/content/deletebuttondark.jpg");
            deleteConvo.setIcon(deletePic);

            ImageIcon addPic = new ImageIcon("./client/app/content/addbuttondark.jpg");
            addToConvo.setIcon(addPic);

            ImageIcon removePic = new ImageIcon("./client/app/content/removebuttondark.jpg");
            removeFromConvo.setIcon(removePic);

            ImageIcon darkModePic = new ImageIcon("./client/app/content/darkmodebuttondark.jpg");
            darkMode.setIcon(darkModePic);

            about.setForeground(Color.WHITE);

            settings.setForeground(Color.WHITE);
         } else if (!isDark) {

            panel.setBackground(Color.WHITE);

            dList.setBackground(Color.WHITE);
            dList.setForeground(Color.BLACK);
            dList.setSelectionBackground(Color.LIGHT_GRAY);
            dList.setSelectionForeground(Color.BLACK);

            list.setBackground(Color.WHITE);
            list.setForeground(Color.BLACK);
            list.setSelectionBackground(Color.LIGHT_GRAY);
            list.setSelectionForeground(Color.BLACK);

            message.setBackground(Color.WHITE);
            message.setForeground(Color.BLACK);
            message.setCaretColor(Color.BLACK);

            ImageIcon sendPic = new ImageIcon("./client/app/content/sendbutton.jpg");
            send.setIcon(sendPic);

            ImageIcon logoutPic = new ImageIcon("./client/app/content/logoutbutton.jpg");
            logout.setIcon(logoutPic);

            ImageIcon createPic = new ImageIcon("./client/app/content/createbutton.jpg");
            create.setIcon(createPic);

            ImageIcon deletePic = new ImageIcon("./client/app/content/deletebutton.jpg");
            deleteConvo.setIcon(deletePic);

            ImageIcon addPic = new ImageIcon("./client/app/content/addbutton.jpg");
            addToConvo.setIcon(addPic);

            ImageIcon removePic = new ImageIcon("./client/app/content/removebutton.jpg");
            removeFromConvo.setIcon(removePic);

            ImageIcon darkModePic = new ImageIcon("./client/app/content/darkmodebutton.jpg");
            darkMode.setIcon(darkModePic);

            about.setForeground(Color.BLACK);

            settings.setForeground(Color.BLACK);
         }

      }
      if ((JButton) e.getSource() == textToSpeak) {
         muted = !muted;
         if (muted) {
            ImageIcon m = new ImageIcon("./client/app/content/muted.jpg");
            textToSpeak.setIcon(m);
            synthesizer.cancelAll();
         } else {
            ImageIcon um = new ImageIcon("./client/app/content/unmuted.jpg");
            textToSpeak.setIcon(um);
         }
      }

      if ((JButton) e.getSource() == play) {
         paused = !paused;

         if (paused) {
            ImageIcon PLAY = new ImageIcon("./client/app/content/play.jpg");
            play.setIcon(PLAY);
            synthesizer.pause();
         } else {
            for (int i = list.getSelectedIndex(); i < list.getModel().getSize(); i++) {
               textSpeech((String) list.getModel().getElementAt(i));
            }
            textSpeech((String) list.getSelectedValue());

            ImageIcon PAUSE = new ImageIcon("./client/app/content/pause.jpg");
            play.setIcon(PAUSE);
            try {
               synthesizer.resume();
            } catch (Exception e1) {
               e1.printStackTrace();
            }
         }

      }
   }

   // keylistener
   private class keyListener implements KeyListener {

      @Override
      public void keyTyped(KeyEvent e) {

      }

      @Override
      public void keyPressed(KeyEvent e) {
         // when enter is pressed it sends a message
         if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!message.getText().equals("") && chatSelected) {
               // model.addElement(user + ": " + message.getText());
               addMessage(user, message.getText(), convoID);
               try {
                  client.message(convoID, message.getText());
               } catch (Exception ex) {
                  ex.printStackTrace();
               }
            }
            // System.out.println(message.getText());
            message.setText("");
         }
      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
   }
}
