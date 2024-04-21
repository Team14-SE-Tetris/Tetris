package tetris;

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
    private static int createBlockNum = 0;
    private static int level = 1; // 레벨 기본값
    private static int mode = 0; // 기본값 none item mode
    public int nextnum = 0;
    public int liney = 0;
    
    // 생성자
    public Tetris(int level) {
    	this.level = level;
        clearBoard();
        randomBlock();
        dropSpeed = 1_000_000_000;
        score=0;
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
    	dropSpeed = Math.max(100_000_000, dropSpeed - (deleteBar*100*levelSpeed + (createBlockNum/10)*500)); 
    }
    
    //dropSpeed와 score초기화 함수
    public void resetSpeedScore(){
    	dropSpeed = 1_000_000_000;
    	score = 0;
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
    private void placeBlock() {
        for (int y = 0; y < block.height(); y++) {
            for (int x = 0; x < block.width(); x++) {
                if (block.getShape(x, y) == 1) {
                    board[currentY + y][currentX + x] = block.getColorNum();
                }
            }
        }
    }
    
 // 선 지우는 메소드
    public void removeLine(int line) {
        for (int y = line; y > 0; y--) {
            for (int x = 0; x < BoardWidth; x++) {
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
    private int randomBlock() {
    	double basic = 0.14; // 1/7 반올림
    	double[] fitness=  {basic,basic,basic,basic,basic,basic,basic};;
    	if (level == 1) {
    		fitness[0] = 0.168;
    	} else if (level == 3) {
    		fitness[0] = 0.112;
    	}
    	
    	int randomNum = RandomFunction.randomFunction(fitness)+1;
    	
    	switch(randomNum) {
        case 1:
        	nextBlock = new IBlock();
            break;
        case 2:
        	nextBlock = new JBlock();
            break;
        case 3:
        	nextBlock = new LBlock();
            break;
        case 4:
        	nextBlock = new OBlock();
            break;
        case 5:
        	nextBlock = new SBlock();
            break;
        case 6:
        	nextBlock = new TBlock();
            break;
        case 7:
        	nextBlock = new ZBlock();
            break;
        default:
            System.out.print("System Error: randomNum");
            break;
    	}
    	
    	return randomNum;
    	
	}

    // 블럭들 랜덤 생성
    private int randomItemBlock() {
    	double basic = 0.2; // 1/7 반올림
    	double[] fitness=  {basic,basic,basic,basic,basic};;
    	
    	int randomNum = RandomFunction.randomFunction(fitness)+1;
    	int[][] shape;
    	switch(randomNum) {
        case 1: // 한개 블럭 삭제 색 하얀색 블럭크기 1개
        	nextBlock = new IBlock();
        	shape = new int[][] {{1}};
        	nextBlock.changeShape(shape);
        	nextBlock.changeItem();
            break;
        case 2: // 아래 2줄 삭제
        	nextBlock = new JBlock();
        	shape = new int[][] {{1,0,0,1},{1, 1, 1, 1}};
        	nextBlock.changeShape(shape);
        	nextBlock.changeItem();
            break;
        case 3: // 위아래 1줄 삭제
        	nextBlock = new LBlock();
        	shape = new int[][] {{1},{1},{1}};
        	nextBlock.changeShape(shape);
        	nextBlock.changeItem();
            break;
        case 4: // 줄삭제 아이템
        	randomBlock();
        	nextBlock.changeItem();
            break;
        case 5: // 무게추 아이템
        	nextBlock = new SBlock();
        	shape = new int[][] {{1, 1, 1, 1},{1, 1, 1, 1}};
        	nextBlock.changeShape(shape);
        	nextBlock.changeItem();
            break;
        default:
            System.out.print("System Error: randomNum");
            break;
    	}
    	
    	return randomNum;
    	
	}
    
    private void increaseDropScore() { // 떨어질때 점수추가
    	score += 100;
    	score += (deleteBar+createBlockNum/5)*100; // 증가된 속도만큼 주가점수
    	
    }
    
    // 블럭 생성 위치 지정
    public boolean initialiBlock() {
    	block = nextBlock;
    	createBlockNum++;
    	randomBlock();
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
    	increaseDropScore(); // 점수 증가
    	adjustDropSpeed();
    	if (moveBlock(0,1)) {
    		return true;
    	}
    	else {
    		placeBlock();
    		checkLines();
    		currentX = BoardWidth / 2;
    		currentY = 0;
    		return false;
    	}
  
    }
    // 쭉내리기
    public boolean moveBottom() {
    	
    	boolean l = true;
    	
    	while(l) {
    		l = moveDown();
    	}
    		
    	return false;
    }
    
    // 블럭 왼쪽으로 이동
    public boolean moveLeft() {
    	if (moveBlock(-1,0)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    // 블럭 오른쪽으로 이동
    public boolean moveRight() {
    	if (moveBlock(1,0)) {
    		return true;
    	}
    	else {
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
                if (block.getShape(x, y) == 1) {
                	printBoard[currentY + y + 1][currentX + x + 1] = block.getColorNum();
                }
            }
        }
    	return printBoard;
    	
    }
    
    // score 출력
    public int getScore() {
		return score;
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
    
 // Level 변경
    public void changeLevel(int level) {
    	this.level = level;
	}
    
}
