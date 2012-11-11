package pl.czyzycki.towerdef.gameplay.helpers;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.math.Vector2;

public class AStarNode {
	public AStarNode parent;
	public float x, y;
	public int cost, heuristic;
	
	public AStarNode(float x, float y, int cost, AStarNode parent) {
		this.x = x;
		this.y = y;
		this.cost = cost;
		this.parent = parent;
	}
	
	public void calculateHeuristic(Vector2 base) {
		heuristic = Math.round(Math.abs((base.x-x)/GameplayScreen.tileWidth));
		heuristic += Math.round(Math.abs((base.y-y)/GameplayScreen.tileHeight));
	}
	
	public int total() {
		return cost + heuristic;
	}
}
