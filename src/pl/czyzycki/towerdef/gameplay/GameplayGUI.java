package pl.czyzycki.towerdef.gameplay;

import pl.czyzycki.towerdef.gameplay.entities.Tower;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;

class GameplayGUI {

	final GameplayScreen screen;
	
	Array<Tower> modelTowers;
	Tower selectedTowerType;
	OrthographicCamera hudCamera;
	
	GameplayGUI(GameplayScreen screen) {
		this.screen = screen;
	}
	
	void render(float dt) {
		screen.batch.setProjectionMatrix(hudCamera.projection);
		screen.shapeRenderer.setProjectionMatrix(hudCamera.projection);
		screen.debug.renderGUI();
	}
}
