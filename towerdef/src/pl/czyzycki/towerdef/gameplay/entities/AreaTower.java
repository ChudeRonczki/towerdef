package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.TowerDef.GameSound;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


/**
 * Wie�yczka obszarowa. Zadaje obra�enia wszystkim celom znajduj�cym si�
 * w zasi�gu w momencie strza�u, kt�ry oddawany jest gdy w zasi�gu jest
 * przynajmniej jeden cel i nie jest aktywny cooldown.
 * @author Ciziu
 *
 */
public class AreaTower extends Tower {
	
	Array<Enemy> targetedEnemies, targetedEnemies2;
		
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
		if(dt >= timer) {
			Circle range = getRange();
			if(screen.enemyInRange(range, targeted) != null) {
				screen.game.playSound(GameSound.AREA);
				for(Enemy enemy : targetedEnemies) {
					if(Circle.colliding(range, enemy.hitZone)) enemy.takeHit(getDamage());
				}
				for(Enemy enemy : targetedEnemies2) {
					if(Circle.colliding(range, enemy.hitZone)) enemy.takeHit(getDamage());
				}
				timer += getCooldown()-dt;
			} else timer = 0f;
		} else timer -= dt;
	}

	public static AreaTowerPool pool;
	
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
