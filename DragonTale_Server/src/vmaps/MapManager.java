package vmaps;

import java.util.Iterator;
import java.util.Vector;

import game.GameConstants;
import game.World;
import network.WorldSession;
import objects.GameObject;

public class MapManager {

		Vector<GameMap> maps = new Vector<GameMap>();
		private static MapManager instance = null;
		
		public static MapManager getInstance() {
			if (instance == null)
				instance = new MapManager();
			
			return instance;
		}
		
		public GameMap createMap(int tileSize) {
			GameMap gm = new GameMap(tileSize); 
			maps.add(gm);
			return gm;
		}
		

}
