package frc.robot.easypath;

import java.util.function.Function;

/**
 * This class is the command you run in order to have your robot follow along a path.
 */
public class FollowPath {

  private Path path;
  private Function<Double, Double> speedFunction;
  private double shiftAt;
  private boolean shifted;

  /**
   * Constructor for the command for the robot to actually follow the path. All math is done in the
   * execute method. You should provide a Path object and a speed function in the form of a lambda
   * or method reference, for example:
   *
   * <pre>{@code
   * new FollowPath(myPath, p -> {
   *     // If we are less than 25% done with the path, drive at 30% speed
   *     if (p < 0.25) {
   *         return 0.3;
   *     }
   *     // If we are between 25% and 90% done with the path, drive at 80% speed
   *     else if (0.25 <= p && p < 90) {
   *         return 0.8;
   *     }
   *     // Otherwise drive at 25% speed
   *     else return 0.25;
   * });
   * }</pre>
   *
   * @param path the path object the robot should follow
   * @param speedFunction the function describing the speed the robot should drive along the path,
   * accepting a double representing the percentage completion of the path and returning a double
   * representing the speed the robot should drive at
   */
  public FollowPath(Path path, Function<Double, Double> speedFunction) {
    this(path, speedFunction, Double.MAX_VALUE);
  }

  /**
   * Constructor for the command for the robot to actually follow the path. See the other
   * constructor more info. The difference with this constructor is that it takes in a constant
   * speed for the robot to drive at along the entire path.
   *
   * @param path the path object the robot should follow
   * @param constantSpeed the constant speed the robot should drive at along the course of the path
   */
  public FollowPath(Path path, double constantSpeed) {
    this(path, unused -> constantSpeed);
  }

  /**
   * Constructor for the command for the robot to actually follow the path. All math is done in the
   * execute method. You should provide a Path object and a speed function in the form of a lambda
   * or method reference, for example:
   *
   * <pre>{@code
   * new FollowPath(myPath, p -> {
   *     // If we are less than 25% done with the path, drive at 30% speed
   *     if (p < 0.25) {
   *         return 0.3;
   *     }
   *     // If we are between 25% and 90% done with the path, drive at 80% speed
   *     else if (0.25 <= p && p < 90) {
   *         return 0.8;
   *     }
   *     // Otherwise drive at 25% speed
   *     else return 0.25;
   * });
   * }</pre>
   *
   * @param path the path object the robot should follow
   * @param speedFunction the function describing the speed the robot should drive along the path,
   * accepting a double representing the percentage completion of the path and returning a double
   * representing the speed the robot should drive at
   * @param shiftAt the percentage of the path that you want your drive train to shift up at; e.g.
   * 0.5 if you want to shift halfway through the path
   */
  public FollowPath(Path path, Function<Double, Double> speedFunction, double shiftAt) {
    this.path = path;
    this.speedFunction = speedFunction;
    this.shiftAt = shiftAt;
    this.shifted = false;
  }

  /**
   * Constructor for the command for the robot to actually follow the path. See the other
   * constructor more info. The difference with this constructor is that it takes in a constant
   * speed for the robot to drive at along the entire path.
   *
   * @param path the path object the robot should follow
   * @param constantSpeed the constant speed the robot should drive at along the course of the path
   * @param shiftAt the percentage of the path that you want your drive train to shift up at; e.g. *
   * 0.5 if you want to shift halfway through the path
   */
  public FollowPath(Path path, double constantSpeed, double shiftAt) {
    this(path, unused -> constantSpeed, shiftAt);
  }

  /**
   * Helper function to calculate the derivative of the path at a certain distance
   *
   * @param distanceTraveled the total distance that the robot has traveled thus far
   * @return The value of the path's derivative at the given distance traveled
   */
  private double calculateDerivativeAt(double distanceTraveled) {
    return path.getDerivative().apply(distanceTraveled / path.getLength());
  }

  /**
   * Calculate the error in angle for the robot's position - that is, given the robot's current
   * angle, calculate how far off we are from where we should be.
   *
   * @param currentAngle the current angle of the robot
   * @return the error in the robot's current angle
   */
  private double calculateAngleDelta(double currentAngle) {
    double currentSlope = Math.tan(currentAngle * Math.PI / 180.0);
    double nextSlope = calculateDerivativeAt(
        EasyPath.getConfig().getGetInchesTraveledFunction().get());

    return Math.atan(
        (nextSlope - currentSlope) /
            (1 + currentSlope * nextSlope)
    ) * 180.0 / Math.PI;
  }

  /**
   * Calculate what speed the robot should be driving at at the current time.
   *
   * @return the speed the robot should be traveling at
   */
  private double calculateSpeed() {
    return -speedFunction
        .apply(EasyPath.getConfig().getGetInchesTraveledFunction().get() / path.getLength());
  }

  /**
   * Calls the reset encoders function that you provided. {@inheritDoc}
   */
  public void initialize() {
    EasyPath.getConfig().getResetEncodersAndGyroFunction().run();
  }

  /**
   * Calculates how the robot should drive and applies it to the drive train via the method that you
   * provided to EasyPath. {@inheritDoc}
   */
  public void execute() {
    EasyPathConfig config = EasyPath.getConfig();
    double error = -calculateAngleDelta(config.getGetCurrentAngleFunction().get());
    double speed = calculateSpeed();

    if (!shifted && config.getGetInchesTraveledFunction().get() >= shiftAt) {
      config.getShiftDriveTrainFunction().run();
      shifted = true;
    }

    double leftSpeed, rightSpeed;

    // quick disclaimer
    // Not 100% why this if statement is necessary
    // Sometimes it doesn't work if you exclude it...
    // Basically just says "drive straight for 3 inches then start turning"
    // will do further testing soon-ish...
    if (Math.abs(config.getGetInchesTraveledFunction().get()) > 3) {
      double leftVariation = error * Math.abs(speed) * config.getkP();
      double rightVariation = error * Math.abs(speed) * config.getkP();

      if (config.isSwapTurningDirection()) {
        leftSpeed = speed - leftVariation;
        rightSpeed = speed + rightVariation;
      } else {
        leftSpeed = speed + leftVariation;
        rightSpeed = speed - rightVariation;
      }

    } else {
      leftSpeed = speed;
      rightSpeed = speed;
    }

    if (config.isSwapDrivingDirection()) {
      leftSpeed *= -1;
      rightSpeed *= -1;
    }

    config.getSetLeftRightDriveSpeedFunction().accept(leftSpeed, rightSpeed);
  }

  /**
   * Checks if the robot has driven further than the total length of the path, and if it has, then
   * it's finished. If an exception is thrown during that check, the path will end prematurely in
   * order to prevent the robot from going nuts. {@inheritDoc}
   *
   * @return true if the robot has driven the total length of the path; false otherwise
   */
  public boolean isFinished() {
    try {
      return Math.abs(EasyPath.getConfig().getGetInchesTraveledFunction().get()) > path.getLength();
    } catch (Exception e) {
      // todo: log?
      return true;
    }
  }

  /**
   * At the end of the command, set the left and right drive train speeds to 0. {@inheritDoc}
   */
  public void end() {
    EasyPath.getConfig().getSetLeftRightDriveSpeedFunction().accept(0.0, 0.0);
  }

}
