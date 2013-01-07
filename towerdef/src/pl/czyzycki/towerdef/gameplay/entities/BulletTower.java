package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.TowerDef.GameSound;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Wie¿yckza strzelaj¹ca pociskami skierowanymi w konkretny cel
 * i zadaj¹cymi obra¿enia obszarowe.
 * @author Ciziu
 *
 */
public class BulletTower extends TargetTower {


	Bullet bullet;
	
	public static Animation[] bulletTowerAnimation;
	
	float rotation = 0;
	
	static public void loadAnimations(AssetManager assetMgr) {
		float animSpeed = 10;
		bulletTowerAnimation = new Animation[3];
		
		{	
			Texture texture = assetMgr.get("images/bullet-tower-airborne.png", Texture.class);
			TextureRegion[][] tmp = TextureRegion.split(texture, 80, 80);
			
			TextureRegion[] frames = new TextureRegion[tmp.length*tmp[0].length];

			for(int i=0; i<tmp.length; i++) {
				for(int j=0; j<tmp[i].length; j++) {
					frames[i*tmp.length+j] = tmp[i][j];
				}
			}
		
			bulletTowerAnimation[Targeted.AIRBORNE.ordinal()] = new Animation(animSpeed, frames);
		}
		
		{	
			Texture texture = assetMgr.get("images/bullet-tower-both.png", Texture.class);
			TextureRegion[][] tmp = TextureRegion.split(texture, 80, 80);
			
			TextureRegion[] frames = new TextureRegion[tmp.length*tmp[0].length];

			for(int i=0; i<tmp.length; i++) {
				for(int j=0; j<tmp[i].length; j++) {
					frames[i*tmp.length+j] = tmp[i][j];
				}
			}
		
			bulletTowerAnimation[Targeted.BOTH.ordinal()] = new Animation(animSpeed, frames);
		}
		
		{	
			Texture texture = assetMgr.get("images/bullet-tower-ground.png", Texture.class);
			TextureRegion[][] tmp = TextureRegion.split(texture, 80, 80);
			
			TextureRegion[] frames = new TextureRegion[tmp.length*tmp[0].length];

			for(int i=0; i<tmp.length; i++) {
				for(int j=0; j<tmp[i].length; j++) {
					frames[i*tmp.length+j] = tmp[i][j];
				}
			}
		
			bulletTowerAnimation[Targeted.GROUND.ordinal()] = new Animation(animSpeed, frames);
		}
		
	}
	
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
			rotation = 720-(direction.angle()+90);
			if(dt >= timer) {
				timer += getCooldown() - dt;
				screen.addBullet(screen.getBulletPool().obtain().set(bullet, this));
				screen.game.playSound(GameSound.BULLET_START);
			} else timer -= dt;
		} else if(dt >= timer) timer = 0f;
		else timer -= dt;
	}
	
	public void draw(SpriteBatch batch) {
		TextureRegion currentFrame = bulletTowerAnimation[targeted.ordinal()].getKeyFrame(rotation, true);
		batch.draw(currentFrame, pos.x-currentFrame.getRegionWidth()/2.0f, pos.y-currentFrame.getRegionHeight()/2.0f);
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

