package pl.czyzycki.towerdef.gameplay.entities;


import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.entities.Tower.Upgrade.Level;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
	public enum Upgradeable {
		RANGE,
		COOLDOWN,
		MULTIPLIER,
		DAMAGE
	}
	// Pojedynczy upgrade. Szkic klasy :P
	static public class Upgrade {
		static public class Level {
			public float value, cost;
		}
		public Level[] levels;
		public Upgradeable upgraded;
	}
	//Enum mo¿liwych grup celów wie¿yczki
	public enum Targeted {
		GROUND,
		AIRBORNE,
		BOTH
	}
	
	GameplayScreen screen;
	public Upgrade[] upgrades; // Tablica ugrade'ów danej wie¿y (wype³niane z JSONa)
	public int[] upgradeLevelIters = new int[16]; // Poziom upgrade dla konkretnej wie¿yczki. 
	Targeted targeted; // Wybrana grupa celów (ignorowane w SlowdownTower, zgodnie z projektem)
	Vector2 pos;
	Circle range;	// To jest base range, bez upgrade - ¿eby dostaæ prawdziwy range nale¿y wywo³aæ getRange()
	Circle realRange;
	Sprite sprite;
	float cooldown, timer; // W cooldown parametr, w timer jego licznik
	float damage;
	
	public String icon;
	public String groupIcon;
	
	public String textureFileName;

	public float cost;
	
	StringBuilder timerText;
	
	public Tower() {
		
	}
	
	Circle getRange() {
		Level level = getUpgradeLevel(Upgradeable.RANGE);
		if(level == null)
			return range;
		
		realRange.pos = range.pos;
		realRange.radius = level.value;
		
		return realRange;
	}
	
	float getCooldown() {
		Level level = getUpgradeLevel(Upgradeable.COOLDOWN);
		if(level == null)
			return cooldown;
		
		return level.value;
	}
	
	float getDamage() {
		Level level = getUpgradeLevel(Upgradeable.DAMAGE);
		if(level == null)
			return damage;
		
		return level.value;
	}
	
	Level getUpgradeLevel(Upgradeable upgradeable) {
		Upgrade upgrade = null;
		int upgradeIndex = 0;
		
		// szukaj odpowiedniego upgrade na liscie
		if(upgrades != null) {
			for(Upgrade u: upgrades) {
				if(u.upgraded == upgradeable) {
					upgrade = u;
					break;
				}
				upgradeIndex++;
			}
		}
		
		if(upgrade == null) return null;
		
		if(upgradeLevelIters[upgradeIndex] == 0) return null;
		
		if(screen.maxUpgradeIsWorking()) {
			return upgrade.levels[upgrade.levels.length-1];
		} else {
			int levelId = upgradeLevelIters[upgradeIndex] - 1;
		
			return upgrade.levels[levelId];
		}
	}
	
	public Vector2 getPos() {
		return pos;
	}
	
	// Inicjalizacja pamiêci nowego obiektu dodawanego do puli
	protected void _init(GameplayScreen screen) {
		this.screen = screen;
		pos = new Vector2();
		range = new Circle();
		realRange = new Circle();
		timerText = new StringBuilder();
		range.pos = pos;
		sprite = new Sprite();
	}
	
	// Pseudokonstruktor obiektu wyjêtego z puli
	protected void _set(Tower tower, float x, float y) {
		upgrades = tower.upgrades;
		for(int i=0; i<upgradeLevelIters.length; i++) {
			upgradeLevelIters[i] = 0;
		}
		targeted = tower.targeted;
		pos.set(x, y);
		range.radius = tower.range.radius;
		cooldown = tower.cooldown;
		timer = 0f;
		cost = tower.cost;
		sprite.set(tower.sprite);
		sprite.setPosition(pos.x-sprite.getWidth()/2f, pos.y-sprite.getHeight()/2f);
	}
	
	public void loadSprite(TextureAtlas texAtlas) {
		sprite = new Sprite(texAtlas.createSprite(textureFileName));
	}
	
	public void preDraw(ShapeRenderer shapeRenderer) {
		// Rysowanie czegoœ przed wszystkimi wie¿yczkami.
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	public boolean collision(float x, float y) {
		float radius = sprite.getWidth()/2.0f;
		float dx = x - pos.x;
		float dy = y - pos.y;
		return radius*radius > dx*dx + dy*dy;
	}
	
	abstract public void update(float dt);
	
	public void debugDraw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.FilledCircle);
		shapeRenderer.setColor(0,0,1,0.2f);
		getRange().draw(shapeRenderer);
		shapeRenderer.end();
	}
	
	public void debugText(SpriteBatch batch, BitmapFont debugFont) {
		timerText.length = 0;
		timerText.append(timer);
		debugFont.draw(batch, timerText, pos.x + 30f, pos.y - 30f);
	}
	
	public void whenSelling() {
		// tutaj mo¿e jakieœ liczenie kasy do zwrócenia za upgrady?
	}
	
	abstract public Tower obtainCopy(float x, float y);
}
