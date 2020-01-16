package frc.robot.autotasks;
//This class will be similar to robotinit from 2019

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;


public class AutoSetup {


    private static SendableChooser<String> autoChooser = new SendableChooser<>();
    public static void init() {



    }
    public static SendableChooser<String> getAutoChooser() {
        return autoChooser;

    }

}
