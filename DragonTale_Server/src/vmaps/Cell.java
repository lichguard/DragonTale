package vmaps;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import objects.GameObject;
import objects.Player;

public class Cell {

	private boolean visited = false;
	private boolean loaded = false;
	private Map<Integer, GameObject> map = new HashMap<Integer, GameObject>();
	
	public void addtoCell(GameObject obj) {
		for (GameObject current_obj : map.values()) {
			current_obj.broadcaster.ClearListeners();
		}
	}
	
	
	public void registerObject(GameObject obj) {
		for (GameObject current_obj : map.values()) {
			if (current_obj instanceof Player)
				current_obj.broadcaster.AddListener((Player) current_obj);
		}
	}
	
	public void unregisterObject(int handle) {
		GameObject obj = map.get(handle) ;
		if  (obj != null) {
			for (GameObject current_obj : map.values()) {
				if (current_obj instanceof Player)
				current_obj.broadcaster.RemoveListener((Player) current_obj);
			}
		}
		
	}
}
