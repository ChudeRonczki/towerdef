package pl.czyzycki.towerdef.gameplay.entities;


import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;
import pl.czyzycki.towerdef.menus.OptionsScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

public class Enemy {
	
	public static Sprite basicSprite;
	public static Animation leftWalkingAnimation;
	public static Animation rightWalkingAnimation;
	public static Animation downWalkingAnimation;
	public static Animation upWalkingAnimation;
	
	int sideWalking = 0; // 0=left, 1=right, 2=down else up
	
	GameplayScreen screen;
	Base base;
	
	Field slowdownField;
	Circle hitZone;
	Array.ArrayIterator<Vector2> pathIter;
	Vector2 pos, direction, currentWaypoint;
	Sprite sprite;
	float speed, maxSpeed, hp, maxHp, timer;
	int damage, points, money;
	boolean flying, alive, hitBase;
	
	StringBuilder hpText;
	
	float time = 0; // TODO
	
	public Enemy() {}
	
	static public void loadAnimations(AssetManager assetMgr) {
		Texture texture = assetMgr.get("images/enemy-anim.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(texture, 80, 80);
		
		float animSpeed = 0.15f;
		
		TextureRegion[] upWalkFrames = new TextureRegion[4];
		upWalkFrames[0] = tmp[0][0];
		upWalkFrames[1] = tmp[0][1];
		upWalkFrames[2] = tmp[0][0];
		upWalkFrames[3] = tmp[0][2];
		
		upWalkingAnimation = new Animation(animSpeed, upWalkFrames);
		
		TextureRegion[] leftWalkFrames = new TextureRegion[4];
		leftWalkFrames[0] = tmp[1][0];
		leftWalkFrames[1] = tmp[1][1];
		leftWalkFrames[2] = tmp[1][0];
		leftWalkFrames[3] = tmp[1][2];
		
		leftWalkingAnimation = new Animation(animSpeed, leftWalkFrames);
		
		TextureRegion[] rightWalkFrames = new TextureRegion[4];
		rightWalkFrames[0] = tmp[2][0];
		rightWalkFrames[1] = tmp[2][1];
		rightWalkFrames[2] = tmp[2][0];
		rightWalkFrames[3] = tmp[2][2];
		
		rightWalkingAnimation = new Animation(animSpeed, rightWalkFrames);
		
		TextureRegion[] downWalkFrames = new TextureRegion[4];
		downWalkFrames[0] = tmp[3][0];
		downWalkFrames[1] = tmp[3][1];
		downWalkFrames[2] = tmp[3][0];
		downWalkFrames[3] = tmp[3][2];
		
		downWalkingAnimation = new Animation(animSpeed, downWalkFrames);
	}
	
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
		points = enemy.points;
		money = enemy.money;
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
		
		time += dt;
		
		// Sekcja update'owania aktualnie dzia³aj¹cego pola spowalniaj¹cego
		if((slowdownField != null) && (!slowdownField.active || !Circle.colliding(hitZone, slowdownField.range))) slowdownField = null;
		Field strongerField = screen.fieldInRange(hitZone, (slowdownField != null) ? slowdownField.tower.getMultiplier() : 1.f);
		if(strongerField != null) slowdownField = strongerField;
		if(slowdownField != null) speed = maxSpeed*slowdownField.tower.getMultiplier();
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
			hitBase = true;
			if(OptionsScreen.vibrationEnabled()) Gdx.input.vibrate(200);
			return true;
		}
		
		if(direction.x < 0) sideWalking = 0;
		else if(direction.x > 0) sideWalking = 1;
		else if(direction.y > 0) sideWalking = 2;
		else sideWalking = 3;
		
		pos.add(direction.tmp().mul(dt*speed));
		sprite.setPosition(pos.x-sprite.getWidth()/2f,pos.y-sprite.getHeight()/2f);
		return false;
	}
	
	public void draw(SpriteBatch batch) {
		Animation anim = upWalkingAnimation;
		if(sideWalking == 0) anim = leftWalkingAnimation;
		else if(sideWalking == 1) anim = rightWalkingAnimation;
		else if(sideWalking == 2) anim = downWalkingAnimation;
		
		TextureRegion currentFrame = anim.getKeyFrame(time, true);
		batch.draw(currentFrame, pos.x-currentFrame.getRegionWidth()/2.0f, pos.y-currentFrame.getRegionHeight()/2.0f);
		//sprite.draw(batch);
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

	public Circle getHitZone() {
		return hitZone;
	}

	public Vector2 getPos() {
		return pos;
	}

	public int getPoints() {
		return points;
	}

	public int getMoney() {
		return money;
	}

	public boolean hitTheBase() {
		return hitBase;
	}
}

