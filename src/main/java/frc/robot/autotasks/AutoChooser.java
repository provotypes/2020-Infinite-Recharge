package frc.robot.autotasks;

import java.util.ArrayList;

public class AutoChooser {


	public static AutoRoutine getChosenAuto() {
		String autoSelected = AutoSetup.getAutoChooser().getSelected();

		if(autoSelected == null) {
			System.out.println("Auto selected null!!");
			return new AutoRoutine(AutoFactory.DEFAULT_AUTO());
		}

		System.out.println("Auto selected" + autoSelected);
		AutoRoutine chosenRoutine;
		
		switch (autoSelected) {
			case AutoSetup.RIGHT_SIDE_AUTO:
				chosenRoutine = new AutoRoutine(AutoFactory.RIGHT_SIDE_AUTO());
				break;
			case AutoSetup.MIDDLE_AUTO:
				chosenRoutine = new AutoRoutine(AutoFactory.MIDDLE_AUTO());
				break;
			case AutoSetup.LEFT_SIDE_AUTO:
				chosenRoutine = new AutoRoutine(AutoFactory.LEFT_SIDE_AUTO());
				break;
			case AutoSetup.TRENCH_AUTO:
				chosenRoutine = new AutoRoutine(AutoFactory.TRENCH_AUTO());
				break;
			case AutoSetup.FORWARD_AUTO:
				chosenRoutine = new AutoRoutine(AutoFactory.FORWARD_AUTO());
				break;
			case AutoSetup.DEFAULT_AUTO:
			default:
				chosenRoutine = new AutoRoutine(AutoFactory.DEFAULT_AUTO());
				break;
			}
		return chosenRoutine;
	}
}
