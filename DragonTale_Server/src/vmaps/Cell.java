package vmaps;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import game.GameConstants;
import game.World;
import main.LOGGER;
import objects.GameObject;
import objects.Player;
import objects.Spawner;

public class Cell {

	private boolean visited = false;
	private boolean loaded = false;
	private Map<Integer, GameObject> map = new HashMap<Integer, GameObject>();
	private int cell_x;
	private int cell_y;
	public Cell(int x, int y) {
		cell_x = x;
		cell_y = y;
		if (x == 0 && y == 0) {
			LOGGER.info("SPAWN NEW CELLLLL 0,0", this);
			Point[] points = new Point[] { new Point(100, 20) };
	
			for (Point p : points) {
				World.getInstance().requestObjectSpawn(Spawner.SLUGGER, p.x, p.y, true, false, null);
			}
		}
	}
	
	public void addtoCell(GameObject obj) {
		for (GameObject current_obj : map.values()) {
			current_obj.broadcaster.ClearListeners();
		}
	}
	
	
	public void registerObject(GameObject obj) {
		obj.cell = this;
		map.put(obj.gethandle(), obj);
		LOGGER.debug("REGISTERING: " +  cell_x +  "," + cell_y + " HANDLE:" + obj.gethandle() , this);
		
		for (GameObject current_obj : map.values()) {
			if (obj instanceof Player)
				current_obj.broadcaster.AddListener((Player) obj);
			
			if (current_obj instanceof Player)
				obj.broadcaster.AddListener((Player) current_obj);
			
		}
		
	}
	
	public void unregisterObject(int handle) {
		GameObject obj = map.get(handle) ;
		if  (obj != null) {
			for (GameObject current_obj : map.values()) {
				if (obj instanceof Player)
					current_obj.broadcaster.RemoveListener((Player) obj);
				
				if (current_obj instanceof Player)
					obj.broadcaster.RemoveListener((Player) current_obj);
			}
		}
		//map.remove(handle);
		
	}
	
	public void update() {
		//if (visited)
		//	return;
		
		//visited = true;
		for (Iterator<GameObject> iterator = map.values().iterator(); iterator.hasNext();) {
			GameObject obj = (GameObject) iterator.next();
			obj.update(World.getInstance());
			if (!obj.setMapPosition()) {
				unregisterObject(obj.gethandle());
				LOGGER.debug("UNREGISTERING: " +  cell_x +  "," + cell_y + " HANDLE:" + obj.gethandle() , this);
			
				iterator.remove();
			}
			
		}

	}
	
	
	
}
