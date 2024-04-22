package blocks;

import java.awt.Color;

public abstract class Block {
		
	protected int[][] shape;
	protected Color color;
	protected int color_num;
	protected int item;
	
	public Block() {
		shape = new int[][]{ 
				{1, 1}, 
				{1, 1}
		};
		color = Color.YELLOW;
		color_num = 4;
		item = 0; // 0이면 아이템 모드아님
	}
	
	public int getShape(int x, int y) {
		return shape[y][x];
	}
	public int getBlockNums() {
		int blockNums = 0;
		for (int i = height()-1; i>=0; i-- ) {
			for (int j=width()-1; j>=0;j--) {
				if(shape[i][j]>0) {
					blockNums++;
				}
			}
		}
			
		return blockNums;
	}
	public int[] whereBlock(int w) {
		int[] result = new int[2];
		for (int i = width()-1; i>=0; i-- ) {
			for (int j=height()-1; j>=0;j--) {
					if (shape[i][j]>0) {
						w--;
					}
					if(w==0) {
						result[0]=i;
						result[1]=j;
					}
			}
		}
			
		return result;
	}
	public void changeShape(int[][] shape) {
		this.shape = shape;
	}
	public void changeShapeDetail(int x, int y, int num) {
		this.shape[x][y] = num;
		if (num ==0) {
			this.shape[x][y] = ' ';
		}
	}
	public void changeColor(Color color, int color_num) {
		this.color = color;
		this.color_num = color_num;
	}
	public void changeItem(int item) {
		this.item = item;
	}
	public int[][] getShapeDetail() {
		return shape;
	}
	public Color getColor() {
		return color;
	}
	public int getItem() {
		return item;
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

