package vmaps;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import componentNew.Broadcast;
import componentNew.EntityManager;
import main.LOGGER;

public class Cell {

	private boolean visited = true;
	public Set<Integer> map = new HashSet<Integer>();
	private int cell_x;
	private int cell_y;
	
	public Cell(int x, int y) {
		cell_x = x;
		cell_y = y;
		LOGGER.info("SPAWN NEW CELL 0,0", this);
	}

	
	public void registerObject(int handle) {

		map.add(handle);
		LOGGER.debug("REGISTERING: " + cell_x + "," + cell_y + " HANDLE:" + handle, this);

		for (Integer cell_handle : map) {
			Broadcast.AddListener(cell_handle, handle);
			Broadcast.AddListener(handle, cell_handle);
		}

	}
	
	public void unregisterObject(int handle) {

		for (Integer cell_handle : map) {
			Broadcast.RemoveListenerfromID(cell_handle, handle);
			Broadcast.RemoveListenerfromID(handle, cell_handle);
		}
	}
	
	public void update(boolean visited) {
		// makes sure we update only once per update
		if (this.visited == visited)
			return;

		this.visited = !this.visited;

		//LOGGER.info("updaing cell: " + cell_x + " " + cell_y, this);
		for (Iterator<Integer> iterator = map.iterator(); iterator.hasNext();) {
			EntityManager.getInstance().update(iterator.next());
		}

	}
	
	
	
}
