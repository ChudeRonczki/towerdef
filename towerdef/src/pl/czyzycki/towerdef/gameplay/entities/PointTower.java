package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.TowerDef.GameSound;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;

/**
 * Najbardziej klasyczna wie¿yczka. Zadaje obra¿enia pojedynczemu celowi
 * znajduj¹cemu siê w zasiêgu. Z przyczyn wydajnoœciowo praktycznych,
 * pociski fizycznie nie istniej¹ :P
 *
 */
public class PointTower extends TargetTower {
	
	public static Animation[] pointTowerAnimation;
	float rotation = 0;
	float lineTimer = 0;

	public PointTower() {
		super();
	}
	
	static public void loadAnimations(AssetManager assetMgr) {
		float animSpeed = 10;
		pointTowerAnimation = new Animation[3];
		
		{	
			Texture texture = assetMgr.get("images/point-tower-airborne.png", Texture.class);
			TextureRegion[][] tmp = TextureRegion.split(texture, 80, 80);
			
			TextureRegion[] frames = new TextureRegion[tmp.length*tmp[0].length];

			for(int i=0; i<tmp.length; i++) {
				for(int j=0; j<tmp[i].length; j++) {
					frames[i*tmp.length+j] = tmp[i][j];
				}
			}
		
			pointTowerAnimation[Targeted.AIRBORNE.ordinal()] = new Animation(animSpeed, frames);
		}
		
		{	
			Texture texture = assetMgr.get("images/point-tower-both.png", Texture.class);
			TextureRegion[][] tmp = TextureRegion.split(texture, 80, 80);
			
			TextureRegion[] frames = new TextureRegion[tmp.length*tmp[0].length];

			for(int i=0; i<tmp.length; i++) {
				for(int j=0; j<tmp[i].length; j++) {
					frames[i*tmp.length+j] = tmp[i][j];
				}
			}
		
			pointTowerAnimation[Targeted.BOTH.ordinal()] = new Animation(animSpeed, frames);
		}
		
		{	
			Texture texture = assetMgr.get("images/point-tower-ground.png", Texture.class);
			TextureRegion[][] tmp = TextureRegion.split(texture, 80, 80);
			
			TextureRegion[] frames = new TextureRegion[tmp.length*tmp[0].length];

			for(int i=0; i<tmp.length; i++) {
				for(int j=0; j<tmp[i].length; j++) {
					frames[i*tmp.length+j] = tmp[i][j];
				}
			}
		
			pointTowerAnimation[Targeted.GROUND.ordinal()] = new Animation(animSpeed, frames);
		}
		
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
		lineTimer -= dt*4;
		if(lineTimer < 0) lineTimer = 0;
		if(checkTarget()) {
			direction.set(target.pos).sub(pos).nor();
			rotation = 720-(direction.angle()+90);
			if(dt >= timer) {
				lineTimer = 1;
				timer = getCooldown();
				target.takeHit(getDamage());
				screen.game.playSound(GameSound.POINT);
			} else timer -= dt;
		} else if(dt >= timer) timer = 0f;
		else timer -= dt;
	}

	public void preDrawLine(ShapeRenderer shapeRenderer) {
		if(target != null && lineTimer > 0) {
			switch(targeted) {
			case AIRBORNE:
				shapeRenderer.setColor(0.5f, 0.5f, 1, lineTimer);
				break;
			case GROUND:
				shapeRenderer.setColor(0.5f, 1, 0.5f, lineTimer);
				break;
			default:
				shapeRenderer.setColor(1, 1, 0.5f, lineTimer);
				break;
			}
			shapeRenderer.line(pos.x, pos.y-7.5f, target.pos.x, target.pos.y);
		}
	}
	
	public void draw(SpriteBatch batch) {
		TextureRegion currentFrame = pointTowerAnimation[targeted.ordinal()].getKeyFrame(rotation, true);
		batch.draw(currentFrame, pos.x-currentFrame.getRegionWidth()/2.0f, pos.y-currentFrame.getRegionHeight()/2.0f);
	}
	
	public static PointTowerPool pool;
	
	public PointTower obtainCopy(float x, float y) {
		return pool.obtain().set(this, x, y);
	}

	static public class PointTowerPool extends Pool<PointTower> {

		GameplayScreen screen;
		
		public PointTowerPool(GameplayScreen screen) {
			super(15);
			this.screen = screen;
		}

		@Override
		protected PointTower newObject() {
			return new PointTower().init(screen);
		}
	}
}
