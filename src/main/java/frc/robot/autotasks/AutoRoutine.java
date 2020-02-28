package frc.robot.autotasks;

import java.util.List;

import frc.robot.autotasks.TaskInterface;

public class AutoRoutine implements TaskInterface {

	private boolean isFinished;

	List<TaskInterface> taskList;
	int curTaskIndex;

	public AutoRoutine(List<TaskInterface> taskList) {
		this.taskList = taskList;
	}

	public void addTask(TaskInterface task) {
		taskList.add(task);
	}

	@Override
	public void start() {
		isFinished = false;
		curTaskIndex = 0;
		taskList.get(curTaskIndex).start();
	}

	@Override
	public void execute() {
		if (!isFinished) {
			if (!taskList.get(curTaskIndex).isFinished()) {
				taskList.get(curTaskIndex).execute();
			} else {
				taskList.get(curTaskIndex).end();
				curTaskIndex += 1;
				if (taskList.size() > curTaskIndex) {
					taskList.get(curTaskIndex).start();
				} else {
					isFinished = true;
				}
			}
		}

	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}

	@Override
	public void end() {
		isFinished = true;
	}

}