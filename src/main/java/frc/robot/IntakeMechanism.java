package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class IntakeMechanism {
    
    private static IntakeMechanism instance;
    //Port numbers are inaccurate
    private static TalonSRX outerIntakeWheels = new TalonSRX(1);
	private static TalonSRX innerIntakeWheels = new TalonSRX(2);
    private static VictorSPX indexer = new VictorSPX(1);
   
    private IntakeMechanism() {}

    public static IntakeMechanism getInstance() {
        if(instance == null) {
            instance = new IntakeMechanism();
         }
        return instance;
    }
    

}
