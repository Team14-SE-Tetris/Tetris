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
		board = new Board(1);
        Scene scene = board.createScene(stage);
        
        stage.setScene(scene);
        stage.show();
    }

    @Before
    public void setUp() throws Exception {
        // StartMenu 인스턴스를 생성하기 위해 start() 메서드가 먼저 호출되도록 설정
        //launch(Board.class);
    	
    }

	@Test
	public void test01() {
		board.gameSize = 1;//gamesize 작게로 설정
		
		board.settingConfigLoader();//gamesize가 
		
    	assertEquals(2, board.gameSize);
	}
	
	@Test
	public void testRight() {
		int CurrentR = board.inGame.getCurrentX();
		press(KeyCode.RIGHT);
		
		if(CurrentR < 12) {

			assertEquals(CurrentR+1, board.inGame.getCurrentX());
		}
		
		
		
    	
	}
	@Test
	public void testLeft() {
		int CurrentL = board.inGame.getCurrentX();
		press(KeyCode.LEFT);
		
    	assertEquals(CurrentL-1, board.inGame.getCurrentX());
	}
	@Test
	public void testDown() {
		int CurrentB = board.inGame.getCurrentY();
		press(KeyCode.DOWN);
		
    	assertEquals(CurrentB+1, board.inGame.getCurrentY());
	}
	@Test
	public void testTelpo() {
		
		press(KeyCode.UP);
		
		boolean result = board.inGame.moveDown();
		
    	assertEquals(false, result);
	}
	@Test
	public void testrotate() {
		
		press(KeyCode.ALT);
		
		boolean result = board.inGame.rotateBlock();
		
    	assertEquals(true, result);
	}
	@Test
	public void testexit() {
		
		press(KeyCode.Q);
		
		verifyThat(board.scene.getRoot(), isVisible());
	}
	@Test
	public void testpause() {
		
		boolean result = false;
		
		press(KeyCode.SPACE);
		
		assertEquals(false, result);
	}
	@Test
	public void testblindcolor0() {
		
		board.colorBlindMode = 0;
		
		board.settingConfigLoader();
		
		assertEquals(1, board.colorBlindMode);
	}
	@Test
	public void testdiff() {
		
		board.difficulty = 3;
		
		board.settingConfigLoader();
		
		assertEquals(2, board.difficulty);
	}
	@Test
	public void testgamepause() {
		Pane pane = new Pane();
		board.gamePaused = true;
		
		board.pauseGame(pane);
		
		assertEquals(false, board.gamePaused);
		

	}

	

}
