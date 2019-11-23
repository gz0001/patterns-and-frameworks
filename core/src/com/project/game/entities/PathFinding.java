package com.project.game.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

public class PathFinding {

	private TiledMapTileLayer tileLayer;
	public final static int BLOCK = 248;

	public PathFinding(TiledMapTileLayer tileLayer) {
		this.tileLayer = tileLayer;

	}

	public Integer getCellId(Vector2 vector) {
		Cell cell = tileLayer.getCell((int) vector.x, (int) vector.y);
		if (cell != null) {
			return cell.getTile().getId();
		}
		return -1;

	}

	private ArrayList<Vector2> getAdjacents(Vector2 point) {
		int x = (int) point.x, y = (int) point.y;
		ArrayList<Vector2> adjacents = new ArrayList<Vector2>();

		for (int i = -1; i <= 1; i++) {
			int curX = x + i;
			for (int j = -1; j <= 1; j++) {
				int curY = y + j;
				// System.out.println("x: " + curX + " cury: " + curY);
				if (curX > -1 && curY > -1 && curX < tileLayer.getWidth() - 1 && curY < tileLayer.getHeight() - 1) {

					if (curX != x || curY != y) {
						Vector2 adjacent = new Vector2(curX, curY);
						int id = getCellId(adjacent);
						if (id > 0 && id != BLOCK) {
							adjacents.add(adjacent);
						}

					}
				}
			}
		}

		return adjacents;
	}

	private int inList(Vector2 point, ArrayList<Node> nodes) {
		int index = -1;
		boolean founded = false;
		for (Node node : nodes) {
			index++;
			if (node.point.x == point.x && node.point.y == point.y) {
				founded = true;
				break;
			}
		}
		return founded ? index : -1;
	}

	private boolean moveDiagonal(Node first, Node second) {
		Vector2 f = first.point, s = second.point;
		boolean canMoveDiag = true;

		if (f.x != s.x && f.y != s.y) {

			// check 1st adjacent:
			if (getCellId(new Vector2(f.x, s.y)) == BLOCK)
				canMoveDiag = false;

			// check 2nd adjacent:
			if (getCellId(new Vector2(s.x, f.y)) == BLOCK)
				canMoveDiag = false;
		}
		return canMoveDiag;
	}

	private ArrayList<Vector2> restructurePath(ArrayList<Node> nodes) {

		ArrayList<Vector2> points = new ArrayList<Vector2>(), paths = new ArrayList<Vector2>(),
				open = new ArrayList<Vector2>();

		for (Node node : nodes) {
			points.add(node.point);
		}

		Vector2 end = points.get(points.size() - 1);

		open.add(points.get(0));

		while (!open.isEmpty()) {

			Vector2 current = open.get(0);
			open.remove(0);
			paths.add(current);

			if (current.dst(end) == 0)
				break;

			ArrayList<Vector2> adjacents = getAdjacents(current), currentAdj = new ArrayList<Vector2>();
			System.out.println("adj: " + adjacents);
			for (Vector2 adjacent : adjacents) {
				System.out.println(adjacent);
				Optional<Vector2> point = paths.stream().filter(v -> v.dst(adjacent) == 0).findFirst();
				System.out.println("poin: ");
			}
			// System.out.println(currentAdj);
		}

		return null;
	}

	public ArrayList<Node> findPath(Vector2 start, Vector2 end) {

		ArrayList<Node> open = new ArrayList<Node>(), close = new ArrayList<Node>();

		Node first = new Node(start);

		open.add(first);

		while (!open.isEmpty()) {
			int minIndex = open.indexOf(Collections.min(open));

			Node current = open.get(minIndex);

			open.remove(minIndex);
			close.add(current);

			if (end.x == current.point.x && end.y == current.point.y) {
				break;
			}

			ArrayList<Vector2> adjacents = getAdjacents(current.point);
			for (Vector2 v : adjacents) {
				if (inList(v, close) > -1)
					continue;

				Node newNode = new Node(v);

				if (moveDiagonal(current, newNode)) {
					newNode.setWeight(current, end);
				} else {
					newNode.setWeight(current, end, 2);
				}

				int openIndex = inList(newNode.point, open);
				if (openIndex > -1) {
					Node updateNode = open.get(openIndex);
					if (updateNode.g > newNode.g) {
						updateNode.f = newNode.f;
						updateNode.g = newNode.g;
						updateNode.h = newNode.h;
					}
				} else {
					open.add(newNode);
				}
			}
		}

		for (int i = close.size() - 1; i > 0; i--) {
			Node cur = close.get(i), prev = close.get(i - 1);
			if (Math.abs(cur.point.x - prev.point.x) > 1 || Math.abs(cur.point.y - prev.point.y) > 1) {
				// close.remove(i - 1);
			}
		}

		System.out.println("Path founded !");
		ArrayList<Vector2> paths = restructurePath(close);
		for (Node node : close) {
			// System.out.println(node.toString());
		}

		return close;

	}

	public static class Node implements Comparable {
		Vector2 point;
		float f, g, h;

		public Node(Vector2 node) {
			this.point = node;
			this.f = 0;
			this.g = 0;
			this.h = 0;
		}

		public void setWeight(Node current, Vector2 end) {

			g = current.g + point.dst(current.point);
			h = point.dst(end);
			f = g + h;

		}

		public void setWeight(Node current, Vector2 end, float gValue) {
			g = current.g + gValue;
			h = point.dst(end);
			f = g + h;

		}

		@Override
		public int compareTo(Object o) {
			Node other = (Node) o;
			if (this.f > other.f) {
				return 1;
			} else if (this.f < other.f) {
				return -1;
			} else {
				if (this.g > other.g) {
					return 1;
				} else if (this.g < other.g) {
					return -1;
				} else {
					if (this.h > other.h) {
						return 1;
					} else if (this.h < other.h) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		}

		@Override
		public String toString() {
			return point.x + ":" + point.y + " (" + g + ")" + " (" + f + ")";
		}

	}

}
