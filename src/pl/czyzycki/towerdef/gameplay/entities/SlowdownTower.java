package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;

/**
 * Wie¿yczka spowalniaj¹ca. Je¿eli w zasiêgu jest jakiœ wróg,
 * tworzy obszarowe pole spowalniaj¹ce, dzia³aj¹ce przez okreœlony czas.
 * Po wygaœniêciu pola, przed stworzeniem nowego wie¿yczka ma cooldown.
 * @author Ciziu
 *
 */
public class SlowdownTower extends Tower {
	
	Field modelField;
	boolean fieldActivated;
	
	public SlowdownTower() {
		super();
	}
	
	public SlowdownTower init(GameplayScreen screen) {
		_init(screen);
		modelField = new Field();
		modelField.range = range;
		modelField.pos = pos;
		return this;
	}
	
	public SlowdownTower set(SlowdownTower tower, float x, float y) {
		super._set(tower,x,y);
		modelField.set(tower.modelField);
		return this;
	}
	
	@Override
	public void update(float dt) {
		if(fieldActivated) {
			if(!modelField.active) {
				timer = cooldown;
				fieldActivated = false;
			}
		} else {
			if(dt >= timer) {
				timer = 0.f;
				if(screen.enemyInRange(range, Targeted.BOTH) != null) {
					fieldActivated = true;
					screen.addField(modelField.activate());
				}
			} else timer -= dt;
		}
	}
	
	@Override
	public void debugDraw(ShapeRenderer shapeRenderer) {
		super.debugDraw(shapeRenderer);
	}
	
	static public class SlowdownTowerPool extends Pool<SlowdownTower> {
		
		GameplayScreen screen;
		
		public SlowdownTowerPool(GameplayScreen screen) {
			super(15);
			this.screen = screen;
		}

		@Override
		protected SlowdownTower newObject() {
			return new SlowdownTower().init(screen);
		}
		
	}
}