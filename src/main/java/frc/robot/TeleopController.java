package frc.robot;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopController {

    private static TeleopController instance;
   
    ShootingMechanism shootingMech = ShootingMechanism.getInstance();
    IntakeMechanism intakeMech = IntakeMechanism.getInstance();
    LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();
    ControlPanelMechanism controlPanel = ControlPanelMechanism.getInstance();
    ClimbingMechanism climber = ClimbingMechanism.getInstance();
    Drivetrain drivetrain = Drivetrain.getInstance();
    private LogitechDriverController driverController = new LogitechDriverController(0);
    private LogitechOperatorController operatorController = new LogitechOperatorController(1);

    private boolean isHumanControlled = true;
 
    private final Supplier<Double> rotateMultiplierSupplier = () -> SmartDashboard.getNumber("Rotate Multiplier", 0.65);
    private final Supplier<Double> speedMultiplierSupplier = () -> SmartDashboard.getNumber("Speed Multiplier", 0.65);

    private TeleopController() {
        SmartDashboard.putNumber("Rotate Multiplier", 0.65);
        SmartDashboard.putNumber("Speed Multiplier", 0.65);
    }

    public static TeleopController getInstance() {
        if(instance == null) {
            instance = new TeleopController();
        }
        return instance;
    }

    public void TeleopInit() {
        System.out.println("Teleop has started!");
        // Pick between buttons 3,4,5,6 for the climber. It would be best if they weren't in the base of the controller.
        // We don't want to hit the climber button by accident. Maybe do something like press both 3,4 at the same time?
        // operatorController.bindButtonToggle(LogitechOperatorController.TRIGGER, 
        //          this::limelightShooting, () -> {isHumanControlled = true;});
        operatorController.bindButton(LogitechOperatorController.TRIGGER, this::limelightShooting);
        operatorController.bindButtonRelease(LogitechOperatorController.TRIGGER, () -> {isHumanControlled = true; shootingMech.off();});
        operatorController.bindButtonToggle(LogitechOperatorController.THUMB_BUTTON, 
                 intakeMech::indexer, intakeMech::off); 
        operatorController.bindButtonToggle(LogitechOperatorController.TOP_LEFT_BASE_BUTTON, 
                 intakeMech::indexerAndIntakes, intakeMech::off);  
        operatorController.bindButtonToggle(LogitechOperatorController.TOP_RIGHT_BASE_BUTTON, 
                 intakeMech::bothIntakesOn, intakeMech::off); 
        operatorController.bindButtonToggle(LogitechOperatorController.MIDDLE_LEFT_BASE_BUTTON, 
                 intakeMech::reverse, intakeMech::off);
        operatorController.bindButtonToggle(LogitechOperatorController.MIDDLE_RIGHT_BASE_BUTTON, 
                 intakeMech::outerIntakeOn, intakeMech::off);
        driverController.bindAxes(driverController.LEFT_Y_AXIS, driverController.RIGHT_X_AXIS, this::arcade);
    }

    public void update(){
        driverController.run();
        operatorController.run();

    }

    private void arcade(double speed, double rotation) {
        if (isHumanControlled){
            double rotateMultiplier = rotateMultiplierSupplier.get();
            double speedMultiplier = speedMultiplierSupplier.get();
            
            double outSpeed = speedMultiplier * speed;
            double outRotation = rotation * rotateMultiplier;
            
            drivetrain.safeArcade(outSpeed, outRotation);
        }
    }

    private void limelightShooting(){
        isHumanControlled = false;
        drivetrain.drvietrainAngleLineup();
        shootingMech.shoot();
    }

}