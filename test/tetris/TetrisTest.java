package tetris;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import blocks.Block;

public class TetrisTest {
    private Tetris tetris;

    @Before
    public void setUp() throws Exception {
        tetris = new Tetris(2); // level 2로 테트리스 게임 시작
    }
    
    
    @Test
  public void testRandom2Block() {
      int totalRuns = 10000;
      int[] counts = new int[7];
      for (int i = 0; i < totalRuns; i++) {
      	tetris.randomBlock();
          int selected = tetris.nextBlock.getColorNum()-1;
          counts[selected]++;
      }

      // 각 인덱스가 선택될 확률이 fitness에 비례하는지 확인
      for (int i = 0; i < counts.length; i++) {
          double expected = totalRuns/7;
          double actual = counts[i];
          double tolerance = totalRuns * 0.05; // 5%의 허용 오차
          assertTrue(Math.abs(expected - actual) < tolerance,
                  "Index " + i + " selected " + actual + " times, but expected " + expected);
      }
     }
    
    @Test
    public void testRandom1Block() {
  	  tetris.changeLevel(1);
  	  int totalRuns = 10000;
        int[] counts = new int[7];
  	  
        for (int i = 0; i < totalRuns; i++) {
          	tetris.randomBlock();
              int selected = tetris.nextBlock.getColorNum()-1;
              counts[selected]++;
          }

          // 각 인덱스가 선택될 확률이 fitness에 비례하는지 확인
          for (int i = 0; i < counts.length; i++) {
              double expected = totalRuns/7.2;
              double actual = counts[i];
              
              if (i==0) {
              	expected = expected*1.2;
              }
              double tolerance = totalRuns * 0.05; // 5%의 허용 오차
              assertTrue(Math.abs(expected - actual) < tolerance,
                      "Index " + i + " selected " + actual + " times, but expected " + expected);
          }
    }
    @Test
    public void testRandom3Block() {
  	  tetris.changeLevel(3);
  	  int totalRuns = 10000;
        int[] counts = new int[7];
  	  
        for (int i = 0; i < totalRuns; i++) {
          	tetris.randomBlock();
              int selected = tetris.nextBlock.getColorNum()-1;
              counts[selected]++;
          }

          // 각 인덱스가 선택될 확률이 fitness에 비례하는지 확인
          for (int i = 0; i < counts.length; i++) {
              double expected = totalRuns/6.8;
              double actual = counts[i];
              
              if (i==0) {
              	expected = expected*0.8;
              }
              double tolerance = totalRuns * 0.05; // 5%의 허용 오차
              assertTrue(Math.abs(expected - actual) < tolerance,
                      "Index " + i + " selected " + actual + " times, but expected " + expected);
          }
    }
    
    @Test
    public void testrandomItemBlock() {
    	tetris.changeLevel(2);
        int totalRuns = 10000;
        int[] counts = new int[5];
        for (int i = 0; i < totalRuns; i++) {
        	tetris.randomBlock();
        	tetris.randomItemBlock();
            int selected = tetris.nextBlock.getItem()-1;
            counts[selected]++;
        }

        // 각 인덱스가 선택될 확률이 fitness에 비례하는지 확인
        for (int i = 0; i < counts.length; i++) {
            double expected = totalRuns/5;
            double actual = counts[i];
            double tolerance = totalRuns * 0.05; // 5%의 허용 오차
            assertTrue(Math.abs(expected - actual) < tolerance,
                    "Index " + i + " selected " + actual + " times, but expected " + expected);
        }
       }
      
  
    
