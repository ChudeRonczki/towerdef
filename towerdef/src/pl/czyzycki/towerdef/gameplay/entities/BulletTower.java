package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.TowerDef.GameSound;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.utils.Pool;

/**
 * Wie�yckza strzelaj�ca pociskami skierowanymi w konkretny cel
 * i zadaj�cymi obra�enia obszarowe.
 * @author Ciziu
 *
 */
public class BulletTower extends TargetTower {


	Bullet bullet;
	
	public BulletTower() {
		super();
	}
	
	public BulletTower init(GameplayScreen screen) {
		super._init(screen);
		bullet = new Bullet(false);
		return this;
	}
	
	public BulletTower set(BulletTower tower, float x, float y) {
		super._set(tower, x, y);
		bullet = tower.bullet;
		if(targeted == Targeted.BOTH) {
			bullet.targetedEnemies = screen.getEnemiesList(Targeted.GROUND);
			bullet.targetedEnemies2 = screen.getEnemiesList(Targeted.AIRBORNE);
		} else {
			bullet.targetedEnemies = screen.getEnemiesList(targeted);
			bullet.targetedEnemies2 = emptyEnemyList;
		}
		return this;
	}
	
	@Override
	public void update(float dt) {
		if(checkTarget()) {
			direction.set(target.pos).sub(pos).nor();
			if(dt >= timer) {
				timer += getCooldown() - dt;
				screen.addBullet(screen.getBulletPool().obtain().set(bullet, this));
				screen.game.playSound(GameSound.BULLET_START);
			} else timer -= dt;
		} else if(dt >= timer) timer = 0f;
		else timer -= dt;
	}

	public static BulletTowerPool pool;
	
	public BulletTower obtainCopy(float x, float y) {
		return pool.obtain().set(this, x, y);
	}
	
	static public class BulletTowerPool extends Pool<BulletTower> {

		GameplayScreen screen;
		
		public BulletTowerPool(GameplayScreen screen) {
			super(15);
			this.screen = screen;
		}

		@Override
		protected BulletTower newObject() {
			return new BulletTower().init(screen);
		}
	}
}

