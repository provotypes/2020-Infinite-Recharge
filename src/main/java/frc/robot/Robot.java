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
  @Override
  public void robotInit() {
 
  }

  
  @Override
  public void robotPeriodic() {
    // SmartDashboard.putNumber("Red", detectedColor.red);
    // SmartDashboard.putNumber("Green", detectedColor.green);
    // SmartDashboard.putNumber("Blue", detectedColor.blue);
    // SmartDashboard.putNumber("Confidence", match.confidence);
    // SmartDashboard.putString("Detected Color", colorString);
  }

  
  @Override
  public void autonomousInit() {
   // autoRoutine = autoChooser.getAutoChooser;
      autoRoutine.start();
   
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
   
    }
  

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
