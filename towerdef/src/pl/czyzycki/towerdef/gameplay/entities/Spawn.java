package pl.czyzycki.towerdef.gameplay.entities;

import java.util.LinkedList;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.AStarNode;
import pl.czyzycki.towerdef.gameplay.helpers.MapChecker;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Spawn, miejsce "produkcji" wrogów, z którego wychodz¹
 * na mapê rozgrywki.
 *
 */
public class Spawn {
	public enum Border {
		LEFT,RIGHT,TOP,BOTTOM
	}
	
	public static float waveTimeout;
	
	GameplayScreen screen;
	
	Vector2 pos;
	Border border;
	Array<Vector2> flyingPath;
	Array<Vector2> roadPath;
	LinkedList<LinkedList<Enemy>> spawnList;
	float timer;
	boolean waveFinished = false;
	
	StringBuilder timerText;
	
	public Spawn(float x, float y, Border border, GameplayScreen screen) {
		this.screen = screen;
		
		pos = new Vector2(x,y);
		this.border = border;
		flyingPath = new Array<Vector2>(true,2,Vector2.class);
		roadPath = new Array<Vector2>(true,2,Vector2.class);
		spawnList = new LinkedList<LinkedList<Enemy>>();
		waveFinished = false;
		timerText = new StringBuilder();
	}
	
	public boolean calculatePaths(MapChecker roadMap, Vector2 base) {
		flyingPath.add(new Vector2(pos.x, pos.y));
		switch(border) {
		case LEFT:
		case RIGHT:
			flyingPath.add(new Vector2(base.x,pos.y));
			break;
		case TOP:
		case BOTTOM:
			flyingPath.add(new Vector2(pos.x,base.y));
		}
		flyingPath.add(new Vector2(base.x,base.y));
		
		
		AStarNode node = new AStarNode(pos.x, pos.y, 0, null);
		node.calculateHeuristic(base);
		
		Array<AStarNode> openNodes = new Array<AStarNode>(false, 10, AStarNode.class);
		Array<AStarNode> closedNodes = new Array<AStarNode>(false, 10);
		final int nearFields[] = {-1,0,1,0,0,-1,0,1};
		
		openNodes.add(node);
		
		AStarNode endNode = null;
		
		while(endNode == null && openNodes.size > 0) {
			AStarNode bestNode = openNodes.items[0];
			for(AStarNode nextNode : openNodes) {
				if(nextNode.total() < bestNode.total()) bestNode = nextNode;
			}
			
			closedNodes.add(bestNode);
			openNodes.removeValue(bestNode, true);
		
			for(int i = 0; i < 8; i += 2) {
				float newX = bestNode.x + nearFields[i] * GameplayScreen.tileWidth;
				float newY = bestNode.y + nearFields[i+1] * GameplayScreen.tileHeight;
				if(roadMap.check(newX, newY)) {
					boolean found = false;
					
					for(AStarNode nextNode : closedNodes) {
						if(nextNode.x == newX && nextNode.y == newY) {
							found = true;
							break;
						}
					}
					
					if(!found) {
						for(AStarNode nextNode : openNodes) {
							if(nextNode.x == newX && nextNode.y == newY) {
								found = true;
								if(node.cost > bestNode.cost + 1) {
									node.cost = bestNode.cost + 1;
									node.parent = bestNode;
								}
								break;
							}
						}
					}
					
					if(!found) {
						node = new AStarNode(newX, newY, bestNode.cost + 1, bestNode);
						node.calculateHeuristic(base);
						openNodes.add(node);
						if(node.x == base.x && node.y == base.y) endNode = node;
					}
				}
			}
		}
		
		if(endNode == null) return false;
		
		do {
			roadPath.add(new Vector2(endNode.x, endNode.y));
			endNode = endNode.parent;
		} while(endNode != null);
		
		roadPath.reverse();
		
		for(int i = 1; i < roadPath.size - 1; ++i) {
			if(roadPath.items[i-1].x == roadPath.items[i+1].x ||
					roadPath.items[i-1].y == roadPath.items[i+1].y) {
				roadPath.removeIndex(i--);
			}
		}
		
		return true;
	}
	
	public void update(float dt) {
		if(!waveFinished) {
			if(timer <= dt) {
				Enemy enemy = spawnList.peek().poll();
				if(spawnList.peek().isEmpty()) waveFinished = true;
				if(enemy != null) {
					timer += enemy.timer;
					screen.addEnemy(enemy, enemy.flying);
				}
			}
			timer -= dt;
		}
	}
	
	public void startWave() {
		if(waveFinished) spawnList.poll();
		waveFinished = false;
		timer = waveTimeout;
	}
	
	public void addEnemy(Enemy enemy) {
		float offsetX = 0f, offsetY = 0f;
		switch(border) {
			case LEFT:
				offsetX -= enemy.sprite.getHeight()/2f + GameplayScreen.tileWidth/2f;
				break;
			case RIGHT:
				offsetX += enemy.sprite.getHeight()/2f + GameplayScreen.tileWidth/2f;
				break;
			case TOP:
				offsetY += enemy.sprite.getHeight()/2f + GameplayScreen.tileHeight/2f;
				break;
			case BOTTOM:
				offsetY -= enemy.sprite.getHeight()/2f + GameplayScreen.tileHeight/2f;
				break;
		}
		if(enemy.flying) enemy.set(pos.tmp().add(offsetX, offsetY), new Array.ArrayIterator<Vector2>(flyingPath), screen);
		else enemy.set(pos.tmp().add(offsetX, offsetY), new Array.ArrayIterator<Vector2>(roadPath), screen);
		spawnList.getLast().add(enemy);
	}
	
	public void addWave() {
		spawnList.add(new LinkedList<Enemy>());
	}
	
	public void debugDraw(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(0, 1, 0, 1);
		for(int i = 0; i < roadPath.size - 1; ++i) {
			shapeRenderer.line(roadPath.items[i].x, roadPath.items[i].y, roadPath.items[i+1].x, roadPath.items[i+1].y);
		}
		
		shapeRenderer.setColor(0, 0, 1, 1);
		for(int i = 0; i < flyingPath.size - 1; ++i) {
			shapeRenderer.line(flyingPath.items[i].x, flyingPath.items[i].y, flyingPath.items[i+1].x, flyingPath.items[i+1].y);
		}
	}
	
	public void debugText(SpriteBatch batch, BitmapFont debugFont) {
		timerText.length = 0;
		timerText.append(timer);
		debugFont.draw(batch, timerText, pos.x, pos.y);
	}
	
	public boolean isWaveFinished() {
		return waveFinished;
	}
}
