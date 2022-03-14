import java.io.IOException;

import org.json.simple.parser.ParseException;

public class test {
    public static void main(String[] args) throws IOException, ParseException {
        Client client = new Client();
        client.login("test", "test");

        String[] convos = client.getUserConvos();
        String convo = convos[0];
        System.out.println("cid: " + convo);
        String[] users = client.getConvoUsers(convo);
        if (users[0] != null) {
            System.out.println("fetched users successfully");
        }

        client.message(convo, "testingtesting123");

        String[] convosToo = client.getUserConvos();
        String convoToo = convosToo[0];
        System.out.println("cidToo: " + convoToo);
        String[] usersToo = client.getConvoUsers(convoToo);
        if (usersToo[0] != null) {
            System.out.println("fetched usersToo successfully");
        }
    }
}
