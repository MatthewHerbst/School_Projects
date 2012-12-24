import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A messenger search program designed to simulate:
 * Breadth-First Search
 * Depth-First Search
 * Uniform-Cost Search
 * Recursive Depth-First Search
 * 
 * All are implemented used a closed list.
 * Miami University - CSE 586 - HW2
 * Last updated: 12/23/2012
 * @author Matthew Herbst - herbstmb@muohio.edu
 */
public class Messenger 
{
	//Holds world data
	private static MessengerInfo myInfo;
	private static double routeCost;
	
	/**
	 * The main entry point for the program
	 * @param args the input arguments for the program (not used in this implementation)
	 */
	public static void main(String[] args)
	{
		//Initialize the world information
		myInfo = new MessengerInfo("map3.txt");
		routeCost = 0.0;
		
		//Initialize the search type; Add the start location to the search; Find the path and the cost
		System.out.println("Breadth-first search:");
		Comparator<Node> BFScomp = new Node.BFSComparator();
		PriorityQueue<Node> BFSqueue = new PriorityQueue<Node>(1000, BFScomp);
		BFSqueue.add(new Node(myInfo.getStartRow(), myInfo.getStartCol(), myInfo.getCity()[myInfo.getStartRow()][myInfo.getStartCol()].getType(), 0.0));
		System.out.println("Route: " + getRoute(BFSqueue));
		System.out.println("Path cost: " + routeCost + "\n");
		
		//Initialize the search type; Add the start location to the search; Find the path and the cost
		System.out.println("Depth-first search:");
		Comparator<Node> DFScomp = new Node.DFSComparator();
		PriorityQueue<Node> DFSqueue = new PriorityQueue<Node>(1000, DFScomp);
		DFSqueue.add(new Node(myInfo.getStartRow(), myInfo.getStartCol(), myInfo.getCity()[myInfo.getStartRow()][myInfo.getStartCol()].getType(), 0.0));
		System.out.println("Route: " + getRoute(DFSqueue));
		System.out.println("Path cost: " + routeCost + "\n");
		
		//Initialize the search type; Add the start location to the search; Find the path and the cost
		System.out.println("Uniform-cost search:");
		Comparator<Node> UCScomp = new Node.UCSComparator();
		PriorityQueue<Node> UCSqueue = new PriorityQueue<Node>(1000, UCScomp);
		UCSqueue.add(new Node(myInfo.getStartRow(), myInfo.getStartCol(), myInfo.getCity()[myInfo.getStartRow()][myInfo.getStartCol()].getType(), 0.0));
		System.out.println("Route: " + getRoute(UCSqueue));
		System.out.println("Path cost: " + routeCost + "\n");
		
		//Initialize the search type; Add the start location to the search; Find the path and the cost
		System.out.println("Recursive depth-first search:");
		ArrayList<Node> closed = new ArrayList<Node>();
		System.out.println("Route: " + recursiveDFS(new Node(myInfo.getStartRow(), myInfo.getStartCol(), myInfo.getCity()[myInfo.getStartRow()][myInfo.getStartCol()].getType(), 0.0), closed));
		System.out.println("Path cost: " + routeCost + "\n");
	}
	
	/**
	 * Returns the route for the messenger by using the specified PriorityQueue
	 * @param queue the queue passed in - a custom comparator is used to define the type of search to perform
	 * @return the route for the messenger should use
	 */
	public static String getRoute(PriorityQueue<Node> queue)
	{	
		ArrayList<Node> closedList = new ArrayList<Node>();
		boolean foundAll = false;
		
		//Keep searching if there are elements in the queue
		while(!queue.isEmpty())
		{
			//Get the first element in the queue
			Node current = queue.remove();
			
			//If it is the goal
			if(current.equals(myInfo.getGoalNode()))
			{				
				//Find the cost and return it
				routeCost = current.getCost();
				return current.getActions();
			}
			else
			{
				//If we have found all the packages, allow us to get back to the start!				
				if(current.packagesEqual(myInfo.getGoalNode().getPackagesSeen()) && !foundAll)
				{
					foundAll = true;
					closedList.clear();
					
					//Not needed, but should speed up search
					queue.clear();
				}
				
				//Add it to the closed list
				closedList.add(current);
				
				//Expand it
				queue.addAll(expand(current, closedList));
			}
			
		}
		
		return "Not found";
	}
	
	/**
	 * Returns the route for the messenger by using a recursive depth-first search
	 * @param current the node currently being looked at
	 * @param closedList the list of nodes already looked at
	 * @return the route for the messenger
	 */
	public static String recursiveDFS(Node current, ArrayList<Node> closedList)
	{
		//Base case
		if(current.equals(myInfo.getGoalNode()))
		{
			routeCost = current.getCost();
			return current.getActions();
		}
		else if(!closedList.contains(current)) //Prevent infinite loops
		{
			closedList.add(current.getParent());
			PriorityQueue<Node> neighbors = new PriorityQueue<Node>(4, new Node.DFSComparator());
			neighbors.addAll(expand(current, closedList));
			
			//Expand neighbors and see if the results in a correct answer
			String path = "";
			while(!neighbors.isEmpty())
			{
				Node temp = neighbors.remove();
				path = recursiveDFS(temp, closedList);
				
				if(!path.equals(""))
				{
					return path;
				}
			}
		}
		
		return "";
	}
	
