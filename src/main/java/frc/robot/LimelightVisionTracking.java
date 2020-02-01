package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimelightVisionTracking {

    private static LimelightVisionTracking instance;
    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    private NetworkTableEntry tx = table.getEntry("tx");
    private NetworkTableEntry ty = table.getEntry("ty");
    private NetworkTableEntry tv = table.getEntry("tv");

    //fix me
    private final double REL_TARGET_HEIGHT = 100; // this should definitly be changed || the target height - shooter height. 
    //fix me
    private final double MOUNT_ANGLE = 45; // this should be the mount angle for the limelight + the limelight angle

    private LimelightVisionTracking() {}

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
        double distance = REL_TARGET_HEIGHT/Math.tan(MOUNT_ANGLE + ty.getDouble(0.0));
        return distance;
    }

    public boolean targetFound() {
        return tv.getBoolean(true);
    }

}
