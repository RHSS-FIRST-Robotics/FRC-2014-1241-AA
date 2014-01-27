/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.auton;

import edu.wpi.first.wpilibj.Timer;
import theory6.utilities.Queue;

/**
 *
 * @author Sagar
 */
public class AutonController {
    Queue cmdList = new Queue();
    AutonCommand curCmd = null;

    // Timeout handling
    Timer timeoutTimer = new Timer();
    AutonCommand timeoutCommand = null;
    double timeoutTime = 100;
    boolean firstRunOfController = true;
    AutonCommandRunner timeoutRunner = null;

    // Add commands
    public void addCommand(AutonCommand cmd) {
        cmdList.addToQueue(cmd);
    }

    public void setTimeoutCommand(AutonCommand toCmd, double time) {
        timeoutCommand = toCmd;
        timeoutTime = time;
    }

    public void clear() {
        cmdList.removeAllElements();
        curCmd = null;
    }

    public void executeCommands() { 
        boolean firstRun = false;
        if (curCmd == null) {
            curCmd = (AutonCommand) cmdList.populateQueue();
            if (curCmd != null) {
                System.out.println("Command Started: " + curCmd);
            }
            firstRun = true;
        }

        if (curCmd != null) {
            if (firstRun) {
                curCmd.init();
                firstRun = false;
            }
            if (curCmd.run()) {
                // this will happen when command is done
                // Will grab next command on next call of execute()
                curCmd.done();
                curCmd = null;
                System.out.println("Finished Command");
            }
        } 
        else { 
            //do nothing
        }

        //timeout handling code
        if (timeoutCommand != null) {
            if (firstRunOfController) {
                timeoutTimer.start();
                firstRunOfController = false;
                timeoutRunner = new AutonCommandRunner(timeoutCommand);
            }
            if (timeoutTimer.get() >= timeoutTime) { //run timeout command
                boolean result = timeoutRunner.run();
                if(result){
                    timeoutCommand = null; //dont do any more work on this command
                }
            }
        }

    }
    
}
