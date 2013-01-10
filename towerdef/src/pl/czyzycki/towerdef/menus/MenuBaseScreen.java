package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;

/*
 * Klasa bazowa dla wszystkich ekranów menu.
 * 
 */
public abstract class MenuBaseScreen extends Skinable implements Screen, InputProcessor {
	
	Stage stage;
	TowerDef game;
	Sprite backgroundSprite;
	OrthographicCamera hudCamera;
	
	public MenuBaseScreen(TowerDef game, boolean isMenu) {
		super(game.getAssetManager());
		stage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
		this.game = game;
		hudCamera = new OrthographicCamera();
		if(isMenu)
			backgroundSprite = new Sprite(game.getAssetManager().get("layouts/menu-bg.png", Texture.class));
		else
			backgroundSprite = new Sprite(game.getAssetManager().get("layouts/menu-bg2.png", Texture.class));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	
		hudCamera.setToOrtho(false, GameplayScreen.viewportWidth,
				GameplayScreen.viewportHeight);
		
		game.getGameplayScreen().batch.setProjectionMatrix(hudCamera.combined);
		game.getGameplayScreen().batch.begin();
		backgroundSprite.setPosition(0, GameplayScreen.viewportHeight - backgroundSprite.getHeight());
		backgroundSprite.draw(game.getGameplayScreen().batch);
		game.getGameplayScreen().batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		float aspect = (float)width/(float)height;
		float adjustedWidth = 480*aspect;
		float adjustedHeight = 480;
		
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
		super.dispose();
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
	public boolean touchMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}