	/**
	 * Expands a node
	 * @param root - the node being expanded
	 * @param closedList - the list of nodes already examined
	 * @return - a list of child nodes of root that are not in the closedList
	 */
	public static ArrayList<Node> expand(Node root, ArrayList<Node> closedList)
	{
		ArrayList<Node> children = new ArrayList<Node>();
		
		int row = root.getRow();
		int col = root.getCol();		
		
		//North
		if(row < myInfo.getRows() - 1)
		{
			ArrayList<Integer> tempPackages = new ArrayList<Integer>();
			tempPackages.addAll(root.getPackagesSeen());
			for(int i = 0; i < myInfo.getCity()[row+1][col].getPackagesSeen().size(); i++)
			{
				if(!tempPackages.contains(myInfo.getCity()[row+1][col].getPackagesSeen().get(i)))
				{
					tempPackages.add(myInfo.getCity()[row+1][col].getPackagesSeen().get(i));
				}
			}
			
			Node temp = new Node(
					row + 1, //Rows
					col, //Col
					myInfo.getCity()[row+1][col].getType(), //Type (this doesn't really matter)
					root.getCost() + myInfo.getCity()[row+1][col].getCost(), //Cost
					root, //Parent
					root.getDepth() + 1, //Depth
					root.getActions() + "N", //Actions
					tempPackages); //Packages
			
			if(!closedList.contains(temp))
			{
				children.add(temp);
			}
		}
		//South
		if(row > 0)
		{
			ArrayList<Integer> tempPackages = new ArrayList<Integer>();
			tempPackages.addAll(root.getPackagesSeen());
			for(int i = 0; i < myInfo.getCity()[row-1][col].getPackagesSeen().size(); i++)
			{
				if(!tempPackages.contains(myInfo.getCity()[row-1][col].getPackagesSeen().get(i)))
				{
					tempPackages.add(myInfo.getCity()[row-1][col].getPackagesSeen().get(i));
				}
			}
			
			Node temp = new Node(
					row - 1, //Rows
					col, //Col
					myInfo.getCity()[row-1][col].getType(), //Type (this doesn't really matter)
					root.getCost() + myInfo.getCity()[row-1][col].getCost(), //Cost
					root, //Parent
					root.getDepth() + 1, //Depth
					root.getActions() + "S", //Actions
					tempPackages); //Packages

			if(!closedList.contains(temp))
			{
				children.add(temp);
			}
		}
		//East
		if(col < myInfo.getCols() - 1)
		{
			ArrayList<Integer> tempPackages = new ArrayList<Integer>();
			tempPackages.addAll(root.getPackagesSeen());
			for(int i = 0; i < myInfo.getCity()[row][col+1].getPackagesSeen().size(); i++)
			{
				if(!tempPackages.contains(myInfo.getCity()[row][col+1].getPackagesSeen().get(i)))
				{
					tempPackages.add(myInfo.getCity()[row][col+1].getPackagesSeen().get(i));
				}
			}
			
			Node temp = new Node(
					row, //Rows
					col + 1, //Col
					myInfo.getCity()[row][col+1].getType(), //Type (this doesn't really matter)
					root.getCost() + myInfo.getCity()[row][col+1].getCost(), //Cost
					root, //Parent
					root.getDepth() + 1, //Depth
					root.getActions() + "E", //Actions
					tempPackages); //Packages

			if(!closedList.contains(temp))
			{
				children.add(temp);
			}
		}
		//West
		if(col > 0)
		{
			ArrayList<Integer> tempPackages = new ArrayList<Integer>();
			tempPackages.addAll(root.getPackagesSeen());
			for(int i = 0; i < myInfo.getCity()[row][col-1].getPackagesSeen().size(); i++)
			{
				if(!tempPackages.contains(myInfo.getCity()[row][col-1].getPackagesSeen().get(i)))
				{
					tempPackages.add(myInfo.getCity()[row][col-1].getPackagesSeen().get(i));
				}
			}
			
			Node temp = new Node(
					row, //Rows
					col - 1, //Col
					myInfo.getCity()[row][col-1].getType(), //Type (this doesn't really matter)
					root.getCost() + myInfo.getCity()[row][col-1].getCost(), //Cost
					root, //Parent
					root.getDepth() + 1, //Depth
					root.getActions() + "W", //Actions
					tempPackages); //Packages

			if(!closedList.contains(temp))
			{
				children.add(temp);
			}
		}
		
		return children;
	}
}