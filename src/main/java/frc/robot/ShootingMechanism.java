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

import edu.wpi.first.wpilibj.Servo;
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
    public Servo hood = new Servo(0);
    private CANEncoder shooterEncoder = shooter.getEncoder();
    private CANPIDController pidController;
    private Timer shooterTimer = new Timer();
    private int lastHoodSet = 5;
    private double hoodTimeToPos = 0.0;

    // second per (10*percentage)
    private final double HOOD_SPEED = 3.5d / 4.06;

    private final double SHOOTER_KP = 0.0005;
    private final double SHOOTER_KI = 0.000001;
    private final double SHOOTER_KD = 0.0;
    private final double SHOOTER_I_ZONE = 2000;
    private final double FEED_FORWARD = 0.000015;
    
    private double hoodDistance;
    private final double FLY_WHEEL_SPEED_THRESH = 20;
    private final double BALL_FEEDER_SPEED = 0.6;
    private final double SHOOTER_DEFAULT_SPEED = 0.7;
    private final double DRIVE_TRAIN_THRESHOLD = 0.5;
    private final double MAX_HOOD_POSITION = 0.8;
    private final double MIN_HOOD_POSITION = 0.2;
    private final double IDLE_HOOD_POSITION = 0.5;


    private ShootingMechanism() {

        ballFeeder.setNeutralMode(NeutralMode.Brake);

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
        SmartDashboard.putNumber("hood old", lastHoodSet);
        SmartDashboard.putNumber("hoodTimeToPos", hoodTimeToPos);
        
    }

    public void hoodPositioning() {
        if (limelight.targetFound()) {
            hoodDistance = limelight.getDistance();
            double hoodPosition = ShooterCalculator.calculateAngle(hoodDistance);
            if (hoodPosition > MAX_HOOD_POSITION) {
                hoodPosition = MAX_HOOD_POSITION;
            } else if (hoodPosition < MIN_HOOD_POSITION) {
                hoodPosition = MIN_HOOD_POSITION;
            }
            hood.setPosition(hoodPosition);

            int intHoodSet = (int)(hoodPosition * 10);
            if (intHoodSet != lastHoodSet) {
                hoodTimeToPos = HOOD_SPEED * Math.abs(intHoodSet - lastHoodSet);

                // if (shooterTimer.get() > hoodTimeToPos) {
                //     hoodTimeToPos = HOOD_SPEED * Math.abs(intHoodSet - lastHoodSet);
                // }
                // else {
                //     hoodTimeToPos = (HOOD_SPEED * Math.abs(intHoodSet - lastHoodSet)) - (hoodTimeToPos - shooterTimer.get());
                // }
                
                shooterTimer.reset();
                shooterTimer.start();
                lastHoodSet = intHoodSet;
            }
        }
        else {
            hood.setPosition(MIN_HOOD_POSITION);
        }
    } 

    public void off() {
        this.curMode = ShooterMechanismModes.off;
    }

    public void executeOff() {
        shooterOFF();
        ballFeederOFF();
        hood.setPosition(IDLE_HOOD_POSITION);
        lastHoodSet = (int)(IDLE_HOOD_POSITION * 10);
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
        hoodPositioning();
        if (shooterEncoder.getVelocity() < (-ShooterCalculator.calculateRPM(limelight.getDistance()) + FLY_WHEEL_SPEED_THRESH)) {
            if (shooterTimer.get() > hoodTimeToPos) {
                ballFeederON();
            }
        } else {
            ballFeederOFF();
            
        }
    }

    private void executeForceShoot() {
        shooterON();
        hoodPositioning();
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
        ballFeeder.set(ControlMode.PercentOutput, -BALL_FEEDER_SPEED);
    }

    private void ballFeederOFF() {
        ballFeeder.set(ControlMode.PercentOutput, 0);
    }

    private void feederReverse() {
        ballFeeder.set(ControlMode.PercentOutput, BALL_FEEDER_SPEED);

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
