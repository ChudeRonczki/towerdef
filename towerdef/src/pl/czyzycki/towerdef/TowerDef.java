package pl.czyzycki.towerdef;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.entities.Base;
import pl.czyzycki.towerdef.gameplay.entities.BulletTower;
import pl.czyzycki.towerdef.gameplay.entities.Enemy;
import pl.czyzycki.towerdef.menus.InstructionsScreen;
import pl.czyzycki.towerdef.menus.MainMenuScreen;
import pl.czyzycki.towerdef.menus.OptionsScreen;
import pl.czyzycki.towerdef.menus.SelectLevelScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;


/**
 * Klasa g³ówna gry (nie licz¹c klas startowych backendów).
 * @author Ciziu
 *
 */
public class TowerDef extends Game {
	public enum GameSound {
		CLICK,
		DESTROYED,
		BOMB,
		MAXUP,
		BONUS,
		AREA,
		BULLET_HIT,
		BULLET_START,
		POINT,
		SLOW_ON,
		SLOW_OFF
	}
	
	public enum Particle {
		ENEMY_DESTROYED
	}
	
	static TowerDef game; // Po co siê mêczyæ :P Aktualnie bodaj nieu¿ywane, ale jest :)
	
	public BitmapFont debugFont; // Tymczasowa czcionka do wszystkiego
	
	AssetManager assetManager; // Manager assetów (grafika, dŸwiêki, czcionki)
	TileAtlas tileAtlas; // Atlas tile'i. Nie jest obs³ugiwany przez AssetManager
	public Music theme;
	Sound sounds[] = new Sound[20];
	ParticleEffectPool particlePool[] = new ParticleEffectPool[5];
	
	GameplayScreen gameplayScreen;
	MainMenuScreen mainMenuScreen;
	OptionsScreen optionsScreen;
	InstructionsScreen instructionsScreen;
	SelectLevelScreen selectLevelScreen;
	
	@Override
	public void create() {
		game = this;
		debugFont = new BitmapFont(Gdx.files.internal("fonts/hud.fnt"), false);
		// TODO skala ma byæ inna, ale ta jest dostosowana do rozdzia³ki 480
		debugFont.setScale(800.0f/480.0f);
		tileAtlas = new TileAtlas(TiledLoader.createMap(Gdx.files.internal("maps/emptyMap.tmx")), Gdx.files.internal("maps"));
		assetManager = new AssetManager();
		assetManager.load("images/objects.pack", TextureAtlas.class);
		assetManager.load("layouts/menuskin.png", Texture.class);
		assetManager.load("layouts/star.png", Texture.class);
		assetManager.load("layouts/starslot.png", Texture.class);
		assetManager.load("layouts/star-big.png", Texture.class);
		assetManager.load("layouts/starslot-big.png", Texture.class);
		assetManager.load("music/theme.ogg", Music.class);
		assetManager.load("layouts/menu-bg.png", Texture.class);
		assetManager.load("layouts/menu-bg2.png", Texture.class);
		assetManager.load("layouts/font.png", Texture.class);
		assetManager.load("images/enemy-anim.png", Texture.class);
		assetManager.load("images/base-anim.png", Texture.class);
		assetManager.load("images/flying.png", Texture.class);
		assetManager.load("images/bullet-tower-airborne.png", Texture.class);
		assetManager.load("images/bullet-tower-both.png", Texture.class);
		assetManager.load("images/bullet-tower-ground.png", Texture.class);
		assetManager.load("sounds/click.wav", Sound.class);
		assetManager.load("sounds/destroyed.wav", Sound.class);
		assetManager.load("sounds/bomb.mp3", Sound.class);
		assetManager.load("sounds/maxup.mp3", Sound.class);
		assetManager.load("sounds/bonus.wav", Sound.class);
		assetManager.load("sounds/area.wav", Sound.class);
		assetManager.load("sounds/bullethit.ogg", Sound.class);
		assetManager.load("sounds/bulletstart.wav", Sound.class);
		assetManager.load("sounds/point.wav", Sound.class);
		assetManager.load("sounds/slowon.wav", Sound.class);
		assetManager.load("sounds/slowoff.wav", Sound.class);
		assetManager.finishLoading();
		
		Enemy.loadAnimations(assetManager);
		BulletTower.loadAnimations(assetManager);
		Base.loadAnimations(assetManager);
		
		theme = assetManager.get("music/theme.ogg", Music.class);
		theme.setLooping(true);
		
		sounds[0] = assetManager.get("sounds/click.wav", Sound.class);
		sounds[1] = assetManager.get("sounds/destroyed.wav", Sound.class);
		sounds[2] = assetManager.get("sounds/bomb.mp3", Sound.class);
		sounds[3] = assetManager.get("sounds/maxup.mp3", Sound.class);
		sounds[4] = assetManager.get("sounds/bonus.wav", Sound.class);
		sounds[5] = assetManager.get("sounds/area.wav", Sound.class);
		sounds[6] = assetManager.get("sounds/bullethit.ogg", Sound.class);
		sounds[7] = assetManager.get("sounds/bulletstart.wav", Sound.class);
		sounds[8] = assetManager.get("sounds/point.wav", Sound.class);
		sounds[9] = assetManager.get("sounds/slowon.wav", Sound.class);
		sounds[10] = assetManager.get("sounds/slowoff.wav", Sound.class);
		
		loadParticle(Particle.ENEMY_DESTROYED.ordinal(), "particles/enemy-destroyed.txt");
		
		// Inaczej back key bedzie pauzowal apke:
		Gdx.input.setCatchBackKey(true);
		
		gameplayScreen = new GameplayScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		optionsScreen = new OptionsScreen(this);
		instructionsScreen = new InstructionsScreen(this);
		selectLevelScreen = new SelectLevelScreen(this);
		
		if(OptionsScreen.musicEnabled()) theme.play();
		
		setScreen(mainMenuScreen);
	}

	public void loadParticle(int i, String filename) {
		ParticleEffect particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal(filename), Gdx.files.internal("particles"));
		particlePool[i] = new ParticleEffectPool(particleEffect, 1, 2);
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

	public Screen getSelectLevelScreen() {
		return selectLevelScreen;
	}

	public void playSound(GameSound sound) {
		if(OptionsScreen.musicEnabled()) sounds[sound.ordinal()].play(); 
	}
	
	// TODO cos czuje ze to tutaj nie pasuje :<
	public void createParticle(Particle particle, float x, float y) {
		if(OptionsScreen.particleEnabled()) {
			PooledEffect effect = particlePool[particle.ordinal()].obtain();
			effect.setPosition(x, y);
			gameplayScreen.addEffect(effect);
		}
	}
}
