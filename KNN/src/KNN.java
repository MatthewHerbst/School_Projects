import java.util.ArrayList;

/**
 * Implements a Kth-Nearest Neighbor Classifier on liver data from
 * http://mlearn.ics.uci.edu/MLRepository.html using leave one out cross-validation
 * Miami University - CSE 586 - HW5
 * Last updated: 12/23/2012
 * @author Matthew Herbst - herbstmb@muohio.edu
 *
 */
public class KNN
{
	//Each value will be tested with the KNN program
	public static final int[] ksToTry = {1, 3, 5};
	
	//The data set being used
	private static ArrayList<double[]> data;
	
	/**
	 * Main entry point for the program. Runs the classifier for each value of k
	 * @param args input arguments - none used for this program
	 */
	public static void main(String[] args)
	{
		//Saving a copy of the object data rather than the object saves a LOT of method calls
		data = new KNNInfo("bupa.data.txt").getDataSet();
		
		System.out.println("Running kth-nearest neighbor with leave one out cross validation. Data set size: " + data.size() + " elements.");
		
		//Run the program for each value of k
		for(int i = 0; i < ksToTry.length; i++)
		{
			System.out.println("Using K = " + ksToTry[i] + " accuracy is: " + runKNN(ksToTry[i]));
		}
	}
	
	/**
	 * Runs the Kth-Nearest neighbor algorithm
	 * @param k the number of points to use for the classification
	 * @return the accuracy of the classifier at successfully classifying the data
	 */
	public static double runKNN(int k)
	{
		//Used to keep track of the running accuracy
		double numCorrect = 0;
		
		//Use each data point once as the test data
		for(int i = 0; i < data.size(); i++)
		{				
			//Used to store the kth closest data points for comparison
			ArrayList<double[]> kthNearest = new ArrayList<double[]>(k);
			
			//Saves function calls
			double[] currentTestPoint = data.get(i);
			
			//Compare using distance against all the other data points
			//Goal inside this loop: Find k nearest neighbors to data[i] which will be used as judges
			for(int j = 0; j < data.size(); j++)
			{				
				//Don't compare a data point against itself!
				if(j != i)
				{
					//Base case - make sure we have at least k elements before we start comparing neighbors
					if(kthNearest.size() < k)
					{
						kthNearest.add(data.get(j));
					}
					else //See if the newest data point should replace (it is closer to data[i]) one of the neighbors
					{							
						//Find the farthest away neighbor in the list of closest neighbors
						int farthestNeighbor = 0;
						for(int f = 0; f < kthNearest.size(); f++)
						{
							if(distance(currentTestPoint, kthNearest.get(f)) > distance(currentTestPoint, kthNearest.get(farthestNeighbor)))
							{
								farthestNeighbor = f;
							}
						}						
						
						//Compare the distance between the current training data point and the farthest in the list of neighbors
						//If the distance of the new point is less, than it is a new closest neighbor, replacing the old farthest closest neighbor
						if(distance(currentTestPoint, data.get(j)) < distance(currentTestPoint, kthNearest.get(farthestNeighbor)))
						{
							kthNearest.remove(farthestNeighbor);
							kthNearest.add(data.get(j));
						}						
					}
				}
			}
			
			//Vote on the data point based on the data points in kthNearest
			//Goal for this: use the k-nearest neighbors as judges for classifying data[i]
			int votes = 0;
			for(int v = 0; v < kthNearest.size(); v++)
			{
				//If the judge and data[i] have the same classifier
				if(currentTestPoint[KNNInfo.INDEX_OF_CLASSIFIER] == kthNearest.get(v)[KNNInfo.INDEX_OF_CLASSIFIER])
				{
					votes++;
				}
				else
				{
					votes--;
				}
			}
			
			//If data[i] has received a majority of votes from the k-nearest judges
			if(votes > 0)
			{
				numCorrect++;
			}
		}
		
		//-1 to account for leave one out cross validation
		return numCorrect / (data.size() - 1);
	}
	
	/**
	 * Returns the Euclidean distance between two vectors
	 * @param a the first vector to be used
	 * @param b the second vector to be used
	 * @return the Euclidean distance between a and b
	 */
	public static double distance(double[] a, double[] b)
	{
		assert(a.length == b.length);
		
		double distance = 0;
	
		for(int i = 0; i < a.length; i++)
		{
			//So we don't count the classifier
			if(i != KNNInfo.INDEX_OF_CLASSIFIER)
			{
				distance += Math.pow(a[i] - b[i], 2);
			}
		}
		
		return Math.sqrt(distance);
	}
}