package pl.czyzycki.towerdef.menus;

// Klasa bazowa dla wszystkich ekranów menu

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MenuBaseScreen implements Screen {

	static Skin buttonsSkin = new Skin(	Gdx.files.internal("layouts/buttons.json"),
										Gdx.files.internal("layouts/menuskin.png"));;
	
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
		Gdx.input.setInputProcessor(stage);
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
	}
	
	public static Skin getButtonsSkin() {
		return buttonsSkin;
	}
}