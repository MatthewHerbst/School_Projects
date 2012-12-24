import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * @author Matthew Herbst - herbstmb@muohio.edu
 * Miami University - CSE 564 - Term Project Implementation
 * Last updated: 12/23/2012
 * A program for simulating a basic implementation of the Shortest-Splitline Algorithm
 * 
		 The Shortest-Splitline Algorithm:

		 If N=1 then output entire state as the district;
		 A = floor(N/2);
		 B = ceiling(N/2);
		 find shortest splitline resulting in A:B pop ratio (breaking ties,
		                     if any, as described in notes);
		 Use it to split the state into the two HemiStates SA and SB;
		 ShortestSplitLine( SB, B );
		 ShortestSplitLine( SA, A );
		 
		 ***I currently have not implemented a shortest-splitline algorithm. The splitline I
		 *currently use is a median point horizontal-splitline. I plan on fixing this soon.
		 *There is a known bug: If a a vertex of a splitline is also a vertex of one of the
		 *line that define the polygon the program will fail. This will be fixed soon.***
 */
public class ShortestSplitline 
{
	//Basic data for the algorithm - read in from input file
	private ShortestSplitlineData myData;
	
	//Debug level
	private static int DEBUG_LEVEL;
	
	/**
	 * Constructs a representation of the problem from the given input file
	 * @param fileName
	 */
	ShortestSplitline(String fileName) 
	{
		//Read in the problem information
		myData = new ShortestSplitlineData(fileName);
	}
	
	/**
	 * The main entry point of the program
	 * @param args args[0] = the file containing problem information; args[1] = debug on/off (1 for on)
	 */
	public static void main(String[] args)
	{
		ShortestSplitline myClass;
		
		//Check for valid run command
		if(args.length == 2)
		{
			//Set up problem definition and data
			myClass = new ShortestSplitline(args[0]);
			
			//Define debug level
			DEBUG_LEVEL = Integer.parseInt(args[1]);
		}
		else //Get needed info from prompts to user
		{			
			System.out.println("Invalid run paramters. Format should be \"java ShortestSplitline [input file] [debug level - 0 or 1]\"");
			System.out.println("Please respond to the prompts below:\n");
			
			Scanner in = new Scanner(System.in);
			
			//Set up problem definition and data
			System.out.print("Please enter the name of the file contianing problem information: ");
			myClass = new ShortestSplitline(in.next());
			
			//Define debug level
			System.out.print("Turn debug mode on? Enter 1 for yes, any other integer for no: ");
			
			try
			{
				DEBUG_LEVEL = Integer.parseInt(in.next());
			}
			catch(NumberFormatException nfe)
			{
				System.out.println("Invalid input. The debug prompt requires an integer. Exiting.");
				System.exit(0);
			}
			
			System.out.println("");
			in.close();
		}	
		
		//Perform the shortest-splitline algorithm on the data
		State startState = new State(myClass.myData.getEdges(), myClass.myData.getPopulation());
		ArrayList<State> finalDistricts = myClass.shortestSplitline(startState, myClass.myData.getNumDistricts());
		
		//Write the districts to the command line
		for(int i = 0; i < finalDistricts.size(); i++)
		{
			int number = i + 1;
			System.out.println("District #" + number + ": " + finalDistricts.get(i).toString());
		}
	}
	
	/**
	 * Performs the shortest-splitline algorithm
	 * @param state the area to be divided
	 * @param N the num of districts desired
	 * @return the list of final districts
	 */
	public ArrayList<State> shortestSplitline(State state, double N)
	{			
		ArrayList<State> districts = new ArrayList<State>((int) N);
		
		//Base case
		if(N == 1)
		{
			if(DEBUG_LEVEL == 1)
			{
				System.out.println("Final district: " + state.toString());
			}
			
			districts.add(state);
		}
		else
		{
			int A = (int)Math.floor(N/2);
			int B = (int)Math.ceil(N/2);
			
			//Find the splitline
			Line splitline = findSplitline(state, A, B);
			
			if(DEBUG_LEVEL == 1)
			{
				System.out.println("Current state: " + state.toString());
				System.out.println("A = " + A + ", B = " + B + " splitline = " + splitline.toString());
			}
				
			//Use that line to create the two new states
			ArrayList<ArrayList<Line>> newStates = splitState(state.getEdges(), splitline);
			State state1 = new State(newStates.get(0));
			State state2 = new State(newStates.get(1));
			
			//Assign population to one side of the line or the other (state 1 gets A, state 2 gets B)
			for(int i = 0; i < state.getPeople().size(); i++)
			{
				if(state1.containsPoint(state.getPeople().get(i)))
				{
					state1.addPerson(state.getPeople().get(i));
				}
				else if(state2.containsPoint(state.getPeople().get(i)))
				{
					state2.addPerson(state.getPeople().get(i));
				}
				else //Error
				{
					System.out.println("Error assigning population units to correct state. Exiting.");
					System.exit(0);
				}
			}
			
			if(DEBUG_LEVEL == 1)
			{
				System.out.println("New state 1: " + state1.toString());
				System.out.println("New state 2: " + state2.toString());
				System.out.println();
			}
			
			//Recurse on each new state
			if(DEBUG_LEVEL == 1)
			{
				System.out.println("Recursing on state 1");
			}
			districts.addAll(shortestSplitline(state1, A));
			if(DEBUG_LEVEL == 1)
			{
				System.out.println("Recursing on state 2");
			}
			districts.addAll(shortestSplitline(state2, B));
		}
		
		return districts;
	}
	
