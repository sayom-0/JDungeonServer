package hart.JDungeon.server;

import java.io.IOException;
import java.util.Scanner;

public class Launcher
{
    static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args)
    {
        ServerSide t = null;
        JDCLI cli = null;
        double ver = 0.4;
        boolean run = true;

        System.out.print("Starting JDungeon Server Alpha Version " + ver + "\n    Specify Port : ");
        int port = Integer.parseInt(scan.nextLine());

        try
        {
            t = new ServerSide(port);
            t.start();
            cli = new JDCLI(t.getCon(), scan);
            cli.start();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Unable to start server on port " + port + "\nCLI init aborted");
        }

        while (run)
        {
            if(!cli.isAlive())
            {
                t.run = false;
                run = false;
            }
        }

    }
}
