package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

public class Pause implements InputProcessor {
	TowerDef game;
	GameplayScreen screen;
	Stage stage;
	Skin skin;
	boolean showed = false;
	
	public Pause(TowerDef game, GameplayScreen screen) {
		this.game = game;
		this.screen = screen;
		stage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
	}
	
	// Rafal: ta sama funkcja jest w MenuBaseScreen, ale nie chce tego na razie wiazac ze soba
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
	
	public void render() {
		if(showed) {
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			screen.shapeRenderer.begin(ShapeType.FilledRectangle);
			screen.shapeRenderer.setColor(0f, 0f, 0f, 0.8f);
			screen.shapeRenderer.filledRect(-1000, -1000, 2000, 2000);
			screen.shapeRenderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
			
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
		}
	}
	
	public void resize(int width, int height) {
		float aspect = (float)width/(float)height;
		float adjustedWidth = GameplayScreen.viewportHeight*aspect;
		float adjustedHeight = GameplayScreen.viewportHeight;
		
		stage.setViewport(adjustedWidth, adjustedHeight, true);
		stage.clear();
		
		Skin skin = getSkin();
		
		Table table = new Table(skin);
		table.width = stage.width();
		table.height = stage.height();
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		
		TextButton resumeButton = new TextButton("Wróæ do gry", skin);
		resumeButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	showed = false;
            }
        } );
		layout.register("resumeButton", resumeButton);
		
		TextButton restartButton = new TextButton("Restart poziomu", skin);
		restartButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	screen.restartMap();
            	showed = false;
            }
        } );
		layout.register("restartButton", restartButton);
		
		TextButton exitButton = new TextButton("Wyjœcie do menu", skin);
		exitButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.setScreen(game.getMainMenuScreen());
            }
        } );
		layout.register("exitButton", exitButton);
		
		layout.parse(Gdx.files.internal( "layouts/pause-menu.txt" ).readString("UTF-8"));
	}

	@Override
	public boolean keyDown(int keycode) {
		if(!showed) return false;
		return stage.keyDown(keycode) || showed;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(!showed) return false;
		return stage.keyUp(keycode) || showed;
	}

	@Override
	public boolean keyTyped(char character) {
		if(!showed) return false;
		return stage.keyTyped(character) || showed;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(!showed) return false;
		return stage.touchDown(x, y, pointer, button) || showed;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(!showed) return false;
		return stage.touchUp(x, y, pointer, button) || showed;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if(!showed) return false;
		return stage.touchDragged(x, y, pointer) || showed;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		if(!showed) return false;
		return stage.touchMoved(x, y)  || showed;
	}

	@Override
	public boolean scrolled(int amount) {
		if(!showed) return false;
		return stage.scrolled(amount) || showed;
	}

	public void show() {
		showed = true;
	}
	
	public void hide() {
		showed = false;
	}
	
	public boolean isShowed() {
		return showed;
	}

}
