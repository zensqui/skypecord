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
    //* 0 = User created successfully.
    //* 1 = User already exists.
    //* 2 = User creation failed.
    public int addUser(String user, String pass) {
        try {
            if (userExists(user) == 1) {
                Statement stmt = conn.createStatement();
                String sql = String.format("INSERT INTO users(user, pass) VALUES ('%s', '%s')", user, pass);
                stmt.executeUpdate(sql);
                return 0;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        }
    }

    //* 0 = User exists.
    //* 1 = User does not exist.
    //* 2 = User check failed.
    public int userExists(String user) {
        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM users WHERE user='%s'", user);
            ResultSet res = stmt.executeQuery(sql);
            return res.next() ? 0 : 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;
        }
    }

    //* 0 = User authenticated successfully.
    //* 1 = User does not exist.
    //* 2 = User password incorrect.
    //* 3 = User authentication failed.
    public int auth(String user, String pass) {
        try {
            if(userExists(user) == 0) {
                Statement stmt = conn.createStatement();
                String sql = String.format("SELECT * FROM users WHERE user='%s'", user);
                ResultSet res = stmt.executeQuery(sql);
                res.next();
                int exit = res.getString("pass").equals(pass) ? 0 : 2;
                return exit;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;
        }
    }

    //* 0 = User deleted successfully.
    //* 1 = User does not exist.
    //* 2 = User deletion failed.
    public int delUser(String user) {
        try {
            if (userExists(user) == 0) {
                Statement stmt = conn.createStatement();
                String sql = String.format("DELETE FROM users WHERE user='%s'", user);
                stmt.executeUpdate(sql);
                return 0;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        }
    }
}
