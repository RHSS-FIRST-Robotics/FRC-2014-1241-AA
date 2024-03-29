/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package theory6.main;

/**
 *
 * @author Sagar
 */
public class ElectricalConstants {
    
    //**************************************************************************
    //****************************** PWMs **************************************
    //**************************************************************************        
    
    // 1, 2, 3, 4 LEFT SIDE 
    // 5, 6, 7, 8 RIGHT SIDE 

    public static final int FRONT_AND_BACK_LEFT_DRIVE_PWM       = 1; 
    public static final int TOP_LEFT_DRIVE_PWM                  = 2;
    
    public static final int FRONT_AND_BACK_RIGHT_DRIVE_PWM      = 7; 
    public static final int TOP_RIGHT_DRIVE_PWM                 = 10;
    
    public static final int LEFT_SIDE_INTAKE_PWM                = 4;
    public static final int RIGHT_SIDE_INTAKE_PWM               = 5;
    
    public static final int LEFT_WINCH_PWM                      = 6;
    public static final int RIGHT_WINCH_PWM                     = 3;

    //**************************************************************************
    //***************************Analog Sensors*********************************
    //**************************************************************************

    public static final int DRIVE_GYRO_PORT                     = 1;
    public static final int WINCH_POT                           = 2;

    //**************************************************************************
    //*************************** ENCODERS *************************************
    //**************************************************************************
 
    public static final int LEFT_DRIVE_ENC_A                    = 13; 
    public static final int LEFT_DRIVE_ENC_B                    = 14; 
    public static final int RIGHT_DRIVE_ENC_A                   = 3; 
    public static final int RIGHT_DRIVE_ENC_B                   = 4;     

    //**************************************************************************
    //*************************** Digital Sensors ******************************
    //**************************************************************************
  
    public static final int COMPRESSOR_PRESSURE_SENSOR          = 5;
    public static final int CATAPULT_LIMIT_SWITCH               = 6;

    //******************UPDATE THESE CONSTANTS WITH REAL ROBOT******************
    public static final int driveWheelRadius = 2;//wheel radius in inches
    public static final int pulsePerRotation = 256; //encoder pulse per rotation
    public static final double gearRatio = 1/1; //ratio between wheel and encoder
    public static final double driveEncoderPulsePerRot = pulsePerRotation*gearRatio; //pulse per rotation * gear ratio
    public static final double driveEncoderDistPerTick =(Math.PI*2*driveWheelRadius)/driveEncoderPulsePerRot;
    public static final boolean rightDriveTrainEncoderReverse = true; //true on real
    public static final boolean leftDriveTrainEncoderReverse = true; //true on real

   //***************************************************************************
   //*************************** Pneumatics ************************************
   //***************************************************************************
    public static final int INTAKE_DOWN                         = 1;
    public static final int INTAKE_UP                           = 2;
    public static final int WINCH_ENGAGE                        = 3;
    public static final int WINCH_DISENGAGE                     = 4;
    public static final int HOLD_ENGAGE                         = 5;
    public static final int HOLD_DISENGAGE                      = 6;     

   //***************************************************************************
   //*************************** Relays ****************************************
   //***************************************************************************
    
    public static final int COMPRESSOR_RELAY                    = 1;

}
