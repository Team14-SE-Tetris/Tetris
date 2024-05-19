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
import tetris.Tetris2;
import tetris.Board2;
import javafx.concurrent.Task;


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
    public Pane pane2;
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
    
    public Tetris inGame;
    
    public Board2 Board2;
    
    public boolean gamePaused = false;
    
    private int liney = 0;
    private int gradation = 1;
    private int removedelay = 0;
    public boolean removeflag = true;
    private boolean delayflag = true;
    private int removestep = 0;
    
    private boolean telpoflag = false;
    
    public AnimationTimer timer;
    
	//설정파일 변수
		//키코드
	public KeyCode 
			rotateKey_Player1 = KeyCode.G, 
			teleportKey_Player1 = KeyCode.H, 
			leftKey_Player1 = KeyCode.A, 
			downKey_Player1 = KeyCode.S, 
			rightKey_Player1 = KeyCode.D,
	
			rotateKey_Player2 = KeyCode.PERIOD, 
			teleportKey_Player2 = KeyCode.SLASH, 
			leftKey_Player2 = KeyCode.LEFT, 
			downKey_Player2 = KeyCode.DOWN, 
			rightKey_Player2 = KeyCode.RIGHT;
	
		//화면 크기
		//위에 정의함, gameSize변수
		//색맹모드
	public int colorBlindMode = 0;
		//게임 난이도
	public int difficulty = 2;
	
	public int battleMode=0;
	
	public String difficultyText = "normal";
    
    public Board(int mode) {
    	timer = null;
    	settingConfigLoader();//Setting.txt파일에서 설정값들을 불러와 변수에 저장하는 함수 
    	inGame = new Tetris(difficulty);
    	pane = new Pane();
    	this.mode = mode;
    	if(mode==2) {
    		Board2 = new Board2(mode);
            pane2 = Board2.createpane(null);
        	SplitPane splitPane1 = new SplitPane();
            splitPane1.getItems().addAll(pane, pane2);
            pane.setStyle("-fx-background-color: #000000;");//배경 검은색 설정
            scene = new Scene(splitPane1, XMAX*2, YMAX);
            delayflag=true;
    	}
    	else{
    		Board2 = new Board2(mode);
            pane2 = Board2.createpane(null);
            pane.setStyle("-fx-background-color: #000000;");//배경 검은색 설정
            scene = new Scene(pane, XMAX, YMAX);
            delayflag=true;
            
    	}
    }
    
    public Board(int mode,int battleMode) { //battleMode 인수를 받았을 때 
    	this(mode);
    	this.battleMode=battleMode;
    }


	public Scene createScene(Stage primaryStage) {

		//initializeBoard(); -> inGame 객체 내부 시작

		System.out.println(1);// 테스트
		if(mode ==1){
        	inGame.changeMode(1);
        }
        else if(mode==0) {
        	inGame.changeMode(0);
        }
        else if(mode==2){
        	if(battleMode==1) {
        		inGame.changeMode(0);/* *********중요************  대전 일반모드*/
        		System.out.print("대전모드 일반 연결해야함");
        	}
        	else if(battleMode==2) {
        		inGame.changeMode(1);/* *********중요************  대전 아이템모드*/
        		System.out.print("대전모드 아이템 연결해야함");
        	}
        	else if(battleMode==3) {
        		inGame.changeMode(0);/* *********중요************  대전 타이머모드  나중에 연결해야 함*/
        		System.out.print("대전모드 타이머 연결해야함");
        	}
        }
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
            private long lastUpdate2 = 0;
            private long interval = inGame.getDropSpeed(); // 1초마다 이동
            private long interval2 = Board2.getDropspeed(); // 1초마다 이동
            private long delay = 300_000_000; // 딜레이 설정 (0.3초)
            public boolean isLineRemovalScheduled = false; // 줄 제거가 예정되었는지 확인
            public boolean isLineRemovalScheduled2 = false; // 줄 제거가 예정되었는지 확인
            private long removalScheduledTime = 0; // 줄 제거 예정 시간
            private long removalScheduledTime2 = 0; // 줄 제거 예정 시간

            @Override
            public void handle(long now) {
                if (!isLineRemovalScheduled) {
                    if (now - lastUpdate >= interval) { // interval 간격마다 수행
                    	telpoflag=false;
                        if (!inGame.moveDown()) { // moveDown 실패 시
                            if (inGame.checkLines() > 0 && inGame.checkLines() < 22) {
                                // 완성된 줄이 있는 경우, 줄 제거 예정으로 설정
                                isLineRemovalScheduled = true;
                                removalScheduledTime = now;
                            } else {
                                // 완성된 줄이 없고 블록을 초기화할 수 없는 경우 게임 오버 처리
                                if (!inGame.initialiBlock()) {
                                    this.stop();
                                    Platform.runLater(() -> {
                                        ScoreBoard scoreBoard = new ScoreBoard();
                                        if (mode == 0) {
                                            scoreBoard.showSettingDialog(score, primaryStage, "Standard Mode",difficulty);
                                        } else if(mode==1) {
                                            scoreBoard.showSettingDialog(score, primaryStage, "Item Mode",difficulty);
                                        } else {//대전모드인 경우 -> 승리조건이 타이머 모드와 기본+아이템 대전모드와 다름
                                        	if(battleMode==3) {//타이머 대전모드
                                        		
                                        	}
                                        	else {//기본 또는 아이템 대전모드
                                        		
                                        	}
                                        	Alert alert = new Alert(AlertType.INFORMATION);
                                            alert.setTitle("승리자");
                                            alert.setHeaderText("게임 결과");
                                            alert.setContentText("winnerName" + "(이)가 승리했습니다!");
                                            alert.showAndWait();
                                            primaryStage.setScene(StartMenu.scene);
                                	        centerStage(primaryStage);
                                        }
                                    });
                                }
                            }
                        }
                        lastUpdate = now;
                    }
                } else {
                	
                	removeflag = false;
     		
            		liney = inGame.checkLines();
            		
            		
            		if (removestep == 0) {//완성된 줄 확인 후 1번째 프레임

            				drawBoard();
            				lastUpdate = now;//완성된 줄 확인 후 1번째 프레임의 시간 기록

            		}
            		else if(removestep == 1) {//완성된 줄 확인 후 2번째 프레임

            				drawBoard();

            		}
            		else if(removestep == 2) {//완성된 줄 확인 후 3번째 프레임

            			if(now - lastUpdate >= delay) {//1번쨰 프레임 이후 0.3초 이상 지난 경우
            				drawBoard();
                    		inGame.removeLine(liney);//removeLine에서 checkline반환값이 0으로 변홤
                    		// vsModeBoardPrint() 가져오기
                    		removestep = 0;
                    		//placedelete_board()
            			}
            			
            			removestep--;
            		}
            		
            		if(!(inGame.checkLines() > 0 && inGame.checkLines() < 22)) {
            			isLineRemovalScheduled = false;
            			removeflag = true;
            			
            			if (!inGame.initialiBlock()) {
                          this.stop();
                          Platform.runLater(() -> {
                              ScoreBoard scoreBoard = new ScoreBoard();
                              if (mode == 0) {
                                  scoreBoard.showSettingDialog(score, primaryStage, "Standard Mode",difficulty);
                              } else {
                                  scoreBoard.showSettingDialog(score, primaryStage, "Item Mode",difficulty);
                              }
                          });
                      }
            		}
            		
            		removestep++;
                }
                
                if (!isLineRemovalScheduled2) { //Board2 컨트롤
                    if (now - lastUpdate2 >= interval2) { // interval 간격마다 수행
                    	Board2.telpoflag=false;
                        if (!Board2.movedown()) { // moveDown 실패 시
                            if (Board2.checkline() > 0 && Board2.checkline() < 22) {
                                // 완성된 줄이 있는 경우, 줄 제거 예정으로 설정
                                isLineRemovalScheduled2 = true;
                                removalScheduledTime2 = now;
                            } else {
                                // 완성된 줄이 없고 블록을 초기화할 수 없는 경우 게임 오버 처리
                                if (!Board2.initializeblock()) {
                                    this.stop();
                                    Platform.runLater(() -> {
                                        ScoreBoard scoreBoard = new ScoreBoard();
                                        if (mode == 0) {
                                            scoreBoard.showSettingDialog(score, primaryStage, "Standard Mode",difficulty);
                                        } else if(mode==1) {
                                            scoreBoard.showSettingDialog(score, primaryStage, "Item Mode",difficulty);
                                        } else {//대전모드인 경우
                                        	Alert alert = new Alert(AlertType.INFORMATION);
                                            alert.setTitle("승리자");
                                            alert.setHeaderText("게임 결과");
                                            alert.setContentText("winnerName" + "(이)가 승리했습니다!");
                                            alert.showAndWait();
                                            primaryStage.setScene(StartMenu.scene);
                                	        centerStage(primaryStage);
                                        }
                                    });
                                }
                            }
                        }
                        lastUpdate2 = now;
                    }
                } else {
                	
                	Board2.removeflag = false;
     		
            		Board2.liney = Board2.checkline();
            		
            		
            		if (Board2.removestep == 0) {//완성된 줄 확인 후 1번째 프레임

            				Board2.drawBoard();
            				lastUpdate2 = now;//완성된 줄 확인 후 1번째 프레임의 시간 기록

            		}
            		else if(Board2.removestep == 1) {//완성된 줄 확인 후 2번째 프레임

            				Board2.drawBoard();

            		}
            		else if(Board2.removestep == 2) {//완성된 줄 확인 후 3번째 프레임

            			if(now - lastUpdate2 >= delay) {//1번쨰 프레임 이후 0.3초 이상 지난 경우
            				Board2.drawBoard();
            				Board2.removeline(Board2.liney); //removeLine에서 checkline반환값이 0으로 변홤
                    		Board2.removestep = 0;
            			}
            			
            			Board2.removestep--;
            		}
            		
            		if(!(Board2.checkline() > 0 && Board2.checkline() < 22)) {
            			isLineRemovalScheduled2 = false;
            			Board2.removeflag = true;
            			
            			if (!Board2.initializeblock()) {
                          this.stop();
                          Platform.runLater(() -> {
                              ScoreBoard scoreBoard = new ScoreBoard();
                              if (mode == 0) {
                                  scoreBoard.showSettingDialog(score, primaryStage, "Standard Mode",difficulty);
                              } else {
                                  scoreBoard.showSettingDialog(score, primaryStage, "Item Mode",difficulty);
                              }
                          });
                      }
            		}
            		
            		Board2.removestep++;
                }
                
                delayflag=true;
                drawBoard(); // 화면 업데이트
                deadLine();
                drawScore();
                Styleset();
                
                if(mode == 2) { 
                	drawLine();
                	Board2.delayflag = true;
                	Board2.drawBoard();
                	Board2.drawScore();
                }
                
            }
        };
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
        	@Override
            public void handle(KeyEvent event) {
                if (mode == 1 || mode == 0) {
        		    Block itemtest = inGame.getCurrentBlock();
        		    KeyCode keyCode = event.getCode();
        		    if (delayflag) {
        		        if (gamePaused == false) {
        		            if (telpoflag == false) {
        		                if (keyCode == leftKey_Player1) {
        		                    inGame.moveLeft();
        		                } else if (keyCode == rightKey_Player1) {
        		                    inGame.moveRight(); // 오른쪽으로 이동
        		                } else if (keyCode == downKey_Player1) {

        		                    if (!(inGame.checkBlock())) {
        		                        delayflag = false;
        		                        if (itemtest.getItem() == 5 && !(inGame.checkCollisionBottom(inGame.getCurrentX(), inGame.getCurrentY()))) {
        		                            delayflag = true;
        		                            inGame.moveDown();
        		                        }
        		                    } else {
        		                        inGame.moveDown();
        		                    }
        		                } else if (keyCode == teleportKey_Player1) {
        		                    telpoflag = true;
        		                    if (itemtest.getItem() == 5 && !(inGame.checkCollisionBottom(inGame.getCurrentX(), inGame.getCurrentY()))) {
        		                        delayflag = true;
        		                        inGame.moveBottom();
        		                    } else {
        		                        inGame.moveBottom();
        		                        delayflag = false;
        		                    }
        		                } else if (keyCode == rotateKey_Player1) {
        		                    inGame.rotateBlock();
        		                } else if (keyCode == KeyCode.Q) {
        		                    timer.stop();
        		                    primaryStage.setScene(StartMenu.scene);
        		                    inGame.resetVariable();
        		                    score = 0;
        		                    System.out.println("점수: " + score);
        		                    System.out.println("점수: " + inGame.getScore());
        		                    // 게임 종료시, inGame의 dropSpeed와 Score를 초기상태로 초기화
        		                    centerStage(primaryStage);
        		                }
        		            }

        		            }

    		            if (keyCode == KeyCode.SPACE) {
    		                if (gamePaused == false) {
    		                    timer.stop();
    		                } else {
    		                    timer.start();
    		                }
    		                pauseGame(pane);
        		        }
        		    }
                } else if (mode == 2) {
                	KeyCode keyCode = event.getCode();
                		Block itemtest1 = inGame.getCurrentBlock();
                		Block itemtest2 = Board2.getCurrentblock();
            		    
            		    if (delayflag) {
            		        if (gamePaused == false) {
            		            if (telpoflag == false) {
            		                if (keyCode == leftKey_Player1) {
            		                    inGame.moveLeft();
            		                } else if (keyCode == rightKey_Player1) {
            		                    inGame.moveRight(); // 오른쪽으로 이동
            		                } else if (keyCode == downKey_Player1) {

            		                    if (!(inGame.checkBlock())) {
            		                        delayflag = false;
            		                        if (itemtest1.getItem() == 5 && !(inGame.checkCollisionBottom(inGame.getCurrentX(), inGame.getCurrentY()))) {
            		                            delayflag = true;
            		                            inGame.moveDown();
            		                        }
            		                    } else {
            		                        inGame.moveDown();
            		                    }
            		                } else if (keyCode == teleportKey_Player1) {
            		                    telpoflag = true;
            		                    if (itemtest1.getItem() == 5 && !(inGame.checkCollisionBottom(inGame.getCurrentX(), inGame.getCurrentY()))) {
            		                        delayflag = true;
            		                        inGame.moveBottom();
            		                    } else {
            		                        inGame.moveBottom();
            		                        delayflag = false;
            		                    }
            		                } else if (keyCode == rotateKey_Player1) {
            		                    inGame.rotateBlock();
            		                } else if(keyCode == KeyCode.Q) {
                            			timer.stop();
                            			primaryStage.setScene(StartMenu.scene);
                            			
                            			inGame.resetVariable();
                            			Board2.resetVariable();
                            			score = 0;
                            			Board2.score = 0;
                            			
                            			System.out.println("Player1 점수: " + score);
                            			System.out.println("Player1 점수: " + inGame.getScore());
                            			
                            			System.out.println("Player2 점수: " + Board2.score);
                            			System.out.println("Player2 점수: " + Board2.getscore());
                            			//게임 종료시, Board2의 dropSpeed와 Score를 초기상태로 초기화
                            			
                                    	centerStage(primaryStage);
                            		}
            		                
            	        			
            		            }
            		        }//Gamepaused_flag
            		        
            		        if(keyCode == KeyCode.SPACE) {
                    			if(gamePaused == false) {
                    				timer.stop();
                    			}
                    			else {
                    				timer.start();
                    			}
                    			Board2.pauseGame(pane2);
        		                pauseGame(pane);
        		                
                    		}

            		    }
            		    //Board2 키 시작
                        if (Board2.delayflag) {
                        if(Board2.gamePaused == false) {
                        if(Board2.telpoflag == false) {
                        if (keyCode == leftKey_Player2) {
                        	Board2.moveleft();; // 왼쪽으로 이동
                        } else if (keyCode == rightKey_Player2) {
                            Board2.moveright(); // 오른쪽으로 이동
                        } else if (keyCode == downKey_Player2) {
                        	
                        	if(!(Board2.checkblock())) {
                        		Board2.delayflag=false;
                        		if(itemtest2.getItem()==5 && !(Board2.checkcollisionBottom(Board2.getcurrentx(),Board2.getcurrenty()))) {
                        			Board2.delayflag=true;
                        			Board2.movedown();
                        		}
                    		}
                        	else {
                        		Board2.movedown();
                        	}
                        } else if (keyCode == teleportKey_Player2) {
                        	Board2.telpoflag = true;
                        	if(itemtest2.getItem()==5 && !(Board2.checkcollisionBottom(Board2.getcurrentx(),Board2.getcurrenty()))) {
                        		Board2.delayflag=true;
                    			Board2.movebottom();
                    		} else {
                    			Board2.movebottom();
                    			Board2.delayflag=false;
                    		}
                		} else if (keyCode == rotateKey_Player2) {
                			Board2.rotateblock();
                		}
                		
                      }
                        
                    }

                    }//Board2.delayfalg
            		    
                }//mode == 2
            
        	}//keyevent_handler
        });
        
    	
        timer.start(); // AnimationTimer 시작
        
        return scene; 
    }
	
	/*private boolean IsKeyBoard1(KeyCode keyCode) {
		if(keyCode == rotateKey_Player1 || keyCode == teleportKey_Player1 || 
				keyCode == leftKey_Player1 || keyCode == downKey_Player1 || keyCode == rightKey_Player1) {
			return true;
		}
		else if(keyCode == rotateKey_Player2 || keyCode == teleportKey_Player2 || 
				keyCode == leftKey_Player2 || keyCode == downKey_Player2 || keyCode == rightKey_Player2) {
			return false;
		}
		return false;
	}*/
	
	private void handleKeyEventForBoard1(KeyEvent event) {
		 	
	}
	
	private void handleKeyEventForBoard2(KeyEvent event) {
	    // Board2에 대한 KeyEvent 처리 로직
	}
	
	// 수정됨 -> inGame 객체 내부의 숫자로 색 구분
	private void drawBoard() {
		
		pane.getChildren().clear(); //pane(게임화면) 비우기
		
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
            	Text cellText = new Text(String.valueOf(board[i][j])); //vsmodeprint()
                
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
                int[][] board = inGame.boardPrint();// 현재 Board 상태 출력
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
	
	public void drawLine() {
		
		for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
            	Text cellText = new Text(String.valueOf(board[i][j]));
            	
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
                 else if(i > 17) {
                	 cellText.setText("■");
                	 cellText.setFill(Color.GRAY);
                	 cellText.setX(j* interver/2.09 + boardsize*14 + 6);//블럭 X좌표
                     cellText.setY(i* interver/2.09 + boardsize*12 + 10);//블럭 Y좌표
                     cellText.setFont(Font.font(scoresize/1.3));//블럭사이즈
                	 
                 }
                 else {
                	 cellText.setText(" ");
                	 cellText.setFill(Color.BLACK);
                	 cellText.setX(j* interver/1.5 + boardsize*14);//블럭 X좌표
                     cellText.setY(i* interver/1.5 + boardsize*12);//블럭 Y좌표
                     cellText.setFont(Font.font(scoresize/1.1));//블럭사이즈
                	 
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
			Board2.gamePaused = false;
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
			Board2.gamePaused = true;
		}

	    
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
                case "rotateKey_Player1":
                    rotateKey_Player1 = KeyCode.valueOf(value);
                    break;
                case "teleportKey_Player1":
                    teleportKey_Player1 = KeyCode.valueOf(value);
                    break;
                case "leftKey_Player1":
                    leftKey_Player1 = KeyCode.valueOf(value);
                    break;
                case "downKey_Player1":
                    downKey_Player1 = KeyCode.valueOf(value);
                    break;
                case "rightKey_Player1":
                    rightKey_Player1 = KeyCode.valueOf(value);
                    break;
                    
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

	private void centerStage(Stage stage) {
	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); // 화면의 크기를 얻음
	    
	    // 스테이지의 크기를 고려하여 중앙에 배치
	    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2 + screenBounds.getMinX());
	    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2 + screenBounds.getMinY());
	}
}
