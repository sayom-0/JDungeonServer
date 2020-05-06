package hart.JDungeon.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class JDCLI extends Thread
{
    private Scanner scan;
    private HashMap<String, Socket> con;
    private HashMap<Socket, ObjectOutputStream> outs;
    private HashMap<Socket, ObjectInputStream> ins;
    private boolean run;

    public JDCLI(HashMap<String, Socket> con, HashMap<Socket, ObjectOutputStream> outs, HashMap<Socket, ObjectInputStream> ins, Scanner scan)
    {
        this.scan = scan;
        this.con = con;
        this.run = true;
        this.outs = outs;
        this.ins = ins;
        System.out.println("Creating CLI Thread");
    }

    public void run()
    {
        System.out.println("CLI Thread Started");
        while (run)
        {
            System.out.print("=> ");
            switch (scan.nextLine())
            {
                case "list":
                    if (!con.isEmpty())
                        System.out.println(con);
                    else
                        System.out.println("No player sockets registered in connections HashMap");
                    break;

                case "exit":
                    run = false;
                    System.out.println("Exiting, thank you for hosting JDungeon!");
                    break;

                case "say":
                    System.out.print("Message => ");
                    String msg = scan.nextLine();
                    Socket server;
                    ObjectInputStream in;
                    ObjectOutputStream out;
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
                    break;

                case "kick":
                    System.out.print("Name => ");
                    try
                    {
                        con.remove(scan.nextLine()).close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    break;

                default:
                    System.out.println("Unrecognized Command");
                    break;
            }
        }

        System.out.println("CLI thread has ended!");
    }
}
