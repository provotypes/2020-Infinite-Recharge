package frc.robot.easypath;

/**
 * Interface to clarify the parts of the drive train class made for EasyPath.
 * Override these methods and give them to EasyPathConfig.
 */
public interface EasyPathDrivetrain {

    /**
     * Function for EasyPath to set the left and right drive train speeds
     * @param left the speed to set the left side drive train
     * @param right the speed to set the right side drive train
     */
    public void setLeftRightDriveSpeed(double left, double right);

    /**
     * Function for EasyPath to get the total inches traveled by the robot.
     * This can be from one encoder or the average of the left and right sides.
     * @return the total inches traveled by the robot
     */
    public double getInchesTraveled();

    /**
     * Function for EasyPath to get the current heading of the robot.
     * @return the current angle of the robot
     */
    public double getCurrentAngle();

    /**
     * Function for EasyPath to reset the encoders and gyro to zero.
     * This can directly reset the encoders and gyro to zero, or the 
     * angle and position can be tracked from a seperate variable so 
     * EasyPath does not interfere with the robots true angle and position.
     * 
     * In the future this will be intigrated into EasyPath, so you 
     * dont have to provide the reset functions yourself.
     */
    public void resetEncodersAndGyro();

}
