package tetris;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import tetris.RandomFunction;

import blocks.*;

public class Tetris {
    private static final int BoardWidth = 10;
    private static final int BoardHeight = 20;
    private static int[][] board = new int[BoardHeight][BoardWidth];
    private static int currentX = BoardWidth / 2; // 현재 블록의 X 위치
    private static int currentY = 0; // 현재 블록의 Y 위치
    private static int score = 0;
    private static int dropSpeed = 1_000_000_000;
    private static Block block;
    private static Block nextBlock;
    private static int deleteBar = 0;
    private static int dropBlocks = 0;
    private static int deleteItem = 0;
    private static int itemBar = 1;
    private static int createBlockNum = 0;
    private int level = 1; // 레벨 기본값
    private static int mode = 0; // 기본값 none item mode
    public static int nextnum = 0;
    public static int liney = 0;
    private static int randomDelete_3 = 0;
    private static int randomDelete_4 = 0;
    private boolean heavyFlag = true;
    
    // 생성자
    public Tetris(int level) {
    	this.level = level;
        clearBoard();
        randomBlock();
        resetVariable();
    }
    
    
    // 보드 초기화 
    private void clearBoard() {
    	
        for (int y = 0; y < BoardHeight; y++) {
            for (int x = 0; x < BoardWidth; x++) {
                board[y][x] = ' ';
            }
        }
    }
    
    // 충돌 점검 (바닥에 닫는 것도 포함함)
    private boolean checkCollision(int posX, int posY) {
        for (int y = 0; y < block.height(); y++) {
            for (int x = 0; x < block.width(); x++) {
                if (block.getShape(x, y) != 0) {
                    int newX = posX + x;
                    int newY = posY + y;

                    if (newX < 0 || newX >= BoardWidth || newY < 0 || newY >= BoardHeight) {
                        return true; // 보드 바깥으로 나가는 경우
                    }

                    if (board[newY][newX] != ' ') {
                        return true; // 이미 채워진 공간으로 이동하는 경우
                    }
                }
            }
        }
        return false;
    }
    
    public boolean checkCollisionBottom(int posX, int posY) {
        for (int y = 0; y < block.height(); y++) {
            for (int x = 0; x < block.width(); x++) {
                if (block.getShape(x, y) != 0) {
                    int newX = posX + x;
                    int newY = posY + y;

                    if (newX < 0 || newX >= BoardWidth || newY < 0 || newY >= BoardHeight) {
                        return true; // 보드 바깥으로 나가는 경우
                    }

                }
            }
        }
        return false;
    }
    
 // 최소 속도는 200ms로 제한 임의로 1번 내려올때마다 0.1ms씩 빨라지도록 설정 1_000_000_000 은 1초임
    private void adjustDropSpeed() {
    	int levelSpeed = 10; // default normal
    	if (level == 1) {
    		levelSpeed = 8;
    	}
    	else if (level == 2) {
    		levelSpeed = 10;
    	}
    	else if (level == 3) {
    		levelSpeed = 12;
    	}
    	dropSpeed = Math.max(1_000_000,1_000_000_000 - (deleteBar*100*levelSpeed + (createBlockNum/10)*500)); 
    	
    }
    
    //dropSpeed와 score초기화 함수
    public void resetVariable(){
    	dropSpeed = 1_000_000_000;
    	score = 0;
    	itemBar = 1;
    	deleteBar = 0;
    	createBlockNum = 0;
    	deleteItem = 0;
    	dropBlocks = 0;	
    }
    
    // 블럭 이동
    private boolean moveBlock(int deltaX, int deltaY) {
        int newX = currentX + deltaX;
        int newY = currentY + deltaY;

        if (!checkCollision(newX, newY)) {
        	currentX = newX;
        	currentY = newY;
            return true;
        }
        return false;
    }
    
