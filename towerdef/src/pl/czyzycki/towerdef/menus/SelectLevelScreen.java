package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import sun.misc.GC.LatencyRequest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.esotericsoftware.tablelayout.Cell;

public class SelectLevelScreen extends MenuBaseScreen {
	class LevelInfo {
		int stars;
	}
	
	public LevelInfo []levelInfos = null;
	
	private int selectedLevel = 0;
	
	public SelectLevelScreen(TowerDef game) {
		super(game);
	}
	
	void setLevelInfos() {
		// TODO wlasciwe poziomy i gwiazdki
		levelInfos = new LevelInfo[5];
		for(int i=0; i<5; i++)
			levelInfos[i] = new LevelInfo();
		levelInfos[0].stars = 1;
		levelInfos[1].stars = 3;
		levelInfos[2].stars = 0;
		levelInfos[3].stars = 2;
		levelInfos[4].stars = 1;
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		setLevelInfos();
		
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
		captionCell.colspan(5);
		captionCell.spaceBottom(100);

		
		layout.row();
		for(int i=0; levelInfos!=null && i<levelInfos.length; i++) {
			LevelInfo info = levelInfos[i];
			
			TextButton level1 = new TextButton("Poziom "+(i+1), skin);
			class LevelClickListener implements ClickListener {
				private int levelId = 0;
				public LevelClickListener(int level) {
					levelId = level;
				}
				@Override
				public void click(Actor actor, float x, float y) {
					selectedLevel = levelId;
					game.setScreen(game.getGameplayScreen());
				}
			}
			
			level1.setClickListener(new LevelClickListener(i));
			level1.width(195);
			layout.add(level1);
			
			Cell<Actor> nullCell = layout.add(null);
			nullCell.width(40);
		
			for(int j=0; j<3; j++)
			{
				Image img;
				if(j<info.stars)
					img = new Image(game.getAssetManager().get("layouts/star.png", Texture.class));
				else
					img = new Image(game.getAssetManager().get("layouts/starslot.png", Texture.class));
				layout.add(img);
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
		backCell.colspan(5);
		backCell.spaceTop(100);
	}

	@Override
	public void backDown() {
		Gdx.app.exit();
	}
	
	// Pierwszy level=0, drugi=1, ...
	public int getSelectedLevel() {
		return selectedLevel;
	}
}
