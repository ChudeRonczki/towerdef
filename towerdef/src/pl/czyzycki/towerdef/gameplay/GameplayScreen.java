package pl.czyzycki.towerdef.gameplay;

import java.util.Iterator;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.TowerDef.GameSound;
import pl.czyzycki.towerdef.gameplay.entities.AreaTower;
import pl.czyzycki.towerdef.gameplay.entities.AreaTower.AreaTowerPool;
import pl.czyzycki.towerdef.gameplay.entities.Base;
import pl.czyzycki.towerdef.gameplay.entities.Bonus;
import pl.czyzycki.towerdef.gameplay.entities.Bonus.BonusPool;
import pl.czyzycki.towerdef.gameplay.entities.Bonus.BonusType;
import pl.czyzycki.towerdef.gameplay.entities.Bullet;
import pl.czyzycki.towerdef.gameplay.entities.Bullet.BulletPool;
import pl.czyzycki.towerdef.gameplay.entities.BulletTower;
import pl.czyzycki.towerdef.gameplay.entities.BulletTower.BulletTowerPool;
import pl.czyzycki.towerdef.gameplay.entities.Enemy;
import pl.czyzycki.towerdef.gameplay.entities.Field;
import pl.czyzycki.towerdef.gameplay.entities.PointTower;
import pl.czyzycki.towerdef.gameplay.entities.PointTower.PointTowerPool;
import pl.czyzycki.towerdef.gameplay.entities.SlowdownTower;
import pl.czyzycki.towerdef.gameplay.entities.SlowdownTower.SlowdownTowerPool;
import pl.czyzycki.towerdef.gameplay.entities.Spawn;
import pl.czyzycki.towerdef.gameplay.entities.Tower;
import pl.czyzycki.towerdef.gameplay.entities.Tower.Targeted;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;
import pl.czyzycki.towerdef.gameplay.helpers.MapChecker;
import pl.czyzycki.towerdef.menus.Lose;
import pl.czyzycki.towerdef.menus.OptionsScreen;
import pl.czyzycki.towerdef.menus.Pause;
import pl.czyzycki.towerdef.menus.Win;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * G³ówna klasa ekranu w³aœciwej rozgrywki.
 *
 */
public class GameplayScreen implements Screen {
	
	enum ViewportConstraint {
		WIDTH,
		HEIGHT
	}
	
	final static int[] visibleLayers = {0,1,2};
	
	final public static float viewportWidth = 1280,
			viewportHeight = 800,
			tileWidth = 80,
			tileHeight = 80;
	
	final static ViewportConstraint viewportConstraint = ViewportConstraint.HEIGHT;
	
	public TowerDef game;
	
	Json json;
	
	OrthographicCamera camera;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	
	Pause pauseMenu;
	Lose loseMenu;
	Win winMenu;
	
	/*
	 * Informacje zwi¹zane ze spawnowaniem bonusów, nie z samymi bonusami
	 */
	static class BonusData {
		int odds;
		float cooldown, timer;
	}
	BonusData bonusesData[];
	Bonus modelBonuses[];
	float bombDamage;
	
	int starGoals[];
	
	Array<Spawn> spawns;
	Array<Enemy> groundEnemies, airborneEnemies;
	Array<Tower> towers;
	Array<Bullet> bullets;
	Array<Field> fields;
	Array<Bonus> bonuses;
	
	Array<PooledEffect> effects = new Array<PooledEffect>();
	
	BulletPool bulletPool;
	BonusPool bonusPool;
	
	TileMapRenderer tileMapRenderer;
	TiledMap map;
	MapChecker buildingChecker;
	MapChecker roadChecker;
	Base base;
	int wavesLeft;
	float timeAcc;//, sweepAcc;
	int money, points;
	float maxUpgradeTimer = -5;
	boolean faster;
	
	InputMultiplexer inputMultiplexer;
	GameplayDebug debug;
	GameplayGUI gui;
	GameplayLoader loader;
	GameplayUpgradeGUI upgradeGui;

	float maxUpgradeTime;
	
