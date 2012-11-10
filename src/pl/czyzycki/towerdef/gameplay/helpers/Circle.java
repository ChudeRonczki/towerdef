package pl.czyzycki.towerdef.gameplay.helpers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Circle {
	public float radius;
	public Vector2 pos;
	
	public Circle() {}
	
	public void set(Circle circle) {
		radius = circle.radius;
		pos.set(circle.pos);
	}
	
	public static boolean colliding(Circle c1, Circle c2) {
		return (c1.pos.tmp().sub(c2.pos).len() <= c1.radius + c2.radius);
	}
	
	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.filledCircle(pos.x, pos.y, radius);
	}
}
