package pl.czyzycki.towerdef.gameplay.entities;


import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Klasa bazowa wie¿yczek
 * @author Ciziu
 *
 */
abstract public class Tower {

	public static Sprite basicSprite; // Sprite placeholder
	
	// Pusta lista wrogów, s³u¿¹ca za placeholder niektórym wie¿óm
	protected static Array<Enemy> emptyEnemyList = new Array<Enemy>(false,1);
	
	// Enum cech mo¿liwych do upgrade'owania
	enum Upgradeable {
		RANGE,
		COOLDOWN,
		MULTIPLIER,
		DAMAGE
	}
	// Pojedynczy upgrade. Szkic klasy :P
	static class Upgrade {
		static class Level {
			float value, cost;
		}
		Level[] levels;
		int iter = 0;
		Upgradeable upgraded;
	}
	//Enum mo¿liwych grup celów wie¿yczki
	public enum Targeted {
		GROUND,
		AIRBORNE,
		BOTH
	}
	
	GameplayScreen screen;
	Upgrade[] upgrades; // Tablica ugrade'ów danej wie¿y (wype³niane z JSONa)
	Targeted targeted; // Wybrana grupa celów (ignorowane w SlowdownTower, zgodnie z projektem)
	Vector2 pos;
	Circle range;
	Sprite sprite;
	float cooldown, timer, cost; // W cooldown parametr, w timer jego licznik
	
	public Tower() {
		sprite = new Sprite(basicSprite);
	}
	
	// Inicjalizacja pamiêci nowego obiektu dodawanego do puli
	protected void _init(GameplayScreen screen) {
		this.screen = screen;
		pos = new Vector2();
		range = new Circle();
		range.pos = pos;
	}
	
	// Pseudokonstruktor obiektu wyjêtego z puli
	protected void _set(Tower tower, float x, float y) {
		upgrades = tower.upgrades;
		targeted = tower.targeted;
		pos.set(x, y);
		range.radius = tower.range.radius;
		cooldown = tower.cooldown;
		timer = 0f;
		cost = tower.cost;
		sprite.setPosition(pos.x-sprite.getWidth()/2f, pos.y-sprite.getHeight()/2f);
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	abstract public void update(float dt);
	
	public void debugDraw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.FilledCircle);
		shapeRenderer.setColor(0,0,1,0.2f);
		range.draw(shapeRenderer);
		shapeRenderer.end();
	}
	
	public void debugText(SpriteBatch batch, BitmapFont debugFont,  StringBuilder timerText) {
		timerText.length = 0;
		timerText.append(timer);
		debugFont.draw(batch, timerText, pos.x + 30f, pos.y - 30f);
	}
}
