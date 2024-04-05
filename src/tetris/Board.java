package tetris;

import javafx.application.Application;
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
import start.ScoreBoard;
import start.StartMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

import tetris.BlockData;
import tetris.Tetris;

public class Board{
    
    public static final int SIZE = 25;
    public static final int XMAX = SIZE * 20;
    public static final int YMAX = SIZE * (26);
    public static Pane pane;
    public static Scene scene;
    
    private static final int BOARD_WIDTH = 12;
    private static final int BOARD_HEIGHT = 22;
    
    private static int level = 1;
    private int score = 1;
    
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
    
    public Tetris inGame;
    
    public Board() {
    	pane = new Pane();
		scene = new Scene(pane, XMAX + 150, YMAX - SIZE);
		inGame = new Tetris(level);
    }

	public Scene createScene(Stage primaryStage) {
		//initializeBoard(); -> inGame 객체 내부 시작
    	inGame.initialiBlock();
    	
    	// board 내부 블럭은 inGame에서 가져옴
        drawBoard();
        
        // score inGame에서 가져옴
        drawScore();
        
        deadLine();
        
        Styleset();
        
        // addBlock(); -> inGame의 initialiBlock()에서 이미 배치 함 
        
        // MoveLeft(x, y);
        // MoveLeft(x, y);

        
        //MoveDown(x, y);
        
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
            		//MoveDown(x, y); // 1초마다 MoveDown() 메서드 호출
                    lastUpdate = now;
                }
            	
