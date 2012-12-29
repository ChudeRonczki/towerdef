package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

public class InstructionsScreen extends MenuBaseScreen {
	
	public InstructionsScreen(TowerDef game) {
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
		
		FlickScrollPane pane = new FlickScrollPane();
		layout.register("scrollPane", pane);
		
		TextButton backButton = new TextButton("OK", buttonsSkin);
		backButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
                game.setScreen(game.getMainMenuScreen());
            }
        } );
		layout.register("backButton", backButton);
		
		layout.parse(Gdx.files.internal( "layouts/instructions.txt" ).readString());
	}
}