	/*
	 * Custom comparator for sorting Points
	 */
	public class HorizontalComparator implements Comparator<Point2D.Double> 
	{
	    public int compare(Point2D.Double p1, Point2D.Double p2) 
	    {
	        if(p1.y > p2.y)
	        {
	        	return 1;
	        }
	        else if(p1.y < p2.y)
	        {
	        	return -1;
	        }
	        else
	        {
	        	return 0;
	        }
	    }
	}
	
	/**
	 * Returns the splitline in the polygon that maintains the A:B population ratio on each side of the line
	 * @param state the state being looked at
	 * @param A the number of population units on one side of the line
	 * @param B the number of population units on the other side of the line
	 * @return the splitline in the polygon that maintains the A:B population ratio on each side of the line
	 */
	private Line findSplitline(State state, int A, int B)
	{		
		Collections.sort(state.getPeople(), new HorizontalComparator());
		
		//Find a point between the Ath and Ath+1 points from which to draw the splitline
		//BIG ASSUMPTION: They do not share the same value being compared - TODO: Fix this!
		Point2D.Double lastInA = state.getPeople().get(A-1);
		Point2D.Double firstInB = state.getPeople().get(A);
		Point2D.Double lineLoc = new Point2D.Double(lastInA.x + (firstInB.x - lastInA.x), lastInA.y + ((firstInB.y - lastInA.y)/2));
		
		//Find all lines that the splitline could intersect and grab the points at which they do
		ArrayList<Point2D.Double> intersectPoints = new ArrayList<Point2D.Double>();
		for(int i = 0; i < state.getEdges().size(); i++)
		{
			//Saves a bunch of function calls
			Line tempLine = state.getEdges().get(i);
			
			//If the lineLoc y value is within the proper range
			if((tempLine.getP1().y >= lineLoc.y && lineLoc.y >= tempLine.getP2().y) || (tempLine.getP1().y <= lineLoc.y && lineLoc.y <= tempLine.getP2().y))
			{
				intersectPoints.add(state.getEdges().get(i).findIntersection(lineLoc));
			}
		}
		
		//Error checking - there must be at least two bounding lines/intersect points
		if(intersectPoints.size() < 2)
		{
			System.out.println("Failed to find bounding lines via intersect points. Exiting.");
			System.exit(0);
		}
		
		//Using the points above, find bounding lines by finding the min distance point to the left and min distance point to the right
		Point2D.Double left = new Point2D.Double(-Double.MAX_VALUE, lineLoc.y);
		Point2D.Double right = new Point2D.Double(Double.MAX_VALUE, lineLoc.y);
		for(int i = 0; i < intersectPoints.size(); i++)
		{
			//Line/intersect point is to the left
			if(intersectPoints.get(i).x <= lineLoc.x)
			{
				//If it is closer than the current left
				if(lineLoc.x - intersectPoints.get(i).x < lineLoc.x - left.x)
				{
					left = intersectPoints.get(i);
				}
			}
			else //It is to the right
			{
				//If it is closer than the current right
				if(intersectPoints.get(i).x - lineLoc.x < right.x - lineLoc.x)
				{
					right = intersectPoints.get(i);
				}
			}
		}
		
		//Error checking
		if(left.x == -Double.MAX_VALUE || right.x == Double.MAX_VALUE)
		{
			System.out.println("Problem assigning left and right bounding points. Exiting.");
			System.exit(0);
		}
		
		//These two points are now the two points used to create the splitline
		return new Line(left, right);
	}
	
