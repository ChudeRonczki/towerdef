package pl.czyzycki.towerdef.menus;

// Klasa bazowa dla wszystkich ekranów menu

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class MenuBaseScreen implements Screen, InputProcessor {

	private Skin skin = null;
	
	Stage stage;
	TowerDef game;
	
	public MenuBaseScreen(TowerDef game) {
		stage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		float aspect = (float)width/(float)height;
		float adjustedWidth = GameplayScreen.viewportHeight*aspect;
		float adjustedHeight = GameplayScreen.viewportHeight;
		
		stage.setViewport(adjustedWidth, adjustedHeight, true);
		stage.clear();
	}

	@Override
	public void show() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
	
	public Skin getSkin() {
		// HACK: Teraz skin jest tworzony dla kazdego ekranu osobno.
		// Gdy probuje zrobic z tego zmienna statyczna to wtedy tekstury
		// OpenGLowe sie nie chca przeladowac.
		// Zostawiam takie, poniewaz nie wnosi to duzego narzutu.
		// (jest ladowany tylko jeden plik tekstowy)
		if(skin == null) {
			Texture skinTexture = game.getAssetManager().get("layouts/menuskin.png", Texture.class);
			skin = new Skin(Gdx.files.internal("layouts/menuskin.json"), skinTexture);
		}
		return skin;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK){
			backDown();
	    }
		return false;
	}

	public abstract void backDown();

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}