                // 화면 업데이트 로직을 작성
            	drawBoard();
                deadLine();
                drawScore();
            }
        };
        // 키 이벤트 핸들러 등록
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.LEFT /* && x[0] > 1 */) {
                    // MoveLeft(x, y); // 왼쪽으로 이동
                	inGame.moveLeft();
                } else if (keyCode == KeyCode.RIGHT /*&& x[2] < BOARD_WIDTH - 2*/) {
                    inGame.moveRight();
                	//MoveRight(x, y); // 오른쪽으로 이동
                } else if (keyCode == KeyCode.DOWN /*&& y[1] <= BOARD_HEIGHT - 2*/) {
                	if(!(inGame.moveDown())) {
            			if(!(inGame.initialiBlock())) {
            				// Game over 시 작동
            			}
            		}
                	//MoveDown(x, y); // 아래로 이동
                } else if (keyCode == KeyCode.UP) {
        			inGame.moveBottom(); // 맨 아래로 이동
        			if(!(inGame.initialiBlock())) {
        				// Game over 시 작동
        			}
        		} else if (keyCode == KeyCode.ALT) {
        			inGame.rotateBlock();
        		} else if (keyCode == KeyCode.ESCAPE) {
        			timer.stop();
        			primaryStage.setScene(StartMenu.scene);
        		}
            }
        });
        
        
        
        
        timer.start(); // AnimationTimer 시작
        
        return scene;
        

    }
	
	// 수정됨 -> inGame 객체 내부의 숫자로 색 구분
	private void drawBoard() {
		
		pane.getChildren().clear();
		
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
            	Text cellText = new Text(String.valueOf(board[i][j]));
                
                if(i == 0 || i == 21) {
                	cellText.setText("□");
                	cellText.setFill(Color.WHITE);
                	
                }
                else if(j == 0 || j == 11) {
                	cellText.setText("□");
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
        
    }
	
	private void Styleset() {
		
		pane.setStyle("-fx-background-color: black;");
		
	}
	
	private void drawScore() {
		score=1;
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
		
		pane.getChildren().add(lv);
		pane.getChildren().add(sc);
		
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
	
//	private void initializeBoard() {
//  for (int i = 0; i < BOARD_HEIGHT ; i++) {
//      for (int j = 0; j < BOARD_WIDTH ; j++) {
//          board[i][j] = ' ';
//          
//      }
//  }
// 
//}
	
//	private void addBlock() {
//		
//		int k = 0;
//		
//		for(int i=0; i < 2; i++) {
//			for(int j=0; j < 3; j++) {
//				
//				int row = 1 + i; // 시작 행
//	            int col = BOARD_WIDTH / 2 + j - 1; // 시작 열
//				
//	            if (BlockData.JBlock[i][j] == 1 && row < BOARD_HEIGHT && col < BOARD_WIDTH) {
//	                // 현재 위치에 블록이 없을 경우에만 블록 추가
//	                if (board[row][col] == ' ') {
//	                    board[row][col] = '■';
//	                    x[j] = col;
//	                    
//	                    
//	                }
//	            }
//				
//				
//			}
//			
//			y[i] = i+1;
//			
//		}
//		
//	}
//	
//	private void MoveDown(int x[], int y[]) {
//		
//		int check = 0;
//		
//		for(int i=0; i<3; i++) {
//			for(int j=0; j<2; j++) {
//				if(board[y[j]][x[i]] == '■') {
//					check += 1;
//				}
//			}
//		}
//		
//		if (check == 6) {
//			addBlock();
//			return;
//			}
//		
//		for(int j=1; j>=0; j--) {
//			
//			for(int i=0; i < 3; i++) {
//				
//				int nextY = y[j] + 1;
//				if(board[y[j]][x[i]] != ' ') {
//					if (isValidMove(x[i], nextY)) {
//		        			// 이동 가능하다면 블록 이동
//						
//		        			board[y[j]][x[i]] = ' '; // 이동 전 위치에서 블록 제거
//		        			board[y[j]+1][x[i]] = '■'; // 새 위치에 블록 추가
//						}
//		        			
//					
//					else {
//						return;
//					}
//				}
//		    }
//			//y[j] = y[j] + 1; // 블록의 y 좌표 업데이트
//		}
//		
//		y[1] = y[1] + 1;
//		y[0] = y[0] + 1;
//		
//	}
	
//	private boolean isValidMove(int x, int y) {
//	    // 블록이 보드를 벗어나는지 확인
//	    if (x < 1 || x >= BOARD_WIDTH-1 || y >= BOARD_HEIGHT-1) {
//	    	addBlock();
//	        return false;
//	    }
//
//	    // 블록이 다른 블록 위에 있는지 확인
//	    if (board[y][x] != ' ') {
//	    	addBlock();
//	        return false;
//	    }
//
//	    return true;
//	}
//	
//	private void MoveLeft(int x[], int y[]) {
//		for(int j=1; j>=0; j--) {
//			
//			for(int i=0; i < 3; i++) {
//				
//				int nextX = x[i] - 1;
//				
//				if(board[y[j]][x[i]] != ' ') {
//					if (isValidMoveL(nextX, y[j])) {
//		        			// 이동 가능하다면 블록 이동
//						
//		        			board[y[j]][x[i]] = ' '; // 이동 전 위치에서 블록 제거
//		        			board[y[j]][x[i]-1] = '■'; // 새 위치에 블록 추가
//					}
//					else {
//						
//					}
//		        			
//		    	}
//			//y[j] = y[j] + 1; // 블록의 y 좌표 업데이트
//			}
//		
//		}
//		
//		x[2] = x[2] - 1;
//		x[1] = x[1] - 1;
//		x[0] = x[0] - 1;
//	}
	
//	private boolean isValidMoveL(int x, int y) {
//	    // 블록이 보드를 벗어나는지 확인
//	    if (x < 1 || x >= BOARD_WIDTH-1 || y >= BOARD_HEIGHT-1) {
//	        return false;
//	    }
//
//	    // 블록이 다른 블록 위에 있는지 확인
//	    if (board[y][x] != ' ') {
//	        return false;
//	    }
//
//	    return true;
//	}
	
//	private void MoveRight(int x[], int y[]) {
//		for(int j=1; j>=0; j--) {
//			
//			for(int i=3; i >= 0; i--) {
//				
//				int nextX = x[i] + 1;
//				
//				if(board[y[j]][x[i]] != ' ') {
//					if (isValidMoveR(nextX, y[j])) {
//		        			// 이동 가능하다면 블록 이동
//						
//		        			board[y[j]][x[i]] = ' '; // 이동 전 위치에서 블록 제거
//		        			board[y[j]][x[i]+1] = '■'; // 새 위치에 블록 추가
//					}
//					else {
//						
//					}
//		        			
//		    	}
//			//y[j] = y[j] + 1; // 블록의 y 좌표 업데이트
//			}
//		
//		}
//		
//		x[2] = x[2] + 1;
//		x[1] = x[1] + 1;
//		x[0] = x[0] + 1;
//	}
//	
//	private boolean isValidMoveR(int x, int y) {
//	    // 블록이 보드를 벗어나는지 확인
//	    if (x < 1 || x >= BOARD_WIDTH-1 || y >= BOARD_HEIGHT-1) {
//	        return false;
//	    }
//
//	    // 블록이 다른 블록 위에 있는지 확인
//	    if (board[y][x] != ' ') {
//	        return false;
//	    }
//
//	    return true;
//	}


//	public static void main(String[] args) {
//		
//		// TODO Auto-generated method stub
//		
//		launch(args);
//
//	}

}