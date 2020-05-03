package hart.JDungeon.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class ServerSide extends Thread
{
    private ServerSocket serverSocket;
    private HashMap<String, Socket> connections;
    public boolean run;

    public ServerSide(int port) throws IOException
    {
        connections = new HashMap<>();
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
        run = true;
    }

    public void run()
    {
        Socket server;
        String name;
        DataInputStream in;
        DataOutputStream out;
        System.out.println("Waiting for client(s) on port " +
                serverSocket.getLocalPort() + "...");
        while (run)
        {
            try
            {
                server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                in = new DataInputStream(server.getInputStream());
                out = new DataOutputStream(server.getOutputStream());

                System.out.println("Creating input / output streams");

                System.out.println(in.readUTF());
                out.writeUTF("Requesting : Player Name");
                name = in.readUTF();

                out.writeUTF("Welcome to JDungeon from " + server.getLocalSocketAddress());

                connections.put(name, server);
                System.out.println("Added new player socket under name : " + name);

            } catch (SocketTimeoutException s) { } catch (IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("SeverSocket thread has ended!");
    }

    public HashMap<String, Socket> getCon()
    {
        return connections;
    }
}
