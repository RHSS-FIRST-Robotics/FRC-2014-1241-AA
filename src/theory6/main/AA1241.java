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
import theory6.subsystems.Catapult;
import theory6.subsystems.DriveTrain;
import theory6.subsystems.Intake;
import theory6.utilities.Constants;

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
    
    public void robotInit() 
    {
        compressor = new Compressor(ElectricalConstants.COMPRESSOR_PRESSURE_SENSOR,ElectricalConstants.COMPRESSOR_RELAY);
      
        dsLCD = DriverStationLCD.getInstance();
        driveTrain = DriveTrain.getInstance();
        intake = Intake.getInstance();
        catapult = Catapult.getInstance();
        drivePad = new Joystick(GamepadConstants.DRIVE_USB_PORT);
        toolPad = new Joystick(GamepadConstants.TOOL_USB_PORT);
        
        Constants.getInstance();
        Constants.load();
        
        
    }

    public void autonomousInit(){
        
    }
    
    public void autonomousPeriodic() {

    }
    public void disabledInit(){
        Constants.load();
    }
            
    public void disabledPeriodic(){
 
        if(toolPad.getRawButton(GamepadConstants.A_BUTTON))
            driveTrain.recalibrateGyro();
        
        if(toolPad.getRawButton(GamepadConstants.B_BUTTON))
            driveTrain.resetEncoders();
        
        if(toolPad.getRawButton(GamepadConstants.Y_BUTTON))
            driveTrain.resetGyro();

        compressor.stop();
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "R Enc: "
        + driveTrain.getRightEncoderDist());
        
        dsLCD.println(DriverStationLCD.Line.kUser2, 1, "L Enc: "
        + driveTrain.getLeftEncoderDist());
        
        dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Gyro: "
        + driveTrain.getGyroAngle());
        
        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Catapult Pot: "
        + catapult.getWinchPot());
        
//        dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Catapult Pot: "
//        + catapult.getWinchPot());
        
//        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Limit Switch: "
//        + intake.ballDetected());
        
        
        
        
        if ((lcdUpdateCycle % 50) == 0) {
            dsLCD.updateLCD();
        } 
        else {
            lcdUpdateCycle++;
        }
    }
    
    public void teleopInit(){
        
    }

    public void teleopPeriodic() 
    {
        compressor.start();
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "R Enc: "
        + driveTrain.getRightEncoderDist());
        
        dsLCD.println(DriverStationLCD.Line.kUser2, 1, "L Enc: "
        + driveTrain.getLeftEncoderDist());
        
        dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Gyro: "
        + driveTrain.getGyroAngle());
        
        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Catapult Pot: "
        + catapult.getWinchPot());
        
//        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Limit Switch: "
//        + intake.ballDetected());
        
        if ((lcdUpdateCycle % 50) == 0) {
            dsLCD.updateLCD();
        } 
        else {
            lcdUpdateCycle++;
        }
        
        double leftAnalogY = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
        double rightAnalogY = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
        double intakeSpeed = toolPad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
        double winchSpeed = toolPad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);

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

        intake.intakeBall(intakeSpeed, 3);
            
            
        intake.setIntakePosition(toolPad.getRawButton(GamepadConstants.LEFT_BUMPER));

        catapult.toggleWinchPistonPos(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
        
        if(!(catapult.getWinchPot() == Constants.getDouble("bWinchPosOne"))){
              
            catapult.toggleWinchPistonPos(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
            catapult.setWinchPWM(winchSpeed);
        
        }
        else{
            
            catapult.setWinchPWM(0);
            
            
            
        }
            
        
//        if(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER)/* && intake.ballDetected() */)
//            catapult.disengageWinch();

    }
     
}
