package pl.czyzycki.towerdef.gameplay;

import pl.czyzycki.towerdef.gameplay.entities.AreaTower;
import pl.czyzycki.towerdef.gameplay.entities.BulletTower;
import pl.czyzycki.towerdef.gameplay.entities.PointTower;
import pl.czyzycki.towerdef.gameplay.entities.SlowdownTower;
import pl.czyzycki.towerdef.gameplay.entities.Tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;

class GameplayGUI {

	final GameplayScreen screen;
	
	Array<Tower> modelTowers;
	Tower selectedTowerType;
	OrthographicCamera hudCamera;
	
	@SuppressWarnings("unchecked")
	GameplayGUI(GameplayScreen screen) {
		this.screen = screen;
		modelTowers = screen.json.fromJson(Array.class, AreaTower.class, Gdx.files.internal("config/areaTowers.json"));
		modelTowers.addAll(screen.json.fromJson(Array.class, PointTower.class, Gdx.files.internal("config/pointTowers.json")));
		modelTowers.addAll(screen.json.fromJson(Array.class, BulletTower.class, Gdx.files.internal("config/bulletTowers.json")));
		modelTowers.add(screen.json.fromJson(SlowdownTower.class, Gdx.files.internal("config/slowdownTower.json")));
		selectedTowerType = modelTowers.get(0);
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, GameplayScreen.viewportWidth, GameplayScreen.viewportHeight);
	}
	
	void render(float dt) {
		screen.batch.setProjectionMatrix(hudCamera.projection);
		screen.shapeRenderer.setProjectionMatrix(hudCamera.projection);
		screen.debug.renderGUI();
	}
}
