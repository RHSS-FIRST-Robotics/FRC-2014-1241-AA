/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.utilities;

/**
 *
 * @author Sagar
 */
public class JoystickScaler {
    
    
    double joyScaled;
    public double scaleJoystick(double joyRaw, int scalePower) {

        switch(scalePower){
            case 1:
                joyScaled = joyRaw;
                break;
            case 2:
                joyScaled = joyRaw*joyRaw;
                if (joyRaw < 0) {
                    joyScaled *= -1;
                }
            case 3:
                joyScaled = joyRaw*joyRaw*joyRaw;
                break;
                
            case 4:
                joyScaled = joyRaw*joyRaw*joyRaw*joyRaw;
                if (joyRaw < 0) {
                    joyScaled *= -1;
                }
                
                break;
                
            case 5:
                joyScaled = joyRaw*joyRaw*joyRaw*joyRaw*joyRaw;
                break;
        }
        
        return joyScaled;
    }
    
}
