package pl.czyzycki.towerdef.gameplay;

import pl.czyzycki.towerdef.gameplay.entities.Bullet;
import pl.czyzycki.towerdef.gameplay.entities.Enemy;
import pl.czyzycki.towerdef.gameplay.entities.Spawn;
import pl.czyzycki.towerdef.gameplay.entities.Tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.StringBuilder;

class GameplayDebug {

	GameplayScreen screen;
	
	boolean base, spawns, enemies, towers, bullets, camera;
	StringBuilder stringBuilder;
	
	GameplayDebug(GameplayScreen screen) {
		this.screen = screen;
	}
	
	void render() {
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		if(base) screen.base.debugDraw(screen.shapeRenderer);
		if(spawns) {
			screen.shapeRenderer.begin(ShapeType.Line);
			for(Spawn spawn : screen.spawns) spawn.debugDraw(screen.shapeRenderer);
			screen.shapeRenderer.end();
		}
		if(enemies) {
			screen.shapeRenderer.begin(ShapeType.FilledCircle);
			for(Enemy enemy : screen.groundEnemies) enemy.debugDraw(screen.shapeRenderer);
			for(Enemy enemy : screen.airborneEnemies) enemy.debugDraw(screen.shapeRenderer);
			screen.shapeRenderer.end();
		}
		if(towers) {
			for(Tower tower : screen.towers) tower.debugDraw(screen.shapeRenderer);
		}
		if(bullets) {
			screen.shapeRenderer.begin(ShapeType.FilledCircle);
			for(Bullet bullet : screen.bullets) bullet.debugDraw(screen.shapeRenderer);
			screen.shapeRenderer.end();
		}
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
		screen.batch.begin();
		if(base) screen.base.debugText(screen.batch, screen.game.debugFont);
		if(spawns) {
			for(Spawn spawn : screen.spawns) spawn.debugText(screen.batch, screen.game.debugFont);
		}
		if(enemies) {
			for(Enemy enemy : screen.groundEnemies) enemy.debugText(screen.batch, screen.game.debugFont);
			for(Enemy enemy : screen.airborneEnemies) enemy.debugText(screen.batch, screen.game.debugFont);
		}
		if(towers) {
			for(Tower tower : screen.towers) tower.debugText(screen.batch, screen.game.debugFont);
		}
		screen.batch.end();
	}
	
	void renderGUI() {
		if(camera) {
			screen.batch.begin();
			stringBuilder.length = 0;
			stringBuilder.append("Position: ");
			stringBuilder.append(screen.camera.position.x);
			stringBuilder.append(' ');
			stringBuilder.append(screen.camera.position.y);
			stringBuilder.append(" Zoom: ");
			stringBuilder.append(screen.camera.zoom);
			screen.game.debugFont.draw(screen.batch, stringBuilder, 15f, 30f);
			screen.batch.end();
		}
	}
}
