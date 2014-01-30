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
        winchPID = new PIDController(0,0,0);
        
        winchPot= new AnalogChannel(ElectricalConstants.WINCH_POT);
    
        rightWinchMotor = new Talon(ElectricalConstants.RIGHT_WINCH_PWM);
        leftWinchMotor = new Talon(ElectricalConstants.LEFT_WINCH_PWM);
    
        winchReleasePiston = new DoubleSolenoid (ElectricalConstants.INTAKE_DOWN, ElectricalConstants.INTAKE_UP);
        winchReleaseToggle = new ToggleBoolean();
    
    
    
    }
    
    
    
    public static Catapult getInstance() {
        if(inst == null) {
            inst = new Catapult();
        }
        return inst;
    }
    public void setWinchSpeed(double pwm) {
        rightWinchMotor.set(-pwm);
        leftWinchMotor.set(-pwm);
    }
    public double getWinchPot() {
        return winchPot.getAverageValue();
    }
    
    public void setWinchPID (double p, double i, double d) {
        winchPID.changePIDGains(p, i, d);
    }
    public void setWinchPos(double potVal) {
        
        engageWinch();
        
        double currentPos = winchPot.getValue();
        
        double winchOutput = winchPID.calcPID(potVal, currentPos);
        
        setWinchSpeed(winchOutput);
    }
    
    public void resetPID() {
        winchPID.resetDerivative();
        winchPID.resetIntegral();
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
    public void engageWinch()
    {
        
        if(!winchPistonState)
        {
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
            winchPistonState = true;
        }
        
        
    }
    public void disengageWinch()
    {
        if(winchPistonState)
        {
            winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
            winchPistonState = false;
            
        }
        
        
    }
    
    
    
    
    
}
