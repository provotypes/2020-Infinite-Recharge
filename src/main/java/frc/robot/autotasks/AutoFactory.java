package frc.robot.autotasks;

import java.util.ArrayList;
import java.util.List;

import frc.robot.easypath.FollowPath;
import frc.robot.easypath.PathUtil;

public class AutoFactory {

    public static List<TaskInterface> DEFAULT_AUTO() {
        List<TaskInterface> taskList = new ArrayList<TaskInterface>();
        taskList.add(new EasyPathTask(new FollowPath(PathUtil.createStraightPath(24), 0.4)));
        taskList.add(new ShootingMechanismTask(3));
        return taskList;
    }
    
    public static List<TaskInterface> RIGHT_SIDE_AUTO() {
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
        taskList.add(new ShootingMechanismTask(3));
        taskList.add(new IntakeMechanismTask());
        // taskList.add(new EasyPathTask(new FollowPath(new Path(t ->
        /*{"start":{"x":94,"y":208},"mid1":{"x":179,"y":214},"mid2":{"x":267,"y":300},"end":{"x":260,"y":229}} */
        // (-711 * Math.pow(t, 2) + 480 * t + 18) / (-294 * Math.pow(t, 2) + 18 * t + 255),
        // 196.337),
        // -0.7)));
        // Shoot 2 or a very large number?
        taskList.add(new ShootingMechanismTask(2));
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
