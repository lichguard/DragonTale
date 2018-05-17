package dt.entity;

import dt.network.Session;
import main.World;

public class EntityManager {

	public Entity player;
	public Entity[] Fireball;
	public Entity[] Slugger;
	public Entity[] Coin;
	public Entity[] NetworkPlayer;

		EntityManager()
		{
			//entities = new Vector<Entity>();
		}
		
		public int SpawnEntity(int entity_type, int model, boolean isNetwork,
				float x, float y, boolean facing,float dx,float dy)
		{
			//entities.add(index, element);
			return 0;
		}
		
		public void update(World world,Session session)
		{/*
			int count = entities.size();
			for (int i = 0; i < count; i++) {
				entities.get(i).update();
				
			}
			*/
			
		}
		
		public void localPhysics()
		{
			
		}
		
}
