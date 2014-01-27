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
    double pGain;
    double iGain;
    double dGain;
    
    double pOut;
    double iOut;
    double dOut;
    
    double error;
    double errorSum = 0;
    double lastError = 0;
    double dProcessVar;
    
    public PIDController(double p, double i, double d) {   
        errorSum = 0;       //initialize errorSum to 0
        lastError = 0;      //initialize lastError to 0 
        pGain = p;
        iGain = i;
        dGain = d;
    }
            
    public void resetIntegral() {
        errorSum = 0.0;
    }
    
    public void resetDerivative() {
       lastError = 0.0;
    }
    
    public void changePIDGains(double kP, double kI, double kD) {
        pGain = kP;
        iGain = kI;
        dGain = kD;
    }
    
    public double calcPID(double setPoint, double currentValue) {
        error = setPoint - currentValue;
        pOut = pGain * error;
        
        errorSum += error;
        iOut = iGain * errorSum;
        
        dProcessVar = (error - lastError);
        dOut = dGain * dProcessVar;
        
        lastError = error;
        
        return pOut + iOut + dOut;
    }
}
