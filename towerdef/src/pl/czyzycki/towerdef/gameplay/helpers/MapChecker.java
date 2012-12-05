package pl.czyzycki.towerdef.gameplay.helpers;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;

public class MapChecker {
	boolean[][] map;
	int cols, rows;
	float height;
	boolean outOfBounds;
	
	public MapChecker(boolean[][] map, int cols, int rows, boolean outOfBounds) {
		this.map = map;
		this.cols = cols;
		this.rows = rows;
		height = rows*GameplayScreen.tileHeight;
		this.outOfBounds = outOfBounds;
	}
	
	public boolean check(float x, float y) {
		int r = (int)Math.floor((height-y)/GameplayScreen.tileHeight);
		int c = (int)Math.floor(x/GameplayScreen.tileWidth);
		if(r < 0 || r >= rows || c < 0 || c >= cols) return outOfBounds;
		else return map[r][c];
	}
	
	public void set(float x, float y, boolean value) {
		int r = (int)Math.floor((height-y)/GameplayScreen.tileHeight);
		int c = (int)Math.floor(x/GameplayScreen.tileWidth);
		if(r < 0 || r >= rows || c < 0 || c >= cols) throw new RuntimeException();
		else map[r][c] = value;
	}
}
