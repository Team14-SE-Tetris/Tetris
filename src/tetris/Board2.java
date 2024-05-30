package tetris;

import javafx.application.Platform;
import blocks.*;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.InnerShadow;

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
import javafx.concurrent.Task;
public class Board2{
    
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
    
//    //mac용 
//    public double boardsize = SIZE;//pane에서의 한칸의 크기, Text를 배치할때 좌표로 활용.
//    public double blocksize = 1.18*boardsize;  //테트리스 블럭 크기
//    public double interver = boardsize;
//    public double dlsize = boardsize;
//    public double scoresize = blocksize;
    
    //final public static Pane으로 하면 메인화면 => gamestart => 메인화면 후 다시 gamestart를 눌렀을때 오류가 발생
    //gamestart후 게임을 하다가 메인으로 다시 돌아가서 다시 gamestart를 누르면 
    //게임 내의 점수, 블록 떨어지는 속도가 안되는 현상발생 
    public Pane pane;
    public Scene scene;
    
    private static final int BOARD_WIDTH = 12;
    private static final int BOARD_HEIGHT = 22;
    
    private static int level = 1;
    public int score = 1;
    private static int mode =0;
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
    
    public Tetris2 inGame2;
    
    public boolean gamePaused = false;
    
    public int liney = 0;
    private int gradation = 1;
    private int removedelay = 0;
    public boolean removeflag = true;
    public boolean delayflag = true;
    public int removestep = 0;
    
    public boolean telpoflag = false;
    
    public AnimationTimer timer;
    
    private boolean timerflag = false;
    
	//설정파일 변수
		//키코드
	public KeyCode rotateKey_Player2 = KeyCode.U, 
			teleportKey_Player2 = KeyCode.T, 
			leftKey_Player2 = KeyCode.LEFT, 
			downKey_Player2 = KeyCode.DOWN, 
			rightKey_Player2 = KeyCode.RIGHT;
		//화면 크기
		//위에 정의함, gameSize변수
		//색맹모드
	public int colorBlindMode = 0;
		//게임 난이도
	public int difficulty = 2;
	
	public String difficultyText = "normal";
	
	public int battleMode=0;
	
	public int time;
	
	private Timeline timeline; // 타이머를 제어할 Timeline 객체
	
	public Text timerText = new Text();
	 
    
    public Board2(int mode) {
    	timer = null;
    	settingConfigLoader();//Setting.txt파일에서 설정값들을 불러와 변수에 저장하는 함수 
    	inGame2 = new Tetris2(difficulty);
    	pane = new Pane();
    	this.mode = mode;
    	this.time = 60; 
        pane.setStyle("-fx-background-color: #000000;");//배경 검은색 설정
        scene = new Scene(pane, XMAX, YMAX);
        delayflag=true;
    }
    
    public Board2(int mode,int battleMode) {
    	this(mode);
    	this.battleMode=battleMode;
    	if(battleMode == 3) {
            initializeTimer(); // 타이머 초기화 함수 호출
    	}
    }
    
    public int getTime() {
    	return this.time;
    }
    
