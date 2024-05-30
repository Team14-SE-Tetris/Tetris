package setting;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;

public class settingCtrlTest {
    
    private SettingCtrl settingCtrl;
    private Stage primaryStage;
    private Scene previousScene;
    
    @Before
    public void setUp() {
        settingCtrl = new SettingCtrl();
    }
    
    @Test
    public void testCheckDuplicateKey() {
        // Given
        KeyCode existingKey = KeyCode.UP;
        KeyCode newKey = KeyCode.Q;
        
        // When
        boolean result = settingCtrl.checkDuplicateKey(newKey);
        
        // Then
        assertTrue(result); // LEFT 키가 이미 다른 기능에 할당되어 있으므로 중복됨
    }
    
}
