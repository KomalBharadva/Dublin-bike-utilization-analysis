
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MRTemperature{
	static Map<String,List<String>> bikeUsedHM = new HashMap<String,List<String>>(); // This is HM for storing Dublin bikes data.
	static Map<Double,List<String>> temperatureHM = new HashMap<Double,List<String>>(); // This is HM for storing temperature data.

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
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception // This is the main class that will be called when we run the java program.
    {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "Reduce-side-Join");
        job.setJarByClass(MRTemperature.class);
        job.setReducerClass(temperatureReducerJoin.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, dBikesMapperT.class); 
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, dTemperatureMapper.class);
        Path outputPath = new Path(args[2]);
        FileOutputFormat.setOutputPath(job, outputPath);
        outputPath.getFileSystem(conf).delete(outputPath);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
class dBikesMapperT extends Mapper <Object, Text, Text, Text> // This is a mapper class to map Dublin bikes data with unique key.
{
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
    	try
    	{
            String[] parts = value.toString().split(","); // Splitting data in string array.
            String Key1 = MRTemperature.dateParser(parts[0])+","+parts[1]; // Creating Key.
            String Value1 = MRTemperature.dateParser(parts[0])+","+parts[1]+","+parts[2]+","+parts[4]; // Creating Value using dateTime,standName,standCapacity,bikeUtilized.
            context.write(new Text(Key1), new Text("dBikes="+Value1)); // Writting context to pass this output in reducer.
    	}
    	catch(Exception e)
    	{
    		System.out.println("Exception occurred:"+e);
    	}
    }
}
class dTemperatureMapper extends Mapper <Object, Text, Text, Text> // This is a mapper class to map Weather data with unique key.
{
    public void map(Object key, Text value, Context context)throws IOException, InterruptedException{
    	try
    	{
            String[] parts = value.toString().split(","); // Splitting data in string array.
            String Key2 = MRTemperature.dateParser(parts[0]); // Creating Key.
            String Value2 = MRTemperature.dateParser(parts[0])+","+parts[2]; // Creating Value.
            context.write(new Text(Key2), new Text("dWeather=" + Value2)); // Writting contect to pass this output in reducer.
    	}
    	catch(Exception e)
    	{
    		System.out.println("Exception occurred:"+e);
    	}
    }
}
class temperatureReducerJoin extends Reducer <Text, Text, String, String> // This is a reducer class that will reduce our outputs of both mappers.
{
	public void reduce(Text key, Iterable <Text> values, Context context)
	{
		try // Handling exception.
		{
    		for(Text t:values) // This will run for all values from both mappers.
    		{
    			String ValuePair[] = t.toString().split("="); // {dBikes,[date, standName, V1, V2]}
    			String Values[] = ValuePair[1].split(","); // {date, standName/Temp, BC, UB}
    			if(ValuePair[0].equals("dBikes")) // If 1st part of array is dBikes, values are identified for dBikes.
    			{
    				if(Integer.parseInt(Values[3])>0)
    				{
	    				List<String> temp;
	    				if(MRTemperature.bikeUsedHM.containsKey(Values[1]))
	    				{
	    					temp = MRTemperature.bikeUsedHM.get(Values[1]);
	    				}
	    				else
	    				{
	    					temp = new ArrayList<String>();
	    				}
	    				if(!temp.contains(Values[0]))
	    					temp.add(Values[0]);
	    				MRTemperature.bikeUsedHM.put(Values[1], temp);
    				}
    			}
    			else if(ValuePair[0].equals("dWeather")) // If 1st part of an array is dWeather, values are identified for rain and temperature.
    			{
    				List<String> tempTemperature;
    				if(MRTemperature.temperatureHM.containsKey(Double.parseDouble(Values[1])))
    				{
    					tempTemperature = MRTemperature.temperatureHM.get(Double.parseDouble(Values[1]));
    				}
    				else
    				{
    					tempTemperature = new ArrayList<String>();
    				}
    				if(!tempTemperature.contains(Values[0]))
    					tempTemperature.add(Values[0]);
    				MRTemperature.temperatureHM.put(Double.parseDouble(Values[1]),tempTemperature);
    			}
    		}
		}
		catch(Exception e) // Catching exception.
		{
			System.out.println("Exception Occurred."); // Printing message.
			e.printStackTrace(); // Printing stack trace.
		}
    }

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException // This function will be called once reducer is complete.
	{
		for(String standName:MRTemperature.bikeUsedHM.keySet())
		{
			for(Double value:MRTemperature.temperatureHM.keySet())
			{
    			List<String> bikesTempDates = new ArrayList<String>(MRTemperature.bikeUsedHM.get(standName));
				List<String> temperatureDates = new ArrayList<String>(MRTemperature.temperatureHM.get(value));
				temperatureDates.retainAll(bikesTempDates);
				context.write(standName+","+value+","+temperatureDates.size(),"");
			}
		}
	}
}
