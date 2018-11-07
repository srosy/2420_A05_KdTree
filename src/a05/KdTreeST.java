/****************************
 * Author: Spencer Rosenvall
 * Class: CSIS 2420
 * Professor: Frau Posch
 * Assignment: A05_KdTree
 ****************************/

package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

/**
 * Class kdTreeST creates a binary tree using a series of Node objects defined
 * withing private class Node.
 * 
 * @author SpencerR
 *
 */
public class KdTreeST<Value> {

	/**
	 * Class Node determines the type of node to be placed in the tree. It takes a
	 * Point2D point and sets vertical propoerty.
	 * 
	 * @author SpencerR
	 *
	 */
	private static class Node<Value> {
		private Point2D point;
		private Value value;
		private Node<Value> tr; // topRight
		private Node<Value> bl; // bottomLeft
		private boolean isVertical;

		/**
		 * Constructs the node from the parameters p<Point2D> and vert.
		 * 
		 * @param p
		 * @param vert
		 */
		public Node(Point2D p, Value val, boolean vert) {
			this.point = p;
			this.isVertical = vert;
			this.value = val;
			bl = null;
			tr = null;
		}
	}

	private Node<Value> root;
	private int size;

	/**
	 * Construct an empty set of points.
	 */
	public KdTreeST() {
		// empty => (int default = 0, Object default = null)
	}

	/**
	 * Checks if the set is empty.
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of points in the set.
	 * 
	 * @return int
	 */
	public int size() {
		return size;
	}

	/**
	 * Associates the value with point p.
	 * 
	 * @param p
	 */
	public void put(Point2D p, Value val) {
		if (p == null)
			throw new NullPointerException();
		root = insert(root, p, val, true);
	}

	/**
	 * Inserts a node with point p, determines if node is vertical.
	 * 
	 * @param node
	 * @param p
	 * @param isVertical
	 * @return Node
	 */
	private Node<Value> insert(Node<Value> node, Point2D p, Value val, boolean isVertical) {
		int result;

		if (node == null) {
			size++;
			return new Node<Value>(p, val, isVertical);
		}

		if (p.equals(node.point))
			return node;

		if (node.isVertical) {
			result = orderByX(p, node.point);
		} else {
			result = orderByY(p, node.point);
		}

		if (result < 0) {
			node.bl = insert(node.bl, p, val, !node.isVertical);
		} else {
			node.tr = insert(node.tr, p, val, !node.isVertical);
		}

		return node;
	}

	/**
	 * Orders two points by comparing their x value.
	 * 
	 * @param point1
	 * @param point2
	 * @return int
	 */
	private int orderByX(Point2D point1, Point2D point2) {
		if (point1.x() > point2.x())
			return 1;
		if (point1.x() < point2.x())
			return -1;
		return 0;
	}

	/**
	 * Orders two points by comparing their y value.
	 * 
	 * @param p1
	 * @param p2
	 * @return int
	 */
	private int orderByY(Point2D p1, Point2D p2) {
		if (p1.y() > p2.y())
			return 1;
		if (p1.y() < p2.y())
			return -1;
		return 0;
	}

	/**
	 * Checks if the set contain point p.
	 * 
	 * @param p
	 * @return boolean
	 */
	public boolean contains(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		Node<Value> node = root;
		int result;
		while (node != null) {
			if (p.equals(node.point)) {
				return true;
			}
			if (node.isVertical) {
				result = orderByX(p, node.point);
			} else {
				result = orderByY(p, node.point);
			}
			if (result < 0)
				node = node.bl;
			else
				node = node.tr;
		}
		return false;
	}

	/**
	 * Iterates through all points that are inside the rectangle to determine the
	 * range.
	 * 
	 * @param rect
	 * @return Iterable<Point2D>
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new NullPointerException();
		}
		Queue<Point2D> q = new Queue<Point2D>();
		range(root, new RectHV(0, 0, 1, 1), rect, q);
		return q;
	}

	/**
	 * Checks if node rectangle and query rectange intersects, helps parent range
	 * method.
	 * 
	 * @param node
	 * @param nRect
	 * @param qRect
	 * @param q
	 */
	private void range(Node<Value> node, RectHV nRect, RectHV qRect, Queue<Point2D> q) {
		if (node == null)
			return;
		if (qRect.intersects(nRect)) {
			if (qRect.contains(node.point)) {
				q.enqueue(node.point);
			}
			range(node.bl, createLeftRect(node, nRect), qRect, q);
			range(node.tr, createRightRect(node, nRect), qRect, q);
		}
	}

	/**
	 * Returns the nearest Point2D point.
	 * 
	 * @param p
	 * @return Point2D
	 */
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException();

		if (contains(p))
			return p;

		return nearest(root, new RectHV(0, 0, 1, 1), p, null);
	}

	/**
	 * Helps parent method determine the closest node.
	 * 
	 * @param node
	 * @param r
	 * @param p
	 * @param pFinal
	 * @return Point2D
	 */
	private Point2D nearest(Node<Value> node, RectHV r, Point2D p, Point2D pFinal) {
		if (node == null)
			return pFinal;

		Point2D goal = pFinal;
		double closest = goal != null ? p.distanceSquaredTo(goal) : Double.MAX_VALUE;

		if (closest > r.distanceSquaredTo(p)) {
			double distance = p.distanceSquaredTo(node.point);
			if (distance < closest)
				goal = node.point;

			int result;
			RectHV left = createLeftRect(node, r);
			RectHV right = createRightRect(node, r);
			if (node.isVertical) {
				result = orderByX(p, node.point);
			} else {
				result = orderByY(p, node.point);
			}

			if (result < 0) {
				goal = nearest(node.bl, left, p, goal);
				goal = nearest(node.tr, right, p, goal);
			} else {
				goal = nearest(node.tr, right, p, goal);
				goal = nearest(node.bl, left, p, goal);
			}
		}
		return goal;
	}

	/**
	 * Creates the left rectangle. ************** Check if needed still, watch vid.
	 * 
	 * @param node
	 * @param r
	 * @return RectHV
	 */
	private RectHV createLeftRect(Node node, RectHV r) {
		return node.isVertical ? new RectHV(r.xmin(), r.ymin(), node.point.x(), r.ymax())
				: new RectHV(r.xmin(), r.ymin(), r.xmax(), node.point.y());
	}

	/**
	 * Creates the right rectangle. *********** Check if needed still, watch vid.
	 * 
	 * @param node
	 * @param r
	 * @return RectHV
	 */
	private RectHV createRightRect(Node node, RectHV r) {
		return node.isVertical ? new RectHV(node.point.x(), r.ymin(), r.xmax(), r.ymax())
				: new RectHV(r.xmin(), node.point.y(), r.xmax(), r.ymax());
	}

	/**
	 * Unit testing of the methods (optional).
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
