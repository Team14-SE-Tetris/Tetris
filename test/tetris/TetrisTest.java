package tetris;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tetris.Tetris;
import blocks.Block;

public class TetrisTest  {

    private Tetris tetris;

    @BeforeEach
    public void setUp() {
        tetris = new Tetris(1);
    }

    @Test
    public void testCheckCollisionBottom_NoCollision() {
        // Set up a block that does not collide with the bottom
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;

        boolean result = tetris.checkCollisionBottom(5, 15);
        assertFalse(result, "Block should not collide with the bottom");
    }

    @Test
    public void testCheckCollisionBottom_CollisionWithBottom() {
        // Set up a block that collides with the bottom
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;

        boolean result = tetris.checkCollisionBottom(5, 19);
        assertTrue(result, "Block should collide with the bottom");
    }

    @Test
    public void testCheckCollisionBottom_CollisionWithLeftEdge() {
        // Set up a block that collides with the left edge
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;

        boolean result = tetris.checkCollisionBottom(-1, 10);
        assertTrue(result, "Block should collide with the left edge");
    }

    @Test
    public void testCheckCollisionBottom_CollisionWithRightEdge() {
        // Set up a block that collides with the right edge
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;

        boolean result = tetris.checkCollisionBottom(9, 10);
        assertTrue(result, "Block should collide with the right edge");
    }
    
    @Test
    public void testMoveBlock_Success() {
        // Set up a block that can move without collision
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;
        Tetris.currentX = 5;
        Tetris.currentY = 5;

        boolean result = tetris.moveBlock(1, 0);
        assertTrue(result, "Block should move successfully");
        assertEquals(6, Tetris.currentX, "Block X position should be updated");
        assertEquals(5, Tetris.currentY, "Block Y position should be the same");
    }

    @Test
    public void testMoveBlock_Collision() {
        // Set up a block that will collide with the board boundary
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;
        Tetris.currentX = 9; // Near the right edge
        Tetris.currentY = 5;

        boolean result = tetris.moveBlock(1, 0);
        assertFalse(result, "Block should not move due to collision");
        assertEquals(9, Tetris.currentX, "Block X position should remain the same");
        assertEquals(5, Tetris.currentY, "Block Y position should remain the same");
    }

    @Test
    public void testMoveBlock_SuccessVertical() {
        // Set up a block that can move down without collision
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;
        Tetris.currentX = 5;
        Tetris.currentY = 5;

        boolean result = tetris.moveBlock(0, 1);
        assertTrue(result, "Block should move down successfully");
        assertEquals(5, Tetris.currentX, "Block X position should be the same");
        assertEquals(6, Tetris.currentY, "Block Y position should be updated");
    }

    @Test
    public void testMoveBlock_CollisionBottom() {
        // Set up a block that will collide with the bottom of the board
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;
        Tetris.currentX = 5;
        Tetris.currentY = 19; // Near the bottom edge

        boolean result = tetris.moveBlock(0, 1);
        assertFalse(result, "Block should not move down due to collision");
        assertEquals(5, Tetris.currentX, "Block X position should remain the same");
        assertEquals(19, Tetris.currentY, "Block Y position should remain the same");
    }
    
    @Test
    public void testCheckBlock_NoCollision() {
        // Set up a block that can move down without collision
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;
        Tetris.currentX = 5;
        Tetris.currentY = 5;

        boolean result = tetris.checkBlock();
        assertTrue(result, "Block should be able to move down without collision");
    }

    @Test
    public void testCheckBlock_Collision() {
        // Set up a block that will collide with the bottom of the board
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;
        Tetris.currentX = 5;
        Tetris.currentY = 19; // Near the bottom edge

        boolean result = tetris.checkBlock();
        assertFalse(result, "Block should not be able to move down due to collision");
    }
    
