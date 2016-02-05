package ca.panchem.rlnetworking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * ca.panchem.rlnetworking.RLNetwork
 *
 * @author Caleb Hiebert
 * @version 0.1
 */
public class RLNetwork {

    public static final int PORT = 25456;

    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(PositionUpdate.class);
        kryo.register(AuthRequest.class);
        kryo.register(AuthResponse.class);
        kryo.register(byte[][].class);
        kryo.register(byte[].class);
        kryo.register(byte.class);
        kryo.register(ChunkRequest.class);
        kryo.register(Chunk.class);
    }
}

class PositionUpdate {
    public int positionX, positionY, playerID;
}

class AuthRequest {
    public String name;
    public String password;

    @Override
    public String toString() {
        return "[" + name + ", " + password + "]";
    }
}

class AuthResponse {
    public boolean authSuccess;
    public String serverResponse;
    public int playerID;
}

class User {
    public String name;
    public int id;
}

class Chunk {
    public int x, y;
    public byte[][] chunkData;
}

class ChunkRequest {
    public int x, y;
}