    public boolean checkBlock() {
        int newX = currentX + 0;
        int newY = currentY + 1;

        if (!checkCollision(newX, newY)) {
            return true;
        }
        return false;
    }
  
    // 완성된 줄 있는지 확인후 제거
    public int checkLines() {
        for (int y = BoardHeight - 1; y >= 0; y--) {
            boolean lineComplete = true;
            for (int x = 0; x < BoardWidth; x++) {
            	 if (board[y][x] == ' ' ) {
                    lineComplete = false;
                    break;
                }
            }
                 
            if (lineComplete) {
            	liney = y;
            	break;
            	
            	// 해당 줄 삭제 및 위에 있는 줄을 아래로 내림
            	//removeLine(y);
                //y++; // 삭제된 줄 아래에서부터 다시 검사하기 위해 y 값을 조정
            }
        }
        
        return liney;
        
    }
    
    // 보드에 블록을 그리는 메소드
    public void placeBlock() {
        for (int y = 0; y < block.height(); y++) {
            for (int x = 0; x < block.width(); x++) {
                if (block.getShape(x, y)> 0) {
                    board[currentY + y][currentX + x] = block.getColorNum();
                }
            }
        }
    }
    
 // 선 지우는 메소드
    public void removeLine(int line) {
    	deleteBar++;
    	System.out.print("delte");
        for (int y = line; y > 0; y--) {
            for (int x = 0; x < BoardWidth; x++) {
            	if (board[line][x]==9) {
            		deleteItem++;
            	}
                board[y][x] = board[y - 1][x];
            }
        }
        // 가장 윗 줄은 비워야 하므로 초기화
        for (int x = 0; x < BoardWidth; x++) {
            board[0][x] = ' ';
        }
        
        liney = 0;
    }
    
    // 블럭들 랜덤 생성
    private void randomBlock() {
    	double basic = 0.14; // 1/7 반올림
    	double[] fitness=  {basic,basic,basic,basic,basic,basic,basic};
    	if (level == 1) {
    		fitness[0] = 0.168;
    	} else if (level == 3) {
    		fitness[0] = 0.112;
    	}
    	
    	int randomNum = RandomFunction.randomFunction(fitness);
    	
    	switch(randomNum) {
        case 0:
        	nextBlock = new IBlock();
            break;
        case 1:
        	nextBlock = new JBlock();
            break;
        case 2:
        	nextBlock = new LBlock();
            break;
        case 3:
        	nextBlock = new OBlock();
            break;
        case 4:
        	nextBlock = new SBlock();
            break;
        case 5:
        	nextBlock = new TBlock();
            break;
        case 6:
        	nextBlock = new ZBlock();
            break;
        default:
            System.out.print("System Error: randomNum");
            break;
    	}
    	
    	
	}

    // 블럭들 랜덤 생성
    private void randomItemBlock() {
    	double basic = 0.14; // 1/7 반올림
    	double[] fitness=  {basic,basic,basic,basic,basic};
    	int randomNum = 0;
    	randomNum = RandomFunction.randomFunction(fitness);
    	int[][] shape;
    	switch(randomNum) {
        case 0: // 해당 블럭이 지워진 줄이 2배
        	shape = new int[][] {{8}};
        	nextBlock.changeShape(shape);
        	nextBlock.changeItem(1);
        	nextBlock.changeColor(Color.WHITE,9);
            break;
        case 1: // 폭탄
        	shape = new int[][] {{1, 1},{1, 1}};
        	nextBlock.changeShape(shape);
        	nextBlock.changeItem(2);
        	nextBlock.changeColor(Color.WHITE,10);
            break;
        case 2: // 1줄 랜덤 삭제
        	shape = new int[][] {{1},{1},{1}};
    		double[] fitness_3=  {basic,basic,basic};
        	randomDelete_3 = RandomFunction.randomFunction(fitness_3);
        	nextBlock.changeShape(shape);
        	nextBlock.changeItem(3);
        	nextBlock.changeColor(Color.WHITE,12);
            break;
        case 3: // 줄삭제 아이템
        	int block_nums= nextBlock.getBlockNums();
        	double[] fitness_4=  new double[block_nums];
        	for(int i =0;i<block_nums;i++) {
        		fitness_4[i]=1;
        	}
        	randomDelete_4 = RandomFunction.randomFunction(fitness_4);
        	int[] where = nextBlock.whereBlock(randomDelete_4);
        	nextBlock.changeShapeDetail(where[0],where[1],11);
        	nextBlock.changeItem(4);
        	
            break;
        case 4: // 무게추 아이템
        	shape = new int[][] {{12, 12, 12, 12},{12, 12, 12, 12}};
        	nextBlock.changeShape(shape);
        	nextBlock.changeItem(5);
        	nextBlock.changeColor(Color.WHITE,13);
            break;
        default:
            System.out.print("System Error: randomNum"+randomNum);
            break;
    	}
    	
    	
	}
    