	@SuppressWarnings("unchecked")
	public GameplayScreen(TowerDef game) {
		this.game = game;
		
		json = new Json();
		
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		spawns = new Array<Spawn>(true,5);
		groundEnemies = new Array<Enemy>(false,50);
		airborneEnemies = new Array<Enemy>(false,50);
		towers = new Array<Tower>(false,60);
		bullets = new Array<Bullet>(false,200);
		fields = new Array<Field>(true,20);
		bonuses = new Array<Bonus>(false,5);
		
		bulletPool = new BulletPool();
		bonusPool = new BonusPool(this);
		
		AreaTower.pool = new AreaTowerPool(this);
		BulletTower.pool = new BulletTowerPool(this);
		PointTower.pool = new PointTowerPool(this);
		SlowdownTower.pool = new SlowdownTowerPool(this);
		
		TextureAtlas texAtlas = game.getAssetManager().get("images/objects.pack", TextureAtlas.class);
		Tower.basicSprite = texAtlas.createSprite("pause");
		Enemy.basicSprite = texAtlas.createSprite("pause");
		Bullet.basicSprite = texAtlas.createSprite("bullet");
		
		OrderedMap<String, Object> jsonData = (OrderedMap<String, Object>)new JsonReader().parse(Gdx.files.internal("config/config.json"));
		Spawn.waveTimeout = (Float)((OrderedMap<String, Object>)jsonData.get("spawns")).get("waveTimeout");
		
		camera.setToOrtho(false, viewportWidth, viewportHeight);
		
		debug = new GameplayDebug(this);
		gui = new GameplayGUI(this);
		gui.loadTowerIcons(texAtlas);
		loader = new GameplayLoader(this);
		upgradeGui = new GameplayUpgradeGUI(this);
		upgradeGui.load(texAtlas);
		pauseMenu = new Pause(game, this);
		loseMenu = new Lose(game, this);
		winMenu = new Win(game, this);
		inputMultiplexer = new InputMultiplexer(winMenu, loseMenu, pauseMenu, gui.detector, new GestureDetector(upgradeGui.listener), new GameplayGestureDetector(this));
	
		modelBonuses = new Bonus[3];
		modelBonuses[BonusType.MONEY.ordinal()] = json.fromJson(Bonus.class, Gdx.files.internal("config/moneyBonus.json"));
		modelBonuses[BonusType.MONEY.ordinal()].setType(BonusType.MONEY);
		modelBonuses[BonusType.MONEY.ordinal()].setSprite(texAtlas.createSprite("moneyBonus"));
		
		OrderedMap<String, Object> bombData = (OrderedMap<String, Object>)new JsonReader().parse(Gdx.files.internal("config/bomb.json"));
		gui.bombBlastZone = new Circle();
		gui.bombBlastZone.pos = new Vector2();
		gui.bombBlastZone.radius = (Float)bombData.remove("range");
		bombDamage = (Float)bombData.remove("damage");
		modelBonuses[BonusType.BOMB.ordinal()] = json.readValue(Bonus.class, bombData);
		modelBonuses[BonusType.BOMB.ordinal()].setType(BonusType.BOMB);
		modelBonuses[BonusType.BOMB.ordinal()].setSprite(texAtlas.createSprite("bomb"));
		OrderedMap<String, Object> maxUpData = (OrderedMap<String, Object>)new JsonReader().parse(Gdx.files.internal("config/maxUpgrade.json"));
		maxUpgradeTime = (Float)maxUpData.remove("duration");
		modelBonuses[BonusType.UPGRADE.ordinal()] = json.readValue(Bonus.class, maxUpData);
		modelBonuses[BonusType.UPGRADE.ordinal()].setType(BonusType.UPGRADE);
		modelBonuses[BonusType.UPGRADE.ordinal()].setSprite(texAtlas.createSprite("maxUpgrade"));
	}
	
	public void loadMap(String name) {
		upgradeGui.setSelectedTower(null);
		spawns.clear();
		groundEnemies.clear();
		airborneEnemies.clear();
		for(Tower tower : towers) {
			if(tower instanceof AreaTower) AreaTower.pool.free((AreaTower)tower);
			else if(tower instanceof BulletTower) BulletTower.pool.free((BulletTower)tower);
			else if(tower instanceof PointTower) PointTower.pool.free((PointTower)tower);
			else SlowdownTower.pool.free((SlowdownTower)tower);
		}
		towers.clear();
		bulletPool.free(bullets);
		bullets.clear();
		fields.clear();
		bonusPool.free(bonuses);
		bonuses.clear();
		timeAcc = 0f;
		loader.loadMap(name);
	}

	@Override
	public void dispose() {
		pauseMenu.dispose();
		loseMenu.dispose();
		winMenu.dispose();
		if(tileMapRenderer != null) tileMapRenderer.dispose();
		shapeRenderer.dispose();
		batch.dispose();
	}

