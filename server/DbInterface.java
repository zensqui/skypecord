import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class DbInterface {
    //seborah was here ()
    private static final String url = "jdbc:mysql://localhost:3306/skypecord?autoReconnect=true";
    private static final String user = "skypecord";
    private static final String password = "skypecord";
    private static Connection conn = null;

    public DbInterface() {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //? 0 = User exists.
    //? 1 = User does not exist.
    //? 2 = User check failed.
    public String userExists(String user) { //* utility function to check if a user exists given username
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE user=?");
            stmt.setString(1, user);
            ResultSet res = stmt.executeQuery();
            return res.next() ? "0" : "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "3";
        }
    }

    //? 0 = User created successfully.
    //? 1 = User already exists.
    //? 2 = User creation failed.
    public String addUser(String user, String pass) { //* add user given username and password
        try {
            if (userExists(user).equals("1")) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (user, pass) VALUES (?, ?)");
                stmt.setString(1, user);
                stmt.setString(2, pass);
                stmt.executeUpdate();
                return "0";
            }
            return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "2";
        }
    }

    //? 0 = User deleted successfully.
    //? 1 = User does not exist.
    //? 2 = User deletion failed.
    public String delUser(String user) { //* delete user given username
        try {
            if (userExists(user).equals("0")) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user=?");
                stmt.setString(1, user);
                stmt.executeUpdate();
                return "0";
            }
            return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "2";
        }
    }

    //? 0 = User authenticated successfully.
    //? 1 = User does not exist.
    //? 2 = User password incorrect.
    //? 3 = User authentication failed.
    public String auth(String user, String pass) { //* attempt to authenticate user given username and password
        try {
            if(userExists(user).equals("0")) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE user=?");
                stmt.setString(1, user);
                ResultSet res = stmt.executeQuery();
                res.next();
                String exit = res.getString("pass").equals(pass) ? "0" : "2";
                return exit;
            }
            return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "3";
        }
    }

    //? 0 = Conversation exists.
    //? 1 = Conversation does not exist.
    //? 2 = Conversation check failed.
    public String conversationExists(String cid) { //* utility function to check if a conversation exists given conversation id
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM conversations WHERE cid=?");
            stmt.setString(1, cid);
            ResultSet res = stmt.executeQuery();
            return res.next() ? "0" : "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "3";
        }
    }

    //? Returns conversation id if conversation exists.
    //? 1 = Conversation does not exist.
    //? 2 = Conversation check failed.
    public String userConversationExists(JSONArray users) {
        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM conversations");
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                JSONArray convoUsers = (JSONArray)new JSONParser().parse(res.getString("users"));
                if (convoUsers.containsAll(users) && users.containsAll(convoUsers)) {
                    return res.getString("cid");
                }
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "2";
        }
    }

    //? Returns id of conversation.
    //? Returns id of existing conversation with userlist if it exists.
    public String addConversation(JSONArray users) { //* add conversation given arraylist of users
        try {
            String check = userConversationExists(users);
            if (check.equals("1")) {
                String cid = UUID.randomUUID().toString();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO conversations (cid, users) VALUES (?, ?)");
                stmt.setString(1, cid);
                stmt.setString(2, users.toJSONString());
                stmt.executeUpdate();
                return cid; //? returns conversation id of created conversation
            } else {
                return check; //? returns id of existing conversation
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //? 0 = Conversation deleted successfully.
    //? 1 = Conversation does not exist.
    //? 2 = Conversation deletion failed.
    public String delConversation(String cid) { //* delete conversation given cid
        try {
            if (conversationExists(cid).equals("0")) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM conversations WHERE cid=?");
                stmt.setString(1, cid);
                stmt.executeUpdate();
                return "0";
            }
            return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "2";
        }
    }

    //? Returns list of users.
    public String getConversationUsers(String cid) { //* get list of users in conversation given cid
        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM conversations WHERE cid='%s'", cid);
            ResultSet res = stmt.executeQuery(sql);
            res.next();
            return res.getString("users");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //? Returns list of conversations.
    public String getUserConversations(String user) { //* get list of conversations for a given user
        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM conversations");
            ResultSet res = stmt.executeQuery(sql);
            JSONArray conversations = new JSONArray();
            while (res.next()) {
                JSONArray users = (JSONArray)new JSONParser().parse(res.getString("users"));
                if (users.contains(user)) {
                    conversations.add(res.getString("cid"));
                }
            }
            return conversations.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //? 0 = User added to conversation successfully.
    //? 1 = User already in conversation.
    //? 2 = Conversation does not exist.
    //? 3 = User addition to conversation failed.
    public String addConversationUser(String cid, String user) { //* add user to conversation given cid and user
        try {
            if (conversationExists(cid).equals("0")) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM conversations WHERE cid=?");
                stmt.setString(1, cid);
                ResultSet res = stmt.executeQuery();
                res.next();
                JSONArray users = (JSONArray)new JSONParser().parse(res.getString("users"));
                if (!users.contains(user)) {
                    users.add(user);
                    PreparedStatement stmt2 = conn.prepareStatement("UPDATE conversations SET users=? WHERE cid=?");
                    stmt2.setString(1, users.toJSONString());
                    stmt2.setString(2, cid);
                    stmt2.executeUpdate();
                    return "0";
                }
                return "1";
            }
            return "2";
        } catch (Exception e) {
            e.printStackTrace();
            return "3";
        }
    }

    //? 0 = User removed from conversation successfully.
    //? 1 = User not in conversation.
    //? 2 = Conversation does not exist.
    //? 3 = User removal from conversation failed.
    public String removeConversationUser(String cid, String user) { //* add user to conversation given cid and user
        try {
            if (conversationExists(cid).equals("0")) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM conversations WHERE cid=?");
                stmt.setString(1, cid);
                ResultSet res = stmt.executeQuery();
                res.next();
                JSONArray users = (JSONArray)new JSONParser().parse(res.getString("users"));
                if (users.contains(user)) {
                    users.remove(user);
                    PreparedStatement stmt2 = conn.prepareStatement("UPDATE conversations SET users=? WHERE cid=?");
                    stmt2.setString(1, users.toJSONString());
                    stmt2.setString(2, cid);
                    stmt2.executeUpdate();
                    return "0";
                }
                return "1";
            }
            return "2";
        } catch (Exception e) {
            e.printStackTrace();
            return "3";
        }
    }

    //? Returns list of messages.
    public String getConversationMessages(String cid) { //* get list of messages in conversation given cid
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM messages WHERE cid=?");
            stmt.setString(1, cid);
            ResultSet res = stmt.executeQuery();
            JSONArray messages = new JSONArray();
            while (res.next()) {
                JSONObject json = new JSONObject();
                json.put("mid", res.getString("mid"));
                json.put("user", res.getString("user"));
                json.put("message", res.getString("message"));
                messages.add(json);
            }
            return messages.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //? Returns message id. 
    public String addMessage(String cid, String user, String message) { //* add message to conversation given cid, user, and message
        String mid = UUID.randomUUID().toString();
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO messages (mid, cid, user, message) VALUES (?, ?, ?, ?)");
            stmt.setString(1, mid);
            stmt.setString(2, cid);
            stmt.setString(3, user);
            stmt.setString(4, message);
            stmt.executeUpdate();
            return mid;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}