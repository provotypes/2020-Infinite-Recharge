package frc.robot.autotasks;

import java.util.ArrayList;
import java.util.List;

public class AutoFactory {

    public static List<TaskInterface> test() {
		List<TaskInterface> taskList = new ArrayList<TaskInterface>();
        taskList.add(new ShootingMechanismTask(3));
        return taskList;
    }
}
