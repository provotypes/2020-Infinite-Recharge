package frc.robot;

public class Drivetrain {
    
    private static Drivetrain instance;

    private Drivetrain(){}
    
    public static Drivetrain getInstance(){
        if(instance == null){
            instance = new Drivetrain();
        }
        return instance;
    }
}
