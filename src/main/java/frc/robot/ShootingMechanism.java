package frc.robot;

import java.util.Map;
import static java.util.Map.entry;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;

import frc.robot.IntakeMechanism.IntakeMechanismModes;

public class ShootingMechanism {
        
    private static ShootingMechanism instance;
    private LimelightVisionTracking vision = LimelightVisionTracking.getInstance();
    
    private VictorSPX ballFeeder = new VictorSPX(1);
    private CANSparkMax shooter = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax shooter_b = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
    //These motor controllers have enocders in them
    private TalonSRX turret = new TalonSRX(1);
    private TalonSRX hood = new TalonSRX(2);
    private CANEncoder shooterEncoder = shooter.getEncoder();
    private CANPIDController pidController;
   
    private final double SHOOTER_KP = 5e-5;
    private final double SHOOTER_KI = 1e-6;
    private final double SHOOTER_KD = 0;
    private final double SHOOTER_I_ZONE = 0;
    private final double FEED_FORWARD = 0;
    
    private double turretAngle;
    private double hoodDistance;
    private final double FLY_WHEEL_SPEED = 3000;
    private double FLY_WHEEL_SPEED_MIN = FLY_WHEEL_SPEED - 200;
    private final double BALL_FEEDER_SPEED = 0.5;
    private final double SHOOTER_DEFAULT_SPEED = 0.7;

    private ShootingMechanism() {
        shooter_b.follow(shooter);
        shooter.setIdleMode(IdleMode.kCoast);
        pidController = shooter.getPIDController();

        pidController.setP(SHOOTER_KP);
        pidController.setI(SHOOTER_KI);
        pidController.setD(SHOOTER_KD);
        pidController.setIZone(SHOOTER_I_ZONE);
        pidController.setFF(FEED_FORWARD);
        pidController.setOutputRange(0, 1);
    }

    public static ShootingMechanism getInstance() {
        if(instance == null) {
            instance = new ShootingMechanism();
        }
        return instance;
        }

    enum ShooterMechanismModes {
        shoot,
        off; // upon release in teleop
    }

 private ShooterMechanismModes shooterMechanismModes = ShooterMechanismModes.off;

    public void update() {
	    final Map<ShooterMechanismModes, Runnable> shootingModes = Map.ofEntries(
			entry(ShooterMechanismModes.off, this::executeOff),
			entry(ShooterMechanismModes.shoot, this::executeShoot)
	    );
    }
    private void aim() {
        if (vision.targetFound()) {
            hoodDistance = vision.getDistance();
            turretAngle = vision.getHorizontalAngle();
        }
    }

    public void off() {
        this.shooterMechanismModes = shooterMechanismModes.off;
    }

    public void executeOff() {
        shooterOFF();
        ballFeederOFF();
    }

    public void shoot() {
        this.shooterMechanismModes = shooterMechanismModes.shoot;
        aim();
        startBallFeeder();
    }

    public void executeShoot() {
        shooterON();
        ballFeederON();
    }   

    public void startBallFeeder() {
        if (shooterEncoder.getVelocity() > FLY_WHEEL_SPEED_MIN ) {
            ballFeederON();
        } else {
            ballFeederOFF();
        }
    }

    public void ballFeederON() {
        ballFeeder.set(ControlMode.PercentOutput, BALL_FEEDER_SPEED);
    }

    public void ballFeederOFF() {

    }

    public void shooterON() {
        pidController.setReference(FLY_WHEEL_SPEED, ControlType.kVelocity);
    }

    public void shooterOFF() {

    }

}
