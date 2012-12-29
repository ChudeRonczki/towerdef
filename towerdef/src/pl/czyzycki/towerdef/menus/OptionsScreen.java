package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

public class OptionsScreen extends MenuBaseScreen {
	
	public OptionsScreen(TowerDef game) {
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
		
		CheckBox vibratorButton = new CheckBox(skin);
		layout.register("vibratorCheckBox", vibratorButton);
		
		CheckBox musicButton = new CheckBox(skin);
		layout.register("musicCheckBox", musicButton);
		
		CheckBox particleButton = new CheckBox(skin);
		layout.register("particleCheckBox", particleButton );
		
		TextButton backButton = new TextButton("OK", skin);
		backButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
                game.setScreen(game.getMainMenuScreen());
            }
        } );
		layout.register("backButton", backButton);
		
		layout.parse(Gdx.files.internal( "layouts/options.txt" ).readString());
	}

	@Override
	public void backDown() {
		game.setScreen(game.getMainMenuScreen());
	}
}
