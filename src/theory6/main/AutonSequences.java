/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.main;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import theory6.subsystems.DriveTrain;
import theory6.auton.*;
import theory6.auton.commands.*;
import theory6.utilities.CheesyVisionServer;

import theory6.utilities.Constants;

                                                                                                                
/**
 *
 * @author Sagar
 */
public class AutonSequences {
    
    final int LEFT_HOT = 0;
    final int RIGHT_HOT = 1;
    final int DID_NOT_DETECT = 2;
    
    public AutonController oneBallHot(boolean isLeftSideAuto, int hotGoal) { //Starts right side
        AutonController ac = new AutonController();
        ac.clear();
        Constants.getInstance();
        
        ac.addCommand(new WindBackWinchTimeOutCommand(Constants.getDouble("OBWinchSetpoint"), 
                                                      Constants.getDouble("OBWindTimeout")));
        
        ac.addCommand(new SetBallSettlerCommand());
        
        switch(hotGoal) {
            case RIGHT_HOT:
                ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("OBDriveForwardDist"), 
                                                           Constants.getDouble("OBDriveAngle"), 
                                                           Constants.getDouble("OBDriveDistTimeout"))); 
                
                if (isLeftSideAuto) //if we're on the left side make sure we wait for hot goal!
                    ac.addCommand(new WaitCommand(Constants.getDouble("OBWaitForHot")));
                                
                ac.addCommand(new SetBallSettlerCommand());
                ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("winchDisengageTimeout")));
                break;
            
            case LEFT_HOT:
                ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("OBDriveForwardDist"), 
                                                           Constants.getDouble("OBDriveAngle"), 
                                                           Constants.getDouble("OBDriveDistTimeout"))); 
                
                if (!isLeftSideAuto) //if we're on the right side make sure we wait for hot goal!
                    ac.addCommand(new WaitCommand(Constants.getDouble("OBWaitForHot")));
                
                ac.addCommand(new SetBallSettlerCommand());
                ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("winchDisengageTimeout")));
                break;
                
            case DID_NOT_DETECT:
            default:
                ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("OBDriveForwardDist"), 
                                                           Constants.getDouble("OBDriveAngle"), 
                                                           Constants.getDouble("OBDriveDistTimeout"))); 
                ac.addCommand(new SetBallSettlerCommand());
                ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("winchDisengageTimeout")));
                break;
        }

        return ac;
    }
    
    public AutonController twoBall() { 
        AutonController ac = new AutonController();
        ac.clear();
        Constants.getInstance();   
        
        ac.addCommand(new SetIntakePositionCommand());
        
        ac.addCommand(new WindBackWinchTimeOutCommand(Constants.getDouble("TBV3WinchSetpoint"),
                                                       Constants.getDouble("TBV3FirstShotWindTimeout")));
        
        ac.addCommand(new WaitCommand(Constants.getDouble("TBV3SmallDelay")));
        
        ac.addCommand(new SetBallSettlerCommand());
        
        ac.addCommand(new WaitCommand(Constants.getDouble("TBV3SmallDelay")));
        
        ac.addCommand(new TwoParallelMotionsCommand(new DriveToPosTimeOutCommand(Constants.getDouble("TBV3DriveForwardDist1"), //Drive forward to set point  //
                                                                                 Constants.getDouble("TBV3DriveAngle1"),                                     //
                                                                                 Constants.getDouble("TBV3DriveDistTimeout1")),                              //This is all in one Sequence
                                                        0,                                                                                                  //
                                                        new IntakeTimeOutCommand(Constants.getDouble("TBV3IntakePWM1"), //Setpoint for intake speed         //
                                                                                 Constants.getDouble("TBV3IntakeTime1"))));       
        
        ac.addCommand(new TwoParallelMotionsCommand(new DriveToPosTimeOutCommand(Constants.getDouble("TBV3DriveForwardDist2"), //Drive forward to set point  //
                                                                                 Constants.getDouble("TBV3DriveAngle2"),                                     //
                                                                                 Constants.getDouble("TBV3DriveDistTimeout2")),                              //This is all in one Sequence
                                                        0,                                                                                                  //
                                                        new IntakeTimeOutCommand(Constants.getDouble("TBV3Outake"), //Setpoint for intake speed         //
                                                                                 Constants.getDouble("TBV3OutakeTime1"))));                               //
      
        ac.addCommand(new IntakeTimeOutCommand(Constants.getDouble("TBV3OutakePWM"), 
                                               Constants.getDouble("TBV3OutakeTime")));
        
        ac.addCommand(new SetBallSettlerCommand());
        
        ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("winchDisengageTimeout")));
        
        ac.addCommand(new EngageWinchCommand(Constants.getDouble("bWinchShiftTime")));
             
        ac.addCommand(new TwoParallelMotionsCommand(new IntakeTimeOutCommand(Constants.getDouble("TBV3IntakePWM3"), 
                                               Constants.getDouble("TBV3IntakeTime3")), 0, 
                new WindBackWinchTimeOutCommand(Constants.getDouble("TBV3WinchSetpointTwo"), Constants.getDouble("TBV3FirstShotWindTimeout"))));
                
        ac.addCommand(new TwoParallelMotionsCommand(new WindBackWinchTimeOutCommand(Constants.getDouble("TBV3WinchSetpoint"),
                                                                                    Constants.getDouble("TBV3FirstShotWindTimeout")),
                                                                                    Constants.getDouble("InBetweenTime"), new TwoParallelMotionsCommand(new SetIntakePositionCommand(),
                 0,
                 new IntakeTimeOutCommand(Constants.getDouble("TBV3IntakePWM2"),
                                          Constants.getDouble("TBV3IntakeTime2")))));
        
        
        ac.addCommand(new IntakeTimeOutCommand(0,0.05));
        
        ac.addCommand(new WaitCommand(Constants.getDouble("TBV3WaitBeforeSecondShot")));
        
        ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("winchDisengageTimeout")));
        
        return ac;
    }     
        
    public AutonController testDrive() { //NEW CONSTANTS UPDATE!
        AutonController ac = new AutonController();
        ac.clear();
        Constants.getInstance();   
        
        
        //Delete testDriveDistV2, testDriveAngleV2, testDriveDistTimeoutV2
        
        ac.addCommand(new WaitCommand(Constants.getDouble("waitBeforeDrive")));
        
        ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("testDriveDist"), //new
                                                   Constants.getDouble("testDriveAngle"), //angle goal
                                                   Constants.getDouble("testDriveDistTimeout"))); //new

        return ac;
    }
    
    public AutonController testTurn() { 
        AutonController ac = new AutonController();
        ac.clear();
        Constants.getInstance();   
        
        ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("testAngleGoal"), 
                                                    Constants.getDouble("testAngleTimeout")));
        return ac;
    } 
    
    public AutonController test() { 
        AutonController ac = new AutonController();
        
        ac.clear();
        
        Constants.getInstance();   

//        ac.addCommand(new WindBackWinchTimeOutCommand(Constants.getDouble("TBV3WinchSetpoint"),
//                                                       Constants.getDouble("TBV3FirstShotWindTimeout")));
//        
//        ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("winchDisengageTimeout")));
        
        return ac;
    }
}

