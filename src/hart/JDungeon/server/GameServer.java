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
    private HashMap<Socket, ObjectOutputStream> outs;
    private HashMap<Socket, ObjectInputStream> ins;
    private ArrayList<String> out, in;
    private long Last;

    public GameServer(HashMap<String, Socket> con, HashMap<Socket, ObjectOutputStream> outs, HashMap<Socket, ObjectInputStream> ins, ArrayList<String> out, ArrayList<String> in)
    {
        this.con = con;
        this.out = out;
        this.in = in;
        this.outs = outs;
        this.ins = ins;
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
                        for (int i = 0; i != con.size(); i++)
                        {
                            try
                            {
                                outs.get(con.values().toArray()[i]).writeObject("MSG");
                                ins.get(con.values().toArray()[i]).readObject();
                                outs.get(con.values().toArray()[i]).writeObject(msg);
                                ins.get(con.values().toArray()[i]).readObject();
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
