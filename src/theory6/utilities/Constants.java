/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.utilities;

/**
 *
 * @author Team 1241
 */
import com.sun.squawk.microedition.io.FileConnection;
import com.sun.squawk.util.LineReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.microedition.io.Connector;

public class Constants {
    private static Constants instance;
    public static Hashtable constants;
    private static boolean  usingFile;

    private Constants(){
        usingFile = true;
        this.load();
    }
    
    public static Constants getInstance(){
        if(instance == null)
        {
            instance = new Constants();
        }
        return instance;
    }
    
    public static void load(){

        constants = getDefaults();
        System.out.println("loading File");

        if(usingFile){
            InputStream is = null;
            try {
                System.out.println("creating FileConnection");
                FileConnection f = (FileConnection) Connector.open("file:///constants.txt", Connector.READ_WRITE);
                System.out.println("created FileConnection - Class: ");
                if(f.exists()){
                    System.out.println("FileConnection exists");
                    is = f.openInputStream();
                    LineReader r = new LineReader(new InputStreamReader(is));
                    
                    String line = null;
                    int lineNum = 0;
                    while((line = r.readLine()) != null){
                        lineNum ++;
                        if(line.length() != 0 && line.charAt(0) != '#'){
                            int numSignPos = line.indexOf("#");
                            if(numSignPos > 0){
                                line = line.substring(0, numSignPos);
                            }
                            
                            int equalsSignPos = line.indexOf("=");
                            if(equalsSignPos <= 1){
                                System.out.println("INVALID SETTING LINE: " + line + " (" + lineNum + ")");
                            }else{
                                String key = line.substring(0, equalsSignPos - 1).trim();
                                String value = line.substring(equalsSignPos + 1).trim();
                                
                                if(key.length() > 0 && value.length() > 0){
                                    System.out.println("putting to HashTable: " + key + " = " + value);
                                    constants.put(key, value);
                                }
                            }
                        }

                    }
                }else{
                    f.create();
                    System.out.println("File has been Created");
                }
                f.close();
            } catch (Exception e) {
                System.out.println("failed to open fileConnection");
            }
        }
        
    }
    
    private static Hashtable getDefaults(){
        System.out.println("putting defaults");
        Hashtable defaults = new Hashtable();
        //////// DRIVE ////////
        //defaults.put("driveP", "0.1");
        //defaults.put("driveI", "0.2");
        //etc.
        System.out.println("defaults put");
        return defaults;
    }
    
    public static String getString(String constName, String def) {
        Object val = constants.get(constName);
        System.out.println("Reading String From Hash: " + val);
        if (val == null){
            System.out.println("Failed to return constant: " + constName);
            return def;
        }else{
            return (String) val;
        }
    }
    
    public static double getDouble (String constName) {
        try {
            double val = Double.parseDouble(getString(constName, ""));
            return val;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static int getInteger(String constName, int def) {
        try {
            int val = Integer.parseInt(getString(constName, ""));
            return val;
        } catch (Exception e) {
            return def;
        }
    }
    
    public static long getLong(String constName, long def) {
        try {
            long val = Long.parseLong(getString(constName,""));
            return val;
        } catch(NumberFormatException e){
            return def;
        }
    }
    
    public static boolean getBoolean (String constName, boolean def) {
        try {
            boolean val = getString(constName, "").toLowerCase().equals("true");
            if(getString(constName, "").toLowerCase().equals("false")){
                return val;
            }else{
                return def;
            }
        } catch(NumberFormatException e) {
            return def;
        }
    }
}
