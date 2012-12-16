package pl.czyzycki.towerdef.gameplay;

import pl.czyzycki.towerdef.gameplay.entities.Tower;
import pl.czyzycki.towerdef.gameplay.entities.Tower.Upgrade;
import pl.czyzycki.towerdef.gameplay.entities.Tower.Upgradeable;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

// nowa nazwa tej klasy:
// towerPropsgui?
// towergui?

public class GameplayUpgradeGUI {
	
	class GameplayUpgradeGUIGestureListener extends GestureAdapter {

		@Override
		public boolean tap(int x, int y, int count) {
			Vector3 hudCord = new Vector3(x, y, 1);
			screen.gui.hudCamera.unproject(hudCord);
			
			return onTap(hudCord.x, hudCord.y);
		}
		
	}
	
	private GameplayScreen screen;
	private Tower selectedTower = null;
	private Vector2 upgradePos = new Vector2();
	private Vector3 towerPos = new Vector3();
	
	Sprite removeTowerSprite;
	Sprite rangeSprite;
	Sprite cooldownSprite;
	Sprite multiplierSprite;
	Sprite damageSprite;
	public GameplayUpgradeGUIGestureListener listener;
	
	private int getUpgradesCount() {
		if(selectedTower == null) return 0;
		if(selectedTower.upgrades == null) return 0;
		return selectedTower.upgrades.length;
	}
	
	GameplayUpgradeGUI(GameplayScreen screen) {
		this.screen = screen;
		listener = new GameplayUpgradeGUIGestureListener();
	}
	
	void load(TextureAtlas texAtlas) {
		removeTowerSprite = texAtlas.createSprite("remove-tower");
		
		rangeSprite       = texAtlas.createSprite("upgrade-range");
		cooldownSprite    = texAtlas.createSprite("upgrade-cooldown");
		multiplierSprite  = texAtlas.createSprite("upgrade-multiplier");
		damageSprite      = texAtlas.createSprite("upgrade-damage");
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
	
	void calcTowerPos() {
		// wzór na przekszta³cenie z przestrzeni world do hud:
		
		// towerPos * worldMatrix
		// towerPos * hudMatrix^-1
		
		towerPos.x = selectedTower.getPos().x;
		towerPos.y = selectedTower.getPos().y;
		towerPos.z = 0;
		
		towerPos.mul(screen.camera.combined);
		
		// nie chcê liczyæ macierzy odwrotnej, dlatego robiê przekszta³cenia "rêcznie"
		towerPos.x += 1;
		towerPos.y += 1;
		towerPos.x /= 2.0f;
		towerPos.y /= 2.0f;
		towerPos.x *= screen.gui.hudCamera.viewportWidth;
		towerPos.y *= screen.gui.hudCamera.viewportHeight;
	}
	
	boolean onTap(float x, float y) {
		if(isShowed() == false) return false;
		
		float radius = removeTowerSprite.getWidth()/2.0f;
		calcTowerPos();
		int upgradesCount = getUpgradesCount();
		int buttonsCount = 1+upgradesCount; // bo jeszcze dodatkowo przycisk "remove tower"
		for(int i=0; i<buttonsCount; i++) {
			Vector2 pos = getUpgradeIconPos(i, buttonsCount);
			pos.x += towerPos.x;
			pos.y += towerPos.y;
			pos.x -= x;
			pos.y -= y;
			
			if(radius*radius > pos.x*pos.x + pos.y*pos.y) {
				// tap on upgrade
				
				if(i == 0) { // sell tower
					screen.sellTower(selectedTower);
					this.hide();
				} else {
					Upgrade upgrade = selectedTower.upgrades[i-1];
					
					if(selectedTower.upgradeLevelIters[i-1]<upgrade.levels.length) {
						if(upgrade.levels[selectedTower.upgradeLevelIters[i-1]].cost < screen.money) {
							screen.money -= upgrade.levels[selectedTower.upgradeLevelIters[i-1]].cost;
							selectedTower.upgradeLevelIters[i-1]++;
						}
					}
				}
				
				return true;
			}
		}
		return false;
	}
	
	Vector2 getUpgradeIconPos(int i, int count) {
		float radius = 40/screen.camera.zoom + removeTowerSprite.getWidth()/2.0f;
		
		float sn = (float) Math.sin(Math.PI*2.0f*i/count);
		float cs = (float) Math.cos(Math.PI*2.0f*i/count);
		upgradePos.x = -sn*radius;
		upgradePos.y = -cs*radius;
		
		return upgradePos;
	}
	
	void render(float dt) {
		if(selectedTower == null) return;
		
		calcTowerPos();
		
		Vector2 pos;
		int upgradesCount = getUpgradesCount();
		int buttonsCount = 1+upgradesCount; // bo jeszcze dodatkowo przycisk "remove tower"
		for(int i=0; i<buttonsCount; i++) {
			// wybór odpowiedniego sprite
			Sprite icon = null;
			Upgrade upgrade = null;
			
			if(i == 0) icon = removeTowerSprite;
			else {
				upgrade = selectedTower.upgrades[i-1];
				if(upgrade.upgraded == Upgradeable.RANGE) {
					icon = rangeSprite;
				} else if(upgrade.upgraded == Upgradeable.COOLDOWN) {
					icon = cooldownSprite;
				} else if(upgrade.upgraded == Upgradeable.MULTIPLIER) {
					icon = multiplierSprite;
				} else
					icon = damageSprite;
			}
			
			pos = getUpgradeIconPos(i, buttonsCount);
			
			float halfW = icon.getWidth()  / 2.0f;
			float halfH = icon.getHeight() / 2.0f;
			
			float iconX = towerPos.x+pos.x-halfW;
			float iconY = towerPos.y+pos.y-halfH;
			
			icon.setPosition(iconX, iconY);
			icon.draw(screen.batch);
			
			if(upgrade != null)
			{
				int upgradeLevel = selectedTower.upgradeLevelIters[i-1];
				
				screen.game.debugFont.setScale(2);
				screen.game.debugFont.draw(screen.batch, upgradeLevel+"/"+upgrade.levels.length, iconX, iconY);
				float ydiff = 30;
				if(upgradeLevel == upgrade.levels.length)
					screen.game.debugFont.draw(screen.batch, "MAX", iconX, iconY-ydiff);
				else
					screen.game.debugFont.draw(screen.batch, "cost: "+(upgrade.levels[upgradeLevel].cost), iconX, iconY-ydiff);
				screen.game.debugFont.setScale(1);
			}
		}
	}
}
