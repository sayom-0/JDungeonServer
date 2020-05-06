package hart.JDungeon.server;

import java.io.IOException;
import java.util.Scanner;

public class Launcher
{
    static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args)
    {
        ServerSide server = null;
        JDCLI cli = null;
        GameServer dm = null;
        int ver = 8;
        boolean run = true;

        System.out.print("Starting JDungeon Server Alpha Version " + ver + "\n    Specify Port : ");
        int port = Integer.parseInt(scan.nextLine());

        try
        {
            server = new ServerSide(port, ver);
            server.start();
            cli = new JDCLI(server.getCon(), server.getOuts(), server.getIns(), scan);
            cli.start();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Unable to start server on port " + port + "\nCLI init aborted");
        }

        while (run)
        {
            if (!cli.isAlive())
            {
                server.run = false;
                run = false;
            }
        }

        System.exit(0);

    }
}
