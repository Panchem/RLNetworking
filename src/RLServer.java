import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;

/**
 *
 */
public class RLServer {

    private Server server;

    int tcpPort;
    int udpPort;

    Hashtable<Connection, User> users = new Hashtable<>();

    static int currentID = 0;

    public RLServer(int tcpPort, int udpPort) throws IOException {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        server = new Server();

        server.start();

        System.out.println("> Server Started");

        server.bind(this.tcpPort, this.udpPort);

        System.out.println("> Server Bound to port " + tcpPort);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if(o instanceof PositionUpdate) {
                    server.sendToAllUDP(o);
                } else if (o instanceof AuthRequest) {
                    AuthRequest request = (AuthRequest) o;

                    System.out.println("> Received Auth Request " + request);

                    AuthResponse response = authenticate(request.name, request.password, connection);

                    connection.sendTCP(response);

                    if(!response.authSuccess) {
                        connection.close();
                    }
                } else if (o instanceof ChunkRequest) {
                    ChunkRequest request = (ChunkRequest) o;

                    System.out.println(connection + " requested chunk " + request.x + "x" + request.y);

                    Chunk newChunk = new Chunk();

                    newChunk.chunkData = getChunk(request.x, request.y);

                    newChunk.x = request.x;
                    newChunk.y = request.y;

                    connection.sendTCP(newChunk);
                }
            }

            @Override
            public void connected(Connection connection) {
                System.out.println("Received new connection from: " + connection.getRemoteAddressTCP());
            }

            @Override
            public void disconnected(Connection connection) {
                System.out.println(connection + " Disconnected");
            }
        });

        RLNetwork.registerClasses(server);
    }

    public Server getServer() {
        return server;
    }

    public byte[][] getChunk(int x, int y) {
        byte[][] rChunk = new byte[RLNetwork.CHUNK_SIZE][RLNetwork.CHUNK_SIZE];

        Random r = new Random(x * y);

        for (int i = 0; i < RLNetwork.CHUNK_SIZE; i++) {
            for (int j = 0; j < RLNetwork.CHUNK_SIZE; j++) {
                rChunk[i][j] = (byte) r.nextInt(2);
            }
        }

        return rChunk;
    }

    public AuthResponse authenticate(String name, String password, Connection connection) {

        User newUser = new User();
        newUser.id = ++currentID;
        newUser.name = name;
        users.put(connection, newUser);


        AuthResponse response = new AuthResponse();

        response.authSuccess = true;
        response.playerID = newUser.id;
        response.serverResponse = "Successfully authenticated user " + newUser.name;

        return response;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }
}
