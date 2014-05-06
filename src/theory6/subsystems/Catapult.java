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
import edu.wpi.first.wpilibj.DigitalInput;

import theory6.utilities.Constants;
import theory6.utilities.LogicalNotToggleBoolean;
import theory6.utilities.ToggleBoolean;


public class Catapult {

    static Catapult inst = null;
    
    Talon rightWinchMotor;
    Talon leftWinchMotor;
    
    DigitalInput catapultLimit;
   
    DoubleSolenoid winchReleasePiston;
    
    public DoubleSolenoid ballHolder;
    
    AnalogChannel winchPot;
    
    Timer winchShiftTimer;
    Timer ballHolderTimer;
    
    boolean holdState = false;

    final boolean ENGAGED = true;
    final boolean DISENGAGED = false;
    boolean winchPistonState = DISENGAGED; //piston assumed to be engaged in gearbox in default state when robot is turned on

    final boolean EXTENDED = true;
    final boolean RETRACTED = false;
            
    public double winchSetpoint;
    
    final int ENGAGE = 0;
    final int FULLY_ENGAGED = 1;  
    int setEngaged = FULLY_ENGAGED;
    
    public boolean firstRun = false; //used in disengageWinch
    
    boolean engageFlag = false;
    
    double error;
    double lastError;
    double deltaError;
    double lastDeltaError;
    
    final boolean RETRACT = false;
    final boolean HOLD	= true;
    
    boolean ballSettlerHoldState = RETRACTED;
    
    final boolean PRESSED = false;
    final boolean NOT_PRESSED = true;

    public Catapult()
    {   
        winchPot= new AnalogChannel(ElectricalConstants.WINCH_POT);
    
        rightWinchMotor = new Talon(ElectricalConstants.RIGHT_WINCH_PWM);
        leftWinchMotor = new Talon(ElectricalConstants.LEFT_WINCH_PWM);
        
        winchReleasePiston = new DoubleSolenoid (ElectricalConstants.WINCH_ENGAGE, 
                                                 ElectricalConstants.WINCH_DISENGAGE);
        ballHolder = new DoubleSolenoid(ElectricalConstants.HOLD_ENGAGE, 
                                        ElectricalConstants.HOLD_DISENGAGE);
        
        catapultLimit = new DigitalInput(ElectricalConstants.CATAPULT_LIMIT_SWITCH);
        
        winchShiftTimer = new Timer();
        winchShiftTimer.start();
        
        ballHolderTimer = new Timer();
        ballHolderTimer.start();
        
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
    
    public boolean getLS(){
        return catapultLimit.get();
    }
    
    public void setWinchPos(double setpoint) {
        error = setpoint - getWinchPot();
        
        if(!catapultLimit.get()){
            setWinchPWM(0);
            winchSetpoint = getWinchPot();
            log("in setpoint == winchPot");
        }

        else if (Math.abs(error) > Constants.getDouble("bWinchPosTolerance") && catapultLimit.get()) {
            if(error > 0) {
                setWinchPWM(Constants.getDouble("bWinchWindBackSpeed"));
                log("in first");
            }
            else if(error < 0) {
                setWinchPWM(-Constants.getDouble("bWinchWindBackSpeed"));
                log("in second");
            }
        }
        
        else 
            setWinchPWM(0);     
    }
    
    public void windWinch(double manualAdjustment, 
            boolean presetOne, boolean presetTwo){
        
        ToggleBoolean engageFirst = new ToggleBoolean();
        engageFirst.set(Math.abs(manualAdjustment) > 0.1 || presetOne || presetTwo);
        
        if (winchPistonState == DISENGAGED && engageFirst.get())
            engageFlag = true;
        
        else if(engageFlag == true) 
            engageWinch(true);
            
        if (presetOne)
            winchSetpoint = Constants.getDouble("bWinchPosOne");
        else if (presetTwo) 
            winchSetpoint = Constants.getDouble("bWinchPosTwo");
        
                
        else if(winchPistonState == ENGAGED) {
                
                if(Math.abs(manualAdjustment) > 0.1) {
                   
                    if(manualAdjustment > -0.1 && !catapultLimit.get())
                        setWinchPWM(0); 
                    else
                        setWinchPWM(manualAdjustment); 
                           
                   winchSetpoint = getWinchPot();
                }
                else 
                    setWinchPos(winchSetpoint);
                //else {
                    //winchSetpoint = getWinchPot();
                //}
                log("in preset" + winchSetpoint);
           // }  
        }
        
    }
    
    public boolean winchOnTarget() {
        error = winchSetpoint - getWinchPot();
        
        boolean done = (Math.abs(error) < Constants.getDouble("bWinchPosTolerance")) &&
                       (Math.abs(lastDeltaError) < Constants.getDouble("bWinchDeltaErrorTolerance"));
        
        deltaError = error - lastError;
        lastError = error;
        lastDeltaError = deltaError;
        
        return done;
    }
    
    public void toggleWinchPiston(boolean winchPistonToggleButton) {      
        ToggleBoolean winchStateToggle = new ToggleBoolean();
        winchStateToggle.set(winchPistonToggleButton);
        
        if(winchStateToggle.get())
            winchPistonState = !winchPistonState;
        
        if(winchPistonState == DISENGAGED) 
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
        else 
            winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void engageWinch(boolean winchShiftToggleButton) {
        
        ToggleBoolean winchShiftToggle = new ToggleBoolean();

        winchShiftToggle.set(winchShiftToggleButton);
        
        if(winchShiftToggleButton){
            
            if(winchShiftToggle.get())
                winchShiftTimer.reset();

            if(winchPistonState == DISENGAGED){
                setWinchPWM(Constants.getDouble("bWinchShiftSpeed")); 
                winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
                if(winchShiftTimer.get() > Constants.getDouble("bWinchShiftTime")) { 
                    setWinchPWM(0); 
                    winchPistonState = ENGAGED;
                    winchShiftToggle.set(false);
                    engageFlag = false;
                }
            }
        
        }
    }

    public void disengageWinch(boolean disengage) {
        if(firstRun) {
            winchPistonState = ENGAGED;
            firstRun = false;
        }

        if(winchPistonState && disengage){
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
            winchSetpoint = getWinchPot();
            winchPistonState = DISENGAGED;
        }
        
    }
    
    public boolean isEngaged() {
        return winchPistonState;
    }
    
    public void toggleBallSettler(boolean holdToggle) {
               
        ToggleBoolean holderStateToggle = new ToggleBoolean();
        
        holderStateToggle.set(holdToggle);
        
        if(holderStateToggle.get())
            holdState  = !holdState;
        
        if(holdState)
            ballHolder.set(DoubleSolenoid.Value.kForward);
        else
            ballHolder.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void holdBallSettler(boolean holdToggle, boolean shooterButton) {   
       
        ToggleBoolean ballSettlerHold = new ToggleBoolean();
        
        ballSettlerHold.set(shooterButton);
        
        if(ballSettlerHold.get())
            ballHolderTimer.reset();
            
        if(holdToggle)
            ballHolder.set(DoubleSolenoid.Value.kReverse);
        else if (!holdToggle && ballHolderTimer.get() > Constants.getDouble("HoldWaitTime"))
            ballHolder.set(DoubleSolenoid.Value.kForward);
        else
            ballHolder.set(DoubleSolenoid.Value.kReverse);
 
    }

    public boolean getBallSettlerState() {
            return ballSettlerHoldState;
    }

    private void log(Object aObject){
        System.out.println(String.valueOf(aObject));
    }


}
