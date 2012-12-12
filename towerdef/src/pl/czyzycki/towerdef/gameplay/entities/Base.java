package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

public class Base {
	Vector2 pos;
	Circle hitZone;
	int hp, maxHp;
	boolean destroyed;
	
	StringBuilder hpText;
	
	public Base() {
		hpText = new StringBuilder();
	}
	
	public void init(float x, float y) {
		pos = new Vector2(x, y);
		hitZone.pos = pos;
		maxHp = hp;
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
}