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
   
    private final double SHOOTER_KP = 5e-5;
    private final double SHOOTER_KI = 1e-6;
    private final double SHOOTER_KD = 0;
    private final double SHOOTER_I_ZONE = 0;
    private final double FEED_FORWARD = 0.4;
    
    private double hoodDistance;
    private final double FLY_WHEEL_SPEED = ShooterCalculator.calculateRPM(limelight.getDistance());
    private double FLY_WHEEL_SPEED_MIN = FLY_WHEEL_SPEED + 20;
    private final double BALL_FEEDER_SPEED = 0.3;
    private final double SHOOTER_DEFAULT_SPEED = 0.7;
    private final  double drivetrainAngleThreshhold = 0.5;

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
        SmartDashboard.putNumber("shooter v", shooterEncoder.getVelocity());
        SmartDashboard.putNumber("Shooter pow", shooter.get());
    }

    private void aim() {
        if (limelight.targetFound()) {
            hoodDistance = limelight.getDistance();
        }
    }

    public void off() {
        this.curMode = ShooterMechanismModes.off;
    }

    private void executeOff() {
        shooterOFF();
        ballFeederOFF();
    }

    public void shoot() {
        this.curMode = ShooterMechanismModes.shoot;
    }

    private void executeShoot() {
        shooterON();
        hood.setPosition(ShooterCalculator.calculateAngle(limelight.getDistance()));
                if (shooterEncoder.getVelocity() < FLY_WHEEL_SPEED_MIN ) {
                    ballFeederON();
                } else {
                    ballFeederOFF();
                }
        // }
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
        shooter.set(-0.9);
    }
    

    private void shooterOFF() {
        shooter.set(0);
    }

    public double shooterVelocity(){
      return shooterEncoder.getVelocity();
    }
    
    public double shooterSetpoint(){
        return ShooterCalculator.calculateRPM(limelight.getDistance());
    }
}
