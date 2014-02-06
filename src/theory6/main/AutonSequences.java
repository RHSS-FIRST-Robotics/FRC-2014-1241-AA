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
        
        ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("bWinchPosOne"),
                                                  Constants.getDouble("aShootBallTimeOut")));
        ac.addCommand(new DriveToPosV2TimeOutCommand(Constants.getDouble("DriveForwardDistance"),
                                   0.0,
                                   Constants.getDouble("aDriveToNewZoneTimeOut")));
        

        return ac;
    }
   public AutonController shootGoalAndAllianceDriveForward() {
       
       AutonController ac = new AutonController();
       ac.clear();
       
       ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("bWinchPosOne"), 
                               Constants.getDouble("aShootBallTimeOut")));
       ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("AlliancePartnerAngle"), 
                                 Constants.getDouble("aTurnDegreesTimeOut")));
       ac.addCommand(new DriveForwardIntakeBallTimeOutCommand(Constants.getDouble("DistanceToAlliancePartner"), 
                                            Constants.getDouble("aDriveForwardTimeOut")));
       ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("bWinchPosTwo"), 
                               Constants.getDouble("aShootBallTimeOut")));
       ac.addCommand(new DriveToPosV2TimeOutCommand(Constants.getDouble("DistanceToNewZone"),
                                  0,
                                  Constants.getDouble("aDriveToNewZoneTimeOut")));
       
       return ac;
       
   }
   public AutonController shootOtherGoalAndAllianceDriveForward() {
       
       AutonController ac = new AutonController();
       ac.clear();
       
       ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("OtherGoalAngle"), 
                                 Constants.getDouble("aTurnDegreesTimeOut")));
       ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("WinchPosOne"),
                               Constants.getDouble("aShootBallTimeOut")));
       ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("AlliancePartnerAngleTwo"), 
                                 Constants.getDouble("aTurnDegreesTimeOut")));
       ac.addCommand(new DriveForwardIntakeBallTimeOutCommand(Constants.getDouble("DistanceToAllianceTwo"), 
                                     Constants.getDouble("aDriveForwardTimeOut")));
       ac.addCommand(new TurnDegreesTimeOutCommand(Constants.getDouble("TurnDegreesToGoal"), 
                                 Constants.getDouble("aTurnDegreesTimeOut")));
       ac.addCommand(new ShootBallTimeOutCommand(Constants.getDouble("WinchPosThree"), 
                               Constants.getDouble("aShootBallTimeOut")));
       ac.addCommand(new DriveToPosV2TimeOutCommand(Constants.getDouble("DriveToNewZone"), 
                                  0, 
                                  Constants.getDouble("aDriveToNewZoneTimeOut")));
       
       return ac;
       
       
   }
 
}

