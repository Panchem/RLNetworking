import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 *
 */
public class RLNetwork {

    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(PositionUpdate.class);
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
    public String name;
    int id;

    public int getId() {
        return id;
    }

    
}
