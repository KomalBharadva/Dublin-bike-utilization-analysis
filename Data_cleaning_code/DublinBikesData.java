// This is simple POJO class for structuring Dublin bikes data.

public class DublinBikesData {
	private String timeStamp;
	private String standName;
	private int standCapacity;
	private int availableBikes;
	private double dBLatitude;
	private double dBLongitude;

	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) throws Exception {
		this.timeStamp = Cleansing.dateParser(timeStamp);
	}
	public String getStandName() {
		return standName;
	}
	public void setStandName(String standName) {
		this.standName = Cleansing.cleanString(standName);
	}
	public int getStandCapacity() {
		return standCapacity;
	}
	public void setStandCapacity(String standCapacity) {
		this.standCapacity = Integer.parseInt(standCapacity);
	}
	public int getAvailableBikes() {
		return availableBikes;
	}
	public void setAvailableBikes(String availableBikes) {
		this.availableBikes = Integer.parseInt(availableBikes);
	}
	public double getdBLatitude() {
		return dBLatitude;
	}
	public void setdBLatitude(String dBLatitude) {
		this.dBLatitude = Double.parseDouble(dBLatitude);
	}
	public double getdBLongitude() {
		return dBLongitude;
	}
	public void setdBLongitude(String dBLongitude) {
		this.dBLongitude = Double.parseDouble(dBLongitude);
	}
	@Override
	public String toString() {
		return "DublinBikesData [timeStamp=" + timeStamp + ", standName=" + standName + ", standCapacity="
				+ standCapacity + ", availableBikes=" + availableBikes + ", dBLatitude=" + dBLatitude + ", dBLongitude="
				+ dBLongitude + "]";
	}
}
