import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Holds information to be used by the KNN program
 * Last updated: 12/23/2012
 * @author Matthew Herbst - herbstmb@muohio.edu
 *
 */
public class KNNInfo 
{	
	//The set of all liver data read in
	private ArrayList<double[]> dataSet;
	
	//Which element in each data set is the classifier
	public final static int INDEX_OF_CLASSIFIER = 6;
	
	//How many dimensions the data is in
	public final int DIMENTIONS = 7;
	
	/**
	 * Constructs the data by reading in information from the specified file
	 * @param fileName the file to be read in
	 */
	public KNNInfo(String fileName)
	{
		dataSet = new ArrayList<double[]>();
		
		try 
		{
			Scanner in = new Scanner(new File(fileName));
			
			//Keep reading data while there is data left to read
			while(in.hasNext())
			{
				//Convert string data to the format used
				String[] temp = in.nextLine().split(",");
				
				double[] tempRecord = new double[DIMENTIONS];
				for(int i = 0; i < DIMENTIONS; i++)
				{
					tempRecord[i] = Double.parseDouble(temp[i]);
				}
				
				dataSet.add(tempRecord);
			}
			
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error connecting to file");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the set of liver data
	 * @return the set of liver data
	 */
	public ArrayList<double[]> getDataSet()
	{
		return dataSet;
	}
}