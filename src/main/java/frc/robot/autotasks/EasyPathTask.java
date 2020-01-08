package frc.robot.autotasks;

import easypath.FollowPath;

public class EasyPathTask implements TaskInterface {

	FollowPath path;

	public EasyPathTask(FollowPath p) {
		this.path = p;
	}

	/**
	 * Should be called once at the begining of the task
	 */
	public void start() {
		path.initialize();
		System.out.println("easypath task start");
	}

	/**
	 * Should be called repeatedly while a task is being executed
	 */
	public void execute() {
		path.execute();
		System.out.println("easypath task execute");
	}

	/**
	 * Should return true when the task is finished
	 *
	 * @return whether the task is finished
	 */
	public boolean isFinished() {
		System.out.println("easypath task is finished is " + path.isFinished());
		return path.isFinished();
	}

	//FIXME would be cool to make this link work:

	/**
	 * Should be run once after {@link isFinished()} returns true
	 */
	public void end() {
		path.end();
		System.out.println("easypath task end");
	}

}
