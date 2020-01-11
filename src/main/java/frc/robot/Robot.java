package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.ColorSensor;

public class Robot extends TimedRobot {
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
	public void autonomousInit() {}

	@Override
	public void autonomousPeriodic() {}

	@Override
	public void teleopPeriodic() {}

	@Override
	public void testPeriodic() {}

}
