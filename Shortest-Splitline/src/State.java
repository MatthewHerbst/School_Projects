import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/*
 * Matthew Herbst
 */
/**
 * Represents a state area in polygon form. Also stores the population of that state,
 * both the quantity and their locations.
 * Last updated: 12/23/2012
 * @author Matthew Herbst - herbstmb@muohio.edu
 *
 */
public class State 
{
	private ArrayList<Line> edges;
	private ArrayList<Point2D.Double> people;
	private Path2D.Double shape;
	
	/**
	 * Constructs a new state in polygon form with no population units from the provided set of lines
	 * @param edges the lines that define the polygon of the state
	 */
	public State(ArrayList<Line> edges)
	{
		this.edges = edges;
		people = new ArrayList<Point2D.Double>();
	}
	
	/**
	 * Constructs a new state in polygon form from the provided lines and sets its population units to the provided units
	 * @param edges the lines that define the polygon of the state
	 * @param people the population units contained within the state
	 */
	public State(ArrayList<Line> edges, ArrayList<Point2D.Double> people)
	{
		this.edges = edges;
		this.people = people;
		
		shape = new Path2D.Double();
		shape.moveTo(edges.get(0).x1, edges.get(0).y1);
		for(int i = 0; i < edges.size(); i++)
		{
			shape.lineTo(edges.get(i).x2, edges.get(i).y2);
		}
		shape.closePath();
	}
	
	/**
	 * Returns the lines that define the state in polygon form
	 * @return the lines that define the state in polygon form
	 */
	public ArrayList<Line> getEdges()
	{
		return edges;
	}
	
	/**
	 * Returns the population units contained within the state
	 * @return the population units contained within the state
	 */
	public ArrayList<Point2D.Double> getPeople()
	{
		return people;
	}
	
	/**
	 * Determines if the state contains the specified population unit
	 * @param person the person being checked
	 * @return true if the state contains the person specified, false otherwise
	 */
	public boolean hasPerson(Point2D.Double person)
	{
		return people.contains(person);
	}
	
	/**
	 * Determines if the state contains the specified population unit
	 * Some code from fillPath(): http://code.ohloh.net/file?fid=L05yPpb-MAljgX3d2wqZuDKuAHk&cid=kMKOyzNNSlQ&s=&browser=Default#L0
	 * @param person the population unit being checked
	 * @return true if the state contains the specified population unit, false otherwise
	 */
	public boolean containsPoint(Point2D.Double person)
	{			
		ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
		for(int j = 0; j < edges.size(); j++)
		{
			points.add(edges.get(j).getP1());
		}
		
		Path2D.Double path = new Path2D.Double();
		int i=0;
		
		for(Point2D point : points)
		{
			if(i==0)
			{
				
				path.reset();
				path.moveTo(point.getX(), point.getY());
				
			}
			else
			{
				
				path.lineTo(point.getX(), point.getY());
			}
			
			i++;
		}
		path.closePath();
		
		return path.contains(person);
	}
	
	/**
	 * Adds a population unit to the state
	 * @param newPerson the population unit to be added
	 */
	public void addPerson(Point2D.Double newPerson)
	{
		people.add(newPerson);
	}
	
	/**
	 * Returns a string representation of the state
	 * @return a string representation of the state
	 */
	public String toString()
	{
		String border = "";
		for(int i = 0; i < edges.size(); i++)
		{
			border += edges.get(i).toString() + " ";
		}
		
		String population = "";
		for(int i  = 0; i < people.size(); i++)
		{
			population += "(" + people.get(i).getX() + ", " + people.get(i).getY() + ")";
			if(i + 1 < people.size())
			{
				population += ", ";
			}
		}
		
		return "Population: " + people.size() + " = " + population + " Border: " + border;
	}
}