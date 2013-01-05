package pl.czyzycki.towerdef.gameplay;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.entities.AreaTower;
import pl.czyzycki.towerdef.gameplay.entities.BulletTower;
import pl.czyzycki.towerdef.gameplay.entities.PointTower;
import pl.czyzycki.towerdef.gameplay.entities.SlowdownTower;
import pl.czyzycki.towerdef.gameplay.entities.Tower;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

class GameplayGUI {
	
	Circle bombBlastZone;
	public boolean bombDragged;
	public boolean wasPanning;
	
	Sprite pauseSprite;
	Rectangle pauseArea = new Rectangle();
	
	class GameplayGUIGestureListener extends GestureAdapter {

		Vector3 hudCord = new Vector3();
		private boolean bombSelected;
		
		@Override
		public boolean tap(int x, int y, int count) {
			hudCord.set(x, y, 1);
			hudCamera.unproject(hudCord);

			if(pauseArea.contains(hudCord.x, hudCord.y)) {
				// TODO okienko pause
				return true;
			}
			
			for (TowerButton button : towerButtons) {
				if (button.tap(hudCord.x, hudCord.y))
					return true;
			}

			if(bombSlot.tap(hudCord.x, hudCord.y)) {
				bombDragged = false;  // Tapniêcie na bombê ignorujemy
				return true;
			}
			if(upgradeSlot.tap(hudCord.x, hudCord.y)) {
				if(upgradeSlot.count > 0) {
					screen.performMaxUpgrade();
					upgradeSlot.decrement();
				}
				return true;
			}
			
			return false;
		}

		@Override
		public boolean pan(int x, int y, int deltaX, int deltaY) {
			hudCord.set(x, y, 1);
			hudCamera.unproject(hudCord);
			if(!wasPanning) {
				wasPanning = true;
				if(bombSelected) {
					if(bombSlot.count > 0) {
						bombDragged = true;
						bombBlastZone.pos.set(hudCord.x, hudCord.y);
					}
					return true;
				}
				return false;
			} else if(bombDragged) {
				bombBlastZone.pos.set(hudCord.x, hudCord.y);
				return true;
			}
			return false;
		}
		
		@Override
		public boolean touchDown(int x, int y, int pointer) {
			hudCord.set(x, y, 1);
			hudCamera.unproject(hudCord);
			if(bombSlot.tap(hudCord.x, hudCord.y)) {
				bombSelected = true;
				return true;
			}
			else bombSelected = false;
			return false;
		}
	}
	
	GestureDetector detector;
	GameplayGUIGestureListener listener;
	
	float selectedAnimTime = 0.0f;
		
	class TowerButton {
		Tower tower = null;
		
		Sprite sprite;
		String group;
		boolean expanded = false;
		Rectangle area = new Rectangle();
		boolean selected = false;

		float timer = 60.0f;

		Array<TowerButton> subButtons = null;

		void setSelected(boolean selected) {
			if (selected == false) {
				this.selected = selected;

				if (subButtons != null) {
					for (TowerButton button : subButtons) {
						button.setSelected(selected);
					}
				}
			} else {
				this.selected = selected;

				if (subButtons != null && subButtons.size != 0) {
					this.selected = false;

					for (TowerButton button : subButtons) {
						button.setSelected(false);
					}

					subButtons.get(0).setSelected(true);
				} else {
					selectedTowerType = tower;
				}
			}
		}

		float getWidth() {
			if (subButtons != null && subButtons.size != 0) {
				float width = 0.0f;
				for (TowerButton button : subButtons) {
					width += button.getWidth();
				}

				float time = timer;
				if (time > 1)
					time = 1;

				if (expanded) {
					return sprite.getWidth() + (width - sprite.getWidth())
							* time;

				} else {
					return sprite.getWidth() + (width - sprite.getWidth())
							* (1 - time);
				}
			}

			return sprite.getWidth();
		}

		boolean tap(float x, float y) {
			if (expanded && subButtons != null && subButtons.size != 0) {
				for (TowerButton button : subButtons) {
					if (button.tap(x, y)) {
						return true;
					}
				}
			} else {
				if (area.contains(x, y)) {
					for (TowerButton button : towerButtons) {
						button.setSelected(false);
					}

					this.setSelected(true);

					if (subButtons != null) {
						for (TowerButton button : towerButtons) {
							if (button.expanded == true) {
								button.timer = 0;
								button.expanded = false;
							}
						}

						this.expanded = true;
						this.timer = 0;
					}
					
					return true;
				}
			}
			return false;
		}

