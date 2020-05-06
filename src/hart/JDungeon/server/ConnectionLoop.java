package hart.JDungeon.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ConnectionLoop extends Thread
{
    private ArrayList<String> report, OutInfo;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String info;
    private long Last;

    public ConnectionLoop(ObjectOutputStream out, ObjectInputStream in, ArrayList<String> OutInfo, ArrayList<String> report, String info) throws IOException
    {
        Last = System.currentTimeMillis();
        this.OutInfo = OutInfo;
        this.out = out;
        this.in = in;
        this.report = report;
        this.info = info;
        System.out.println("Creating thread for socket : " + info);
    }

    public void run()
    {
        System.out.println("Starting connection thread for socket : " + info);
        while (true)
        {
            if (System.currentTimeMillis() - 1500 >= Last)
            {
                try
                {
                    out.writeObject("RQ:INFO");
                    report.addAll((ArrayList<String>) in.readObject());
                    out.writeObject(OutInfo);
                } catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                Last = System.currentTimeMillis();
            }
        }
    }
}
