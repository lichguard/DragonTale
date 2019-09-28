package componentNew;

public class AI implements componentNew.IComponent {
	
	public static final int  player = 0;
	public static final int  fireball = 1;
	public static final int  coin = 2;
	
	
	public int type;
	
	public AI(int type) {
		this.type = type;
	}
}
