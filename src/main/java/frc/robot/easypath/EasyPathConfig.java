package frc.robot.easypath;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EasyPathConfig {

  private BiConsumer<Double, Double> setLeftRightDriveSpeedFunction;
  private Supplier<Double> getInchesTraveledFunction;
  private Supplier<Double> getCurrentAngleFunction;
  private Runnable shiftDriveTrainFunction;
  private Runnable resetEncodersFunction;
  private double kP;

  private boolean swapTurningDirection;
  private boolean swapDrivingDirection;

  /**
   * A configuration object that represents your drive train's controls. If you are unsure of how to
   * reference functions as a Java parameter/argument, please see the example code that is linked in
   * the readme of the project on GitHub.
   *
   * @param setLeftRightDriveSpeedFunction the function that sets the left and right drive train
   * speeds; takes in two doubles for arguments and returns nothing
   * @param getInchesTraveledFunction the function that returns a double that represents the total
   * distance traveled by the robot in inches; if you do not have one, you can use a function that
   * simply calls {@link PathUtil#defaultLengthDrivenEstimator PathUtil.defaultLengthDrivenEstimator}
   * with the two values of your encoders.
   * @param getCurrentAngleFunction the function that returns a double that represents the current
   * angle of the robot, from 0 degrees to 360 degrees.
   * @param resetEncodersAndGyroFunction the function that takes no arguments and returns nothing,
   * and resets your encoders and your gyro. You MUST reset your gyro heading to 0 between every
   * path.
   * @param kP the kP value used to tune the control loop. Please see the tuning section in the
   * readme for more details on tuning.
   */
  public EasyPathConfig(
      BiConsumer<Double, Double> setLeftRightDriveSpeedFunction,
      Supplier<Double> getInchesTraveledFunction,
      Supplier<Double> getCurrentAngleFunction,
      Runnable resetEncodersAndGyroFunction,
      double kP
  ) {
    this(setLeftRightDriveSpeedFunction, getInchesTraveledFunction,
        getCurrentAngleFunction,
        () -> {},
        resetEncodersAndGyroFunction,
        kP);
  }

  /**
   * A configuration object that represents your drive train's controls. If you are unsure of how to
   * reference functions as a Java parameter/argument, please see the example code that is linked in
   * the readme of the project on GitHub.
   *
   * @param setLeftRightDriveSpeedFunction the function that sets the left and right drive train
   * speeds; takes in two doubles for arguments and returns nothing
   * @param getInchesTraveledFunction the function that returns a double that represents the total
   * distance traveled by the robot in inches; if you do not have one, you can use a function that
   * simply calls {@link PathUtil#defaultLengthDrivenEstimator PathUtil.defaultLengthDrivenEstimator}
   * with the two values of your encoders.
   * @param getCurrentAngleFunction the function that returns a double that represents the current
   * angle of the robot, from 0 degrees to 360 degrees.
   * @param shiftDriveTrainFunction the function that takes no arguments and returns nothing, and
   * shifts your drive train into high gear. Use the other constructor if you do not shift.
   * @param resetEncodersAndGyroFunction the function that takes no arguments and returns nothing,
   * and resets your encoders and your gyro. You MUST reset your gyro heading to 0 between every
   * path.
   * @param kP the kP value used to tune the control loop. Please see the tuning section in the
   * readme for more details on tuning.
   */
  public EasyPathConfig(
      BiConsumer<Double, Double> setLeftRightDriveSpeedFunction,
      Supplier<Double> getInchesTraveledFunction,
      Supplier<Double> getCurrentAngleFunction,
      Runnable shiftDriveTrainFunction,
      Runnable resetEncodersAndGyroFunction,
      double kP
  ) {
    this.setLeftRightDriveSpeedFunction = setLeftRightDriveSpeedFunction;
    this.getInchesTraveledFunction = getInchesTraveledFunction;
    this.getCurrentAngleFunction = getCurrentAngleFunction;
    this.shiftDriveTrainFunction = shiftDriveTrainFunction;
    this.resetEncodersFunction = resetEncodersAndGyroFunction;
    this.kP = kP;
  }

  /**
   * @return the function that sets the left and right drive train speeds
   */
  public BiConsumer<Double, Double> getSetLeftRightDriveSpeedFunction() {
    return setLeftRightDriveSpeedFunction;
  }

  /**
   * @return the function that returns the total distance traveled, in inches
   */
  public Supplier<Double> getGetInchesTraveledFunction() {
    return getInchesTraveledFunction;
  }

  /**
   * @return the function that returns the current angle of the robot
   */
  public Supplier<Double> getGetCurrentAngleFunction() {
    return getCurrentAngleFunction;
  }

  /**
   * @return the function that resets the encoders and gyro of the robot. This MUST reset the gyro
   * to zero.
   */
  public Runnable getResetEncodersAndGyroFunction() {
    return resetEncodersFunction;
  }

  /**
   * @return the kP value used in the control loop for arcing along the path
   */
  public double getkP() {
    return kP;
  }

  /**
   * @return the function that shifts the drive train of your robot into high gear
   */
  public Runnable getShiftDriveTrainFunction() {
    return shiftDriveTrainFunction;
  }

  /**
   * Tell EasyPath to swap the direction that it turns/arcs. Since sometimes you might wire
   * something backwards, you can use this to fix it if you want (or if EasyPath has a wrong
   * negative sign somewhere...).
   *
   * @param swapTurningDirection whether or not to turn/arc in the opposite direction as normal
   */
  public void setSwapTurningDirection(boolean swapTurningDirection) {
    this.swapTurningDirection = swapTurningDirection;
  }

  /**
   * Tell EasyPath to swap the direction that it drives forwards. Sometimes you might wire something
   * backwards, or EasyPath might have a stray negative sign lying around.
   *
   * @param swapDrivingDirection whether or not to drive forwards in the opposite direction as
   * normal
   */
  public void setSwapDrivingDirection(boolean swapDrivingDirection) {
    this.swapDrivingDirection = swapDrivingDirection;
  }


  /**
   * Getter for configuration option swapTurningDirection.
   *
   * @return true if EasyPath should turn/arc in the opposite direction as normal, false otherwise.
   */
  public boolean isSwapTurningDirection() {
    return swapTurningDirection;
  }

  /**
   * Getter for configuration option swapDrivingDirection.
   *
   * @return true if EasyPath should drive "forwards" in the opposite direction as normal, false
   * otherwise.
   */
  public boolean isSwapDrivingDirection() {
    return swapDrivingDirection;
  }

}