/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import theory6.auton.AutonCommand;
import theory6.subsystems.DriveTrain;
import theory6.pid.PIDController;
import theory6.utilities.Constants;

import theory6.utilities.MathLogic;

/**
 *
 * @author Sagar and Het
 */
public class DriveToPosV2TimeOutCommand implements AutonCommand {
    
    double angleGoal = 0;
    double distGoal = 0;
    double gyroGain = 0;
    double initialGyroAngle;
    
    double timeout = 0.0;
    Timer timeOutTimer = new Timer();
    Timer driveTimer = new Timer();
   
    PIDController drivePID;
    double shortP = 0;
    double shortI = 0;
    double shortD = 0;
    double longP = 0;
    double longI = 0;
    double longD = 0;
    
    double prevTime = 0;
    double prevAvgDist = 0;
    
    DriveTrain driveTrain;
    
    public DriveToPosV2TimeOutCommand(double distance, double angle, double timeOutSecs) {
        Constants.getInstance();
        this.angleGoal = -angle;
        this.distGoal = distance;
        this.gyroGain = Constants.getDouble("gyroGainV2");
        this.timeout = timeOutSecs;

        drivePID = new PIDController(Constants.getDouble("driveLongV2P"), 
                                     Constants.getDouble("driveLongV2I"), 
                                     Constants.getDouble("driveLongV2D"));

        
        longP = Constants.getDouble("driveLongV2P");
        longI = Constants.getDouble("driveLongV2I");
        longD = Constants.getDouble("driveLongV2D");
        
        shortP = Constants.getDouble("driveShortV2P");
        shortI = Constants.getDouble("driveShortV2I");
        shortD = Constants.getDouble("driveShortV2D");
        
        if (Math.abs(distGoal) <= 30){
            drivePID.changePIDGains(shortP, shortI, shortD);
        }
        else if (Math.abs(distGoal) > 30) {
            drivePID.changePIDGains(longP, longI, longD);
        }
        
        driveTrain = DriveTrain.getInstance();
    }
    
    public void init() {
        driveTrain.resetEncoders();
        initialGyroAngle = driveTrain.getGyroAngle();
        
        timeOutTimer.reset();
        timeOutTimer.start();
        
        driveTimer.reset();
        driveTimer.start();
    }

    public boolean run() {
        
        double currLeftDist = driveTrain.getLeftEncoderDist();
        double currRightDist = driveTrain.getRightEncoderDist();
        
        double currAvgDist = (currLeftDist + currRightDist) / 2;
        double currTime = driveTimer.get();
        //double driveVel = currAvgDist/(currTime - prevTime);

        prevTime = currTime;
        prevAvgDist = currAvgDist;

        double drivePIDOutput = MathLogic.limitAbs(drivePID.calcPID(distGoal, currAvgDist), 1);

        double angleDiff = driveTrain.getGyroAngle() - (angleGoal + initialGyroAngle); 
        double straightGain = angleDiff * gyroGain;
        
        double leftPwr = drivePIDOutput - straightGain;
        double rightPwr = drivePIDOutput + straightGain;
        
        leftPwr -= straightGain;
        rightPwr += straightGain;
        
        System.out.println(leftPwr);
        driveTrain.setLeftPWM(MathLogic.PWMLimit(leftPwr));
        driveTrain.setRightPWM(-MathLogic.PWMLimit(rightPwr));

        return (Math.abs(currAvgDist - distGoal) < 3) /*&& (Math.abs(driveVel) < 6)*/ || timeOutTimer.get() > timeout;
    }
    
    public void done() {
        driveTrain.setLeftPWM(0);
        driveTrain.setRightPWM(0);
    }
    
}
