/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import theory6.subsystems.Intake;

/**
 *
 * @author Shubham
 */
public class SetIntakePositionCommand {
    Intake intake;   
    
    public SetIntakePositionCommand (){
        intake = intake.getInstance();
    }
        
    public void init() {
        //Use toggleIntakePos function in Intake class
    }

    public boolean run() {

    intake.toggleIntakePosAuton(true);
    
        return false;
    }

    public void done() {

    } 
}
