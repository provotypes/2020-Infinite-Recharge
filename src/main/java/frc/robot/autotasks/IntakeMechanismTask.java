package frc.robot.autotasks;

import frc.robot.Drivetrain;
import frc.robot.IntakeMechanism;

public class IntakeMechanismTask implements TaskInterface {
    
    private Drivetrain drivetrain = Drivetrain.getInstance();
	private IntakeMechanism intake = IntakeMechanism.getInstance();
    
    @Override
    public void start() {

    }

    @Override
    public void execute() {
        intake.indexerAndIntakes();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        intake.off();
    }


    


}
