// SettingMenu.java 파일 내부
package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SettingMenu {
    private Stage primaryStage;
    private Scene previousScene;

    public SettingMenu(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }
    //Setting Menu 가 다른 파일에서 가져와야 하는것 : 블록 색깔,각 조작키의 키코드, 화면의 크기,

    public void display() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/SettingMenu.fxml"));
            Parent root = loader.load();
            SettingCtrl controller = loader.getController();
            controller.setStageAndScene(primaryStage, previousScene); // 컨트롤러에 스테이지와 이전 씬 설정
            
            Scene settingsScene = new Scene(root);
            primaryStage.setScene(settingsScene);
            settingsScene.setOnKeyPressed(event -> {
            	if(event.getCode() == KeyCode.ENTER) {
            		if(settingsScene.getFocusOwner() instanceof Button) {
            			Button focusedButton = (Button)settingsScene.getFocusOwner();
            			focusedButton.fire();
            		}
            	}
            });
            
        } catch (IOException e) {
            e.printStackTrace(); // 예외를 콘솔에 출력
            // 필요한 경우 여기에서 추가적인 예외 처리를 수행할 수 있습니다.
        }
    }
}
