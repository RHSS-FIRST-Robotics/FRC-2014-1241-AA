package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import theory6.subsystems.Intake;
import theory6.subsystems.DriveTrain;
import theory6.auton.AutonCommand;
import theory6.utilities.Constants;




/**
 *
 * @author Controls
 */
public class DriveForwardIntakeBallTimeOutCommand implements AutonCommand{
    
    Timer t = new Timer();
    
    DriveTrain drivetrain;
    Intake intake;
    
    double encoderTicks;
    double timeOutInSecs;
    
    
    
    
    
    public DriveForwardIntakeBallTimeOutCommand(double ticks, double timeOut){
        Constants.getInstance();
        DriveTrain.getInstance();
        Intake.getInstance();
        
        
        this.encoderTicks = ticks;
        this.timeOutInSecs = timeOut;
        
        
        
        
        
    }
    public void init()
    {
        t.reset();
        t.start();
        drivetrain.resetEncoders();
        drivetrain.resetGyro();
        
    }
    public boolean run(){
        
//        drivetrain.setLeftSpeed(Constants.getDouble(DriveForwardIntakeBallSpeed));
//        drivetrain.setRightSpeed(Constants.getDouble(DriveForwardIntakeBallSpeed));
//        
//        intake.setIntakePosition(true);
//        intake.intakeBall(Constants.getDouble(IntakeBallSpeed), 3);
        
        
        
        return (Math.abs(drivetrain.getLeftEncoderDist()) > Math.abs(encoderTicks) || 
                Math.abs(drivetrain.getRightEncoderDist()) > Math.abs(encoderTicks)) ||
                t.get() > timeOutInSecs;
    }
    public void done(){
        
        drivetrain.setLeftSpeed(0);
        drivetrain.setRightSpeed(0);
        
    }
    
    
    
}
