package start;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ScoreBoard {
	public static VBox scoreBox;
	public static Scene scene;
	public static StartMenu startMenu=new StartMenu();
	//scoreBoard scene 생성
	public static Scene createScene(Stage primaryStage) {	
		
		// 첫 번째 VBox 생성
	    VBox scoreBox1 = createScoreBox(primaryStage,"Standard Mode");
	    // 두 번째 VBox 생성
	    VBox scoreBox2 = createScoreBox(primaryStage,"Item Mode");
	    //// GridPane 생성
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20); // 가로 간격 설정

        // 첫 번째 열(Column)에 대한 너비 설정
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50); // 첫 번째 열의 너비를 전체의 50%로 설정
        gridPane.getColumnConstraints().add(column1);

        // 두 번째 열(Column)에 대한 너비 설정
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50); // 두 번째 열의 너비를 전체의 50%로 설정
        gridPane.getColumnConstraints().add(column2);

        // 첫 번째 행(Row)에 대한 높이 설정
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(90); // 첫 번째 행의 높이를 전체의 80%로 설정
        gridPane.getRowConstraints().add(rowConstraints);
        
        // GridPane에 VBox 추가
        gridPane.add(scoreBox1, 0, 0); // 첫 번째 열(Column)의 첫 번째 행(Row)
        gridPane.add(scoreBox2, 1, 0); // 두 번째 열(Column)의 첫 번째 행(Row
	    gridPane.setStyle("-fx-background-color: #FFFFFF;");
	    
	    // 뒤로 가기 버튼 추가
	    Button backButton = new Button("Back");
	    backButton.setOnAction(event -> {
	        primaryStage.setScene(StartMenu.scene);
	    });
	    
	    // GridPane의 아래쪽 중앙에 버튼 추가
	    GridPane.setHalignment(backButton, HPos.CENTER); // 가로 정렬 설정
	    GridPane.setValignment(backButton, VPos.BOTTOM); // 세로 정렬 설정
	    gridPane.add(backButton, 0, 1, 2, 1); // 첫 번째 열(Column)의 두 번째 행(Row)에 스팬을 지정하여 2열에 걸쳐 버튼 추가
	    
	    Scene scene = new Scene(gridPane, 2*StartMenu.XSIZE, StartMenu.YSIZE);
	    
	    return scene;
	}
	
	private static VBox createScoreBox(Stage primaryStage,String mode) {
		scoreBox = new VBox(20);
	    scoreBox.setStyle("-fx-background-color: #FFFFFF;");
	    scoreBox.setAlignment(Pos.CENTER);
      
	    if(mode.equals("Standard Mode")) {
	    	// "Standard Mode" 텍스트 추가
		    Text modeText = new Text("Standard Mode");
		    modeText.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		    modeText.setFill(Color.BLACK);
		    scoreBox.getChildren().add(modeText);
	    }
	    else {
	    	// "Item mode" 텍스트 추가
		    Text modeText = new Text("Item Mode");
		    modeText.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		    modeText.setFill(Color.BLACK);
		    scoreBox.getChildren().add(modeText);
	    }
	    
	    List<String> scores = readScoresFromFile(mode);//txt 파일에서 score들을 읽어옴
	    
	    Text text = new Text("   순위   이름  점수");
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        text.setFill(Color.BLACK);
        scoreBox.getChildren().add(text);
        
	    // 스코어를 텍스트로 변환하여 VBox에 추가, 자리가 남으면 공석
	    for (int i = 0; i < 10; i++) {
	    	if (i < scores.size() && !scores.get(i).isEmpty()) {
	    		Text scoreText = new Text(i+1+"위 :  "+scores.get(i));
		        scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		        scoreText.setFill(Color.BLACK);
		        scoreBox.getChildren().add(scoreText);
	    	}
	    	else {
	    		Text scoreText = new Text(i+1+"위 :  공석 0");
	    		scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		        scoreText.setFill(Color.BLACK);
		        scoreBox.getChildren().add(scoreText);
	    	}
	    }
		return scoreBox;
	}

	//오버로딩 게임 끝났을 때 방금 기록 강조효과와 스코어 보드에 게임종료 버튼 추가를 위해 사용 , 위의 기본함수는 scoreBoard판 확인했을 때 용도임 -> 빨간색 강조효과 없음
	public static Scene createScene(Stage primaryStage,int score,String name,String mode) {
		// 첫 번째 VBox 생성
	    VBox scoreBox1 = createScoreBox_add(primaryStage,"Standard Mode",score,name);
	    // 두 번째 VBox 생성
	    VBox scoreBox2 = createScoreBox_add(primaryStage,"Item Mode",score,name);
	    //// GridPane 생성
	    if(mode.equals("Standard Mode")) {
	    	scoreBox1.getChildren().get(0).setStyle("-fx-fill: red;");
	    }
	    else {
	    	scoreBox2.getChildren().get(0).setStyle("-fx-fill: red;");
	    }
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20); // 가로 간격 설정

        // 첫 번째 열(Column)에 대한 너비 설정
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50); // 첫 번째 열의 너비를 전체의 50%로 설정
        gridPane.getColumnConstraints().add(column1);

        // 두 번째 열(Column)에 대한 너비 설정
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50); // 두 번째 열의 너비를 전체의 50%로 설정
        gridPane.getColumnConstraints().add(column2);

        // 첫 번째 행(Row)에 대한 높이 설정
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(90); // 첫 번째 행의 높이를 전체의 80%로 설정
        gridPane.getRowConstraints().add(rowConstraints);
        
        // GridPane에 VBox 추가
        gridPane.add(scoreBox1, 0, 0); // 첫 번째 열(Column)의 첫 번째 행(Row)
        gridPane.add(scoreBox2, 1, 0); // 두 번째 열(Column)의 첫 번째 행(Row
	    gridPane.setStyle("-fx-background-color: #FFFFFF;");
	    
	    // 뒤로 가기 버튼 추가
	    Button backButton = new Button("Back");
	    backButton.setOnAction(event -> {
	        primaryStage.setScene(StartMenu.scene);
	    });
	    
	    Button exitButton = new Button("Exit");
	    exitButton.setOnAction(event -> {
	    	System.exit(0);
	    });
	    
	    gridPane.add(backButton, 0, 1); 
	    GridPane.setHalignment(backButton, HPos.CENTER); 
	   
	    gridPane.add(exitButton, 1, 1); 
	    GridPane.setHalignment(exitButton, HPos.CENTER); 
	    	    
	    Scene scene = new Scene(gridPane, 2*StartMenu.XSIZE, StartMenu.YSIZE);
	    
	    return scene;
	}
	
	private static VBox createScoreBox_add(Stage primaryStage,String mode,int score,String name) {
		scoreBox = new VBox(20);
	    scoreBox.setStyle("-fx-background-color: #FFFFFF;");
	    scoreBox.setAlignment(Pos.CENTER);
      
	    if(mode.equals("Standard Mode")) {
	    	// "Standard Mode" 텍스트 추가
		    Text modeText = new Text("Standard Mode");
		    modeText.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		    modeText.setFill(Color.BLACK);
		    scoreBox.getChildren().add(modeText);
	    }
	    else {
	    	// "Item mode" 텍스트 추가
		    Text modeText = new Text("Item Mode");
		    modeText.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		    modeText.setFill(Color.BLACK);
		    scoreBox.getChildren().add(modeText);
	    }
	    
	    List<String> scores = readScoresFromFile(mode);//txt 파일에서 score들을 읽어옴
	    
	    Text text = new Text("   순위   이름  점수");
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        text.setFill(Color.BLACK);
        scoreBox.getChildren().add(text);
        
	    // 스코어를 텍스트로 변환하여 VBox에 추가, 자리가 남으면 공석
	    for (int i = 0; i < 10; i++) {
	    	if (i < scores.size() && !scores.get(i).isEmpty()) {
	    		Text scoreText = new Text(i+1+"위 :  "+scores.get(i));
		        scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		        if (scores.get(i).equals(name + " / " + score)) {
		            scoreText.setFill(Color.RED);
		        }
		        else {
		        	scoreText.setFill(Color.BLACK);
		        }
		        scoreBox.getChildren().add(scoreText);
	    	}
	    	else {
	    		Text scoreText = new Text(i+1+"위 :  공석 0");
	    		scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		        scoreText.setFill(Color.BLACK);
		        scoreBox.getChildren().add(scoreText);
	    	}
	    }
		return scoreBox;
	}

	//게임 종료시 랭크 안에 든다면 뜨는 팝업창을 위한 함수
	public static void showSettingDialog(int score,Stage primaryStage,String mode) {
		
		boolean isInRanking = isScoreInRanking(score,mode);//현재 점수가 랭킹에 들어가는지?
		
		if (isInRanking) {
			TextInputDialog dialog = new TextInputDialog();
	        dialog.setTitle("점수 기록");
	        dialog.setHeaderText("8글자 이하로 이름을 입력해주세요. 특수문자와 띄어쓰기는 불허. (tab으로 포커싱 이동, enter로 확인)");
	        dialog.setContentText("이름 : ");

	        Optional<String> result = dialog.showAndWait();//초기값 null 방지하기 위해 Optional 클래스 사용
	        result.ifPresentOrElse(name -> {
	            if (name.length() <= 8 && isValidName(name)) {//이름 길이가 8 이하, 특수문자 및 띄어쓰기가 없어야 함 
	                addScoreToFile(name, score,mode); //이름과 해당 판의 score를 txt에 추가
	                primaryStage.setScene(ScoreBoard.createScene(primaryStage,score,name,mode)); //스코어보드 scene으로 설정
	            } 
	            else {
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Error");
	                alert.setHeaderText(null);
	                alert.setContentText("이름은 8글자 이하! 특수문자와 띄어쓰기는 포함될 수 없습니다!");
	                alert.showAndWait();
	                showSettingDialog(score,primaryStage,mode);
	            }
	        },
	        ()->{primaryStage.setScene(ScoreBoard.createScene(primaryStage));}//랭킹 등록하기 싫어서 취소버튼누르면 
	        );
		}
		else {
			//기록 name 입력받는 프롬프트창 없이 바로 스코어보드 scene으로 설정
			primaryStage.setScene(ScoreBoard.createScene(primaryStage));
			}
    }
	
	
	
	private static boolean isScoreInRanking(int score,String mode) {
		List<String> scores = readScoresFromFile(mode);
	    if (scores.size() < 10) {
	        return true; // 랭킹에 공간이 있으면 무조건 들어감
	    } 
	    else {
	        // 점수가 랭킹에 들어가는지 확인하는 코드 작성
	        // 점수가 랭킹에 들어가면 true, 아니면 false 반환
	        int minScore = Integer.parseInt(scores.get(scores.size() - 1).split("/")[1].trim()); // 최하 점수 가져오기
	        return score > minScore;
	    }
	}
	
	
	
	private static List<String> readScoresFromFile(String mode) {
        List<String> scores = new ArrayList<>();
        if(mode.equals("Standard Mode")) {
        	try (BufferedReader reader = new BufferedReader(new FileReader("src/score.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    scores.add(line);
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }

            return scores;
        }
        else {
        	try (BufferedReader reader = new BufferedReader(new FileReader("src/scoreItem.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    scores.add(line);
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }

            return scores;
        }
    }
	
	

    private static void addScoreToFile(String name, int score,String mode) {
    	if(mode.equals("Standard Mode")) {
    		 try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/score.txt", true))) {
    	            writer.write(name + " / " + score); //새로운 기록 작성
    	            writer.newLine();
    	        } 
    	        catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	        sortScoresAndSaveToFile(mode); // 파일을 정렬하고 다시 저장
    	}
    	else {
    		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/scoreItem.txt", true))) {
	            writer.write(name + " / " + score); //새로운 기록 작성
	            writer.newLine();
	        } 
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        sortScoresAndSaveToFile(mode); // 파일을 정렬하고 다시 저장
    	}
    }
    

    
    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z0-9ㄱ-힣]+");//영어와 한글,숫자만 가능
    }
    

    
    private static void sortScoresAndSaveToFile(String mode) {
        List<String> scores = readScoresFromFile(mode);
        if(mode.equals("Standard Mode")) {
        	if (scores.size()>1) {
                sortScores(scores); // 새롭게 추가한 기록을 포함하여 점수를 기준으로 정렬 (기록이 2개 이상일 때)
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/score.txt"))) {
                for (String score : scores) {
                    writer.write(score);// 정렬한 것으로 txt파일에 다시 쓴다.
                    writer.newLine();
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
        	if (scores.size()>1) {
                sortScores(scores); // 새롭게 추가한 기록을 포함하여 점수를 기준으로 정렬 (기록이 2개 이상일 때)
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/scoreItem.txt"))) {
                for (String score : scores) {
                    writer.write(score);// 정렬한 것으로 txt파일에 다시 쓴다.
                    writer.newLine();
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

    
    private static void sortScores(List<String> scores) {//정렬
        Collections.sort(scores, (s1, s2) -> {
            int score1 = Integer.parseInt(s1.split("/")[1].trim());
            int score2 = Integer.parseInt(s2.split("/")[1].trim());
            return Integer.compare(score2, score1); // 내림차순 정렬
        });
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear(); //만약 기록이 10개 이상이면 10개까지만 유지
        }
    }
}
