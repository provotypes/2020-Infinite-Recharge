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

    private static double ELEVATOR_SPEED = 0.4;
    private static double WINCH_SPEED = 0.4;

    private ClimbingMechanism() {}

    public static ClimbingMechanism getInstance() {
        if(instance == null) {
            instance = new ClimbingMechanism();
        }  
        return instance;
    }
    
    enum ClimbingMechanismModes {
        off,
        elevator,
        winch
    }
    
    private ClimbingMechanismModes mode = ClimbingMechanismModes.off;
   
    final Map<ClimbingMechanismModes, Runnable> climberModes = Map.ofEntries(
            entry(ClimbingMechanismModes.off, this::executeOff),
            entry(ClimbingMechanismModes.winch, this::executeWinch),
            entry(ClimbingMechanismModes.elevator, this::executeElevator)
        );

    public void update() {
        climberModes.get(mode).run();
    }

    public void off() {
        this.mode = ClimbingMechanismModes.off;
    }

    public void elevator() {
        this.mode = ClimbingMechanismModes.elevator;
    }

    public void winch() {
        this.mode = ClimbingMechanismModes.winch;
    }

    private void executeOff() {
        climberWinchOFF();
        climberElevatorOFF();
    }

    private void executeElevator() {
        climberElevatorON();
        climberWinchOFF();
    }
    
    private void executeWinch() {
        climberWinchON();
        climberElevatorOFF();
    }

    private void climberWinchON() {
        climberWinch.set(ControlMode.PercentOutput, WINCH_SPEED);
    }

    private void climberWinchOFF() {
        climberWinch.set(ControlMode.PercentOutput, 0.0);
    }

    private void climberElevatorON() {
        climberElevator.set(ControlMode.PercentOutput, ELEVATOR_SPEED);
    }

    private void climberElevatorOFF() {
        climberElevator.set(ControlMode.PercentOutput, 0);
    }










































}