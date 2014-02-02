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
        public void setSpeed(double speed) 
        {

            if (Math.abs(speed) < 0.05 ) 
            {
                speed = 0.0;
            
                leftSide.set(speed);
                rightSide.set(speed);
                
                leftBottomSide.set(Relay.Value.kOff);
                rightBottomSide.set(Relay.Value.kOff);
            
            }
            else{
            
        
                leftSide.set(-speed);
                rightSide.set(speed);
        
                leftBottomSide.set(Relay.Value.kReverse);
                rightBottomSide.set(Relay.Value.kForward);
        
            }
        }
        public void intakeBall(double joy, int scaledPower)
        {
            setSpeed(rightAnalogScaler.scaleJoystick(joy, scaledPower));
            
        }
    //public boolean ballDetected() {
        //return !intakeLimit.get();
    //}
    
    public void setIntakePosition(boolean intakeAngleToggleButton) {
        //intakeAngleToggle.set(intakeAngleToggleButton);
        //if(intakeAngleToggle.get())
            //intakeAngleState = !intakeAngleState;
        intakeAngleState = intakeAngleToggleButton;
        if(intakeAngleState)
            intakeAnglePiston.set(DoubleSolenoid.Value.kReverse);
        else 
            intakeAnglePiston.set(DoubleSolenoid.Value.kForward);
    }
    
   
}