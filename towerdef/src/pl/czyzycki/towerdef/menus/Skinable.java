package pl.czyzycki.towerdef.menus;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Bazowa klasa dla ekranów korzystaj¹cych ze skórki.
 *
 */
public class Skinable {
	private Skin skin = null;
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
			SkinLoader loader = new SkinLoader(new InternalFileHandleResolver());
			SkinParameter parameter = new SkinParameter("layouts/menuskin.png");
			skin = loader.loadSync(assetManager, "layouts/menuskin.json", parameter);
		}
		return skin;
	}
	
	public void dispose() {
		if(skin != null)
			skin.dispose();
	}
}
