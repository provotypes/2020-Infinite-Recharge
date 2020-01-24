package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.*;

public class ShootingMechanism {
    
private static ShootingMechanism instance;
private static VictorSPX ballFeeder = new VictorSPX(1);
private static CANSparkMax shooter_a = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
private static CANSparkMax shooter_b = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);

//These motor controllers have enocders in them
private static TalonSRX turret = new TalonSRX(1);
private static TalonSRX hood = new TalonSRX(2);
  
private ShootingMechanism() {}

public static ShootingMechanism getInstance() {
    if(instance == null) {
        instance = new ShootingMechanism();
    }
    return instance;
    }

}
