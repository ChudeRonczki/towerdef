package pl.czyzycki.towerdef.gameplay.entities;


import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Pole spowalniaj¹ce utworzone przez wie¿yczkê spowalniaj¹c¹.
 * Dla ka¿dej wie¿yczki istnieje dok³adnie jedno pole, cyklicznie
 * dodawane i usuwane z listy w GameplayScreenie.
 * @author Ciziu
 *
 */
public class Field implements Comparable<Field> {
	
	Circle range;
	Vector2 pos;
	float lifeTime, maxLifeTime;
	boolean active;
	SlowdownTower tower;
	
	public Field() {}
	
	public void set(Field modelField, SlowdownTower slowdownTower) {
		lifeTime = modelField.lifeTime;
		maxLifeTime = modelField.lifeTime;
		tower = slowdownTower;
	}
	
	public boolean update(float dt) {
		lifeTime -= dt;
		if(lifeTime <= 0.f) {
			lifeTime = maxLifeTime;
			active = false;
			return true;
		}
		return false;
	}
	
	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(0,0,1,0.3f);
		range.draw(shapeRenderer);
	}
	
	public Field activate() {
		active = true;
		return this;
	}
	
	public void deactivate() {
		active = false;
	}

	@Override
	public int compareTo(Field field) {
		if(getMultiplier() > field.getMultiplier()) return -1;
		else if(getMultiplier() < field.getMultiplier()) return 1;
		else return 0;
	}

	public Circle getRange() {
		return range;
	}

	public float getMultiplier() {
		return tower.getMultiplier();
	}
}
