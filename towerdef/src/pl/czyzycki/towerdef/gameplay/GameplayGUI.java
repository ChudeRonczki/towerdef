package pl.czyzycki.towerdef.gameplay;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.entities.AreaTower;
import pl.czyzycki.towerdef.gameplay.entities.BulletTower;
import pl.czyzycki.towerdef.gameplay.entities.PointTower;
import pl.czyzycki.towerdef.gameplay.entities.SlowdownTower;
import pl.czyzycki.towerdef.gameplay.entities.Tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

class GameplayGUI {
	
	class GameplayGUIGestureListener extends GestureAdapter {

		@Override
		public boolean tap(int x, int y, int count) {
			Vector3 hudCord = new Vector3(x, y, 1);
			hudCamera.unproject(hudCord);

			for (TowerButton button : towerButtons) {
				if (button.tap(hudCord.x, hudCord.y))
					return true;
			}

			return false;
		}
		
	}
	
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

	final GameplayScreen screen;

	Array<Tower> modelTowers;
	Array<TowerButton> towerButtons = new Array<TowerButton>();
	Tower selectedTowerType;
	OrthographicCamera hudCamera;

	@SuppressWarnings("unchecked")
	GameplayGUI(GameplayScreen screen) {
		this.screen = screen;
		
		listener = new GameplayGUIGestureListener();
		
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
		
		selectedTowerType = modelTowers.get(0);
		
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, GameplayScreen.viewportWidth,
				GameplayScreen.viewportHeight);
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
		
		screen.game.debugFont.setScale(3);
		screen.game.debugFont.draw(screen.batch, "KASA: "+screen.money, 5, hudCamera.viewportHeight-5);
		screen.game.debugFont.setScale(1);
		
		screen.batch.end();

		screen.batch.setProjectionMatrix(hudCamera.projection);
		screen.shapeRenderer.setProjectionMatrix(hudCamera.projection);

		screen.debug.renderGUI();
	}
}
