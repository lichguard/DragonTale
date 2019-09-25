package componentNew;

public class Position implements componentNew.IComponent {
	public float x;
	public float y;
	public float xmap;
	public float ymap;
	
	public Position(float x, float y) {
		this.x = x;
		this.y =y;
	}
	
	public static float getx(int id, float x) {
		return ((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).x;
	}
	
	public static float gety(int id, float y) {
		return ((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).x;
	}
	
	
	public static void setx(int id, float x) {
		((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).x = x;
	}
	
	public static void sety(int id, float y) {
		((Position)EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID)).y = y;
	}
	
	public static void setPosition(int id, float x,float y) {
		setx(id,x);
		sety(id,y);
	}

}
