package frc.robot;

import java.util.Map;
import static java.util.Map.entry;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class IntakeMechanism {

    private final double INDEXER_PERCENT = 0.9;
    private final double INDEXER_REVERSE = 0.5;
    private final double OUTER_INTAKE_PERCENT = 0.8;
    private final double INNER_INTAKE_PERCENT = 0.5;
    private final double OUTER_INTAKE_REVERSE = -6.0;
    private final double INNER_INTAKE_REVERSE = -6.0;
    private final double OUTER_INTAKE_REVERSE_LOW = 0.0;
    private static IntakeMechanism instance;
    //Port numbers are inaccurate
    private static TalonSRX outerIntakeWheels = new TalonSRX(4);
	private static TalonSRX innerIntakeWheels = new TalonSRX(1);
    private static TalonSRX indexer = new TalonSRX(3);
   
    private IntakeMechanism() {}

    public static IntakeMechanism getInstance() {
        if(instance == null) { 
            instance = new IntakeMechanism();
         }
        return instance;
    }
    
    enum IntakeMechanismModes {
        bothIntakesOn,
        indexerAndIntakes,
        indexer,
        reverse,
        intakeIdle,
        off
    }

    final Map<IntakeMechanismModes, Runnable> intakeModes = Map.ofEntries(
			entry(IntakeMechanismModes.off, this::executeOff),
			entry(IntakeMechanismModes.intakeIdle, this::executeIntakeIdle),
			entry(IntakeMechanismModes.indexerAndIntakes, this::executeIndexerAndIntakes),
			entry(IntakeMechanismModes.indexer, this::executeIndexer),
			entry(IntakeMechanismModes.reverse, this::executeReverse)
	    );

    private IntakeMechanismModes mode = IntakeMechanismModes.off;

    public void update() {
	    intakeModes.get(mode).run();
    }

    public void off() {
        this.mode = IntakeMechanismModes.off;
    }

    public void intakeIdle() {
        this.mode = IntakeMechanismModes.intakeIdle;
    }

    public void indexerAndIntakes() {
        this.mode = IntakeMechanismModes.indexerAndIntakes;
    }

    public void indexer() {
        this.mode = IntakeMechanismModes.indexer;
    }

    public void reverse() {
        this.mode = IntakeMechanismModes.reverse;
    }
  
    private void executeOff() {  
        outerIntakeWheelsOFF();
        innerIntakeWheelsOFF();
        indexerOFF();
    }

    private void executeIntakeIdle() {
        outerIntakeWheelsReverseLow();
        innerIntakeWheelsOFF();
        indexerOFF();
    }

    private void executeIndexerAndIntakes() {
        outerIntakeWheelsON();
        innerIntakeWheelsON();
        indexerON();
    }

    private void executeIndexer() {
        innerIntakeWheelsON();
        outerIntakeWheelsReverseLow();
        indexerON();
    }

    private void executeReverse() {
        innerIntakeWheelsReverse();
        outerIntakeWheelsReverse();
        indexerReverse();
    }

    private void outerIntakeWheelsON() {
        outerIntakeWheels.set(ControlMode.PercentOutput, OUTER_INTAKE_PERCENT);
    }

    private void outerIntakeWheelsOFF() {
        outerIntakeWheels.set(ControlMode.PercentOutput, 0);
    }

    // This will be constantly running when intake is off
    private void outerIntakeWheelsReverseLow() {
        outerIntakeWheels.set(ControlMode.PercentOutput, -OUTER_INTAKE_REVERSE_LOW);
    }

    // This along with inner intake wheels reverse will have a dedicated button in case we jam
    private void outerIntakeWheelsReverse() {
        outerIntakeWheels.set(ControlMode.PercentOutput, OUTER_INTAKE_REVERSE);
    }

    private void innerIntakeWheelsON() {
        innerIntakeWheels.set(ControlMode.PercentOutput, INNER_INTAKE_PERCENT);
    }

    private void innerIntakeWheelsOFF() {
        innerIntakeWheels.set(ControlMode.PercentOutput, 0);
    }

    private void innerIntakeWheelsReverse() {
        innerIntakeWheels.set(ControlMode.PercentOutput, INNER_INTAKE_REVERSE);
    }

    private void indexerON() {
        indexer.set(ControlMode.PercentOutput, INDEXER_PERCENT);
    }

    private void indexerOFF() {
        indexer.set(ControlMode.PercentOutput, 0);
    }

    private void indexerReverse() {
        indexer.set(ControlMode.PercentOutput, INDEXER_REVERSE);
    }
}
