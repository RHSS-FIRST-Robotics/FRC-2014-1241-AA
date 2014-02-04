/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package theory6.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import theory6.main.ElectricalConstants;
import theory6.pid.PIDController;
import theory6.utilities.ToggleBoolean;


public class Catapult {

    static Catapult inst = null;
    
    Talon rightWinchMotor;
    Talon leftWinchMotor;
    PIDController winchPID;
    DoubleSolenoid winchReleasePiston;
    ToggleBoolean winchReleaseToggle;
    AnalogChannel winchPot;
    
    boolean winchPistonState = false;
    
    public Catapult()
    {
        winchPID = new PIDController(0,0,0,0);
        
        winchPot= new AnalogChannel(ElectricalConstants.WINCH_POT);
    
        rightWinchMotor = new Talon(ElectricalConstants.RIGHT_WINCH_PWM);
        leftWinchMotor = new Talon(ElectricalConstants.LEFT_WINCH_PWM);
    
        winchReleasePiston = new DoubleSolenoid (ElectricalConstants.WINCH_ENGAGE, ElectricalConstants.WINCH_DISENGAGE);
        winchReleaseToggle = new ToggleBoolean();
    }

    public static Catapult getInstance() {
        if(inst == null) {
            inst = new Catapult();
        }
        return inst;
    }
    
    public void setWinchPWM(double pwm) {
        rightWinchMotor.set(pwm);
        leftWinchMotor.set(pwm);
    }
    
    public double getWinchPot() {
        return winchPot.getAverageValue();
    }
    
    public void setWinchPID (double p, double i, double d, double ff) {
        winchPID.changeGains(p, i, d, ff);
    }
    
    public void setWinchPosPID(int setpoint) { 
        engageWinch();
        winchPID.setGoal(setpoint);
        setWinchPWM(winchPID.updateOutput(getWinchPot()));
    }
    
    public void setWinchPos(int setpoint, int tol) { 
        engageWinch();
        if(setpoint - getWinchPot() > tol) 
            setWinchPWM(-0.7);
        else if(setpoint - getWinchPot() < tol) 
            setWinchPWM(0.7);
        else 
            setWinchPWM(0);   
    }
    
    public void windBackWinch(boolean windBackButton) {
        if(windBackButton)
            setWinchPWM(0.5);
        else
            setWinchPWM(0);
    }
    
    public void toggleWinchPistonPos(boolean winchPistonToggleButton) {
        winchReleaseToggle.set(winchPistonToggleButton);
        if(winchReleaseToggle.get())
            winchPistonState = !winchPistonState;
        
        if(winchPistonState)
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
        else 
            winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
    }

    public void engageWinch() {
        
        if(!winchPistonState){
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
            winchPistonState = true;   
        }
    }
    
    public void disengageWinch()
    {
        if(winchPistonState){
            winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
            winchPistonState = false;     
        }
    }

}
