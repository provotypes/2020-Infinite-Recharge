package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class IntakeMechanism {
    
    private static IntakeMechanism instance;
    //Port numbers are inaccurate
    private static TalonSRX outerIntakeWheels = new TalonSRX(1);
	private static TalonSRX innerIntakeWheels = new TalonSRX(2);
    private static VictorSPX indexer = new VictorSPX(1);
   
    private IntakeMechanism() {}

    public static IntakeMechanism getInstance() {
        if(instance == null) {
            instance = new IntakeMechanism();
         }
        return instance;
    }
    
    //It will probably make no sense to have only one of the
    // intakes on as the ball would not make it through to the indexer.
    enum intakeMechanismModes {
        outerIntakeOn,
        bothIntakesOn,
        indexerAndIntakes,
        indexer,
        reverse,
        off;
    }

    intakeMechanismModes state = intakeMechanismModes.off;

	public void update() {
		
		switch (state) {
        
            case off:
                outerIntakeWheelsOFF();
                innerIntakeWheelsOFF();
                indexerOFF();
			    break;
            case outerIntakeOn:
                outerIntakeWheelsON();
                innerIntakeWheelsOFF();
             // indexerON();
                break;
            case bothIntakesOn:
                outerIntakeWheelsON();
                innerIntakeWheelsON();
             // indexerOFF(); if indexer is on then it 
             // would be the same as the following state
                break;
            case indexerAndIntakes:
                outerIntakeWheelsON();
                innerIntakeWheelsON();
                indexerON();
                break;
            case indexer:
                innerIntakeWheelsOFF();
                outerIntakeWheelsReverse();
                indexerON();
                break;
            case reverse:
                innerIntakeWheelsReverse();
                outerIntakeWheelsReverse();
             // indexerOFF();
                break;
        }
    }

    public void off(){
        state = intakeMechanismModes.off;
    }

    public void outerIntakeOn(){
        state = intakeMechanismModes.outerIntakeOn;
    }

    public void bothIntakesOn(){
        state = intakeMechanismModes.bothIntakesOn;
    }

    public void indexerAndIntakes(){
        state = intakeMechanismModes.indexerAndIntakes;
    }

    public void indexer(){
        state = intakeMechanismModes.indexer;
    }

    public void reverse(){
        state = intakeMechanismModes.reverse;
    }

    public void outerIntakeWheelsON(){

    }

    public void outerIntakeWheelsOFF(){

    }

    public void innerIntakeWheelsON(){

    }

    public void innerIntakeWheelsOFF(){

    }

    public void indexerON(){
        //indexer.set(ControlMode., INDEXER_PERCENT_VOLTAGE);
    }

    public void indexerOFF(){

    }

    public void innerIntakeWheelsReverse(){

    }

    public void outerIntakeWheelsReverse(){

    }
}
