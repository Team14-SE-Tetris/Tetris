package blocks;

import java.awt.Color;

public abstract class Block {
		
	protected int[][] shape;
	protected Color color;
	protected int color_num;
	
	public Block() {
		shape = new int[][]{ 
				{1, 1}, 
				{1, 1}
		};
		color = Color.YELLOW;
		color_num = 4;
	}
	
	public int getShape(int x, int y) {
		return shape[y][x];
	}
	
	public Color getColor() {
		return color;
	}
	public int getColorNum() {
		return color_num;
	}
	
	 public void rotate() {
	        // Rotate the block 90 degrees clockwise.
	        int[][] rotatedShape = new int[shape[0].length][shape.length];
	        for(int y = 0; y < shape.length; y++) {
	            for(int x = 0; x < shape[y].length; x++) {
	                rotatedShape[x][shape.length - 1 - y] = shape[y][x];
	            }
	        }
	        shape = rotatedShape;
	}
	
	public int height() {
		return shape.length;
	}
	
	public int width() {
		if(shape.length > 0)
			return shape[0].length;
		return 0;
	}
}

