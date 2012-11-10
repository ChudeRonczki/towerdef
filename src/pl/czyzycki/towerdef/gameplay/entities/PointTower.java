package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.utils.Pool;

/**
 * Najbardziej klasyczna wie¿yczka. Zadaje obra¿enia pojedynczemu celowi
 * znajduj¹cemu siê w zasiêgu. Z przyczyn wydajnoœciowo praktycznych,
 * pociski fizycznie nie istniej¹ :P
 * @author Ciziu
 *
 */
public class PointTower extends TargetTower {

	float damage;
	
	public PointTower() {
		super();
	}
	
	public PointTower init(GameplayScreen screen) {
		super._init(screen);
		return this;
	}
	
	public PointTower set(PointTower tower, float x, float y) {
		super._set(tower, x, y);
		damage = tower.damage;
		return this;
	}
	
	@Override
	public void update(float dt) {
		if(checkTarget()) {
			direction.set(target.pos).sub(pos).nor();
			if(dt >= timer) {
				timer += cooldown - dt;
				target.takeHit(damage);
			} else timer -= dt;
		} else if(dt >= timer) timer = 0f;
		else timer -= dt;
	}

	static public class PointTowerPool extends Pool<PointTower> {

		GameplayScreen screen;
		
		public PointTowerPool(GameplayScreen screen, int initialSize) {
			super(15);
			this.screen = screen;
		}

		@Override
		protected PointTower newObject() {
			return new PointTower().init(screen);
		}
	}
}
