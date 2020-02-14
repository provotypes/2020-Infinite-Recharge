package frc.robot;

import java.util.Map;
import static java.util.Map.entry;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class IntakeMechanism {
    
    private static IntakeMechanism instance;
    //Port numbers are inaccurate
    private static TalonSRX outerIntakeWheels = new TalonSRX(1);
	private static TalonSRX innerIntakeWheels = new TalonSRX(3);
    private static TalonSRX indexer = new TalonSRX(2);
   
    private IntakeMechanism() {}

    public static IntakeMechanism getInstance() {
        if(instance == null) { 
            instance = new IntakeMechanism();
         }
        return instance;
    }
    
    //It will probably make no sense to have only one of the
    // intakes on as the ball would not make it through to the indexer.
    enum IntakeMechanismModes {
        outerIntakeOn,
        bothIntakesOn,
        indexerAndIntakes,
        indexer,
        reverse,
        off
    }

    private IntakeMechanismModes intakeMechanismModes = IntakeMechanismModes.off;

    public void update() {
	    final Map<IntakeMechanismModes, Runnable> intakeModes = Map.ofEntries(
			entry(IntakeMechanismModes.off, this::executeOff),
			entry(IntakeMechanismModes.outerIntakeOn, this::executeOuterIntakeOn),
			entry(IntakeMechanismModes.bothIntakesOn, this::executeBothIntakesOn),
			entry(IntakeMechanismModes.indexerAndIntakes, this::executeIndexerAndIntakes),
			entry(IntakeMechanismModes.indexer, this::executeIndexer),
			entry(IntakeMechanismModes.reverse, this::executeReverse)
	    );
    }
    public void off() {
        this.intakeMechanismModes = IntakeMechanismModes.off;
    }

    public void outerIntakeOn() {
        this.intakeMechanismModes = IntakeMechanismModes.outerIntakeOn;
    }

    public void bothIntakesOn() {
        this.intakeMechanismModes = IntakeMechanismModes.bothIntakesOn;
    }    

    public void indexerAndIntakes() {
        this.intakeMechanismModes = IntakeMechanismModes.indexerAndIntakes;
    }

    public void indexer() {
        this.intakeMechanismModes = IntakeMechanismModes.indexer;
    }

    public void reverse() {
        this.intakeMechanismModes = IntakeMechanismModes.reverse;
    }

    public void executeOff() {
        outerIntakeWheelsOFF();
        innerIntakeWheelsOFF();
        indexerOFF();
    }

    public void executeOuterIntakeOn() {
        outerIntakeWheelsON();
        innerIntakeWheelsOFF();
     // indexerON();
    }

    public void executeBothIntakesOn() {
        outerIntakeWheelsON();
        innerIntakeWheelsON();
        // indexerOFF(); if indexer is on then it 
        // would be the same as the following state  
    }

    public void executeIndexerAndIntakes() {
        outerIntakeWheelsON();
        innerIntakeWheelsON();
        indexerON();
    }

    public void executeIndexer() {
        innerIntakeWheelsOFF();
        outerIntakeWheelsReverse();
        indexerON();
    }

    public void executeReverse() {
        innerIntakeWheelsReverse();
        outerIntakeWheelsReverse();
     // indexerOFF();
    }

    public void outerIntakeWheelsON() {

    }

    public void outerIntakeWheelsOFF() {

    }

    public void innerIntakeWheelsON() {

    }

    public void innerIntakeWheelsOFF() {

    }

    public void indexerON() {
        //indexer.set(ControlMode., INDEXER_PERCENT_VOLTAGE);
    }

    public void indexerOFF() {

    }

    public void innerIntakeWheelsReverse() {

    }

    public void outerIntakeWheelsReverse() {

    }
}
