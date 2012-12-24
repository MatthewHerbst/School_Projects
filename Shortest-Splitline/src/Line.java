import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A class to represent a line segement and perform some basic function on it.
 * Extends Line2D.Double, which did not have all the required functions
 * TODO: Edit class to implement more from it's parent
 * Last updated: 12/23/2012
 * @author Matthew Herbst - herbstmb@muohio.edu
 */
public class Line extends Line2D.Double
{
	//Auto generated using Eclipse
	private static final long serialVersionUID = 5511191456809579841L;
	
	//The two points that define the line
	private Point2D.Double p1;
	private Point2D.Double p2;
	
	//Storing these saves some computation time for the other methods
	private double slope;
	private double yIntercept;
	
	/**
	 * Constructs a new Line by using two defined points
	 * @param p1 the first vertex defining the line
	 * @param p2 the second vertex defining the line
	 */
	public Line(Point2D.Double p1, Point2D.Double p2)
	{
		this.p1 = p1;
		this.p2 = p2;
		
		if(p2.x == p1.x)
		{
			//TODO: This should probably actually be fixed
			slope = -3.14159;
		}
		else
		{
			slope = (p2.y - p1.y) / (p2.x - p1.x);
		}
		
		//yIntercept = -1 * (slope * (p1.x/p1.y));
		yIntercept = -1 * ((slope * p1.x) - p1.y);
	}
	
	/**
	 * Returns the first vertex defining the line
	 * @return the first vertex defining the line
	 */
	public Point2D.Double getP1()
	{
		return p1;
	}
	
	/**
	 * Returns the second vertex defining the line
	 * @return the second vertex defining the line
	 */
	public Point2D.Double getP2()
	{
		return p2;
	}
	
	/**
	 * Returns the length of the line
	 * @return the length of the line
	 */
	public double getLength()
	{
		return Math.sqrt(Math.pow(p2.x-p1.x, 2) + Math.pow(p2.y-p1.y, 2));
	}
	
	/**
	 * Returns the slope of the line
	 * @return the slope of the line
	 */
	public double getSlope()
	{
		return slope;
	}
	
	/**
	 * Returns the y-intercept of the line
	 * @return the y-intercept of the line
	 */
	public double getYIntercept()
	{
		return yIntercept;
	}
	
	/**
	 * Determines if a point lies on the line
	 * @param p the point being checked
	 * @return true if the point lies on the line, false otherwise
	 */
	public boolean onLine(Point2D.Double p)
	{
		//y=mx+b
		//y-y1 = m(x-x1)
		
		//Y = slope * X + yIntersect
		if(p1.x - p2.x == 0)
		{
			if(p1.y < p2.y)
			{
				return (p.x == p1.x) && (p.y > p1.y) && (p.y < p2.y);
			}
			else
			{
				return (p.x == p1.x) && (p.y > p2.y) && (p.y < p1.y);
			}
		}
		
		return p.y == (slope * p.x) + yIntercept;
	}
	
	/**
	 * Returns the point at which the line intersects the given line
	 * @param p the point defining the line that is being intersected (guaranteed to be horizontal)
	 * @return the point at which the line intersects the given line
	 */
	public Point2D.Double findIntersection(Point2D.Double p)
	{		
		//y = y'					This is the definition of the incoming line from p
		//y-y1 = m(x-x1)			This is the definition of THIS line
		//y'-y1 = m(x-x1)			
		//x = ((y'-y1/m)+x1)		Solve for x. Intersect point will be (x, y')
		
		//x = x'
		//y-y1 = m(x-x1)
		//y-y1 = m(x'-x1)
		//y = m(x'-x1) + y1
		
		//The y intercept for the incoming line = p.y since it is horizontal
		//Set y's equal to solve for x
		if(slope != -3.14159)
		{
			double x = (p.y - yIntercept) / slope;
			return new Point2D.Double(x, p.y);
		}
		
		//double x = ((p.y-p1.y) / slope) + p1.x;
		return new Point2D.Double(p1.x, p.y);
	}
	
	/**
	 * Returns a string representation of the line
	 * @return a string representation of the line
	 */
	public String toString()
	{
		return "(" + p1.x + "," + p1.y + ")(" + p2.x + "," + p2.y + ")";
	}
}