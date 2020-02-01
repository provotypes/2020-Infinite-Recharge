package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;
import com.revrobotics.*;
import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.easypath.EasyPathDrivetrain;

public class Drivetrain extends DifferentialDrive implements EasyPathDrivetrain {
    public static final double DISTANCE_PER_ROTATION = 1.0d/8.45d * 6.0d * Math.PI; // inches //Find Specific 2020 # later

    private static CANEncoder frontLeftEncoder;
    private static CANEncoder rearLeftEncoder;
    private static CANEncoder frontRightEncoder;
    private static CANEncoder rearRightEncoder;
  
    private static CANSparkMax frontLeft = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax rearLeft = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
	private	static CANSparkMax frontRight = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
	private	static CANSparkMax rearRight = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    
    private static SpeedControllerGroup leftGroup = new SpeedControllerGroup(frontLeft, rearLeft);
    private static SpeedControllerGroup rightGroup = new SpeedControllerGroup(frontRight, rearRight);

    private static Drivetrain instance;
    private static ADIS16470_IMU IMU = new ADIS16470_IMU();
    private static double kP; // figure out later

    private Drivetrain() {
        super(leftGroup, rightGroup);
        frontLeftEncoder = frontLeft.getEncoder();
        rearLeftEncoder = rearLeft.getEncoder();
        frontRightEncoder = frontRight.getEncoder();
        rearRightEncoder = rearRight.getEncoder();

        frontLeftEncoder.setPositionConversionFactor(DISTANCE_PER_ROTATION);
        rearLeftEncoder.setPositionConversionFactor(DISTANCE_PER_ROTATION);
        frontRightEncoder.setPositionConversionFactor(DISTANCE_PER_ROTATION);
        rearRightEncoder.setPositionConversionFactor(DISTANCE_PER_ROTATION);
        resetEncodersAndGyro();
        IMU.setYawAxis(IMUAxis.kY);
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
        frontLeftEncoder.setPosition(0.0d);
		rearLeftEncoder.setPosition(0.0d);
		frontRightEncoder.setPosition(0.0d);
		rearRightEncoder.setPosition(0.0d);
        IMU.reset();
    }

	public void setBrake() {
        
    }

 
	public void setCoast() {}

 
    public double getLeftEncoderDistance() {
		return (((frontLeftEncoder.getPosition() + rearLeftEncoder.getPosition()) / 2.0d));
	}

	public double getRightEncoderDistance() {
		return (((frontRightEncoder.getPosition() + rearRightEncoder.getPosition()) / 2.0d));
	}

	public void calibrateGyro() {
        IMU.calibrate();
    }

    public void safeArcade() {

    }

    public void antiTippingMechanism() {
        double turningValue = (IMU.getAngle()) * kP;
        tankDrive(turningValue, turningValue);
    }

    public void putSmartDashInfo() {
        SmartDashboard.putNumber("gyro y", IMU.getAngle());
        SmartDashboard.putData(IMU);
    }

}
