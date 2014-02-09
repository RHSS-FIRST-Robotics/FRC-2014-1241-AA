/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.auton.commands;

import theory6.auton.AutonCommand;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Sagar
 */
public class WaitCommand implements AutonCommand {
    
    Timer waitTimer = new Timer();
    boolean first = true;
    double timeToWait = 0;

    public WaitCommand(double timeInSeconds) {
        timeToWait = timeInSeconds;
    }
    
    public void init() {
        waitTimer.start();
    }

    public boolean run() {
        return waitTimer.get() >= timeToWait;
    }

    public void done() {
        
    }
    
}
