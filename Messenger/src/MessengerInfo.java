import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Holds information related to the Messenger program.
 * Miami University - CSE 586 - HW2
 * Last Updated: 12/23/2012
 * @author Matthew Herbst - herbstmb@muohio.edu
 *
 */
public class MessengerInfo 
{
	//Constants
	public final char emptySpace = '.';
	public final char messageSpace = 'X';
	
	//The grid to be searched
	private Node[][] city;
	private int rows;
	private int cols;
	
	//Information for where the search should start
	private int startRow;
	private int startCol;
	
	//The goal node
	private Node goalNode;
	
	/**
	 * Constructs the data object by reading in data from the specified file
	 * @param fileName the file to be read in
	 */
	public MessengerInfo(String fileName)
	{
		//Read in the data from the file
		Scanner in;
		try 
		{
			in = new Scanner(new File(fileName));
		
			//Grid information
			rows = in.nextInt();
			cols = in.nextInt();
			city = new Node[rows][cols];
			
			//Origin information
			startRow = in.nextInt();
			startCol = in.nextInt();
			
			in.nextLine();
			
			//Information about what each node is holding
			char[][] nodeTypes = new char[rows][cols];
			for(int r = rows - 1; r >= 0; r--)
			{
				String tempString = in.nextLine();
				for(int c = 0; c < cols; c++)
				{
					nodeTypes[r][c] = tempString.charAt(c);
				}
			}
			
			//Populate the city with node data
			int packageNumber = 0;
			for(int r = rows - 1; r >= 0; r--)
			{
				//The costs for the row being added
				String[] temp1 = in.nextLine().split(" ");
				
				//Format correctly to deal with empty strings created by extra spaces in input
				String[] temp = new String[cols];
				int counter = 0;
				for(int i = 0; i < temp1.length; i++)
				{
					if(!temp1[i].equals(""))
					{
						temp[counter] = temp1[i];
						counter++;
					}
				}
				
				for(int c = 0; c < cols; c++)
				{
					city[r][c] = new Node(r, c, nodeTypes[r][c], Double.parseDouble(temp[c]));
					if(nodeTypes[r][c] == messageSpace)
					{
						city[r][c].addPackage(packageNumber);
						packageNumber++;
					}
				}
			}
			
			in.close();
			
			//Create the goal node: End in the same place we started, but with all messages found
			goalNode = new Node(startRow, startCol, nodeTypes[startRow][startCol], 0);
			for(int i = 0; i < packageNumber; i++)
			{
				goalNode.addPackage(i);
			}
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error connecting to file. Closing program.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Returns the starting row of the messenger
	 * @return the starting row of the messenger
	 */
	public int getStartRow()
	{
		return startRow;
	}
	
	/**
	 * Returns the starting column of the messenger
	 * @return the starting column of the messenger
	 */
	public int getStartCol()
	{
		return startCol;
	}
	
	/**
	 * Returns the number of rows in the grid
	 * @return the number of rows in the grid
	 */
	public int getRows()
	{
		return rows;
	}
	
	/**
	 * Returns the number of columns in the grid
	 * @return the number of columns in the grid
	 */
	public int getCols()
	{
		return cols;
	}
	
	/**
	 * Returns the goal Node
	 * @return the goal Node
	 */
	public Node getGoalNode()
	{
		return goalNode;
	}
	
	/**
	 * Returns the grid representing the messenger world
	 * @return the grid representing the messenger world
	 */
	public Node[][] getCity()
	{
		return city;
	}
	
	/**
	 * Returns a string representation of the data object
	 * @return a string representation of the data object
	 */
	public String toString()
	{
		String data = "";
		
		for(int r = rows - 1; r >= 0; r--)
		{
			for(int c = 0; c < cols; c++)
			{
				data += city[r][c].getType() + " ";
			}
			data += "\n";
		}
		
		data += "\n";
		
		for(int r = rows - 1; r >= 0; r--)
		{
			for(int c = 0; c < cols; c++)
			{
				data += city[r][c].getCost() + " ";
			}
			data += "\n";
		}
		
		return data;
	}
}