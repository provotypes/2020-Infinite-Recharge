package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/* // Fix this to match 2020 game
 * This class gives added functionality to the base WPILib Joystick and allows us to bind actions to buttons and axes.
 * You will need to call {@code run()} on each {@code periodic()} to get the actions to run.
 *
 * The intended use is something like this:
 *
 * <pre>
 * {@code
 * LogitechGamepadController driverController = new LogitechGamepadController(0);
 *
 * driverController.bindButton(LogitechGamepadController.A_BUTTON, shooter::shoot);
 * driverController.bindButton(LogitechGamepadController.B_BUTTON, () -> intake.intake());
 * driverController.bind(driverController.getXButton, arm::moveToPosition);
 * driverController.bindAxes(
 * 		LogitechGamepadController.LEFT_X_AXIS,
 * 		LogitechGamepadController.RIGHT_Y_AXIS,
 * 		drivetrain::arcadeDrive);
 * }
 * </pre>
 *
 * In Java's functional interface, you can pass in functions like this:
 *
 * <pre>
 * {@code
 * (parameter, parameter) -> {
 * 		// Do some stuff
 * 		return value;
 * }
 * }
 * </pre>
 *
 * Or like this:
 * <pre>
 * {@code
 * (parameter, parameter) -> return value
 * }
 * </pre>
 *
 * Java's compiler will also let you do things like {@code shooter::shoot} if it fulfills the required function
 * signature (i.e., if it takes no parameters and doesn't return anything, you can use that for any Runnable).
 *
 * Note how much cleaner this is than a bunch of if statements.
 */
public abstract class BindableJoystick extends Joystick {

	/**
	 * JavaFX is not included in OpenJDK 11, so we include this class here in lieu of a Pair.
	 */
	class BindableAction {

		BooleanSupplier condition;
		Runnable action;

		/**
		 * Create a bindable action.
		 * @param condition This supplier must answer the question "should we run this action?"  Ideally, this is a call
		 *                  to a method on the joystick.
		 * @param action 	What action should I call when the condition is true?"  This probably should be an instance
		 *                  method of a subsystem.
		 */
		public BindableAction(BooleanSupplier condition, Runnable action) {
			this.condition = condition;
			this.action = action;
		}

		/**
		 * Get the condition for this BindableAction.
		 * @return The condition on which running the action is predicated.
		 */
		public BooleanSupplier getCondition() {
			return condition;
		}

		/**
		 * Get the action for this BindableAction.
		 * @return The action which is associated with the condition.
		 */
		public Runnable getAction() {
			return action;
		}
	}

	private List<BindableAction> boundActions = new ArrayList<>();

	/**
	 * Defer to parent constructor.
	 * @param port The controller identifier on the Driver Station.
	 */
	public BindableJoystick(int port) {
		super(port);
	}

	/**
	 * Bind an action to a button.  Whenever the button is held, the action will be run.
	 * @param button The button number for the Joystick in use.
	 * @param action An action (function) to execute when and while the button is pressed (whenever
	 *               {@code getRawButton()} is true).
	 */
	public void bindButton(int button, Runnable action) {
		bind(() -> super.getRawButton(button), action);
	}

	/**
	 * Bind an action to a button press.  Whenever the button is pressed, the action will be run.
	 * This is diffrent from {@code bindButton()} in that it only runs once when the button is pressed,
	 * not continually.  See {@code bindButtonRelease()}.
	 * @param button The button number for the Joystick in use.
	 * @param action An action (function) to execute when the button is pressed (whenever
	 *               {@code getRawButtonPressed()} is true).
	 */
	public void bindButtonPress(int button, Runnable action) {
		bind(() -> super.getRawButtonPressed(button), action);
	}

	/**
	 * Bind an action to a button release.  Whenever the button is released, the action will be run.
	 * This is different from the {@code bindButton()} in that it only runs once when the button is pressed,
	 * not continually.  See {@code bindButtonPress()}.
	 * @param button
	 * @param action
	 */
	public void bindButtonRelease(int button, Runnable action) {
		bind(() -> super.getRawButtonReleased(button), action);
	}

	/**
	 * Bind two actions to a button press and release; typically used for toggling.  This will run only once
	 * when pressed and only once when released, not continually.
	 * @param button
	 * @param actionOnPress
	 * @param actionOnRelease
	 */
	public void bindButtonToggle(int button, Runnable actionOnPress, Runnable actionOnRelease) {
		bindButtonPress(button, actionOnPress);
		bindButtonRelease(button, actionOnRelease);
	}

	/**
	 * Bind two axes to an action that requires two doubles.  Main use case here is for driving.  This action will
	 * always be run when {@code run()} is called.  Be sure that you correctly order axis1 and axis2.
	 *
	 * This action will always be run when run() is called.
	 * @param axis1 The first double for the action comes from this axis.
	 * @param axis2 The second double for the action comes from this axis.
	 * @param action The action to run.  Must take in only two doubles.
	 */
	public void bindAxes(int axis1, int axis2, BiConsumer<Double, Double> action) {
		bind(() -> action.accept(super.getRawAxis(axis1), super.getRawAxis(axis2)));
	}

	/**
	 * Bind an axis to an action that requires a double.  This action will always be run when {@code run()} is called.
	 * @param axis The double for the action comes from this axis.
	 * @param action The action to run.  Must take in only one double.
	 */
	public void bindAxis(int axis, Consumer<Double> action) {
		bind(() -> action.accept(super.getRawAxis(axis)));
	}

	/**
	 * Bind an action to the joystick which will always occur when {@code run()} is called.  While this allows someone
	 * to bind non-controller functionality to run an arbitrary action, this is discouraged.
	 * @param action An action to run each time the joystick is called.
	 */
	public void bind(Runnable action) {
		boundActions.add(new BindableAction(() -> true, action));
	}

	/**
	 * Bind an action to the joystick which will run when the condition is satisfied.  While this allows someone to bind
	 * non-controller functionality to run an arbitrary action, this is discouraged.
	 * @param condition A generic condition upon which the action is conditioned.
	 * @param action A function that is executed when the condition is true.
	 */
	public void bind(BooleanSupplier condition, Runnable action) {
		boundActions.add(new BindableAction(condition, action));
	}

	/**
	 * Iterate through and run actions if their conditions are met.  This function must be called to run actions.
	 */
	public void run() {
		for (BindableAction bindableAction : boundActions) {
			// System.out.println("Found " + bindableAction.getAction());
			if (bindableAction.getCondition().getAsBoolean()) {
				// System.out.println("Running " + bindableAction.getAction());
				bindableAction.getAction().run();
			}
		}
	}

	/**
	 * Clears all actions from the list of bound actions.
	 */
	public void clear() {
		boundActions.clear();
	}
}