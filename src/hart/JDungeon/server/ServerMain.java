package hart.JDungeon.server;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ServerMain extends Thread
{
    private ServerSocket serverSocket;

    public ServerMain(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(0);
    }

    public void run()
    {
        while (true)
        {
            try
            {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());

                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Welcome to JDungeon from " + server.getLocalSocketAddress());
                server.close();

            } catch (SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args)
    {
        double ver = 0.1;
        System.out.print("Starting JDungeon Server Version " + ver + "\n Specify Port : ");
        Scanner scan = new Scanner(System.in);

        int port = Integer.parseInt(scan.nextLine());
        try
        {
            Thread t = new ServerMain(port);
            t.start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
