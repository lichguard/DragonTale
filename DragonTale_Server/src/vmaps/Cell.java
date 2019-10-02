package vmaps;

import java.util.HashSet;
import java.util.Set;

import component.Broadcast;
import component.EntityManager;
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

 		LOGGER.debug("REGISTERING: " + cell_x + "," + cell_y + " HANDLE:" + handle, this);

		for (Integer cell_handle : map) {
			Broadcast.AddListener(cell_handle, handle);
			Broadcast.AddListener(handle, cell_handle);
		}
		map.add(handle);
	}

	public void unregisterObject(int handle) {

		map.remove(handle);
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

		Object[] arr =  map.toArray();
		for (Object handle : arr) {
			EntityManager.getInstance().update((Integer)handle);
		}

	}

}
