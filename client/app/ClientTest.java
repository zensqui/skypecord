public class ClientTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            Client client = new Client();
            client.register("test" + i, "test" + i);
            client.login("test" + i, "test" + i);
        }
    }
}