		void render(float delta) {
			timer += delta * 4.0f;

			if ((expanded || timer < 1) && subButtons != null
					&& subButtons.size != 0) {
				float time = timer;
				if (time > 1)
					time = 1;
				if (expanded == false)
					time = 1 - time;
				float dist = 0.0f;
				for (TowerButton sub : subButtons) {
					sub.setX(area.x + dist * time);
					sub.render(delta);
					dist += sub.getWidth();
				}
				if (timer < 1) {
					dist -= sprite.getWidth();
					float groupAlpha = timer;
					if (expanded)
						groupAlpha = 1 - timer;
					sprite.setPosition(area.x + dist / 2.0f * time, area.y);
					sprite.setColor(1.0f, 1.0f, 1.0f, groupAlpha);
					sprite.draw(screen.batch);
				}
			} else {
				float addY = 0.0f;
				if (this.selected) {
					addY = (float) Math.abs(Math.sin(selectedAnimTime * 10));
				}
				sprite.setPosition(area.x, area.y);
				sprite.setScale(1 + addY * 0.1f);
				sprite.setOrigin(sprite.getWidth() / 2.0f,
						sprite.getHeight() / 2.0f);
				sprite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				sprite.draw(screen.batch);
				sprite.setScale(1);
			}
		}

		void setX(float start) {
			area.x = start;
			area.y = 0;
			area.width = sprite.getWidth();
			area.height = sprite.getHeight();
		}
	}

	class BonusSlot {
		StringBuilder counterString;
		int count;
		Sprite sprite;
		
		public BonusSlot(Sprite sprite) {
			this.sprite = sprite;
			counterString = new StringBuilder(5);
			counterString.append(count);
		}
		
		public void increment() {
			counterString.length = 0;
			counterString.append(++count);
		}
		
		public void decrement() {
			counterString.length = 0;
			counterString.append(--count);
		}
		
		public void reset() {
			counterString.length = 0;
			counterString.append(count = 0);
		}
		
		public void draw(SpriteBatch batch) {
			sprite.draw(batch);
			screen.game.debugFont.draw(batch, counterString, sprite.getX() + 70f, sprite.getY() + 25f);
		}
		
		boolean tap(float x, float y) {
			if((x >= sprite.getX()) && (y >= sprite.getY()) && (x <= sprite.getX() + sprite.getWidth())
					&& (y <= sprite.getY() + sprite.getHeight())) return true;
			else return false;
		}
	}
	
	final GameplayScreen screen;

	Array<Tower> modelTowers;
	Array<TowerButton> towerButtons = new Array<TowerButton>();
	BonusSlot upgradeSlot;
	BonusSlot bombSlot;
	Tower selectedTowerType;
	OrthographicCamera hudCamera;
	StringBuilder moneyText, baseHpText, waveText;
	int moneyTextValue, baseHpTextValue, waveTextValue;
	
	@SuppressWarnings("unchecked")
	GameplayGUI(GameplayScreen screen) {
		this.screen = screen;
		
		listener = new GameplayGUIGestureListener();
		detector = new GestureDetector(listener);
		
		modelTowers = screen.json.fromJson(Array.class, AreaTower.class,
				Gdx.files.internal("config/areaTowers.json"));
		modelTowers.addAll(screen.json.fromJson(Array.class, PointTower.class,
				Gdx.files.internal("config/pointTowers.json")));
		modelTowers.addAll(screen.json.fromJson(Array.class, BulletTower.class,
				Gdx.files.internal("config/bulletTowers.json")));
		modelTowers.add(screen.json.fromJson(SlowdownTower.class,
				Gdx.files.internal("config/slowdownTower.json")));
		
		TextureAtlas texAtlas = TowerDef.getGame().getAssetManager().get("images/objects.pack", TextureAtlas.class);
		
		for(Tower tower: modelTowers) {
			tower.loadSprite(texAtlas);
		}
		
		Sprite buttonSprite = texAtlas.createSprite("bombButton");
		buttonSprite.setPosition(20f, 140f);
		bombSlot = new BonusSlot(buttonSprite);
		buttonSprite = texAtlas.createSprite("maxUpgradeButton");
		buttonSprite.setPosition(20f, 20f);
		upgradeSlot = new BonusSlot(buttonSprite);
		
		pauseSprite = texAtlas.createSprite("pause");
		
		selectedTowerType = modelTowers.get(0);
		
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, GameplayScreen.viewportWidth,
				GameplayScreen.viewportHeight);
		
		moneyText = new StringBuilder(20);
		moneyText.append("KASA: ");
		moneyTextValue = screen.money;
		moneyText.append(moneyTextValue);
		
