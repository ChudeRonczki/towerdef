package pl.czyzycki.towerdef.gameplay.entities;

import pl.czyzycki.towerdef.gameplay.GameplayScreen;
import pl.czyzycki.towerdef.gameplay.helpers.Circle;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * Klasa bazowa dla wie¿yczek celuj¹cych w konkretnych wrogów.
 *
 */
abstract public class TargetTower extends Tower {

	Vector2 direction;
	Enemy target;
	
	public TargetTower() {
		super();
	}
	
	protected void _init(GameplayScreen screen) {
		super._init(screen);
		direction = new Vector2();
	}
	
	protected void _set(TargetTower tower, float x, float y) {
		super._set(tower,x,y);
		targeted = tower.targeted;
		direction.set(0f,-1f);
		target = null;
	}
	
	protected boolean checkTarget() {
		Circle range = getRange();
		if(target == null || !target.alive || !Circle.colliding(range, target.hitZone)) {
			return ((target = screen.enemyInRange(range, targeted)) != null);
		} else return true;
	}
	
	@Override
	public void debugDraw(ShapeRenderer shapeRenderer) {
		super.debugDraw(shapeRenderer);
		if(target != null) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(0,0,1,1);
			shapeRenderer.line(pos.x, pos.y, target.pos.x, target.pos.y);
			shapeRenderer.end();
		}
		
	}
}
