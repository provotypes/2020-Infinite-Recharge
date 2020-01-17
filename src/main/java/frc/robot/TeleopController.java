package frc.robot;

public class TeleopController {

    private static TeleopController instance;

    private TeleopController() {}

    public static TeleopController getInstance() {
        if(instance == null) {
            instance = new TeleopController();
        }
        return instance;
    }










    
}
