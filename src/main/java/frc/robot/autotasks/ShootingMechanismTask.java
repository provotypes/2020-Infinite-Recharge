package frc.robot.autotasks;

import frc.robot.ShootingMechanism;

public class ShootingMechanismTask implements TaskInterface {

	private ShootingMechanism mechanism;
	private Runnable modeMethod;
	private boolean isFinished;
	private int numTicks;
	private int duration;

	//TODO: make unit of duration variable seconds instead of ticks
//fix
	public ShootingMechanismTask(ShootingMechanism c, Runnable modeMethod, int duration) {
		this.mechanism = c;
		this.modeMethod = modeMethod;
		this.duration = duration;
	}

	@Override
	public void start() {
		numTicks = 0;
	}

	@Override
	public void execute() {}

	@Override
	public boolean isFinished() {
		return isFinished;
	}

	@Override
	public void end() {}
	
}