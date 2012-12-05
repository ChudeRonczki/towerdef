package pl.czyzycki.towerdef.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * Klasa odpowiedzialna za obs³ugê typowych gestów dotykowych.
 * @author Ciziu
 *
 */
class GameplayGestureListener extends GestureAdapter {
	
	final GameplayScreen screen;
	
	float minZoom, maxZoom;
	float zoomStartDistance, zoomStartValue;
	
	GameplayGestureListener(GameplayScreen screen) {
		super();
		this.screen = screen;
		@SuppressWarnings("unchecked")
		OrderedMap<String, Object> jsonData = (OrderedMap<String, Object>)new JsonReader().parse(Gdx.files.internal("config/camera.json"));
		minZoom = (Float)jsonData.get("minZoom");
		maxZoom = (Float)jsonData.get("maxZoom");
	}
	
	@Override
	public boolean tap (float x, float y, int count, int button) {
		if(screen.gui.tap(x, y)) return true;
		
		Vector3 worldCord = new Vector3(x,y,0f);
		screen.camera.unproject(worldCord);
		screen.addTower(worldCord.x, worldCord.y);
		return true;
	}

	@Override
	public boolean pan (float x, float y, float deltaX, float deltaY) {
		float ratio = screen.camera.viewportWidth/Gdx.graphics.getWidth();
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			ratio *= screen.camera.zoom;
			screen.camera.position.x -= deltaX*ratio;
			screen.camera.position.y += deltaY*ratio;
		} else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			screen.camera.zoom += deltaY*ratio*0.001f;
			if(screen.camera.zoom < maxZoom) screen.camera.zoom = maxZoom;
			else if(screen.camera.zoom > minZoom) screen.camera.zoom = minZoom;
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
		if(screen.camera.zoom < maxZoom) screen.camera.zoom = maxZoom;
		else if(screen.camera.zoom > minZoom) screen.camera.zoom = minZoom;
		screen.camera.update();
		return true;
	}
}