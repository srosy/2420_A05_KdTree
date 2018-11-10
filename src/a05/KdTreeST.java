/****************************
 * Author: Spencer Rosenvall
 * Class: CSIS 2420
 * Professor: Frau Posch
 * Assignment: A05_KdTreeST
 ****************************/

package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

/**
 * Class kdTreeST represents a tree using symbol table, values, and point2d.
 * 
 * @author SpencerR
 * @param <Value>
 */
public class KdTreeST<Value> {
	private int size;
	private Node root;

	/**
	 * Private helper class to define node and its properties.
	 * 
	 * @author SpencerR
	 *
	 */
	private class Node {
		private Node l; // left
		private Node r; // right
		private Value value;
		private RectHV rect;
		private Point2D p;

		public Node(Point2D point, Value value, RectHV rectangle) {
			this.p = point;
			this.value = value;
			this.rect = rectangle;
		}
	}

	/**
	 * Constructs an empty symbol table of points.
	 */
	public KdTreeST() {
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
		return size;
	}

	/**
	 * * Associates the value val with point p.
	 * 
	 * @param p
	 * @param val
	 */
	public void put(Point2D p, Value val) {
		if (p == null || val == null)
			throw new NullPointerException("Param cannot be null");
		root = put(null, root, p, true, val);
	}

	/**
	 * Creates and places node in the tree.
	 * 
	 * @param preceeding
	 * @param n
	 * @param p
	 * @param isHorizontal
	 * @param v
	 * @return Node
	 */
	private Node put(Node preceeding, Node n, Point2D p, boolean isHorizontal, Value v) {
		if (n == null) {
			size++;
			return new Node(p, v, getRect(preceeding, p, isHorizontal));
		}
		double comparedResult = compare(n, p, isHorizontal);

		if (comparedResult < 0) {
			n.l = put(n, n.l, p, !isHorizontal, v);
		} else if (comparedResult > 0) {
			n.r = put(n, n.r, p, !isHorizontal, v);
		} else if (n.p.equals(p)) {
			n.value = v;
		} else {
			n.r = put(n, n.r, p, !isHorizontal, v);
		}
		return n;
	}

	/**
	 * Compares the coordinates of the passed point and node.
	 * 
	 * @param node
	 * @param point
	 * @param horizontal
	 * @return int
	 */
	private int compare(KdTreeST<Value>.Node node, Point2D point, boolean horizontal) {
		double result = 0;
		if (horizontal) {
			result = point.x() - node.p.x() > 0 ? 1 : -1;
			result = point.x() - node.p.x() == 0 ? 0 : result;
		} else {
			result = point.y() - node.p.y() > 0 ? 1 : -1;
			result = point.y() - node.p.y() == 0 ? 0 : result;
		}
		return (int) result;
	}

	/**
	 * Creates a rectangle from the arguments passed.
	 * 
	 * @param preceeding
	 * @param p
	 * @param isHorizontal
	 * @return RectHV
	 */
	private RectHV getRect(Node preceeding, Point2D p, boolean isHorizontal) {
		if (preceeding == null) {
			return new RectHV(-Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		}
		int comp = compare(preceeding, p, !isHorizontal);

		if (comp >= 0 && isHorizontal) {
			return new RectHV(preceeding.rect.xmin(), preceeding.p.y(), preceeding.rect.xmax(), preceeding.rect.ymax());
		}

		if (comp >= 0 && !isHorizontal) {
			return new RectHV(preceeding.p.x(), preceeding.rect.ymin(), preceeding.rect.xmax(), preceeding.rect.ymax());
		}

		if (comp < 0 && isHorizontal) {
			return new RectHV(preceeding.rect.xmin(), preceeding.rect.ymin(), preceeding.rect.xmax(), preceeding.p.y());
		}

		if (comp < 0 && !isHorizontal) {
			return new RectHV(preceeding.rect.xmin(), preceeding.rect.ymin(), preceeding.p.x(), preceeding.rect.ymax());
		}
		return null;
	}

	/**
	 * Returns the value associated with point p.
	 * 
	 * @param p
	 * @return Value
	 */
	public Value get(Point2D p) {
		if (p == null) {
			throw new NullPointerException("p cannot be null");
		}
		return get(root, p, true);
	}

	/**
	 * Helper method that compares and locates Point2D p.
	 * 
	 * @param node
	 * @param p
	 * @param isHorizontal
	 * @return Value
	 */
	private Value get(Node node, Point2D p, boolean isHorizontal) {
		if (node == null)
			return null;
		double comp = compare(node, p, isHorizontal);

		if (node.p.equals(p))
			return node.value;
		if (comp < 0)
			return get(node.l, p, !isHorizontal);
		if (comp > 0)
			return get(node.r, p, !isHorizontal);
		return get(node.r, p, !isHorizontal);
	}

	/**
	 * Determines if symbol table contains point p.
	 * 
	 * @param p
	 * @return boolean
	 */
	public boolean contains(Point2D p) {
		if (p == null)
			throw new NullPointerException("p cannot be null");
		return get(p) != null;
	}

	/**
	 * Iterates the points in the symbol table.
	 * 
	 * @return Iterable<Point2D>
	 */
	public Iterable<Point2D> points() {
		Queue<Point2D> p2dQ = new Queue<Point2D>();
		Queue<Node> nQ = new Queue<Node>();
		nQ.enqueue(root);

		while (!nQ.isEmpty()) {
			Node x = nQ.dequeue();
			p2dQ.enqueue(x.p);
			nQ.enqueue(x.r);
			nQ.enqueue(x.l);
		}
		return p2dQ;
	}

	/**
	 * Iterates the points inside rectangle bounds.
	 * 
	 * @param rect
	 * @return Iterable<Point2D>
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException("rect cannot be null");

		Queue<Point2D> rQ = new Queue<Point2D>();
		range(rect, rQ, root);
		return rQ;
	}

	/**
	 * Helper method for range that enqeues.
	 * 
	 * @param r
	 * @param q
	 * @param n
	 */
	private void range(RectHV r, Queue<Point2D> q, KdTreeST<Value>.Node n) {
		if (n == null)
			return;
		if (r.contains(n.p))
			q.enqueue(n.p);
		range(r, q, n.r);
		range(r, q, n.l);
	}

	/**
	 * Returns the nearest neighbor to point p.
	 * 
	 * @param p
	 * @return Point2D
	 */
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException("p cannot be null");
		return nearest(p, root, root.p);
	}

	/**
	 * Helper method, locates the nearest neighbor to point p.
	 * 
	 * @param p
	 * @param n
	 * @param p2D
	 * @return point2D
	 */
	private Point2D nearest(Point2D p, Node n, Point2D p2D) {
		if (n == null)
			return p2D;
		if (n.rect.distanceSquaredTo(p) > p2D.distanceSquaredTo(p))
			return p2D;
		if (p.distanceSquaredTo(n.p) < p.distanceSquaredTo(p2D))
			p2D = n.p;

		if (n.l != null && n.l.rect.contains(p)) {
			p2D = nearest(p, n.l, p2D);
			p2D = nearest(p, n.r, p2D);
		} else {
			p2D = nearest(p, n.r, p2D);
			p2D = nearest(p, n.l, p2D);
		}
		return p2D;
	}

	/**
	 * Unit testing of the methods (not graded).
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}
}
