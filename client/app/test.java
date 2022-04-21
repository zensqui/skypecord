import java.io.IOException;
import java.util.UUID;

import org.json.simple.parser.ParseException;

public class test {
    public static void checkForConvos(Client client) throws IOException, ParseException {
        String[] convos = client.getUserConvos();
        String convo = null;
        try {
            convo = convos[0];
            System.out.println("cid: " + convo);
        } catch (Exception e) {
            System.out.println("no convos (will lead to an error fetching users)");
        }
        try {
            String[] users = client.getConvoUsers(convo);
            if (users[0] != null) {
                System.out.println("users fetched successfully");
            }
        } catch (Exception e) {
            System.out.println("error fetching users");
        }
    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Client client = new Client();
        Client clientToo = new Client();

        String uid = UUID.randomUUID().toString();
        String uidToo = UUID.randomUUID().toString();

        client.login(uid, uid);
        clientToo.login(uidToo, uidToo);

        /*
         * System.out.println("\nConvos for client 1:");
         * checkForConvos(client);
         * System.out.println("\nConvos for client 2:");
         * checkForConvos(clientToo);
         */

        System.out.println("\nCreating convo and sending message from client 1 to user bradyap");

        String cid = client.addConvo(uid + ", bradyap");
        client.message(cid, "hello bradyap");

        // System.out.println("\nSending message from client 2 to random person");
        // String temp = clientToo.addConvo("test");
        // client.message(temp, "test");

        /*
         * System.out.println("\nCreating convo from client 1 --> client 2");
         * String temp1 = client.addConvo(uidToo);
         * System.out.println(temp1);
         * 
         * System.out.println("\nSending message to the new conversation:");
         * client.message(temp1, "test");
         * 
         * System.out.println("\nConvos for client 1:");
         * checkForConvos(client);
         * System.out.println("\nConvos for client 2:");
         * checkForConvos(clientToo);
         */

        System.exit(0);
    }
}
