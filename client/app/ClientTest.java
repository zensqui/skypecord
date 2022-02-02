public class ClientTest {
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        
        client.register("test0", "test0");
        client.register("test1", "test1");
        client.register("test2", "test2");
        client.register("test3", "test3");
        client.register("test4", "test4");
        client.register("test5", "test5");

        client.login("test0", "test0");
        client.login("test1", "test1");
        client.login("test2", "test2");
        client.login("test3", "test3");
        client.login("test4", "test4");
        client.login("test5", "test5");
    }
}
