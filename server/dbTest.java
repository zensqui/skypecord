import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class dbTest {
    public static void main(String[] args) {
        DbInterface db = new DbInterface();
        JSONArray users = new JSONArray();
        users.add("test");
        users.add("test1");
        String cid = db.addConversation(users);
        System.out.println(cid);
        System.out.println(db.getUserConversations("test"));
        System.out.println(db.getUserConversations("test1"));
        System.out.println(db.getConversationUsers(cid));
        System.out.println(db.removeConversationUser(cid, "test"));
        System.out.println(db.getUserConversations("test"));
        System.out.println(db.getConversationUsers(cid));
        System.out.println("--------");
        System.out.println(db.addConversationUser(cid, "test"));
        System.out.println(db.getUserConversations("test1"));
        System.out.println(db.getConversationUsers(cid));
        System.out.println("--------");
        db.addMessage(cid, "test", "helkafkdlja;lkd f;lkads fhdjslf dsfhsj");
        db.addMessage(cid, "test1", "89042758940927849357098");
        System.out.println(db.getConversationMessages(cid));
    }
}
