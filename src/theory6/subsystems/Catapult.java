/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package theory6.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import theory6.main.ElectricalConstants;

import theory6.pid.PIDController;

import theory6.utilities.Constants;
import theory6.utilities.ToggleBoolean;


public class Catapult {

    static Catapult inst = null;
    
    Talon rightWinchMotor;
    Talon leftWinchMotor;
    
    DoubleSolenoid winchReleasePiston;
    DoubleSolenoid trussShotPiston;
    ToggleBoolean trussShotToggle;
    ToggleBoolean winchReleaseToggle;
    ToggleBoolean winchShiftToggle;
    AnalogChannel winchPot;
    Timer winchShiftTimer;

    public boolean winchPistonState = true;
    public boolean setDisengaged = false;


    boolean trussShotState = false;
            boolean setEngaged = true;
            
    public double winchSetpoint;
    
    final int SET_ENGAGED = 0;
    final int WIND_WINCH = 1;
    int engagedState = 1;
    
    public Catapult()
    {
        
        winchPot= new AnalogChannel(ElectricalConstants.WINCH_POT);
    
        rightWinchMotor = new Talon(ElectricalConstants.RIGHT_WINCH_PWM);
        leftWinchMotor = new Talon(ElectricalConstants.LEFT_WINCH_PWM);
        
        trussShotPiston = new DoubleSolenoid (ElectricalConstants.TRUSS_ENGAGE, ElectricalConstants.TRUSS_DISENGAGE);
        trussShotToggle = new ToggleBoolean();
        winchReleaseToggle = new ToggleBoolean();
        winchReleasePiston = new DoubleSolenoid ( ElectricalConstants.WINCH_ENGAGE, ElectricalConstants.WINCH_DISENGAGE);
        winchShiftTimer = new Timer();
        winchShiftTimer.start();
        
        winchShiftToggle = new ToggleBoolean();
        
        Constants.getInstance();
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
    
    public void setWinchPos(double setpoint) { 

        switch(engagedState){
            case SET_ENGAGED:
                setEngaged = engageWinch(true);
                if (setEngaged == true)
                    engagedState = WIND_WINCH;
                break;
            case WIND_WINCH:
                if (setpoint == getWinchPot())
                    setWinchPWM(0);
                else if(setpoint - getWinchPot() > Constants.getInteger("bWinchPosTolerance")) 
                    setWinchPWM(Constants.getDouble("bWinchWindBackSpeed"));
                else if(setpoint - getWinchPot() < Constants.getInteger("bWinchPosTolerance")) 
                    setWinchPWM(-Constants.getDouble("bWinchWindBackSpeed"));
                else 
                    setWinchPWM(0);   
                
                if (!winchPistonState && !setDisengaged)
                    engagedState =  SET_ENGAGED;

                break;
        }

        
    }
    
    public void setWinchState(double manualAdjustment, 
            boolean presetOne, boolean presetTwo, boolean disengage){

        if (Math.abs(manualAdjustment) > 0.1){
            if (!winchPistonState && !setDisengaged)
                    engagedState =  SET_ENGAGED;
            switch(engagedState){
            case SET_ENGAGED:
                setEngaged = engageWinch(true);
                if (setEngaged == true)
                    engagedState = WIND_WINCH;
                break;
            case WIND_WINCH:
                setWinchPWM(manualAdjustment);  
                if (!winchPistonState && !setDisengaged)
                    engagedState =  SET_ENGAGED;
                break;
            }         
        }
        else {
        if (presetOne){
                winchSetpoint = Constants.getDouble("bWinchPosOne");
                setDisengaged = false;
        }
        else if (presetTwo){
                winchSetpoint = Constants.getDouble("bWinchPosTwo");
                setDisengaged = false;
            }
        else if(disengage){
            winchSetpoint = getWinchPot();
            setDisengaged = true;
        }
        else { 
            winchSetpoint = getWinchPot();  
            //setDisengaged = true;
        }
        
        setWinchPos(winchSetpoint);
        
        //System.out.println(winchSetpoint);
        }
            
    }
    
    public void setWinchPos(double setpoint, double pwm) { 
        engageWinch(true);
        if(setpoint - getWinchPot() > Constants.getInteger("bWinchPosTolerance")) 
            setWinchPWM(pwm);
        else if(setpoint - getWinchPot() < Constants.getInteger("bWinchPosTolerance")) 
            setWinchPWM(-pwm);
        else 
            setWinchPWM(0);   

    }
    
    public void toggleWinchPistonPos(boolean winchPistonToggleButton) {        
        winchReleaseToggle.set(winchPistonToggleButton);
        
        if(winchReleaseToggle.get())
            winchPistonState = !winchPistonState;
        
        if(!winchPistonState)
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
        else 
            winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void toggleTrussPistonPos(boolean trussPistonToggleButton) {     
        trussShotToggle.set(trussPistonToggleButton);
        
        if(trussShotToggle.get())
            trussShotState = !trussShotState;
        
        if(trussShotState)
            trussShotPiston.set(DoubleSolenoid.Value.kForward);
        else 
            trussShotPiston.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void holdTrussPistonPos(boolean trussPistonToggleButton) {
        if(trussPistonToggleButton)
            trussShotPiston.set(DoubleSolenoid.Value.kForward);
        else 
            trussShotPiston.set(DoubleSolenoid.Value.kReverse);
    }

    public boolean engageWinch(boolean winchShiftToggleButton) {
        boolean returnVar = false;
        winchShiftToggle.set(winchShiftToggleButton);
        if(winchShiftToggleButton){

            if(winchShiftToggle.get()){
                winchShiftTimer.reset();
            }
            //System.out.println(winchShiftToggle.get());

            if(!winchPistonState){
                setWinchPWM(Constants.getDouble("bWinchShiftSpeed")); 
                winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
                System.out.println(winchShiftTimer.get());
                if(winchShiftTimer.get() > Constants.getDouble("bWinchShiftTime")) { 
                    setWinchPWM(0); 
                    winchPistonState = true;
                    winchShiftToggle.set(false);
                    setDisengaged = false;
                    returnVar = true;
                    
                }
            }
        
        }
        return returnVar;
    }

    public void disengageWinch(boolean button)

    {
        if(winchPistonState && button){
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
            winchPistonState = false;  
            setDisengaged = true;
        }
    }


}
