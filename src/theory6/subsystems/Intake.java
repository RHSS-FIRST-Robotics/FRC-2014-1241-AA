/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package theory6.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import theory6.main.ElectricalConstants;
import theory6.utilities.ToggleBoolean;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import theory6.utilities.JoystickScaler;

/**
 *
 * @author Team1241
 */
public class Intake {
    
    static Intake inst = null;
    
    Talon leftSide;
    Talon rightSide;
    
    Relay leftBottomSide;
    Relay rightBottomSide;
    
    DoubleSolenoid intakeAnglePiston;
    
    DigitalInput intakeLimit;
    ToggleBoolean intakeAngleToggle;
    boolean intakeAngleState = false;
    
    JoystickScaler rightAnalogScaler = new JoystickScaler();
    
    
    public Intake(){
        leftSide = new Talon(ElectricalConstants.LEFT_SIDE_INTAKE_PWM);
        rightSide = new Talon(ElectricalConstants.RIGHT_SIDE_INTAKE_PWM);
        
        leftBottomSide = new Relay(ElectricalConstants.LEFT_SIDE_INTAKE);
        rightBottomSide = new Relay(ElectricalConstants.RIGHT_SIDE_INTAKE);
        
        
        intakeAnglePiston = new DoubleSolenoid (ElectricalConstants.INTAKE_DOWN, ElectricalConstants.INTAKE_UP);
        intakeAngleToggle = new ToggleBoolean();
        //intakeLimit = new DigitalInput(ElectricalConstants.INTAKE_BALL_LIMIT);
    }
    public static Intake getInstance() {
    
        if(inst == null) {
            inst = new Intake();
        }
        return inst;
    }
    public void setRollerPWM(double pwm) 
    {
        if (Math.abs(pwm) < 0.05) 
        {
            leftSide.set(0);
            rightSide.set(0);
        }
        else{
            leftSide.set(-pwm);
            rightSide.set(pwm);
        }
    }
    public void setPG71(double intakePWM)
    {
        if(intakePWM > 0.05){ //intake
            leftBottomSide.set(Relay.Value.kReverse);
            rightBottomSide.set(Relay.Value.kForward);
        }
        else if (intakePWM < -0.05){ //outake
            leftBottomSide.set(Relay.Value.kForward);
            rightBottomSide.set(Relay.Value.kReverse);
        }
        else  
        {
            leftBottomSide.set(Relay.Value.kOff);
            rightBottomSide.set(Relay.Value.kOff);
        }
    }
    
    
    public void intakeBall(double joy, int scaledPower)
    {
        setRollerPWM(rightAnalogScaler.scaleJoystick(joy, scaledPower));
        setPG71(joy);
    }
    //public boolean ballDetected() {
        //return !intakeLimit.get();
    //}
    
    //Used for tele-op control of intake position
    public void setIntakePosTeleop(boolean intakeAngleToggleButton) {
        if(intakeAngleToggleButton)
            intakeAnglePiston.set(DoubleSolenoid.Value.kForward);
        else 
            intakeAnglePiston.set(DoubleSolenoid.Value.kReverse);
    }
    
    //Used for toggling position in autonomous
    public void toggleIntakePosAuton(boolean intakeToggle) {
        intakeAngleToggle.set(intakeToggle);
        if(intakeAngleToggle.get())
            intakeAngleState = !intakeAngleState;
        if(intakeAngleState)
            intakeAnglePiston.set(DoubleSolenoid.Value.kReverse);
        else 
            intakeAnglePiston.set(DoubleSolenoid.Value.kForward);
    }
}