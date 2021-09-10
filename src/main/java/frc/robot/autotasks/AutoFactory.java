package frc.robot.autotasks;

import java.util.ArrayList;
import java.util.List;

import frc.robot.Drivetrain;
import frc.robot.easypath.*;

public class AutoFactory {
    
    public static List<TaskInterface> DEFAULT_AUTO() {
        List<TaskInterface> taskList = new ArrayList<TaskInterface>();
        taskList.add(new IntakeMechanismTask());
        taskList.add(new EasyPathTask(new FollowPath(PathUtil.createStraightPath(24), 0.4)));
        taskList.add(new ShootingMechanismTask(69));
        return taskList;
    }

    public static List<TaskInterface> TRENCH_AUTO() {
        List<TaskInterface> taskList = new ArrayList<TaskInterface>();
        taskList.add(new ShootingMechanismTask(3, 6));
        taskList.add(new IntakeMechanismTask());
        taskList.add(new EasyPathTask(new FollowPath(PathUtil.createStraightPath(5), -0.5)));
        taskList.add(new EasyPathTask(new FollowPath(PathUtil.createStraightPath(60), 0.4)));
        taskList.add(new ShootingMechanismTask(3, 6));
        return taskList;
    }  

    public static List<TaskInterface> FORWARD_AUTO() {
        List<TaskInterface> taskList = new ArrayList<TaskInterface>();
        taskList.add(new ShootingMechanismTask(3, 6));
        taskList.add(new DrivetrainRotation(0));
        taskList.add(new IntakeMechanismTask());
        taskList.add(new EasyPathTask(new FollowPath(PathUtil.createStraightPath(120), 0.4)));
        taskList.add(new ShootingMechanismTask(3, 6));
        return taskList;
    }
    
    public static List<TaskInterface> RIGHT_SIDE_AUTO   () {
		List<TaskInterface> taskList = new ArrayList<TaskInterface>();
        taskList.add(new ShootingMechanismTask(3));
        taskList.add(new IntakeMechanismTask());
        // taskList.add(new EasyPathTask(new FollowPath(new Path(t ->
        /*{"start":{"x":94,"y":208},"mid1":{"x":179,"y":214},"mid2":{"x":267,"y":300},"end":{"x":260,"y":229}} */
        // (-711 * Math.pow(t, 2) + 480 * t + 18) / (-294 * Math.pow(t, 2) + 18 * t + 255),
        // 196.337),
        // -0.7)));
        taskList.add(new ShootingMechanismTask(3));
        return taskList;
    }

    public static List<TaskInterface> MIDDLE_AUTO() {
        List<TaskInterface> taskList = new ArrayList<TaskInterface>();
        taskList.add(new IntakeMechanismTask());
        taskList.add(new ShootingMechanismTask(3));
        taskList.add(new EasyPathTask(new FollowPath( new Path(t -> 
		/* {"start":{"x":0,"y":100},"mid1":{"x":79,"y":101},"mid2":{"x":8,"y":41},"end":{"x":156,"y":40}} */
		(360 * Math.pow(t, 2) + -366 * t + 3) / (1107 * Math.pow(t, 2) + -900 * t + 237),
		176.61),
        -0.5)));
        // Shoot 2 or a very large number?
        taskList.add(new ShootingMechanismTask(3));
        return taskList;
    }

    public static List<TaskInterface> LEFT_SIDE_AUTO() {
		List<TaskInterface> taskList = new ArrayList<TaskInterface>();
        taskList.add(new ShootingMechanismTask(3));
        taskList.add(new IntakeMechanismTask());
        // taskList.add(new EasyPathTask(new FollowPath(new Path(t ->
        /*{"start":{"x":94,"y":208},"mid1":{"x":179,"y":214},"mid2":{"x":267,"y":300},"end":{"x":260,"y":229}} */
        // (-711 * Math.pow(t, 2) + 480 * t + 18) / (-294 * Math.pow(t, 2) + 18 * t + 255),
        // 196.337),
        // -0.7)));
        taskList.add(new ShootingMechanismTask(3));
        return taskList;
    }    
}
