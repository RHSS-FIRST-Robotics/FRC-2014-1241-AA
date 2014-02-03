/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import theory6.auton.AutonCommand;
import theory6.subsystems.Catapult;
import theory6.utilities.Constants;

/**
 *
 * @author Controls
 */
public class ShootBallTimeOutCommand implements AutonCommand{
    
    Catapult catapult; 
    
    Timer t = new Timer();
    
    double timeOutInSecs;
    
    
    
    
    public ShootBallTimeOutCommand(double ticks, double timeOut){
       
        
        Catapult.getInstance();
        
        this.timeOutInSecs = timeOut;
        
        
    }
    public void init()
    {
        t.reset();
        t.start();

        
    }
    public boolean run(){
        
        while(!(catapult.getWinchPot() == Constants.getDouble(bWinchPosOne))){
            
            catapult.setWinchPWM(0.7);
            
        }
        
        catapult.toggleWinchPistonPos(true);
        
        

        
        
        return catapult.getWinchPot() > Constants.getDouble(bWinchPosOne) ||
               t.get() > timeOutInSecs;
    }
    public void done(){
        

        
    }    
    
    
    
    
    
    
    
    
    
    
}
