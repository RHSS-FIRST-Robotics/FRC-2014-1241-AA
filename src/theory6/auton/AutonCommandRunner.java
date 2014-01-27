/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.auton;

/**
 *
 * @author Sagar
 */
public class AutonCommandRunner {
    AutonCommand cmd;
    boolean firstRun = true;
    boolean cmdFinished = false;

    public AutonCommandRunner(AutonCommand myCmd){
        cmd = myCmd;
    }

    public boolean run() {
        if(cmdFinished){
            return true; // dont do any more work on it.
        }

        if(firstRun){
            cmd.init();
            firstRun = false;
        }
        boolean finished = cmd.run();

        if(finished){
            cmd.done();
            cmdFinished = true;
        }
        return finished;
    }
}