    private void adjustScore() { // 떨어질때 점수추가 추가적으로 속도와 연관된 deleteBar, createBlockNum을 통해 속도와 점수관 연관시킴
    	adjustDropSpeed();
    	score = (1_000_000_000-dropSpeed)/1000+deleteItem*100+dropBlocks*10+deleteBar*100;
    }
    
    // 블럭 생성 위치 지정
    public boolean initialiBlock() {
    	block = nextBlock;
    	heavyFlag = true;
    	
    	createBlockNum++;
    	randomBlock();
    	 System.out.print("initial");
    	if(mode==1 && itemBar%2==1) {
    		randomItemBlock();
    		
    	}
    	currentX = BoardWidth / 2; // 블록을 중앙 상단에 위치
        currentY = 0; // 블록을 게임 보드 상단에 위치
        if(checkCollision(currentX,currentY)) {
        	System.out.println("Game Over!");
        	return false;
        }
        return true;
    }
    
    
    // 블럭 시계방향 90도 회전
    public boolean rotateBlock() {
        block.rotate();
        if (checkCollision(currentX, currentY)) {
            block.rotate(); // 원래 상태로 되돌림
            block.rotate();
            block.rotate();
            return false;
        }
        return true;
    }
    
    // 블럭 아래로 이동
    public boolean moveDown() {
    	adjustScore(); // 점수 증가
    	adjustDropSpeed();
    	if (block.getItem()!=5) {
	    	if (moveBlock(0,1)) {
	    		dropBlocks++;
	    		return true;
	    	}
	    	else {
	    		int it = block.getItem();
	    		if(mode==1&&it>1) {
	    			switch(it) {
	    			case 2: // 주변 블럭 삭제
	    				
	    				for (int y = 0; y < block.height(); y++) {
	    		            for (int x = 0; x < block.width(); x++) {
	    		            	for (int i = -1; i < 2; i++) {
	    	    		            for (int j = -1; j < 2; j++){ 
	    	    		            	int newX = currentX + x + i;
	    			                    int newY = currentY + y + j;
	
	    			                    if (!(newX < 0 || newX >= BoardWidth || newY < 0 || newY >= BoardHeight)) {
	    			                    	board[newY][newX]=' ';
	    			                    }
	
	    	    		            }
	    	    		        }
	    		                
	    		            }
	    		        }
	    				break;
	    			case 3: // 1줄 랜덤 삭제
	    				for (int x = 0; x < BoardWidth; x++) {
	    					if (block.height()>1) {
	    						board[currentY+randomDelete_3][x] = 10;
	    					}
	    					else {
				                board[currentY][x] = 10;
	    					}
			            }
	    				break;
	    			case 4: // 1줄 삭제
	    				int[] where_4 = block.whereBlock(randomDelete_4);
	    				for (int x = 0; x < BoardWidth; x++) {
			                board[where_4[1]+currentY][x] = 10;
			            }
	    				block.changeShapeDetail(where_4[0],where_4[1],0);
	    				placeBlock();
	    				break;
	    			default:
	    				break;
	    			}
	    		}
	    		else {
	    			placeBlock();
	    		}
	    		return false;
	    	}
    	}
    	else {
    		if (moveBlock(0,1)) {
	    		dropBlocks++;
	    		return true;
	    	}
    		else if (!checkCollisionBottom(currentX, currentY+1)) {
    			for (int y = 0; y < block.height(); y++) {
		            for (int x = 0; x < block.width(); x++) {
		            	int newX = currentX + x;
	                    int newY = currentY + y+1;
	                    board[newY][newX]=' ';
		            }
		        }
    			moveBlock(0,1);
    			dropBlocks++;
	    		return true;
	    		
    		} else {
    			placeBlock();
        		currentX = BoardWidth / 2;
        		currentY = 0;
        		return false;
    		}
    		
    	}
  
    }
    // 쭉내리기
    public void moveBottom() {
    	
    	while(checkBlock()) {
    		moveDown();
    	}
    	
    	while(block.getItem()==5) {
    		
    		if (checkCollisionBottom(currentX, currentY+1)) {
    			break;
    		}
    		
    		moveDown();
    	}
    		
    }
    
