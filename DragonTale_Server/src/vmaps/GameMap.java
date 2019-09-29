package vmaps;



import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import game.GameConstants;

import java.io.InputStream;


public class GameMap {

	// position


	// map
	
	private int[][] map;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	public Cell[][] grid;
	private int tileSize;


	
	public GameMap() {
	}

	public void loadTiles(String s) {
		try {
			tileSize = GameConstants.TILESIZE;
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / GameConstants.TILESIZE;
			tiles = new Tile[2][numTilesAcross];

			BufferedImage subimage;
			for (int i = 0; i < numTilesAcross; i++) {
				subimage = tileset.getSubimage(i * GameConstants.TILESIZE, 0, GameConstants.TILESIZE, GameConstants.TILESIZE);
				tiles[0][i] = new Tile(subimage, Tile.NORMAL);

				subimage = tileset.getSubimage(i * GameConstants.TILESIZE, GameConstants.TILESIZE, GameConstants.TILESIZE, GameConstants.TILESIZE);
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
			map = new int[numRows][numCols]; //tiles
			width = numCols * GameConstants.TILESIZE;
			height = numRows * GameConstants.TILESIZE;
			
			grid = new Cell[(width/GameConstants.WIDTH)+1][(height/GameConstants.HEIGHT)+1];
			
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


	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getNumRows() { return numRows; }
	
	public int getNumCols() { return numCols; }

	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}

	public int getTileSize() {
		// TODO Auto-generated method stub
		return tileSize;
	}

	public Cell getCell(int cell_x, int cell_y) {
		// create the cell if needed
		if (grid[cell_x][cell_y] == null) {
			grid[cell_x][cell_y] = new Cell(cell_x, cell_y);
		}
		// TODO Auto-generated method stub
		return grid[cell_x][cell_y];
	}


	

}
