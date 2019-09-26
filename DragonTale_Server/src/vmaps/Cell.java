package vmaps;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import componentNew.Broadcast;
import componentNew.EntityManager;
import componentNew.Position;
import game.World;
import main.LOGGER;

public class Cell {

	private boolean visited = true;
	public Set<Integer> map = new HashSet<Integer>();
	private int cell_x;
	private int cell_y;
	
	public Cell(int x, int y) {
		cell_x = x;
		cell_y = y;
		if (x == 0 && y == 0) {
			LOGGER.info("SPAWN NEW CELLLLL 0,0", this);
			Point[] points = new Point[] { new Point(100, 20) };
	
			for (Point p : points) {
				World.getInstance().requestObjectSpawn("slug", 1, p.x, p.y, true, 0, null);
			}
		}
	}
	
	public void registerObject(int handle) {

		Position.setCell(handle);
		map.add(handle);
		//LOGGER.debug("REGISTERING: " + cell_x + "," + cell_y + " HANDLE:" + obj.gethandle(), this);

		for (Integer current_obj : map) {
			Broadcast.AddListener(current_obj, handle);
			Broadcast.AddListener(handle, current_obj);
		}

	}
	
	public void unregisterObject(int handle) {

		for (Integer current_obj : map) {
			Broadcast.RemoveListenerfromID(current_obj, handle);
			Broadcast.RemoveListenerfromID(handle, current_obj);
		}
	}
	
	public void update(boolean visited) {
		// makes sure we update only once per update
		if (this.visited == visited)
			return;

		this.visited = !this.visited;

		for (Iterator<Integer> iterator = map.iterator(); iterator.hasNext();) {
			EntityManager.getInstance().update(iterator.next());
		}

	}
	
	
	
}
