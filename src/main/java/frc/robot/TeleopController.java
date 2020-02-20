package frc.robot;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopController {

    private static TeleopController instance;
   
    private ShootingMechanism shootingMech = ShootingMechanism.getInstance();
    private IntakeMechanism intakeMech = IntakeMechanism.getInstance();
    private LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();
    private ControlPanelMechanism controlPanel = ControlPanelMechanism.getInstance();
    private ClimbingMechanism climber = ClimbingMechanism.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
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
    
        operatorController.bindButton(LogitechOperatorController.TRIGGER, this::limelightShooting);
        operatorController.bindButtonRelease(LogitechOperatorController.TRIGGER, () -> {isHumanControlled = true; shootingMech.off();});
        operatorController.bindButtonToggle(LogitechOperatorController.THUMB_BUTTON, 
                 intakeMech::indexer, intakeMech::off); 
        operatorController.bindButtonPress(LogitechOperatorController.BOTTOM_LEFT_BASE_BUTTON, intakeMech::off);
        operatorController.bindButtonPress(LogitechOperatorController.BOTTOM_RIGHT_BASE_BUTTON, intakeMech::off);
        operatorController.bindButtonToggle(LogitechOperatorController.TOP_LEFT_BASE_BUTTON, 
                 intakeMech::indexerAndIntakes, intakeMech::intakeIdle);  
        operatorController.bindButtonToggle(LogitechOperatorController.TOP_RIGHT_BASE_BUTTON, 
                 intakeMech::indexerAndIntakes, intakeMech::intakeIdle); 
        operatorController.bindButtonToggle(LogitechOperatorController.MIDDLE_LEFT_BASE_BUTTON, 
                 intakeMech::reverse, intakeMech::intakeIdle);
        operatorController.bindButtonToggle(LogitechOperatorController.MIDDLE_LEFT_BASE_BUTTON, 
                 intakeMech::reverse, intakeMech::intakeIdle);
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