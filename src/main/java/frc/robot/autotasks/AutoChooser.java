package frc.robot.autotasks;

import java.util.ArrayList;

public class AutoChooser {

	private static AutoFactory autoFactory;
	public void setAutoFactory(AutoFactory factory1) {
		autoFactory = factory1;
	}
	public AutoRoutine getChosenAuto() {
		String autoSelected = AutoSetup.getAutoChooser().getSelected();
		System.out.println("Auto selected" + autoSelected);
		AutoRoutine chosenRoutine = new AutoRoutine(autoFactory.emptyList());
		return chosenRoutine;
	}

}
