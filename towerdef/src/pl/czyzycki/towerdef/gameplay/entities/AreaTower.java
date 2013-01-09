package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.TowerDef.GameSound;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


/**
 * Wie¿yczka obszarowa. Zadaje obra¿enia wszystkim celom znajduj¹cym siê
 * w zasiêgu w momencie strza³u, który oddawany jest gdy w zasiêgu jest
 * przynajmniej jeden cel i nie jest aktywny cooldown.
 * @author Ciziu
 *
 */
public class AreaTower extends Tower {
	
	Array<Enemy> targetedEnemies, targetedEnemies2;
	
	float rangeAlpha = 0;
		
	public AreaTower() {
		super();
	}
	
	public AreaTower init(GameplayScreen screen) {
		super._init(screen);
		return this;
	}
	
	public AreaTower set(AreaTower tower, float x, float y) {
		super._set(tower, x, y);
		damage = tower.damage;
		if(targeted == Targeted.BOTH) {
			targetedEnemies = screen.getEnemiesList(Targeted.GROUND);
			targetedEnemies2 = screen.getEnemiesList(Targeted.AIRBORNE);
		} else {
			targetedEnemies = screen.getEnemiesList(targeted);
			targetedEnemies2 = emptyEnemyList;
		}
		return this;
	}
	
	@Override
	public void update(float dt) {
		rangeAlpha -= dt;
		if(rangeAlpha < 0) rangeAlpha = 0;
		if(dt >= timer) {
			Circle range = getRange();
			if(screen.enemyInRange(range, targeted) != null) {
				screen.game.playSound(GameSound.AREA);
				for(Enemy enemy : targetedEnemies) {
					if(Circle.colliding(range, enemy.hitZone)) {
						enemy.takeHit(getDamage());
						rangeAlpha = 1;
					}
				}
				for(Enemy enemy : targetedEnemies2) {
					if(Circle.colliding(range, enemy.hitZone)) {
						enemy.takeHit(getDamage());
						rangeAlpha = 1;
					}
				}
				timer = getCooldown();
			} else timer = 0f;
		} else timer -= dt;
	}

	public static AreaTowerPool pool;
	
	public void preDraw(ShapeRenderer shapeRenderer) {
		if(rangeAlpha <= 0) return;
		
		shapeRenderer.setColor(1f, 0f, 0f, 0.1f*rangeAlpha);
		getRange().draw(shapeRenderer);
	}
	
	public void draw(SpriteBatch batch) {

		super.draw(batch);
	}
	
	public AreaTower obtainCopy(float x, float y) {
		return pool.obtain().set(this,x,y);
	}
	
	static public class AreaTowerPool extends Pool<AreaTower> {

		GameplayScreen screen;
		
		public AreaTowerPool(GameplayScreen screen) {
			super(15);
			this.screen = screen;
		}

		@Override
		protected AreaTower newObject() {
			return new AreaTower().init(screen);
		}
	}
}
