/*
 * Class to keep absolute orientation of gyro while still maintaining the 
 * relative postition to which gyro was "zeroed"
 */
package theory6.utilities;

import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author Sagar
 */

public class RelativeGyro extends Gyro {

    double curAngle = 0;
    public RelativeGyro(int port){
        super(port);
    }

    public void reset(){
        curAngle = super.getAngle();
    }

    public double getAngle(){
        return super.getAngle() - curAngle;
    }

    public double getAbsoluteAngle(){
        return super.getAngle();
    }

    public void resetAbsolute(){
        super.reset();
    }

}