    @Test
    public void testCheckLines_CompleteLine() {
        // Set up a board with a complete line at the bottom
        for (int x = 0; x < Tetris.BoardWidth; x++) {
            Tetris.board[Tetris.BoardHeight - 1][x] = 1;
        }

        int result = tetris.checkLines();
        assertEquals(Tetris.BoardHeight - 1, result, "The complete line should be at the bottom");
    }
    
    
    @Test
    public void testResetVariable() {
        // Set some initial values
        tetris.dropSpeed = 500_000_000;
        tetris.score = 100;
        tetris.itemBar = 5;
        tetris.deleteBar = 3;
        tetris.createBlockNum = 10;
        tetris.deleteItem = 2;
        tetris.dropBlocks = 5;
        tetris.deletedLines = 5;

        // Reset variables
        tetris.resetVariable();

        // Assert all variables are reset correctly
        assertEquals(1_000_000_000, tetris.dropSpeed, "Drop speed should be reset to default");
        assertEquals(0, tetris.score, "Score should be reset to 0");
        assertEquals(1, tetris.itemBar, "Item bar should be reset to 1");
        assertEquals(0, tetris.deleteBar, "Delete bar should be reset to 0");
        assertEquals(0, tetris.createBlockNum, "Create block number should be reset to 0");
        assertEquals(0, tetris.deleteItem, "Delete item should be reset to 0");
        assertEquals(0, tetris.dropBlocks, "Drop blocks should be reset to 0");
        assertEquals(0, tetris.deletedLines, "Deleted lines should be reset to 0");
    }
    
    @Test
    public void testMoveBlock_NoCollision() {
        // Set up a block that can move right without collision
        Block block = new Block() {
            {
                shape = new int[][] { 
                    { 1, 1 },
                    { 1, 1 }
                };
            }
        };
        Tetris.block = block;
        Tetris.currentX = 5;
        Tetris.currentY = 5;

        boolean result = tetris.moveBlock(1, 0);
        assertTrue(result, "Block should be able to move right without collision");
        assertEquals(6, Tetris.currentX, "Block's X position should be updated");
        assertEquals(5, Tetris.currentY, "Block's Y position should remain the same");
    }

    
    @Test
    public void testMoveBottom() {
        int initialY = tetris.getCurrentY(); // 초기 Y 위치 저장
        int expectedY = initialY;
        
        // When
        tetris.moveBottom();
        
        // Then
        // 블록이 아래로 이동하며 충돌이 없는지 확인
        assertTrue(expectedY < tetris.getCurrentY());
    }
    
    @Test
    public void testGetScore() {
        // Given
        int expectedScore = 0; // 초기 점수는 0으로 예상
        
        // When
        int score = tetris.getScore();
        
        // Then
        assertEquals(expectedScore, score);
    }
    
    @Test
    public void testChangeMode() {
        // Given
        int expectedMode = 1; // 변경하려는 모드 값
        
        // When
        int mode = tetris.changeMode(expectedMode);
        
        // Then
        assertEquals(expectedMode, mode);
    }
    
    @Test
    public void testChangeVsMode() {
        // Given
        int expectedVsMode = 2; // 변경하려는 VS 모드 값
        
        // When
        int vsMode = tetris.changeVsMode(expectedVsMode);
        
        // Then
        assertEquals(expectedVsMode, vsMode);
    }
    
    
    @Test
    public void testGetCurrentX() {
        // Given
        int expectedX = Tetris.BoardWidth / 2; // 초기 X 위치는 보드 너비의 절반으로 예상
        
        // When
        int x = tetris.getCurrentX();
        
        // Then
        assertEquals(expectedX, x);
    }
    
    @Test
    public void testGetDropSpeed() {
        // Given
        int expectedDropSpeed = 1_000_000_000; // 초기 드롭 속도는 1_000_000_000으로 예상
        
        // When
        int dropSpeed = tetris.getDropSpeed();
        
        // Then
        assertEquals(expectedDropSpeed, dropSpeed);
    }
    
    @Test
    public void testGetNextBlock() {
        // When
        Block nextBlock = tetris.getNextBlock();
        
        // Then
        assertNotNull(nextBlock); // 다음 블록이 null이 아닌지 확인
    }
    
