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
public class TwoParallelMotionsCommand implements AutonCommand {
    
    Timer t = new Timer();
    AutonCommand firstCmd;
    AutonCommand secondCmd;
    double wait;
    boolean secondTriggered = false;

    public TwoParallelMotionsCommand(AutonCommand cmd1, double waitTime, AutonCommand cmd2){
        firstCmd = cmd1;
        secondCmd = cmd2;
        wait = waitTime;
    }

    public void init() {
        firstCmd.init();
        t.reset();
        t.start();
    }

    public boolean run() {
        if(t.get() > wait){
            if(!secondTriggered){
                secondCmd.init();
                secondTriggered = true;
            }
            secondCmd.run();
        }
        return firstCmd.run();
    }

    public void done() {
        firstCmd.done();
        secondCmd.done();
    }  
}
