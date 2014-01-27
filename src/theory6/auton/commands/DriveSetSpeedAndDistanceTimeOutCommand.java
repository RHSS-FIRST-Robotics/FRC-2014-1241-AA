/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import theory6.subsystems.DriveTrain;
import theory6.auton.AutonCommand;
import theory6.utilities.Constants;
/**
 *
 * @author Sagar
 */
public class DriveSetSpeedAndDistanceTimeOutCommand implements AutonCommand{
    Timer t = new Timer();
    DriveTrain drivetrain;
    double pwmVal;
    double timeOutInSecs;
    double encoderTicks;
    double gyroGain;
    
    public DriveSetSpeedAndDistanceTimeOutCommand(double pwm, double ticks, double timeOut){
        this.pwmVal = pwm;
        this.timeOutInSecs = timeOut;
        this.encoderTicks = ticks;
        Constants.getInstance();
        this.gyroGain = Constants.getDouble("gyroGainMisc");
        drivetrain = DriveTrain.getInstance();        
    }
        
    public void init() {
        t.reset();
        t.start();
        drivetrain.resetEncoders();
        drivetrain.resetGyro();
    }

    public boolean run() {
        drivetrain.setLeftSpeed(pwmVal - (drivetrain.getGyroAngle() * gyroGain));
        drivetrain.setRightSpeed(pwmVal  + (drivetrain.getGyroAngle() * gyroGain));
        
        return (Math.abs(drivetrain.getLeftEncoderDist()) > Math.abs(encoderTicks) || Math.abs(drivetrain.getRightEncoderDist()) > Math.abs(encoderTicks)) ||t.get() > timeOutInSecs;
    }

    public void done() {
        drivetrain.setLeftSpeed(0);
        drivetrain.setRightSpeed(0);
    }  
}

