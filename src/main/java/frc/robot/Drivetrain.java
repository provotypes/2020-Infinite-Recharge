package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.analog.adis16470.frc.ADIS16470_IMU.ADIS16470CalibrationTime;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;
import com.revrobotics.*;
import com.revrobotics.CANEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
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
  
    private static CANSparkMax frontLeft = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax rearLeft = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
	private static CANSparkMax frontRight = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
	private static CANSparkMax rearRight = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
     
    private static SpeedControllerGroup leftGroup = new SpeedControllerGroup(frontLeft, rearLeft);
    private static SpeedControllerGroup rightGroup = new SpeedControllerGroup(frontRight, rearRight);

    private static Drivetrain instance;
    private IMUAngleTracker IMU = new IMUAngleTracker();
    private double xP;
    private final double MIN_POWER = 0.04;
    private final double MIN_ANGLE_THRESHOLD = 2;
    LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();

    private static double kP = 0.01;

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
        IMU.setYawAxis(IMUAxis.kZ);
        // IMU.configCalTime(ADIS16470CalibrationTime._64s);

        SmartDashboard.putNumber("drivetrain_kP", kP);
        // IMU.calibrate();
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
        IMU.configCalTime(ADIS16470CalibrationTime._64s);
        // IMU.calibrate();
        IMU.calibrate();
    }

    public void safeArcade(double speed, double turn) {
        arcadeDrive(speed, turn, true);
    }

    public void drvietrainAngleLineup(){
        kP = SmartDashboard.getNumber("drivetrain_kP", 0);
        double outputValue = 0.0;
        double tx = limelight.getHorizontalAngle();

        if (tx > 1.0) {
            outputValue = (-tx * kP) - MIN_POWER;
        } else if (tx < 1.0) {
            outputValue = (-tx * kP) + MIN_POWER;
        }

        arcadeDrive(0, -outputValue, false);
        SmartDashboard.putNumber("Limelight HorizontalAngleThing",  limelight.getHorizontalAngle());
    }

    public void antiTippingMechanism() {
        double turningValue = (IMU.getAngle()) * xP;
        tankDrive(turningValue, turningValue);
    }

    public void putSmartDashInfo() {
        SmartDashboard.putNumber("ADIS default angle (Z)", IMU.getAngle());
        SmartDashboard.putNumber("wraper X", IMU.getXAngle());
        SmartDashboard.putNumber("wraper Y", IMU.getYAngle());
        SmartDashboard.putNumber("wraper Z", IMU.getZAngle());
    }

}
