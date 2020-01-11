package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.easypath.EasyPathDrivetrain;

public class Drivetrain extends DifferentialDrive implements EasyPathDrivetrain{
    private static Encoder leftEncoder;
    private static Encoder rightEncoder;
    private static SpeedControllerGroup leftGroup;
    private static SpeedControllerGroup rightGroup;
    private static Drivetrain instance;
    private static final int DISTANCE_PER_PULSE = 1;
    private static ADIS16470_IMU IMU = new ADIS16470_IMU();
    
    private Drivetrain() {
        super(leftGroup, rightGroup);
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        resetEncodersAndGyro();
    }
    
    public static Drivetrain getInstance() {
        if(instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    @Override
    public void setLeftRightDriveSpeed(double left, double right) {
        tankDrive(left, right);
    }

    @Override
    public double getInchesTraveled() {
        return ((getLeftEncoderDistance() + getRightEncoderDistance()) / 2);
    }

    @Override
    public double getCurrentAngle() {
        return IMU.getAngle();
    }

    @Override
    public void resetEncodersAndGyro() {
       leftEncoder.reset();
       rightEncoder.reset();
       IMU.reset();
    }

	public void setBrake() {}

 
	public void setCoast() {}

 
	public double getLeftEncoderDistance() {
        return leftEncoder.getDistance();
    }


    public double getRightEncoderDistance() {
        return rightEncoder.getDistance();
	}


	public void calibrateGyro() {
        IMU.calibrate();
    }

    public void safeArcade() {

    }

    public static void update() {
        
    }
}
