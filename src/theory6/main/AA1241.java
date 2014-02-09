/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package theory6.main;


import edu.wpi.first.wpilibj.Compressor;
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
    
    int lcdUpdateCycle = 0;
    
    boolean b;
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
        Constants.load();
    }

    public void autonomousInit(){
        compressor.stop();
        ac.clear();

        driveTrain.resetGyro();
        driveTrain.resetEncoders();
        
         switch(autonSwitcher.getSelected()){
             case 0:
                ac = autonSeq.testAutonDriveV1();
                break;
            case 1:
                ac = autonSeq.testAutonDriveV2();
                break;                
            case 2:
                ac = autonSeq.testAutonTurn();
                break;
         }
    }
    
    public void autonomousPeriodic() {
        ac.executeCommands();
        
    }
    public void disabledInit(){
        compressor.stop();
        Constants.load();
    }
            
    public void disabledPeriodic(){
        ac.clear();
        if(toolPad.getRawButton(GamepadConstants.A_BUTTON))
            driveTrain.recalibrateGyro();
        
        if(toolPad.getRawButton(GamepadConstants.B_BUTTON))
            driveTrain.resetEncoders();
        
        if(toolPad.getRawButton(GamepadConstants.Y_BUTTON))
            driveTrain.resetGyro();

        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "R Enc: "
        + driveTrain.getRightEncoderDist());
        
        dsLCD.println(DriverStationLCD.Line.kUser2, 1, "L Enc: "
        + driveTrain.getLeftEncoderDist());
        
        dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Gyro: "
        + driveTrain.getGyroAngle());
        
        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Catapult Pot: "
        + catapult.getWinchPot());
        
//        dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Winch State "
//        + catapult.winchPistonState);
        
//        dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Limit Switch: "
//        + intake.ballDetected());

        if ((lcdUpdateCycle % 50) == 0) {
            dsLCD.updateLCD();
        } 
        else {
            lcdUpdateCycle++;
        }
        
        //autonSwitcher.addDefault("Test", 0);
        autonSwitcher.addInteger("Test-Drive V1", 0); 
        autonSwitcher.addInteger("Test-Drive V2", 1);
        autonSwitcher.addInteger("Test-Turn", 3);
        SmartDashboard.putData("Auton Selecter", autonSwitcher);  
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
        if(drivePad.getRawButton(GamepadConstants.RIGHT_BUMPER))
        {
            driveTrain.tankDrive(-leftAnalogY, rightAnalogY, 1);
        }
        else
        {
            driveTrain.tankDrive(-leftAnalogY, rightAnalogY, 3);
        }
        
        //Intake Code
        intake.intakeBall(intakeJoy, 3);

        intake.setIntakePosTeleop(toolPad.getRawButton(GamepadConstants.LEFT_BUMPER));
        
                
////        if(toolPad.getRawButton(GamepadConstants.A_BUTTON))
////                b = true;
////        else {
////            b = false;
////            catapult.toggleWinchPistonPos(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
////        }

        //catapult.engageWinch(b);
        
        catapult.holdTrussPistonPos(toolPad.getRawButton(GamepadConstants.RIGHT_TRIGGER));
        
        catapult.setWinchState(winchJoy, toolPad.getRawButton(GamepadConstants.B_BUTTON), 
                               toolPad.getRawButton(GamepadConstants.A_BUTTON),
                               toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
        
        catapult.disengageWinch(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
//        if(toolPad.getRawButton(GamepadConstants.B_BUTTON)) {
//            catapult.setWinchPos(Constants.getDouble("bWinchPosOne"), Constants.getDouble("bWinchWindBackSpeed"));
//        }
//        else {
//           catapult.setWinchPWM(winchJoy); 
       // catapult.trussShot(toolPad.getRawButton(GamepadConstants.X_BUTTON));
        
//        if(!(catapult.getWinchPot() == Constants.getDouble("bWinchPosOne"))){
//              
//            catapult.toggleWinchPistonPos(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
//            catapult.setWinchPWM(winchSpeed);
//        
//        }
//        else{
//            
//            catapult.setWinchPWM(0);
//            
//            
//            
//        }
        
            
        
//        if(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER)/* && intake.ballDetected() */)
//            catapult.disengageWinch();

//    }
        
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "R Enc: "
        + driveTrain.getRightEncoderDist());
        
        dsLCD.println(DriverStationLCD.Line.kUser2, 1, "L Enc: "
        + driveTrain.getLeftEncoderDist());
        
        dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Gyro: "
        + driveTrain.getGyroAngle());
        
        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Catapult Pot: "
        + catapult.getWinchPot());
        dsLCD.println(DriverStationLCD.Line.kUser5, 1, "WinchSet:"
        + catapult.winchSetpoint);
        
//        dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Winch State "
//        + catapult.winchPistonState);
        
//        dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Limit Switch: "
//        + intake.ballDetected());
        
        if ((lcdUpdateCycle % 50) == 0) {
            dsLCD.updateLCD();
        } 
        else {
            lcdUpdateCycle++;
        }
     
    }
}
