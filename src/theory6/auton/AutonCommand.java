/*
 * List of functions to use as a template for writing autonomous commands
 */
package theory6.auton;

/**
 *
 * @author Sagar
 */
public interface AutonCommand {
    void init();
    boolean run();
    void done();
}
