package application;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class SettingCtrl {
	private Stage primaryStage; // 현재 스테이지 참조
    private Scene previousScene; // 이전 씬 참조
    
    @FXML
    private Button rotateButton, teleportButton,
    leftButton, downButton, rightButton,
    saveButton, closeButton; 
    
    @FXML
    private CheckBox colorBlind; // FXML 파일에서 정의한 체크박스와 연결
    
    @FXML private ChoiceBox<String> screenSizeChoice, difficultyChoice;
    
    private String[] difficultyList = {"easy","normal","hard"};
    private String[] screenSizeList = {"작게","보통","크게"};
    
    //설정파일 변수
    	//키코드
    private KeyCode rotateKey = KeyCode.U, 
    		teleportKey = KeyCode.T, 
    		leftKey = KeyCode.LEFT, 
    		downKey = KeyCode.DOWN, 
    		rightKey = KeyCode.RIGHT;
    	//화면 크기
    private int gameSize = 2;
    	//색맹모드
    private int colorBlindMode = 0;
    	//난이도
    private int gameDifficulty = 2;
    
    //Setting.txt 파일로부터 설정을 읽어들어서 Key 변수들에 KeyCode설정
    	//설정파일의 위치는 src/Settings.txt
    	//현재 SettingCtrl.java의 위치는 src/application/SettingCtrl.java
    	//설정파일로부터 값을 읽어와서 KeyCode 5개 초기화하기
    	//Button 5개의 텍스트 KeyCode의 키 텍스트로 변경하기
    
    @FXML
    private void initialize() {
        // 난이도 ChoiceBox에 항목 추가
        difficultyChoice.getItems().addAll(difficultyList);
        difficultyChoice.setOnAction(this::getDifficulty);

        // 화면 크기 ChoiceBox에 항목 추가
        screenSizeChoice.getItems().addAll(screenSizeList);
        screenSizeChoice.setOnAction(this::getScreenSize);
        
        // 설정값들 불러와서 저장
        settingConfigLoader(); 

        // ChoiceBox에 대한 키 이벤트 처리
        setupChoiceBoxKeyEvents(difficultyChoice);
        setupChoiceBoxKeyEvents(screenSizeChoice);
    }
    
    private void setupChoiceBoxKeyEvents(ChoiceBox<String> choiceBox) {
        choiceBox.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    // 엔터키가 눌리면 ChoiceBox를 열도록 시뮬레이션
                    if (!choiceBox.isShowing()) {
                        choiceBox.show();
                    } else {
                        choiceBox.hide();
                    }
                    break;
                default:
                    break;
            }
        });
    }
    
    public void getScreenSize(ActionEvent event) {
    	String screenSize = screenSizeChoice.getValue();
    	switch(screenSize) {
    	case "작게":
    		gameSize = 1;
    		break;
    	case "보통":
    		gameSize = 2;
    		break;
    	case "크게":
    		gameSize = 3;
    		break;
    	}
    }
    
    public void getDifficulty(ActionEvent event) {
    	String difficulty = difficultyChoice.getValue();
    	switch(difficulty) {
    	case "easy":
    		gameDifficulty = 1;
    		break;
    	case "normal":
    		gameDifficulty = 2;
    		break;
    	case "hard":
    		gameDifficulty = 3;
    		break;
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
                    case "rotateKey":
                        rotateKey = KeyCode.valueOf(value);
                        rotateButton.setText(value); // 버튼 텍스트도 변경
                        break;
                    case "teleportKey":
                        teleportKey = KeyCode.valueOf(value);
                        teleportButton.setText(value);
                        break;
                    case "leftKey":
                        leftKey = KeyCode.valueOf(value);
                        leftButton.setText(value);
                        break;
                    case "downKey":
                        downKey = KeyCode.valueOf(value);
                        downButton.setText(value);
                        break;
                    case "rightKey":
                        rightKey = KeyCode.valueOf(value);
                        rightButton.setText(value);
                        break;
                    case "gameSize":
                    	gameSize = Integer.parseInt(value);
                    	break;
                    case "colorBlindMode":
                    	colorBlindMode = Integer.parseInt(value);
                    	break;
                    case "difficulty":
                    	gameDifficulty = Integer.parseInt(value);
                    	break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 읽기 실패 시, 적절한 예외 처리나 사용자 알림이 필요할 수 있습니다.
        }
        
        colorBlindSet();
        //색맹모드 체크박스 동기화
        
        //Choice 박스 초기화
        switch(gameSize) {
    	case 1:
            screenSizeChoice.setValue("작게");
    		break;
    	case 2:
            screenSizeChoice.setValue("보통");
    		break;
    	case 3:
            screenSizeChoice.setValue("크게");
    		break;
    	}
        
        switch(gameDifficulty) {
    	case 1:
    		difficultyChoice.setValue("easy");
    		break;
    	case 2:
    		difficultyChoice.setValue("normal");
    		break;
    	case 3:
    		difficultyChoice.setValue("hard");
    		break;
    	}
    }
	
	public void setStageAndScene(Stage stage, Scene scene) {
        this.primaryStage = stage;
        this.previousScene = scene;
    }
    
    public void back(ActionEvent e) {
        primaryStage.setScene(previousScene);
    }
    
    public void colorBlindOnOff() {
    	if(colorBlindMode == 0) {
    		colorBlindMode = 1;
    	}
    	else {
    		colorBlindMode = 0;
    	}
    }
    
    public void colorBlindSet() {
    	if(colorBlindMode == 0) {
    		//체크박스를 비운 상태로 표시
    		colorBlind.setSelected(false);
    	}
    	if(colorBlindMode == 1) {
    		//체크박스에 체크된 상태로 표시
    		colorBlind.setSelected(true);
    	}
    }
    
    public void setRotateButton() {
    		primaryStage.getScene().getRoot().requestFocus();
    		//버튼 클릭시 포커스 해제 
    		primaryStage.getScene().setOnKeyPressed(keyEvent ->{
    			KeyCode code = keyEvent.getCode();
    			rotateButton.setText(code.toString());
    			//키 입력시 키텍스트를 누른 키로 변경
    			rotateKey = KeyCode.valueOf(rotateButton.getText());
    			//변경된 키 텍스트를 키코드 변수에 저장
    			System.out.println(rotateKey);
    			//테스트 출력
    			rotateButton.requestFocus();
	    		//버튼에 포커스 되돌리기
				primaryStage.getScene().setOnKeyPressed(null);
				//키가 눌린 이벤트를 해제
    		});
    }
    
    public void setTeleportButton() {
		primaryStage.getScene().getRoot().requestFocus();
		//버튼 클릭시 포커스 해제 
		primaryStage.getScene().setOnKeyPressed(keyEvent ->{
			KeyCode code = keyEvent.getCode();
			teleportButton.setText(code.toString());
			//키 입력시 키텍스트를 누른 키로 변경
			teleportKey = KeyCode.valueOf(teleportButton.getText());
			//변경된 키 텍스트를 키코드 변수에 저장
			teleportButton.requestFocus();
    		//버튼에 포커스 되돌리기
			primaryStage.getScene().setOnKeyPressed(null);
			//키가 눌린 이벤트를 해제
		});
    }
    
    public void setLeftButton() {
		primaryStage.getScene().getRoot().requestFocus();
		//버튼 클릭시 포커스 해제 
		primaryStage.getScene().setOnKeyPressed(keyEvent ->{
			KeyCode code = keyEvent.getCode();
			leftButton.setText(code.toString());
			//키 입력시 키텍스트를 누른 키로 변경
			leftKey = KeyCode.valueOf(leftButton.getText());
			//변경된 키 텍스트를 키코드 변수에 저장
			leftButton.requestFocus();
    		//버튼에 포커스 되돌리기
			primaryStage.getScene().setOnKeyPressed(null);
			//키가 눌린 이벤트를 해제
		});
    }
    
    public void setDownButton() {
		primaryStage.getScene().getRoot().requestFocus();
		//버튼 클릭시 포커스 해제 
		primaryStage.getScene().setOnKeyPressed(keyEvent ->{
			KeyCode code = keyEvent.getCode();
			downButton.setText(code.toString());
			//키 입력시 키텍스트를 누른 키로 변경
			downKey = KeyCode.valueOf(downButton.getText());
			//변경된 키 텍스트를 키코드 변수에 저장
			downButton.requestFocus();
    		//버튼에 포커스 되돌리기
			primaryStage.getScene().setOnKeyPressed(null);
			//키가 눌린 이벤트를 해제
		});
    }
    
    public void setRightButton() {
		primaryStage.getScene().getRoot().requestFocus();
		//버튼 클릭시 포커스 해제 
		primaryStage.getScene().setOnKeyPressed(keyEvent ->{
			KeyCode code = keyEvent.getCode();
			rightButton.setText(code.toString());
			//키 입력시 키텍스트를 누른 키로 변경
			rightKey = KeyCode.valueOf(rightButton.getText());
			//변경된 키 텍스트를 키코드 변수에 저장
			rightButton.requestFocus();
    		//버튼에 포커스 되돌리기
			primaryStage.getScene().setOnKeyPressed(null);
			//키가 눌린 이벤트를 해제
		});
    }
    
    public KeyCode getKeyCodeFromString(String keyName) {//주어진 텍스트를 키코드로 변환시켜주 함
        try {
            return KeyCode.valueOf(keyName);
        } catch (IllegalArgumentException e) {
            // 주어진 이름에 해당하는 KeyCode가 없는 경우 예외 처리
            System.out.println("해당 이름의 KeyCode가 없습니다: " + keyName);
            return KeyCode.UNDEFINED; // 또는 기본 KeyCode를 반환할 수도 있음
        }
    }
    
    //Key 변수들의 값을 Setting.txt파일에 형식에 맞추어 저장해주는 함수
    public void saveKeyConfigurations() {
        String filePath = "src/Settings.txt";
        try {
            // 파일에서 기존 설정을 읽어옴
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            
            // 설정을 저장할 맵 생성
            Map<String, String> settingsMap = new HashMap<>();
            
            // 파일에서 읽은 설정을 맵에 저장
            for (String line : lines) {
                String[] parts = line.split(" = ");
                if (parts.length < 2) continue; // 유효하지 않은 라인은 건너뜀
                settingsMap.put(parts[0].trim(), parts[1].trim().replaceAll("\"", ""));
            }
            
            // 새로운 설정으로 맵 업데이트
            settingsMap.put("rotateKey", "\"" + rotateKey.toString() + "\"");
            settingsMap.put("teleportKey", "\"" + teleportKey.toString() + "\"");
            settingsMap.put("leftKey", "\"" + leftKey.toString() + "\"");
            settingsMap.put("downKey", "\"" + downKey.toString() + "\"");
            settingsMap.put("rightKey", "\"" + rightKey.toString() + "\"");
            settingsMap.put("gameSize", "\"" + Integer.toString(gameSize) + "\"");
            settingsMap.put("colorBlindMode", "\"" + Integer.toString(colorBlindMode) + "\"");
            settingsMap.put("difficulty", "\"" + Integer.toString(gameDifficulty) + "\"");

            
            // 맵의 내용을 기반으로 새로운 설정 문자열 생성
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : settingsMap.entrySet()) {
                sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
            }
            
            // 변경된 설정을 파일에 쓰기
            Files.write(Paths.get(filePath), sb.toString().getBytes());
            System.out.println("키 설정이 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("키 설정 저장 중 오류가 발생했습니다.");
        }
    }
    
    
    public void resetScoreFile() {
    	//score.txt파일을 완전히 비우는 함
        String filePath = "src/score.txt";
        try {
            Files.write(Paths.get(filePath), new byte[0]); // 파일 내용을 비웁니다.
            System.out.println("스코어 파일이 성공적으로 초기화되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("스코어 파일 초기화 중 오류가 발생했습니다.");
        }
    }
    
    
    public void resetSettingFile() {
        String filePath = "src/Settings.txt"; // 설정 파일 경로
        
        rotateKey = KeyCode.U;
        teleportKey = KeyCode.T; 
        leftKey = KeyCode.LEFT; 
        downKey = KeyCode.DOWN; 
        rightKey = KeyCode.RIGHT;
        gameSize = 2;
        colorBlindMode = 0;
        gameDifficulty = 2;
        //변수들 기본값으로 초기화 
        
        downButton.setText(KeyCode.DOWN.toString());
        rightButton.setText(KeyCode.RIGHT.toString());
        leftButton.setText(KeyCode.LEFT.toString());
        rotateButton.setText(KeyCode.U.toString());
        teleportButton.setText(KeyCode.T.toString());     
        colorBlind.setSelected(false);
        difficultyChoice.setValue("normal");
        screenSizeChoice.setValue("보통");
    }
    
}
