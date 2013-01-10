package pl.czyzycki.towerdef.gameplay;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Klasa odpowiedzialna za obs³ugê prostych zdarzeñ inputu. Propaguje informacje do GameplayGestureListenera.
 *
 */
class GameplayGestureDetector extends GestureDetector {
	
	final GameplayScreen screen;
	
	GameplayGestureDetector(GameplayScreen screen) {
		super(20, 0.4f, 1.5f, 0.15f, new GameplayGestureListener(screen));
		this.screen = screen;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.Q) screen.debug.base = !screen.debug.base;
		else if(keycode == Keys.W) screen.debug.spawns = !screen.debug.spawns;
		else if(keycode == Keys.E) screen.debug.enemies = !screen.debug.enemies;
		else if(keycode == Keys.R) screen.debug.towers = !screen.debug.towers;
		else if(keycode == Keys.T) screen.debug.bullets = !screen.debug.bullets;
		else if(keycode == Keys.Y) screen.debug.camera = !screen.debug.camera;
		else if(keycode >= Keys.NUM_1 && keycode <= Keys.NUM_9) {
			screen.gui.selectedTowerType = screen.gui.modelTowers.get(keycode - Keys.NUM_1);
		} else if(keycode == Keys.NUM_0) {
			screen.gui.selectedTowerType = screen.gui.modelTowers.get(9);
		}
		return super.keyDown(keycode);
	}
	
}

