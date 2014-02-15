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
    ToggleBoolean winchStateToggle;
    ToggleBoolean winchShiftToggle;
    
    AnalogChannel winchPot;
    
    Timer winchShiftTimer;

    final boolean ENGAGED = true;
    final boolean DISENGAGED = false;
    boolean winchPistonState = DISENGAGED; //piston assumed to be engaged in gearbox in default state when robot is turned on

    final boolean EXTENDED = true;
    final boolean RETRACTED = false;
    boolean trussPistonState = RETRACTED; //piston assumed to be retracted in default state when robot is turned on
            
    double winchSetpoint;
    
    final int ENGAGE = 0;
    final int FULLY_ENGAGED = 1;  
    int setEngaged = FULLY_ENGAGED;
    
    //final int WIND_WINCH = 2;
    //int setWinchPosSeq = WIND_WINCH;
    
    public boolean firstRun = false; //used in disengageWinch
    
    double error;
    double lastError;
    double deltaError;
    double lastDeltaError;
    
    public Catapult()
    {   
        winchPot= new AnalogChannel(ElectricalConstants.WINCH_POT);
    
        rightWinchMotor = new Talon(ElectricalConstants.RIGHT_WINCH_PWM);
        leftWinchMotor = new Talon(ElectricalConstants.LEFT_WINCH_PWM);
        
        trussShotPiston = new DoubleSolenoid (ElectricalConstants.TRUSS_ENGAGE, ElectricalConstants.TRUSS_DISENGAGE);
        winchReleasePiston = new DoubleSolenoid ( ElectricalConstants.WINCH_ENGAGE, ElectricalConstants.WINCH_DISENGAGE);
        
        trussShotToggle = new ToggleBoolean();
        winchStateToggle = new ToggleBoolean();
        winchShiftToggle = new ToggleBoolean();
        
        winchShiftTimer = new Timer();
        winchShiftTimer.start();
        
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
        double error = setpoint-getWinchPot();
        
        if (error == 0){
            setWinchPWM(0);
        log("in setpoint == winchPot");
        }
        
        else if (Math.abs(error) > Constants.getDouble("bWinchPosTolerance")) {
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
            
        if (winchPistonState == DISENGAGED && (Math.abs(manualAdjustment) > 0.1 || presetOne || presetTwo)) //add more "or" statements as more presets are added
            engageWinch(true);
        
        if (Math.abs(manualAdjustment) > 0.5) {
            setWinchPWM(manualAdjustment);
            log("in manual");
            winchSetpoint = getWinchPot();
        }
        else {

            if (presetOne)
                winchSetpoint = Constants.getDouble("bWinchPosOne");
            else if (presetTwo) 
                winchSetpoint = Constants.getDouble("bWinchPosTwo");
            else
                winchSetpoint = getWinchPot();

            log("in preset" + winchSetpoint);
            setWinchPos(winchSetpoint);
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
        winchStateToggle.set(winchPistonToggleButton);
        
        if(winchStateToggle.get())
            winchPistonState = !winchPistonState;
        
        if(winchPistonState == DISENGAGED) 
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
        else 
            winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void toggleTrussPistonPos(boolean trussPistonToggleButton) {     
        trussShotToggle.set(trussPistonToggleButton);
        
        if(trussShotToggle.get())
            trussPistonState = !trussPistonState;
        
        if(trussPistonState) 
            trussShotPiston.set(DoubleSolenoid.Value.kForward);
        else 
            trussShotPiston.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void holdTrussPistonPos(boolean trussPistonButton) {
        if(trussPistonButton) {
            trussShotPiston.set(DoubleSolenoid.Value.kForward);
            trussPistonState = EXTENDED;
        }
        else {
            trussShotPiston.set(DoubleSolenoid.Value.kReverse);
            trussPistonState = RETRACTED;
        }
    }

//    public void engageWinch(boolean winchShift) {
//        winchShiftToggle.set(winchShift);
//        
//        if(winchPistonState == DISENGAGED && winchShiftToggle.get()){
//            setEngaged = ENGAGE;
//            winchShiftTimer.reset();
//        }
//        else
//            setEngaged = FULLY_ENGAGED;
//        
//        switch (setEngaged){
//            case ENGAGE:
//                setWinchPWM(1);//Constants.getDouble("bWinchShiftSpeed"));
//                winchReleasePiston.set(DoubleSolenoid.Value.kReverse);
//                
//                if (winchShiftTimer.get() > Constants.getDouble("bWinchShiftTime"))
//                    setEngaged = FULLY_ENGAGED;
//                break;
//            case FULLY_ENGAGED:
//                setWinchPWM(0);
//                winchShiftToggle.set(false);
//                winchPistonState = ENGAGED;
//                break;
//        }
//        
//        log("in engageWinch");
//        //winchPistonState = true; 
//    }
    
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
                    //setDisengaged = false;
                    returnVar = true;
                    
                }
            }
        
        }
        return returnVar;
    }

    public void disengageWinch(boolean disengage) {
        if(firstRun) {
            winchPistonState = ENGAGED;
            firstRun = false;
        }
        
        if(winchPistonState && disengage){
            winchReleasePiston.set(DoubleSolenoid.Value.kForward);
            winchPistonState = DISENGAGED;
        }
        
    }
    
    public boolean isEngaged() {
        return winchPistonState;
    }
    
    public boolean isTrussPistonExtended() {
        return trussPistonState;
    }
    
    private void log(Object aObject){
        System.out.println(String.valueOf(aObject));
    }


}
