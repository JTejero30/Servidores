package TESTChatUDP.Model;

public class Cliente {
    String name;
    int port;


    public Cliente() {
    }

    public Cliente(String name, int port, boolean connected) {
        this.name = name;
        this.port = port;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