    @Test
    public void testClearBoard() {
        tetris.clearBoard(); // 보드 초기화
        for (int y = 0; y < tetris.BoardHeight; y++) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
                assertEquals("보드가 올바르게 초기화되어야 합니다.", ' ', tetris.board[y][x]);
            }
        }
    }

    @Test
    public void testAdjustDropSpeed() {
        tetris.adjustDropSpeed();
        assertTrue("드롭 속도가 올바르게 조정되어야 합니다.", tetris.dropSpeed <= 1000000000 && tetris.dropSpeed >= 1000000);
        tetris.changeLevel(2);
        assertTrue("드롭 속도가 올바르게 조정되어야 합니다.", tetris.dropSpeed <= 1000000000 && tetris.dropSpeed >= 1000000);
        tetris.changeLevel(3);
        assertTrue("드롭 속도가 올바르게 조정되어야 합니다.", tetris.dropSpeed <= 1000000000 && tetris.dropSpeed >= 1000000);
    
    }
    
    @Test
    public void testCheckCollisionBottom() {
        tetris.initialiBlock();
        assertTrue("왼쪽", tetris.checkCollisionBottom(-1,0));
        assertTrue("왼쪽", tetris.checkCollisionBottom(10,0));
        assertTrue("아래쪽", tetris.checkCollisionBottom(0,20));
        assertTrue("위쪽", tetris.checkCollisionBottom(0,-1));
        
        assertTrue("none", !(tetris.checkCollisionBottom(5,5)));
    }
    @Test
    public void moveBlock() {
        tetris.initialiBlock();
        assertTrue("왼쪽", tetris.checkCollision(-1,0));
        assertTrue("왼쪽", tetris.checkCollision(10,0));
        assertTrue("아래쪽", tetris.checkCollision(0,20));
        assertTrue("위쪽", tetris.checkCollision(0,-1));
        assertTrue("false", !(tetris.checkCollision(5,5)));
    }
    
    @Test
    public void testMoveBlock() {
        // 가정: currentX = 5, currentY = 5, deltaX = 1, deltaY = 0, checkCollision()이 false를 반환하는 경우
        
        tetris.currentX=5;
        tetris.currentY=5;
        boolean result = tetris.moveBlock(1, 0);
        assertTrue(result);
        assertEquals(6, tetris.getCurrentX());
        assertEquals(5, tetris.getCurrentY());
    }


    @Test
    public void testCheckLines() {
        // 가정: board에 완성된 줄이 있고, 그 줄의 y 인덱스가 10이라고 가정
        int expectedLineY = 10;
        for (int x = 0; x < tetris.BoardWidth; x++) {
        	tetris.board[expectedLineY][x]=1;
        }

        // board 설정 코드 필요, 예를 들어, tetris.setBoard(...) 등을 통해
        int resultLineY = tetris.checkLines();
        assertEquals(expectedLineY, resultLineY);
    }
    
    
    @Test
    public void testPlaceBlock() {
    	tetris.initialiBlock();
    	tetris.currentX = 5;
    	tetris.currentY = 5;
        
        // 블록 배치
    	tetris.placeBlock();
        
        // 기대 결과 검증
        for (int y = 0; y < tetris.block.height(); y++) {
            for (int x = 0; x < tetris.block.width(); x++) {
                if (tetris.block.getShape(x, y) > 0) {
                    assertEquals("블록이 올바르게 배치되어야 합니다.", tetris.block.getColorNum(), tetris.board[tetris.currentY + y][tetris.currentX + x]);
                }
            }
        }
    }

    @Test
    public void testRemoveLine() {
        // 초기 보드 설정 (특정 줄이 완성되었다고 가정)
        for (int x = 0; x < tetris.BoardWidth; x++) {
        	tetris.board[1][x] = 1; // 'X'는 블록이 채워진 부분을 나타냄
        }
        
        // 선 지우기
        tetris.removeLine(1);
        
        // 기대 결과 검증
        for (int x = 0; x < tetris.BoardWidth; x++) {
            assertEquals("지워진 줄 위의 모든 줄이 한 칸씩 내려와야 합니다.", ' ', tetris.board[1][x]);
        }
        
        // 가장 윗 줄이 비어 있는지 확인
        for (int x = 0; x < tetris.BoardWidth; x++) {
            assertEquals("가장 윗 줄은 비워져야 합니다.", ' ', tetris.board[0][x]);
        }
    }
    
    @Test
    public void testAdjustScore() {
        tetris.dropSpeed = 500_000_000;
        tetris.deleteItem = 2; // 삭제된 아이템 수
        tetris.dropBlocks = 5; // 떨어진 블럭 수
        tetris.deleteBar = 3; // 삭제된 라인 수
        tetris.adjustDropSpeed();
        // 예상되는 점수 계산
        int expectedScore = (1_000_000_000 - tetris.dropSpeed) / 1000 + tetris.deleteItem * 100 + tetris.dropBlocks * 10 + tetris.deleteBar * 100;

        // adjustScore 메서드 실행
        tetris.adjustScore();

        // 실제 결과와 예상 결과 비교
        assertEquals("점수 계산이 올바르지 않습니다.", expectedScore, tetris.score);
    }

    @Test
    public void testRotateBlockWithoutCollision() {
        // 이 테스트는 충돌 없이 블록을 회전할 수 있는 경우를 검사합니다.
        // 충돌을 피하기 위해 특정 위치와 상태를 설정해야 할 수 있습니다.
        tetris.initialiBlock();
        tetris.currentX = 5;
    	tetris.currentY = 5;
        boolean result = tetris.rotateBlock();
        assertTrue("블록이 충돌 없이 회전해야 한다.", result);
    }
    
    @Test
    public void testRotateBlockWithCollision() {
        // 이 테스트는 충돌 없이 블록을 회전할 수 있는 경우를 검사합니다.
        // 충돌을 피하기 위해 특정 위치와 상태를 설정해야 할 수 있습니다.
        tetris.initialiBlock();
        tetris.currentX = 0;
    	tetris.currentY = -1;
        assertTrue("블록이 충돌이 있어야 한다.", !tetris.rotateBlock());
    }
    
    @Test
    public void testMoveDown() {
    	tetris.initialiBlock();
    	tetris.currentX = 5;
        tetris.currentY = 0;
        tetris.moveDown();
        assertEquals(tetris.currentY,1); // 블록을 아래로 성공적으로 이동할 수 있는지 확인
        tetris.clearBoard();
        tetris.currentX = 5;
        tetris.currentY = 20-tetris.block.height();
        tetris.clearBoard();
        assertTrue(!tetris.moveDown());
        tetris.changeMode(1);
        tetris.block.changeItem(2);
        tetris.currentX = 5;
        tetris.currentY = 20-tetris.block.height();
        tetris.moveDown();
        assertEquals("블록이 제거됨.",tetris.board[19][5],' ');
        
    }
    
    @Test
    public void testMoveBottomSuccess() {
    	tetris.initialiBlock();
    	tetris.clearBoard();
    	tetris.currentX = 5;
        tetris.currentY = 0;
        tetris.moveBottom();
        assertEquals(tetris.currentY+tetris.block.height(),20); // 블록을 아래로 성공적으로 이동할 수 있는지 확인
        tetris.currentX = 5;
        tetris.currentY = 0;
        tetris.changeMode(1);
        tetris.block.changeItem(5);
        tetris.moveBottom();
        assertEquals(tetris.currentY+tetris.block.height(),20);
    }
    @Test
    public void testMoveRightSuccess() {
    	tetris.initialiBlock();
    	tetris.currentX = 5;
        tetris.currentY = 0;
        tetris.moveRight();
        assertEquals(tetris.currentX,6); // 블록을 아래로 성공적으로 이동할 수 있는지 확인
        tetris.currentX = 9;
        tetris.currentY = 0;
        assertTrue(!(tetris.moveRight()));
        tetris.currentX = 9;
        tetris.currentY = 0;
        tetris.heavyFlag= true;
        tetris.changeMode(1);
        tetris.block.changeItem(5);
        assertTrue(!(tetris.moveRight()));
    }
    
    @Test
    public void testMoveLeft() {
    	tetris.initialiBlock();
    	tetris.currentX = 5;
        tetris.currentY = 0;
        tetris.moveLeft();
        assertEquals(tetris.currentX,4); // 블록을 아래로 성공적으로 이동할 수 있는지 확인
        tetris.currentX = 0;
        tetris.currentY = 0;
        assertTrue(!(tetris.moveLeft()));
        tetris.currentX = 0;
        tetris.currentY = 0;
        tetris.heavyFlag= true;
        tetris.changeMode(1);
        tetris.block.changeItem(5);
        assertTrue(!(tetris.moveLeft()));
    }
   



}
