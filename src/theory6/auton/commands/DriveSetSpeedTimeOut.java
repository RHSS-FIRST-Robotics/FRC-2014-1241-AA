/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import theory6.subsystems.DriveTrain;
import theory6.auton.AutonCommand;
/**
 *
 * @author Sagar
 */
public class DriveSetSpeedTimeOut implements AutonCommand{
    Timer t = new Timer();
    DriveTrain drivetrain;
    double pwmVal;
    double timeOutInSecs;
    
    public DriveSetSpeedTimeOut(double pwm, double timeOut){
        this.pwmVal = pwm;
        this.timeOutInSecs = timeOut;
        drivetrain = DriveTrain.getInstance();
    }
        
    public void init() {
        t.reset();
        t.start();
    }

    public boolean run() {
        drivetrain.setLeftSpeed(pwmVal);
        drivetrain.setRightSpeed(pwmVal);
        
        return t.get() > timeOutInSecs;
    }

    public void done() {
        drivetrain.setLeftSpeed(0);
        drivetrain.setRightSpeed(0);
    }  
}
