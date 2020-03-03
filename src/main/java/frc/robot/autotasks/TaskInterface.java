package frc.robot.autotasks;


/**
 * interface for Task Objects
 */
public interface TaskInterface {

	/**
	 * Should be called once at the begining of the task
	 */
	public void start();

	/**
	 * Should be called repeatedly while a task is being executed
	 */
	public void execute();

	/**
	 * Should return true when the task is finished
	 *
	 * @return whether the task is finished
	 */
	public boolean isFinished();

	/**
	 * Should be run once after {@link isFinished()} returns true
	 */
	public void end();

}