	/**
	 * Returns a list containing the list of lines that define new polygons resulting from the given splitline
	 * @param edges the lines representing the polygon being split with the splitline
	 * @param splitline the line being used to split the polygon defined by edges
	 * @return a list containing the list of lines that define new polygons resulting from the given splitline
	 */
	private ArrayList<ArrayList<Line>> splitState(ArrayList<Line> edges, Line splitline)
	{
		//Will hold the two new states being formed
		ArrayList<ArrayList<Line>> newStates = new ArrayList<ArrayList<Line>>();
		
		//The two new states being created
		ArrayList<Line> state1 = new ArrayList<Line>();
		ArrayList<Line> state2 = new ArrayList<Line>();
		state2.add(splitline);
		
		//Info needed for the split
		Line currentEdge;
		@SuppressWarnings("unused")
		Line wasIntersected;
		int i1 = 0;
		int i2 = 0;
		Point2D.Double vertexToEndState2On = null;
		
		//Start at any vertex in the current state (this implementation uses the first one stored)
		
		//Find the first state
		while(true)
		{
			if(i1 >= edges.size())
			{
				break;
			}
			
			//Get the next edge
			currentEdge = edges.get(i1);
			
			//Check to see if we have completed mapping the state
			if(currentEdge.getP2().equals(edges.get(0).getP1()))
			{
				state1.add(currentEdge);
				break;
			}
			else //We need to keep mapping
			{
				boolean p1OnEdge = currentEdge.onLine(splitline.getP1());
				boolean p2OnEdge = currentEdge.onLine(splitline.getP2());
				
				//Check to see if the splitline starts/ends on the current edge
				if(!p1OnEdge && !p2OnEdge)
				{
					//If it doesn't, add that edge to the state and keep going
					state1.add(currentEdge);
				}
				else if(p1OnEdge) //If it does using its first point
				{
					//Create a new line from currentEdge.p1 to splitline.p1 and add it to the new state
					state1.add(new Line(currentEdge.getP1(), splitline.getP1()));
					
					//Add the other half to the second new state
					state2.add(new Line(splitline.getP1(), currentEdge.getP2()));
					
					//Add the splitline to the new state
					state1.add(new Line(splitline.getP2(), splitline.getP1())); //Keeps the vertexes in proper order
					
					//Save the currentEdge and the current index
					wasIntersected = currentEdge;
					i2 = i1;
					vertexToEndState2On = splitline.getP2();
					
					//p2 of current edge will be p2 of the first edge in the second state after the splitline
					//p2 of the splitline will be the last vertex in the second state
					
					//Find the other edge that the splitline connects to
					Line otherEdge = null;
					int j = i1;
					while(j < edges.size())
					{
						if(edges.get(j).onLine(splitline.getP2()))
						{
							otherEdge = edges.get(j);
							break;
						}
						
						j++;
					}
					//Error checking
					if(otherEdge == null)
					{
						System.out.println("Problem finding other edge splitline connects to. Started at splitline.p1");
						System.exit(0);
					}
					
					//Now that we have found the edge, move to it, add the new partial line
					//We can let the loop take care of the rest
					i1 += j;
					state1.add(new Line(splitline.getP2(), otherEdge.getP2()));
					if(otherEdge.getP2().equals(edges.get(0).getP1()))
					{
						break;
					}
				}
				else //It does using its second point
				{
					//Create a new line from currentEdge.p1 to splitline.p2 and add it to the new state
					state1.add(new Line(currentEdge.getP1(), splitline.getP2()));
					
					//Add the splitline to the new state
					state1.add(new Line(splitline.getP2(), splitline.getP1())); //Keeps the vertexes in proper order
					//state1.add(splitline);
					
					//Add the other half to the second new state
					state2.add(new Line(splitline.getP2(), currentEdge.getP2()));
					
					//Save the currentEdge and the current index
					wasIntersected = currentEdge;
					i2 = i1;
					vertexToEndState2On = splitline.getP1();
					
					//p2 of current edge will be p2 of the first edge in the second state after the splitline
					//p1 of the splitline will be the last vertex in the second state
					
					//Find the other edge that the splitline connects to
					Line otherEdge = null;
					int j = i1;
					while(j < edges.size())
					{
						if(edges.get(j).onLine(splitline.getP1()))
						{
							otherEdge = edges.get(j);
							break;
						}
						
						j++;
					}
					//Error checking
					if(otherEdge == null)
					{
						System.out.println("Problem finding other edge splitline connects to. Started at splitline.p2");
						System.exit(0);
					}
					
					//Now that we have found the edge, move to it, add the new partial line
					//We can let the loop take care of the rest
					i1 += j;
					state1.add(new Line(splitline.getP1(), otherEdge.getP2()));
					if(otherEdge.getP2().equals(edges.get(0).getP1()))
					{
						break;
					}
				}
			}
			
			i1++;
		}
		
		//Error checking
		if(vertexToEndState2On == null)
		{
			System.out.println("vertexToEndState2On is null. Problem with mapping.");
			System.exit(0);
		}
		
		//Find the second state
		i2++;
		while(true)
		{
			//Get the next edge
			currentEdge = edges.get(i2);
			
			if(currentEdge.onLine(vertexToEndState2On))
			{
				state2.add(new Line(currentEdge.getP1(), vertexToEndState2On));
				break;
			}
			else
			{
				state2.add(currentEdge);
			}
			
			i2++;
		}
		
		//Add the two new states to the list to be returned and return it
		newStates.add(state1);
		newStates.add(state2);
		return newStates;
	}
}