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
public class DriveToPosV1TimeOutCommand implements AutonCommand {
    
    double angleGoal = 0;
    double distGoal = 0;
    double gyroGain = 0;
    double initialGyroAngle;
    
    double timeout = 0.0;
    Timer timeOutTimer = new Timer();
    
    double prevTime = 0;
    double prevLeftDist = 0;
    double prevRightDist = 0;
    
    double power = 0;
    
    PIDController leftDrivePID;
    PIDController rightDrivePID;

    double shortP = 0;
    double shortI = 0;
    double shortD = 0;
    
    double longP = 0;
    double longI = 0;
    double longD = 0;
    
    DriveTrain driveTrain;
    
    Timer driveTimer = new Timer();
    
    public DriveToPosV1TimeOutCommand(double distance, double angle, double timeOutSecs) {
        Constants.getInstance();
        this.angleGoal = -angle;
        this.distGoal = distance;
        this.gyroGain = Constants.getDouble("gyroGainV1");
        this.timeout = timeOutSecs;
        
        leftDrivePID = new PIDController(Constants.getDouble("driveLongV1P"), 
                                         Constants.getDouble("driveLongV1I"), 
                                         Constants.getDouble("driveLongV1D"));
        
        rightDrivePID = new PIDController(Constants.getDouble("driveLongV1P"), 
                                          Constants.getDouble("driveLongV1I"), 
                                          Constants.getDouble("driveLongV1D"));
        
        longP = Constants.getDouble("driveLongV1P");
        longI = Constants.getDouble("driveLongV1I");
        longD = Constants.getDouble("driveLongV1D");
        
        shortP = Constants.getDouble("driveShortV1P");
        shortI = Constants.getDouble("driveShortV1I");
        shortD = Constants.getDouble("driveShortV1D");
        
        if (Math.abs(distGoal) <= 30){
            leftDrivePID.changePIDGains(shortP, shortI, shortD);
            rightDrivePID.changePIDGains(shortP, shortI, shortD);
        }
        else if (Math.abs(distGoal) > 30) {
            leftDrivePID.changePIDGains(longP, longI, longD);
            rightDrivePID.changePIDGains(longP, longI, longD);
        }
        
        driveTrain = DriveTrain.getInstance();
    }
    
    public void init() {
        driveTrain.resetEncoders();
        //driveTrain.resetGyro(); //figure out this logic for Hershey!
        initialGyroAngle = driveTrain.getGyroAngle();
        timeOutTimer.reset();
        timeOutTimer.start();
        driveTimer.reset();
        driveTimer.start();
    }

    public boolean run() {
        
        double currLeftDist = driveTrain.getLeftEncoderDist();
        double currRightDist = driveTrain.getRightEncoderDist();
        double currTime = driveTimer.get();
        double lVel = (currLeftDist - prevLeftDist)/(currTime - prevTime);
        double rVel = (currRightDist - prevRightDist)/(currTime - prevTime);
        prevTime = currTime;
        prevLeftDist = currLeftDist;
        prevRightDist = currRightDist;

        double leftPIDOutput = MathLogic.limitAbs(leftDrivePID.calcPID(distGoal, currLeftDist), 1);
        double rightPIDOutput = MathLogic.limitAbs(rightDrivePID.calcPID(distGoal, currRightDist), 1);

        double angleDiff = driveTrain.getGyroAngle() - (angleGoal + initialGyroAngle); 
        double straightGain = angleDiff * gyroGain;
        
        double leftPwr = leftPIDOutput - straightGain;
        double rightPwr = rightPIDOutput + straightGain;

        leftPwr -= straightGain;
        rightPwr += straightGain;
         
        driveTrain.setLeftPWM(MathLogic.PWMLimit(leftPwr));
        driveTrain.setRightPWM(MathLogic.PWMLimit(rightPwr));
        
        return ((Math.abs(currLeftDist - distGoal) < 2 || Math.abs(currRightDist- distGoal) < 2) 
                && (Math.abs(lVel) < 6 || Math.abs(rVel) < 6)) || timeOutTimer.get() > timeout;
    }
    
    public void done() {
        driveTrain.setLeftPWM(0);
        driveTrain.setRightPWM(0);
    }
    
}
