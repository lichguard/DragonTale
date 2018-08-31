package drawcomponenets;

import java.awt.Graphics2D;
import java.awt.image.RescaleOp;
import java.util.Iterator;

import entity.Entity;
import main.World;
import network.Session;

public class DrawEntityComponent implements IRender{

	World world;
	Entity entity;
	Session session;
	
	public DrawEntityComponent(World world,Entity entity,Session session )
	{
		this.world =world;
		this.entity=entity;
		this.session=session;
	}
	
	
	
	@Override
	public void draw(Graphics2D g,Iterator<IRender> it) {
		/*
		float[] scales = { 1f, 1f, 1f, 0.1f };
		float[] offsets = new float[4];
		
		RescaleOp rop = new RescaleOp(scales, offsets, null);

		if (entity.isFacingright())
			g.drawImage(entity.animation.getImage(),rop, (int) (entity.x + entity.xmap - entity.width / 2), (int) (entity.y + entity.ymap - entity.height / 2));
		else
			g.drawImage(entity.animation.getImage(),rop, (int) (entity.x + entity.xmap - entity.width / 2 + entity.width), (int) (entity.y + entity.ymap - entity.height / 2));
		*/
		
		if (entity.isFacingright())
			g.drawImage(entity.animation.getImage(), (int) (entity.x + entity.xmap - entity.width / 2), (int) (entity.y + entity.ymap - entity.height / 2), null);
		else
			g.drawImage(entity.animation.getImage(), (int) (entity.x + entity.xmap - entity.width / 2 + entity.width), (int) (entity.y + entity.ymap - entity.height / 2),
					-entity.width, entity.height, null);
		
	}

}
