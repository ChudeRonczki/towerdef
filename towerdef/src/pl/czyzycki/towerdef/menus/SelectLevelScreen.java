package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import sun.misc.GC.LatencyRequest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.esotericsoftware.tablelayout.Cell;

public class SelectLevelScreen extends MenuBaseScreen {
	
	public SelectLevelScreen(TowerDef game) {
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
		layout.parse("* spacing:5");
		
		
		Label caption = new Label("Wybierz poziom", skin);

		Cell<Actor> captionCell = layout.add(caption);
		captionCell.align("center");
		captionCell.colspan(4);
		captionCell.spaceBottom(100);

		
		layout.row();
		for(int i=0; i<5; i++)
		{
			TextButton level1 = new TextButton("Poziom 1", skin);
			level1.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
                game.setScreen(game.getGameplayScreen());
            	}
        	} );
			level1.width(195);
			layout.add(level1);
		
			for(int j=0; j<3; j++)
			{
				TextButton gw = new TextButton("<3", skin);
				gw.width(30);
				layout.add(gw);
			}
		
			layout.row();
		}
		
		
		TextButton exitButton = new TextButton("Wstecz", skin);
		exitButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
                game.setScreen(game.getMainMenuScreen());
            }
        } );
		exitButton.width(300);
		Cell<Actor> backCell = layout.add(exitButton);
		backCell.colspan(4);
		backCell.spaceTop(100);
	}

	@Override
	public void backDown() {
		Gdx.app.exit();
	}
}
