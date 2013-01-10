package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.TowerDef.GameSound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

/**
 * Ekran opcji gry.
 *
 */
public class OptionsScreen extends MenuBaseScreen {
	private static boolean vibration	= true;
	private static boolean music		= true;
	private static boolean particle	= true;
	
	public static boolean vibrationEnabled() {
		return vibration;
	}
	
	public static boolean musicEnabled() {
		return music;
	}
	
	public static boolean particleEnabled() {
		return particle;
	}
	
	public OptionsScreen(TowerDef game) {
		super(game, false);
		
		Preferences prefs = Gdx.app.getPreferences("options");
		vibration	= prefs.getBoolean("vibration", true);
		music		= prefs.getBoolean("music", 	true);
		particle	= prefs.getBoolean("particle", 	true);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		Preferences prefs = Gdx.app.getPreferences("options");
		vibration	= prefs.getBoolean("vibration", true);
		music		= prefs.getBoolean("music", 	true);
		particle	= prefs.getBoolean("particle", 	true);
		
		Skin skin = getSkin();
		
		Table table = new Table(skin);
		table.width = stage.width();
		table.height = stage.height();
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		
		final CheckBox vibratorButton = new CheckBox(skin);
		layout.register("vibratorCheckBox", vibratorButton);
		vibratorButton.setChecked(vibration);
		vibratorButton.setClickListener( new ClickListener() {

			@Override
			public void click(Actor actor, float x, float y) {
            	game.playSound(GameSound.CLICK);
				if(((CheckBox)actor).isChecked()) Gdx.input.vibrate(100);
			}
			
		});
		
		final CheckBox musicButton = new CheckBox(skin);
		layout.register("musicCheckBox", musicButton);
		musicButton.setChecked(music);
		musicButton.setClickListener( new ClickListener() {

			@Override
			public void click(Actor actor, float x, float y) {
            	game.playSound(GameSound.CLICK);
				if(music = ((CheckBox)actor).isChecked()) game.theme.play();
				else game.theme.stop();
			}
			
		});
		
		final CheckBox particleButton = new CheckBox(skin);
		layout.register("particleCheckBox", particleButton );
		particleButton.setChecked(particle);
		particleButton.setClickListener( new ClickListener() {

			@Override
			public void click(Actor actor, float x, float y) {
            	game.playSound(GameSound.CLICK);
			}
			
		});
		
		TextButton backButton = new TextButton("OK", skin);
		backButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.playSound(GameSound.CLICK);
        		Preferences prefs = Gdx.app.getPreferences("options");
        		prefs.putBoolean("vibration",	vibration	=vibratorButton.isChecked());
        		prefs.putBoolean("music",		music		=musicButton.isChecked());
        		prefs.putBoolean("particle",	particle	=particleButton.isChecked());
        		prefs.flush();
        		
                game.setScreen(game.getMainMenuScreen());
            }
        } );
		layout.register("backButton", backButton);
		
		layout.parse(Gdx.files.internal( "layouts/options.txt" ).readString("UTF-8"));
	}

	@Override
	public void backDown() {
		game.setScreen(game.getMainMenuScreen());
	}
}
