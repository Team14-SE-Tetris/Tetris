import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

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
	//scoreBoard scene 생성
	public static Scene createScene(Stage primaryStage) {	
	    VBox scoreBox = new VBox(20);
	    scoreBox.setStyle("-fx-background-color: #FFFFFF;");
	    scoreBox.setAlignment(Pos.CENTER);

	    List<String> scores = readScoresFromFile();//txt 파일에서 score들을 읽어옴
	    
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

	    // 뒤로 가기 버튼 추가
	    Button backButton = new Button("Back");
	    backButton.setOnAction(event -> {
	        primaryStage.setScene(StartMenu.scene);
	    });
	    
	    scoreBox.getChildren().add(backButton);
	
	    return new Scene(scoreBox, StartMenu.XSIZE,StartMenu.YSIZE);
	}
	
	//오버로딩 게임 끝났을 때 방금 기록 강조효과와 스코어 보드에 게임종료 버튼 추가를 위해 사용 , 위의 기본함수는 scoreBoard판 확인했을 때 용도임 -> 빨간색 강조효과 없음
	public static Scene createScene(Stage primaryStage,int score,String name) {
	    VBox scoreBox = new VBox(20);
	    scoreBox.setStyle("-fx-background-color: #FFFFFF;");
	    scoreBox.setAlignment(Pos.CENTER);

	    List<String> scores = readScoresFromFile();//txt 파일에서 score들을 읽어옴
	    
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

	    // 뒤로 가기 버튼 추가
	    Button backButton = new Button("Back");
	    backButton.setOnAction(event -> {
	        primaryStage.setScene(StartMenu.scene);
	    });
	    
	    Button exitButton = new Button("Exit");
	    exitButton.setOnAction(event -> {
	    	System.exit(0);
	    });

	    scoreBox.getChildren().add(backButton);
	    scoreBox.getChildren().add(exitButton);
	    
	    // StartMenu의 XSIZE와 YSIZE로 scene을 return
	    return new Scene(scoreBox, StartMenu.XSIZE,StartMenu.YSIZE);
	}
	

	//게임 종료시 랭크 안에 든다면 뜨는 팝업창을 위한 함수
	public static void showSettingDialog(int score,Stage primaryStage) {
		
		boolean isInRanking = isScoreInRanking(score);//현재 점수가 랭킹에 들어가는지?
		
		if (isInRanking) {
			TextInputDialog dialog = new TextInputDialog();
	        dialog.setTitle("점수 기록");
	        dialog.setHeaderText("8글자 이하로 이름을 입력해주세요. (특수문자와 띄어쓰기 불허)");
	        dialog.setContentText("이름 : ");

	        Optional<String> result = dialog.showAndWait();//초기값 null 방지하기 위해 Optional 클래스 사용
	        result.ifPresent(name -> {
	            if (name.length() <= 8 && isValidName(name)) {//이름 길이가 8 이하, 특수문자 및 띄어쓰기가 없어야 함 
	                addScoreToFile(name, score); //이름과 해당 판의 score를 txt에 추가
	                primaryStage.setScene(ScoreBoard.createScene(primaryStage,score,name)); //스코어보드 scene으로 설정
	            } 
	            else {
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Error");
	                alert.setHeaderText(null);
	                alert.setContentText("이름은 8글자 이하! 특수문자와 띄어쓰기는 포함될 수 없습니다!");
	                alert.showAndWait();
	                showSettingDialog(score,primaryStage);
	            }
	        });
		}
		else {
			//기록 name 입력받는 프롬프트창 없이 바로 스코어보드 scene으로 설정
			primaryStage.setScene(ScoreBoard.createScene(primaryStage));
			}
    }
	
	
	
	private static boolean isScoreInRanking(int score) {
		List<String> scores = readScoresFromFile();
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
	
	
	
	private static List<String> readScoresFromFile() {
        List<String> scores = new ArrayList<>();

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
	
	

    private static void addScoreToFile(String name, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/score.txt", true))) {
            writer.write(name + " / " + score); //새로운 기록 작성
            writer.newLine();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        sortScoresAndSaveToFile(); // 파일을 정렬하고 다시 저장
    }
    

    
    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z0-9ㄱ-힣]+");//영어와 한글,숫자만 가능
    }
    

    
    private static void sortScoresAndSaveToFile() {
        List<String> scores = readScoresFromFile();
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
