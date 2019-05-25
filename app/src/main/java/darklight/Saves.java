package darklight;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class Saves{

    public static void saveObject(final File f, final Object ob)
    {
        Thread save = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    FileOutputStream sm = new FileOutputStream(f);
                    ObjectOutputStream obs = new ObjectOutputStream(sm);
                    obs.writeObject(ob);
                    obs.close();
                    sm.close();
                }
                catch (NotSerializableException e)
                {
                    System.out.println("ERROR at saving, not serializable object." + ob.getClass().getName());
                }
                catch (IOException e)
                {
                    System.out.println("Problem at saving. '" + f + "', message: " + e.getMessage());

                }
            }
        }, "Save thread..");
        save.start();
    }

    public static Object loadObject(File f)
    {

        try
        {
            FileInputStream sm = new FileInputStream(f);
            ObjectInputStream ob = new ObjectInputStream(sm);
            Object rd = ob.readObject();
            ob.close();
            sm.close();
            System.out.println("LOADED");
            return rd;

        }
        catch (IOException e)
        {
            if(!(e instanceof FileNotFoundException)) {
                f.delete();
                System.out.println("Deleted the save, as it appears corrupted");
                e.printStackTrace();
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
