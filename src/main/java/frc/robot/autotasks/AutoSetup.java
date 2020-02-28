package frc.robot.autotasks;
//This class will be similar to robotinit from 2019

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Drivetrain;
import frc.robot.easypath.EasyPath;
import frc.robot.easypath.EasyPathConfig;


public class AutoSetup {

    private static SendableChooser<String> autoChooser = new SendableChooser<>();
    private static Drivetrain drivetrain = Drivetrain.getInstance();

        public static final String DEFAULT_AUTO = "Default Auto";
        public static final String RIGHT_SIDE_AUTO = "Right Side Start";
        public static final String MIDDLE_AUTO = "Middle  Start";
        public static final String LEFT_SIDE_AUTO =  "Left Side Start";

    public static void init() {
       autoChooser.addOption("Default Auto", DEFAULT_AUTO);
       autoChooser.addOption("Right Side Start", RIGHT_SIDE_AUTO);
       autoChooser.addOption("Middle Side Start", MIDDLE_AUTO);
       autoChooser.addOption("Left Side Start", LEFT_SIDE_AUTO);
       
       SmartDashboard.putData("Auto Choice", autoChooser);

       EasyPathConfig pathConfig = new EasyPathConfig(
        drivetrain::setLeftRightDriveSpeed,
        drivetrain::getInchesTraveled,
        drivetrain::getCurrentAngle,
        drivetrain::resetEncodersAndGyro,
        0.05
    );
        EasyPath.configure(pathConfig);
    }
    public static SendableChooser<String> getAutoChooser() {
        return autoChooser;
    }
    
}