	boolean addTower(float x, float y) {
		if(buildingChecker.check(x,y)) return false;
		if(gui.selectedTowerType.cost <= money) {
			if(OptionsScreen.vibrationEnabled()) Gdx.input.vibrate(50);
			money -= gui.selectedTowerType.cost;
			towers.add(gui.selectedTowerType.obtainCopy((float)Math.floor(x/tileWidth)*tileWidth + tileWidth/2f,
					(float)Math.floor(y/tileHeight)*tileHeight + tileHeight/2f));
			buildingChecker.set(x, y, true);
			return true;
		} else return false;
	}
	
	Tower getTower(float x, float y) {
		for(Tower tower: towers) {
			if(tower.collision(x, y))
				return tower;
		}
		return null;
	}
	
	public void sellTower(Tower tower) {
		towers.removeValue(tower, true);
		tower.whenSelling();
		Vector2 pos = tower.getPos();
		buildingChecker.set(pos.x, pos.y, false);
	}
	
	public void update(float dt) {
		
		if(pauseMenu.isShowed() || loseMenu.isShowed() || winMenu.isShowed())
			return;
		
		if(dt > 1f) return; // Odpauzowanie
		float realDt = 0.01f;
		if(faster) {
			timeAcc += dt*2f;
			realDt = 0.005f;
		} else timeAcc += dt;
		//sweepAcc += dt;
		dt = 0.01f;
		
		for(; timeAcc >= dt; timeAcc -= dt) {
			maxUpgradeTimer -= dt;
			
			for(BonusData bonusData : bonusesData) {
				if(dt >= bonusData.timer) bonusData.timer = 0f;
				else bonusData.timer -= dt;
			}
			
			for(Spawn spawn : spawns) spawn.update(dt);
			for(Tower tower : towers) tower.update(dt);
			

			Iterator<Field> fieldIter = fields.iterator();
			while(fieldIter.hasNext()) {
				Field field = fieldIter.next();
				if(field.update(dt)) fieldIter.remove();
			}
			
			
			Iterator<Enemy> enemyIter = groundEnemies.iterator();
			while(enemyIter.hasNext()) {
				Enemy enemy = enemyIter.next();
				if(enemy.update(dt)) {
					enemyIter.remove();
					game.playSound(GameSound.DESTROYED);
					if(!enemy.hitTheBase()) {
						rollForBonus(enemy);
						money += enemy.getMoney();
						points += enemy.getPoints();
					}
				}
			}
			enemyIter = airborneEnemies.iterator();
			while(enemyIter.hasNext()) {
				Enemy enemy = enemyIter.next();
				if(enemy.update(dt)) {
					enemyIter.remove();
					game.playSound(GameSound.DESTROYED);
					if(!enemy.hitTheBase()) {
						rollForBonus(enemy);
						money += enemy.getMoney();
						points += enemy.getPoints();
					}
				}
			}
			
			Iterator<Bullet> bulletIter = bullets.iterator();
			while(bulletIter.hasNext()) {
				Bullet bullet = bulletIter.next();
				if(bullet.update(dt)) bulletIter.remove();
			}
			
			Iterator<Bonus> bonusIter = bonuses.iterator();
			while(bonusIter.hasNext()) {
				Bonus bonus = bonusIter.next();
				if(bonus.update(realDt)) bonusIter.remove();
			}
			
			// Testowanie warunków koñcowych
			if(base.isDestroyed()) {
				loseMenu.show();
				return;
			} else if(groundEnemies.size == 0 && airborneEnemies.size == 0) {
				boolean reallyFinished = true;
				for(Spawn spawn : spawns) {
					if(!spawn.isWaveFinished()) {
						reallyFinished = false;
						break;
					}
				}
				if(reallyFinished) {
					if(wavesLeft > 1) {
						--wavesLeft;
						for(Spawn spawn : spawns) {
							spawn.startWave();
						}
					} else {
						points += base.getHpBonus()*base.getHp();
						winMenu.show(points, getStars());
						return;
					}
				}
			}
		}
		
		/* Czyszczenie zab³¹kanych kul. W zwi¹zku ze zmianami projektowymi bodaj zbêdne
		for(; sweepAcc >= 1f; sweepAcc -= 1f) {
			Iterator<Bullet> bulletIter = bullets.iterator();
			while(bulletIter.hasNext()) {
				Bullet bullet = bulletIter.next();
				if(bullet.pos.x < -100.f || bullet.pos.x > map.width*tileWidth+100f
						|| bullet.pos.y < -100f || bullet.pos.y > map.height*tileHeight+100f) bulletIter.remove();
			}
		}
		*/
	}

	public void restartMap() {
		loadMap("");
	}
	
