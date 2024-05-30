package tetris;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;

import tetris.Tetris2;
import blocks.Block;

public class Tetris2Test extends ApplicationTest {

    private Tetris2 tetris;

    @BeforeEach
    public void setUp() {
        tetris = new Tetris2(1);
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
        Tetris2.block = block;

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
        Tetris2.block = block;

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
        Tetris2.block = block;

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
        Tetris2.block = block;

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
        Tetris2.block = block;
        Tetris2.currentX = 5;
        Tetris2.currentY = 5;

        boolean result = tetris.moveBlock(1, 0);
        assertTrue(result, "Block should move successfully");
        assertEquals(6, Tetris2.currentX, "Block X position should be updated");
        assertEquals(5, Tetris2.currentY, "Block Y position should be the same");
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
        Tetris2.block = block;
        Tetris2.currentX = 9; // Near the right edge
        Tetris2.currentY = 5;

        boolean result = tetris.moveBlock(1, 0);
        assertFalse(result, "Block should not move due to collision");
        assertEquals(9, Tetris2.currentX, "Block X position should remain the same");
        assertEquals(5, Tetris2.currentY, "Block Y position should remain the same");
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
        Tetris2.block = block;
        Tetris2.currentX = 5;
        Tetris2.currentY = 5;

        boolean result = tetris.moveBlock(0, 1);
        assertTrue(result, "Block should move down successfully");
        assertEquals(5, Tetris2.currentX, "Block X position should be the same");
        assertEquals(6, Tetris2.currentY, "Block Y position should be updated");
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
        Tetris2.block = block;
        Tetris2.currentX = 5;
        Tetris2.currentY = 19; // Near the bottom edge

        boolean result = tetris.moveBlock(0, 1);
        assertFalse(result, "Block should not move down due to collision");
        assertEquals(5, Tetris2.currentX, "Block X position should remain the same");
        assertEquals(19, Tetris2.currentY, "Block Y position should remain the same");
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
        Tetris2.block = block;
        Tetris2.currentX = 5;
        Tetris2.currentY = 5;

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
        Tetris2.block = block;
        Tetris2.currentX = 5;
        Tetris2.currentY = 19; // Near the bottom edge

        boolean result = tetris.checkBlock();
        assertFalse(result, "Block should not be able to move down due to collision");
    }
    
    @Test
    public void testCheckLines_CompleteLine() {
        // Set up a board with a complete line at the bottom
        for (int x = 0; x < Tetris2.BoardWidth; x++) {
            Tetris2.board[Tetris2.BoardHeight - 1][x] = 1;
        }

        int result = tetris.checkLines();
        assertEquals(Tetris2.BoardHeight - 1, result, "The complete line should be at the bottom");
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
        Tetris2.block = block;
        Tetris2.currentX = 5;
        Tetris2.currentY = 5;

        boolean result = tetris.moveBlock(1, 0);
        assertTrue(result, "Block should be able to move right without collision");
        assertEquals(6, Tetris2.currentX, "Block's X position should be updated");
        assertEquals(5, Tetris2.currentY, "Block's Y position should remain the same");
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
        int expectedX = Tetris2.BoardWidth / 2; // 초기 X 위치는 보드 너비의 절반으로 예상
        
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
}
