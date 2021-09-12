package frc.robot;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopController {

    private static TeleopController instance;
   
    private ShootingMechanism shootingMech = ShootingMechanism.getInstance();
    private IntakeMechanism intakeMech = IntakeMechanism.getInstance();
    private ClimbingMechanism climber = ClimbingMechanism.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();
    private LogitechDriverController driverController = new LogitechDriverController(0);
    private LogitechOperatorController operatorController = new LogitechOperatorController(1);

    private boolean isHumanControlled = true;
 
    private final Supplier<Double> rotateMultiplierSupplier = () -> SmartDashboard.getNumber("Rotate Multiplier", 0.65);
    private final Supplier<Double> speedMultiplierSupplier = () -> SmartDashboard.getNumber("Speed Multiplier", 0.6);

    private Timer teleTimer = new Timer();
    // private static double SECONDS_IN_TELEOP = 105.0;
    private static double SECONDS_IN_TELEOP = 5.0;

    private TeleopController() {
        SmartDashboard.putNumber("Rotate Multiplier", -0.5);
        SmartDashboard.putNumber("Speed Multiplier", 0.6);
    }

    public static TeleopController getInstance() {
        if(instance == null) {
            instance = new TeleopController();
        }
        return instance;
    }

    public void TeleopInit() {
        System.out.println("Teleop has started!");

        operatorController.clear();
        driverController.clear();
    
        operatorController.bindButton(LogitechOperatorController.TRIGGER, this::limelightShooting);
        operatorController.bindButtonRelease(LogitechOperatorController.TRIGGER, () -> {isHumanControlled = true; shootingMech.off();});
        // operatorController.bindButtonToggle(LogitechOperatorController.TRIGGER, shootingMech.hoodPositioning(), shootingMech.);
        // operatorController.bindButtonToggle(LogitechOperatorController.TOP_RIGHT_TOP_BUTTON, 
        //             this::limelightForceShooting, () -> {isHumanControlled = true; shootingMech.off();});
        // operatorController.bindButtonToggle(LogitechOperatorController.BOTTOM_RIGHT_BASE_BUTTON,
        //             shootingMech::slowShoot, shootingMech::off);
        operatorController.bindButtonToggle(LogitechOperatorController.MIDDLE_RIGHT_BASE_BUTTON,
            intakeMech::reverseIndexer, intakeMech::off);
        operatorController.bindButtonToggle(LogitechOperatorController.THUMB_BUTTON, 
                 intakeMech::indexer, intakeMech::off);
        operatorController.bindButtonToggle(LogitechOperatorController.BOTTOM_LEFT_BASE_BUTTON,
            shootingMech::reverseFeeder, shootingMech::off);
        operatorController.bindButtonToggle(LogitechOperatorController.TOP_LEFT_BASE_BUTTON, 
                 intakeMech::indexerAndIntakes, intakeMech::intakeIdle);
        operatorController.bindButton(LogitechOperatorController.TOP_RIGHT_BASE_BUTTON, 
                 intakeMech::indexerAndIntakes/*, intakeMech::intakeIdle*/); 
        operatorController.bindButtonToggle(LogitechOperatorController.MIDDLE_LEFT_BASE_BUTTON, 
                 intakeMech::reverseEverything, intakeMech::intakeIdle);

        operatorController.bindButtonToggle(LogitechOperatorController.TOP_LEFT_TOP_BUTTON,
                     this::elevatorUp, climber::off);
        operatorController.bindButtonToggle(LogitechOperatorController.BOTTOM_LEFT_TOP_BUTTON,
                     climber::elevatorDown, climber::off);
        operatorController.bindButtonToggle(LogitechOperatorController.TOP_RIGHT_TOP_BUTTON, 
                    this::winchDown, climber::off);
        operatorController.bindButtonToggle(LogitechOperatorController.BOTTOM_RIGHT_TOP_BUTTON,
                    climber::winchUp, climber::off);
        
        driverController.bindAxes(LogitechDriverController.RIGHT_Y_AXIS, LogitechDriverController.LEFT_Y_AXIS, this::tank);
        driverController.bindButton(LogitechDriverController.RIGHT_BUMPER, () -> {this.tank(-0.9, -0.9);});
        driverController.bindButton(LogitechDriverController.LEFT_BUMPER, () -> {this.tank(0.9, 0.9);});

        isHumanControlled = true;
    }

    public void update() {
        driverController.run();
        operatorController.run();
    }

    public void resetTimer() {
        teleTimer.reset();
        teleTimer.start();
    }

    private void tank(double left, double right) {
        if (isHumanControlled) {
            double speedMultiplier = speedMultiplierSupplier.get();
            
            double leftOut = left * speedMultiplier;
            double rightOut = right * speedMultiplier;

            drivetrain.tankDrive(-rightOut, -leftOut, true);
        }
    }

    private void arcade(double speed, double rotation) {
        if (isHumanControlled) {
            double rotateMultiplier = rotateMultiplierSupplier.get();
            double speedMultiplier = speedMultiplierSupplier.get();
            
            double outSpeed = speedMultiplier * speed;
            // outSpeed = Math.pow(outSpeed, 3);
            double outRotation = rotation * rotateMultiplier;
            // outRotation = Math.pow(outRotation, 3);

            drivetrain.specialCurveDrive(outSpeed, -outRotation);
        }
    }

    private void limelightForceShooting() {
        double outSpeed = 0.3 * driverController.getLeftY();

        drivetrain.drvietrainAngleLineup(outSpeed);
        shootingMech.forceShoot();
    }

    private void limelightShooting() {
        if (limelight.targetFound()) {
            isHumanControlled = false;
            double outSpeed = 0.3 * driverController.getLeftY();
            drivetrain.drvietrainAngleLineup(outSpeed);
        }
        else {
            isHumanControlled = true;
        }
        
        shootingMech.shoot();
    }

    private void elevatorUp() {
        if (teleTimer.get() > SECONDS_IN_TELEOP) {
            climber.elevatorUp();
        }
    }

    private void winchDown() {
        if (teleTimer.get() > SECONDS_IN_TELEOP) {
            climber.winchDown();
        }
    }

}