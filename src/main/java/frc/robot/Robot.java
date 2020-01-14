package frc.robot;

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
 // private ControlPanelMechanism controlPanelSpinner = ControlPanelMechanism.getInstance();
  private Drivetrain drivetrain = Drivetrain.getInstance();
  private IntakeMechanism intake = IntakeMechanism.getInstance();
  private AutoChooser autoChooser = new AutoChooser(new AutoFactory());
  private TaskInterface autoRoutine;
  private boolean isTaskRunning = false;

  private static ColorSensor colorSensor = ColorSensor.getInstance();
  
  @Override
  public void robotInit() {
    colorSensor.matchColors();
  }

  
	@Override
	public void robotPeriodic() {
		colorSensor.update();
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
	public void teleopPeriodic() {}

	@Override
	public void testPeriodic() {}


}
 