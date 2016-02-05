package ca.panchem.rlnetworking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.Hashtable;

/**
 * ca.panchem.rlnetworking.RLServer
 *
 * @author Caleb Hiebert
 * @version 0.1
 */
public abstract class RLServer {

    private Server server;

    Hashtable<Connection, User> users = new Hashtable<>();

    static int currentID = 0;

    public RLServer() throws IOException {

        server = new Server();

        server.start();

        System.out.println("> Server Started");

        server.bind(RLNetwork.PORT, RLNetwork.PORT);

        System.out.println("> Server Bound to port " + RLNetwork.PORT);

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

    public abstract byte[][] getChunk(int x, int y);
}
