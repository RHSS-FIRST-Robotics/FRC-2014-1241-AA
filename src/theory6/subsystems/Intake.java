/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package theory6.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import theory6.main.ElectricalConstants;
import theory6.pid.PIDController;
import edu.wpi.first.wpilibj.AnalogChannel;
import theory6.utilities.ToggleBoolean;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import theory6.utilities.JoystickScaler;

/**
 *
 * @author Team1241
 */
public class Intake {
    
    static Intake inst = null;
    
    Talon leftSide;
    Talon rightSide;
    DoubleSolenoid intakeAnglePiston;
    DigitalInput intakeLimit;
    ToggleBoolean intakeAngleToggle;
    boolean intakeAngleState = false;
    
    JoystickScaler rightAnalogScaler = new JoystickScaler();
    
    
    public Intake(){
        
        
        

        leftSide = new Talon(ElectricalConstants.LEFT_SIDE_INTAKE_PWM);
        rightSide = new Talon(ElectricalConstants.RIGHT_SIDE_INTAKE_PWM);
     //   intakeAnglePiston = new DoubleSolenoid (ElectricalConstants.INTAKE_DOWN, ElectricalConstants.INTAKE_UP);
       // intakeAngleToggle = new ToggleBoolean();
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
        }
        leftSide.set(-speed);
        rightSide.set(speed);
        }
        public void intakeBall(double joy, int scaledPower)
        {
            setSpeed(rightAnalogScaler.scaleJoystick(joy, scaledPower));
            
        }
    //public boolean ballDetected() {
        //return !intakeLimit.get();
    //}
    
//    public void setIntakePosition(boolean intakeAngleToggleButton) {
//        intakeAngleToggle.set(intakeAngleToggleButton);
//        if(intakeAngleToggle.get())
//            intakeAngleState = !intakeAngleState;
//        
//        if(intakeAngleState)
//            intakeAnglePiston.set(DoubleSolenoid.Value.kForward);
//        else 
//            intakeAnglePiston.set(DoubleSolenoid.Value.kReverse);
//    }
    
   
}