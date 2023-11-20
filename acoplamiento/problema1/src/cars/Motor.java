package cars;

public interface Motor {
	void accelerate();

	void stop();

	int getSpeed();

	int getRpm();

	float getOilLevel();

	float getGasLevel();
}