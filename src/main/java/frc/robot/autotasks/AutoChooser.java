package frc.robot.autotasks;

import java.util.ArrayList;

public class AutoChooser {

    ArrayList<TaskInterface> taskList = new ArrayList<TaskInterface>();

	AutoFactory autoFactory;

	public AutoChooser(AutoFactory factory) {
		autoFactory = factory;
	}

}
