package frc.robot.autotasks;

import java.util.ArrayList;

public class AutoChooser {

	public static AutoRoutine getChosenAuto() {
		String autoSelected = AutoSetup.getAutoChooser().getSelected();
		System.out.println("Auto selected" + autoSelected);
		AutoRoutine chosenRoutine = new AutoRoutine(AutoFactory.test());
		return chosenRoutine;
	}

}
