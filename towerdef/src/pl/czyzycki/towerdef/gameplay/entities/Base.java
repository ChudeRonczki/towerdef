package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Broniona przez gracza baza
 *
 */
public class Base {
	Vector2 pos;
	Circle hitZone;
	int hp, maxHp, hpBonus;
	boolean destroyed;
	float timer=0;
	
	StringBuilder hpText;
	
	static Animation baseAnimation;
	
	static public void loadAnimations(AssetManager assetMgr) {
		Texture texture = assetMgr.get("images/base-anim.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(texture, 80, 80);
		
		float animSpeed = 0.05f;
		
		
		TextureRegion[] frames = new TextureRegion[100];

		int k = 0;
		boolean done = false;
		
		for(int i=0; i<tmp.length && !done; i++) {
			for(int j=0; j<tmp[i].length && !done; j++) {
				frames[k] = tmp[i][j];
				k++;
				if(k == frames.length) done = true;
			}
		}
		
		baseAnimation = new Animation(animSpeed, frames);
	}
	
	public Base() {
		hpText = new StringBuilder();
	}
	
	public void init(float x, float y) {
		pos = new Vector2(x, y);
		hitZone.pos = pos;
		maxHp = hp;
	}
	
	public void draw(SpriteBatch batch, float dt) {
		timer += dt;
		TextureRegion currentFrame = baseAnimation.getKeyFrame(timer, true);
		batch.draw(currentFrame, pos.x-currentFrame.getRegionWidth()/2.0f,
								 pos.y-currentFrame.getRegionHeight()/2.0f);
	}
	
	public void takeHit(int damage) {
		hp -= damage;
		if(hp <= 0) {
			hp = 0;
			destroyed = true;
		}
	}
	
	public void debugDraw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.FilledCircle);
		shapeRenderer.setColor(0,1,0,0.5f);
		hitZone.draw(shapeRenderer);
		shapeRenderer.end();
	}
	
	public void debugText(SpriteBatch batch, BitmapFont debugFont) {
		hpText.length = 0;
		hpText.append(hp);
		hpText.append('/');
		hpText.append(maxHp);
		debugFont.draw(batch, hpText, pos.x + 30f, pos.y - 30f);
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public Vector2 getPos() {
		return pos;
	}

	public int getHp() {
		return hp;
	}

	public int getHpBonus() {
		return hpBonus;
	}

	public int getMaxHp() {
		return maxHp;
	}
}
