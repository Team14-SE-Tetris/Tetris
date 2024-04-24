package blocks;

import java.awt.Color;

public abstract class Block {
		
	protected int[][] shape;
	protected Color color;
	protected int color_num;
	protected int item;
	//item 변수 = 현재 블럭의 아이템 종류
  	//0 : 평범한 블럭
  	//1 : 해당 블럭이 지워진 줄이 2배
  	//2 : 폭탄
  	//3 : 1줄 랜덤 삭제
  	//4 : 줄삭제 아이템
  	//5 : 무게추 아이템
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
		for (int i = 0; i<width(); i++ ) {
			for (int j=0; j<height();j++) {
				if(shape[j][i]!=0) {
					blockNums++;
				}
			}
		}
			
		return blockNums;
	}
	public int[] whereBlock(int w) {
		int[] result = new int[2];
		for (int i = 0; i<width(); i++ ) {
			for (int j=0; j<height();j++) {
					if (shape[j][i]>0) {
						if(w==0) {
							result[0]=i;
							result[1]=j;
						} else {
							w--;
						}
						
					} 
			}
		}
			
		return result;
	}
	//현재 블록의 오오른쪽 아래에서 시작하여 왼쪽 위로 올라가면서 n번째로 비어있지 않은 블럭위 위치를 반환
	public void changeShape(int[][] shape) {
		this.shape = shape;
	}
	public void changeShapeDetail(int x, int y, int num) {
		shape[y][x] = num;
		if (num ==0) {
			shape[y][x] = ' ';
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

