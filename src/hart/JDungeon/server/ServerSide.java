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
    public int ver;

    public ServerSide(int port, int ver) throws IOException
    {
        connections = new HashMap<>();
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
        run = true;
        this.ver = ver;
    }

    public void run()
    {
        Socket server;
        String name;
        DataInputStream in;
        DataOutputStream out;
        int cver;
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
                out.writeUTF("RQ:NAME");
                name = in.readUTF();
                out.writeUTF("RQ:VER");
                cver = in.readInt();

                if (!(cver == ver))
                {
                    out.writeUTF("Invalid Version Error\nSever Version : " + ver + "\nYour Version : " + cver);
                    server.close();
                    break;
                }

                out.writeUTF("MSG");
                in.readUTF();
                out.writeUTF("Welcome to JDungeon from " + server.getLocalSocketAddress());
                in.readUTF();

                connections.put(name, server);
                System.out.println("Added new player socket under name : " + name);
                System.out.println("Announcing join");
                for (int i = 0; i != connections.size(); i++)
                {
                    server = (Socket) connections.values().toArray()[i];
                    try
                    {
                        in = new DataInputStream(server.getInputStream());
                        out = new DataOutputStream(server.getOutputStream());
                        out.writeUTF("MSG");
                        in.readUTF();
                        out.writeUTF(name + " has joined the Junjeon crawl!");
                        in.readUTF();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                System.out.print("=> ");
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
