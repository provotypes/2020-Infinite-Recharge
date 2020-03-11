package frc.robot;

import java.util.Map;
import static java.util.Map.entry;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;

public class ShootingMechanism {
        
    private static ShootingMechanism instance;
    private LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();
    private TalonSRX ballFeeder = new TalonSRX(2);
    private CANSparkMax shooter = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax shooter_b = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    //These motor controllers have enocders in them
    private CANEncoder shooterEncoder = shooter.getEncoder();
    private CANPIDController pidController;
    private Timer shooterTimer = new Timer();

    private final double SHOOTER_KP = 0.0005;
    private final double SHOOTER_KI = 0.000001;
    private final double SHOOTER_KD = 0.0;
    private final double SHOOTER_I_ZONE = 2000;
    private final double FEED_FORWARD = 0.000015;
    
    private final double FLY_WHEEL_SPEED_THRESH = 20;
    private final double BALL_FEEDER_SPEED = -0.9;
    private final double FEEDER_IDLE_POWER = 0.1;


    private ShootingMechanism() {

        ballFeeder.setNeutralMode(NeutralMode.Brake);
        ballFeeder.configVoltageCompSaturation(11);
        ballFeeder.enableVoltageCompensation(true);

        shooter_b.follow(shooter, true);
        shooter.setIdleMode(IdleMode.kCoast);

        pidController = shooter.getPIDController();

        shooter.setSmartCurrentLimit(45);
        shooter_b.setSmartCurrentLimit(45);

        shooterTimer.start();

        pidController.setP(SHOOTER_KP);
        pidController.setI(SHOOTER_KI);
        pidController.setD(SHOOTER_KD);
        pidController.setIZone(SHOOTER_I_ZONE);
        pidController.setFF(FEED_FORWARD);
        pidController.setOutputRange(-1, 0);

        SmartDashboard.putNumber("set shooter dis", 0);
        SmartDashboard.putNumber("set shooter RPM", 0);
        SmartDashboard.putNumber("set shooter angle", 0);
        SmartDashboard.putBoolean("set Shooter Table val", false);
    }

    public static ShootingMechanism getInstance() {
        if (instance == null) {
            instance = new ShootingMechanism();
        }
            return instance;
    }

    enum ShooterMechanismModes {
        shoot,
        forceShoot,
        slowShoot,
        feederReverse,
        off; // upon release in teleop
    }

    private ShooterMechanismModes curMode = ShooterMechanismModes.off;
    
    final Map<ShooterMechanismModes, Runnable> shootingModes = Map.ofEntries(
                entry(ShooterMechanismModes.off, this::executeOff),
                entry(ShooterMechanismModes.shoot, this::executeShoot),
                entry(ShooterMechanismModes.forceShoot, this::executeForceShoot),
                entry(ShooterMechanismModes.slowShoot, this::executeSlowShoot),
                entry(ShooterMechanismModes.feederReverse, this::executeFeedeReverse)
    );

    public void update() {
        shootingModes.get(curMode).run();
        SmartDashboard.putNumber("Shooter Velocity", shooterEncoder.getVelocity());
        SmartDashboard.putNumber("Shooter pow", shooter.get());
        SmartDashboard.putNumber("shooter current", shooter.getOutputCurrent());

        if (SmartDashboard.getBoolean("set Shooter Table val", false)) {
            ShooterCalculator.setValues(
            (int)SmartDashboard.getNumber("set shooter dis", 0),
            SmartDashboard.getNumber("set shooter RPM", 0),
            SmartDashboard.getNumber("set shooter angle", 0)
            );

            SmartDashboard.putBoolean("set Shooter Table val", false);
        }
        
        SmartDashboard.putNumber("shooterTimer", shooterTimer.get());
        
    }

    public void off() {
        this.curMode = ShooterMechanismModes.off;
    }

    public void executeOff() {
        shooterOFF();
        ballFeederOFF();
    }

    public void shoot() {
        this.curMode = ShooterMechanismModes.shoot;
    }

    public void forceShoot() {
        this.curMode = ShooterMechanismModes.forceShoot;
    }

    public void slowShoot() {
        this.curMode = ShooterMechanismModes.slowShoot;
    }

    public void reverseFeeder() {
        this.curMode = ShooterMechanismModes.feederReverse;
    }

    private void executeShoot() {
        shooterON();
        if (shooterEncoder.getVelocity() < (-ShooterCalculator.calculateRPM(limelight.getDistance()) + FLY_WHEEL_SPEED_THRESH)) {
            ballFeederON();
        } else {
            ballFeederOFF();
        }
    }

    private void executeForceShoot() {
        shooterON();
        ballFeederON();
    }

    private void executeSlowShoot() {
        shooterSlow();
        ballFeederON();
    }

    private void executeFeedeReverse() {
        feederReverse();
        shooterOFF();
    }

    private void ballFeederON() {
        ballFeeder.set(ControlMode.PercentOutput, BALL_FEEDER_SPEED);
    }

    private void ballFeederOFF() {
        ballFeeder.set(ControlMode.PercentOutput, FEEDER_IDLE_POWER);
    }

    private void feederReverse() {
        ballFeeder.set(ControlMode.PercentOutput, -BALL_FEEDER_SPEED);

    }

    private void shooterON() {
        double flyWheelSpeed = -ShooterCalculator.calculateRPM(limelight.getDistance());
        pidController.setReference(flyWheelSpeed, ControlType.kVelocity);
        // shooter.set(-0.4);
    }

    private void shooterSlow() {
        shooter.set(-0.15);
    }

    private void shooterOFF() {
        shooter.set(0);
    }

    public double shooterVelocity() {
      return shooterEncoder.getVelocity();
    }
    
    public double shooterSetpoint() {
        return -ShooterCalculator.calculateRPM(limelight.getDistance());
    }
}
