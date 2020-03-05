package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LimelightVisionTracking {

    private static LimelightVisionTracking instance;
    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    private  NetworkTableEntry tx = table.getEntry("tx");
    private NetworkTableEntry ty = table.getEntry("ty");
    private NetworkTableEntry tv = table.getEntry("tv");
    private NetworkTableEntry pipeline = table.getEntry("pipeline");

    //fix me
    private final double REL_TARGET_HEIGHT = 98.25-16.5; // this should definitly be changed || the target height - shooter height. 
    private final double MOUNT_ANGLE = 32.7; // this should be the mount angle for the limelight + the limelight angle
    private final double DISTANCE_THRESHOLD = 200;
    private LimelightVisionTracking() {
        SmartDashboard.putNumber("limelight angle", MOUNT_ANGLE);
    }

    public static LimelightVisionTracking getInstance() {
        if(instance == null) {
            instance = new LimelightVisionTracking();
        }
        return instance;
    }

    public double getHorizontalAngle() {
        double horizontalAngle = tx.getDouble(0.0);
        return horizontalAngle;
    } 

    public double getDistance() {
        double angleInRadians = ((SmartDashboard.getNumber("limelight angle", MOUNT_ANGLE) + ty.getDouble(0.0)) * Math.PI) / 180;
        double distance = REL_TARGET_HEIGHT/Math.tan(angleInRadians);
        return distance;
    }

    public boolean targetFound() {
        return tv.getBoolean(true);
    }

    public void optimizedDistance(){
        if(getDistance() >= DISTANCE_THRESHOLD){
            pipeline.setDouble(1.0);
        } else {
            pipeline.setDouble(0.0);
        }
    }

}
