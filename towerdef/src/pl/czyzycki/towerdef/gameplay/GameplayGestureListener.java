package pl.czyzycki.towerdef.gameplay;

import java.util.Iterator;

import pl.czyzycki.towerdef.TowerDef.GameSound;
import pl.czyzycki.towerdef.gameplay.entities.Bonus;
import pl.czyzycki.towerdef.gameplay.entities.Tower;
import pl.czyzycki.towerdef.menus.OptionsScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector3;

/**
 * Klasa odpowiedzialna za obs³ugê typowych gestów dotykowych.
 * @author Ciziu
 *
 */
class GameplayGestureListener extends GestureAdapter {
	
	final GameplayScreen screen;
	
	float zoomStartDistance, zoomStartValue;
	Vector3 worldCord = new Vector3();
	
	GameplayGestureListener(GameplayScreen screen) {
		super();
		this.screen = screen;
	}
	
	@Override
	public boolean tap (int x, int y, int count) {
		worldCord.set(x,y,0f);
		screen.camera.unproject(worldCord);
		Tower selectedTower = screen.getTower(worldCord.x, worldCord.y);
		if(selectedTower != null) {
			screen.upgradeGui.setSelectedTower(selectedTower);
		} else {
			screen.upgradeGui.hide();
			boolean bonusTapped = false;
			Iterator<Bonus> bonusIter = screen.bonuses.iterator();
			while(bonusIter.hasNext()) {
				Bonus bonus = bonusIter.next();
				if(bonus.getZone().contains(worldCord.x, worldCord.y)) {
					bonusTapped = true;
					bonus.onCollected();
					bonusIter.remove();
					if(OptionsScreen.vibrationEnabled()) Gdx.input.vibrate(50);
					screen.game.playSound(GameSound.BONUS);
					break;
				}
			}
			if(!bonusTapped) screen.addTower(worldCord.x, worldCord.y);
		}
		return true;
	}

	@Override
	public boolean pan (int x, int y, int deltaX, int deltaY) {
		float ratio = screen.camera.viewportWidth/Gdx.graphics.getWidth();
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			ratio *= screen.camera.zoom;
			screen.camera.position.x -= deltaX*ratio;
			screen.camera.position.y += deltaY*ratio;
		} else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			screen.camera.zoom += deltaY*ratio*0.001f;
		}
		screen.camera.update();
		return true;
	}

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		if(originalDistance != zoomStartDistance) {
			zoomStartDistance = originalDistance;
			zoomStartValue = screen.camera.zoom;
		}
		screen.camera.zoom = zoomStartValue*(zoomStartDistance/currentDistance);
		screen.camera.update();
		return true;
	}
}