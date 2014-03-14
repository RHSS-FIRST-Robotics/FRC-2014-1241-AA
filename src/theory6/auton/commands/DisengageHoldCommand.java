/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package theory6.auton.commands;

import theory6.subsystems.Catapult;
import theory6.auton.AutonCommand;
/**
 *
 * @author Robotics
 */
public class DisengageHoldCommand implements AutonCommand{
     Catapult catapult;   
    
    public DisengageHoldCommand (){
        catapult = catapult.getInstance();
    }
        
    public void init() {
        catapult.releaseBall();
    }

    public boolean run() {

        catapult.releaseBall();
    
        return true;
    }

    public void done() {

    } 
}


