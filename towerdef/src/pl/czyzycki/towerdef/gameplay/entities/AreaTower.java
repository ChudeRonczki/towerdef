package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
				timer += getCooldown()-dt;
			} else timer = 0f;
		} else timer -= dt;
	}

	public static AreaTowerPool pool;
	
	public void draw(SpriteBatch batch) {
		ShapeRenderer sr = TowerDef.getGame().getGameplayScreen().shapeRenderer;
		
		batch.end();
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.FilledCircle);
		sr.setColor(1f, 0f, 0f, 0.1f*rangeAlpha);
		getRange().draw(sr);
		sr.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
		batch.begin();
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
