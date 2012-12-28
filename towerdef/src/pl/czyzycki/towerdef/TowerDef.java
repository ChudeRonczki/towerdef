package pl.czyzycki.towerdef;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.menus.MainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;


/**
 * Klasa g��wna gry (nie licz�c klas startowych backend�w).
 * @author Ciziu
 *
 */
public class TowerDef extends Game {

	static TowerDef game; // Po co si� m�czy� :P Aktualnie bodaj nieu�ywane, ale jest :)
	
	public BitmapFont debugFont; // Tymczasowa czcionka do wszystkiego
	
	AssetManager assetManager; // Manager asset�w (grafika, d�wi�ki, czcionki)
	TileAtlas tileAtlas; // Atlas tile'i. Nie jest obs�ugiwany przez AssetManager
	
	GameplayScreen gameplayScreen;
	MainMenu mainMenu;
	
	@Override
	public void create() {
		game = this;
		debugFont = new BitmapFont();
		tileAtlas = new TileAtlas(TiledLoader.createMap(Gdx.files.internal("maps/emptyMap.tmx")), Gdx.files.internal("maps"));
		assetManager = new AssetManager();
		assetManager.load("images/objects.pack", TextureAtlas.class);
		assetManager.finishLoading();
		gameplayScreen = new GameplayScreen(this);
		mainMenu = new MainMenu();
		setScreen(mainMenu);
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

}
