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

/**
 *
 * @author Team1241
 */
public class Intake {
    
    static Intake inst = null;
    
    Talon leftSide;
    Talon rightSide;
    
    DigitalInput intakeLimit;
    

    public Intake(){

        leftSide = new Talon(ElectricalConstants.LEFT_SIDE_INTAKE_PWM);
        rightSide = new Talon(ElectricalConstants.RIGHT_SIDE_INTAKE_PWM);
        
        intakeLimit = new DigitalInput(ElectricalConstants.INTAKE_BALL_LIMIT);
        

    }
    public static Intake getInstance() {
    
        if(inst == null) {
            inst = new Intake();
        }
        return inst;
    }
    public void setIntakeSpeed(double pwm) {
        
        rightSide.set(pwm);
        leftSide.set(pwm);
    }
    public boolean ballDetected() {
        return !intakeLimit.get();
    }
    
   
}