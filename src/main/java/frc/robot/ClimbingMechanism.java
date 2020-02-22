package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class ClimbingMechanism {

private static ClimbingMechanism instance;
//Port numbers should be fixed later //Delete this comment when done
private static TalonSRX climberWinch = new TalonSRX(6);
private static TalonSRX climberElevator = new TalonSRX(5);

private ClimbingMechanism() {}

public static ClimbingMechanism getInstance() {
    if(instance == null) {
        instance = new ClimbingMechanism();
    }  
    return instance;
}

}
