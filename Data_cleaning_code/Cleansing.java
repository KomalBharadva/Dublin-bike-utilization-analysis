import java.text.*;

// All the data cleansing functions will be performed here in this class.
public class Cleansing
{
	// Below method will be used to calculate distance between GARDA stations and DUBLIN bike stand location.
	// I am using Sir Haversine's formula to calculate distance offline.
	public static double calculateDistance(double lat1, double lon1, double lat2, double lon2)
	{
		double earthRadius = 6371;
		double diffInLat = Math.toRadians(lat2-lat1); // Calculating difference in Latitude of 2 locations and converting it in radians.
		double diffInLon = Math.toRadians(lon2-lon1); // Calculating difference in Longitude of 2 locations and converting it in radians.
		lat1 = Math.toRadians(lat1); // Converting Latitude1 in radians.
		lat2 = Math.toRadians(lat2); // Converting Latitude2 in radians.
		// As per Sir Haversine's formula,
		// Distance(Location1 to Location 2) in KMs = 6371*2*ATAN(SQRT(SubPart), SQRT(1-SubPart))
		// SubPartOfFormula = (Sin(Power 2 of DifferenceInLatitude)+Cos(Latitude1 in radians))*Cos(Latitude2 in radians)*Sin(Power 2 of DifferenceInLongitude)
		// Below is a part of whole Sir Haversine's Formula.
		double subPartOfFormula = Math.pow(Math.sin(diffInLat/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(diffInLon/2),2);
		// In below formula, I have multiplied the ATAN result of SubPartOfFormula with 6371*2 which is diameter of Earth.
		double formula = earthRadius*2*Math.atan2(Math.sqrt(subPartOfFormula), Math.sqrt(1-subPartOfFormula));
		DecimalFormat restrict = new DecimalFormat("#.####"); // Formating distance and convert 
		formula = Double.parseDouble(restrict.format(formula))*1000; // Converting distance in meters.
		return formula; // returning the final distance in meters.
	}

	// Below method is used to remove unwanted characters from string.
	public static String cleanString(String s)
	{
		StringBuilder sb = new StringBuilder(); // SB will be used to form a new string with valid characters(i.e. a-z and A-Z) only.
		for(int i=0;i<s.length();i++) // This loop will run for length of input string.
		{
			// In below if condition, I am converting character into ASCII value to check if character in string is valid or not.
			if(((int)s.charAt(i)>=65 && (int)s.charAt(i)<=90) || ((int)s.charAt(i)>=97 && (int)s.charAt(i)<=122) || (int)s.charAt(i) == 32)
			{
				sb.append(s.charAt(i)); // If above is true, character will be appended in StringBuilder.
			}
		}
		return sb.toString(); // returning final valid String.
	}

	// This will format date in one format.
	public static String dateParser(String timeStamp) throws Exception
	{
		if(timeStamp.contains("-")) // In case my date contains "-", parse below.
		{
			SimpleDateFormat requiredFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // This is required format.
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm"); // This will be parsing format.
			timeStamp = requiredFormat.format(sdf.parse(timeStamp));
		}
		return timeStamp; // Convert date into format and parse.
	}

	// Below method will be used to identify which time needs to be considered in weather data set as a key.
	public static String getConsiderationTime(String dateTime)
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<dateTime.length(); i++)
		{
			if(i > 13 && i != 16) // This if condition will be used to replace minutes in date format: dd/mm/yyyy hh:mm:ss.
				sb.append("0");
			else
				sb.append(dateTime.charAt(i));
		}
		return sb.toString();
	}

	// Below method will check if there are any data present in weather data set for given timeStamp.
	public static String[] identifyWData(String dateTime)
	{
		String[] temp = {};
		if(dateTime != null)
		{
			for(int i=0; i<ReadData.wData.size(); i++)
			{
				WeatherData wd = ReadData.wData.get(i);
				if(dateTime.compareTo(wd.getTimeStamp()) == 0)
				{
					temp = wd.toString().split(",");
					break;
				}
			}
		}
		return temp;
	}
}
