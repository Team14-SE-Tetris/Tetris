package tetris;

import javafx.application.Application;
import blocks.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import tetris.Tetris;

public class Board extends Application {
    
    public static final int SIZE = 25;
    public static final int XMAX = SIZE * 20;
    public static final int YMAX = SIZE * (26);
    final public static Pane pane = new Pane();
    public static Scene scene = new Scene(pane, XMAX + 150, YMAX - SIZE);
    
    private static final int BOARD_WIDTH = 12;
    private static final int BOARD_HEIGHT = 22;
    
    private static int level = 1;
    private static int score = 1;
    
    private static int x[] = {0, 0, 0, 0, 0, 0};
    private static int y[] = {0, 0, 0, 0, 0, 0};
    
    public int startpointX = 85;
    public int startpointY = 90;
    public int endpointX = 325;
    
    public int interver = 25;
    
    public int deadlinenum = 9;
    
    public int boardsize = 45;
    public int dlsize = 20;
    public int scoresize = 20;
    
    public int BlockType = 0;
    
    private char[][] board = new char[BOARD_HEIGHT][BOARD_WIDTH];
    
    public static Text Title = new Text("board");
    
    public Tetris inGame = new Tetris(level);
    
    private boolean gamePaused;

    @Override
	public void start(Stage primaryStage) {
    	
		//initializeBoard(); -> inGame 객체 내부 시작
    	inGame.initialiBlock();
    	
    	// board 내부 블럭은 inGame에서 가져옴
        drawBoard();
        
        // score inGame에서 가져옴
        drawScore();
        
        deadLine();
        
        Styleset();
        
        AnimationTimer timer = new AnimationTimer() {
        	private long lastUpdate = 0;
            private long interval = inGame.getDropSpeed(); // 1초마다 이동
        	
            @Override
            public void handle(long now) {
            	
            	if (now - lastUpdate >= interval) {
            		if(!(inGame.moveDown())) {
            			if(!(inGame.initialiBlock())) {
            				// Game over 시 작동
            			}
            		}
                    lastUpdate = now;
                }
            	
                // 화면 업데이트 로직을 작성
            	drawBoard();
                deadLine();
                drawScore();
                Styleset();
            }
        };
        timer.start(); // AnimationTimer 시작
        
     // 키 이벤트 핸들러 등록
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.LEFT) {
                	inGame.moveLeft(); // 왼쪽으로 이동
                } else if (keyCode == KeyCode.RIGHT) {
                    inGame.moveRight(); // 오른쪽으로 이동
                } else if (keyCode == KeyCode.DOWN) {
                	if(!(inGame.moveDown())) {
            			if(!(inGame.initialiBlock())) {
            				// Game over 시 작동
            			}
            		}
                } else if (keyCode == KeyCode.UP) {
        			inGame.moveBottom(); // 맨 아래로 이동
        			if(!(inGame.initialiBlock())) {
        				// Game over 시 작동
        			}
        		} else if (keyCode == KeyCode.ALT) {
        			inGame.rotateBlock();
        		}
        		else if(keyCode == KeyCode.SPACE) {
        			pauseGame();
        			//timer.stop();
        		}
            }
        });
        
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Text Tetris");
        primaryStage.show();
        

    }
	
	// 수정됨 -> inGame 객체 내부의 숫자로 색 구분
	private void drawBoard() {
		
		pane.getChildren().clear();
		
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
            	Text cellText = new Text(String.valueOf(board[i][j]));
                
                if(i == 0 || i == 21) {
                	cellText.setFill(Color.WHITE);
                	
                }
                else if(j == 0 || j == 11) {
                	cellText.setFill(Color.WHITE);
                }
                
                cellText.setFont(Font.font(boardsize));
                cellText.setX(j * interver + 0.4 * startpointX);
                cellText.setY(i * interver + 0.8 * startpointY);
                pane.getChildren().add(cellText);
                int[][] board = inGame.boardPrint();
                if(board[i][j] != ' ') {
                	cellText.setText("■");
                	switch (board[i][j]) {
        			case 1:
        				cellText.setFill(Color.CYAN);
        				break;
        			case 2:		
        				cellText.setFill(Color.BLUE);
        				break;
        			case 3:
        				cellText.setFill(Color.ORANGE);
        				break;
        			case 4:
        				cellText.setFill(Color.YELLOW);
        				break;
        			case 5:
        				cellText.setFill(Color.GREEN);
        				break;
        			case 6:
        				cellText.setFill(Color.MAGENTA);
        				break;
        			case 7:
        				cellText.setFill(Color.RED);
        				break;
        			default:
        			
        			}

                }
                
             }
        }
        
        printnext();
        
    }
	
	private void Styleset() {
		
		scene.setFill(Color.BLACK);
		
	}
	
	private void drawScore() {
		Text lv = new Text("Level  :  " + String.valueOf(level));
		lv.setX(endpointX + 30);
		lv.setY(startpointY);
		lv.setFill(Color.WHITE);
		lv.setFont(Font.font(scoresize));
		score = inGame.getScore(); // inGame 에서 score 가져옴
		
		Text sc = new Text("Score  :  " + String.valueOf(score));
		sc.setX(endpointX + 30);
		sc.setY(startpointY + 60);
		sc.setFill(Color.WHITE);
		sc.setFont(Font.font(scoresize));
		
		Text nb = new Text("""
    ++-  N E X T  -++
    |                           |
    |                           |
    |                           |
    |                           |
    +-- -  -  -   -  -  --+
    """);
		nb.setX(endpointX + 60);
		nb.setY(startpointY + 120);
		nb.setFill(Color.WHITE);
		nb.setFont(Font.font(scoresize));
		
		pane.getChildren().add(lv);
		pane.getChildren().add(sc);
		pane.getChildren().add(nb);
		
	}
	
	private void deadLine() {
		
		for(int i=0; i < deadlinenum; i++) {
			Text line = new Text("-");
			line.setX(startpointX - 5 + i*interver);
			line.setY(startpointY + interver*3);
			line.setFont(Font.font(dlsize));
			line.setFill(Color.WHITE);
			
			pane.getChildren().add(line);
		}
		
		
	}
	
	public void printnext() {
	
		Block nextBlock = inGame.getNextBlock();
		
		int[][] next = nextBlock.getShapeDetail();
		
        for(int i=0; i<next.length; i++) {
        	for(int j=0; j<next[i].length; j++) {
        		
        		Text nbText = new Text();
        		if(next[i][j] != 0) {
                	nbText.setText("■");
                	pane.getChildren().add(nbText);
                	
        		}
        		
        		nbText.setX(j * interver + endpointX + 90);
            	nbText.setY(i * interver + startpointY + 180);
            	nbText.setFill(Color.WHITE);
            	nbText.setFont(Font.font(boardsize));
            	
            	switch (next[i][j]) {
    			case 1:
    				nbText.setFill(Color.CYAN);
    				break;
    			case 2:		
    				nbText.setFill(Color.BLUE);
    				break;
    			case 3:
    				nbText.setFill(Color.ORANGE);
    				break;
    			case 4:
    				nbText.setFill(Color.YELLOW);
    				break;
    			case 5:
    				nbText.setFill(Color.GREEN);
    				break;
    			case 6:
    				nbText.setFill(Color.MAGENTA);
    				break;
    			case 7:
    				nbText.setFill(Color.RED);
    				break;
    			default:
    			
    			}
        	}
        }
		
	}
	
	private void pauseGame() {
		
		// 다이얼로그를 생성하고 표시하는 코드 작성
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Paused");
	    alert.setHeaderText(null);
	    alert.setContentText("Game Paused");

	    // 확인 버튼 추가
	    ButtonType buttonTypeOk = new ButtonType("OK");
	    alert.getButtonTypes().setAll(buttonTypeOk);

	    // 다이얼로그를 표시하고 사용자 입력을 기다림
	    alert.showAndWait();
    }


//	public static void main(String[] args) {
//		
//		// TODO Auto-generated method stub
//		
//		launch(args);
//
//	}

}
