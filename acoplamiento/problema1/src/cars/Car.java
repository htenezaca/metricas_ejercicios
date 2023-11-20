package cars;

public class Car {
	private CarMotor motor;
	private CarDashboard dashboard;

	public Car() {
	}

	public void setMotor(CarMotor motor) {
		this.motor = motor;
	}

	public void setDashboard(CarDashboard dashboard) {
		this.dashboard = dashboard;
	}

	public void accelerate() {
		if (motor != null) {
			motor.accelerate();
		}
	}

	public void stop() {
		if (motor != null) {
			motor.stop();
		}
	}

	public void displayDashboard() {
		if (dashboard != null) {
			dashboard.printDashboard();
		}
	}

	public static void main(String[] args) {
		Car car = new Car();
		CarMotor motor = new CarMotor();
		CarDashboard dashboard = new CarDashboard(motor);

		car.setMotor(motor);
		car.setDashboard(dashboard);

		car.displayDashboard();
		car.accelerate();
		car.displayDashboard();
		car.stop();
		car.displayDashboard();
	}
}
