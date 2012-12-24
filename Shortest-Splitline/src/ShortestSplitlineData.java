import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Holds information related to the redistricting problem 
 * Implemented using the shortest-splitline algorithm
 * Last updated: 12/23/2012
 * @author Matthew Herbst - herbstmb@muohio.edu *
 */
public class ShortestSplitlineData 
{
	private ArrayList<Point2D.Double> vertices;
	private ArrayList<Line> edges;
	private ArrayList<Point2D.Double> population;
	private int numDistricts;
	
	/**
	 * Constructs the object by reading information from the specified file
	 * @param fileName the path of the file to be read
	 */
	public ShortestSplitlineData(String fileName)
	{
		//Read in all the data
		try 
		{
			Scanner in = new Scanner(new File(fileName));
			
			int numVertices = Integer.parseInt(in.nextLine());
			
			if(numVertices < 3)
			{
				System.out.println("Bad input: polygon must have at least 3 points. Exiting");
				System.exit(0);
			}
			
			vertices = new ArrayList<Point2D.Double>(numVertices);
			
			//Add all the vertices
			for(int i = 0; i < numVertices; i++)
			{
				String[] temp = in.nextLine().split(",");
				
				Point2D.Double tempPoint = new Point2D.Double(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
				
				//Error prevention
				if(vertices.contains(tempPoint))
				{
					System.out.println("Every polygon vertex must be unique. Exiting.");
					System.exit(0);
				}
				
				vertices.add(tempPoint);
			}
			
			int numPopulation = Integer.parseInt(in.nextLine());
			
			if(numPopulation < 1)
			{
				System.out.println("Bad input: No districts can be created with no people.");
				System.exit(0);
			}
			
			population = new ArrayList<Point2D.Double>(numPopulation);
			
			//Add all the population units
			for(int i = 0; i < numPopulation; i++)
			{
				String[] temp = in.nextLine().split(",");
				
				population.add(new Point2D.Double(Double.parseDouble(temp[0]), Double.parseDouble(temp[1])));
			}
			
			numDistricts = Integer.parseInt(in.nextLine());
			
			if(numDistricts <= 0)
			{
				System.out.println("There must be at least 1 district. Please correct input. Exiting.");
				System.exit(0);
			}
			
			if(numPopulation < numDistricts)
			{
				System.out.println("Bad input: Question invalid. Cannot create more districts than people.");
				System.exit(0);
			}
			
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Problem connecting to file. Exiting program.");			
			System.exit(0);
		}
		
		//Create all the edges
		initEdges();
	}
	
	/**
	 * Creates the edges between vertices to form lines
	 */
	private void initEdges()
	{
		edges = new ArrayList<Line>();
		
		for(int i = 0; i < vertices.size(); i++)
		{			
			if(i+1 < vertices.size())
			{
				edges.add(new Line(vertices.get(i), vertices.get(i+1)));
			}
			else //i = the last vertex, which connects to the first point
			{
				edges.add(new Line(vertices.get(i), vertices.get(0)));;
			}
		}
	}
	
	/**
	 * Returns the points that were read in
	 * @return the points that were read in
	 */
	public ArrayList<Point2D.Double> getVertices()
	{
		return vertices;
	}
	
	/**
	 * Returns the edges that were created from the vertex data
	 * @return the edges that were created from the bertex data
	 */
	public ArrayList<Line> getEdges()
	{
		return edges;
	}
	
	/**
	 * Returns the population units that were read in
	 * @return the population untis that were read in
	 */
	public ArrayList<Point2D.Double> getPopulation()
	{
		return population;
	}
	
	/**
	 * Returns the quantity of desired districts that was read in
	 * @return the quantity of desired districts that was read in
	 */
	public int getNumDistricts()
	{
		return numDistricts;
	}
}
