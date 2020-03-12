package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU.ADIS16470CalibrationTime;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.easypath.EasyPathDrivetrain;

public class Drivetrain extends DifferentialDrive implements EasyPathDrivetrain {
   
    public static final double DISTANCE_PER_ROTATION = 1.0d/8.0d * 6.1d * Math.PI; // inches
    private static CANEncoder frontLeftEncoder;
    private static CANEncoder rearLeftEncoder;
    private static CANEncoder frontRightEncoder;
    private static CANEncoder rearRightEncoder;
  
    private static CANSparkMax frontLeft = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax rearLeft = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
	private static CANSparkMax frontRight = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
	private static CANSparkMax rearRight = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
     
    private static SpeedControllerGroup leftGroup = new SpeedControllerGroup(frontLeft, rearLeft);
    private static SpeedControllerGroup rightGroup = new SpeedControllerGroup(frontRight, rearRight);

    private static Drivetrain instance;
    private IMUAngleTracker IMU = new IMUAngleTracker();
    private double xP;
    private final double MIN_POWER = 0.06;
    private final double TURN_MIN_ANGLE_DEGREES = 1;
      
    LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();

    private static double kP = 0.01;
    private static double turningkP = 0.01;

    private Drivetrain() {
		super(leftGroup, rightGroup);

		setCoast();

		rearLeft.setOpenLoopRampRate(0.6);
		frontLeft.setOpenLoopRampRate(0.6);
		rearRight.setOpenLoopRampRate(0.6);
		frontRight.setOpenLoopRampRate(0.6);
		
		leftGroup.setInverted(false);
		rightGroup.setInverted(false);

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
        

        SmartDashboard.putNumber("drivetrain_kP", kP);
        SmartDashboard.putNumber("turn_kP", 0);
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
        rearLeft.setIdleMode(IdleMode.kBrake);
		frontLeft.setIdleMode(IdleMode.kCoast);
		rearRight.setIdleMode(IdleMode.kBrake);
		frontRight.setIdleMode(IdleMode.kCoast);
    }

 
	public void setCoast() {
		rearLeft.setIdleMode(IdleMode.kCoast);
		frontLeft.setIdleMode(IdleMode.kCoast);
		rearRight.setIdleMode(IdleMode.kCoast);
		frontRight.setIdleMode(IdleMode.kCoast);

	}

 
    public double getLeftEncoderDistance() {
		return (((frontLeftEncoder.getPosition() + rearLeftEncoder.getPosition()) / 2.0d));
	}

	public double getRightEncoderDistance() {
		return -(((frontRightEncoder.getPosition() + rearRightEncoder.getPosition()) / 2.0d));
	}

	public void calibrateGyro() {
        IMU.configCalTime(ADIS16470CalibrationTime._8s);
        IMU.calibrate();
    }

    public void safeArcade(double speed, double turn) {
        arcadeDrive(speed, turn, true);
    }

    /** 
     * cuvature drive with auto quick turn at slow speeds
     */
    public void specialCurveDrive(double speed, double turn) {
        double outSpeed = speed;
        double outTurn = turn;
        boolean quickTurn = false;

        double gyroRate = IMU.getRate();
        if ((inRange(outTurn, -0.1, 0.1)) && (!inRange(gyroRate, -1, 1))) {
            if (inRange(outSpeed, -0.1, 0.1)) {
                // outTurn += (gyroRate * 0.003);
            }
            // else if (inRange(outSpeed, -0.4, 0.4)) {
            //     outTurn += (gyroRate * 0.004) * (Math.abs(outSpeed) * 7);
            // }
            else {
                outTurn += (gyroRate * 0.0035) * (Math.abs(outSpeed) * 3);
            }
            SmartDashboard.putBoolean("turnfix", true);
            SmartDashboard.putNumber("turnFix turn", outTurn);
        }
        else {
            SmartDashboard.putBoolean("turnfix", false);
        }

        if (outSpeed < 0.1 && outSpeed > -0.1) { // cuvature drive with auto quick turn at slow speeds
            quickTurn =  true;
        }
        else {
            quickTurn = false;
        }

        super.curvatureDrive(outSpeed, outTurn, quickTurn);
    }

    public void drvietrainAngleLineup(double xSpeed) {
        kP = SmartDashboard.getNumber("drivetrain_kP", 0);
        double outputValue = 0.0;
        double tx = limelight.getHorizontalAngle();

        outputValue = -tx * kP;

        if (inRange(outputValue, -MIN_POWER, MIN_POWER)) {
            outputValue = MIN_POWER * -Math.signum(tx);
        }

        if ((tx > TURN_MIN_ANGLE_DEGREES) && (tx < -TURN_MIN_ANGLE_DEGREES)) {
            outputValue = 0;
        }

        arcadeDrive(xSpeed, -outputValue, false);
        SmartDashboard.putNumber("Limelight HorizontalAngle",  limelight.getHorizontalAngle());
    }

    public void drivetrainTurn(double xSpeed, double targetAngle){
        turningkP = SmartDashboard.getNumber("turn_kP", 0);

        double turnPower = (getCurrentAngle() - targetAngle) * turningkP;

         if (inRange(turnPower, -MIN_POWER, MIN_POWER)) {
            turnPower = MIN_POWER * Math.signum(turnPower);
        }

        if (inRange(getCurrentAngle() - targetAngle, -TURN_MIN_ANGLE_DEGREES, TURN_MIN_ANGLE_DEGREES)) {
            turnPower = 0;
        }

        if (turnPower > 0.5){
            turnPower = 0.5;
        } else if (turnPower < -0.5){
            turnPower = -0.5;
        }

        arcadeDrive(xSpeed, turnPower, false); 
        
    }

    public void antiTippingMechanism() {
        double turningValue = (IMU.getAngle()) * xP;
        tankDrive(turningValue, turningValue);
    }

    public void putSmartDashInfo() {
        SmartDashboard.putNumber("ADIS default angle (Z)", IMU.getAngle());
        SmartDashboard.putNumber("Drivetrain avg", getInchesTraveled());
        SmartDashboard.putNumber("Drivetrain left", getLeftEncoderDistance());
        SmartDashboard.putNumber("Drivetrain right", getRightEncoderDistance());
        // SmartDashboard.putNumber("wraper X", IMU.getXAngle());
        // SmartDashboard.putNumber("wraper Y", IMU.getYAngle());
        // SmartDashboard.putNumber("wraper Z", IMU.getZAngle());
    }

    /**
     * 
     * @param input input value
     * @param low lower bound
     * @param high upper bound
     * @return if input is between low and high (inclusive)
     */
    private boolean inRange(double input, double low, double high) {
        if ((input >= low) && (input <= high)) {
            return true;
        }
        else {
            return false;
        }
    }

}
