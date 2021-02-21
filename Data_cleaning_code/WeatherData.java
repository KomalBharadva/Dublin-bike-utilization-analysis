// This is simple POJO class for structuring Weather data.

public class WeatherData
{
	private String timeStamp;
	private double rain;
	private double temperature;

	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) throws Exception {
		this.timeStamp = Cleansing.dateParser(timeStamp);
	}
	public double getRain() {
		return rain;
	}
	public void setRain(String rain) {
		this.rain = Double.parseDouble(rain);
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = Double.parseDouble(temperature);
	}
	@Override
	public String toString() {
		return timeStamp + ","+ rain + "," + temperature;
	}
}
