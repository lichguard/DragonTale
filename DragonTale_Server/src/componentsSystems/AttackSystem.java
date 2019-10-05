package componentsSystems;

import java.util.ArrayList;

import component.Animation;
import component.EntityManager;
import component.Health;
import component.Position;
import component.Size;
import component.Velocity;
import vmaps.Cell;

public class AttackSystem {

	
	public static void meleeattack(int handle) {
		
		Animation ac = (Animation) EntityManager.getInstance().getEntityComponent(handle, Animation.componentID);
		Size sc = (Size) EntityManager.getInstance().getEntityComponent(handle, Size.componentID);
		
		int angle = 0;
		if (!ac.facingRight) {
			angle = 180;
		}	
		for (Integer close_entity : getNearEntities(handle,sc.width + 10,angle,angle + 180)) {
		
			Health.decreaseHealth(close_entity,15);
			Velocity.hit(close_entity);
		}
		
	}

	public static ArrayList<Integer> getNearEntities(int handle, int range, int direction_start, int direction_end) {

		Cell cell = Position.getCell(handle);
		ArrayList<Integer> returnObjects = new ArrayList<Integer>();


		for (Integer close_entity : cell.map) {
			if (handle == close_entity)
				continue;
			
			float dx = Position.getx(close_entity) - Position.getx(handle);
			float dy = Position.gety(close_entity) - Position.gety(handle);
			
			System.out.println("RAG: " + Math.abs(dx) + Math.abs(dy));
			if (Math.abs(dx) + Math.abs(dy) <= range) {
				double angle = getHeading(dx, dy);

				System.out.println("anhge: " + angle);
				if (direction_start <= angle && angle <= direction_end) {
					returnObjects.add(close_entity);
				}
			}
		}
		return returnObjects;
	}

	public static float getHeading(float x, float y) {
		return (float) (((90.0 - (180.0 / Math.PI) * Math.atan2(y,x)) + 360.0) % 360);
	}
	
}
