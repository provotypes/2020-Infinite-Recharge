package frc.robot;

import java.util.Map;
import static java.util.Map.entry;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.hal.sim.DIOSim;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import frc.robot.IntakeMechanism.IntakeMechanismModes;

public class ShootingMechanism {
        
    private static ShootingMechanism instance;
    private LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();
    private TalonSRX ballFeeder = new TalonSRX(2);
    private CANSparkMax shooter = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
    // private CANSparkMax shooter_b = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    //These motor controllers have enocders in them
    private Servo hood = new Servo(0);
    private CANEncoder shooterEncoder = shooter.getEncoder();
    private CANPIDController pidController;
   
    private final double SHOOTER_KP = 0.0005;
    private final double SHOOTER_KI = 0.000001;
    private final double SHOOTER_KD = 0.0;
    private final double SHOOTER_I_ZONE = 2000;
    private final double FEED_FORWARD = 0.000015;
    
    private double hoodDistance;
    private final double FLY_WHEEL_SPEED = -1000;
    private double FLY_WHEEL_SPEED_MIN = FLY_WHEEL_SPEED + 20;
    private final double BALL_FEEDER_SPEED = 0.5;
    private final double SHOOTER_DEFAULT_SPEED = 0.7;
    private final double DRIVE_TRAIN_THRESHOLD = 0.5;
    private final double MAX_HOOD_POSITION = 0.8;
    private final double MIN_HOOD_POSITION = 0.2;


    private ShootingMechanism() {

        ballFeeder.setNeutralMode(NeutralMode.Brake);

        // shooter_b.follow(shooter);
        shooter.setIdleMode(IdleMode.kCoast);
        pidController = shooter.getPIDController();

        pidController.setP(SHOOTER_KP);
        pidController.setI(SHOOTER_KI);
        pidController.setD(SHOOTER_KD);
        pidController.setIZone(SHOOTER_I_ZONE);
        pidController.setFF(FEED_FORWARD);
        pidController.setOutputRange(-1, 1);
    }

    public static ShootingMechanism getInstance() {
        if (instance == null) {
            instance = new ShootingMechanism();
        }
            return instance;
    }

    enum ShooterMechanismModes {
        shoot,
        off; // upon release in teleop
    }

    private ShooterMechanismModes curMode = ShooterMechanismModes.off;
    
    final Map<ShooterMechanismModes, Runnable> shootingModes = Map.ofEntries(
                entry(ShooterMechanismModes.off, this::executeOff),
                entry(ShooterMechanismModes.shoot, this::executeShoot)
    );

    public void update() {
        shootingModes.get(curMode).run();
        SmartDashboard.putNumber("Shooter Velocity", shooterEncoder.getVelocity());
        SmartDashboard.putNumber("Shooter pow", shooter.get());
    }

    public void hoodPositioning() {
        if (limelight.targetFound()) {
            hoodDistance = limelight.getDistance();
            double hoodPosition = ShooterCalculator.calculateAngle(hoodDistance);
            if (hoodPosition > MAX_HOOD_POSITION) {
                hood.setPosition(MAX_HOOD_POSITION);
            } else if (hoodPosition < MIN_HOOD_POSITION) {
                hood.setPosition(MIN_HOOD_POSITION);
            } else {
                hood.setPosition(hoodPosition);
            }
        }
    } 

    public void off() {
        this.curMode = ShooterMechanismModes.off;
    }

    private void executeOff() {
        shooterOFF();
        ballFeederOFF();
        hood.setPosition(MIN_HOOD_POSITION);
    }

    public void shoot() {
        this.curMode = ShooterMechanismModes.shoot;
    }

    private void executeShoot() {
        shooterON();
        hoodPositioning();
        if (shooterEncoder.getVelocity() < FLY_WHEEL_SPEED_MIN ) {
            ballFeederON();
        } else {
            ballFeederOFF();
        }
      
    }   

    private void ballFeederON() {
        ballFeeder.set(ControlMode.PercentOutput, -BALL_FEEDER_SPEED);
    }

    private void ballFeederOFF() {
        ballFeeder.set(ControlMode.PercentOutput, 0);
    }

    private void shooterON() {
        double flyWheelSpeed = ShooterCalculator.calculateRPM(limelight.getDistance());
        // pidController.setReference(FLY_WHEEL_SPEED, ControlType.kVelocity);
        shooter.set(SHOOTER_DEFAULT_SPEED);
    }
    

    private void shooterOFF() {
        shooter.set(0);
    }

    public double shooterVelocity() {
      return shooterEncoder.getVelocity();
    }
    
    public double shooterSetpoint() {
        return ShooterCalculator.calculateRPM(limelight.getDistance());
    }
}
