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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import theory6.auton.AutonController;
import theory6.subsystems.Catapult;
import theory6.subsystems.DriveTrain;
import theory6.subsystems.Intake;
import theory6.utilities.CheesyVisionServer;
import theory6.utilities.Constants;
import theory6.utilities.SendableChooser;
import theory6.utilities.ToggleBoolean;

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
    
    ToggleBoolean shootDelay = new ToggleBoolean();
    Timer settlerTimer;
    
    Timer hotDetectTimer;
        
    CheesyVisionServer cvServer = CheesyVisionServer.getInstance();
    public final int listenPort = 1180;
    
    final int LEFT_HOT = 0;
    final int RIGHT_HOT = 1;
    final int DID_NOT_DETECT = 2;
    
    int hotGoal = -1;
    
    boolean RIGHT = false;
    boolean LEFT = true;

    boolean autoSide = RIGHT;
    
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
        
        settlerTimer = new Timer();
        settlerTimer.stop();
        
        hotDetectTimer = new Timer();
        //hotDetectTimer.reset();
        hotDetectTimer.stop();
        
        cvServer.setPort(listenPort);
        cvServer.start();
        Constants.getInstance();
        
        SmartDashboard.putBoolean("Left Hot", cvServer.getLeftStatus());
        SmartDashboard.putBoolean("Right Hot", cvServer.getRightStatus());
        SmartDashboard.putString("Registered", "None");
        SmartDashboard.putString("Hands", "Down");
    }

    public void autonomousInit(){
        compressor.stop();
        
        cvServer.reset();
        cvServer.startSamplingCounts();

        ac.clear();
        catapult.firstRun = true;

        driveTrain.resetGyro();
        driveTrain.resetEncoders();
        catapult.ballHolder.set(DoubleSolenoid.Value.kReverse);
        
        SmartDashboard.putBoolean("Left Hot", cvServer.getLeftStatus());
        SmartDashboard.putBoolean("Right Hot", cvServer.getRightStatus());
        SmartDashboard.putString("Registered", "None");
        SmartDashboard.putString("Hands", "Up");
         
        switch(autonSwitcher.getSelected()){
            case 0:
               
                hotDetectTimer.start();
                while (!cvServer.getLeftStatus() && !cvServer.getRightStatus()) {
                    if(hotDetectTimer.get() > Constants.getDouble("hotDetectTimeout"))
                        break;
                }
                
                if(cvServer.getLeftStatus() && !cvServer.getRightStatus()) {
                     hotGoal = LEFT_HOT;
                     SmartDashboard.putBoolean("Left Hot", true);
                     SmartDashboard.putBoolean("Right Hot", false);
                     SmartDashboard.putString("Registered", "Left");
                 }
                 else if(!cvServer.getLeftStatus() && cvServer.getRightStatus()){
                     hotGoal = RIGHT_HOT;
                     SmartDashboard.putBoolean("Left Hot", false);
                     SmartDashboard.putBoolean("Right Hot", true);
                     SmartDashboard.putString("Registered", "Right");
                 }
                 else {
                     hotGoal = DID_NOT_DETECT;
                     SmartDashboard.putBoolean("Left Hot", false);
                     SmartDashboard.putBoolean("Right Hot", false);
                     SmartDashboard.putString("Registered", "DNR");
                 }
                
                SmartDashboard.putString("Hands", "Down");
                
                ac = autonSeq.oneBallHot(autoSide, hotGoal);
                break;                
            case 1:
                ac = autonSeq.twoBall();
                break;
            case 2:
                ac = autonSeq.testDrive();
                break;
            case 3:
                ac = autonSeq.testTurn();
                break;
            case 4:
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
        cvServer.stopSamplingCounts();
        //log("Entered disabledInit... reloading constants...");
        compressor.stop();
        Constants.load();
    }
            
    public void disabledPeriodic(){
        ac.clear();

        
        if(toolPad.getRawButton(GamepadConstants.B_BUTTON)){
            driveTrain.resetEncoders();
            //log("Reset Encoders");
        }
        else if(toolPad.getRawButton(GamepadConstants.Y_BUTTON)){
            driveTrain.resetGyro();
            //log("Reset Gyro");
        }
        else if (toolPad.getRawButton(GamepadConstants.START_BUTTON)){
            Constants.load();
            //log("Reloading Constants in disabled periodic");
        }
        else if((toolPad.getRawAxis(GamepadConstants.DPAD_Y) == -1)
                && toolPad.getRawButton(GamepadConstants.A_BUTTON) 
                && toolPad.getRawButton(GamepadConstants.START_BUTTON)
                && toolPad.getRawButton(GamepadConstants.BACK_BUTTON)){
            //log("About to recalibrate gyro");
            driveTrain.recalibrateGyro();
            driveTrain.resetGyro();
            //log("Finished recalibrating Gyro");
        }
        
        if (toolPad.getRawAxis(GamepadConstants.DPAD_X) == -1)
            autoSide = RIGHT;
        else if (toolPad.getRawAxis(GamepadConstants.DPAD_X) == 1)
            autoSide = LEFT;

        autonSwitcher.addDefault("One Ball Hot", 0);
      
        autonSwitcher.addInteger("Two Ball", 1);
        autonSwitcher.addInteger("Test-Drive", 2);
        autonSwitcher.addInteger("Test-Turn", 3);
        autonSwitcher.addInteger("Test", 4);
        
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
        
        //Winch code
        catapult.windWinch(winchJoy, toolPad.getRawButton(GamepadConstants.B_BUTTON), //preset one
                                     toolPad.getRawButton(GamepadConstants.A_BUTTON)); //preset two
               
        
        //catapult.engageWinch(toolPad.getRawButton(GamepadConstants.RIGHT_TRIGGER));
        //catapult.setWinchPWM(winchJoy);
        
        //Ball Holder
        shootDelay.set(toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
                
        if(shootDelay.get()){
            settlerTimer.start();
            settlerTimer.reset();   
        }
        
        if(settlerTimer.get() > Constants.getDouble("ShooterWaitTime")) {
            catapult.disengageWinch(true);
            settlerTimer.reset();
            settlerTimer.stop();
        }
        
        catapult.holdBallSettler(toolPad.getRawButton(GamepadConstants.LEFT_TRIGGER), 
                                 toolPad.getRawButton(GamepadConstants.RIGHT_BUMPER));
        
        //DSLCD and SmartDashboard Output
        updateDSLCD();
        updateSmartDashboard();
        
    }
    
    private void updateSmartDashboard() {
        
        //SmartDashboard.putString("Left Target", SmartDashboard.getString("Left Target", "No Connection"));
        //SmartDashboard.putString("Right Target", SmartDashboard.getString("Right Target", "No Connection"));
        SmartDashboard.putString("Registered", "None");
        SmartDashboard.putString("Hands", "Down");
        SmartDashboard.putString("Auto-Side", autoSide ? "Left" : "Right");
        SmartDashboard.putBoolean("CV Left", cvServer.getLeftStatus());
        SmartDashboard.putBoolean("CV Right", cvServer.getRightStatus());
    }
    
    private void updateDSLCD() {
        
        dsLCD.println(DriverStationLCD.Line.kUser1, 1, "L: "
        + (Math.floor(driveTrain.getLeftEncoderDist()*10) / 10.0) + " R: " 
        + (Math.floor(driveTrain.getRightEncoderDist()*10) / 10.0));
        
        dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Gyro: "
        + Math.floor(driveTrain.getGyroAngle() * 100) / 100);
        
        dsLCD.println(DriverStationLCD.Line.kUser3, 1,  "?:" + (catapult.winchOnTarget() ? 1 : 0) + 
                                                       " Winch Pot: " + catapult.getWinchPot());

        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "WEngaged?: " + (catapult.isEngaged() ? 1 : 0));
                
        dsLCD.println(DriverStationLCD.Line.kUser5, 1, "CLimit: " + catapult.getLS());
        
        dsLCD.println(DriverStationLCD.Line.kUser6, 1, "CVL:" + (cvServer.getLeftStatus() ? 1 : 0) + " " + "CVR:" + (cvServer.getRightStatus() ? 1 : 0));

        if ((lcdUpdateCycle % 50) == 0) {
            dsLCD.updateLCD();
        } 
        else {
            lcdUpdateCycle++;
        }
    }
    
    private void updateJavaSystemOut() {
        log("Current left: " + cvServer.getLeftStatus() + ", current right: " + cvServer.getRightStatus());
        log("Left count: " + cvServer.getLeftCount() + ", right count: " + cvServer.getRightCount() + ", total: " + cvServer.getTotalCount() + "\n");
    }
    
    private void log(Object aObject){
        System.out.println(String.valueOf(aObject));
    }
}
