package frc.robot.autotasks;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Drivetrain;
import frc.robot.IntakeMechanism;
import frc.robot.ShootingMechanism;

public class ShootingMechanismTask implements TaskInterface {

	private ShootingMechanism shooter = ShootingMechanism.getInstance();
	private Drivetrain drivetrain = Drivetrain.getInstance();
	private IntakeMechanism intake = IntakeMechanism.getInstance();

	private int numberOfBalls;
	private double flywheelVelocity;
	private int ballsShot;
	private final double SHOOTER_OFFSET = 20;
	private boolean shooterAtTarget;

	public ShootingMechanismTask(int numberOfBalls) {
		this.numberOfBalls = numberOfBalls;
	}

	@Override
	public void start() {
		ballsShot = 0;
		shooterAtTarget = false;
	}

	@Override
	public void execute() {
		shooter.shoot();
		intake.indexer();
		drivetrain.drvietrainAngleLineup(0.0);
		
		if (shooter.shooterVelocity() <= (shooter.shooterSetpoint() + SHOOTER_OFFSET)) {
			shooterAtTarget = true;
		} else {
			if (shooterAtTarget == true){
				ballsShot++;
				shooterAtTarget = false;
			}
			
		}
		SmartDashboard.putNumber("Balls shot", ballsShot);
		SmartDashboard.putBoolean("ShooterAtTarget", shooterAtTarget);
	}

	@Override
	public boolean isFinished() {
		if (ballsShot >= numberOfBalls) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void end() {
		shooter.off();
		intake.off();
	}
}