package vmaps;

import java.util.Iterator;
import java.util.Vector;

import game.World;
import network.WorldSession;

public class MapManager {

		Vector<GameMap> maps = new Vector<GameMap>();
		
		public void update() {
			//if in new cell
			int old_cell_x = 0;
			int old_cell_y = 0;
			int new_cell_x = 0;
			int new_cell_y = 0;
			for (int i = new_cell_x-2; i <= new_cell_x+2; i++) {
				for (int j = new_cell_y-2; j <= new_cell_y+2; j++) {
					if ()
				}
			}
			
			for (Iterator<WorldSession> iterator = World.getInstance().SessionMap.values().iterator(); iterator.hasNext();) {
				WorldSession s = (WorldSession) iterator.next();
				
				
				
			}
		}
}