	private void rollForBonus(Enemy enemy) {
		int roll = MathUtils.random(99);
		if(roll < bonusesData[BonusType.MONEY.ordinal()].odds) {
			if(bonusesData[BonusType.MONEY.ordinal()].timer <= 0f) {
				bonuses.add(bonusPool.obtain().set(modelBonuses[BonusType.MONEY.ordinal()], enemy.getPos()));
				bonusesData[BonusType.MONEY.ordinal()].timer = bonusesData[BonusType.MONEY.ordinal()].cooldown;
			}
		} else if(roll < bonusesData[BonusType.BOMB.ordinal()].odds) {
			if(bonusesData[BonusType.BOMB.ordinal()].timer <= 0f) {
				bonuses.add(bonusPool.obtain().set(modelBonuses[BonusType.BOMB.ordinal()], enemy.getPos()));
				bonusesData[BonusType.BOMB.ordinal()].timer = bonusesData[BonusType.BOMB.ordinal()].cooldown;
			}
		} else if(roll < bonusesData[BonusType.UPGRADE.ordinal()].odds) {
			if(bonusesData[BonusType.UPGRADE.ordinal()].timer <= 0f) {
				bonuses.add(bonusPool.obtain().set(modelBonuses[BonusType.UPGRADE.ordinal()], enemy.getPos()));
				bonusesData[BonusType.UPGRADE.ordinal()].timer = bonusesData[BonusType.UPGRADE.ordinal()].cooldown;
			} 
		}
	}

	public void adjustCamera() {
		float mapHeight = tileMapRenderer.getMapHeightUnits();
		float mapWidth  = tileMapRenderer.getMapWidthUnits();
		
		float widthAspect  = mapWidth/camera.viewportWidth;
		float heightAspect = mapHeight/camera.viewportHeight;
		
		float minAspect = widthAspect < heightAspect ? widthAspect : heightAspect;
		
		float maxZoom = minAspect;
		
		float minZoom = Gdx.graphics.getHeight() / camera.viewportHeight;
		
		float zoom = camera.zoom;
	
		if(maxZoom < zoom) {
			zoom = maxZoom;
			camera.zoom = zoom;
		}
		
		if(minZoom < maxZoom) {
			if(minZoom > zoom) {
				zoom = minZoom;
				camera.zoom = zoom;
			}
		} else {
			// jeœli jest na odwrót (maxZoom < minZoom) to mamy bardzo du¿y wyœwietlacz i
			// nie da siê zrobiæ tak ¿eby piksel w œwiecie gry do piksela na wyœwietlaczu by³ w proporcji 1:1
			// poniewa¿ widaæ by by³o obszar za plansz¹
			
			// ustawiamy taki zoom, zeby by³o widaæ ca³¹ planszê
			zoom = maxZoom;
			camera.zoom = zoom;
		}
		
		if(camera.position.x/zoom < camera.viewportWidth/2.0f)
			camera.position.x = camera.viewportWidth/2.0f*zoom;
		if(camera.position.y/zoom < camera.viewportHeight/2.0f)
			camera.position.y = camera.viewportHeight/2.0f*zoom;
		
		float maxX = mapWidth - camera.viewportWidth/2.0f*zoom;
		if(camera.position.x > maxX)
			camera.position.x = maxX;
		float maxY = mapHeight - camera.viewportHeight/2.0f*zoom;
		if(camera.position.y > maxY)
			camera.position.y = maxY;
		
		camera.update();
	}
	
	@Override
	public void render(float dt) {
		update(dt);
		
		// zapewnia ¿e kamera nie wyjdzie poza ekran
		adjustCamera();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		tileMapRenderer.render(camera,visibleLayers);
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.FilledCircle);
		for(Tower tower : towers) {
			tower.preDraw(shapeRenderer);
		}
		
