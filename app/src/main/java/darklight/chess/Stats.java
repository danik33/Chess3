package darklight.chess;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Stats implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    static File fn;


    int gamesPlayed;
    int gamesWon;


    public Stats()
    {
        this.gamesPlayed = 0;
        this.gamesWon = 0;
    }

    public static void setFile(File fn)
    {
        Stats.fn = fn;
    }





    public static Stats load()
    {
        try
        {
            FileInputStream sm = new FileInputStream(fn);
            ObjectInputStream ob = new ObjectInputStream(sm);
            Object a = ob.readObject();
            ob.close();
            sm.close();
            return (Stats)a;

        }
        catch (Exception e)
        {
            System.err.println("IZVINITE YA DEBIL");
            e.printStackTrace();
        }
        return null;
    }

    public void save()
    {
        try
        {
            FileOutputStream sm = new FileOutputStream(fn);
            ObjectOutputStream ob = new ObjectOutputStream(sm);
            ob.writeObject(this);
            ob.close();
            sm.close();
        }
        catch (Exception e)
        {
            System.err.println("IZVINITE YA DEBIL");
            e.printStackTrace();
        }
    }



}
