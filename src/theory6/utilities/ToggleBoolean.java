/*
 * Boolean that only returns true when going from false to true
 * Boolean is only true once when set true
 * 
 * e.g. operator presses a button
 *      -for the first cycle the boolean is true
 *      -for subsequent cycles the boolean will be false if the button is held
 *      -when button is let go, boolean is still false
 */
package theory6.utilities;

/**
 *
 * @author Sagar
 */
public class ToggleBoolean {

    boolean state = false;
    boolean oldInput = false;

    public void set(boolean input) {
        if (oldInput == false && input == true) {
            state = true;
        } else {
            state = false;
        }

        oldInput = input;
    }

    public boolean get() {
        return state;
    }
}
