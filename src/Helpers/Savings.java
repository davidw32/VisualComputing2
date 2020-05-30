package Helpers;

import Model.GraphicsObject;
import com.sun.tools.javac.Main;


import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

/**
 * @author Patrick Pavlenko
 * @version  0.1
 * Speicherung der Szene ( Alle ihre GraphicOBJs)
 */
public class Savings {

    public LinkedList<GraphicsObject> loadGraphicOBJs(String path)
    {
        LinkedList<GraphicsObject> objList = new LinkedList<>();
        try
        {

            InputStream in = Main.class.getClassLoader().getResourceAsStream(path);
            ObjectInputStream  objIN = new ObjectInputStream(in);
            while(objIN.read() != -1)
            {
                objList.add( (GraphicsObject) objIN.readObject() );
            }
        }
        catch(Exception e)
        {
            System.out.println("----NO SAVINGS LOADED-----");
            System.out.println(e);
        }
        return objList;
    }

    public void saveGraphicOBJs(LinkedList<GraphicsObject> obj,String path)
    {
        try
        {
            PrintStream outStream =  new PrintStream(path);
            ObjectOutputStream out = new ObjectOutputStream(outStream);
            for(GraphicsObject gobj : obj)
            {
                out.writeObject(obj);
            }
            out.flush();
        }
        catch(Exception e)
        {
            System.out.println("----SAVING FAILED----");
            System.out.println(e);
        }
    }
}
