package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

public class MainMenuScreen extends MenuBaseScreen {
	
	public MainMenuScreen(TowerDef game) {
		super(game);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		Skin skin = getSkin();
		
		Table table = new Table(skin);
		table.width = stage.width();
		table.height = stage.height();
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		
		TextButton startButton = new TextButton("Graj", skin);
		startButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.setScreen(game.getSelectLevelScreen());
            }
        } );
		layout.register("startButton", startButton);
		
		TextButton instructionsButton = new TextButton("Instrukcje", skin);
		instructionsButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.setScreen(game.getInstructionsScreen());
            }
        } );
		layout.register("instructionsButton", instructionsButton);
		
		TextButton optionsButton = new TextButton("Opcje", skin);
		optionsButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.setScreen(game.getOptionsButton());
            }
        } );
		layout.register("optionsButton", optionsButton );
		
		TextButton exitButton = new TextButton("Wyjœcie", skin);
		exitButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
                Gdx.app.exit();
            }
        } );
		layout.register("exitButton", exitButton);
		
		layout.parse(Gdx.files.internal( "layouts/main-menu.txt" ).readString("UTF-8"));
	}

	@Override
	public void backDown() {
		Gdx.app.exit();
	}
}
