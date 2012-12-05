package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

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
	
	float damage;
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
			if(screen.enemyInRange(range, targeted) != null) {
				for(Enemy enemy : targetedEnemies) {
					if(Circle.colliding(range, enemy.hitZone)) enemy.takeHit(damage);
				}
				for(Enemy enemy : targetedEnemies2) {
					if(Circle.colliding(range, enemy.hitZone)) enemy.takeHit(damage);
				}
				timer += cooldown-dt;
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
