/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import theory6.auton.AutonCommand;
import theory6.subsystems.Catapult;

/**
 *
 * @author Shubham
 */
public class SetBallSettlerCommand implements AutonCommand{
    Catapult catapult;   
    
    public SetBallSettlerCommand (){
        catapult = catapult.getInstance();
    }
        
    public void init() {
        catapult.toggleBallSettler(false);
    }

    public boolean run() {

        catapult.toggleBallSettler(true);
    
        return true;
    }

    public void done() {

    } 
}
