package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU;

public class IMUAngleTracker extends ADIS16470_IMU {

	private double XAngle = 0.0;
	private double YAngle = 0.0;
	private double ZAngle = 0.0;

	private IntegralHandler mathThread = new IntegralHandler(this);

	IMUAngleTracker() {
		super();
		mathThread.start();
	}

	class IntegralHandler extends Thread {

		IMUAngleTracker imu;

		IntegralHandler(IMUAngleTracker imu) {
			this.imu = imu;
		}

		@Override
		public void run() {
			imu.integrals();
		}
	}

	private void integrals() {
		long lastTime = System.currentTimeMillis();
		long thisTime;
		double dt = 0.0;

		double lastX = super.getGyroInstantX();
		double lastY = super.getGyroInstantY();
		double lastZ = super.getGyroInstantZ();

		double thisX = super.getGyroInstantX();
		double thisY = super.getGyroInstantY();
		double thisZ = super.getGyroInstantZ();

		while (true) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			thisX = super.getGyroInstantX();
			thisY = super.getGyroInstantY();
			thisZ = super.getGyroInstantZ();

			synchronized (this) {
				thisTime = System.nanoTime();
				dt = (thisTime - lastTime) / 1000000000.0d;

				XAngle += ((lastX + thisX) / 2) * dt;
				YAngle += ((lastY + thisY) / 2) * dt;
				ZAngle += ((lastZ + thisZ) / 2) * dt;
			}

			lastX = thisX;
			lastY = thisY;
			lastZ = thisZ;
			lastTime = thisTime;

		}
	}

	public synchronized double getXAngle() {
		return XAngle;
	}
	public synchronized double getYAngle() {
		return YAngle;
	}
	public synchronized double getZAngle() {
		return ZAngle;
	}

	@Override
	public void reset() {
		synchronized(this) {
			XAngle += 0.0;
			YAngle += 0.0;
			ZAngle += 0.0;
		}
		super.reset();
	}

}



