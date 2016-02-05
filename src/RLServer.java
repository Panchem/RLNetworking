import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

/**
 *
 */
public class RLServer {

    private Server server;

    int tcpPort;
    int udpPort;

    public RLServer(int tcpPort, int udpPort) throws IOException {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        server = new Server();

        server.start();

        server.bind(this.tcpPort, this.udpPort);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {

                if(o instanceof PositionUpdate) {
                    server.sendToAllUDP(o);
                }
            }

            @Override
            public void connected(Connection connection) {
                System.out.println("Received new connection from: " + connection.getRemoteAddressTCP());
            }

            @Override
            public void disconnected(Connection connection) {
                System.out.println(connection.getRemoteAddressTCP() + " Disconnected");
            }
        });
    }

    public Server getServer() {
        return server;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }
}
