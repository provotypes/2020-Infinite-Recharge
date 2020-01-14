package frc.robot;

public class ControlPanelMechanism {
    private static ControlPanelMechanism instance;

    private ControlPanelMechanism() {}
    
    public static ControlPanelMechanism getInstance() {
        if(instance == null) {
            instance = new ControlPanelMechanism();
        }
        return instance;
    }

    
}