		for(Bullet bullet : bullets) {
			bullet.drawExplosion(shapeRenderer);
		}
		
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Line);
		for(Tower tower : towers) {
			tower.preDrawLine(shapeRenderer);
		}
		shapeRenderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
		batch.begin();
		for(Tower tower : towers) {
			tower.draw(batch);
		}
		for(Bullet bullet : bullets) {
			bullet.draw(batch);
		}
		base.draw(batch, dt);
		
		// Poni¿szy blok odpowiada za sortowanie po Y przeciwników
		{
			int drawedEnemies = 0;
			float maxY = -1000;
			float prevMaxY = 1000;
			
			while(drawedEnemies != groundEnemies.size + airborneEnemies.size) {
				
				for(Enemy enemy : groundEnemies) {
					float posY = enemy.getPos().y;
					if(posY < prevMaxY && posY > maxY)
						maxY = posY;
				}
				
				for(Enemy enemy : airborneEnemies) {
					float posY = enemy.getPos().y;
					if(posY < prevMaxY && posY > maxY)
						maxY = posY;
				}
				
				for(Enemy enemy : groundEnemies) {
					if(enemy.getPos().y == maxY) {
						enemy.draw(batch);
						drawedEnemies++;
					}
				}
				
				for(Enemy enemy : airborneEnemies) {
					if(enemy.getPos().y == maxY) {
						enemy.draw(batch);
						drawedEnemies++;
					}
				}
				
				prevMaxY = maxY;
				maxY = -1000;
			}
		}
		
		for (int i = effects.size - 1; i >= 0; i--) {
	        PooledEffect effect = effects.get(i);
	        effect.draw(batch, dt);
	        if (effect.isComplete()) {
	                effect.free();
	                effects.removeIndex(i);
	        }
		}
		
		for(Bonus bonus : bonuses) {
			bonus.draw(batch);
		}
		
		batch.end();
		debug.render();
		
		batch.setProjectionMatrix(gui.hudCamera.combined);
		batch.begin();
		upgradeGui.render(dt);
		batch.end();
		gui.render(dt);
		pauseMenu.render();
		loseMenu.render();
		winMenu.render();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMultiplexer);
		pauseMenu.hide();
		loseMenu.hide();
		winMenu.hide();
		restartMap();
	}

	@Override
	public void hide() {		
	}

	@Override
	public void resize(int w, int h) {
		pauseMenu.resize(w, h);
		loseMenu.resize(w, h);
		winMenu.resize(w, h);
		
		float ratio = (float)w/(float)h;
		switch(viewportConstraint) {
			case HEIGHT:
				camera.setToOrtho(false, viewportHeight*ratio, viewportHeight);
				gui.hudCamera.setToOrtho(false, viewportHeight*ratio, viewportHeight);
				break;
			case WIDTH:
				camera.setToOrtho(false, viewportWidth, viewportHeight/ratio);
				gui.hudCamera.setToOrtho(false, viewportWidth, viewportHeight/ratio);
				break;
		}
		camera.update();
		gui.hudCamera.update();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public Enemy enemyInRange(Circle range, Targeted targeted) {
		if(targeted != Targeted.AIRBORNE) {
			for(Enemy enemy : groundEnemies) {
				if(Circle.colliding(range, enemy.getHitZone())) return enemy;
			}
		}
		if(targeted != Targeted.GROUND) {
			for(Enemy enemy : airborneEnemies) {
				if(Circle.colliding(range, enemy.getHitZone())) return enemy;
			}
		}
		return null;
	}

	public Field fieldInRange(Circle hitZone, float limit) {
		for(Field field : fields) {
			if(field.getMultiplier() >= limit) return null;
			if(Circle.colliding(hitZone, field.getRange())) return field;
		}
		return null;
	}
	

	public void addEnemy(Enemy enemy, boolean flying) {
		if(flying) airborneEnemies.add(enemy);
		else groundEnemies.add(enemy);
	}

	
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}
	
	public void addField(Field field) {
		fields.add(field);
		fields.sort();
	}
	
	public void removeField(Field field) {
		fields.removeValue(field, true);
	}
	
	public BulletPool getBulletPool() {
		return bulletPool;
	}

	public Array<Enemy> getEnemiesList(Targeted targeted) {
		if(targeted == Targeted.GROUND) return groundEnemies;
		else return airborneEnemies;
	}

	final public Base getBase() {
		return base;
	}

	public void addMoney(int value) {
		money += value;
	}

	public void addBomb() {
		gui.bombSlot.increment();
	}

	public void addMaxUpgrade() {
		gui.upgradeSlot.increment();
	}

	public void detonateBomb(Circle bombBlastZone) {
		for(Enemy enemy : groundEnemies) {
			if(Circle.colliding(bombBlastZone, enemy.getHitZone())) enemy.takeHit(bombDamage);
		}
		for(Enemy enemy : airborneEnemies) {
			if(Circle.colliding(bombBlastZone, enemy.getHitZone())) enemy.takeHit(bombDamage);
		}
	}
	
	public boolean maxUpgradeIsWorking() {
		return maxUpgradeTimer > 0;
	}
	
	public void performMaxUpgrade() {
		maxUpgradeTimer = maxUpgradeTime;
	}

	private int getStars() {
		int stars = 0;
		for(int i=0; i<3; ++i) {
			if(points >= starGoals[i]) stars = i+1;
			else break;
		}
		return stars;
	}
	
	public void addEffect(PooledEffect effect) {
		effects.add(effect);
	}
}
