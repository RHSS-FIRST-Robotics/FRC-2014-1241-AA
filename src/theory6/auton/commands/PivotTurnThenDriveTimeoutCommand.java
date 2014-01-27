/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import theory6.auton.AutonCommand;
import theory6.subsystems.DriveTrain;
import theory6.pid.PIDController;
import theory6.utilities.Constants;

import theory6.utilities.MathLogic;

/**
 *
 * @author Sagar and Het
 */
public class PivotTurnThenDriveTimeoutCommand implements AutonCommand {

    double gyroGain = 0;
    
    double firstPivotAngle = 0;
    double secondPivotAngle = 0;
    double straightDistGoal = 0;
    
    double straightDistAngle = 0;
    
    double timeout = 0.0;
    Timer timeOutTimer = new Timer();
    
    double prevTime = 0;
    double prevAvgDist = 0;
    
    PIDController drivePID;

    double shortP = 0;
    double shortI = 0;
    double shortD = 0;
    
    double longP = 0;
    double longI = 0;
    double longD = 0;
    
    DriveTrain driveTrain;
    PIDController gyroPID;

    Timer driveTimer = new Timer();
  
    double firstPivSpeed;
    
    int state = 0;
    
    boolean doneMotion = false;
    
    public static final int FIRST_PIVOT_ANGLE = 0;
    public static final int STRAIGHT_DIST = 1;
    public static final int FINISHED_SEQUENCE = 2;

    
    public PivotTurnThenDriveTimeoutCommand(double firstPivotAngle, double straightDist, double timeOutSecs) {
        Constants.getInstance();
        this.firstPivotAngle= firstPivotAngle;
        this.straightDistGoal = straightDist;
        this.secondPivotAngle = 0;
        this.timeout = timeOutSecs;
        
        this.firstPivSpeed = Constants.getDouble("pivotTurnSpeed");
        
        this.gyroGain = Constants.getDouble("gyroGainPivAndDrive");
        
        drivePID = new PIDController(Constants.getDouble("driveLongV2P"), 
                                     Constants.getDouble("driveLongV2I"), 
                                     Constants.getDouble("driveLongV2D"));

        
        longP = Constants.getDouble("driveLongV2P");
        longI = Constants.getDouble("driveLongV2I");
        longD = Constants.getDouble("driveLongV2D");
        
        shortP = Constants.getDouble("driveShortV2P");
        shortI = Constants.getDouble("driveShortV2I");
        shortD = Constants.getDouble("driveShortV2D");
        
        if (Math.abs(straightDistGoal) <= 30){
            drivePID.changePIDGains(shortP, shortI, shortD);
        }
        else if (Math.abs(straightDistGoal) > 30) {
            drivePID.changePIDGains(longP, longI, longD);
        }
        
        driveTrain = DriveTrain.getInstance();
    }
    
    public void init() {
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        
        timeOutTimer.reset();
        timeOutTimer.start();
        
        driveTimer.reset();
        driveTimer.start();
    }

    public boolean run() {
        
        switch (state){
            
        case FIRST_PIVOT_ANGLE:
            //double error = firstPivotAngle - driveTrain.getGyroAngle();
            //double speed = maxPWMRate * gyroPID.calcPID(firstPivotAngle, driveTrain.getGyroAngle());
            
            double speed = firstPivSpeed;
            
            if (firstPivotAngle >= 0){
                driveTrain.setLeftSpeed(0);
                driveTrain.setRightSpeed(-speed);
            }
            else if (firstPivotAngle < 0) {
                driveTrain.setLeftSpeed(-speed);
                driveTrain.setRightSpeed(0);
            }
        
            //if(Math.abs(error - lastError) < speedDeadband && Math.abs(error) < angleDeadband) {
            if(Math.abs(driveTrain.getGyroAngle()) >= Math.abs(firstPivotAngle)) {
                driveTrain.setLeftSpeed(0);
                driveTrain.setRightSpeed(0);
                
                straightDistAngle = firstPivotAngle - driveTrain.getGyroAngle();
                driveTrain.resetGyro();
                driveTrain.resetEncoders();
                
                state = STRAIGHT_DIST;
            }
            
            break;
        case STRAIGHT_DIST:
            double currLeftDist = driveTrain.getLeftEncoderDist();
            double currRightDist = driveTrain.getRightEncoderDist();

            double currAvgDist = (currLeftDist + currRightDist) / 2;
            double currTime = driveTimer.get();
            double driveVel = currAvgDist/(currTime - prevTime);

            prevTime = currTime;
            prevAvgDist = currAvgDist;

            double drivePIDOutput = MathLogic.limitAbs(drivePID.calcPID(straightDistGoal, currAvgDist), 1);

            double angleDiff = driveTrain.getGyroAngle() - straightDistAngle; 
            double straightGain = angleDiff * gyroGain;

            double leftPwr = drivePIDOutput - straightGain;
            double rightPwr = drivePIDOutput + straightGain;

            leftPwr -= straightGain;
            rightPwr += straightGain;

            driveTrain.setLeftSpeed(MathLogic.PWMLimit(leftPwr));
            driveTrain.setRightSpeed(MathLogic.PWMLimit(rightPwr));
        
            if((Math.abs(currAvgDist - straightDistGoal) < 3) /*&& Math.abs(driveVel) < 6*/) {
                driveTrain.setLeftSpeed(0);
                driveTrain.setRightSpeed(0);
               
                state = FINISHED_SEQUENCE;
            }
                break;
           case FINISHED_SEQUENCE:
               
                doneMotion = true;
                
                break;
        }
        
        return doneMotion == true || timeOutTimer.get() > timeout;
    }
    
    public void done() {
        driveTrain.setLeftSpeed(0);
        driveTrain.setRightSpeed(0);
    }
    
}
