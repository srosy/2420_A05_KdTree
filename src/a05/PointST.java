/****************************
 * Author: Spencer Rosenvall
 * Class: CSIS 2420
 * Professor: Frau Posch
 * Assignment: A05_KdTree
 ****************************/

package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

/**
 * Class PointSt is a mutable data type that is a symbol table with Point2D by
 * using a red-black BST from algs4.jar.
 * 
 * @author SpencerR
 *
 * @param <Value>
 */
public class PointST<Value> {
	private SET<Point2D> tree; // no duplicates allowed

	/**
	 * Constructs an empty symbol table of points.
	 */
	public PointST() {
		tree = new SET<Point2D>();
	}

	/**
	 * Checks if the symbol table empty?
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		return tree.isEmpty();
	}

	/**
	 * Returns number of points.
	 * 
	 * @return int
	 */
	public int size() {
		return tree.size();
	}

	/**
	 * Associates the value val with point p.
	 * 
	 * @param p
	 * @param val
	 */
	public void put(Point2D p, Value val) {
		if (p == null)
			throw new NullPointerException();
		if (!tree.contains(p))
			tree.add(p);
	}

	/**
	 * Gets the value associated with point p.
	 * 
	 * @param p
	 * @return Value
	 */
	@SuppressWarnings("unchecked")
	public Value get(Point2D p) {
		if (tree.contains(p)) {
			for (Point2D d : tree) {
				if (d == p)
					return (Value) d.toString();
			}
		} else {
			throw new IllegalAccessError("Point p doesn't exist");
		}
		return null;
	}

	/**
	 * Checks if the symbol table contain point p.
	 * 
	 * @param p
	 * @return boolean
	 */
	public boolean contains(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		return tree.contains(p);
	}

	/**
	 * Iterates and returns the points in the symbol table.
	 * 
	 * @return Iterable<Point2D>
	 */
	public Iterable<Point2D> points() {
		if (tree == null)
			throw new IllegalAccessError("Tree is null");
		SET<Point2D> points = new SET<Point2D>();
		for (Point2D p : tree) {
			points.add(p);
		}
		return points;
	}

	/**
	 * Iterates and returns all the points inside the rectangle.
	 * 
	 * @param rect
	 * @return Iterable<Point2D>
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException();

		SET<Point2D> c = new SET<Point2D>();
		for (Point2D s : tree) {
			if (rect.contains(s))
				c.add(s);
		}
		return c;
	}

	/**
	 * Gets the nearest neighbor to point p; null if the symbol table is empty.
	 * 
	 * @param p
	 * @return Point2D
	 */
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException();

		double lastMinDist = Double.MAX_VALUE;
		double currentDist = 0;
		Point2D output = null;
		for (Point2D s : tree) {
			currentDist = p.distanceSquaredTo(s);
			if (currentDist < lastMinDist) {
				output = s;
				lastMinDist = currentDist;
			}
		}
		return output;
	}

	/**
	 * Unit testing of the methods (not graded).
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}
}
