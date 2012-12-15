package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.entities.Tower.Upgrade.Level;

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
	float multiplier;
	
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
		modelField.set(tower.modelField, this);
		multiplier = tower.multiplier;
		return this;
	}
	
	public void whenSelling() {
		super.whenSelling();
		
		modelField.deactivate();
		screen.removeField(modelField);
	}
	
	@Override
	public void update(float dt) {
		if(fieldActivated) {
			if(!modelField.active) {
				timer = getCooldown();
				fieldActivated = false;
			}
		} else {
			if(dt >= timer) {
				timer = 0.f;
				if(screen.enemyInRange(getRange(), Targeted.BOTH) != null) {
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


	public static SlowdownTowerPool pool;
	
	public SlowdownTower obtainCopy(float x, float y) {
		return pool.obtain().set(this, x, y);
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

	public float getMultiplier() {
		Level level = getUpgradeLevel(Upgradeable.MULTIPLIER);
		if(level == null)
			return multiplier;
		
		return level.value;
	}
}