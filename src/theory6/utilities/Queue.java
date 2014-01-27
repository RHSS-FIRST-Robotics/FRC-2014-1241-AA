/*
 * Class Allows for queuing in J2ME
 */
package theory6.utilities;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 *
 * @author Sagar
 */
public class Queue extends Vector{
    
    public void addToQueue (Object obj) {
        this.addElement(obj);
    }
    
    public Object populateQueue() { 
        if (this.isEmpty()) {
            return null;
        }
        try {
            Object ret = this.firstElement();
            this.removeElementAt(0);
            return ret;
        }
        catch(NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }      
    }
}

