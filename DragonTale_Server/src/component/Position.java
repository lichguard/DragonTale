package component;

import game.GameConstants;
import game.World;
import vmaps.Cell;
import vmaps.GameMap;

public class Position implements component.IComponent {
	
	public static final int componentID = EntityManager.PositionID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public float x;
	public float y;
	public float xmap;
	public float ymap;
	public Cell cell = null;
	public GameMap gameMap = null;
	

	public static void init(int handle, float x, float y) {
		Position pc = ((Position) EntityManager.getInstance().getEntityComponent(handle, EntityManager.PositionID));

		pc.gameMap = World.getInstance().tm;
		Position.setPosition(handle,x,y);
	}
	
	public static float getx(int id) {
		return ((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).x;
	}
	
	public static float gety(int id) {
		return ((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).y;
	}
	
	
	public static void setx(int id, float x) {
		((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).x = x;
	}
	
	public static void sety(int id, float y) {
		((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).y = y;
	}
	
	public static void setPosition(int handle, float x,float y) {
		setx(handle,x);
		sety(handle,y);
		setCell(handle);
	}

	public static void setCell(int handle) {
		Position pc = ((Position) EntityManager.getInstance().getEntityComponent(handle, EntityManager.PositionID));

		int cell_x = (int) (Position.getx(handle) / GameConstants.WIDTH);
		int cell_y = (int) (Position.gety(handle) / GameConstants.HEIGHT);

		Cell cell = pc.gameMap.getCell(cell_x, cell_y);
		if (cell != pc.cell) {
			if (pc.cell != null ) {
				pc.cell.unregisterObject(handle);
			}
			// finally register object
      			cell.registerObject(handle);
			// LOGGER.info(gethandle() + " MOVED TO CELL: " + cell_x + "," + cell_y , this);
			pc.cell = cell;
		}

	}
	
	public static void removeFromCell(int handle) {
		Position pc = ((Position) EntityManager.getInstance().getEntityComponent(handle, EntityManager.PositionID));
		if (pc == null)
			return;
		Position.getCell(handle).unregisterObject(handle);
		

	}
	

	public static Cell getCell(int handle) {
		Position pc = ((Position) EntityManager.getInstance().getEntityComponent(handle, EntityManager.PositionID));
		return pc.cell;
	}

}
