package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

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
	
	float lifetime, range, duration;
	int value, damage;

	Bonus init(GameplayScreen screen) {
		this.screen = screen;
		pos = new Vector2();
		zone = new Circle();
		zone.pos = pos;
		zone.radius = 40f;
		sprite = new Sprite();
		return this;
	}
	
	public Bonus set(Bonus bonus, Vector2 pos) {
		type = bonus.type;
		lifetime = bonus.lifetime;
		range = bonus.range;
		duration = bonus.duration;
		value = bonus.value;
		damage = bonus.damage;
		
		sprite.set(bonus.sprite);
		this.pos.set(pos);
		
		sprite.setPosition(this.pos.x - 40f, this.pos.y - 40f);
		
		return this;
	}
	
	public void draw(SpriteBatch batch) {
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
			
		}
	}
}
