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
		
		Skin buttonsSkin = getButtonsSkin();
		
		Table table = new Table(buttonsSkin);
		table.width = stage.width();
		table.height = stage.height();
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		
		CheckBox vibratorButton = new CheckBox(buttonsSkin);
		layout.register("vibratorCheckBox", vibratorButton);
		
		CheckBox musicButton = new CheckBox(buttonsSkin);
		layout.register("musicCheckBox", musicButton);
		
		CheckBox particleButton = new CheckBox(buttonsSkin);
		layout.register("particleCheckBox", particleButton );
		
		TextButton backButton = new TextButton("OK", buttonsSkin);
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
}
