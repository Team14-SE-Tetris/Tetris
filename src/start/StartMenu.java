package start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import setting.SettingMenu;
import tetris.Board;
import setting.*;

public class StartMenu extends Application {
    public static int XSIZE = 400;	 
    public static int YSIZE = 600;
    public static Scene scene;
    public static Text[] menuItems = new Text[5];
    private int selectedIndex = 0;
    private static Stage primaryStage;
    
    Text menuTitle = new Text("Tetris by 14 Team");
    VBox menuTitleBox;
    VBox menuItemsBox;
    VBox menuBox;
    Board board;
    Text keyDescription;
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
  	
    public static Runnable updateKeyDescription;
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage=primaryStage;
        scene=createScene(primaryStage);
        //scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public Scene createScene(Stage primaryStage) {
    	primaryStage.setTitle("Tetris");
    	settingConfigLoader();
        menuTitle = new Text("Tetris by 14 Team");
        menuTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 30));

        menuItems[0] = createMenuItem("Standard Game"); //메뉴 종류
        menuItems[1] = createMenuItem("Item Mode Game"); //메뉴 종류
        menuItems[2] = createMenuItem("Setting");
        menuItems[3] = createMenuItem("Score Board");
        menuItems[4] = createMenuItem("Exit");

        menuTitleBox = new VBox(menuTitle);
        menuTitleBox.setAlignment(Pos.CENTER);
        
        menuItemsBox = new VBox(30);
        menuItemsBox.getChildren().addAll(menuItems);
        menuItemsBox.setAlignment(Pos.CENTER);

        
        menuBox = new VBox(100, menuTitleBox, menuItemsBox);
        
     // 키 설명을 담는 Text 객체 생성
        keyDescription = new Text();
        keyDescription.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
      
        // 키 설명을 변경하는 메서드
        updateKeyDescription = () -> {
        	settingConfigLoader();
            keyDescription.setText("아래방향키 및 블럭 아래 이동 키: " + downKey.toString() + "\n"
            				+ "게임 중 메인화면으로 돌아가기: Q" + "\n"
            				+ "게임 중 일시정지/재개: SPACE바" + "\n"
                            + "윗방향키 및 블럭 순간 이동 키: " + teleportKey.toString() + "\n"
                            + "오른쪽방향키 및 블럭 오른쪽 이동 키: " + rightKey.toString() + "\n"
                            + "왼쪽방향키 및 블럭 왼쪽 이동 키: " + leftKey.toString() + "\n"
                            + "블럭 회전 키: " + rotateKey.toString()+ "\n"
                            + "(UP, DOWN, LEFT, RIGHT은 화살표 키)");
        };
        
        updateKeyDescription.run();
        //키설명 text 업데이트 함수인 것 따라서 SettingCtrl에서 키 바꿀 때 실행하게 SettingCtrl.java에서 저장할 때 이 함수 실행시킴
                
        menuBox.getChildren().add(keyDescription);
        menuBox.setAlignment(Pos.CENTER);
        //menuBox.setStyle("-fx-border-style: solid; -fx-border-width: 5px; -fx-border-color: black;");
        
        scene = new Scene(menuBox, XSIZE, YSIZE);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                navigateMenu(1);
            } 
            else if (event.getCode() == KeyCode.UP) {
                navigateMenu(-1);
            } 
            else if (event.getCode() == KeyCode.ENTER) {
                executeSelectedMenuItem();
            }
        });
      //System.out.print(settingCtrl.getDownKey());
        
        return scene;
    }

    private Text createMenuItem(String label) {
        Text menuItem = new Text(label);
        menuItem.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        menuItem.setFill(Color.BLACK);
        if (label.equals("Standard Game")) { //default는 Game Start임
            menuItem.setFill(Color.RED);
        }
        return menuItem;
    }

    //메뉴에서 select 할 때 화살표 입력 방향(값)에 따라 선택되는 메뉴가 바뀌는 함수
    private void navigateMenu(int direction) {
        selectedIndex = (selectedIndex + direction + menuItems.length) % menuItems.length;
        updateMenu();
    }
    
    //바뀌면 UI에서 Text 색이 빨강으로 바뀜
    private void updateMenu() {
        for (int i = 0; i < menuItems.length; i++) {
            if (i == selectedIndex) {
                menuItems[i].setFill(Color.RED);
            } 
            else {
                menuItems[i].setFill(Color.BLACK);
            }
        }
    }
    
    //enter를 눌렀을 때 실행되는 함수
    private void executeSelectedMenuItem() {
        String selectedItem = menuItems[selectedIndex].getText();
        System.out.println("Selected menu item: " + selectedItem);
        if (selectedItem.equals("Standard Game")) {
		board=new Board(0);
        	primaryStage.setScene(board.createScene(primaryStage));
        	//화면 중앙배치
        	centerStage(primaryStage);
        }
        else if(selectedItem.equals("Item Mode Game")) {
        	board=new Board(1);
        	primaryStage.setScene(board.createScene(primaryStage));
        	//화면 중앙배치
        	centerStage(primaryStage);
        }
        else if (selectedItem.equals("Setting")) {
        	new SettingMenu().display(primaryStage, scene);//설정화면 표시
        	//화면 중앙배치
        	centerStage(primaryStage);
        } 
        else if (selectedItem.equals("Score Board")) {
            primaryStage.setScene(ScoreBoard.createScene(primaryStage));
        } 
        else if (selectedItem.equals("Exit")) {
            System.exit(0);
        }
    }
    
    //사용하는 키 가져오는 것
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
                    case "colorBlindMode":
                    	colorBlindMode = Integer.parseInt(value);
                    	break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 읽기 실패 시, 적절한 예외 처리나 사용자 알림이 필요할 수 있습니다.
        }
    }
    
    private void centerStage(Stage stage) {
	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); // 화면의 크기를 얻음
	    
	    // 스테이지의 크기를 고려하여 중앙에 배치
	    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2 + screenBounds.getMinX());
	    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2 + screenBounds.getMinY());
	}
}
