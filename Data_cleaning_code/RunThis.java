// Author: Komal Riddhish Bharadva

import java.util.*;

public class RunThis
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("Author: Komal Riddhish Bharadva");

		// Declaring required variables.
		String filePath = "";
		Scanner sc = new Scanner(System.in); // Creating object of Scanner class to scan line given by user.
		// Declaration of variables ends here.

		System.out.println();
		System.out.println("Please ensure to copy all the csv files in same folder/directory before running this program.");
		System.out.println("Please enter csv file path without file name and extention.");
		System.out.println("Eg.: D:/NCI/Assignments/CSV_Files/");
		while(filePath.length() == 0) // This loop will run unless user gives some input for file name.
		{
			filePath = sc.nextLine(); // Reading File Path.
		}
		if(filePath.charAt(filePath.length()-1) != '/') // Checking in case file path does not have '/' at end, append it.
			filePath = filePath+'/'; // Appending required character.
		ReadData rd = new ReadData(filePath); // Passing file path to constructor while creating an object of ReadData class.
		rd.readTLData(); // Calling Function to read traffic lights csv file.
		rd.readWData(); // Calling Function to read weather data csv file.
		rd.readDBData(); // Calling Function to read dublin bikes csv file.
		sc.close(); // Closing Scanner class object to avoid resource leak.
	}
}
