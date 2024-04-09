package tetris;

import javafx.application.Platform;
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

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import start.ScoreBoard;
import start.StartMenu;
import tetris.Tetris;

public class Board{
    
public int gameSize = 2; //게임 사이즈
	
	//윈도우를 (xPoint)x(Ypoint)칸의 좌표 나누었다.
	public int SIZE = gameSize*5 + 20; //윈도우 창 한 칸의 크기    public static final int XMAX = SIZE * 20;
    public int xPoint = 22; //가로 칸 수 21
    public int yPoint = 24; //세로 칸 수 24
    public int XMAX = SIZE * xPoint; //윈도우의 실제 가로 크기
    public int YMAX = SIZE * yPoint; //윈도우의 실제 세로 크기
    
    //윈도우 창 좌표 한칸과 pane 좌표에서의 한칸의 크기를 동기화시킴 
    public double boardsize = SIZE;//pane에서의 한칸의 크기, Text를 배치할때 좌표로 활용.
    public double blocksize = 1.9*boardsize;  //테트리스 블럭 크기
    public double interver = boardsize;
    public double dlsize = boardsize;
    public double scoresize = blocksize*0.6;
    
    //final public static Pane으로 하면 메인화면 => gamestart => 메인화면 후 다시 gamestart를 눌렀을때 오류가 발생
    //gamestart후 게임을 하다가 메인으로 다시 돌아가서 다시 gamestart를 누르면 
    //게임 내의 점수, 블록 떨어지는 속도가 안되는 현상발생 
    public Pane pane;
    public Scene scene;
    
    private static final int BOARD_WIDTH = 12;
    private static final int BOARD_HEIGHT = 22;
    
    private static int level = 1;
    private static int score = 1;
    
    private static int x[] = {0, 0, 0, 0, 0, 0};
    private static int y[] = {0, 0, 0, 0, 0, 0};
    
    //윈도우 창 좌표 한칸과 pane 좌표에서의 한칸의 크기를 동기화시킴 
//    public double startpointX = boardsize*1;//pane에서의 x좌표 1
//    public double startpointY = boardsize*2;//pane에서의 y좌표 2
//    public int endpointX = xPoint * 16; 
    
    public int deadlinenum = 10;
    
    public int BlockType = 0;
    
    private char[][] board = new char[BOARD_HEIGHT][BOARD_WIDTH];
    
    public static Text Title = new Text("board");
    
    public Tetris inGame;
    
    private boolean gamePaused;
    
	//설정파일 변수
		//키코드
	private KeyCode rotateKey = KeyCode.U, 
			teleportKey = KeyCode.T, 
			leftKey = KeyCode.LEFT, 
			downKey = KeyCode.DOWN, 
			rightKey = KeyCode.RIGHT;
		//화면 크기
		//위에 정의함, gameSize변수
		//색맹모드
	private int colorBlindMode = 0;
		//게임 난이도
	private int difficulty = 2;
	
	private String difficultyText = "normal";
    
