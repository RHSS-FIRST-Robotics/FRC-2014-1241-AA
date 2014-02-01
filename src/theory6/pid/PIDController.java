/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.pid;

/**
 *
 * @author Team 1241
 * Simple PID Controller that assumes regular loop intervals 
 */
public class PIDController {
    double pGain = 0;
    double iGain = 0;
    double dGain = 0;
    double ffGain = 0;
    
    double pOut = 0;
    double iOut = 0;
    double dOut = 0;
    double ffOut = 0;
    
    double out = 0;
    
    double error = 0;
    double errorSum = 0;
    double lastError = 0;
    double dProcessVar = 0;
    
    double minIError = 10.0;
    
    double setpoint = 0;
    
    double onTargetError = 4.0, onTargetDeltaError = 0.05;
    
    double lastDeltaError = 0;
    
    public PIDController(double p, double i, double d, double ff) {   
        errorSum = 0;       //initialize errorSum to 0
        lastError = 0;      //initialize lastError to 0 
        
        pGain = p;
        iGain = i;
        dGain = d;
        
        ffGain = ff;
    }
    
    public void setEpsilons(double onTargetError, double onTargetDeltaError) {
        this.onTargetError = onTargetError;
        this.onTargetDeltaError = onTargetDeltaError;
    }
      
    public double updateOutput(double currentValue) {
        error = setpoint - currentValue;
        pOut = pGain * error;
        
        if (Math.abs(error) < minIError) {
            errorSum += error;
        }
        
        errorSum += error;
        iOut = iGain * errorSum;
        
        dProcessVar = (error - lastError);
        dOut = dGain * dProcessVar;
        
        ffOut = ffGain*setpoint;
        
        lastError = error;

        out = pOut + iOut + dOut + ffOut;
        
        return out;
    }
     
    public void changeGains(double kP, double kI, double kD, double kFF) {
        pGain = kP;
        iGain = kI;
        dGain = kD;
        ffGain = kFF;
    }
    
    public void setGoal(double goal) {
        errorSum = 0;
        this.setpoint = goal;
        out = 0;
    }

    public void setGoalRaw(double goal) {
        this.setpoint = goal;
    }

    public double getGoal() {
        return this.setpoint;
    }

    void setMinI(double minI) {
        this.minIError = minI;
    }
  
    public boolean onTarget() {
        boolean done = (Math.abs(error) < onTargetError) &&
                (Math.abs(lastDeltaError) < onTargetDeltaError);
        if (done) {
          System.out.println("DONE");
        }
        return done;
  }
}
