package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import frc.robot.IntakeMechanism.intakeMechanismModes;

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

enum shooterMechanismModes {
        shoot,
    //  shootWithVisionTracking, ??
        shootWithTurret,
        shootWithHood,
        shootAndIntake,
     // shootWithHoodAndTurret, //If we need to shood with the hood down as well as the turret, add this mode
        off;
    }

    shooterMechanismModes state = shooterMechanismModes.off;

	public void update() {
		
		switch (state) {
        
            case off:
                shooter_aOFF();
                shooter_bOFF();
                turretOFF();
                hoodOFF();
			    break;
            case shoot:
                shooter_aON();
                shooter_bON();
                turretOFF();
                hoodOFF();
                break;
            case shootWithTurret:
                shooter_aON();
                shooter_bON();
                turretON();
                hoodOFF();
                break;
            case shootWithHood:
                shooter_aON();
                shooter_bON();
                turretOFF();
                hoodON();
                break;
            case shootAndIntake:
                // this is why we shouldn't have done a switch statement :(
                break;
        }
    }

    public void off(){
        state = shooterMechanismModes.off;
    }

    public void shoot(){
        state = shooterMechanismModes.shoot;
    }

    public void shootWithTurret(){
        state = shooterMechanismModes.shootWithTurret;
    }

    public void shootWithHood(){
        state = shooterMechanismModes.shootWithHood;
    }

    public void shootAndIntake(){
        state = shooterMechanismModes.shootAndIntake;
    }

   public void ballFeederON(){

   }

   public void ballFeederOFF(){

   }
// shooter a and b motor controllers probs do need to be in a speed controller group; They will either be both on or off.
   public void shooter_aON(){

   }

   public void shooter_aOFF(){

   }

   public void shooter_bON(){

   }

   public void shooter_bOFF(){

   }
// Turret methods probs need to have something like fixed in place or spinning and aiming(using limelight).
   public void turretON(){

   }

   public void turretOFF(){

   }
// Hood methods probs need to be renamed to up and down. At this point i dont know what the hood does.
  // Hood off makes no sense. Hood is either adjusted or just up.
    public void hoodOFF(){

   }

   public void hoodON(){

   }

}
