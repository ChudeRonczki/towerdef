package pl.czyzycki.towerdef.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Skinable {
	private Skin skin;
	private AssetManager assetManager;
	
	Skinable(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public Skin getSkin() {
		// HACK: Teraz skin jest tworzony dla kazdego ekranu osobno.
		// Gdy probuje zrobic z tego zmienna statyczna to wtedy tekstury
		// OpenGLowe sie nie chca przeladowac.
		// Zostawiam takie, poniewaz nie wnosi to duzego narzutu.
		// (jest ladowany tylko jeden plik tekstowy)
		if(skin == null) {
			Texture skinTexture = assetManager.get("layouts/menuskin.png", Texture.class);
			skin = new Skin(Gdx.files.internal("layouts/menuskin.json"), skinTexture);
		}
		return skin;
	}
	
	public void dispose() {
		skin.dispose();
	}
}
