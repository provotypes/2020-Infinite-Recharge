package frc.robot;

public class ShootingMechanism {
    
private static ShootingMechanism instance;

private ShootingMechanism(){}

public static ShootingMechanism getInstance(){
    if(instance == null){
        instance = new ShootingMechanism();
    }
    return instance;
    }
}
