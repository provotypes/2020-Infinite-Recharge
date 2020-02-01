package frc.robot;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class TeleopController {

    private static TeleopController instance;
   
    ShootingMechanism shootingMech = ShootingMechanism.getInstance();
    IntakeMechanism intakeMech = IntakeMechanism.getInstance();
    LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();
    ControlPanelMechanism controlPanel = ControlPanelMechanism.getInstance();
    ClimbingMechanism climber = ClimbingMechanism.getInstance();

    private LogitechDriverController driverController = new LogitechDriverController(0);
    private LogitechOperatorController operatorController = new LogitechOperatorController(1);

    private TeleopController() {}

    public static TeleopController getInstance() {
        if(instance == null) {
            instance = new TeleopController();
        }
        return instance;
    }

    public void TeleopInit() {
        System.out.println("Teleop has started!");

        operatorController.bindButtonToggle(LogitechOperatorController.TRIGGER, 
                 shootingMech::shoot, shootingMech::off);
        operatorController.bindButtonToggle(LogitechOperatorController.THUMB_BUTTON, 
                 intakeMech::indexerAndIntakes, intakeMech::off);
        // operatorController.bindButtonToggle(LogitechOperatorController.TRIGGER, 
        //          shootingMech::shoot, shootingMech::off);
        // operatorController.bindButtonToggle(LogitechOperatorController.TRIGGER, 
        //          shootingMech::shoot, shootingMech::off);
        // operatorController.bindButtonToggle(LogitechOperatorController.TRIGGER, 
        //          shootingMech::shoot, shootingMech::off);



    }








    
}
