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
        
        ShootBallTimeOutCommand(Constants.getDouble(WinchPosOne),Constants.getDouble(aShootBallTimeOut));
        DriveToPosV2TimeOutCommand(Constants.getDouble(DriveForwardDistance),
                                   0,
                                   aDriveToNewZoneTimeOut);
        

        return ac;
    }
   public AutonController shootGoalAndAllianceDriveForward() {
       
       AutonController ac = new AutonController();
       ac.clear();
       
       ShootBallTimeOutCommand(Constants.getDouble(WinchPosOne), 
                               Constants.getDouble(aShootBallTimeOut));
       TurnDegreesTimeOutCommand(Constants.getDouble(AlliancePartnerAngle), 
                                 Constants.getDouble(aTurnDegreesTimeOut));
       DriveForwardIntakeBallTimeOutCommand(Constants.getDouble(DistanceToAlliancePartner), 
                                            Constants.getDouble(aDriveForwardTimeOut));
       ShootBallTimeOutCommand(Constants.getDouble(WinchPosTwo), 
                               Constants.getDouble(aShootBallTimeOut));
       DriveToPosV2TimeOutCommand(Constants.getDouble(DistanceToNewZone),
                                  0,
                                  Constants.getDouble(aDriveToNewZoneTimeOut));
       
       return ac;
       
   }
   public AutonController shootOtherGoalAndAllianceDriveForward() {
       
       AutonController ac = new AutonController();
       ac.clear();
       
       TurnDegreesTimeOutCommand(Constants.getDouble(OtherGoalAngle), 
                                 Constants.getDouble(aTurnDegreesTimeOut));
       ShootBallTimeOutCommand(Constants.getDouble(WinchPosOne),
                               Constants.getDouble(aShootBallTimeOut));
       TurnDegreesTimeOutCommand(Constants.getDouble(AlliancePartnerAngleTwo), 
                                 Constants.getDouble(aTurnDegreesTimeOut));
       DriveForwardIntakeBallCommand(Constants.getDouble(DistanceToAllianceTwo), 
                                     Constants.getDouble(aDriveForwardTimeOut));
       TurnDegreesTimeOutCommand(Constants.getDouble(TurnDegreesToGoal), 
                                 Constants.getDouble(aTurnDegreesTimeOut));
       ShootBallTimeOutCommand(Constants.getDouble(WinchPosThree), 
                               Constants.getDouble(aShootBallTimeOut));
       DriveToPosV2TimeOutCommand(Constants.getDouble(DriveToNewZone), 
                                  0, 
                                  Constants.getDouble(aDriveToNewZoneTimeOut));
       
       
   }
 
}

