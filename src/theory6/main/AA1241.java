/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package theory6.main;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import theory6.auton.AutonController;
import theory6.subsystems.Catapult;
import theory6.subsystems.DriveTrain;
import theory6.subsystems.Intake;
import theory6.utilities.Constants;
import theory6.utilities.SendableChooser;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class AA1241 extends IterativeRobot {
    
    DriverStationLCD dsLCD;
    DriveTrain driveTrain;
    Intake intake;
    Catapult catapult;
    Compressor compressor;
    
    Joystick drivePad;
    Joystick toolPad;
    
    double lcdUpdateCycle = 0;
    
    SendableChooser autonSwitcher;
    AutonController ac = new AutonController();
    AutonSequences autonSeq = new AutonSequences();

    public void robotInit() 
    {
        compressor = new Compressor(ElectricalConstants.COMPRESSOR_PRESSURE_SENSOR,
                                    ElectricalConstants.COMPRESSOR_RELAY);
      
        dsLCD = DriverStationLCD.getInstance();
        driveTrain = DriveTrain.getInstance();
        intake = Intake.getInstance();
        catapult = Catapult.getInstance();
        drivePad = new Joystick(GamepadConstants.DRIVE_USB_PORT);
        toolPad = new Joystick(GamepadConstants.TOOL_USB_PORT);
        autonSwitcher = new SendableChooser();
        
        Constants.getInstance();
        //Constants.load();
    }

    public void autonomousInit(){
        compressor.stop();

        ac.clear();
        catapult.firstRun = true;

        driveTrain.resetGyro();
        driveTrain.resetEncoders();
        
         switch(autonSwitcher.getSelected()){
             case 0:
                ac = autonSeq.oneBallDriveForward();
                break;
            case 1:
                ac = autonSeq.driveForwardOneBall();
                break;                
            case 2:
                ac = autonSeq.twoBallDriveForwardV1GTREast();
                break;
            case 3:
                ac = autonSeq.twoBallDriveForwardV2();
                break;
            case 4:
                ac = autonSeq.twoBallDriveForwardV3();
                break;
            case 5:
                ac = autonSeq.twoBallHotDriveForward();
                break;
            case 6:
                ac = autonSeq.testDrive();
                break;
            case 7:
                ac = autonSeq.testTurn();
                break;
            case 8:
                ac = autonSeq.test();
                break;
         }
    }
    
    public void autonomousPeriodic() {
        ac.executeCommands();
        updateDSLCD();
        updateSmartDashboard();
    }
    public void disabledInit(){
        log("Entered disabledInit... reloading constants...");
        compressor.stop();
        Constants.load();
    }
            
    public void disabledPeriodic(){
        ac.clear();
        /*if(toolPad.getRawButton(GamepadConstants.A_BUTTON)){
            log("About to recalibrate gyro");
            driveTrain.recalibrateGyro();
            driveTrain.resetGyro();
            log("Finished recalibrating Gyro");
        }*/
        if(toolPad.getRawButton(GamepadConstants.B_BUTTON)){
            driveTrain.resetEncoders();
            log("Reset Encoders");
        }
        if(toolPad.getRawButton(GamepadConstants.Y_BUTTON)){
            driveTrain.resetGyro();
            log("Reset Gyro");
        }
        if (toolPad.getRawButton(GamepadConstants.START_BUTTON)){
            Constants.load();
            log("Reloading Constants in disabled periodic");
        }

        //autonSwitcher.addDefault("Test", 0);
        autonSwitcher.addInteger("One Ball - Drive Forward", 0); 
        autonSwitcher.addInteger("Drive Forward - One Ball", 1);
        autonSwitcher.addInteger("Two Ball V1 GTR East", 2);
        autonSwitcher.addInteger("Two Ball V2", 3);
        autonSwitcher.addInteger("Two Ball V3", 4);
        autonSwitcher.addInteger("Two Ball Hot", 5);
        autonSwitcher.addInteger("Test-Drive", 6);
        autonSwitcher.addInteger("Test-Turn", 7);
        autonSwitcher.addInteger("Test", 8);
        
        SmartDashboard.putData("Autonomous Mode", autonSwitcher);  
        
        updateDSLCD();
        updateSmartDashboard();
    }
    
    public void teleopInit(){
        ac.clear();
        compressor.start();
    }

    public void teleopPeriodic() 
    {

        double leftAnalogY = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
        double rightAnalogY = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
        double intakeJoy = toolPad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
        double winchJoy = toolPad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);

        //Drive Code
        if(drivePad.getRawButton(GamepadConstants.RIGHT_BUMPER)) //rightBumper is half speed button for driver!
            driveTrain.tankDrive(-leftAnalogY/2, rightAnalogY/2, 1);
        else
            driveTrain.tankDrive(-leftAnalogY, rightAnalogY, 3);

        //Intake Code
        intake.intakeBall(intakeJoy, 3);
        intake.setIntakePosTeleop(toolPad.getRawButton(GamepadConstants.LEFT_BUMPER));
        
        //Ball Holder
        //catapult.toggleBallSettler(toolPad.getRawButton(GamepadConstants.LEFT_TRIGGER));
        catapult.autoBallSettler(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER), 
                                 toolPad.getRawButton(GamepadConstants.LEFT_BUMPER), 
                                 toolPad.getRawButton(GamepadConstants.START_BUTTON));
        
                
        //Winch code
        catapult.windWinch(winchJoy, toolPad.getRawButton(GamepadConstants.B_BUTTON), //preset one
                                     toolPad.getRawButton(GamepadConstants.A_BUTTON)); //preset two
        
        catapult.disengageWinch(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
       
        //catapult.engageWinch(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
        //catapult.setWinchPWM(winchJoy);
        
        updateDSLCD();
        updateSmartDashboard();
        
    }
    
    private void updateSmartDashboard() {
        
        SmartDashboard.putString("Left Target", SmartDashboard.getString("Left Target", "No Connection"));
        SmartDashboard.putString("Right Target", SmartDashboard.getString("Right Target", "No Connection"));
    }
    
    private void updateDSLCD() {
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "L: "
        + (Math.floor(driveTrain.getLeftEncoderDist()*10) / 10.0) + " R: " 
        + (Math.floor(driveTrain.getRightEncoderDist()*10) / 10.0));
        
        dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Gyro: "
        + Math.floor(driveTrain.getGyroAngle() * 100) / 100);
        
        dsLCD.println(DriverStationLCD.Line.kUser3, 1,  "?:" + (catapult.winchOnTarget() ? 1 : 0) + 
                                                       " Winch Pot: " + catapult.getWinchPot());
        
        //dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Limit Switch: " + catapult.getLimitSwitch());
        
        dsLCD.println(DriverStationLCD.Line.kUser5, 1, "WEngaged?: " + (catapult.isEngaged() ? 1 : 0));

        if ((lcdUpdateCycle % 50) == 0) {
            dsLCD.updateLCD();
        } 
        else {
            lcdUpdateCycle++;
        }
     
    }
    private void log(Object aObject){
        
        System.out.println(String.valueOf(aObject));
        
    }
}
