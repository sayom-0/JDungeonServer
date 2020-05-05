package hart.JDungeon.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class GameServer extends Thread
{
    private HashMap<String, Socket> con;
    private ArrayList<String> out, in;
    private long Last;

    public GameServer(HashMap<String, Socket> con, ArrayList<String> out, ArrayList<String> in)
    {
        this.con = con;
        this.out = out;
        this.in = in;
        Last = System.currentTimeMillis();
    }

    public void run()
    {
        while (true)
        {
            if (System.currentTimeMillis() - 500 >= Last)
            {
                while (!in.isEmpty())
                {
                    String msg = in.remove(0);
                    if (msg.charAt(0) == '*')
                    {

                    }
                    else if (msg.charAt(0) == '!')
                    {
                        Socket server;
                        ObjectInputStream in;
                        ObjectOutputStream out;
                        for (int i = 0; i != con.size(); i++)
                        {
                            server = (Socket) con.values().toArray()[i];
                            try
                            {
                                in = new ObjectInputStream(server.getInputStream());
                                out = new ObjectOutputStream(server.getOutputStream());
                                out.writeObject("MSG");
                                in.readObject();
                                out.writeObject(msg);
                                in.readObject();
                            } catch (IOException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                Last = System.currentTimeMillis();
            }
        }
    }
}
