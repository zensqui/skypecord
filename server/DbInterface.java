import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DbInterface {
    private static final String url = "jdbc:mysql://localhost:3306/skypecord";
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

    public Boolean createUser(String user, String pass) {
        try {
            if (!userExists(user)) {
                Statement stmt = conn.createStatement();
                String sql = String.format("INSERT INTO users(user, pass) VALUES ('%s', '%s')", user, pass);
                stmt.executeUpdate(sql);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean userExists(String user) {
        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM users WHERE user='%s'", user);
            ResultSet res = stmt.executeQuery(sql);
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean auth(String user, String pass) {
        try {
            if(userExists(user)) {
                Statement stmt = conn.createStatement();
                String sql = String.format("SELECT * FROM users WHERE user='%s'", user);
                ResultSet res = stmt.executeQuery(sql);
                return res.getString("pass").equals(pass);
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean delUser(String user) {
        try {
            if (userExists(user)) {
                Statement stmt = conn.createStatement();
                String sql = String.format("DELETE FROM users WHERE user='%s'", user);
                stmt.executeUpdate(sql);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
