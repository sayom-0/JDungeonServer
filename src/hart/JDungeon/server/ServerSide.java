package hart.JDungeon.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerSide extends Thread
{
    private ArrayList<String> conInfo, report;
    private ServerSocket serverSocket;
    private HashMap<String, Socket> connections;
    private HashMap<Socket, ObjectOutputStream> outs;
    private HashMap<Socket, ObjectInputStream> ins;
    private GameServer dm;
    public boolean run;
    public int ver;

    public ServerSide(int port, int ver) throws IOException
    {
        connections = new HashMap<>();
        outs = new HashMap<>();
        ins = new HashMap<>();
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
        run = true;
        this.ver = ver;
        this.conInfo = new ArrayList<>();
        this.report = new ArrayList<>();
    }

    public HashMap<Socket, ObjectOutputStream> getOuts()
    {
        return outs;
    }

    public HashMap<Socket, ObjectInputStream> getIns()
    {
        return ins;
    }

    public void run()
    {
        Socket server;
        String name;
        ObjectInputStream in;
        ObjectOutputStream out;
        int cver;
        dm = new GameServer(connections, outs, ins, conInfo, report);
        dm.start();
        System.out.println("Waiting for client(s) on port " +
                serverSocket.getLocalPort() + "...");
        while (run)
        {
            try
            {
                server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                in = new ObjectInputStream(server.getInputStream());
                out = new ObjectOutputStream(server.getOutputStream());

                System.out.println("Created input / output streams");

                ins.put(server, in);
                outs.put(server, out);

                System.out.println("Adding streams to HashMap");

                System.out.println((String) in.readObject());
                out.writeObject("RQ:NAME");
                name = (String) in.readObject();
                out.writeObject("RQ:VER");
                cver = (int) in.readObject();

                if (!(cver == ver))
                {
                    out.writeObject("Invalid Version Error\nSever Version : " + ver + "\nYour Version : " + cver);
                    server.close();
                    System.out.println("Invalid Version, aborting connection");
                    break;
                }

                out.writeObject("MSG");
                in.readObject();
                out.writeObject("Welcome to JDungeon from " + server.getLocalSocketAddress());
                in.readObject();

                connections.put(name, server);
                System.out.println("Added new player socket under name : " + name);

                ConnectionLoop cl = new ConnectionLoop(out, in, conInfo, report, server.getLocalSocketAddress().toString());
                cl.start();

                System.out.println("Announcing join");
                for (int i = 0; i != connections.size(); i++)
                {
                    try
                    {
                        outs.get(connections.values().toArray()[i]).writeObject("MSG");
                        ins.get(connections.values().toArray()[i]).readObject();
                        outs.get(connections.values().toArray()[i]).writeObject(name + " has joined the Junjeon crawl!");
                        ins.get(connections.values().toArray()[i]).readObject();
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
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            System.gc();
        }
        System.out.println("SeverSocket thread has ended!");
    }

    public HashMap<String, Socket> getCon()
    {
        return connections;
    }
}
