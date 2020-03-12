package frc.robot.autotasks;

import frc.robot.Drivetrain;

public class DrivetrainRotation implements TaskInterface{

    private Drivetrain drivetrain = Drivetrain.getInstance();
    private double rotationDegrees;

    public DrivetrainRotation(double rotationDegrees){
        this.rotationDegrees = rotationDegrees;
    }

    @Override
    public void start() {
    
    }

    @Override
    public void execute() {
        drivetrain.drivetrainTurn(0, rotationDegrees);
    }

    @Override
    public boolean isFinished() {
        return inRange(drivetrain.getCurrentAngle() - rotationDegrees, -3, 3);
    }

    @Override
    public void end() {
        drivetrain.arcadeDrive(0, 0);
    }   
    
      /**
     * 
     * @param input input value
     * @param low lower bound
     * @param high upper bound
     * @return if input is between low and high (inclusive)
     */
    private boolean inRange(double input, double low, double high) {
        if ((input >= low) && (input <= high)) {
            return true;
        } else {
            return false;
        }
    }



}
