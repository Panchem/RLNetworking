import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 *
 */
public class RLNetwork {

    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(PositionUpdate.class);
        kryo.register(AuthRequest.class);
        kryo.register(AuthResponse.class);
        kryo.register(byte[][].class);
        kryo.register(byte[].class);
        kryo.register(byte.class);
    }
}

class PositionUpdate {
    public int positionX, positionY;
}

class AuthRequest {
    public String name;
    public String password;
}

class AuthResponse {
    public boolean authSuccess;
    public int playerID;
}

class User {
    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class Chunk {
    public int x, y;
    public byte[][] chunkData;
}
