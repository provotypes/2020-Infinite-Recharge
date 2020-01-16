package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU;

import edu.wpi.first.wpilibj.AnalogGyro;
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
    private static int DISTANCE_PER_PULSE = 1;
    private static ADIS16470_IMU IMU = new ADIS16470_IMU();
    private static AnalogGyro gyro = new AnalogGyro(1);
    private static double targetPosition = 0.0;
    private static double kP; //figure out later
    
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
        // return IMU.getAngle();
        return 0;
    }

    @Override
    public void resetEncodersAndGyro() {
       leftEncoder.reset();
       rightEncoder.reset();
    //    IMU.reset();
    }

	public void setBrake() {
        
    }

 
	public void setCoast() {}

 
	public double getLeftEncoderDistance() {
        return leftEncoder.getDistance();
    }


    public double getRightEncoderDistance() {
        return rightEncoder.getDistance();
	}


	public void calibrateGyro() {
        // IMU.calibrate();
    }

    public void safeArcade() {

    }
// also, target position is not necessary since it is always 0 

// continue working on this when its not 12am
///////////////////////////No ONE MESS With this code///////////////////////////////////////////////
    public static void update() {
       // if(IMU.getAngle() <= whatever angle threshhold that will tip us) {
        double turningValue = (targetPosition - IMU.getAngle()) * kP;
       //Speed Controller Groups are confusing right now as we don't have a good design of the robot.
        leftGroup.set(turningValue);
        rightGroup.set(turningValue);
     //   } else if(IMU.getAngle() >= whatever value that will tip us backwards) {
//              repeat process
     //}

    }
}
