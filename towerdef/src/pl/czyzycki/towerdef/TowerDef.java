package pl.czyzycki.towerdef;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.menus.InstructionsScreen;
import pl.czyzycki.towerdef.menus.MainMenuScreen;
import pl.czyzycki.towerdef.menus.OptionsScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;


/**
 * Klasa g³ówna gry (nie licz¹c klas startowych backendów).
 * @author Ciziu
 *
 */
public class TowerDef extends Game {

	static TowerDef game; // Po co siê mêczyæ :P Aktualnie bodaj nieu¿ywane, ale jest :)
	
	public BitmapFont debugFont; // Tymczasowa czcionka do wszystkiego
	
	AssetManager assetManager; // Manager assetów (grafika, dŸwiêki, czcionki)
	TileAtlas tileAtlas; // Atlas tile'i. Nie jest obs³ugiwany przez AssetManager
	
	GameplayScreen gameplayScreen;
	MainMenuScreen mainMenuScreen;
	OptionsScreen optionsScreen;
	InstructionsScreen instructionsScreen;
	
	@Override
	public void create() {
		game = this;
		debugFont = new BitmapFont();
		tileAtlas = new TileAtlas(TiledLoader.createMap(Gdx.files.internal("maps/emptyMap.tmx")), Gdx.files.internal("maps"));
		assetManager = new AssetManager();
		assetManager.load("images/objects.pack", TextureAtlas.class);
		assetManager.finishLoading();
		
		gameplayScreen = new GameplayScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		optionsScreen = new OptionsScreen(this);
		instructionsScreen = new InstructionsScreen(this);
		
		setScreen(mainMenuScreen);
	}

	@Override
	public void dispose() {
		gameplayScreen.dispose();
		assetManager.dispose();
		tileAtlas.dispose();
		debugFont.dispose();
		super.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public TileAtlas getTileAtlas() {
		return tileAtlas;
	}

	public static TowerDef getGame() {
		return game;
	}

	public GameplayScreen getGameplayScreen() {
		return gameplayScreen;
	}
	
	public InstructionsScreen getInstructionsScreen() {
		return instructionsScreen;
	}

	public OptionsScreen getOptionsButton() {
		return optionsScreen;
	}
	
	public MainMenuScreen getMainMenuScreen() {
		return mainMenuScreen;
	}

}
