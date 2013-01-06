package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

public class Lose extends MiniMenu {
	Stage stage;
	TowerDef game;
	
	public Lose(TowerDef game, GameplayScreen screen) {
		super(game.getAssetManager(), screen);
		this.game = game;
		stage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
	}

	@Override
	protected Stage currentStage() {
		return stage;
	}
	
	public void resize(int width, int height) {
		float aspect = (float)width/(float)height;
		float adjustedWidth = 480*aspect;
		float adjustedHeight = 480;
		
		stage.setViewport(adjustedWidth, adjustedHeight, true);
		stage.clear();
		
		Skin skin = getSkin();
		
		Table table = new Table(skin);
		table.width = stage.width();
		table.height = stage.height();
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		
		Label caption = new Label("Przegra³eœ - nie za³amuj siê!", skin);
		LabelStyle st = skin.getStyle("default", LabelStyle.class);
		caption.setStyle(st);
		layout.register("windowCaption", caption);
		
		TextButton restartButton = new TextButton("Restart poziomu", skin);
		restartButton.setClickListener( new ClickListener() {
			@Override
			public void click(Actor actor, float x, float y )
			{
				screen.restartMap();
				hide();
			}
		} );
		layout.register("restartButton", restartButton);
		
		TextButton exitButton = new TextButton("Wyjœcie do menu", skin);
		exitButton.setClickListener( new ClickListener() {
			@Override
			public void click(Actor actor, float x, float y )
			{
				game.setScreen(game.getMainMenuScreen());
				hide();
			}
		} );
		layout.register("exitButton", exitButton);
		
		layout.parse(Gdx.files.internal( "layouts/lose-menu.txt" ).readString("UTF-8"));
	}
	
	public void dispose() {
		stage.dispose();
		super.dispose();
	}
}
