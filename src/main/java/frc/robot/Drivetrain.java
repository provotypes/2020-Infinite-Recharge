package frc.robot;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.easypath.EasyPathDrivetrain;

public class Drivetrain extends DifferentialDrive implements EasyPathDrivetrain{
    
    private static SpeedControllerGroup leftGroup;
    private static SpeedControllerGroup rightGroup;
    private static Drivetrain instance;

    private Drivetrain(){
        super(leftGroup, rightGroup);
    }
    
    public static Drivetrain getInstance(){
        if(instance == null){
            instance = new Drivetrain();
        }
        return instance;
    }

    @Override
    public void setLeftRightDriveSpeed(double left, double right) {
        tankDrive(left, right);
    }

    @Override
    public double getInchesTraveled() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getCurrentAngle() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void resetEncodersAndGyro() {
        // TODO Auto-generated method stub
    }

    public void setArcadeDriveSpeed(double speed, double turn) {
		arcadeDrive(speed, turn, true);
    }
    
	public void setBrake(){

    }

 
	public void setCoast() {

    }

 
	public double getLeftEncoderDistance() {
        return 0;
    }


    public double getRightEncoderDistance() {
        return 0;
	}


	public void calibrateGyro() {
	}
}
