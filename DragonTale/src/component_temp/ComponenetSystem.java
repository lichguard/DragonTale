package component_temp;

import java.awt.Graphics2D;

import dt.entity.Animation;

public class ComponenetSystem {

	public void Physics(PositionComponent pc, Graphics2D g)
	{
		
	}
	
	public void DrawEntity(Animation animation, PositionComponent position, Graphics2D g)
	{
		if (animation == null || position == null)
			return;
		
		if (position.facing)
			g.drawImage(animation.getImage(), (int) (position.x + position.xmap - animation.width / 2), (int) (position.y + position.ymap - animation.height / 2), null);
		else
			g.drawImage(animation.getImage(), (int) (position.x + position.xmap - animation.width / 2 + animation.width), (int) (position.y + position.ymap - animation.height / 2),
					-animation.width, animation.height, null);
	}
	
}
