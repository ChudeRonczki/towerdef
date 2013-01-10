package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.TowerDef.GameSound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

/**
 * Menu g³ówne.
 *
 */
public class MainMenuScreen extends MenuBaseScreen {
	
	public MainMenuScreen(TowerDef game) {
		super(game, true);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		Skin skin = getSkin();
		
		Table table = new Table(skin);
		table.width = stage.width();
		table.height = stage.height();
		
		table.align(Align.LEFT);
		table.x = 50;
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		
		TextButton startButton = new TextButton("Graj", skin);
		startButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.playSound(GameSound.CLICK);
            	game.setScreen(game.getSelectLevelScreen());
            }
        } );
		layout.register("startButton", startButton);
		
		TextButton instructionsButton = new TextButton("Instrukcje", skin);
		instructionsButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.playSound(GameSound.CLICK);
            	game.setScreen(game.getInstructionsScreen());
            }
        } );
		layout.register("instructionsButton", instructionsButton);
		
		TextButton optionsButton = new TextButton("Opcje", skin);
		optionsButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.playSound(GameSound.CLICK);
            	game.setScreen(game.getOptionsButton());
            }
        } );
		layout.register("optionsButton", optionsButton );
		
		TextButton exitButton = new TextButton("Wyjœcie", skin);
		exitButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.playSound(GameSound.CLICK);
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