    public Board() {
    	settingConfigLoader();//Setting.txt파일에서 설정값들을 불러와 변수에 저장하는 함수 
    	inGame = new Tetris(difficulty);
    	pane = new Pane();
        pane.setStyle("-fx-background-color: #000000;");//배경 검은색 설정
        scene = new Scene(pane, XMAX, YMAX);
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
            				this.stop();
            				// Game over 시 작동
            				Platform.runLater(() -> {
            				
            		            ScoreBoard scoreBoard = new ScoreBoard();
            		            // UI 블로킹 메서드 실행
            		            scoreBoard.showSettingDialog(score, primaryStage,"Standard Mode"); //이건 기본모드에서 점수 기록 아래는 아이템모드에서 점수기록
            		            //scoreBoard.showSettingDialog(score, primaryStage,"Item Mode");
            		        });
            			       
            			}
            		}
            		//MoveDown(x, y); // 1초마다 MoveDown() 메서드 호출
                    lastUpdate = now;
                }
            	
                // 화면 업데이트 로직을 작성
            	drawBoard();
                deadLine();
                drawScore();
                Styleset();
            }
        };
        // 키 이벤트 핸들러 등록
     // 키 이벤트 핸들러 등록
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode == leftKey) {
                	inGame.moveLeft(); // 왼쪽으로 이동
                } else if (keyCode == rightKey) {
                    inGame.moveRight(); // 오른쪽으로 이동
                } else if (keyCode == downKey) {
                	if(!(inGame.moveDown())) {
            			if(!(inGame.initialiBlock())) {
            				// Game over 시 작동
            			}
            		}
                } else if (keyCode == teleportKey) {
        			inGame.moveBottom(); // 맨 아래로 이동
        			if(!(inGame.initialiBlock())) {
        				// Game over 시 작동
        			}
        		} else if (keyCode == rotateKey) {
        			inGame.rotateBlock();
        		}
        		else if(keyCode == KeyCode.SPACE) {
        			timer.stop();
        			inGame.resetSpeedScore();
        			//게임 종료시, inGame의 dropSpeed와 Score를 초기상태로 초기화
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
                
                cellText.setFont(Font.font(blocksize));
                cellText.setX(j * interver + boardsize*1);
                cellText.setY(i * interver + boardsize*2);
                pane.getChildren().add(cellText);
                int[][] board = inGame.boardPrint();
                if(board[i][j] != ' ') {
                	cellText.setText("■");
                	switch (board[i][j]) {
                	case 1:
        				if(colorBlindMode == 0) {//색맹모드 off일 때
            				cellText.setFill(Color.CYAN);
        				}
        				else {//색맹모드 on일 때
        		            cellText.setFill(Color.rgb(0, 255, 255));
        					//하늘색 => 밝은 청록
        				}
        				break;
        			case 2:		
        				if(colorBlindMode == 0) {//색맹모드 off일 때
            				cellText.setFill(Color.BLUE);
        				}
        				else {//색맹모드 on일 때
        		            cellText.setFill(Color.rgb(153, 102, 255));
        					//파란색 => 밝은 청보라
        				}
        				break;
        			case 3:
        				if(colorBlindMode == 0) {//색맹모드 on일 때
        		            cellText.setFill(Color.ORANGE);
        		        } else {//색맹모드 off일
        		            // 주황색(ORANGE) => 밝은 주황색 유지
        		            cellText.setFill(Color.ORANGE);
        		        }
        				break;
        			case 4:
        				if(colorBlindMode == 0) {//색맹모드 off일 때
            				cellText.setFill(Color.YELLOW);
        				}
        				else {//색맹모드 on일 때
        					cellText.setFill(Color.rgb(255, 255, 102)); 
        					//노란색 => 밝은 노란
        				}
        				break;
        			case 5:
        				if(colorBlindMode == 0) {//색맹모드 off일 때
            				cellText.setFill(Color.GREEN);
        				}
        				else {//색맹모드 on일 때
        					cellText.setFill(Color.rgb(0, 255, 204));
        					//초록색 =>청록색 
        				}
        				break;
        			case 6:
        				if(colorBlindMode == 0) {//색맹모드 off일 때
            				cellText.setFill(Color.MAGENTA);
        				}
        				else {//색맹모드 on일 때
        					cellText.setFill(Color.rgb(204, 0, 204)); 
        					//자주색 => 진보라
        				}
        				break;
        			case 7:
        				if(colorBlindMode == 0) {//색맹모드 off일 때
            				cellText.setFill(Color.RED);
        				}
        				else {//색맹모드 on일 때
        		            cellText.setFill(Color.rgb(210, 105, 30));
        					//빨간색 => 주황
        				}
        				break;
        			default:
        			
        			}

                }
                
             }
        }
        printnext();
    }
	
	private void Styleset() {
		
		pane.setStyle("-fx-background-color: black;");
		
	}
	
	private void drawScore() {
		
		switch(difficulty) { //difficulty 값에 따른 난이도 텍스트 설정
    	case 1:
    	    difficultyText = "easy";
    		break;
    	case 2:
    	    difficultyText = "normal";
    		break;
    	case 3:
    	    difficultyText = "hard";
    		break;
    	}
		
		Text lv = new Text("Level  :  " + difficultyText);
		lv.setX(boardsize*14);
		lv.setY(boardsize*9);
		lv.setFill(Color.WHITE);
		lv.setFont(Font.font(scoresize*0.8));
		
		score = inGame.getScore(); // inGame 에서 score 가져옴
		Text sc = new Text("Score  :  " + String.valueOf(score));
		sc.setX(boardsize*14);
		sc.setY(boardsize*10);
		sc.setFill(Color.WHITE);
		sc.setFont(Font.font(scoresize*0.8));
		
		//다음블럭 나타날 텍스트 테두리
		//가로 7칸 세로 5칸
		for(int i=0; i<7; i++) {
			for(int j=0; j<6; j++) {
				Text nextblock = new Text("");
				
				if(i==0 || i==6 || j==0 || j==5) {//테두리, 텍스트 설정
					if(j==0&& i<=4&& i>=2) {//제목위치
						nextblock = new Text(""); // NEXT 들어갈 자리 비우기
						if(i==2 && j==0) {
							nextblock = new Text("NEXT");//비운자리에 NEXT 넣기
							nextblock.setFont(Font.font(scoresize));//NEXT텍스트 크기조절
						}
					}
					else {
						nextblock = new Text("="); //테두리 텍스트 설정
						nextblock.setFont(Font.font(scoresize*1.3));//테두리 텍스트 크기조절
					}
				}
				
				//텍스트 기본 설정
				nextblock.setX(i*interver + boardsize*14);
				nextblock.setY(j*interver + boardsize*3);
				nextblock.setFill(Color.WHITE);
				pane.getChildren().add(nextblock);
			}
		}
		
		pane.getChildren().add(lv);
		pane.getChildren().add(sc);
	}
	
	private void deadLine() {
		
		for(int i=0; i < deadlinenum; i++) {
			Text line = new Text(" -");
			line.setX(boardsize*2 + i*interver);
			line.setY(boardsize*4);
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
        		
        		nbText.setX(j * interver + boardsize*16);
            	nbText.setY(i * interver + boardsize*5);
            	nbText.setFill(Color.WHITE);
            	nbText.setFont(Font.font(blocksize));
            	
            	
            	switch (nextBlock.getColorNum()) {
            	case 1:
    				if(colorBlindMode == 0) {//색맹모드 off일 때
    					nbText.setFill(Color.CYAN);
    				}
    				else {//색맹모드 on일 때
    					nbText.setFill(Color.rgb(0, 255, 255));
    					//하늘색 => 밝은 청록
    				}
    				break;
    			case 2:		
    				if(colorBlindMode == 0) {//색맹모드 off일 때
    					nbText.setFill(Color.BLUE);
    				}
    				else {//색맹모드 on일 때
    					nbText.setFill(Color.rgb(153, 102, 255));
    					//파란색 => 밝은 청보라
    				}
    				break;
    			case 3:
    				if(colorBlindMode == 0) {//색맹모드 on일 때
    					nbText.setFill(Color.ORANGE);
    		        } else {//색맹모드 off일
    		            // 주황색(ORANGE) => 밝은 주황색 유지
    		        	nbText.setFill(Color.ORANGE);
    		        }
    				break;
    			case 4:
    				if(colorBlindMode == 0) {//색맹모드 off일 때
    					nbText.setFill(Color.YELLOW);
    				}
    				else {//색맹모드 on일 때
    					nbText.setFill(Color.rgb(255, 255, 102)); 
    					//노란색 => 밝은 노란
    				}
    				break;
    			case 5:
    				if(colorBlindMode == 0) {//색맹모드 off일 때
    					nbText.setFill(Color.GREEN);
    				}
    				else {//색맹모드 on일 때
    					nbText.setFill(Color.rgb(0, 255, 204));
    					//초록색 =>청록색 
    				}
    				break;
    			case 6:
    				if(colorBlindMode == 0) {//색맹모드 off일 때
    					nbText.setFill(Color.MAGENTA);
    				}
    				else {//색맹모드 on일 때
    					nbText.setFill(Color.rgb(204, 0, 204)); 
    					//자주색 => 진보라
    				}
    				break;
    			case 7:
    				if(colorBlindMode == 0) {//색맹모드 off일 때
    					nbText.setFill(Color.RED);
    				}
    				else {//색맹모드 on일 때
    					nbText.setFill(Color.rgb(210, 105, 30));
    					//빨간색 => 주황
    				}
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
	
	public void settingConfigLoader() {
        // 설정파일의 위치 설정
        String filePath = "src/Settings.txt"; // 상대 경로
        try {
            // 파일의 모든 라인을 읽어온다.
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // 각 라인을 순회하며 키와 값을 분리
            for (String line : lines) {
                String[] parts = line.split(" = ");
                if (parts.length < 2) continue; // 유효하지 않은 라인 건너뛰기

                String key = parts[0].trim();
                String value = parts[1].trim().replaceAll("\"", ""); // 따옴표 제거

                // 문자열을 KeyCode로 변환하고 적절한 변수에 할당
                switch (key) {
                    case "rotateKey":
                        rotateKey = KeyCode.valueOf(value);
                        break;
                    case "teleportKey":
                        teleportKey = KeyCode.valueOf(value);
                        break;
                    case "leftKey":
                        leftKey = KeyCode.valueOf(value);
                        break;
                    case "downKey":
                        downKey = KeyCode.valueOf(value);
                        break;
                    case "rightKey":
                        rightKey = KeyCode.valueOf(value);
                        break;
                    case "gameSize":
                    	gameSize = Integer.parseInt(value);
                    	break;
                    case "colorBlindMode":
                    	colorBlindMode = Integer.parseInt(value);
                    	break;
                    case "difficulty" :
                    	difficulty = Integer.parseInt(value);
                    	break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 읽기 실패 시, 적절한 예외 처리나 사용자 알림이 필요할 수 있습니다.
        }
   
        
        //화면 관련 변수 초기화
        
        SIZE = gameSize*5 + 20; //윈도우 창 한 칸의 크기
        xPoint = 22; //가로 칸 수 21
        yPoint = 24; //세로 칸 수 24
        XMAX = SIZE * xPoint; //윈도우의 실제 가로 크기
        YMAX = SIZE * yPoint; //윈도우의 실제 세로 크기
        boardsize = SIZE;//pane에서의 한칸의 크기, Text를 배치할때 좌표로 활용.
        blocksize = 1.9*boardsize; //테트리스 블럭 크기
        interver = boardsize;
        dlsize = boardsize;
        scoresize = blocksize*0.6;
	}

//	public static void main(String[] args) {
//		
//		// TODO Auto-generated method stub
//		
//		launch(args);
//
//	}

}