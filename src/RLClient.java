import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

/**
 *
 */
public class RLClient {

    private Client client;
    private String host;

    private int tcpPort;
    private int udpPort;

    public RLClient(String host, int tcpPort, int udpPort) throws IOException {
        this.host = host;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        client = new Client();

        client.connect(5000, this.host, this.tcpPort, this.udpPort);

        RLNetwork.registerClasses(client);

        client.addListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                
            }
        });
    }

    public void sendPositionUpdate(int x, int y) {
        PositionUpdate pu = new PositionUpdate();

        pu.positionX = x;
        pu.positionY = y;
    }

    public Client getClient() {
        return client;
    }

    public String getHost() {
        return host;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }
}