		waveText = new StringBuilder(15);
		waveText.append("FAL: ");
		waveTextValue = screen.wavesLeft;
		waveText.append(waveTextValue);
		
		baseHpText = new StringBuilder(20);
		baseHpText.append("BAZA: ");
		baseHpText.append(baseHpTextValue);
		baseHpText.append('/');
		baseHpText.append(0);
	}

	void loadTowerIcons(TextureAtlas texAtlas) {
		for (Tower tower : modelTowers) {
			TowerButton group = null;
			
			if (tower.groupIcon != null) {
				for (TowerButton button : towerButtons) {
					if (tower.groupIcon.equals(button.group)) {
						group = button;
						break;
					}
				}
				if (group == null) {
					group = new TowerButton();
					group.subButtons = new Array<TowerButton>();
					group.sprite = texAtlas.createSprite(tower.groupIcon);
					group.group = tower.groupIcon;
					towerButtons.add(group);
				}

			}

			TowerButton button = new TowerButton();
			button.tower = tower;
			button.sprite = texAtlas.createSprite(tower.icon);
			button.group = tower.groupIcon;

			if (group != null) {
				group.subButtons.add(button);
			} else {
				towerButtons.add(button);
			}
		}
	}

	float getButtonsWidth() {
		float width = 0.0f;
		for (TowerButton button : towerButtons) {
			width += button.getWidth();
		}
		return width;
	}

	void render(float dt) {
		if(wasPanning && !detector.isPanning()) wasPanning = false;
		if(bombDragged && !wasPanning) {
			listener.hudCord.set(Gdx.input.getX(), Gdx.input.getY(), 1);
			hudCamera.unproject(listener.hudCord);
			if(!bombSlot.tap(listener.hudCord.x, listener.hudCord.y)) {
				screen.detonateBomb(bombBlastZone);
				bombSlot.decrement();
			}
			bombDragged = false;
		}
		
		screen.batch.setProjectionMatrix(hudCamera.combined);
		screen.shapeRenderer.setProjectionMatrix(hudCamera.combined);

		selectedAnimTime += dt;

		screen.batch.begin();
		float buttonsWidth = getButtonsWidth();
		float start = -buttonsWidth / 2.0f + hudCamera.viewportWidth / 2.0f;
		for (TowerButton button : towerButtons) {
			button.setX(start);
			button.render(dt);
			start += button.getWidth();
		}
		
		bombSlot.draw(screen.batch);
		upgradeSlot.draw(screen.batch);
		
		pauseArea.set(hudCamera.viewportWidth-64, hudCamera.viewportHeight-64, 
						pauseSprite.getWidth(), pauseSprite.getHeight());
		pauseSprite.setPosition(pauseArea.x, pauseArea.y);
		pauseSprite.draw(screen.batch);
		
		if(screen.money != moneyTextValue) {
			moneyText.length = 6;
			moneyTextValue = screen.money;
			moneyText.append(moneyTextValue);
		}
		
		if(screen.wavesLeft != waveTextValue) {
			waveText.length = 5;
			waveTextValue = screen.wavesLeft;
			waveText.append(waveTextValue);
		}
		
		if(screen.base.getHp() != baseHpTextValue) {
			baseHpText.length = 6;
			baseHpTextValue = screen.base.getHp();
			baseHpText.append(baseHpTextValue);
			baseHpText.append('/');
			baseHpText.append(screen.base.getMaxHp());
		}
		
		screen.game.debugFont.setScale(3);
		screen.game.debugFont.draw(screen.batch, moneyText, 5, hudCamera.viewportHeight-5);
		screen.game.debugFont.drawMultiLine(screen.batch, baseHpText, 0, hudCamera.viewportHeight-5,
				hudCamera.viewportWidth, HAlignment.CENTER);
		screen.game.debugFont.drawMultiLine(screen.batch, waveText, 0, screen.game.debugFont.getLineHeight(),
				hudCamera.viewportWidth - 10, HAlignment.RIGHT);
		screen.game.debugFont.setScale(1);
		
		screen.batch.end();
		
		
		if(bombDragged) {
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			screen.shapeRenderer.begin(ShapeType.FilledCircle);
			screen.shapeRenderer.setColor(0f, 1f, 1f, 0.4f);
			bombBlastZone.draw(screen.shapeRenderer);
			screen.shapeRenderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		}

		screen.batch.setProjectionMatrix(hudCamera.projection);
		screen.shapeRenderer.setProjectionMatrix(hudCamera.projection);

		screen.debug.renderGUI();
	}
}
