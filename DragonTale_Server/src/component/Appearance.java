package component;

public class Appearance implements component.IComponent {

	public static final int componentID = EntityManager.AppearanceID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public float alpha = 1.0f; //draw half transparent
	public boolean fadeout = false;
	public boolean fadein = false;
	public float fadeDuration = 500.0f;
	public long statFadeTimer = 0;
	
	
	
	public Appearance() {
		setFadeIn();
	}
	public void setFadeOut() {
		alpha = 1.0f;
		fadeout = true;
		statFadeTimer = System.currentTimeMillis();
	}
	public void setFadeIn() {
		alpha = 0;
		fadein = true;
		statFadeTimer = System.currentTimeMillis();
	}
}

