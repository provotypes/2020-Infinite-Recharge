package frc.robot;


/**
 * Describes a Logitech Gamepad Controller.  This type of controller is used
 * for driver control, and it simplifies and abstracts the raw joystick.
 */
public class LogitechDriverController extends BindableJoystick {

	//Buttons
	public static final int A_BUTTON = 1;
	public static final int B_BUTTON = 2;	
	public static final int X_BUTTON = 3;
	public static final int Y_BUTTON = 4;
	public static final int LEFT_BUMPER = 5;
	public static final int RIGHT_BUMPER = 6;
	public static final int START_BUTTON = 8;
	public static final int LEFT_STICK_IN = 9;

	//Axis
	public static final int LEFT_X_AXIS = 0;
	public static final int LEFT_Y_AXIS = 1;
	public static final int LEFT_TRIGGER = 2;
	public static final int RIGHT_X_AXIS = 4;
	public static final int RIGHT_Y_AXIS = 5;
	public static final int RIGHT_TRIGGER = 3;

	public LogitechDriverController(int pos) {
		super(pos);
	}

	public boolean getAButton() {
		return super.getRawButton(A_BUTTON);
	}

	public boolean getBButton() {
		return super.getRawButton(B_BUTTON);
	}

	public boolean getXButton() {
		return super.getRawButton(X_BUTTON);
	}

	public boolean getYButton() {
		return super.getRawButton(Y_BUTTON);
	}

	public boolean getLeftBumper() {
		return super.getRawButton(LEFT_BUMPER);
	}

	public boolean getRightBumper() {
		return super.getRawButton(RIGHT_BUMPER);
	}

	public boolean getStart() {
		return super.getRawButton(START_BUTTON);
	}

	public boolean getLeftJoystick() {
		return super.getRawButton(LEFT_STICK_IN);
	}

	public double getLeftX() {
		return super.getRawAxis(LEFT_X_AXIS);
	}

	public double getLeftY() {
		return super.getRawAxis(LEFT_Y_AXIS);
	}

	public double getLeftTrigger() {
		return super.getRawAxis(LEFT_TRIGGER);
	}

	public double getRightX() {
		return super.getRawAxis(RIGHT_X_AXIS);
	}

	public double getRightY() {
		return super.getRawAxis(RIGHT_Y_AXIS);
	}

	public double getRightTrigger() {
		return super.getRawAxis(RIGHT_TRIGGER);
	}
}


