package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Communication {

    public static void sendObjectToClient(ObjectOutputStream out, Object o) throws IOException{
    	out.writeObject(o);
    	out.flush();
    }
    
    public static Object getObjectFromClient(ObjectInputStream in) throws IOException, ClassNotFoundException{
    	Object o = in.readObject();
    	return o;
    }
}
