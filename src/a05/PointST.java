/****************************
 * Author: Spencer Rosenvall
 * Class: CSIS 2420
 * Professor: Frau Posch
 * Assignment: A05_KdTreeST
 ****************************/

package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;

/**
 * Class PointST is of type Value and is based off the Redblack BST.
 * 
 * @author SpencerR
 * @param <Value>
 */
public class PointST<Value> {
	private RedBlackBST<Point2D, Value> rb;

	/**
	 * Constructs an empty symbol table of points.
	 */
	public PointST() {
		rb = new RedBlackBST<>();
	}

	/**
	 * Determines if symbol table is empty.
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of points.
	 * 
	 * @return int
	 */
	public int size() {
		return rb.size();
	}

	/**
	 * Associates the value val with point p.
	 * 
	 * @param p
	 * @param val
	 */
	public void put(Point2D p, Value val) {
		if (p == null)
			throw new NullPointerException("p cannot be null");
		rb.put(p, val);
	}

	/**
	 * Returns the value associated with point p.
	 * 
	 * @param p
	 * @return Value
	 */
	public Value get(Point2D p) {
		if (p == null)
			throw new NullPointerException("p cannot be null");
		return rb.get(p);
	}

	/**
	 * Checks if symbol table contains point p.
	 * 
	 * @param p
	 * @return boolean
	 */
	public boolean contains(Point2D p) {
		if (p == null)
			throw new NullPointerException("p cannot be null");
		return rb.contains(p);
	}

	/**
	 * Iterates the points in the symbol table.
	 * 
	 * @return Iterable<Point2D>
	 */
	public Iterable<Point2D> points() {
		return rb.keys();
	}

	/**
	 * Iterates the points that are inside the rectangle.
	 * 
	 * @param rect
	 * @return Iterable<Point2D>
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException("rect cannot be null");

		Stack<Point2D> s = new Stack<>();
		for (Point2D p : rb.keys()) {
			if (rect.contains(p)) {
				s.push(p);
			}
		}
		return s;
	}

	/**
	 * Returns nearest neighbor to point p.
	 * 
	 * @param p
	 * @return Point2D
	 */
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException("p cannot be null");

		Point2D nearest = rb.min();
		for (Point2D point : rb.keys()) {
			if (p.distanceSquaredTo(nearest) > p.distanceSquaredTo(point)) {
				nearest = point;
			}
		}
		return nearest;
	}

	/**
	 * Unit testing of the methods (not graded).
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}
}