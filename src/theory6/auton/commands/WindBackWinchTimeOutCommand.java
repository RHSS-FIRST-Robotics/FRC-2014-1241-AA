/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package theory6.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import theory6.subsystems.Catapult;
import theory6.utilities.Constants; 
import theory6.auton.AutonCommand;

/**
 *
 * @author Shubham
 */
public class WindBackWinchTimeOutCommand implements AutonCommand {
    Catapult catapult;   
    Timer t = new Timer();
    
    double winchPos;
    double timeOutInSecs;
   
    public WindBackWinchTimeOutCommand(double setpoint, double timeOut){ 
        catapult = Catapult.getInstance();
        
        this.winchPos = setpoint;
            
        
        this.timeOutInSecs = timeOut; 
        Constants.getInstance();
    }
    public void init()
    {
        t.reset();
        t.start();   
    }
    public boolean run(){
       
//        if(!(catapult.getWinchPot() == winchPos))
//            catapult.setWinchPos(winchPos, Constants.getDouble("CatapultPotPosTolerance"));
//        else
//            catapult.setWinchPWM(0);
       //catapult.winchSetpoint = winchPos;
        catapult.setWinchPos(winchPos);
        return Math.abs(catapult.getWinchPot()-winchPos)<Constants.getDouble("bWinchPosTolerance") || t.get() > timeOutInSecs ;
    }
    public void done()
    {
        catapult.setWinchPWM(0);
    }       

}
