package frc.robot;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autotasks.AutoChooser;
import frc.robot.autotasks.AutoFactory;
import frc.robot.autotasks.AutoRoutine;
import frc.robot.autotasks.TaskInterface;

public class Robot extends TimedRobot {

  private ShootingMechanism shooter = ShootingMechanism.getInstance();
  private ClimbingMechanism climber = ClimbingMechanism.getInstance();
  private ControlPanelMechanism controlPanelSpinner = ControlPanelMechanism.getInstance();
  private Drivetrain drivetrain = Drivetrain.getInstance();
  private IntakeMechanism intake = IntakeMechanism.getInstance();
  private TeleopController teleopController = TeleopController.getInstance();
  private TaskInterface autoRoutine;
  private boolean isTaskRunning = false;
  private LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();
  private static ColorSensor colorSensor = ColorSensor.getInstance();

  //leds
  private DigitalOutput ledColor = new DigitalOutput(0);
  
  @Override
  public void robotInit() {
    colorSensor.matchColors();
    teleopController.TeleopInit();
  }

  
	@Override
	public void robotPeriodic() {
    colorSensor.ourColor();
    drivetrain.putSmartDashInfo();
    SmartDashboard.putNumber("Limelight Distance", limelight.getDistance());
    limelight.optimizedDistance();

    if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue) {
      ledColor.set(false);
    }
    else {
      ledColor.set(true);
    }

  }

  
  @Override
  public void autonomousInit() {
   // autoRoutine = autoChooser.getAutoChooser;
      autoRoutine.start();
      isTaskRunning = true;
  }

	@Override
	public void autonomousPeriodic() {
    if (isTaskRunning) {
        if (!autoRoutine.isFinished()) {
            autoRoutine.execute();
        }
        else {
            autoRoutine.end();
            isTaskRunning = false;
          }
     }
  }

	@Override
	public void teleopPeriodic() {
    colorSensor.ourColor();
    teleopController.update();
    shooter.update();
    intake.update();
  }

  @Override
  public void testInit() {
    drivetrain.calibrateGyro();
  }

	@Override
	public void testPeriodic() {  
  }
  
}