    // 블럭 왼쪽으로 이동
    public boolean moveLeft() {
    	
    	if(mode==1 && block.getItem()==5) {
    		if(heavyFlag) {
    			if (moveBlock(-1,0)) {
    				return true;
    			}else {
    				heavyFlag = false;
    	    		return false;
    	    	}
    		} else {
    			return false;
    		}
    	} else if (moveBlock(-1,0)) {
    		
    		return true;
    	}
    	else {
    		
    		return false;
    	}
    }
    
    // 블럭 오른쪽으로 이동
    public boolean moveRight() {
    	if(mode==1 && block.getItem()==5) {
    		if(heavyFlag) {
    			if (moveBlock(1,0)) {
    				return true;
    			}else {
    				heavyFlag = false;
    	    		return false;
    	    	}
    		} else {
    			return false;
    		}
    	} else if (moveBlock(1,0)) {
    		return true;
    	}
    	else {
    		heavyFlag = false;
    		return false;
    	}
    }
    
    // 현재 현황판 출력
    public int[][] boardPrint() {
    	int[][] printBoard = new int[BoardHeight+2][BoardWidth+2];
    	for (int y = 0; y < BoardHeight; y++) {
            for (int x = 0; x < BoardWidth; x++) {
            	printBoard[y+1][x+1] = board[y][x];
            }
        }
    	for (int y = 0; y < block.height(); y++) {
            for (int x = 0; x < block.width(); x++) {
                if (block.getShape(x, y) > 0) {
                	printBoard[currentY + y + 1][currentX + x + 1] = block.getColorNum();
                	
                }
                if(mode==1&&block.getItem()==4&&block.getShape(x, y)==11) {
            		printBoard[currentY + y + 1][currentX + x + 1] = 11;
            		
            	}
            }
        }
    	
    	return printBoard;
    	
    }
    
    // score 출력
    public int getScore() {
    	adjustScore();
		return score;
	}
    public int changeMode(int mode) {
		return this.mode=mode;
	}
    public int getMode() {
		return this.mode=mode;
	}
    // 블록의 현재 x 좌표
    public int getCurrentY() {
		return currentY;
	}
    
    // 블록의 현재 Y좌표
    public int getCurrentX() {
		return currentX;
	}
    
    // 현재 속도
    public int getDropSpeed() {
		return dropSpeed;
	}
    
    // preBlock 출력
    public Block getNextBlock() {
		return nextBlock;
	}
    
    public Block getCurrentBlock() {
		return block;
	}
    
 // Level 변경
    public void changeLevel(int level) {
    	this.level = level;
	}
    
}
