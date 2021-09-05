package frc.robot;

import static java.util.Map.entry;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class ClimbingMechanism {

    private static ClimbingMechanism instance;
    //Port numbers should be fixed later //Delete this comment when done
    private TalonSRX climberWinch = new TalonSRX(6);
    private TalonSRX climberElevator = new TalonSRX(5);

    private static double ELEVATOR_SPEED = 0.2;
    private static double WINCH_SPEED = 0.9;

    private ClimbingMechanism() {}

    public static ClimbingMechanism getInstance() {
        if(instance == null) {
            instance = new ClimbingMechanism();
        }  
        return instance;
    }
    
    enum ClimbingMechanismModes {
        off,
        elevatorUp,
        elevatorDown,
        winchUp,
        winchDown
    }
    
    private ClimbingMechanismModes mode = ClimbingMechanismModes.off;
   
    final Map<ClimbingMechanismModes, Runnable> climberModes = Map.ofEntries(
            entry(ClimbingMechanismModes.off, this::executeOff),
            entry(ClimbingMechanismModes.winchUp, this::executeWinchUp),
            entry(ClimbingMechanismModes.winchDown, this::executeWinchDown),
            entry(ClimbingMechanismModes.elevatorUp, this::executeElevatorUp),
            entry(ClimbingMechanismModes.elevatorDown, this::executeElevatorDown)
        );

    public void update() {
        climberModes.get(mode).run();
    }

    public void off() {
        this.mode = ClimbingMechanismModes.off;
    }

    public void elevatorUp() {
        this.mode = ClimbingMechanismModes.elevatorUp;
    }

    public void elevatorDown() {
        this.mode = ClimbingMechanismModes.elevatorDown;
    }

    public void winchUp() {
        this.mode = ClimbingMechanismModes.winchUp;
    }

    public void winchDown() {
        this.mode = ClimbingMechanismModes.winchDown;
    }

    private void executeOff() {
        climberWinchOff();
        climberElevatorOff();
    }

    private void executeElevatorUp() {
        climberElevatorOn();
        climberWinchOff();
    }
    private void executeElevatorDown() {
        climberElevatorReverse();
        climberWinchOff();
    }
    
    private void executeWinchUp() {
        climberWinchOn();
        climberElevatorOff();
    }

    private void executeWinchDown() {
        climberWinchReverse();
        climberElevatorOff();
    }

    private void climberWinchOn() {
        climberWinch.set(ControlMode.PercentOutput, WINCH_SPEED);
    }

    private void climberWinchOff() {
        climberWinch.set(ControlMode.PercentOutput, 0.0);
    }

    private void climberWinchReverse() {
        climberWinch.set(ControlMode.PercentOutput, -WINCH_SPEED);
    }

    private void climberElevatorOn() {
        climberElevator.set(ControlMode.PercentOutput, ELEVATOR_SPEED);
    }

    private void climberElevatorOff() {
        climberElevator.set(ControlMode.PercentOutput, 0);
    }

    private void climberElevatorReverse() {
        climberElevator.set(ControlMode.PercentOutput, -ELEVATOR_SPEED);
    }

}