package TileMap;


import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.Gameplay;

import java.io.InputStream;

public class TileMap {

	// position
	private float x;
	private float y;

	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	private float tween; // smoothly scrolling camera

	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;

	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = Gameplay.HEIGHT / tileSize + 2;
		numColsToDraw = Gameplay.WIDTH / tileSize + 2;
		tween = 0.07f;
	}

	public void loadTiles(String s) {
		try {
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];

			BufferedImage subimage;
			for (int i = 0; i < numTilesAcross; i++) {
				subimage = tileset.getSubimage(i * tileSize, 0, tileSize, tileSize);
				tiles[0][i] = new Tile(subimage, Tile.NORMAL);

				subimage = tileset.getSubimage(i * tileSize, tileSize, tileSize, tileSize);
				tiles[1][i] = new Tile(subimage, Tile.BLOCKED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String s) {
		try {
			InputStream in = (InputStream) getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;

			xmin = Gameplay.WIDTH - width;
			xmax = 0;
			
			ymin = Gameplay.HEIGHT - height;
			ymax = 0;
							
			String delims = "\\s+";
			for (int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
					}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getTileSize() {
		return tileSize;
	}

	public float getx() {
		return x;
	}

	public float gety() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setTween(float t)
	{
		tween = t;
	}
	
	public int getNumRows() { return numRows; }
	
	public int getNumCols() { return numCols; }

	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}

	public void initPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setPosition(float x, float y) {
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;

		fixBounds();

		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
		
	}

	private void fixBounds() {
		if (x < xmin)
			x = xmin;
		if (y < ymin)
			y = ymin;
		if (x > xmax)
			x = xmax;
		if (y > ymax)
			y = ymax;
		
	}

	public void draw(Graphics2D g) {
		
		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) 
		{
			if (row >= numRows)
				break;
			for (int col = colOffset; col < colOffset + numColsToDraw; col++) 
			{
				if (col >= numCols)
					break;

				if (map[row][col] == 0)
					continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
	
				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
			}
		}
	}

}
