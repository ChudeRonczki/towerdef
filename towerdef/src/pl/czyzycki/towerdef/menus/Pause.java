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
	Stage areYouSureStage;
	Skin skin;
	boolean showed = false;
	boolean areYouSureShowed = false;
	boolean areYouSureRestarting = false; // czy to areYouSure zosta³o wyœwietlone przez klikniêcie restart czy go to menu? 
	
	public Pause(TowerDef game, GameplayScreen screen) {
		this.game = game;
		this.screen = screen;
		stage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
		areYouSureStage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
	}
	
	private Stage currentStage() {
		if(areYouSureShowed) {
			return areYouSureStage;
		} else {
			return stage;
		}
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
			
			currentStage().act(Gdx.graphics.getDeltaTime());
			currentStage().draw();
		}
	}
	
	public void resize(int width, int height) {
		float aspect = (float)width/(float)height;
		float adjustedWidth = GameplayScreen.viewportHeight*aspect;
		float adjustedHeight = GameplayScreen.viewportHeight;
		
		// G³ówny stage menu pauzy.
		{
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
					areYouSureRestarting = true;
					areYouSureShowed = true;
				}
			} );
			layout.register("restartButton", restartButton);
		
			TextButton exitButton = new TextButton("Wyjœcie do menu", skin);
			exitButton.setClickListener( new ClickListener() {
				@Override
				public void click(Actor actor, float x, float y )
				{
					areYouSureRestarting = false;
					areYouSureShowed = true;
				}
			} );
			layout.register("exitButton", exitButton);
		
			layout.parse(Gdx.files.internal( "layouts/pause-menu.txt" ).readString("UTF-8"));
		}
		
		// "Are you sure" stage.
		{
			areYouSureStage.setViewport(adjustedWidth, adjustedHeight, true);
			areYouSureStage.clear();
		
			Skin skin = getSkin();
		
			Table table = new Table(skin);
			table.width = areYouSureStage.width();
			table.height = areYouSureStage.height();
		
			areYouSureStage.addActor(table);
		
			TableLayout layout = table.getTableLayout();
		
			TextButton noButton = new TextButton("Nie", skin);
			noButton.setClickListener( new ClickListener() {
				@Override
				public void click(Actor actor, float x, float y )
				{
					areYouSureShowed = false;
				}
			} );
			layout.register("noButton", noButton);
		
			TextButton yesButton = new TextButton("Tak", skin);
			yesButton.setClickListener( new ClickListener() {
				@Override
				public void click(Actor actor, float x, float y )
				{
					areYouSureShowed = false;
					if(areYouSureRestarting) {
						screen.restartMap();
						showed = false;
					} else
						game.setScreen(game.getMainMenuScreen());
				}
			} );
			layout.register("yesButton", yesButton);
		
			layout.parse(Gdx.files.internal( "layouts/sure-pause-menu.txt" ).readString("UTF-8"));
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if(!showed) return false;
		return currentStage().keyDown(keycode) || showed;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(!showed) return false;
		return currentStage().keyUp(keycode) || showed;
	}

	@Override
	public boolean keyTyped(char character) {
		if(!showed) return false;
		return currentStage().keyTyped(character) || showed;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(!showed) return false;
		return currentStage().touchDown(x, y, pointer, button) || showed;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(!showed) return false;
		return currentStage().touchUp(x, y, pointer, button) || showed;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if(!showed) return false;
		return currentStage().touchDragged(x, y, pointer) || showed;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		if(!showed) return false;
		return currentStage().touchMoved(x, y)  || showed;
	}

	@Override
	public boolean scrolled(int amount) {
		if(!showed) return false;
		return currentStage().scrolled(amount) || showed;
	}

	public void show() {
		showed = true;
		areYouSureShowed = false;
	}
	
	public void hide() {
		showed = false;
		areYouSureShowed = false;
	}
	
	public boolean isShowed() {
		return showed;
	}

}