    @Test
    public void testGetCurrentBlock() {
        // When
        Block currentBlock = tetris.getCurrentBlock();
        
        // Then
        assertNotNull(currentBlock); // 현재 블록이 null이 아닌지 확인
    }
    
    @Test
    public void testChangeLevel() {
        // Given
        int expectedLevel = 2; // 변경하려는 레벨 값
        
        // When
        tetris.changeLevel(expectedLevel);
        int level = tetris.level;
        
        // Then
        assertEquals(expectedLevel, level);
    }
    
  

    @Test
    public void testRemoveLineWithFullLine() {
        // 보드에 완전히 채워진 줄이 있을 때 removeLine 호출 시 해당 줄이 제거되어야 함
       tetris = new Tetris(2);
        int[][] expectedBoard = new int[tetris.BoardHeight][tetris.BoardWidth];
        int[][] expectedDeleteBoard = new int[tetris.BoardWidth][tetris.BoardWidth];
        
        for (int y = 0; y < tetris.BoardHeight; y++) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
               expectedBoard[y][x] = ' ';
            }
        }
        for (int y = 0; y < tetris.BoardWidth; y++) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
               expectedDeleteBoard[y][x] = ' ';
            }
        }

        for (int x = 0; x < Tetris.BoardWidth; x++) {
            tetris.board[10][x] = 1;
        }

        tetris.removeLine(10, 1);

        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
                expectedBoard[y + 1][x] = tetris.board[y][x];
            }
        }

        assertArrayEquals(expectedBoard, tetris.board);
        assertArrayEquals(expectedDeleteBoard, tetris.deleteBoard);
    }

    
    
    @Test
    public void testDeleteBoardCheckWithEmptyBoards() {
       tetris = new Tetris(2);
        // deleteBoard와 testDeleteBoard가 모두 비어있을 때 아무 작업도 하지 않아야 함
        int[][] expectedDeleteBoard = new int[Tetris.BoardWidth][Tetris.BoardWidth];
        int[][] expectedTestDeleteBoard = new int[Tetris.BoardWidth][Tetris.BoardWidth];
        for (int y = 0; y < tetris.BoardWidth; y++) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
               expectedTestDeleteBoard[y][x] = 0;
            }
        }
        for (int y = 0; y < tetris.BoardWidth; y++) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
               expectedDeleteBoard[y][x] = ' ';
            }
        }

        tetris.deleteBoardCheck();

        assertArrayEquals(expectedDeleteBoard, tetris.deleteBoard);
        assertArrayEquals(expectedTestDeleteBoard, tetris.testDeleteBoard);
    }


    @Test
    public void testDeleteBoardCheckWithPartialDeleteBoard() {
        // deleteBoard가 부분적으로 채워져 있을 때 testDeleteBoard에 합쳐야 함
        int[][] expectedDeleteBoard = new int[Tetris.BoardWidth][Tetris.BoardWidth];
        int[][] expectedTestDeleteBoard = new int[Tetris.BoardWidth][Tetris.BoardWidth];
        for (int y = 0; y < tetris.BoardWidth; y++) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
               expectedTestDeleteBoard[y][x] = 0;
            }
        }
        for (int y = 0; y < tetris.BoardWidth; y++) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
               expectedDeleteBoard[y][x] = ' ';
            }
        }

        for (int y = 0; y < Tetris.BoardWidth / 2; y++) {
            for (int x = 0; x < Tetris.BoardWidth; x++) {
                tetris.deleteBoard[y + Tetris.BoardWidth / 2][x] = 1;
            }
        }

        for (int y = 0; y < Tetris.BoardWidth / 2; y++) {
            for (int x = 0; x < Tetris.BoardWidth; x++) {
                tetris.testDeleteBoard[y][x] = 2;
            }
        }

        tetris.deleteBoardCheck();

        for (int y = 0; y < Tetris.BoardWidth / 2; y++) {
            for (int x = 0; x < Tetris.BoardWidth; x++) {
                expectedDeleteBoard[y + Tetris.BoardWidth / 2][x] = 1;
                expectedTestDeleteBoard[y][x] = 2;
            }
        }

        assertArrayEquals(expectedDeleteBoard, tetris.deleteBoard);
        assertArrayEquals(expectedTestDeleteBoard, tetris.testDeleteBoard);
    }
    
    
    @Test
    public void testRandom2Block() {
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작
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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작
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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

        tetris.clearBoard(); // 보드 초기화
        for (int y = 0; y < tetris.BoardHeight; y++) {
            for (int x = 0; x < tetris.BoardWidth; x++) {
                assertEquals("보드가 올바르게 초기화되어야 합니다.", ' ', tetris.board[y][x]);
            }
        }
    }

    @Test
    public void testAdjustDropSpeed() {
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

        tetris.adjustDropSpeed();
        assertTrue("드롭 속도가 올바르게 조정되어야 합니다.", tetris.dropSpeed <= 1000000000 && tetris.dropSpeed >= 1000000);
        tetris.changeLevel(2);
        assertTrue("드롭 속도가 올바르게 조정되어야 합니다.", tetris.dropSpeed <= 1000000000 && tetris.dropSpeed >= 1000000);
        tetris.changeLevel(3);
        assertTrue("드롭 속도가 올바르게 조정되어야 합니다.", tetris.dropSpeed <= 1000000000 && tetris.dropSpeed >= 1000000);
    
    }
    
    @Test
    public void testCheckCollisionBottom() {
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

        tetris.initialiBlock();
        assertTrue("왼쪽", tetris.checkCollisionBottom(-1,0));
        assertTrue("왼쪽", tetris.checkCollisionBottom(10,0));
        assertTrue("아래쪽", tetris.checkCollisionBottom(0,20));
        assertTrue("위쪽", tetris.checkCollisionBottom(0,-1));
        
        assertTrue("none", !(tetris.checkCollisionBottom(5,5)));
    }
    @Test
    public void moveBlock() {
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

        tetris.initialiBlock();
        assertTrue("왼쪽", tetris.checkCollision(-1,0));
        assertTrue("왼쪽", tetris.checkCollision(10,0));
        assertTrue("아래쪽", tetris.checkCollision(0,20));
        assertTrue("위쪽", tetris.checkCollision(0,-1));
        assertTrue("false", !(tetris.checkCollision(5,5)));
    }
    
    @Test
    public void testMoveBlock() {
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

        // 초기 보드 설정 (특정 줄이 완성되었다고 가정)
        for (int x = 0; x < tetris.BoardWidth; x++) {
           tetris.board[1][x] = 1; // 'X'는 블록이 채워진 부분을 나타냄
        }
        
        // 선 지우기
//        tetris.removeLine(1);
        
//        // 기대 결과 검증
//        for (int x = 0; x < tetris.BoardWidth; x++) {
//            assertEquals("지워진 줄 위의 모든 줄이 한 칸씩 내려와야 합니다.", ' ', tetris.board[1][x]);
//        }
//        
//        // 가장 윗 줄이 비어 있는지 확인
//        for (int x = 0; x < tetris.BoardWidth; x++) {
//            assertEquals("가장 윗 줄은 비워져야 합니다.", ' ', tetris.board[0][x]);
//        }
    }
    
    @Test
    public void testAdjustScore() {
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

        // 이 테스트는 충돌 없이 블록을 회전할 수 있는 경우를 검사합니다.
        // 충돌을 피하기 위해 특정 위치와 상태를 설정해야 할 수 있습니다.
        tetris.initialiBlock();
        tetris.currentX = 0;
       tetris.currentY = -1;
        assertTrue("블록이 충돌이 있어야 한다.", !tetris.rotateBlock());
    }
    
    @Test
    public void testMoveDown() {
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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
       tetris = new Tetris(2); // level 2로 테트리스 게임 시작

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