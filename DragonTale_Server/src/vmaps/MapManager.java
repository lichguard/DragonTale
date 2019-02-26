package vmaps;

import java.util.Vector;

public class MapManager {

		Vector<GameMap> maps = new Vector<GameMap>();
		private static MapManager instance = null;
		
		public static MapManager getInstance() {
			if (instance == null)
				instance = new MapManager();
			
			return instance;
		}
		
		public GameMap createMap(int tileSize) {
			GameMap gm = new GameMap(); 
			maps.add(gm);
			return gm;
		}
		

}
