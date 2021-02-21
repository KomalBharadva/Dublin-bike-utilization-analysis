import java.io.*;
import java.text.*;
import java.util.*;

public class ReadData {
	// Declaration of Global variable starts.
	private String filePath = "";
	private String fileName = "";
	private String finalPath = "";
	protected static List<TrafficLightsData> tlData = new ArrayList<TrafficLightsData>();
	public static List<WeatherData> wData = new ArrayList<WeatherData>();
	private String fNFE = "File not found on given location. Please check if file path and file name entered are correct and if file is present in given file path.";
	private String waitMessage = "Please wait while this program is cleansing the data from given csv file.";
	private String exceptionMessage = "Exception occurred while cleansing the data. Please close the file in case it is already open and try again.";
	private String successMessage = "File has been successfully cleansed and new csv file created is stored to: ";
	Scanner sc;
	// Declaration of Global variable ends.

	// Constructor declaration starts.
	public ReadData(String filePath)
	{
		this.filePath = filePath;
		this.successMessage = this.successMessage+this.filePath;
	}
	public ReadData() {}
	// Constructor declaration ends.

	// Below is a method to read GardaStation csv file, cleans data and write a new csv file with valid data to path given by user.
	public void readTLData()
	{
		sc = new Scanner(System.in); // Creating object of Scanner class to scan line given by user.
		System.out.println("Please enter Traffic Lights data csv file name with extention.");
		System.out.println("Eg.: TrafficLights.csv");
		while(fileName.length() == 0) // This loop will run unless user gives some input for file name.
		{
			fileName = sc.nextLine(); // Reading file name.
		}
		finalPath = filePath+fileName; // Final file path is file path plus file name. Eg.: D:/NCI/Assignments/CSV_Files/TrafficLights.csv.
		fileName = ""; // Deleting fileName to take input for another file.

		File fileReference = new File(finalPath);
		if(fileReference.isFile()) // Checking if filename entered exists on given file path or not.
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(finalPath));
				String rowData = br.readLine();
				String[] eachData;
				System.out.println(waitMessage);
				while((rowData = br.readLine())!=null) // This loop will run for all rows in csv.
				{
					TrafficLightsData tlObject = new TrafficLightsData(); // This is object of TrafficLightsData class.
					eachData = rowData.split(","); // Splitting each row of csv into string array.
					tlObject.setSignalName(eachData[1]); // Signal Name
					tlObject.setLatitude(eachData[4]); // Signal Latitude
					tlObject.setLongitude(eachData[5]); // Signal Longitude
					tlData.add(tlObject); // Storing object of traffic light each row data from csv into list. This will further be used to cleans dublin bikes data.
				}
				br.close();
				System.out.println("Traffic lights data has been successfully cleansed.");
			}
			catch(Exception e)
			{
				System.out.println(exceptionMessage);
				e.printStackTrace();
			}
		}
		else
			System.out.println(fNFE);
	}

	// Below is a method to read Dublin Weather csv file, cleans data and write a new csv file with valid data to path given by user.
	public void readWData()
	{
		sc = new Scanner(System.in);
		System.out.println();
		System.out.println("Please enter weather data csv file name with extention.");
		System.out.println("Eg.: WeatherData.csv");
		while(fileName.length() == 0) // This loop will run unless user gives some input for file name.
		{
			fileName = sc.nextLine(); // Reading file name.
		}
		finalPath = filePath+fileName; // Final file path is file path plus file name. Eg.: D:/NCI/Assignments/CSV_Files/DublinWeatherData.csv.
		fileName = "";
		File fileReference = new File(finalPath);
		if(fileReference.isFile())
		{
			try
			{
				StringBuilder sbWD = new StringBuilder();
				FileWriter fw = new FileWriter(new File(this.filePath+"DublinWeatherFinal.csv"));

//				// Creating header for new csv file.
//				fw.append("timeStamp");
//				fw.append(",");
//				fw.append("Rain");
//				fw.append(",");
//				fw.append("Temperature");
//				fw.append("\n");
//				// Header created.

				BufferedReader br = new BufferedReader(new FileReader(finalPath));
				String rowData = br.readLine();
				String[] eachData;
				System.out.println(waitMessage);
				while((rowData = br.readLine())!=null) // This loop will run for all the records in csv file.
				{
					WeatherData wd = new WeatherData(); // This is an object of WeatherData class.
					eachData = rowData.split(","); // Converting each row of csv into string array.
					wd.setTimeStamp(eachData[0]); // TimeStamp
					wd.setRain(eachData[2]); // Rain
					wd.setTemperature(eachData[4]); // Temperature
					wData.add(wd);
					// Creating record using StringBuilder to write in new CSV file.
					for(int i=0; i<12; i++) // This for loop will be used to convert Weather data into per 5 minutes data.
					{
						String time = wd.getTimeStamp();
						StringBuilder sb = new StringBuilder();
						for(int j=0; j<time.length(); j++)
						{
							if(j > 13 && j < 16) // This if condition will be used to replace minutes in date format: dd/mm/yyyy hh:mm:ss.
							{
								if(j == 14 && i*5 == 0 && i*5 == 5)
									sb.append("0"+i*5);
								else if(j != 15)
									sb.append(i*5);
							}
							else
								sb.append(time.charAt(j));
						}
						time = sb.toString();
						sbWD.append(time); // TimeStamp.
						sbWD.append(",");
						sbWD.append(wd.getRain()); // Rain.
						sbWD.append(",");
						sbWD.append(wd.getTemperature()); // temperature.
						sbWD.append("\n");
						// Record created.
						fw.append(sbWD.toString()); // Writing in new CSV file.
						sbWD.setLength(0); // Clearing StringBuilder.
					}
				}
				br.close();
				fw.flush(); // Flushing file writter for Weather data.
				fw.close(); // Closing file writter for Weather data.
				System.out.println("Weather data has been successfully cleansed.");
			}
			catch(Exception e)
			{
				System.out.println(exceptionMessage);
				e.printStackTrace();
			}
		}
		else
			System.out.println(fNFE);
	}

	// Below is a method to read Dublin Bikes csv file, cleans data and write a new csv file with valid data to path given by user.
	public void readDBData()
	{
		Map<Integer, Double> standNear = new HashMap<Integer, Double>(); // This hashmap will store already calculated distance of stands from traffic lights.
		int fileNumber = 0; // This will contain number of files to read dublin bike data from.
		String[] wdTemp = {}; // This will contain weather data. This is blank initially and will be filled once all below mentioned conditions satisfies.
		sc = new Scanner(System.in);
		while(fileNumber <= 0) // This loop will run unless user gives some input for file count.
		{
			System.out.println();
			System.out.println("Please enter number of csv files for Dublin bikes data.");
			System.out.println("Eg.: 1");
			fileNumber = Integer.parseInt(sc.nextLine()); // Reading file count.
		}
		for(int count = 1; count<=fileNumber; count++)
		{
			System.out.println();
			System.out.println("Please enter Dublin bikes data csv file name with extention.");
			System.out.println("Eg.: DublinBikes.csv");
			while(fileName.length() == 0) // This loop will run unless user gives some input for file name.
			{
				fileName = sc.nextLine(); // Reading file name.
			}
			finalPath = filePath+fileName; // Final file path is file path plus file name. Eg.: D:/NCI/Assignments/CSV_Files/DublinBikes.csv.
			fileName = ""; // Deleting fileName to take input for another file.
			File fileReference = new File(finalPath);
			if(fileReference.isFile()) // In case file is found on given path, do as below.
			{
				try
				{
					BufferedReader br = new BufferedReader(new FileReader(finalPath));
					FileWriter fw;
					if(count == 1)
					{
						fw = new FileWriter(new File(this.filePath+"DublinBikesFinal.csv"));
//						// Writing file header.
//						fw.append("timeStamp");
//						fw.append(",");
//						fw.append("standName");
//						fw.append(",");
//						fw.append("standCapacity");
//						fw.append(",");
//						fw.append("availableBikes");
//						fw.append(",");
//						fw.append("utilizedBikes");
//						fw.append("\n");
//						// File header ends here.
					}
					else
					{
						fw = new FileWriter(new File(this.filePath+"DublinBikesFinal.csv"), true);
					}
					String readRowData = br.readLine();
					String[] eachData;
					StringBuilder sb = new StringBuilder();
					int previousDBStand = 0;
					boolean near = false;
					boolean considerData = false;
					System.out.println(waitMessage);
					while((readRowData = br.readLine())!=null) // This loop will run for all records in our csv file.
					{
						DublinBikesData dBDObject = new DublinBikesData(); // Object will be created for each record in our csv file.
						eachData = readRowData.split(","); // Splitting data into array of strings.
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // I will convert date from this format to below format.
						SimpleDateFormat requiredFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // I need date in this format.
						if(eachData[1].contains("-"))
						{
							eachData[1] = requiredFormat.format(sdf.parse(eachData[1])); // Converting date and storing in same array position.
						}
						if(eachData[1].length() != 0 && eachData[3].length() != 0 && eachData[4].length() != 0 && eachData[6].length() != 0) // Checking if all the fields are not blank.
						{
							if(Integer.parseInt(eachData[0]) == previousDBStand && near) // In case our current Dublin bike stand number is same as previous one and if it is near to decided distance from traffic light, do as below.
							{
								wdTemp = Cleansing.identifyWData(Cleansing.getConsiderationTime(eachData[1])); // This will return array of data with rain and temperature values.
								if(wdTemp.length > 0) //  In case we do not want to consider dates with no rainfall, put condition " && Double.parseDouble(wdTemp[1]) > 0" in if loop.
								{
									considerData = true; // Set consider to true in case no data is missing from above mentioned fields.
								}
								else
								{
									considerData = false; // Set consider to false so that current record is skipped.
								}
							}
							else if(Integer.parseInt(eachData[0]) != previousDBStand) // In case above if condition is false, check if dublin bike stand is near to any traffic light we have data for.
							{
								near = false; // Resetting near for different stand id.
								if(standNear.containsKey(Integer.parseInt(eachData[0]))) // If we find the standNumber in hashmap of already calculated distance, do as below.
								{
									near = true;
									wdTemp = Cleansing.identifyWData(Cleansing.getConsiderationTime(eachData[1])); // This will return array of data with rain and temperature values.
									if(wdTemp.length > 0) //  In case we do not want to consider dates with no rainfall, put condition " && Double.parseDouble(wdTemp[1]) > 0" in if loop.
									{
										considerData = true; // Set consider to true.
									}
									else
									{
										considerData = false; // Set consider to false so that current record is skipped.
									}
								}
								else // In case standNumber is not present in hashmap, do below calculation and store data in hashmap.
								{
									for(int i=0; i<tlData.size();i++) // This loop runs to check distance of Dublin bike stand from all traffic lights.
									{
										TrafficLightsData tl = tlData.get(i);
										double dist = Cleansing.calculateDistance(tl.getLatitude(), tl.getLongitude(), Double.parseDouble(eachData[9]), Double.parseDouble(eachData[10]));
										if(dist<=25) // In case distance is less than or equal to given distance in meters, do as below.
										{
											near = true; // Set near variable to true.
											standNear.put(Integer.parseInt(eachData[0]), dist);
											wdTemp = Cleansing.identifyWData(Cleansing.getConsiderationTime(eachData[1])); // This will return array of data with rain and temperature values.
											if(wdTemp.length > 0) //  In case we do not want to consider dates with no rainfall, put condition " && Double.parseDouble(wdTemp[1]) > 0" in if loop.
											{
												considerData = true; // Set consider to true.
												break; // Break from this loop.
											}
											else
											{
												considerData = false; // Set consider to false so that current record is skipped.
											}
										}
										else // Else if distance calculated is more than given distance in meters, do as below.
										{
											considerData = false; // Set consider to false so that current record is skipped.
										}
									}
								}
							}
						}
						else // In case any of fields are blank, do not consider data.
						{
							considerData = false; // Set consider to false so that current record is skipped.
						}
						if(considerData) // In case above checks defines current record to be consider, do as below else skip current record.
						{
							dBDObject.setTimeStamp(eachData[1]); // timeStamp
							dBDObject.setStandName(eachData[3]); // dublinBikesStandName
							dBDObject.setStandCapacity(eachData[4]); // standCapacity
							dBDObject.setAvailableBikes(eachData[6]); // availableBikes
							dBDObject.setdBLatitude(eachData[9]); // latitude
							dBDObject.setdBLongitude(eachData[10]); // longitude

							// Creating record to write in new CSV.
							sb.append(dBDObject.getTimeStamp()); // TimeStamp.
							sb.append(",");
							sb.append(dBDObject.getStandName()); // StandName.
							sb.append(",");
							sb.append(dBDObject.getStandCapacity()); // Stand Total Capacity.
							sb.append(",");
							sb.append(dBDObject.getAvailableBikes()); // Available Bikes at Stand.
							sb.append(",");
							sb.append(dBDObject.getStandCapacity() - dBDObject.getAvailableBikes()); // Utilized Bikes from Stand.
							sb.append("\n");
							// Record created.
							fw.append(sb.toString()); // Writing data in new CSV file.
							sb.setLength(0); // Clearing StringBuilder for next record.
							considerData = false; // Change consider to false.
						}
						previousDBStand = Integer.parseInt(eachData[0]); // Using this previous stand number, we identify if next record belongs for same stand or not.
					}
					fw.flush(); // Flushing file writter for DB data.
					fw.close(); // Closing file writter for DB data.
					br.close(); // Closing bufferred reader.
					System.out.println(successMessage+"DublinBikesFinal.csv");
				}
				catch(Exception e)
				{
					System.out.println(exceptionMessage); // Printing exception message.
					e.printStackTrace(); // Printing the stack trace.
				}
			}
			else // In case file is not found on given path, print below message.
			{
				System.out.println(fNFE);
			}
		}
	}
}