    // 타이머 초기화 함수
    private void initializeTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int minutes = time / 60;
            int seconds = time % 60;
            timerText.setText(String.format("Time  :  %02d:%02d", minutes, seconds));
            time--;
            if (time <= 0) {
                timeline.stop();
                // 타이머가 0이 되었을 때의 동작 추가
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    public void timerStop() {
 	   this.timeline.stop();
    }
    
    public void timerStart() {
 	   this.timeline.play();
    }


	public Pane createpane(Stage primaryStage) {

		//initializeBoard(); -> inGame2 객체 내부 시작

		if (mode ==1){
        	inGame2.changeMode(1);
        }
        else if(mode==0){
        	inGame2.changeMode(0);
        }
        else if(mode==2) {
        	if(battleMode==1) {
        		inGame2.changeMode(0);//기본모드
        	}
        	else if(battleMode==2) {
        		inGame2.changeMode(1);//아이템모드
        	}
        	else if(battleMode==3) {
        		inGame2.changeMode(0);//타임어택모드
        	}
        }
    	inGame2.initialiBlock();
        
        
        return pane;
    }
	
	public void resetVariable() {
		inGame2.resetVariable();
		timerflag = true;
		score = 0;
		System.out.println("점수2: " + score);
		System.out.println("점수2: " + inGame2.getScore());
		
	}
	public void moveleft() {
		inGame2.moveLeft();
	}
	
	public void moveright() {
		inGame2.moveRight();
	}
	
	public boolean movedown() {
		return inGame2.moveDown();
		
	}
	
	public void movebottom() {
		inGame2.moveBottom();
	}
	
	public void rotateblock() {
		inGame2.rotateBlock();
	}
	
	public long getDropspeed() {
		return inGame2.getDropSpeed();
	}
	
	public int checkline() {
		return inGame2.checkLines();
	}
	
	public boolean initializeblock() {
		return inGame2.initialiBlock();
	}
	
	public void removeline(int liney, int deleteLine) {
		inGame2.removeLine(liney, deleteLine);
	}
	
	public Block getCurrentblock() {
		return inGame2.getCurrentBlock();
	}
	
	public boolean checkblock() {
		return inGame2.checkBlock();
	}
	
	public int getscore() {
		return inGame2.getScore();
	}
	
	public boolean checkcollisionBottom(int currentx, int currenty) {
		return inGame2.checkCollisionBottom(currentx, currenty);
	}
	
	public int getcurrentx() {
		return inGame2.getCurrentX();
	}
	
	public int getcurrenty() {
		return inGame2.getCurrentY();
	}
	
	// 수정됨 -> inGame2 객체 내부의 숫자로 색 구분
	public void drawBoard() {
		
		pane.getChildren().clear(); //pane(게임화면) 비우기
		
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
            	Text cellText = new Text(String.valueOf(board[i][j]));
                
                if(i == 0 || i == 21) {//테트리스 벽 채우기
                	cellText.setText("□");
                	cellText.setFill(Color.WHITE);
                	
                }
                else if(j == 0 || j == 11) {//테트리스 벽 채우기
                	cellText.setText("□");
                	cellText.setFill(Color.WHITE);
                }
                
                cellText.setFont(Font.font(blocksize));//블럭사이즈
                cellText.setX(j * interver + boardsize*1);//블럭 X좌표
                cellText.setY(i * interver + boardsize*2);//블럭 Y좌표
                pane.getChildren().add(cellText);//블럭 pane에 추가
                int[][] board = inGame2.boardPrint();// 현재 Board 상태 출력
                if(board[i][j] != ' ') {
                	cellText.setText("■");
                	
                	if(0<liney && liney < 22) {//완성된 줄이 있는 경우
                		if(j !=0 && j !=11) {//양쪽 끝 벽이 아닌경우
                			
                			if(removeflag == false) {

                				board[liney+1][j] = 8;//완성된 줄의 밑을 8로 채움

                			}
                		}
                	}
                	//줄 삭제 이펙트
                	
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
        			case 8://줄삭제 이벤트 색깔
            			cellText.setFill(Color.GRAY);
            			break;
        			case 9: // item 1 용
        				cellText.setText("a");
            			cellText.setFill(Color.WHITE);
            			break;
        			case 10: // item 색 맞추기
        				cellText.setText("b");
            			cellText.setFill(Color.WHITE);
            			break;
        			case 11: // item 4 용
        				cellText.setText("L");
            			cellText.setFill(Color.WHITE);
            			break;
        			case 12: // item 색 맞추기
        				cellText.setText("c");
            			cellText.setFill(Color.WHITE);
            			break;
        			case 13: // item 색 맞추기
        				cellText.setText("d");
            			cellText.setFill(Color.WHITE);
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
	
	public void drawLine(int deletedLines1, int [][] lineBoard) {
		
		if(deletedLines1 > 1) {
			
			int[][] reversedLineBoard = new int[BOARD_WIDTH][BOARD_WIDTH];
			
			// 행을 역순으로 재배열
			for (int i = 0; i < BOARD_WIDTH; i++) {
			    reversedLineBoard[i] = lineBoard[BOARD_WIDTH - 1 - i];
			}
			
			// 재배열된 배열을 lineBoard에 다시 할당
			lineBoard = reversedLineBoard;
			
			//System.out.println(Arrays.deepToString(lineBoard));
			
			for(int k=0; k < BOARD_WIDTH; k++) {
				for(int m=0; m<BOARD_WIDTH; m++) {
	            	Text cellTextD = new Text(String.valueOf(lineBoard[k][m]));
	            	
	            	 // 블럭 X 좌표와 Y 좌표를 일관되게 계산
	                double x = m * interver / 2.12 + boardsize * 14 + 6;
	                double y = k * interver / 2.12 + boardsize * 17 + 3;
	            	
	    			//System.out.println(lineBoard[k][m]);
	            	
	            	if(m == 0 || m == 11) {
	            		
	            		cellTextD.setText(" ");
		               	cellTextD.setX(m* interver/2.09 + boardsize*14 + 6);//블럭 X좌표
		                cellTextD.setY(k* interver/2.09 + boardsize*12 + 8);//블럭 Y좌표
		                cellTextD.setFont(Font.font(scoresize/1.1));//블럭사이즈
	            	}
	            	
	            	
	            	else if(lineBoard[k][m] == 0 || lineBoard[k][m] == 32) {
	               	 
	               	 cellTextD.setText(" ");
	               	 cellTextD.setFill(Color.GRAY);
	               	 cellTextD.setX(x);//블럭 X좌표
	                 cellTextD.setY(y);//블럭 Y좌표
	                 cellTextD.setFont(Font.font(scoresize/1.3));//블럭사이즈

	                    
	                }
	                else {
	                //System.out.println(lineBoard[k][m]);
	               	 
	               	 cellTextD.setText("■");
	               	 cellTextD.setFill(Color.GRAY);
	               	 cellTextD.setX(x);//블럭 X좌표
	                 cellTextD.setY(y);//블럭 Y좌표
	                 cellTextD.setFont(Font.font(scoresize/1.3));//블럭사이즈

	                }
	            	
	                pane.getChildren().add(cellTextD);
					
				}
			}
		}
		
		
		for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
            	Text cellText = new Text();
            	
            	 if(i == 0 || i == 21) {//테트리스 벽 채우기
                 	cellText.setText("■");
                 	cellText.setFill(Color.WHITE);
                 	cellText.setX(j* interver/2 + boardsize*14);//블럭 X좌표
                    cellText.setY(i* interver/2 + boardsize*12);//블럭 Y좌표
                    cellText.setFont(Font.font(scoresize));//블럭사이즈
                 	
                 }
                 else if(j == 0 || j == 11) {//테트리스 벽 채우기
                 	cellText.setText("■");
                 	cellText.setFill(Color.WHITE);
                 	cellText.setX(j* interver/2 + boardsize*14);//블럭 X좌표
                    cellText.setY(i* interver/2 + boardsize*12);//블럭 Y좌표
                    cellText.setFont(Font.font(scoresize));//블럭사이즈
                 }
                 

                pane.getChildren().add(cellText);

            	

            }
		
		}
	}
	
	public void drawScore() {
		
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
		
		score = inGame2.getScore(); // inGame2 에서 score 가져옴
		Text sc = new Text("Score  :  " + String.valueOf(score));
		sc.setX(boardsize*14);
		sc.setY(boardsize*10);
		sc.setFill(Color.WHITE);
		sc.setFont(Font.font(scoresize*0.8));
		
		//만약 battelmode ==3 이면 타이머 추가
				// 배틀모드가 3일 때만 타이머 텍스트 추가
		        if (battleMode == 3) {
		        	// 빛나는 효과
		            InnerShadow innerShadow = new InnerShadow();
		            innerShadow.setOffsetX(0f);
		            innerShadow.setOffsetY(0f);
		            innerShadow.setColor(Color.YELLOW);
		            innerShadow.setRadius(10);

		            timerText.setEffect(innerShadow);
		            timerText.setStroke(Color.YELLOW); // 테두리 색상
		            timerText.setStrokeWidth(1); // 테두리 두께
		            timerText.setX(boardsize * 14);
		            timerText.setY(boardsize * 1.5);
		            timerText.setFill(Color.WHITE);
		            timerText.setFont(Font.font(scoresize * 0.8));
		            pane.getChildren().add(timerText);
		        }
        
		
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
	
	public void printnext() {
		
		Block nextBlock = inGame2.getNextBlock();
		
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
    			case 8:
    				nbText.setFill(Color.GRAY);
    				break;
    			case 9: // item 1 용
    				nbText.setText("a");
    				nbText.setFill(Color.WHITE);
        			break;
    			case 10: // item 색 맞추기
    				nbText.setText("b");
    				nbText.setFill(Color.WHITE);
        			break;
    			case 11: // item 4 용
    				nbText.setText("L");
    				nbText.setFill(Color.WHITE);
        			break;
    			case 12: // item 색 맞추기
    				nbText.setText("c");
    				nbText.setFill(Color.WHITE);
        			break;
    			case 13: // item 색 맞추기
    				nbText.setText("d");
    				nbText.setFill(Color.WHITE);
        			break;
    			case 14:
    				nbText.setFill(Color.LIGHTGRAY);
    			default:
    				
    			}
            	if (next[i][j] == 11) {
            		nbText.setText("L");
            		nbText.setFill(Color.WHITE);
            	}
        	}
        }
		
	}
	
	public void pauseGame(Pane pane) {
		if(gamePaused == true) {
			
			pane.setStyle("-fx-background-color: #000000;");
			
			gamePaused = false;
		}
		else {
			
			 pane.setStyle("-fx-background-color: rgb(211, 211, 211);");
			 Text pause1 = new Text("Paused");
			 Text pause2 = new Text("Press space again to resume");
			 
			 pause1.setX(XMAX/2/1.25);
			 pause1.setY(YMAX/2/1.12);
			 pause1.setFont(Font.font(boardsize));
			 
			 pause2.setX(XMAX/2/1.7);
			 pause2.setY(1.12*YMAX/2/1.12);
			 pause2.setFont(Font.font(boardsize - 10));
			 
			 
			 pane.getChildren().add(pause1);
			 pane.getChildren().add(pause2);
			
			gamePaused = true;
		}

	    
    }
	
	public Pane returnPane() {
		
		return pane;
		
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
                case "rotateKey_Player2":
                	rotateKey_Player2 = KeyCode.valueOf(value);
                    break;
                case "teleportKey_Player2":
                	teleportKey_Player2 = KeyCode.valueOf(value);
                    break;
                case "leftKey_Player2":
                	leftKey_Player2 = KeyCode.valueOf(value);
                    break;
                case "downKey_Player2":
                	downKey_Player2 = KeyCode.valueOf(value);
                    break;
                case "rightKey_Player2":
                	rightKey_Player2 = KeyCode.valueOf(value);
                    break;
                    
                    
                case "gameSize":
                	gameSize = Integer.parseInt(value);
                	break;
                case "colorBlindMode":
                	colorBlindMode = Integer.parseInt(value);
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
        
//        //mac용 
//        SIZE = gameSize*5 + 20; //윈도우 창 한 칸의 크기
//        xPoint = 22; //가로 칸 수 21
//        yPoint = 24; //세로 칸 수 24
//        XMAX = SIZE * xPoint; //윈도우의 실제 가로 크기
//        YMAX = SIZE * yPoint; //윈도우의 실제 세로 크기
//        boardsize = SIZE;//pane에서의 한칸의 크기, Text를 배치할때 좌표로 활용.
//        blocksize = 1.18*boardsize; //테트리스 블럭 크기
//        interver = boardsize;
//        dlsize = boardsize;
//        scoresize = blocksize;
	}

//	public static void main(String[] args) {
//		
//		// TODO Auto-generated method stub
//		
//		launch(args);
//
//	}
	private void centerStage(Stage stage) {
	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); // 화면의 크기를 얻음
	    
	    // 스테이지의 크기를 고려하여 중앙에 배치
	    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2 + screenBounds.getMinX());
	    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2 + screenBounds.getMinY());
	}
	
    public Tetris2 getTetris() {
    	return this.inGame2;
    }
}