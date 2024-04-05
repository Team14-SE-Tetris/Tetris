package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tetris.Board;

public class StartMenu extends Application {
    public static int XSIZE = 400;	 
    public static int YSIZE = 600;
    public static Scene scene;
    public static Text[] menuItems = new Text[4];
    private int selectedIndex = 0;
    private static Stage primaryStage;
    
    Text menuTitle = new Text("Tetris by 14 Team");
    VBox menuTitleBox;
    VBox menuItemsBox;
    VBox menuBox;
    Board board;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Tetris");

        menuTitle = new Text("Tetris by 14 Team");
        menuTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 30));

        menuItems[0] = createMenuItem("Game Start"); //메뉴 종류
        menuItems[1] = createMenuItem("Setting");
        menuItems[2] = createMenuItem("Score Board");
        menuItems[3] = createMenuItem("Exit");

        menuTitleBox = new VBox(menuTitle);
        menuTitleBox.setAlignment(Pos.CENTER);
        
        menuItemsBox = new VBox(30);
        menuItemsBox.getChildren().addAll(menuItems);
        menuItemsBox.setAlignment(Pos.CENTER);

        menuBox = new VBox(100, menuTitleBox, menuItemsBox);
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
        //scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    //화면 크기 변경 함수
    public static void changeSize(int xsize, int ysize) {
        XSIZE = xsize;
        YSIZE = ysize;
        primaryStage.setWidth(XSIZE);
        primaryStage.setHeight(YSIZE);
    }

    private Text createMenuItem(String label) {
        Text menuItem = new Text(label);
        menuItem.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        menuItem.setFill(Color.BLACK);
        if (label.equals("Game Start")) { //default는 Game Start임
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

    boolean a=true;
    //enter를 눌렀을 때 실행되는 함수
    private void executeSelectedMenuItem() {
        String selectedItem = menuItems[selectedIndex].getText();
        System.out.println("Selected menu item: " + selectedItem);
        if (selectedItem.equals("Game Start")) {
        	int score=(int) (Math.random() * 100);
        	System.out.println(score);
        	//중요!!******************************************************************************************
        	//게임이 종료되었을 때 밑에 코드를 쓰면 창이 나오면서 이름을 입력받을거임, 위의 score는 해당 판의 점수임! 지금은 random값 넣음
        	//***********************************************************************************************
            //ScoreBoard scoreBoard = new ScoreBoard();
//			ScoreBoard.showSettingDialog(score,primaryStage);
        	board=new Board();
        	primaryStage.setScene(board.createScene(primaryStage));
        }
        else if (selectedItem.equals("Setting")) {
        	/*if(a==true) {
            	changeSize(700, 1000);
            	a=false;
            }
            else {
            	changeSize(400, 800);
            	a=true;
            }*/
        	new SettingMenu(primaryStage, scene).display();//설정화면 표시
        } 
        else if (selectedItem.equals("Score Board")) {
            primaryStage.setScene(ScoreBoard.createScene(primaryStage));
        } 
        else if (selectedItem.equals("Exit")) {
            System.exit(0);
        }
    }
}
