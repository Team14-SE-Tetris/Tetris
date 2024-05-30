package tetris;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.hamcrest.CoreMatchers.is;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;



public class BoardTest extends ApplicationTest {
	
	private Board board;
	private Pane pane;
	
	@Override
    public void start(Stage stage) throws Exception {
		
		board = new Board(2,1);
    	assertEquals(1, board.battleMode);
		Scene scene = board.createScene(stage);
        
        board = new Board(2,2);
    	board.deletedLines2 = 2;
    	assertEquals(2, board.battleMode);
        scene = board.createScene(stage);
        
		board = new Board(1);
    	board.colorBlindMode = 0;
        scene = board.createScene(stage);
        
		board = new Board(2,3);
    	assertEquals(3, board.battleMode);
    	board.pressedKey1 = false;
    	press(KeyCode.Q);
		scene = board.createScene(stage);
    	press(KeyCode.Q);
    	
    	board = new Board(2,3);
    	assertEquals(3, board.battleMode);
    	board.pressedKey1 = true;
    	press(KeyCode.SPACE);
		scene = board.createScene(stage);
    	press(KeyCode.SPACE);
    	
    	board = new Board(2,3);
    	assertEquals(3, board.battleMode);
    	board.gamePaused = false;
		scene = board.createScene(stage);
		
    	board = new Board(2,3);
    	assertEquals(3, board.battleMode);
    	board.delayflag = true;
		scene = board.createScene(stage);
		
		board = new Board(2,3);
    	assertEquals(3, board.battleMode);
    	board.Board2.delayflag = true;
		scene = board.createScene(stage);
		
		board = new Board(2,3);
    	board.Board2.time = -1 ;
		scene = board.createScene(stage);
		
		board = new Board(2,3);
    	board.inGame.endFlag = false ;
		scene = board.createScene(stage);
		
        stage.setScene(scene);
        stage.show();
    }

    @Before
    public void setUp() throws Exception {
        // StartMenu 인스턴스를 생성하기 위해 start() 메서드가 먼저 호출되도록 설정
        //launch(Board.class);
    	
    }
    
    @Test
    public void testTimer() {
    	board = new Board(2,3);
    	
    	
    	board.Board2.time = -1;
    }
    
    @Test
    public void testDrawLine() {
    	board = new Board(2,3);
    	board.deletedLines2 = 2;
    }
    
    
//    @Test
//    public void testFailMoveDown() {
//		board = new Board(1);
//		board.inGame.currentX = 0;
//		board.inGame.currentY = 0;
//		board.inGame.moveBlock(0, 0);
//		board.inGame.checkCollision(0, 0);		
//		
//		board.inGame.currentX = 5;
//		board.inGame.currentY = 0;
//    }

	@Test
	public void test01() {
		board = new Board(1);

		board.gameSize = 1;//gamesize 작게로 설정
		
		board.settingConfigLoader();//gamesize가 
		
		board.gameSize = 2;
		
    	assertEquals(2, board.gameSize);
	}
	
	@Test
	public void testRight() {
		board = new Board(1);

		int CurrentR = board.inGame.getCurrentX();
		
		if(0< CurrentR && CurrentR < 10) {
			
			board.inGame.moveRight();
			assertEquals(CurrentR+1, board.inGame.getCurrentX());
		}
		
		
		
    	
	}
	

	@Test
	public void testLeft() {
		board = new Board(1);

		int CurrentL = board.inGame.getCurrentX();

		if(1< CurrentL && CurrentL < 11) {
			board.inGame.moveLeft();
			assertEquals(CurrentL-1, board.inGame.getCurrentX());
		}
	}
	@Test
	public void testDown() {
		board = new Board(1);

		int CurrentB = board.inGame.getCurrentY();
		
		if(1<CurrentB) {
			board.inGame.moveDown();
	    	assertEquals(CurrentB+1, board.inGame.getCurrentY());
		}
	}
	@Test
	public void testTelpo() {
		board = new Board(1);

		board.inGame.moveBottom();		
		boolean result = board.inGame.moveDown();
		
    	assertEquals(false, result);
	}
	@Test
	public void testrotate() {
		board = new Board(1);

		boolean result = board.inGame.rotateBlock();
		
    	assertEquals(true, result);
	}
	@Test
	public void testexit() {
		board = new Board(1);

		press(KeyCode.Q);
		
		verifyThat(board.scene.getRoot(), isVisible());
	}
	@Test
	public void testpause() {
		board = new Board(1);

		boolean result = false;
		
		press(KeyCode.SPACE);
		
		assertEquals(false, result);
	}
	@Test
	public void testblindcolor0() {
		board = new Board(1);

		board.colorBlindMode = 0;
		
		board.settingConfigLoader();
		
		assertEquals(1, board.colorBlindMode);
	}
	
	@Test
	public void testblindcolor1() {
		board = new Board(1);

		board.colorBlindMode = 0;
		
		int CurrentR = board.inGame.getCurrentX();

		if(0< CurrentR && CurrentR < 10) {
			
			board.inGame.moveRight();
			assertEquals(CurrentR+1, board.inGame.getCurrentX());
		}
		
		board.colorBlindMode = 1;
				
		assertEquals(1, board.colorBlindMode);
	}
	
	@Test
	public void testdiff() {
		board = new Board(1);

		board.settingConfigLoader();
		board.difficulty = 3;

		assertEquals(3, board.difficulty);
	}
	@Test
	public void testgamepause() {
		board = new Board(1);
		
		Pane pane = new Pane();
		board.gamePaused = true;
		
		board.pauseGame(pane);
		
		assertEquals(false, board.gamePaused);
		

	}

}
