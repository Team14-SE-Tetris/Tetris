package tetris;

import java.util.concurrent.ThreadLocalRandom;

import blocks.*;

public class TetrisBoard {
	private static final int BoardWidth = 10;
    private static final int BoardHeight = 20;
    private static int[][] board = new int[BoardHeight][BoardWidth];
    private static int currentX = BoardWidth / 2; // 현재 블록의 X 위치
    private static int currentY = 0; // 현재 블록의 Y 위치
    private static int score = 0;
    private static int dropSpeed = 1000;
    private static Block block;
    private static Block preBlock;
    
    
    // 생성자
    public TetrisBoard() {
        clearBoard();
        randomBlock();
    }
    
    
    // 보드 초기화
    private void clearBoard() {
    	
        for (int y = 0; y < BoardHeight; y++) {
            for (int x = 0; x < BoardWidth; x++) {
                board[y][x] = 0;
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

                    if (board[newY][newX] != 0) {
                        return true; // 이미 채워진 공간으로 이동하는 경우
                    }
                }
            }
        }
        return false;
    }
    
    // 최소 속도는 200ms로 제한 임의로 5번 내려올때마다 1ms씩 빨라지도록 설정 1000 은 1초임
    private void adjustDropSpeed() {
    	dropSpeed = Math.max(200, 1000 - (score/500)); 
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
    private void checkLines() {
        for (int y = BoardHeight - 1; y >= 0; y--) {
            boolean lineComplete = true;
            for (int x = 0; x < BoardWidth; x++) {
            	 if (board[y][x] == 0 ) {
                    lineComplete = false;
                    break;
                }
            }
            if (lineComplete) {
                // 해당 줄 삭제 및 위에 있는 줄을 아래로 내림
            	removeLine(y);
                y++; // 삭제된 줄 아래에서부터 다시 검사하기 위해 y 값을 조정
            }
        }
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
    private void removeLine(int line) {
        for (int y = line; y > 0; y--) {
            for (int x = 0; x < BoardWidth; x++) {
                board[y][x] = board[y - 1][x];
            }
        }
        // 가장 윗 줄은 비워야 하므로 초기화
        for (int x = 0; x < BoardWidth; x++) {
            board[0][x] = 0;
        }
    }
    
    // 블럭들 랜덤 생성
    private void randomBlock() {
    	int randomNum = ThreadLocalRandom.current().nextInt(1, 8);
    	
    	switch(randomNum) {
        case 1:
        	preBlock = new IBlock();
            break;
        case 2:
        	preBlock = new JBlock();
            break;
        case 3:
        	preBlock = new LBlock();
            break;
        case 4:
        	preBlock = new OBlock();
            break;
        case 5:
        	preBlock = new SBlock();
            break;
        case 6:
        	preBlock = new TBlock();
            break;
        case 7:
        	preBlock = new ZBlock();
            break;
        default:
            System.out.print("System Error: randomNum");
            break;
    	}
    	
	}
    
    
    // 블럭 생성 위치 지정
    public boolean initialiBlock() {
    	block = preBlock;
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
    	score += 100; // 점수 증가
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
    	int[][] printBoard = new int[BoardHeight][BoardWidth];
    	for (int y = 0; y < BoardHeight; y++) {
            for (int x = 0; x < BoardWidth; x++) {
            	printBoard[y][x] = board[y][x];
            }
        }
    	for (int y = 0; y < block.height(); y++) {
            for (int x = 0; x < block.width(); x++) {
                if (block.getShape(x, y) == 1) {
                	printBoard[currentY + y][currentX + x] = block.getColorNum();
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
    public Block getPreBlock() {
		return preBlock;
	}
    
}
