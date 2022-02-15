package frc.robot;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autotasks.AutoChooser;
import frc.robot.autotasks.AutoSetup;
import frc.robot.autotasks.TaskInterface;

public class Robot extends TimedRobot {

  private ShootingMechanism shooter = ShootingMechanism.getInstance();
  private Drivetrain drivetrain = Drivetrain.getInstance();
  private IntakeMechanism intake = IntakeMechanism.getInstance();
  private TeleopController teleopController = TeleopController.getInstance();
  private ClimbingMechanism climber = ClimbingMechanism.getInstance();
 

  private TaskInterface autoRoutine;
  private boolean isTaskRunning = false;
  private LimelightVisionTracking limelight = LimelightVisionTracking.getInstance();
  // private static ColorSensor colorSensor = ColorSensor.getInstance();

  private PowerDistribution pdp = new PowerDistribution();



  //leds
  private DigitalOutput ledColor = new DigitalOutput(0);
  
  @Override
  public void robotInit() {
    // colorSensor.matchColors();
    teleopController.TeleopInit();
    drivetrain.setCoast();

    AutoSetup.init();

    SmartDashboard.putBoolean("Calibrate gyro", false);
  }

  
	@Override
	public void robotPeriodic() {
    // colorSensor.ourColor();
    drivetrain.putSmartDashInfo();
    SmartDashboard.putNumber("Limelight Distance", limelight.getDistance());
    SmartDashboard.putBoolean("limelight.targetFound()", limelight.targetFound());
    // limelight.optimizedDistance();

    if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
      ledColor.set(false);
    }
    else {
      ledColor.set(true);
    }

    SmartDashboard.putNumber("shooter calc dis", ShooterCalculator.roundDis(limelight.getDistance()));
    SmartDashboard.putNumber("shooter calc power", ShooterCalculator.calculateRPM(limelight.getDistance()));

    SmartDashboard.putNumber("total current", pdp.getTotalCurrent());

    if (SmartDashboard.getBoolean("Calibrate gyro", false)) {
      drivetrain.calibrateGyro();
      SmartDashboard.putBoolean("Calibrate gyro", false);
    }
  }

  @Override
  public void disabledInit() {
    drivetrain.setCoast();
    if (autoRoutine != null) {
      autoRoutine.end();
      shooter.update();
      intake.update();
    }
  }
  
  @Override
  public void autonomousInit() {
      autoRoutine = AutoChooser.getChosenAuto();
      autoRoutine.start();
      isTaskRunning = true;
      drivetrain.setBrake();
      drivetrain.resetEncodersAndGyro();
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

     shooter.update();
    intake.update();
  }

    @Override
    public void teleopInit() {
      drivetrain.setBrake();
      teleopController.resetTimer();
      if (autoRoutine != null) {
        autoRoutine.end();
      }
    }

	@Override
	public void teleopPeriodic() {
    teleopController.update();
    // System.out.print("TeleControl");
    shooter.update();
    // System.out.print("shooter");
    intake.update();
    // System.out.print("intake");
    climber.update();
    // System.out.print("climber");
  }

  @Override
  public void testInit() {
    // shooter.off();
  }

	@Override
	public void testPeriodic() {  
    // shooter.update();
    // shooter.executeOff();
  }
  
}