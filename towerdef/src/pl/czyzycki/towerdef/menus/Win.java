package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.gameplay.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.esotericsoftware.tablelayout.Cell;

public class Win extends MiniMenu {
	Stage stage;
	TowerDef game;
	
	int stars;
	int points;
	
	float adjustedWidth;
	float adjustedHeight;
	
	public Win(TowerDef game, GameplayScreen screen) {
		super(game.getAssetManager(), screen);
		this.game = game;
		stage = new Stage(GameplayScreen.viewportWidth, GameplayScreen.viewportHeight, true);
	}

	@Override
	protected Stage currentStage() {
		return stage;
	}

	public void show(int points, int stars) {
		super.show();
		this.points = points;
		this.stars = stars;
		Preferences prefs = Gdx.app.getPreferences("stars");
		String key = "level-"+((SelectLevelScreen)game.getSelectLevelScreen()).getSelectedLevel();
		if(stars > prefs.getInteger(key,0)) prefs.putInteger(key, stars);
		prefs.flush();
		buildScreen();
	}
	
	private void buildScreen() {
		stage.setViewport(adjustedWidth, adjustedHeight, true);
		stage.clear();
		
		Skin skin = getSkin();
		
		Table table = new Table(skin);
		table.width = stage.width();
		table.height = stage.height();
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		layout.parse("* spacing:5");
		
		LabelStyle whiteLabelStyle = skin.getStyle("white", LabelStyle.class);
		
		Label caption = new Label("Gratulacje, wygra³eœ!", whiteLabelStyle);

		Cell<Actor> captionCell = layout.add(caption);
		captionCell.align("center");
		
		layout.row();

		Label earnedPoints = new Label("Zdoby³eœ "+points+" punktów.", whiteLabelStyle);

		Cell<Actor> pointsCell = layout.add(earnedPoints);
		pointsCell.align("center");
		pointsCell.spaceBottom(50);
		
		layout.row();
		
		Table starsTable = new Table(skin);
		starsTable.width = 128*3;
		starsTable.height = 128;
		TableLayout starsLayout = starsTable.getTableLayout();
		
		for(int j=0; j<3; j++)
		{
			Image img;
			if(j<stars)
				img = new Image(game.getAssetManager().get("layouts/star-big.png", Texture.class));
			else
				img = new Image(game.getAssetManager().get("layouts/starslot-big.png", Texture.class));
			
			starsLayout.add(img);
		}
		
		layout.add(starsTable);
		
		layout.row();
		
		Cell<Actor> nullCell = layout.add(null);
		nullCell.height(100);
		
		layout.row();
		
		Table buttonsTable = new Table(skin);
		buttonsTable.width = 500;
		buttonsTable.height = 100;
		TableLayout buttonsLayout = buttonsTable.getTableLayout();
		buttonsLayout.parse("* spacing:5");
		
		TextButton exitButton = new TextButton("Menu", skin);
		exitButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
                game.setScreen(game.getMainMenuScreen());
            }
        } );
		exitButton.width(200);
		buttonsLayout.add(exitButton);
		
		TextButton restartButton = new TextButton("Restartuj", skin);
		restartButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	screen.restartMap();
            	hide();
            }
        } );
		restartButton.width(200);
		buttonsLayout.add(restartButton);
		
		TextButton nextButton = new TextButton("Dalej", skin);
		nextButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	// TODO przejscie do nastêpnego poziomu
            	screen.restartMap();
            	hide();
            }
        } );
		nextButton.width(200);
		buttonsLayout.add(nextButton);
		
		layout.add(buttonsTable);
	}
	
	public void resize(int width, int height) {
		float aspect = (float)width/(float)height;
		adjustedWidth = 480*aspect;
		adjustedHeight = 480;
	}
	
	public void dispose() {
		stage.dispose();
		super.dispose();
	}
}