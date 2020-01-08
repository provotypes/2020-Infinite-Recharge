package frc.robot;

public class IntakeMechanism {
    
private static IntakeMechanism instance;

private IntakeMechanism(){}

public static IntakeMechanism getInstance(){
    if(instance == null){
        instance = new IntakeMechanism();
    }
    return instance;
}

}
