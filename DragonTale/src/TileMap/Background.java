package TileMap;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.GameConstants;
import java.awt.Graphics;

public class Background {
	
	private BufferedImage image;
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms)
	{
		try
		{
			image = ImageIO.read(new File(GameConstants.assetBasePath + s));
			
			moveScale = ms;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y)
	{
		this.x = (x * moveScale) % GameConstants.WIDTH;
		this.y = (y * moveScale) % GameConstants.HEIGHT;
	}
	
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	public  void update(long timeDelta)
	{
		x += dx;
		y += dy;
	}
	public void draw(Graphics g)
	{
		g.drawImage(image, (int)x,	(int)y, GameConstants.WIDTH ,GameConstants.HEIGHT,	null);
		if ( x< 0)
		{
			g.drawImage(image, (int)x + GameConstants.WIDTH, (int)y,GameConstants.WIDTH ,GameConstants.HEIGHT,	 null);
		}
		if ( x > 0)
		{
			g.drawImage(image, (int)x - GameConstants.WIDTH, (int)y,GameConstants.WIDTH ,GameConstants.HEIGHT,	 null);
		}
	}
}
