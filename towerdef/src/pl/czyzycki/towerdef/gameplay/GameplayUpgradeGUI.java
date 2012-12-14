package pl.czyzycki.towerdef.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

import pl.czyzycki.towerdef.gameplay.entities.Tower;

// nowa nazwa tej klasy:
// towerPropsgui?
// towergui?

public class GameplayUpgradeGUI {
	
	private GameplayScreen screen;
	private Tower selectedTower = null;
	private Vector2 upgradePos = new Vector2();
	
	Sprite removeTowerSprite;
	
	GameplayUpgradeGUI(GameplayScreen screen) {
		this.screen = screen;
	}
	
	void load(TextureAtlas texAtlas) {
		removeTowerSprite = texAtlas.createSprite("remove-tower");
	}
	
	Tower getSelectedTower() {
		return selectedTower;
	}
	
	void setSelectedTower(Tower selectedTower) {
		this.selectedTower = selectedTower;
	}
	
	boolean isShowed() {
		return selectedTower != null;
	}
	
	void hide() {
		selectedTower = null;
	}
	
	boolean tap(float x, float y) {
		if(isShowed() == false) return false;
		
		float radius = removeTowerSprite.getWidth()/2.0f;
		Vector2 towerPos = selectedTower.getPos();
		
		for(int i=0; i<5; i++) {
			Vector2 pos = getUpgradeIconPos(i, 5);
			pos.x += towerPos.x;
			pos.y += towerPos.y;
			pos.x -= x;
			pos.y -= y;
			if(radius*radius > pos.x*pos.x + pos.y*pos.y) {
				// tapniêto
				return true;
			}
		}
		return false;
	}
	
	Vector2 getUpgradeIconPos(int i, int count) {
		float radius = removeTowerSprite.getWidth()*1.5f;
		
		float sn = (float) Math.sin(Math.PI*2.0f*i/5);
		float cs = (float) Math.cos(Math.PI*2.0f*i/5);
		upgradePos.x = -sn*radius;
		upgradePos.y = -cs*radius;
		
		return upgradePos;
	}
	
	void render(float dt) {
		if(selectedTower == null) return;
		
		float halfW = removeTowerSprite.getWidth()  / 2.0f;
		float halfH = removeTowerSprite.getHeight() / 2.0f;
		
		Vector2 towerPos = selectedTower.getPos();
		Vector2 pos;
		for(int i=0; i<5; i++) {
			pos = getUpgradeIconPos(i, 5);
			removeTowerSprite.setPosition(towerPos.x+pos.x-halfW, towerPos.y+pos.y-halfH);
			removeTowerSprite.draw(screen.batch);
		}
	}
}
