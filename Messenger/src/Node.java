import java.util.ArrayList;
import java.util.Comparator;

/**
 * Node data structure optimized for the Messenger program
 * Miami University - CSE 586 - HW2
 * Last Updated: 12/23/2012
 * @author Matthew Herbst - herbstmb@muohio.edu
 */
public class Node
{
	//Node core data
	private Node parent;
	private double cost;
	private char type;
	
	//Node location
	private int row;
	private int col;
	
	//Searching data
	private String actions;
	private ArrayList<Integer> packagesSeen;
	private int depth;
	
	/**
	 * Constructs a Node from the specified parameters, setting parent to null, 
	 * depth to 0, actions to an empty string, and packagesSeen to an empty ArrayList
	 * @param row the row the node is on
	 * @param col the column the row is on
	 * @param type the type of this node (empty or contains a message)
	 * @param cost the cost of moving onto this node
	 */
	public Node(int row, int col, char type, double cost)
	{
		this.row = row;
		this.col = col;
		this.type = type;
		this.cost = cost;
		
		parent = null;
		depth = 0;
		actions = "";
		packagesSeen = new ArrayList<Integer>();
	}
	
	/**
	 * Constructs a Node from the specified parameters
	 * @param row the row the node is one
	 * @param col the column the row is on
	 * @param type the type of this node (empty or contains a message)
	 * @param cost the cost of moving onto this node
	 * @param parent the parent Node of this node
	 * @param depth the depth of this node
	 * @param actions the actions taken by the messenger before moving to this node
	 * @param packagesSeen the packages seen by the messenger before moving to this node
	 */
	public Node(int row, int col, char type, double cost, Node parent, int depth, String actions, ArrayList<Integer> packagesSeen)
	{
		this.row = row;
		this.col = col;
		this.type = type;
		this.cost = cost;
		this.parent = parent;
		this.depth = depth;
		this.actions = actions;
		this.packagesSeen = packagesSeen;
	}
	
	/**
	 * A Breadth-First search comparator
	 */
	public static class BFSComparator implements Comparator<Node>
	{
		public int compare(Node a, Node b) 
		{
			return a.getDepth() - b.getDepth();
		}
	}
	
	/**
	 * A Depth-First Search Comparator
	 */
	public static class DFSComparator implements Comparator<Node>
	{
		public int compare(Node a, Node b) 
		{
			return b.getDepth() - a.getDepth();
		}
	}
	
	/**
	 * A Uniform-Cost Search Comparator
	 */
	public static class UCSComparator implements Comparator<Node>
	{
		public int compare(Node a, Node b)
		{
			return (int) (a.getCost() - b.getCost());
		}
	}
	
	/**
	 * Returns the node's parent
	 * @return the node's parent
	 */
	public Node getParent()
	{
		return parent;
	}
	
	/**
	 * Returns the depth of the node
	 * @return the depth of the node
	 */
	public int getDepth()
	{
		return depth;
	}
	
	/**
	 * Returns the actions done by the messenger up to but not including this node
	 * @return the actions done by the messenger up to but not including this node
	 */
	public String getActions()
	{
		return actions;
	}
	
	/**
	 * Returns the cost of moving onto this node
	 * @return the cost of moving onto this node
	 */
	public double getCost()
	{
		return cost;
	}
	
	/**
	 * Returns the type of this node (empty or has package)
	 * @return the type of this node (empty or has package)
	 */
	public char getType()
	{
		return type;
	}
	
	/**
	 * Returns the row this node is on
	 * @return the row this node is on
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Returns the column this node is on
	 * @return the column this node is ons
	 */
	public int getCol()
	{
		return col;
	}
	
	/**
	 * Returns the list of packages seen by the messenger up to and possibly including this node
	 * @return the list of packages seen by the messenger up to and possibly including this node
	 */
	public ArrayList<Integer> getPackagesSeen()
	{
		return packagesSeen;
	}
	
	/**
	 * Adds a packages to the list of packages seen by the messenger
	 * @param p the package ID to be added
	 */
	public void addPackage(Integer p)
	{
		packagesSeen.add(p);
	}
	
	/**
	 * Checks to see if the packages for this node match the packages seen by another (order is ignored)
	 * @param theirPackages the packages being checked against
	 * @return true if and only if all the packages seen at this node match those sent in, false otherwise
	 */
	public boolean packagesEqual(ArrayList<Integer> theirPackages)
	{
		if(packagesSeen.size() != theirPackages.size())
		{
			return false;
		}
		
		for(int i = 0; i < packagesSeen.size(); i++)
		{
			if(!theirPackages.contains(packagesSeen.get(i)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns true if this node is equal to the passed object
	 * @return true if this node is equal to the passed object
	 */
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		
		if(obj == this)
		{
            return true;
		}
        
		if(obj.getClass() != getClass())
		{
            return false;
		}
		
		Node other = (Node) obj;		
		
		//Ensure other parameters are the same
		return (other.getRow() == row) && (other.getCol() == col) && packagesEqual(other.getPackagesSeen());
	}
	
	/**
	 * Returns a string representation of the node
	 * @return a string representation of the node
	 */
	public String toString()
	{
		return "(" + row + ", " + col + ")";
	}
}