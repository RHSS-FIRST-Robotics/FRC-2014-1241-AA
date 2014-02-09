    /**
    *
    * @author Rohan K.
    */      

    package theory6.subsystems;

    import edu.wpi.first.wpilibj.Encoder;
    import theory6.utilities.TheoryGyro;
    import theory6.main.ElectricalConstants;
    import theory6.utilities.JoystickScaler;
    import edu.wpi.first.wpilibj.Talon;

    public class DriveTrain 
    {
        static DriveTrain inst = null;

        Encoder leftDriveEncoder;
        Encoder rightDriveEncoder;

        Talon leftDriveBackAndFront;
        Talon leftDriveTop;
        
        Talon rightDriveBackAndFront;
        Talon rightDriveTop;
        
        TheoryGyro driveGyro;

        JoystickScaler leftAnalogScaler = new JoystickScaler();
        JoystickScaler rightAnalogScaler = new JoystickScaler();

        public DriveTrain()
        {
            driveGyro = new TheoryGyro(ElectricalConstants.DRIVE_GYRO_PORT);

            leftDriveEncoder = new Encoder(ElectricalConstants.LEFT_DRIVE_ENC_A, 
                                           ElectricalConstants.LEFT_DRIVE_ENC_B, 
                                           ElectricalConstants.leftDriveTrainEncoderReverse, 
                                           Encoder.EncodingType.k4X);
            leftDriveEncoder.setDistancePerPulse(ElectricalConstants.driveEncoderDistPerTick);  
            leftDriveEncoder.start();

            rightDriveEncoder = new Encoder(ElectricalConstants.RIGHT_DRIVE_ENC_A, 
                                            ElectricalConstants.RIGHT_DRIVE_ENC_B, 
                                            ElectricalConstants.rightDriveTrainEncoderReverse, 
                                            Encoder.EncodingType.k4X);        
            rightDriveEncoder.setDistancePerPulse(ElectricalConstants.driveEncoderDistPerTick); 
            rightDriveEncoder.start();
        
            
            leftDriveBackAndFront = new Talon(ElectricalConstants.FRONT_AND_BACK_LEFT_DRIVE_PWM);
            leftDriveTop = new Talon(ElectricalConstants.TOP_LEFT_DRIVE_PWM);
            
            rightDriveBackAndFront = new Talon(ElectricalConstants.FRONT_AND_BACK_RIGHT_DRIVE_PWM);
            rightDriveTop = new Talon(ElectricalConstants.TOP_RIGHT_DRIVE_PWM);

            driveGyro.initGyro();
            
        }
        public static DriveTrain getInstance() 
        {
            if(inst == null) 
            {
                inst = new DriveTrain();
            }
            return inst;
        }
        
        public void setLeftPWM(double speed) 
        {
            if (Math.abs(speed) < 0.05 ) 
                speed = 0.0;

            if (Math.abs(speed) < 0.05 ) 
            {
                speed = 0.0;
            }
            
        leftDriveBackAndFront.set(speed);
        leftDriveTop.set(speed);
        }
        
        public void setRightPWM(double speed)

        {
            if (Math.abs(speed) < 0.05 ) 
            {
                speed = 0.0;
            }
        rightDriveBackAndFront.set(speed);
        rightDriveTop.set(speed);
        }


        public void tankDrive (double leftJoy, double rightJoy, int scaledPower) 
        {
            setLeftPWM(leftAnalogScaler.scaleJoystick(leftJoy, scaledPower));
            setRightPWM(rightAnalogScaler.scaleJoystick(rightJoy, scaledPower));
        }

    
        /************************ENCODER FUNCTIONS************************/

        public void stopEncoders() 
        {
            leftDriveEncoder.stop();
            rightDriveEncoder.stop();
        }

        public double getLeftEncoderDist() 
        {
            return leftDriveEncoder.getDistance();
        }

        public double getRightEncoderDist() 
        {
            return rightDriveEncoder.getDistance();
        }

        public double getLeftEncoderRaw() 
        {
            return leftDriveEncoder.getRaw();
        }

        public double getRightEncoderRaw() 
        {
            return rightDriveEncoder.getRaw();
        }

        public double getLeftEncoderRate() 
        {
            return leftDriveEncoder.getRate();
        }

        public double getRightEncoderRate() 
        {
            return rightDriveEncoder.getRate(); 
        }

        public void resetEncoders()
        {
            leftDriveEncoder.reset();
            rightDriveEncoder.reset();
        }
    /************************GYRO FUNCTIONS**************************/
    public double getGyroAngle()
    {
        return (driveGyro.getAngle() / 180)*180.0;
    }
    
    public void resetGyro()
    {
        driveGyro.reset();
    }
    public void recalibrateGyro()
    {
        driveGyro.initGyro();
    }
}


