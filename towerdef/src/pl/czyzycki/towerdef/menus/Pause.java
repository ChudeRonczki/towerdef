package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

public class Pause extends MiniMenu {
	TowerDef game;
	GameplayScreen screen;
	Stage stage;
	Stage areYouSureStage;

	boolean areYouSureShowed = false;
	boolean areYouSureRestarting = false; // czy to areYouSure zosta³o wyœwietlone przez klikniêcie restart czy go to menu? 
	
	public Pause(TowerDef game, GameplayScreen screen) {
		super(game.getAssetManager(), screen);
		this.game = game;
		this.screen = screen;
		stage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
		areYouSureStage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
	}
	
	protected Stage currentStage() {
		if(areYouSureShowed) {
			return areYouSureStage;
		} else {
			return stage;
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
						hide();
					} else
						game.setScreen(game.getMainMenuScreen());
				}
			} );
			layout.register("yesButton", yesButton);
		
			layout.parse(Gdx.files.internal( "layouts/sure-pause-menu.txt" ).readString("UTF-8"));
		}
	}
	
	public void dispose() {
		stage.dispose();
		areYouSureStage.dispose();
		super.dispose();
	}

	public void show() {
		super.show();
		areYouSureShowed = false;
	}
	
	public void hide() {
		super.hide();
		areYouSureShowed = false;
	}
}
