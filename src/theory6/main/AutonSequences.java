/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.main;

import theory6.subsystems.DriveTrain;
import theory6.auton.*;
import theory6.auton.commands.*;

import theory6.utilities.Constants;

                                                                                                                
/**
 *
 * @author Sagar
 */
public class AutonSequences {
        
    DriveTrain driveTrain = DriveTrain.getInstance();
    
   public AutonController shootBallDriveForward() { //finalized sequence
        AutonController ac = new AutonController();
        ac.clear();
        
        ac.addCommand(new ShootBallTimeOutCommand());
        ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("DriveForwardDistance"),
                                   0.0,
                                   Constants.getDouble("aDriveToNewZoneTimeOut")));
        

        return ac;
    }
   public AutonController shootGoalAndAllianceDriveForward() {
       
       AutonController ac = new AutonController();
       ac.clear();
       
       ac.addCommand(new ShootBallTimeOutCommand());
       ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("aAlliancePartnerAngle"), 
                                 Constants.getDouble("aTurnDegreesTimeOut")));
       //ac.addCommand(new DriveForwardIntakeBallTimeOutCommand(Constants.getDouble("aDistanceToAlliancePartner"), 
                                            //Constants.getDouble("aDriveForwardTimeOut")));
       ac.addCommand(new ShootBallTimeOutCommand());
       ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("aDistanceToNewZone"),
                                  0,
                                  Constants.getDouble("aDriveToNewZoneTimeOut")));
       
       return ac;
       
   }
   public AutonController shootOtherGoalAndAllianceDriveForward() {
       
       AutonController ac = new AutonController();
       ac.clear();
       
       ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("aOtherGoalAngle"), 
                                 Constants.getDouble("aTurnDegreesTimeOut")));
       ac.addCommand(new ShootBallTimeOutCommand());
       ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("aAlliancePartnerAngleTwo"), 
                                 Constants.getDouble("aTurnDegreesTimeOut")));
       //ac.addCommand(new DriveForwardIntakeBallTimeOutCommand(Constants.getDouble("aDistanceToAllianceTwo"), 
                                     //Constants.getDouble("aDriveForwardTimeOut")));
       
       ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("aTurnDegreesToGoal"), 
                                 Constants.getDouble("aTurnDegreesTimeOut")));
       ac.addCommand(new ShootBallTimeOutCommand());
       ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("aDriveToNewZone"), 
                                  0, 
                                  Constants.getDouble("aDriveToNewZoneTimeOut")));
       
       return ac;
   }   
       
   
       
       public AutonController testAutonDriveV1() { //NEW CONSTANTS UPDATE!
        AutonController ac = new AutonController();
        ac.clear();
   
        Constants.getInstance();   
//ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("testDriveDistV2"), //new
//                                                   Constants.getDouble("testDriveAngleV2"), //angle goal
//                                                     Constants.getDouble("testDriveDistTimeoutV2"))); //new
       ac.addCommand(new SetIntakePositionCommand());
       ac.addCommand(new IntakeTimeOutCommand(Constants.getDouble("intakePWM"),Constants.getDouble("intakeTime")));
        return ac;
    }
    
    public AutonController testAutonDriveV2() { 
        AutonController ac = new AutonController();
        ac.clear();
   
        Constants.getInstance();   
        
        ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("testDriveDistV2"), //new
                                                     Constants.getDouble("testDriveAngleV2"), //angle goal
                                                     Constants.getDouble("testDriveDistTimeoutV2"))); //new
        ac.addCommand(new WaitCommand(1));
        ac.addCommand(new ShootBallTimeOutCommand());
                
        return ac;
    }
    
    public AutonController driveAndShootOneBall() {
        AutonController ac = new AutonController();
        ac.clear();
        Constants.getInstance();
        ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("OBDriveForwardDist"), //new
                                                     Constants.getDouble("OBDriveAngle"), //angle goal
                                                     Constants.getDouble("OBDriveDistTimeout"))); //new
        return ac;
    }
    
    public AutonController twoBall() {
        AutonController ac = new AutonController();
        ac.clear();
        Constants.getInstance();
        //ac.addCommand(new ShootBallTimeOutCommand());
        ac.addCommand(new WindBackWinchTimeOutCommand(Constants.getDouble("bWinchPosTwo"),Constants.getDouble("TBSecondShotWindTimeout")));
        //ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("twoBallTurn1"),Constants.getDouble("twoBallTurnTimeout1")));
//        ac.addCommand(new SetIntakePositionCommand());
//        ac.addCommand(new TwoParallelMotionsCommand((new IntakeTimeOutCommand(Constants.getDouble("intakePWM"),Constants.getDouble("intakeTime"))),0,
//                new DriveToPosTimeOutCommand(Constants.getDouble("TBDriveForwardDist1"), //new
//                                                     Constants.getDouble("TBDriveAngle1"), //angle goal
//                                                     Constants.getDouble("TBDriveDistTimeout1")))); //new)))
//        ac.addCommand(new SetIntakePositionCommand());
//        ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("twoBallTurn2"),Constants.getDouble("twoBallTurnTimeout2")));
//                ac.addCommand(new DriveToPosTimeOutCommand(Constants.getDouble("TBDriveForwardDist2"), //new
//                                                     Constants.getDouble("TBDriveAngle2"), //angle goal
//                                                     Constants.getDouble("TBDriveDistTimeout2"))); //new
//        ac.addCommand(new ShootBallTimeOutCommand());
        return ac;
    }
    
    public AutonController testAutonTurn() { 
        AutonController ac = new AutonController();
        ac.clear();
   
        Constants.getInstance();   
        
        ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("testAngleGoal"), 
                                                    Constants.getDouble("testAngleTimeout"))); 
        return ac;
    } 
 
}

