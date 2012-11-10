package pl.czyzycki.towerdef.gameplay.entities;


import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

public class Enemy {
	
	static Sprite basicSprite;
	
	GameplayScreen screen;
	Base base;
	
	Field slowdownField;
	Circle hitZone;
	Array.ArrayIterator<Vector2> pathIter;
	Vector2 pos, direction, currentWaypoint;
	Sprite sprite;
	float speed, maxSpeed, hp, maxHp, timer;
	int damage;
	boolean flying, alive;
	
	StringBuilder hpText;
	
	public Enemy() {}
	
	public Enemy(Enemy enemy, float timer) {
		hitZone = new Circle();
		pos = new Vector2();
		direction = new Vector2();
		currentWaypoint = new Vector2();
		sprite = new Sprite(basicSprite);
		hpText = new StringBuilder();
		
		hitZone.pos = pos;
		hitZone.radius = enemy.hitZone.radius;
		speed = enemy.speed;
		maxSpeed = speed;
		hp = enemy.hp;
		maxHp = hp;
		damage = enemy.damage;
		flying = enemy.flying;
		alive = true;
		this.timer = timer;
	}
	
	public void set(Vector2 pos, Array.ArrayIterator<Vector2> pathIter, GameplayScreen screen) {
		this.screen = screen;
		this.pos.set(pos);
		this.pathIter = pathIter;
		pathIter.next(); // Pominiêcie pozycji spawna
		currentWaypoint = pathIter.next();
		direction.set(currentWaypoint.tmp().sub(this.pos).nor());
		sprite.setRotation(direction.angle()-90f);
		sprite.setPosition(this.pos.x-sprite.getWidth()/2f,this.pos.y-sprite.getHeight()/2f);
		base = screen.getBase();
	}
	
	/**
	 * @return Do usuniêcia?
	 */
	public boolean update(float dt) {
		if(!alive) return true;
		
		// Sekcja update'owania aktualnie dzia³aj¹cego pola spowalniaj¹cego
		if((slowdownField != null) && (!slowdownField.active || !Circle.colliding(hitZone, slowdownField.range))) slowdownField = null;
		Field strongerField = screen.fieldInRange(hitZone, (slowdownField != null) ? slowdownField.multiplier : 1.f);
		if(strongerField != null) slowdownField = strongerField;
		if(slowdownField != null) speed = maxSpeed*slowdownField.multiplier;
		else speed = maxSpeed;
		
		
		float toGo = pos.tmp().sub(currentWaypoint).len();
		
		// Osi¹gniêto waypoint
		if(toGo <= dt*speed) {
			if(!pathIter.hasNext()) { // W teorii nie powinno siê zdarzyæ
				alive = false;
				return true;
			}
			dt = (dt*speed - toGo)/speed;
			pos.set(currentWaypoint);
			currentWaypoint = pathIter.next();
			direction.set(currentWaypoint.tmp().sub(pos).nor());
			sprite.setRotation(direction.angle()-90f);
		}
		
		// Kolizja z baz¹
		if(Circle.colliding(hitZone, base.hitZone)) {
			alive = false;
			base.takeHit(damage);
			return true;
		}
		
		pos.add(direction.tmp().mul(dt*speed));
		sprite.setPosition(pos.x-sprite.getWidth()/2f,pos.y-sprite.getHeight()/2f);
		return false;
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	public void takeHit(float damage) {
		hp -= damage;
		if(hp <= 0f) {
			alive = false;
		}
	}
	
	public void debugDraw(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(1, 0, 0, 0.2f);
		hitZone.draw(shapeRenderer);
	}
	
	public void debugText(SpriteBatch batch, BitmapFont debugFont) {
		hpText.length = 0;
		hpText.append(hp);
		hpText.append('/');
		hpText.append(maxHp);
		debugFont.draw(batch, hpText, pos.x + 30f, pos.y - 30f);
	}
}

