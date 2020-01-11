package frc.robot;

public class ClimbingMechanism {

private static ClimbingMechanism instance;

private ClimbingMechanism() {

}

public static ClimbingMechanism getInstance() {
    if(instance == null) {
        instance = new ClimbingMechanism();
    }
    return instance;
}
}
