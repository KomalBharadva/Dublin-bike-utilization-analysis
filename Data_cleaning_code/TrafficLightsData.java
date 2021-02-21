// This is simple POJO class used to structure Traffic Lights Data.
public class TrafficLightsData {
	private String signalName;
	private double latitude;
	private double longitude;

	public String getSignalName() {
		return signalName;
	}
	public void setSignalName(String signalName) {
		this.signalName = Cleansing.cleanString(signalName);
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = Double.parseDouble(latitude);
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = Double.parseDouble(longitude);
	}
	@Override
	public String toString() {
		return "TrafficLightsData [signalLocation=" + signalName + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
}
