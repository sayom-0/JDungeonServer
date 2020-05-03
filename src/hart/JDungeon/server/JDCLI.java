package hart.JDungeon.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class JDCLI extends Thread
{
    private Scanner scan;
    private HashMap<String, Socket> con;
    private boolean run;

    public JDCLI(HashMap<String, Socket> con, Scanner scan)
    {
        this.scan = scan;
        this.con = con;
        this.run = true;
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
                    if(!con.isEmpty())
                        System.out.println(con);
                    else
                        System.out.println("No player sockets registered in connections HashMap");
                    break;

                case "exit":
                    run = false;
                    System.out.println("Exiting, thank you for hosting JDungeon!");
                    break;

                default:
                    System.out.println("Unrecognized Command");
                    break;
            }
        }

        System.out.println("CLI thread has ended!");
    }
}
