package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

public class MainMenu extends MenuBase {
	
	public MainMenu() {
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		Skin buttonsSkin = getButtonsSkin();
		
		Table table = new Table();
		table.width = stage.width();
		table.height = stage.height();
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		
		TextButton startButton = new TextButton("Graj", buttonsSkin);
		startButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	// todo: poprawic to ponizej
            	TowerDef.getGame().setScreen(TowerDef.getGame().getGameplayScreen());
            }
        } );
		layout.register("startButton", startButton);
		
		TextButton instructionsButton = new TextButton("Instrukcje", buttonsSkin);
		layout.register("instructionsButton", instructionsButton);
		
		TextButton optionsButton = new TextButton("Opcje", buttonsSkin);
		layout.register("optionsButton", optionsButton );
		
		TextButton exitButton = new TextButton("Wyjscie", buttonsSkin);
		exitButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
                Gdx.app.exit();
            }
        } );
		layout.register("exitButton", exitButton);
		
		layout.parse(Gdx.files.internal( "layouts/main-menu.txt" ).readString());
	}
}
