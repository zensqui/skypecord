import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class DbInterface {
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
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM users WHERE user='%s'", user);
            ResultSet res = stmt.executeQuery(sql);
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
                Statement stmt = conn.createStatement();
                String sql = String.format("INSERT INTO users(user, pass) VALUES ('%s', '%s')", user, pass);
                stmt.executeUpdate(sql);
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
                Statement stmt = conn.createStatement();
                String sql = String.format("DELETE FROM users WHERE user='%s'", user);
                stmt.executeUpdate(sql);
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
                Statement stmt = conn.createStatement();
                String sql = String.format("SELECT * FROM users WHERE user='%s'", user);
                ResultSet res = stmt.executeQuery(sql);
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
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM conversations WHERE cid='%s'", cid);
            ResultSet res = stmt.executeQuery(sql);
            return res.next() ? "0" : "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "3";
        }
    }

    //? Returns id of conversation.
    public String addConversation(JSONArray users) { //* add conversation given arraylist of users
        String cid = UUID.randomUUID().toString();
        JSONObject json = new JSONObject();
        json.put("users", users);

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("INSERT INTO conversations(cid, users) VALUES ('%s', '%s')", cid, users);
            stmt.executeUpdate(sql);
            return cid; //? returns conversation id of created conversation
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
                Statement stmt = conn.createStatement();
                String sql = String.format("DELETE FROM conversations WHERE cid='%s'", cid);
                stmt.executeUpdate(sql);
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
                Statement stmt = conn.createStatement();
                String sql = String.format("SELECT * FROM conversations WHERE cid='%s'", cid);
                ResultSet res = stmt.executeQuery(sql);
                res.next();
                JSONArray users = (JSONArray)new JSONParser().parse(res.getString("users"));
                if (!users.contains(user)) {
                    users.add(user);
                    sql = String.format("UPDATE conversations SET users='%s' WHERE cid='%s'", users.toJSONString(), cid);
                    stmt.executeUpdate(sql);
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
                Statement stmt = conn.createStatement();
                String sql = String.format("SELECT * FROM conversations WHERE cid='%s'", cid);
                ResultSet res = stmt.executeQuery(sql);
                res.next();
                JSONArray users = (JSONArray)new JSONParser().parse(res.getString("users"));
                if (users.contains(user)) {
                    users.remove(user);
                    sql = String.format("UPDATE conversations SET users='%s' WHERE cid='%s'", users.toJSONString(), cid);
                    stmt.executeUpdate(sql);
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
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM messages WHERE cid='%s'", cid);
            ResultSet res = stmt.executeQuery(sql);
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
            Statement stmt = conn.createStatement();
            String sql = String.format("INSERT INTO messages(mid, cid, user, message) VALUES ('%s', '%s', '%s', '%s')", mid, cid, user, message);
            stmt.executeUpdate(sql);
            return mid;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}