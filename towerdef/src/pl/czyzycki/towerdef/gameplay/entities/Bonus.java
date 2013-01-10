package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Klasa bonusu do zebrania przez gracza (tego na mapce).
 *
 */
public class Bonus {
	public enum BonusType {
		MONEY,
		BOMB,
		UPGRADE
	}
	
	GameplayScreen screen;
	
	Vector2  pos;
	Circle zone;
	Sprite sprite;
	BonusType type;
	
	float maxLifeTime;
	float lifetime;
	int value;

	Bonus init(GameplayScreen screen) {
		this.screen = screen;
		pos = new Vector2();
		zone = new Circle();
		zone.pos = pos;
		zone.radius = 40f;
		sprite = new Sprite();
		return this;
	}
	
	public final float TweenBonusSize(float t) {
		final float PI = 3.14159265f;
        float a = 1;
        float p = .3f;
        if (t<=0) return 0;  if (t>=1) return 1;
        float s=p/4;
        return a*(float)Math.pow(2,-10*t) * (float)Math.sin( (t-s)*(2*PI)/p ) + 1;
}

	
	public Bonus set(Bonus bonus, Vector2 pos) {
		type = bonus.type;
		lifetime = bonus.lifetime;
		maxLifeTime = bonus.lifetime;
		value = bonus.value;
		
		sprite.set(bonus.sprite);
		this.pos.set(pos);
		
		sprite.setPosition(this.pos.x - 40f, this.pos.y - 40f);
		
		return this;
	}
	
	public void draw(SpriteBatch batch) {
		sprite.setScale(TweenBonusSize(maxLifeTime-lifetime));
		if(lifetime*4 < 1)
			sprite.setScale(lifetime*4);
		sprite.draw(batch);
	}
	
	public boolean update(float dt) {
		if(dt >= lifetime) return true;
		else lifetime -= dt;
		return false;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	public void setType(BonusType type) {
		this.type = type;
	}
	
	static public class BonusPool extends Pool<Bonus> {

		GameplayScreen screen;
		
		public BonusPool(GameplayScreen screen) {
			super(10);
			this.screen = screen;
		}

		@Override
		protected Bonus newObject() {
			return new Bonus().init(screen);
		}
	}

	public Circle getZone() {
		return zone;
	}

	public void onCollected() {
		switch(type) {
			case MONEY:
			screen.addMoney(value);
			break;
			case BOMB:
			screen.addBomb();
			break;
			case UPGRADE:
			screen.addMaxUpgrade();
			break;
		}
	}
}
