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
    //Catapult catapult;
    Compressor compressor;
    
    Joystick drivePad;
    Joystick toolPad;
    
    int lcdUpdateCycle = 0;
    
    public void robotInit() 
    {
        compressor = new Compressor(ElectricalConstants.COMPRESSOR_RELAY,
                                    ElectricalConstants.COMPRESSOR_PRESSURE_SENSOR);
      
        dsLCD = DriverStationLCD.getInstance();
        driveTrain = DriveTrain.getInstance();
        intake = Intake.getInstance();
        //catapult = Catapult.getInstance();
        drivePad = new Joystick(GamepadConstants.DRIVE_USB_PORT);
        toolPad = new Joystick(GamepadConstants.TOOL_USB_PORT);
    }


    public void autonomousPeriodic() {

    }
    public void disabledPeriodic(){
 
        compressor.stop();
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "Left Encoder: "
        + driveTrain.getLeftEncoderDist() + "      ");
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "Right Encoder: "
        + driveTrain.getRightEncoderDist() + "      ");
        
        if ((lcdUpdateCycle % 50) == 0) {
            dsLCD.updateLCD();
        } 
        else {
            lcdUpdateCycle++;
        }
    }

    public void teleopPeriodic() 
    {
        compressor.start();
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "Left Encoder: "
        + driveTrain.getLeftEncoderDist() + "      ");
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "Right Encoder: "
        + driveTrain.getRightEncoderDist() + "      ");
        
        if ((lcdUpdateCycle % 50) == 0) {
            dsLCD.updateLCD();
        } 
        else {
            lcdUpdateCycle++;
        }
        
        double leftAnalogY = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
        double rightAnalogY = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
        double intakeSpeed = toolPad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);

        //Drive Code
        if(drivePad.getRawButton(GamepadConstants.RIGHT_BUMPER))
        {
            driveTrain.tankDrive(-leftAnalogY/2, rightAnalogY/2, 3);
        }
        else
        {
            driveTrain.tankDrive(-leftAnalogY, rightAnalogY, 3);
        }
        
        //Intake Code

        intake.intakeBall(intakeSpeed, 3);
            
            
        intake.setIntakePosition(toolPad.getRawButton(GamepadConstants.LEFT_BUMPER));
        
        if(toolPad.getRawButton(GamepadConstants.RIGHT_TRIGGER))
        {
            
        }
        

    }
    
    public void testPeriodic() {
    
    }   
}
