package componentNew;

import game.GameConstants;
import vmaps.Cell;
import vmaps.GameMap;

public class Position implements componentNew.IComponent {
	public float x;
	public float y;
	public float xmap;
	public float ymap;
	public Cell cell = null;
	public GameMap gameMap = null;
	
	public Position(float x, float y) {
		this.x = x;
		this.y =y;
	}
	
	public static float getx(int id) {
		return ((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).x;
	}
	
	public static float gety(int id) {
		return ((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).x;
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

		if (pc.cell == null || pc.cell != pc.gameMap.grid[cell_x][cell_y]) {
			if (pc.cell != null) {
				pc.cell.unregisterObject(handle);
			}
			// create the cell if needed
			if (pc.gameMap.grid[cell_x][cell_y] == null) {
				pc.gameMap.grid[cell_x][cell_y] = new Cell(cell_x, cell_y);
			}
			// finally register object
			pc.gameMap.grid[cell_x][cell_y].registerObject(handle);
			// LOGGER.info(gethandle() + " MOVED TO CELL: " + cell_x + "," + cell_y , this);
			pc.cell =  pc.gameMap.grid[cell_x][cell_y];
		}

	}

}
