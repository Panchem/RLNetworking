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

    private boolean initialized;

    private int localID;

    public RLClient(String host, int tcpPort, int udpPort) throws IOException {
        this.host = host;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        client = new Client();
        client.start();

        client.addListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                System.out.println("> Sending Auth Request");

                AuthRequest ar = new AuthRequest();

                ar.name = "Name";
                ar.password = "Password";

                client.sendTCP(ar);
            }

            @Override
            public void received(Connection connection, Object o) {

                if(o instanceof AuthResponse) {
                    AuthResponse response = (AuthResponse) o;

                    if(response.authSuccess) {
                        localID = response.playerID;

                        initialized = true;

                        System.out.println("> " + response.serverResponse);
                    } else {
                        System.out.print("> There was an error authenticating! " + response.serverResponse);
                    }
                } if (o instanceof Chunk) {
                    String block = "";

                    for (byte[] arr : ((Chunk)o).chunkData) {
                        for (byte b : arr) {
                            block += b;
                        }
                        block += "\n";
                    }

                    System.out.print(block);
                } if (o instanceof PositionUpdate) {
                    PositionUpdate positionUpdate = (PositionUpdate) o;

                    if(positionUpdate.playerID != localID) {
                        //move the stuff
                    }
                }
            }
        });

        RLNetwork.registerClasses(client);

        client.connect(5000, host, tcpPort, udpPort);
    }

    public void sendPositionUpdate(int x, int y) {
        if(!initialized) {
            System.out.println("Client is not authenticated yet! Position update will not be sent.");
            return;
        }

        PositionUpdate pu = new PositionUpdate();

        pu.positionX = x;
        pu.positionY = y;
        pu.playerID = localID;

        client.sendUDP(pu);
    }

    public void requestChunk(int x, int y) {
        if(!initialized) {
            System.out.println("Client is not authenticated yet! Chunk request will not be sent.");
            return;
        }

        System.out.println("> Requesting chunk at " + x + "x" + y);

        ChunkRequest chunkRequest = new ChunkRequest();
        chunkRequest.x = x;
        chunkRequest.y = y;

        client.sendTCP(chunkRequest);
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
