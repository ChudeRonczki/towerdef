package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;

// Menu które jest rysowane na gameplayu, np. Pause, LevelSummary
public abstract class MiniMenu extends Skinable implements InputProcessor {
	
	GameplayScreen screen;
	boolean showed = false;
	
	protected abstract Stage currentStage();
	
	MiniMenu(AssetManager assetManager, GameplayScreen screen) {
		super(assetManager);
		this.screen = screen;
	}
	public boolean isShowed() {
		return showed;
	}
	
	public void render() {
		if(isShowed()) {
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			screen.shapeRenderer.begin(ShapeType.FilledRectangle);
			screen.shapeRenderer.setColor(0f, 0f, 0f, 0.8f);
			screen.shapeRenderer.filledRect(-1000, -1000, 2000, 2000);
			screen.shapeRenderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
			
			currentStage().act(Gdx.graphics.getDeltaTime());
			currentStage().draw();
		}
	}
	
	public void show() {
		showed = true;
	}
	
	public void hide() {
		showed = false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(!isShowed()) return false;
		return currentStage().keyDown(keycode) || isShowed();
	}

	@Override
	public boolean keyUp(int keycode) {
		if(!isShowed()) return false;
		return currentStage().keyUp(keycode) || isShowed();
	}

	@Override
	public boolean keyTyped(char character) {
		if(!isShowed()) return false;
		return currentStage().keyTyped(character) || isShowed();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(!isShowed()) return false;
		return currentStage().touchDown(x, y, pointer, button) || isShowed();
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(!isShowed()) return false;
		return currentStage().touchUp(x, y, pointer, button) || isShowed();
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if(!isShowed()) return false;
		return currentStage().touchDragged(x, y, pointer) || isShowed();
	}

	@Override
	public boolean touchMoved(int x, int y) {
		if(!isShowed()) return false;
		return currentStage().touchMoved(x, y)  || isShowed();
	}

	@Override
	public boolean scrolled(int amount) {
		if(!isShowed()) return false;
		return currentStage().scrolled(amount) || isShowed();
	}
}
